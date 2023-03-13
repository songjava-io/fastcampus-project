package kr.songjava.web.domain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import kr.songjava.web.configuration.serializer.ScheduleTimeSerializer;

/**
 * packageName :  kr.songjava.web.domain
 * fileName : ScheduleTime
 * author :  82108
 * date : 2023-03-09
 * description :
 */
@JsonSerialize(using = ScheduleTimeSerializer.class)
public enum ScheduleTime {
    TOO("0", "00"),
    TO1("1", "01"),
    TO2("2", "02"),
    TO3("3", "03"),
    TO4("4", "04"),
    TO5("5", "05"),
    TO6("6", "06"),
    TO7("7", "07"),
    TO8("8", "08"),
    TO9("9", "09"),
    T10("10", "10"),
    T11("11", "11"),
    T12("12", "12"),
    T13("13", "13"),
    T14("14", "14"),
    T15("15", "15"),
    T16("16", "16"),
    T17("17", "17"),
    T18("18", "18"),
    T19("19", "19"),
    T20("20", "20"),
    T21("21", "21"),
    T22("22", "22"),
    T23("23", "23"),
    ;

    private String label;
    private String code;

    ScheduleTime(String label, String code) {
        this.label = label;
        this.code = code;
    }

    public String label() {
        return label;
    }
    public String code() {
        return code;
    }

}
