package kr.songjava.web.controller;

import com.nimbusds.oauth2.sdk.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.songjava.web.domain.*;
import kr.songjava.web.exception.ApiException;
import kr.songjava.web.form.MemberSaveUploadForm;
import kr.songjava.web.form.MemberScheduleSaveForm;
import kr.songjava.web.response.ScheduleDefaultInfo;
import kr.songjava.web.security.userdetails.OAuth2KakaoAccount;
import kr.songjava.web.security.userdetails.SecurityUserDetails;
import kr.songjava.web.service.FileCopyResult;
import kr.songjava.web.service.FileService;
import kr.songjava.web.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 회원 일정관리 컨트롤러
 */
@RestController
@RequestMapping("/member/schedule")
@RequiredArgsConstructor
@Validated
@Slf4j
@Tag(name = "Schedule", description = "회원 일정관리")
public class ScheduleController {

	private static final String ATTACHMENT_FORMAT = "attachment; filename=\"%s\";";

	private final MemberService memberService;

	/**
	 * 일정 목록
	 * @param userDetails
	 * @return
	 */
	@GetMapping
	@Operation(summary = "일정 조회", description = "회원이 등록된 일정 목록을 리턴한다.", responses = {
			@ApiResponse(responseCode = "200", description = "일정 조회 성공", content = @Content(array = @ArraySchema(schema = @Schema(implementation = MemberSchedule.class)))),
	})
	public List<MemberSchedule> getList(@AuthenticationPrincipal SecurityUserDetails userDetails) {
		return memberService.getScheduleList(userDetails.getMemberSeq());
	}

	/**
	 * 스케줄 등록/수정 화면에 사용할 기본 정보를 리턴
	 * @return
	 */
	@GetMapping("/default-info")
	public ScheduleDefaultInfo defaultInfo() {
		return ScheduleDefaultInfo.builder()
			.scheduleTimes(ScheduleTime.values())
			.scheduleTypes(ScheduleType.values())
			.build();
	}
	/**
	 * 일정 등록/수정 처리
	 * @param form
	 * @param userDetails
	 * @return
	 */
	@PostMapping("/save")
	public HttpEntity<Boolean> save(@Validated @Parameter(name = "form", required = true, description = "일정 등록/ 폼") MemberScheduleSaveForm form,
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

	@GetMapping("/download")
	public void download(HttpServletRequest request, HttpServletResponse response) {
		List<MemberSchedule> excelList = memberService.getScheduleList();
		try {
			Workbook workbook = new XSSFWorkbook();

			org.apache.poi.ss.usermodel.Sheet sheet = workbook.createSheet("데이터");

			int rowNo = 0;

			Row headerRow = sheet.createRow(rowNo);
			headerRow.createCell(0).setCellValue("회원 닉네임");
			headerRow.createCell(1).setCellValue("스케줄 제목");
			headerRow.createCell(2).setCellValue("스케줄 종류");
			headerRow.createCell(3).setCellValue("스케줄 일자");
			headerRow.createCell(4).setCellValue("스케줄 시간");
			headerRow.createCell(5).setCellValue("등록일자");



			java.util.Date date = new java.util.Date(System.currentTimeMillis());
			Instant instant = date.toInstant();

			LocalDateTime ldt = instant.atZone(ZoneId.systemDefault()).toLocalDateTime();

			DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");

			ldt.format(fmt);


			for (MemberSchedule schedule : excelList) {
				rowNo++;
				Row row = sheet.createRow(rowNo);
				row.createCell(0).setCellValue(schedule.getNickname());
				row.createCell(1).setCellValue(schedule.getTitle());
				row.createCell(2).setCellValue(schedule.getScheduleType().label());
				row.createCell(3).setCellValue(DateFormatUtils.format(schedule.getScheduleDate(), "yyyy-MM-dd"));
				row.createCell(4).setCellValue(schedule.getStartTime().label() + "~" + schedule.getEndTime().label());
				row.createCell(5).setCellValue(DateFormatUtils.format(schedule.getRegDate(), "yy.MM.dd HH:mm"));
			}

			// 컨텐츠 타입과 파일명 지정

			String disposition = String.format(ATTACHMENT_FORMAT, getEncodeFilename(request, "회원일정목록.xlsx"));
			response.setContentType("ms-vnd/excel");
			response.setHeader("Content-Disposition", disposition);

			workbook.write(response.getOutputStream());
			workbook.close();

		} catch (Exception e) {
			log.error("download error", e);
			throw new ApiException("파일을 찾을 수가 없습니다.");
		}
	}

	private String getEncodeFilename(HttpServletRequest request, String filename) throws UnsupportedEncodingException {
		String userAgent = request.getHeader(HttpHeaders.USER_AGENT);
		log.info("userAgent : {}", userAgent);
		// 접속한 브라우저 정보
		if (userAgent.contains("MSIE") || userAgent.contains("Trident") || userAgent.contains("Edg")) {
			log.info("userAgent : {}", "MSIE!!!");
			// 인터넷 익스플로러
			return URLEncoder.encode(filename, StandardCharsets.UTF_8.name()).replaceAll("\\+", "%20");
		} else if (userAgent.contains("Firefox")) {
			log.info("userAgent : {}", "FireFox");
			// 파이어폭스
			return new String(filename.getBytes(StandardCharsets.UTF_8.name()), StandardCharsets.ISO_8859_1.name());
		} else if (userAgent.contains("Chrome")) {
			log.info("userAgent : {}", "Chrome");
			// 크롬
			return new String(filename.getBytes(StandardCharsets.UTF_8.name()), StandardCharsets.ISO_8859_1.name());
		}
		// 그외 브라우져
		return new String(filename.getBytes(StandardCharsets.UTF_8.name()), StandardCharsets.ISO_8859_1.name());
	}

}
