package pharmacie.dao;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import pharmacie.entity.Commande;

public interface CommandeRepository extends JpaRepository<Commande, Integer> {
    /**
     * Trouve toutes les commandes après une date donnée 
     * @return une liste de commandes 
     */
    List<Commande> findByDateSaisieAfter(LocalDate dateSaisie);

    /**
     * Calucler le nombre de commandes passées par un dispensaire connu par sa clé
     * @param codeDispensaire la clé du dispensaire
     * @return le nombre de commandes
     */
    @Query ("SELECT COUNT(*) AS nombreCommandes"
        + " FROM Commande commande "
        + " WHERE commande.dispensaire.id = :codeDispensaire"
        + " AND commande.dateExpedition IS NOT NULL")
    public int nombreCommandesPourDispensaire(Integer codeDispensaire);

    

}
