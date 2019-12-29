package carzanodev.genuniv.microservices.precord.api.student;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import carzanodev.genuniv.microservices.common.api.service.InformationService;
import carzanodev.genuniv.microservices.common.config.CommonExceptionHandler.InvalidReferenceValueException;
import carzanodev.genuniv.microservices.common.config.CommonExceptionHandler.InvalidTargetEntityException;
import carzanodev.genuniv.microservices.common.config.CommonExceptionHandler.NonEmptyException;
import carzanodev.genuniv.microservices.common.model.dto.ResponseMeta;
import carzanodev.genuniv.microservices.common.model.dto.StandardResponse;
import carzanodev.genuniv.microservices.common.persistence.repository.InformationRepository;
import carzanodev.genuniv.microservices.precord.cache.model.DegreeDTO;
import carzanodev.genuniv.microservices.precord.persistence.entity.Student;
import carzanodev.genuniv.microservices.precord.persistence.repository.StudentRepository;
import carzanodev.genuniv.microservices.precord.util.CommonUtilities;

import static carzanodev.genuniv.microservices.common.util.MetaMessage.CREATE_MSG;
import static carzanodev.genuniv.microservices.common.util.MetaMessage.DELETE_MSG;
import static carzanodev.genuniv.microservices.common.util.MetaMessage.LIST_MSG;
import static carzanodev.genuniv.microservices.common.util.MetaMessage.RETRIEVE_MSG;
import static carzanodev.genuniv.microservices.common.util.MetaMessage.UPDATE_MSG;
import static carzanodev.genuniv.microservices.precord.cache.GeneralCacheContext.DEGREE;

@Service
class StudentService extends InformationService {

    private final StudentRepository studentRepo;

    @Autowired
    StudentService(StudentRepository studentRepo, RestTemplate restTemplate) {
        this.studentRepo = studentRepo;
    }

    @Override
    protected InformationRepository getInfoRepo() {
        return studentRepo;
    }

    StandardResponse<StudentDTO.List> getAllStudents(boolean isActiveOnly) {
        List<StudentDTO> studentDtos = (isActiveOnly ? studentRepo.findAllActive() : studentRepo.findAll())
                .stream()
                .map(this::entityToDto)
                .collect(Collectors.toList());

        StudentDTO.List response = new StudentDTO.List(studentDtos);
        ResponseMeta meta = ResponseMeta.createBasicMeta(LIST_MSG.make(studentDtos.size()));

        return new StandardResponse<>(response, meta);
    }

    StandardResponse<StudentDTO> getStudentById(long id, boolean isActiveOnly) throws InvalidTargetEntityException {
        Optional<Student> student = isActiveOnly ? studentRepo.findActiveById(id) : studentRepo.findById(id);

        if (student.isEmpty()) {
            throw new InvalidTargetEntityException("student", String.valueOf(id));
        }

        StudentDTO response = entityToDto(student.get());
        ResponseMeta meta = ResponseMeta.createBasicMeta(RETRIEVE_MSG.make(id));

        return new StandardResponse<>(response, meta);
    }

    StandardResponse<StudentDTO> addStudent(StudentDTO studentDto) throws InvalidTargetEntityException, InvalidReferenceValueException, NonEmptyException {
        Student saved = studentRepo.save(dtoToNewEntity(studentDto));

        StudentDTO response = entityToDto(saved);
        ResponseMeta meta = ResponseMeta.createBasicMeta(CREATE_MSG.make(saved.getId()));

        return new StandardResponse<>(response, meta);
    }

    StandardResponse<StudentDTO> updateStudent(long id, StudentDTO studentDto, boolean isActiveOnly) throws InvalidTargetEntityException, InvalidReferenceValueException, NonEmptyException {
        Student saved = studentRepo.save(dtoToUpdatedEntity(id, studentDto, isActiveOnly));

        StudentDTO response = entityToDto(saved);
        ResponseMeta meta = ResponseMeta.createBasicMeta(UPDATE_MSG.make(saved.getId()));

        return new StandardResponse<>(response, meta);
    }

    StandardResponse<StudentDTO> deleteStudent(long id) throws InvalidTargetEntityException {
        Optional<Student> studentFromDb = studentRepo.findById(id);

        if (studentFromDb.isEmpty()) {
            throw new InvalidTargetEntityException("student", String.valueOf(id));
        }

        Student student = studentFromDb.get();
        studentRepo.delete(student);

        StudentDTO response = entityToDto(student);
        ResponseMeta meta = ResponseMeta.createBasicMeta(DELETE_MSG.make(response.getId()));

        return new StandardResponse<>(response, meta);
    }

    private StudentDTO entityToDto(Student entity) {
        return new StudentDTO(
                entity.getId(),
                entity.getFirstName(),
                entity.getMiddleName(),
                entity.getLastName(),
                entity.getBirthdate().toString(),
                entity.getAddress(),
                entity.getDegreeId());
    }

    private Student dtoToNewEntity(StudentDTO studentDto) throws InvalidTargetEntityException, InvalidReferenceValueException, NonEmptyException {
        return dtoToUpdatedEntity(0L, studentDto, false);
    }

    private Student dtoToUpdatedEntity(long id, StudentDTO studentDto, boolean isActiveOnly) throws InvalidTargetEntityException, InvalidReferenceValueException, NonEmptyException {
        Optional<Student> s;
        if (id == 0) {
            s = Optional.of(new Student());
        } else {
            s = isActiveOnly ? studentRepo.findActiveById(id) : studentRepo.findById(id);
        }

        if (s.isEmpty()) {
            throw new InvalidTargetEntityException("student", String.valueOf(id));
        }

        Student student = s.get();

        String firstName = studentDto.getFirstName();
        if (!StringUtils.isEmpty(firstName)) {
            student.setFirstName(firstName);
        }

        String middleName = studentDto.getMiddleName();
        if (!StringUtils.isEmpty(middleName)) {
            student.setMiddleName(middleName);
        }

        String lastName = studentDto.getLastName();
        if (!StringUtils.isEmpty(lastName)) {
            student.setLastName(lastName);
        }

        String birthDateStr = studentDto.getBirthDate();
        if (!StringUtils.isEmpty(birthDateStr)) {
            LocalDate birthDate = CommonUtilities.toBirthDate(birthDateStr);
            student.setBirthdate(birthDate);
        }

        String address = studentDto.getAddress();
        if (!StringUtils.isEmpty(address)) {
            student.setAddress(address);
        }

        int degreeId = studentDto.getDegreeId();
        if (degreeId > 0) {
            Optional<DegreeDTO> degree = Optional.ofNullable(DEGREE.get(degreeId));

            if (degree.isEmpty()) {
                throw new InvalidReferenceValueException("degree_id", String.valueOf(degreeId));
            }

            student.setDegreeId(degreeId);
        } else {
            throw new NonEmptyException("degree_id");
        }

        return student;
    }

}