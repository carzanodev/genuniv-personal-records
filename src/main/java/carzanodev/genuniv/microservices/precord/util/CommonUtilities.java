package carzanodev.genuniv.microservices.precord.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class CommonUtilities {

    public static final DateTimeFormatter DEFAULT_BIRTHDATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static LocalDate toBirthDate(String date) {
        return LocalDate.parse(date, DEFAULT_BIRTHDATE_FORMATTER);
    }

}
