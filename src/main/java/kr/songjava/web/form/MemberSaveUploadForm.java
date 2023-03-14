package kr.songjava.web.form;

import javax.validation.GroupSequence;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

import kr.songjava.web.validation.ValidationSteps;
import lombok.Data;

@Data
@GroupSequence({
	MemberSaveUploadForm.class,
	ValidationSteps.Step1.class,
	ValidationSteps.Step2.class,
	ValidationSteps.Step3.class,
	ValidationSteps.Step4.class,
	ValidationSteps.Step5.class,
	ValidationSteps.Step6.class,
	ValidationSteps.Step7.class,
})
@Schema(description = "회원 가입 폼")
public class MemberSaveUploadForm {

	@NotEmpty(groups = ValidationSteps.Step1.class, 
		message = "{MemberSaveForm.account.NotEmpty}")
	@Email(groups = ValidationSteps.Step2.class, 
		message = "{MemberSaveForm.account.Email}")
	@Schema(description = "계정", example = "test@gmail.com")
	private String account;
	
	@NotEmpty(groups = ValidationSteps.Step3.class, 
			message = "{MemberSaveForm.password.NotEmpty}")
	@Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,12}$", groups = ValidationSteps.Step4.class, 
		message = "{MemberSaveForm.password.Pattern}")
	@Schema(description = "비밀번호", example = "test1234@@")
	private String password;
	
	@NotEmpty(groups = ValidationSteps.Step5.class, 
			message = "{MemberSaveForm.nickname.NotEmpty}")
	@Length(min = 2, max = 10, groups = ValidationSteps.Step6.class, 
		message = "{MemberSaveForm.nickname.Length}")
	@Schema(description = "닉네임", example = "홍길동")
	private String nickname;
	
	//@NotEmptyMultipartFile(groups = ValidationSteps.Step7.class, 
		//	message = "{MemberSaveForm.profileImage.NotEmpty}")
	@Schema(description = "프로필 이미지")
	private MultipartFile profileImage;
	
	
}
