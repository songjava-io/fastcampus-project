package kr.songjava.web.response;

import kr.songjava.web.domain.ScheduleTime;
import kr.songjava.web.domain.ScheduleType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * packageName :  kr.songjava.web.response
 * fileName : ScheduleDefaultInfo
 * author :  82108
 * date : 2023-03-13
 * description :
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleDefaultInfo {

    private ScheduleType[] scheduleTypes;
    private ScheduleTime[] scheduleTimes;
}
