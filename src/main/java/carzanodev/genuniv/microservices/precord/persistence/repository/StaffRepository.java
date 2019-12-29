package carzanodev.genuniv.microservices.precord.persistence.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import carzanodev.genuniv.microservices.common.persistence.repository.InformationRepository;
import carzanodev.genuniv.microservices.precord.persistence.entity.Staff;

@Repository
public interface StaffRepository extends JpaRepository<Staff, Long>, InformationRepository {

    @Query("SELECT s FROM Staff s WHERE s.isActive = TRUE")
    List<Staff> findAllActive();

    @Query("SELECT s FROM Staff s WHERE s.id = :id AND s.isActive = TRUE")
    Optional<Staff> findActiveById(long id);

}
