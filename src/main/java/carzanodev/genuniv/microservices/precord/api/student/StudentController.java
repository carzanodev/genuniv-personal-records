package carzanodev.genuniv.microservices.precord.api.student;

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
import carzanodev.genuniv.microservices.precord.api.student.StudentDTO.List;

@RestController
@RequestMapping("/api/v1/student")
class StudentController {

    private final StudentService studentService;

    @Autowired
    StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    // ↓↓↓ GET ↓↓↓

    @GetMapping
    StandardResponse<List> getStudent() {
        return studentService.getAllStudents(false);
    }

    @GetMapping(path = "{id}")
    StandardResponse<StudentDTO> getStudentById(@PathVariable("id") long id) throws InvalidTargetEntityException {
        return studentService.getStudentById(id, false);
    }

    @GetMapping(path = "info")
    StandardResponse<SourceInfo> getStudentInfo(@RequestParam(name = "last_updated", defaultValue = "") String lastUpdated) {
        return studentService.getInfo(lastUpdated);
    }

    // ↓↓↓ POST ↓↓↓

    @PostMapping
    StandardResponse<StudentDTO> postStudent(@Valid @NonNull @RequestBody StudentDTO studentDto) throws InvalidTargetEntityException, InvalidReferenceValueException, NonEmptyException {
        return studentService.addStudent(studentDto);
    }

    // ↓↓↓ PUT ↓↓↓

    @PutMapping(path = "{id}")
    StandardResponse<StudentDTO> putStudent(@PathVariable("id") long id,
                                            @Valid @NonNull @RequestBody StudentDTO studentDto) throws InvalidTargetEntityException, InvalidReferenceValueException, NonEmptyException {
        return studentService.updateStudent(id, studentDto, false);
    }

    // ↓↓↓ DELETE ↓↓↓

    @DeleteMapping(path = "{id}")
    StandardResponse<StudentDTO> deleteStudent(@PathVariable("id") long id) throws InvalidTargetEntityException {
        return studentService.deleteStudent(id);
    }

}
