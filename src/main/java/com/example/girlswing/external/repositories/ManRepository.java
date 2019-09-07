package com.example.girlswing.external.repositories;

import com.example.girlswing.external.entities.Man;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ManRepository extends JpaRepository<Man, Long> {
}
