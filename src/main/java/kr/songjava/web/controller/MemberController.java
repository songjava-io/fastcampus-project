package kr.songjava.web.controller;

import java.io.IOException;
import java.net.URL;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import kr.songjava.web.domain.Member;
import kr.songjava.web.exception.ApiException;
import kr.songjava.web.form.MemberSaveUploadForm;
import kr.songjava.web.security.userdetails.OAuth2KakaoAccount;
import kr.songjava.web.service.FileCopyResult;
import kr.songjava.web.service.FileService;
import kr.songjava.web.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 회원 컨트롤러
 */
@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
@Validated
@Slf4j
@Tag(name = "Member", description = "회원 API")
public class MemberController {

	private final MemberService memberService;
	private final FileService fileService;
	
	/**
	 * 회원 가입 처리. (파일첨부 포함)
	 * @param form
	 * @return
	 * @throws IOException 
	 */
	@PostMapping("/save")
	@Operation(summary = "회원 가입 등록/수정", description = "회원 가입 양식 및 프로필 이미지 업로드 등록/수정 처리를 한다.")
	@ApiResponse(description = "회원가입 성공에 대한 boolean 리턴")
	public HttpEntity<Boolean> save(@Validated @Parameter(name = "form", required = true, description = "회원가입 폼") MemberSaveUploadForm form,
			@AuthenticationPrincipal OAuth2KakaoAccount kakaoAccount) throws IOException {
		log.info("form : {}", form);
		log.info("nickname : {}", form.getNickname());
		// 사용이 불가능 상태인경우
		
		boolean useAccount = memberService.isUseAccount(form.getAccount());
		log.info("useAccount : {}", useAccount);
		if (useAccount) {
			throw new ApiException("아이디는 중복으로 사용이 불가능 합니다.");
		}
		FileCopyResult result = null;
		String oauth2Id = null;
		String oauth2ClientName = null;
		if (kakaoAccount != null) {
			oauth2Id = kakaoAccount.id();
			oauth2ClientName = "KAKAO";
			String profileImage = kakaoAccount.imageUrl();
			log.info("profileImage : {}", profileImage);
			String originalFilename = profileImage.substring(profileImage.lastIndexOf("/") + 1, profileImage.length());
			result = fileService.copy(new URL(profileImage).openStream(), originalFilename);
		} else {
			// 파일첨부 객체
			MultipartFile profileImage = form.getProfileImage();
			if (profileImage != null) {
				result = fileService.copy(profileImage.getInputStream(), profileImage.getOriginalFilename());
			}
		}
		// form -> member 로 변환
		Member member = Member.builder()
			.account(form.getAccount())
			.password(form.getPassword())
			.nickname(form.getNickname())
			.profileImagePath(result != null ? result.imagePath() : null)
			.profileImageName(result != null ? result.originalFilename() : null)
			.oauth2Id(oauth2Id)
			.oauth2ClientName(oauth2ClientName)
			.build();
		// 등록 처리
		memberService.save(member);
		return ResponseEntity.ok(true);
	}
	
	
}
