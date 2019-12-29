package carzanodev.genuniv.microservices.precord.persistence.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import carzanodev.genuniv.microservices.common.persistence.repository.InformationRepository;
import carzanodev.genuniv.microservices.precord.persistence.entity.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long>, InformationRepository {

    @Query("SELECT s FROM Student s WHERE s.isActive = TRUE")
    List<Student> findAllActive();

    @Query("SELECT s FROM Student s WHERE s.id = :id AND s.isActive = TRUE")
    Optional<Student> findActiveById(long id);

}
