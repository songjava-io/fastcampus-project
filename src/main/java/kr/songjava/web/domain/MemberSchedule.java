package kr.songjava.web.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "회원 일정")
public class MemberSchedule {

	@Schema(description = "스케줄 번호")
	private int scheduleSeq;

	@Schema(description = "회원 번호")
	@JsonIgnore
	private int memberSeq;

	@Schema(description = "스케줄 종류")
	private ScheduleType scheduleType;

	@Schema(description = "스케줄 일자")
	private Date scheduleDate;
	@Schema(description = "시작시간")
	private ScheduleTime startTime;

	@Schema(description = "종료시간")
	private ScheduleTime endTime;
	@Schema(description = "제목")
	private String title;
	@Schema(description = "등록일자")
	@JsonIgnore
	private Date regDate;
}
