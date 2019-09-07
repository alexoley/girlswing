package com.example.girlswing.local.repositories;

import com.example.girlswing.local.entities.LocalMan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocalManRepository extends JpaRepository<LocalMan, Long>{
}
