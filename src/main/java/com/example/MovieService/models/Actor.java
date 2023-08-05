//package com.example.MovieService.models;
//
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//import javax.persistence.*;
//import java.util.List;
//
//@Entity
//@Table(name = "actors")
//@Data
//@NoArgsConstructor
//public class Actor {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private long id;
//    private String name;
//    private String nationality;
//    @ManyToMany(mappedBy = "actors")
//    private List<Movie> movies;
//}
