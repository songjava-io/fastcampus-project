package kr.songjava.web.domain;

public enum ScheduleType {
    WORK("업무"),
    VACATION("휴가"),
    OUTSIDE_WORK("외근"),
    MEETING("회의"),
    LUNCH("점심"),
    DINING_TOGETHER("회식"),
    PARENTAL_LEAVE("육아휴직"),
    NIGHT_SHIFT("야간근무"),
    SELF_DEVLEOPMENT("자기개발");

    private final String description;

    ScheduleType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static ScheduleType of(String description) {
        for (ScheduleType type : values()) {
            if (type.getDescription().equals(description)) {
                return type;
            }
        }
        throw new IllegalArgumentException("해당하는 스케줄 타입이 없습니다.");
    }


}
