package pharmacie.dao;


import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import jakarta.persistence.EntityManager;
import pharmacie.entity.*;


@DataJpaTest
public class ContraintesTest {
    @Autowired
    private CategorieRepository categorieRepository;
    @Autowired
    private MedicamentRepository medicamentRepository;
    @Autowired
    private CommandeRepository commandeRepository;
    @Autowired
    private DispensaireRepository dispensaireRepository;
    @Autowired
    private LigneRepository ligneRepository;

    @Autowired
    private EntityManager entityManager;


    
    @Test
    public void unMedicamentSansCategorieEstInterdit(){
        Medicament med = new Medicament();
        med.setNom("Doliprane");

        assertThrows(DataIntegrityViolationException.class, () -> {
            medicamentRepository.saveAndFlush(med);
        },"Un médicament sans catégorie ne doit pas être autorisé");
    }

    @Test
    public void uneCategorieSansMedicamentPeutEtreSupprimee(){
        Categorie cat = new Categorie();
        cat.setLibelle("TestCatSansMed");

        categorieRepository.saveAndFlush(cat);
        categorieRepository.delete(cat);
        categorieRepository.flush(); // Pour forcer l'exécution immédiate
    }    

    @Test
    public void uneCategorieAvecMedicamentNePeutEtreSupprimee(){
        Categorie cat = new Categorie();
        cat.setLibelle("TestCatAvecMed");
        categorieRepository.saveAndFlush(cat);

        Medicament med = new Medicament();
        med.setNom("MedTest");
        med.setCategorie(cat);

        medicamentRepository.saveAndFlush(med);
        entityManager.clear();

        assertThrows(DataIntegrityViolationException.class, () -> {
            categorieRepository.delete(cat);
            categorieRepository.flush(); 
        },"Une catégorie avec des médicaments ne doit pas pouvoir être supprimée");
    }

    @Test
    public void uneCommandeSupprimeeSupprimeSesLignes(){
        Dispensaire dispensaire = new Dispensaire();
        dispensaire.setNom("DispensaireTest");
        dispensaire.setRegion("RégionTest");
        dispensaireRepository.saveAndFlush(dispensaire);

        Commande cmd = new Commande();
        cmd.setDateSaisie(java.time.LocalDate.now());
        cmd.setDispensaire(dispensaire);
        commandeRepository.saveAndFlush(cmd);

        Medicament med = new Medicament();
        med.setNom("MedForCmdTest");

        Categorie cat = new Categorie();
        cat.setLibelle("CatForCmdTest");    
        categorieRepository.saveAndFlush(cat);

        med.setCategorie(cat);
        medicamentRepository.saveAndFlush(med);

        Ligne ligne = new Ligne();
        ligne.setMedicament(med);
        ligne.setCommande(cmd);
        ligne.setQuantite(5);  
        ligneRepository.saveAndFlush(ligne);
        
        
        cmd.getLignes().add(ligne);

        commandeRepository.saveAndFlush(cmd);
        entityManager.clear();

        commandeRepository.delete(cmd);
        commandeRepository.flush();

        Ligne foundLigne = entityManager.find(Ligne.class, ligne.getId());
        assert(foundLigne == null);
    }


    @Test
    public void unDispensaireSupprimeSesCommandes(){
        Dispensaire dispensaire = new Dispensaire();
        dispensaire.setNom("DispensaireWithCmds");
        dispensaire.setRegion("RegionWithCmds");
        dispensaireRepository.saveAndFlush(dispensaire);

        Commande cmd1 = new Commande();
        cmd1.setDateSaisie(java.time.LocalDate.now());
        cmd1.setDispensaire(dispensaire);
        commandeRepository.saveAndFlush(cmd1);

        Commande cmd2 = new Commande();
        cmd2.setDateSaisie(java.time.LocalDate.now());
        cmd2.setDispensaire(dispensaire);
        commandeRepository.saveAndFlush(cmd2);

        entityManager.clear();

        dispensaireRepository.delete(dispensaire);
        dispensaireRepository.flush();

        Commande foundCmd1 = entityManager.find(Commande.class, cmd1.getNumero());
        Commande foundCmd2 = entityManager.find(Commande.class, cmd2.getNumero());
        assert(foundCmd1 == null);
        assert(foundCmd2 == null);
    }
}

