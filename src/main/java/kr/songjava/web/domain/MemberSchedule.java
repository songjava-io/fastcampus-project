package kr.songjava.web.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * packageName :  kr.songjava.web.domain
 * fileName : MemberSchedule
 * author :  82108
 * date : 2023-03-09
 * description :
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberSchedule {

    private int scheduleSeq;
    private int memberSeq;
    private ScheduleType scheduleType;
    private Date scheduleDate;
    private ScheduleTime startTime;
    private ScheduleTime endTime;
    private String title;
    private Date regDate;

}
