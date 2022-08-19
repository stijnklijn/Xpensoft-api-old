package nl.xpensoft.xpensoft.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull(message = "Vul een datum in")
    private LocalDate date;

    @NotBlank(message = "Vul een omschrijving in")
    @Size(max = 128, message = "Omschrijving kan maximaal 128 karakters bevatten")
    private String description;

    @NotNull(message = "Geef een bestaande rubriek op om de transactie aan toe te voegen")
    @ManyToOne
    @JoinColumn(name = "header_id", referencedColumnName = "id", nullable = false)
    private Header header;

    @NotNull(message = "Vul een bedrag in")
    @DecimalMin(value = "0.01", message = "Bedrag moet minimaal 0.01 zijn")
    @DecimalMax(value = "999999.99", message = "Bedrag kan maximaal 999999.99 zijn")
    private double amount;
}
