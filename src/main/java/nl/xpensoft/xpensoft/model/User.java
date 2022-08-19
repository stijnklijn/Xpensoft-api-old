package nl.xpensoft.xpensoft.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User {

    @JsonIgnore //Protects against POST request with existing id and thus against attempts to change existing user
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank(message = "Vul een geldig e-mailadres in")
    @Email(message = "Vul een geldig e-mailadres in")
    @Size(max = 64, message = "E-mail kan niet meer dan 64 karakters bevatten")
    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) //Ensures hashed password is not sent back when registering new user
    @Size(min = 8, max = 64, message = "Wachtwoord moet tussen 8 en 64 karakters bevatten")
    private String password;
}
