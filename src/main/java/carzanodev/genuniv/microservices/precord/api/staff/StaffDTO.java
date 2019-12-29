package carzanodev.genuniv.microservices.precord.api.staff;

import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
class StaffDTO {

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

    @Data
    @AllArgsConstructor
    static class List {
        @JsonProperty("staffs")
        private Collection<StaffDTO> staffs;
    }

}
