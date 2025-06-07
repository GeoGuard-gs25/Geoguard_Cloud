package global.geoguard.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "energy_bill")
public class EnergyBill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User owner;

    @NotNull(message = "O consumo em kWh não pode ser nulo.")
    @Positive(message = "O consumo em kWh deve ser positivo.")
    private Double consumoKwh;

    @NotNull(message = "O valor do kWh não pode ser nulo.")
    @PositiveOrZero(message = "O valor do kWh deve ser zero ou positivo.")
    private Double valorKwh;

    @NotNull(message = "O valor total da conta (amount) não pode ser nulo.")
    @PositiveOrZero(message = "O valor total da conta (amount) deve ser zero ou positivo.")
    private Double amount;

    @NotNull(message = "O mês de referência (month) não pode ser nulo.")
    private String month;

}
