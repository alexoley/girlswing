package com.example.girlswing.external.repositories;

import com.example.girlswing.external.entities.Man;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ManRepository extends CrudRepository<Man, Long> {
}
