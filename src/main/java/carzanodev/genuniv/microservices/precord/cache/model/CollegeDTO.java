package carzanodev.genuniv.microservices.precord.cache.model;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CollegeDTO {

    @JsonProperty("id")
    private int id;

    @JsonProperty("name")
    private String name;

    @Data
    @NoArgsConstructor
    public static class List {
        @JsonProperty("colleges")
        private Set<CollegeDTO> colleges;
    }

}
