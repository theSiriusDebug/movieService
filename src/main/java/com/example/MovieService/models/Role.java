package com.example.MovieService.models;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import javax.persistence.*;

@Entity
@Table(name = "roles")
@Data
@NoArgsConstructor
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "reviewText must not be blank")
    @Min(value = 1)
    private String name;

    public Role(String name) {
        super();
        this.name = name;
    }
}
