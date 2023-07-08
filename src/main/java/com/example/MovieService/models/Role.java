package com.example.MovieService.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;

@Entity
@Table(name = "roles")
@Getter
@Setter
@NoArgsConstructor
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;

    public Role(String name) {
        super();
        this.name = name;
    }
}
