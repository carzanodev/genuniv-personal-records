package carzanodev.genuniv.microservices.precord.api.staff;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import carzanodev.genuniv.microservices.common.api.service.InformationService;
import carzanodev.genuniv.microservices.common.config.CommonExceptionHandler.InvalidTargetEntityException;
import carzanodev.genuniv.microservices.common.model.dto.ResponseMeta;
import carzanodev.genuniv.microservices.common.model.dto.StandardResponse;
import carzanodev.genuniv.microservices.common.persistence.repository.InformationRepository;
import carzanodev.genuniv.microservices.precord.persistence.entity.Staff;
import carzanodev.genuniv.microservices.precord.persistence.repository.StaffRepository;
import carzanodev.genuniv.microservices.precord.util.CommonUtilities;

import static carzanodev.genuniv.microservices.common.util.MetaMessage.CREATE_MSG;
import static carzanodev.genuniv.microservices.common.util.MetaMessage.DELETE_MSG;
import static carzanodev.genuniv.microservices.common.util.MetaMessage.LIST_MSG;
import static carzanodev.genuniv.microservices.common.util.MetaMessage.RETRIEVE_MSG;
import static carzanodev.genuniv.microservices.common.util.MetaMessage.UPDATE_MSG;

@Service
class StaffService extends InformationService {

    private final StaffRepository staffRepo;

    @Autowired
    StaffService(StaffRepository staffRepo) {
        this.staffRepo = staffRepo;
    }

    @Override
    protected InformationRepository getInfoRepo() {
        return staffRepo;
    }

    StandardResponse<StaffDTO.List> getAllStaffs(boolean isActiveOnly) {
        List<StaffDTO> staffDtos = (isActiveOnly ? staffRepo.findAllActive() : staffRepo.findAll())
                .stream()
                .map(this::entityToDto)
                .collect(Collectors.toList());

        StaffDTO.List response = new StaffDTO.List(staffDtos);
        ResponseMeta meta = ResponseMeta.createBasicMeta(LIST_MSG.make(staffDtos.size()));

        return new StandardResponse<>(response, meta);
    }

    StandardResponse<StaffDTO> getStaffById(long id, boolean isActiveOnly) throws InvalidTargetEntityException {
        Optional<Staff> staff = isActiveOnly ? staffRepo.findActiveById(id) : staffRepo.findById(id);

        if (staff.isEmpty()) {
            throw new InvalidTargetEntityException("staff", String.valueOf(id));
        }

        StaffDTO response = entityToDto(staff.get());
        ResponseMeta meta = ResponseMeta.createBasicMeta(RETRIEVE_MSG.make(id));

        return new StandardResponse<>(response, meta);
    }

    StandardResponse<StaffDTO> addStaff(StaffDTO staffDto) throws InvalidTargetEntityException {
        Staff saved = staffRepo.save(dtoToNewEntity(staffDto));

        StaffDTO response = entityToDto(saved);
        ResponseMeta meta = ResponseMeta.createBasicMeta(CREATE_MSG.make(saved.getId()));

        return new StandardResponse<>(response, meta);
    }

    StandardResponse<StaffDTO> updateStaff(long id, StaffDTO staffDto, boolean isActiveOnly) throws InvalidTargetEntityException {
        Staff saved = staffRepo.save(dtoToUpdatedEntity(id, staffDto, isActiveOnly));

        StaffDTO response = entityToDto(saved);
        ResponseMeta meta = ResponseMeta.createBasicMeta(UPDATE_MSG.make(saved.getId()));

        return new StandardResponse<>(response, meta);
    }

    StandardResponse<StaffDTO> deleteStaff(long id) throws InvalidTargetEntityException {
        Optional<Staff> staffFromDb = staffRepo.findById(id);

        if (staffFromDb.isEmpty()) {
            throw new InvalidTargetEntityException("staff", String.valueOf(id));
        }

        Staff staff = staffFromDb.get();
        staffRepo.delete(staff);

        StaffDTO response = entityToDto(staff);
        ResponseMeta meta = ResponseMeta.createBasicMeta(DELETE_MSG.make(response.getId()));

        return new StandardResponse<>(response, meta);
    }

    private StaffDTO entityToDto(Staff entity) {
        return new StaffDTO(
                entity.getId(),
                entity.getFirstName(),
                entity.getMiddleName(),
                entity.getLastName(),
                entity.getBirthdate().toString(),
                entity.getAddress());
    }

    private Staff dtoToNewEntity(StaffDTO staffDto) throws InvalidTargetEntityException {
        return dtoToUpdatedEntity(0L, staffDto, false);
    }

    private Staff dtoToUpdatedEntity(long id, StaffDTO staffDto, boolean isActiveOnly) throws InvalidTargetEntityException {
        Optional<Staff> s;
        if (id == 0) {
            s = Optional.of(new Staff());
        } else {
            s = isActiveOnly ? staffRepo.findActiveById(id) : staffRepo.findById(id);
        }

        if (s.isEmpty()) {
            throw new InvalidTargetEntityException("staff", String.valueOf(id));
        }

        Staff staff = s.get();

        String firstName = staffDto.getFirstName();
        if (!StringUtils.isEmpty(firstName)) {
            staff.setFirstName(firstName);
        }

        String middleName = staffDto.getMiddleName();
        if (!StringUtils.isEmpty(middleName)) {
            staff.setMiddleName(middleName);
        }

        String lastName = staffDto.getLastName();
        if (!StringUtils.isEmpty(lastName)) {
            staff.setLastName(lastName);
        }

        String birthDateStr = staffDto.getBirthDate();
        if (!StringUtils.isEmpty(birthDateStr)) {
            LocalDate birthDate = CommonUtilities.toBirthDate(birthDateStr);
            staff.setBirthdate(birthDate);
        }

        String address = staffDto.getAddress();
        if (!StringUtils.isEmpty(address)) {
            staff.setAddress(address);
        }

        return staff;
    }

}