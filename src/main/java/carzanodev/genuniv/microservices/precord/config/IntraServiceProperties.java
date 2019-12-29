package carzanodev.genuniv.microservices.precord.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import carzanodev.genuniv.microservices.common.config.ClientLenientErrorHandler;

@AllArgsConstructor
@Getter
@ConstructorBinding
@ConfigurationProperties(prefix = "intra.service")
public class IntraServiceProperties {

    public final College college;

    @AllArgsConstructor
    @Getter
    @ConstructorBinding
    @ToString
    public static final class College {
        private final String protocol;
        private final String serviceName;
        private final CollegeApi collegeApi;
        private final DegreeApi degreeApi;
        private final DegreeTypeApi degreeTypeApi;
        private final CourseApi courseApi;

        public String getCollegeApiUrl() {
            return protocol + "://" + serviceName + collegeApi.getApiPrefix();
        }

        public String getDegreeApiUrl() {
            return protocol + "://" + serviceName + degreeApi.getApiPrefix();
        }

        public String getDegreeTypeApiUrl() {
            return protocol + "://" + serviceName + degreeTypeApi.getApiPrefix();
        }

        public String getCourseApiUrl() {
            return protocol + "://" + serviceName + courseApi.getApiPrefix();
        }

        @ConstructorBinding
        public static final class CollegeApi extends Api {
            public CollegeApi(String apiPrefix) {
                super(apiPrefix);
            }
        }

        @ConstructorBinding
        public static final class DegreeApi extends Api {
            public DegreeApi(String apiPrefix) {
                super(apiPrefix);
            }
        }

        @ConstructorBinding
        public static final class DegreeTypeApi extends Api {
            public DegreeTypeApi(String apiPrefix) {
                super(apiPrefix);
            }
        }

        @ConstructorBinding
        public static final class CourseApi extends Api {
            public CourseApi(String apiPrefix) {
                super(apiPrefix);
            }
        }

    }

    @AllArgsConstructor
    @Getter
    public static abstract class Api {
        private final String apiPrefix;
    }

}
