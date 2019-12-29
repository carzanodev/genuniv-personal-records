package carzanodev.genuniv.microservices.precord.api.staff;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import carzanodev.genuniv.microservices.common.config.CommonExceptionHandler.InvalidReferenceValueException;
import carzanodev.genuniv.microservices.common.config.CommonExceptionHandler.InvalidTargetEntityException;
import carzanodev.genuniv.microservices.common.model.dto.SourceInfo;
import carzanodev.genuniv.microservices.common.model.dto.StandardResponse;
import carzanodev.genuniv.microservices.precord.api.staff.StaffDTO.List;

@RestController
@RequestMapping("/api/v1/staff")
class StaffController {

    private final StaffService staffService;

    @Autowired
    StaffController(StaffService staffService) {
        this.staffService = staffService;
    }

    // ↓↓↓ GET ↓↓↓

    @GetMapping
    StandardResponse<List> getStaff() {
        return staffService.getAllStaffs(false);
    }

    @GetMapping(path = "{id}")
    StandardResponse<StaffDTO> getStaffById(@PathVariable("id") long id) throws InvalidTargetEntityException {
        return staffService.getStaffById(id, false);
    }

    @GetMapping(path = "info")
    StandardResponse<SourceInfo> getStaffInfo(@RequestParam(name = "last_updated", defaultValue = "") String lastUpdated) {
        return staffService.getInfo(lastUpdated);
    }

    // ↓↓↓ POST ↓↓↓

    @PostMapping
    StandardResponse<StaffDTO> postStaff(@Valid @NonNull @RequestBody StaffDTO staffDto) throws InvalidTargetEntityException, InvalidReferenceValueException {
        return staffService.addStaff(staffDto);
    }

    // ↓↓↓ PUT ↓↓↓

    @PutMapping(path = "{id}")
    StandardResponse<StaffDTO> putStaff(@PathVariable("id") long id,
                                        @Valid @NonNull @RequestBody StaffDTO staffDto) throws InvalidTargetEntityException, InvalidReferenceValueException {
        return staffService.updateStaff(id, staffDto, false);
    }

    // ↓↓↓ DELETE ↓↓↓

    @DeleteMapping(path = "{id}")
    StandardResponse<StaffDTO> deleteStaff(@PathVariable("id") long id) throws InvalidTargetEntityException {
        return staffService.deleteStaff(id);
    }

}
