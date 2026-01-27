package pharmacie.dao;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import pharmacie.entity.Medicament;
import pharmacie.entity.UnitesParMedicament;

// Cette interface sera auto-implémentée par Spring
public interface MedicamentRepository extends JpaRepository<Medicament, Integer> {
    /**
     * Trouve un médicament à partir de son nom (unique dans Medicament)
     * @return un médicament "optionnel"
     */
    Optional<Medicament>findByNom(String nom);

    /**
     * Trouve les médicaments disponibles (indisponible = false)
     * @return la liste des médicaments disponibles
     */
    List<Medicament> findByIndisponibleFalse();

    /**
     * Calcul le nombre d'unités commandées pour chaque produit d'une catégorie
     * @param codeCategorie la catégorie à traiter
     * @return le nomber d'unités commandées pour chaque produit, 
     * sous la forme d'une liste de projections UnitesParProduit
     */
    @Query("SELECT ligne.medicament.nom AS nom, SUM(ligne.quantite) AS unites"
        + " FROM Ligne ligne "
        + " WHERE ligne.medicament.categorie.code = :codeCategorie"
        + " GROUP BY nom")
    public List<UnitesParMedicament> medicamentsVendusPour(Integer codeCategorie);

    /*Trouver tous les médicaments disponible à la commande pour une catégorie 
    connue pas sa clé (Un médicament est disponible à la commande si il n'est pas 
    indisponible et si sa quantité en stock unitesEnStock est supérieure ou égale à sa 
    quantité en commande unitesCommandees) */

    @Query ("SELECT med FROM Medicament med"
        + " WHERE med.categorie.code = :codeCategorie"
        + " AND med.indisponible = false"
        + " AND med.unitesEnStock >= med.unitesCommandees"
    )
    public List<Medicament> medicamentsDisponiblesPourCategorie (Integer codeCategorie);
}
