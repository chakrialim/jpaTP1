package pharmacie.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import pharmacie.entity.*;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class RepositoryCustomMethodsTest {

    @Autowired
    private CategorieRepository categorieRepository;
    @Autowired
    private MedicamentRepository medicamentRepository;
    @Autowired
    private DispensaireRepository dispensaireRepository;
    @Autowired
    private CommandeRepository commandeRepository;


    @Test // Ce test se base uniquement sur les données définies dans data.sql
    public void testMedicamentCustomMethods() {    
        Medicament indisponible = medicamentRepository.findByNom("Lévofloxacine 500mg").orElseThrow();
        Medicament disponible   = medicamentRepository.findByNom("Doliprane Effervescent 1g").orElseThrow();
    
        // Trouve tous les médicaments disponibles
        List<Medicament> disponibles = medicamentRepository.findByIndisponibleFalse();

        assertTrue(disponibles.contains(disponible));
        assertFalse(disponibles.contains(indisponible));        
        assertFalse(disponibles.isEmpty());
    }

    @Test // Ce test crée les enregistrements nécessaires
    public void testCategorieCustomMethods() {
        Categorie c1 = new Categorie();
        c1.setLibelle("AnalgesiquesTest");
        categorieRepository.save(c1);

        Categorie c2 = new Categorie();
        c2.setLibelle("AntibiotiquesTest");
        categorieRepository.save(c2);

        // findByLibelle
        Categorie found = categorieRepository.findByLibelle("AnalgesiquesTest");
        assertNotNull(found);
        assertEquals("AnalgesiquesTest", found.getLibelle());

        // findByLibelleContaining
        List<Categorie> list = categorieRepository.findByLibelleContaining("iquesTest");
        assertEquals(2, list.size());
        assertTrue(list.stream().anyMatch(cat -> cat.getLibelle().equals("AntibiotiquesTest")));
        assertTrue(list.stream().anyMatch(cat -> cat.getLibelle().equals("AnalgesiquesTest")));
    }

    @Test // Ce test se base uniquement sur les données définies dans data.sql
    public void testDispensaireCustomMethods() {
        // Trouve toutes les dispensaires dans une région donnée
        List<Dispensaire> dispensaires = dispensaireRepository.findByRegion("Île-de-France");

        assertFalse(dispensaires.isEmpty());
        for (Dispensaire d : dispensaires) {
            assertEquals("Île-de-France", d.getRegion());
        }
    }

  @Test // Ce test se base uniquement sur les données définies dans data.sql
    public void testCommandeCustomMethods() {
        LocalDate dateSeuil = LocalDate.of(2025, 1, 1);

        // Trouve toutes les commandes après une date donnée
        List<Commande> commandes = commandeRepository.findByDateSaisieAfter(dateSeuil);

        assertFalse(commandes.isEmpty());
        for (Commande c : commandes) {
            assertTrue(c.getDateSaisie().isAfter(dateSeuil));
        }
    }

}
