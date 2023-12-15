package com.unicap.idear.idear.repositories;

import com.unicap.idear.idear.models.ProblemModel;
import com.unicap.idear.idear.models.TeamModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProblemRepository extends JpaRepository<ProblemModel, Long> {
}
