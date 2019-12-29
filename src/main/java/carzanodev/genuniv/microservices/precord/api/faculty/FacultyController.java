package carzanodev.genuniv.microservices.precord.api.faculty;

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
import carzanodev.genuniv.microservices.common.config.CommonExceptionHandler.NonEmptyException;
import carzanodev.genuniv.microservices.common.model.dto.SourceInfo;
import carzanodev.genuniv.microservices.common.model.dto.StandardResponse;
import carzanodev.genuniv.microservices.precord.api.faculty.FacultyDTO.List;

@RestController
@RequestMapping("/api/v1/faculty")
class FacultyController {

    private final FacultyService facultyService;

    @Autowired
    FacultyController(FacultyService facultyService) {
        this.facultyService = facultyService;
    }

    // ↓↓↓ GET ↓↓↓

    @GetMapping
    StandardResponse<List> getFaculty() {
        return facultyService.getAllFaculties(false);
    }

    @GetMapping(path = "{id}")
    StandardResponse<FacultyDTO> getFacultyById(@PathVariable("id") long id) throws InvalidTargetEntityException {
        return facultyService.getFacultyById(id, false);
    }

    @GetMapping(path = "info")
    StandardResponse<SourceInfo> getFacultyInfo(@RequestParam(name = "last_updated", defaultValue = "") String lastUpdated) {
        return facultyService.getInfo(lastUpdated);
    }

    // ↓↓↓ POST ↓↓↓

    @PostMapping
    StandardResponse<FacultyDTO> postFaculty(@Valid @NonNull @RequestBody FacultyDTO facultyDto) throws InvalidTargetEntityException, InvalidReferenceValueException, NonEmptyException {
        return facultyService.addFaculty(facultyDto);
    }

    // ↓↓↓ PUT ↓↓↓

    @PutMapping(path = "{id}")
    StandardResponse<FacultyDTO> putFaculty(@PathVariable("id") long id,
                                            @Valid @NonNull @RequestBody FacultyDTO facultyDto) throws InvalidTargetEntityException, InvalidReferenceValueException, NonEmptyException {
        return facultyService.updateFaculty(id, facultyDto, false);
    }

    // ↓↓↓ DELETE ↓↓↓

    @DeleteMapping(path = "{id}")
    StandardResponse<FacultyDTO> deleteFaculty(@PathVariable("id") long id) throws InvalidTargetEntityException {
        return facultyService.deleteFaculty(id);
    }

}
