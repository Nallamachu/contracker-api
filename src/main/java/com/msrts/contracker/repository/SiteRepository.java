package com.msrts.contracker.repository;

import com.msrts.contracker.entity.Site;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SiteRepository extends JpaRepository<Site, Long> {

    @Query(value = "from Site s where s.contact=?1")
    List<Site> findAllSitesByContact(String contact, Pageable pageable);
}
