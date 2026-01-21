package pharmacie.dao;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import pharmacie.entity.Commande;

public interface CommandeRepository extends JpaRepository<Commande, Integer> {
    /**
     * Trouve toutes les commandes après une date donnée 
     * @return une liste de commandes 
     */
    List<Commande> findByDateSaisieAfter(LocalDate dateSaisie);

}
