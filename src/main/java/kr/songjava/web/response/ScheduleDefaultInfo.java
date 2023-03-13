package kr.songjava.web.response;

import kr.songjava.web.domain.ScheduleTime;
import kr.songjava.web.domain.ScheduleType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
public class ScheduleDefaultInfo {

	private ScheduleType[] scheduleTypes;
	private ScheduleTime[] scheduleTimes;

}
