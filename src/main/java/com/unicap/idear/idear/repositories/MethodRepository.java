package com.unicap.idear.idear.repositories;

import com.unicap.idear.idear.models.MethodModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MethodRepository extends JpaRepository<MethodModel, Long> {
}
