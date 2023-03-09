package kr.songjava.web.controller;

import kr.songjava.web.domain.Member;
import kr.songjava.web.domain.MemberSchedule;
import kr.songjava.web.exception.ApiException;
import kr.songjava.web.form.MemberSaveUploadForm;
import kr.songjava.web.form.MemberScheduleSaveForm;
import kr.songjava.web.security.userdetails.OAuth2KakaoAccount;
import kr.songjava.web.security.userdetails.SecurityUserDetails;
import kr.songjava.web.service.FileCopyResult;
import kr.songjava.web.service.FileService;
import kr.songjava.web.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;
import java.util.List;

/**
 * 회원 일정관리 컨트롤러
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
	 * @param userDetails
	 * @return
	 */
	@GetMapping
	public List<MemberSchedule> getList(@AuthenticationPrincipal SecurityUserDetails userDetails) {
		return memberService.getScheduleList(userDetails.getMemberSeq());
	}

	/**
	 * 일정 등록/수정 처리
	 * @param form
	 * @param userDetails
	 * @return
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
	 * 일정 삭제 처리.
	 * @param userDetails
	 * @param scheduleSeq
	 * @return
	 */
	@PostMapping("/delete")
	public HttpEntity<Boolean> delete(@AuthenticationPrincipal SecurityUserDetails userDetails, @RequestParam int scheduleSeq) {
		MemberSchedule schedule = memberService.getSchedule(scheduleSeq);
		Assert.notNull(schedule, "스케줄이 없습니다.");
		// 본인 체크
		if (schedule.getMemberSeq() != userDetails.getMemberSeq()) {
			throw new ApiException("스케줄 삭제 권한이 없습니다.");
		}
		// 스케줄 삭제 처리
		memberService.deleteSchedule(scheduleSeq);
		return ResponseEntity.ok(true);
	}

}
