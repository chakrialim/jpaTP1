package pharmacie.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;

@Entity
@Getter @Setter @NoArgsConstructor @RequiredArgsConstructor @ToString
public class Commande {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer numero;

    @NonNull @NotNull
    private LocalDate dateSaisie;

    private LocalDate dateExpedition;

    @ManyToOne(optional = false)
    @NonNull
    private Dispensaire dispensaire;

    @OneToMany(mappedBy = "commande", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<Ligne> lignes =  new ArrayList<>();
}