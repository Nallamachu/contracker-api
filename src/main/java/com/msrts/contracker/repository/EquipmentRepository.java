package com.msrts.contracker.repository;

import com.msrts.contracker.entity.Equipment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EquipmentRepository extends JpaRepository<Equipment, Long> {

    @Query(value = "from Equipment r where r.site=?1")
    List<Equipment> findAllEquipmentsBySiteId(Long siteId, Pageable pageable);

}
