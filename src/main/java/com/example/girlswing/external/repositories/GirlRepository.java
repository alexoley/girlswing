package com.example.girlswing.external.repositories;

import com.example.girlswing.external.entities.Girl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GirlRepository extends JpaRepository<Girl, Long> {
}

