package pharmacie.dao;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import pharmacie.entity.*;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
public class RequetesTest {
    @Autowired
    private MedicamentRepository medicamentRepository;
    
    @Autowired
    private CommandeRepository commandeRepository;

    @Test
    public void testMedicamentVendusPourCategorie(){
        List<UnitesParMedicament> result = medicamentRepository.medicamentsVendusPour(1); // Supposons que 1 est le code d'une catégorie existante
        List<String> noms = List.of("Morphine 10mg", "Doliprane Effervescent 1g", "Efferalgan Vitamine C ");
        for (int i = 0; i < result.size(); i++) {
            assertTrue(noms.contains(result.get(i).getNom()));
        }
    }

    @Test
    public void testCalculerNombreCommandesPourDispensaire(){
        int nombreCommandes = commandeRepository.nombreCommandesPourDispensaire(1); // Supposons que 1 est le code d'un dispensaire existant
        assertEquals(0, nombreCommandes);
    }

    

}
