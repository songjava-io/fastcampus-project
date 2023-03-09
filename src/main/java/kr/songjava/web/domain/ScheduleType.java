package kr.songjava.web.domain;

public enum ScheduleType {
    WORK("업무"),
    VACATION("휴가"),
    MEETING("회의"),
    LUNCH("점심"),
    DINING_TOGETHER("회식"),
    PARENTAL_LEAVE("육아 휴직");

    private String label;

    ScheduleType(String label){
        this.label = label;
    }
}
