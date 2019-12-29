package carzanodev.genuniv.microservices.precord.cache.model;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DegreeDTO {

    @JsonProperty("id")
    private int id;

    @JsonProperty("code")
    private String code;

    @JsonProperty("name")
    private String name;

    @JsonProperty("college_id")
    private int collegeId;

    @JsonProperty("degree_type_id")
    private int degreeTypeId;

    @Data
    @NoArgsConstructor
    public static class List {
        @JsonProperty("degrees")
        private Set<DegreeDTO> degrees;
    }

}
