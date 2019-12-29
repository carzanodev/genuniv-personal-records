package carzanodev.genuniv.microservices.precord.api.student;

import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
class StudentDTO {

    @JsonProperty("id")
    private long id;

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("middle_name")
    private String middleName;

    @JsonProperty("last_name")
    private String lastName;

    @JsonProperty("birthdate")
    private String birthDate;

    @JsonProperty("address")
    private String address;

    @JsonProperty("degree_id")
    private int degreeId;

    @Data
    @AllArgsConstructor
    static class List {
        @JsonProperty("students")
        private Collection<StudentDTO> students;
    }

}
