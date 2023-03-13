package kr.songjava.web.controller;

import kr.songjava.web.domain.MemberSchedule;
import kr.songjava.web.domain.ScheduleTime;
import kr.songjava.web.domain.ScheduleType;
import kr.songjava.web.exception.ApiException;
import kr.songjava.web.form.MemberScheduleSaveForm;
import kr.songjava.web.response.ScheduleDefaultInfo;
import kr.songjava.web.security.userdetails.SecurityUserDetails;
import kr.songjava.web.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 회원 일정 관리 컨트롤러
 */
@RestController
@RequestMapping("/member/schedule")
@RequiredArgsConstructor
@Validated
@Slf4j
public class ScheduleController {

	private final MemberService memberService;


	/**
	 * 일정 목록
	 *
	 * @param userDetails the user details
	 * @return List<MemberSchedule>  list
	 */
	@GetMapping
	public List<MemberSchedule> getList(@AuthenticationPrincipal SecurityUserDetails userDetails) {
		return memberService.getScheduleList(userDetails.getMemberSeq());
	}

	/**
	 * 스케줄 등록/수정 화면에 사용할 기본 정보를 리턴
	 *
	 * @return the schedule default info
	 */
	@GetMapping("/default-info")
	public ScheduleDefaultInfo defaultInfo() {
		return new ScheduleDefaultInfo().builder()
				.scheduleTimes(ScheduleTime.values())
				.scheduleTypes(ScheduleType.values())
				.build();
	}

	/**
	 * 일정 등록/수정 처리
	 *
	 * @param form        the form
	 * @param userDetails the user details
	 * @return http entity
	 */
	@PostMapping("/save")
	public HttpEntity<Boolean> save(@Validated MemberScheduleSaveForm form,
									@AuthenticationPrincipal SecurityUserDetails userDetails) {

		// form -> domain 으로 변환
		MemberSchedule schedule = MemberSchedule.builder()
				.memberSeq(userDetails.getMemberSeq())
				.scheduleType(form.getScheduleType())
				.scheduleDate(form.getScheduleDate())
				.startTime(form.getStartTime())
				.endTime(form.getEndTime())
				.title(form.getTitle())
				.scheduleSeq(form.getScheduleSeq())
				.build();
		// 등록 처리
		memberService.saveSchedule(schedule);
		return ResponseEntity.ok(true);
	}

	/**
	 * 일정 삭제 처리
	 *
	 * @param userDetails the user details
	 * @param scheduleSeq the schedule seq
	 * @return http entity
	 */
	@PostMapping("/delete")
	public HttpEntity<Boolean> delete(@AuthenticationPrincipal SecurityUserDetails userDetails,
									  @RequestParam int scheduleSeq) {
		MemberSchedule schedule = memberService.getSchedule(scheduleSeq);
		Assert.notNull(schedule, "스케줄이 없습니다.");
		if (schedule.getMemberSeq() != userDetails.getMemberSeq()) {
			throw new ApiException("스케줄 삭제 권한이 없습니다.");
		}
		// 스케줄 삭제 처리
		memberService.delete(scheduleSeq);

		return ResponseEntity.ok(true);
	}

}
