package com.unicap.idear.idear.repositories;

import com.unicap.idear.idear.models.RoomModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface RoomRepository extends JpaRepository<RoomModel, Long> {
    boolean existsByAccessCode(Long accessCode);

    Optional<RoomModel> findByAccessCode(Long accessCode);
}
