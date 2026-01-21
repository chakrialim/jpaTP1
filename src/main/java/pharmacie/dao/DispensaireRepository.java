package pharmacie.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import pharmacie.entity.Dispensaire;

public interface DispensaireRepository extends JpaRepository<Dispensaire, Integer> {
        /**
     * Trouve toutes les dispensaires dans une région donnée
     * @return une liste de dispensaires
     */
    List<Dispensaire>findByRegion(String region);
}
