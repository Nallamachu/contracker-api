package com.msrts.contracker.repository;

import com.msrts.contracker.entity.ReferralSequence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ReferralSequenceRepository extends JpaRepository<ReferralSequence, Long> {
    @Query(value = "select rs.sequence from ReferralSequence rs where rs.firstName=?1 and rs.lastName=?2")
    Long getSequenceByFirstAndLastName(String firstName, String lastName);
}