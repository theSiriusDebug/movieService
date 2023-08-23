package com.example.MovieService.controllers.TEST;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface filmRepository extends JpaRepository<film, Long> {
}
