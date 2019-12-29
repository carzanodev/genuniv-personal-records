package carzanodev.genuniv.microservices.precord.api.faculty;

import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@AllArgsConstructor
@ToString
class FacultyDTO {

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

    @JsonProperty("college_id")
    private int collegeId;

    @Data
    @AllArgsConstructor
    static class List {
        @JsonProperty("faculties")
        private Collection<FacultyDTO> faculties;
    }

}
