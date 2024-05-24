package com.msrts.contracker.repository;

import com.msrts.contracker.entity.Work;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkRepository extends JpaRepository<Work, Long> {
    @Query(value = "from Work w where w.equipment = ?1 and w.date between ?2 and ?3")
    List<Work> findAllWorksByEquipmentIdAndDateRange(Long equipmentId, String startDate, String endDate);

    @Query(value = "from Work w where w.equipment = ?1")
    List<Work> findAllWorksByEquipmentId(Long equipmentId, Pageable pageable);
}
