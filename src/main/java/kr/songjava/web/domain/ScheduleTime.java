package kr.songjava.web.domain;

public enum ScheduleTime {
    T00("0", "00"),
    T01("1", "01"),
    T02("2", "02"),
    T03("3", "03"),
    T04("4", "04"),
    T05("5", "05"),
    T06("6", "06"),
    T07("7", "07"),
    T08("8", "08"),
    T09("9", "09"),
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
    T23("23", "23");

    private final String time;
    private final String code;

    ScheduleTime(String time, String code) {
        this.time = time;
        this.code = code;
    }

    public String getTime() {
        return time;
    }

    public String getCode() {
        return code;
    }

    public static ScheduleTime of(String time) {
        for (ScheduleTime type : values()) {
            if (type.getTime().equals(time)) {
                return type;
            }
        }
        throw new IllegalArgumentException("해당하는 스케줄 타입이 없습니다.");
    }
}
