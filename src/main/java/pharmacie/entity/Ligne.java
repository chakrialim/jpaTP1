package pharmacie.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Entity
@Getter @Setter @NoArgsConstructor @RequiredArgsConstructor @ToString
public class Ligne {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Positive
    private int quantite;

    @ManyToOne(optional = false)
    @NonNull
    private Commande commande;

    @ManyToOne(optional = false)
    @NonNull
    private Medicament medicament;
}