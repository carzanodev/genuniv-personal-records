package carzanodev.genuniv.microservices.precord.persistence.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import carzanodev.genuniv.microservices.common.persistence.repository.InformationRepository;
import carzanodev.genuniv.microservices.precord.persistence.entity.Faculty;

@Repository
public interface FacultyRepository extends JpaRepository<Faculty, Long>, InformationRepository {

    @Query("SELECT f FROM Faculty f WHERE f.isActive = TRUE")
    List<Faculty> findAllActive();

    @Query("SELECT f FROM Faculty f WHERE f.id = :id AND f.isActive = TRUE")
    Optional<Faculty> findActiveById(long id);

}
