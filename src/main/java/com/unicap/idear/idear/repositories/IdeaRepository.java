package com.unicap.idear.idear.repositories;

import com.unicap.idear.idear.models.IdeaModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IdeaRepository extends JpaRepository<IdeaModel, Long> {
}
