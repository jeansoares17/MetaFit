package br.com.fiap.metafit.meta;

import br.com.fiap.metafit.user.User;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Entity
@Data
public class Meta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @NotBlank
    String title;

    @NotBlank
    String type;

    @Size(min = 10, message = "a descrição deve ter pelo menos 10 caracteres")
    String description;

    @Min(1)
    @Max(100)
    Integer score;

    @Min(0)
    @Max(100)
    Integer status;

    @ManyToOne
    User user;
}
