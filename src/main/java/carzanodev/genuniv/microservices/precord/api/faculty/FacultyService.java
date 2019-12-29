package carzanodev.genuniv.microservices.precord.api.faculty;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import carzanodev.genuniv.microservices.common.api.service.InformationService;
import carzanodev.genuniv.microservices.common.config.CommonExceptionHandler.InvalidReferenceValueException;
import carzanodev.genuniv.microservices.common.config.CommonExceptionHandler.InvalidTargetEntityException;
import carzanodev.genuniv.microservices.common.config.CommonExceptionHandler.NonEmptyException;
import carzanodev.genuniv.microservices.common.model.dto.ResponseMeta;
import carzanodev.genuniv.microservices.common.model.dto.StandardResponse;
import carzanodev.genuniv.microservices.common.persistence.repository.InformationRepository;
import carzanodev.genuniv.microservices.precord.cache.model.CollegeDTO;
import carzanodev.genuniv.microservices.precord.persistence.entity.Faculty;
import carzanodev.genuniv.microservices.precord.persistence.repository.FacultyRepository;
import carzanodev.genuniv.microservices.precord.util.CommonUtilities;

import static carzanodev.genuniv.microservices.common.util.MetaMessage.CREATE_MSG;
import static carzanodev.genuniv.microservices.common.util.MetaMessage.DELETE_MSG;
import static carzanodev.genuniv.microservices.common.util.MetaMessage.LIST_MSG;
import static carzanodev.genuniv.microservices.common.util.MetaMessage.RETRIEVE_MSG;
import static carzanodev.genuniv.microservices.common.util.MetaMessage.UPDATE_MSG;
import static carzanodev.genuniv.microservices.precord.cache.GeneralCacheContext.COLLEGE;

@Service
class FacultyService extends InformationService {

    private final FacultyRepository facultyRepo;

    @Autowired
    FacultyService(FacultyRepository facultyRepo) {
        this.facultyRepo = facultyRepo;
    }

    @Override
    protected InformationRepository getInfoRepo() {
        return facultyRepo;
    }

    StandardResponse<FacultyDTO.List> getAllFaculties(boolean isActiveOnly) {
        List<FacultyDTO> facultyDtos = (isActiveOnly ? facultyRepo.findAllActive() : facultyRepo.findAll())
                .stream()
                .map(this::entityToDto)
                .collect(Collectors.toList());

        FacultyDTO.List response = new FacultyDTO.List(facultyDtos);
        ResponseMeta meta = ResponseMeta.createBasicMeta(LIST_MSG.make(facultyDtos.size()));

        return new StandardResponse<>(response, meta);
    }

    StandardResponse<FacultyDTO> getFacultyById(long id, boolean isActiveOnly) throws InvalidTargetEntityException {
        Optional<Faculty> faculty = isActiveOnly ? facultyRepo.findActiveById(id) : facultyRepo.findById(id);

        if (faculty.isEmpty()) {
            throw new InvalidTargetEntityException("faculty", String.valueOf(id));
        }

        FacultyDTO response = entityToDto(faculty.get());
        ResponseMeta meta = ResponseMeta.createBasicMeta(RETRIEVE_MSG.make(id));

        return new StandardResponse<>(response, meta);
    }

    StandardResponse<FacultyDTO> addFaculty(FacultyDTO facultyDto) throws InvalidTargetEntityException, InvalidReferenceValueException, NonEmptyException {
        Faculty saved = facultyRepo.save(dtoToNewEntity(facultyDto));

        FacultyDTO response = entityToDto(saved);
        ResponseMeta meta = ResponseMeta.createBasicMeta(CREATE_MSG.make(saved.getId()));

        return new StandardResponse<>(response, meta);
    }

    StandardResponse<FacultyDTO> updateFaculty(long id, FacultyDTO facultyDto, boolean isActiveOnly) throws InvalidTargetEntityException, InvalidReferenceValueException, NonEmptyException {
        Faculty saved = facultyRepo.save(dtoToUpdatedEntity(id, facultyDto, isActiveOnly));

        FacultyDTO response = entityToDto(saved);
        ResponseMeta meta = ResponseMeta.createBasicMeta(UPDATE_MSG.make(saved.getId()));

        return new StandardResponse<>(response, meta);
    }

    StandardResponse<FacultyDTO> deleteFaculty(long id) throws InvalidTargetEntityException {
        Optional<Faculty> facultyFromDb = facultyRepo.findById(id);

        if (facultyFromDb.isEmpty()) {
            throw new InvalidTargetEntityException("faculty", String.valueOf(id));
        }

        Faculty faculty = facultyFromDb.get();
        facultyRepo.delete(faculty);

        FacultyDTO response = entityToDto(faculty);
        ResponseMeta meta = ResponseMeta.createBasicMeta(DELETE_MSG.make(response.getId()));

        return new StandardResponse<>(response, meta);
    }

    private FacultyDTO entityToDto(Faculty entity) {
        return new FacultyDTO(
                entity.getId(),
                entity.getFirstName(),
                entity.getMiddleName(),
                entity.getLastName(),
                entity.getBirthdate().toString(),
                entity.getAddress(),
                entity.getCollegeId());
    }

    private Faculty dtoToNewEntity(FacultyDTO facultyDto) throws InvalidTargetEntityException, InvalidReferenceValueException, NonEmptyException {
        return dtoToUpdatedEntity(0L, facultyDto, false);
    }

    private Faculty dtoToUpdatedEntity(long id, FacultyDTO facultyDto, boolean isActiveOnly) throws InvalidTargetEntityException, InvalidReferenceValueException, NonEmptyException {
        Optional<Faculty> f;
        if (id == 0) {
            f = Optional.of(new Faculty());
        } else {
            f = isActiveOnly ? facultyRepo.findActiveById(id) : facultyRepo.findById(id);
        }

        if (f.isEmpty()) {
            throw new InvalidTargetEntityException("faculty", String.valueOf(id));
        }

        Faculty faculty = f.get();

        String firstName = facultyDto.getFirstName();
        if (!StringUtils.isEmpty(firstName)) {
            faculty.setFirstName(firstName);
        } else {
            throw new NonEmptyException("first_name");
        }

        String middleName = facultyDto.getMiddleName();
        if (!StringUtils.isEmpty(middleName)) {
            faculty.setMiddleName(middleName);
        }

        String lastName = facultyDto.getLastName();
        if (!StringUtils.isEmpty(lastName)) {
            faculty.setLastName(lastName);
        } else {
            throw new NonEmptyException("last_name");
        }

        String birthDateStr = facultyDto.getBirthDate();
        if (!StringUtils.isEmpty(birthDateStr)) {
            LocalDate birthDate = CommonUtilities.toBirthDate(birthDateStr);
            faculty.setBirthdate(birthDate);
        } else {
            throw new NonEmptyException("birthdate");
        }

        String address = facultyDto.getAddress();
        if (!StringUtils.isEmpty(address)) {
            faculty.setAddress(address);
        }

        int collegeId = facultyDto.getCollegeId();
        if (collegeId > 0) {
            Optional<CollegeDTO> college = Optional.ofNullable(COLLEGE.get(collegeId));

            if (college.isEmpty()) {
                throw new InvalidReferenceValueException("college_id", String.valueOf(collegeId));
            }

            faculty.setCollegeId(collegeId);
        } else {
            throw new NonEmptyException("college_id");
        }

        return faculty;
    }

}