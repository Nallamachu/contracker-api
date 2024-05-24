package com.msrts.contracker.repository;

import com.msrts.contracker.entity.Payment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    @Query(value = "from Payment p where p.equipment=?1")
    List<Payment> findAllByEquipment(Long equipment, Pageable pageable);

    @Query(value = "select p.* from Payment p join Work t on p.equipment=t.id and t.id in (?1) and t.isActive = true and p.date between ?2 and ?3", nativeQuery = true)
    List<Payment> findAllPaymentsByEquipmentIdsAndTimePeriod(Long equipmentId, String startDate, String endDate, Pageable pageable);
}
