package com.msrts.contracker.repository;

import com.msrts.contracker.entity.Expense;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    @Query(value = "from Expense e where e.equipment = ?1")
    List<Expense> findAllAllExpensesByEquipmentId(Long equipmentId, Pageable pageable);

    @Query(value = "from Expense e where e.equipment = ?1 and e.date between ?2 and ?3")
    List<Expense> findAllExpensesByEquipmentIdAndTimePeriod(Long equipmentId, String startDate, String endDate, Pageable pageable);
}
