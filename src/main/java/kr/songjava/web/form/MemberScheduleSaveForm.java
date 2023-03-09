package kr.songjava.web.form;

import kr.songjava.web.domain.ScheduleTime;
import kr.songjava.web.domain.ScheduleType;
import kr.songjava.web.validation.ValidationSteps;
import lombok.Data;

import javax.validation.GroupSequence;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@GroupSequence({
	MemberScheduleSaveForm.class,
	ValidationSteps.Step1.class,
	ValidationSteps.Step2.class,
	ValidationSteps.Step3.class,
	ValidationSteps.Step4.class,
	ValidationSteps.Step5.class,
})
public class MemberScheduleSaveForm {

	private int scheduleSeq;
	@NotNull(message = "스케줄 종류를 선택해주세요.", groups = ValidationSteps.Step1.class)
	private ScheduleType scheduleType;

	@NotEmpty(message = "제목을 입력해주세요.", groups = ValidationSteps.Step2.class)
	private String title;

	@NotNull(message = "스케줄 일자를 선택해주세요.", groups = ValidationSteps.Step3.class)
	private Date scheduleDate;

	@NotNull(message = "시작 시간을 선택해주세요.", groups = ValidationSteps.Step4.class)
	private ScheduleTime startTime;

	@NotNull(message = "종료 시간을 선택해주세요.", groups = ValidationSteps.Step5.class)
	private ScheduleTime endTime;


	
}
