package kr.songjava.web.domain;

import lombok.Builder;

import java.util.Date;

@Builder
public class MemberSchedule {
    private int scheduleSeq;
    private int memberSeq;
    private ScheduleType scheduleType;
    private Date scheduleDate;
    private ScheduleTime startTime;
    private ScheduleTime endTime;
    private String title;
    private Date regDate;


    public int getScheduleSeq() {
        return scheduleSeq;
    }

    public int getMemberSeq() {
        return memberSeq;
    }
}
