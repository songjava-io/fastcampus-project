package kr.songjava.web.configuration;

import java.nio.file.Paths;
import java.util.List;
import java.util.Locale;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.boot.info.OsInfo;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import kr.songjava.web.configuration.properties.FileProperties;
import kr.songjava.web.error.CustomErrorAttributes;
import kr.songjava.web.interceptor.RequestLoggingInterceptor;
import kr.songjava.web.thymeleaf.HtmlTagDialect;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Configuration
@EnableAspectJAutoProxy
@RequiredArgsConstructor
@Slf4j
public class WebMvcConfiguration implements WebMvcConfigurer {

	private final FileProperties fileProperties;
	
	@Bean
	MappingJackson2JsonView jsonView() {

		MappingJackson2JsonView jsonView = new MappingJackson2JsonView();
		return jsonView;
	}
	
	@Bean
	MessageSource messageSource() {
		ReloadableResourceBundleMessageSource source = 
			new ReloadableResourceBundleMessageSource();
		source.setBasename("classpath:config/messages/message");
		source.setDefaultEncoding("UTF-8");
		source.setDefaultLocale(Locale.ENGLISH);
		return source;
	}
	
	@Bean
	@Override
	public Validator getValidator() {
		LocalValidatorFactoryBean factory =
			new LocalValidatorFactoryBean();
		factory.setValidationMessageSource(messageSource());
		return factory;
	}
	
	@Bean
	CustomErrorAttributes customErrorAttributes() {
		return new CustomErrorAttributes();
	}
	
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry
			// 사용할 인터셉터를 set
			.addInterceptor(new RequestLoggingInterceptor())
			// 추가한 인터셉터가 동작해야할 URL 패턴 추가
			.addPathPatterns("/**")
			// 로그인 제외
			.excludePathPatterns("/member/login")
			.order(1);
	}
	
	@Bean
	public HtmlTagDialect htmlTagDialect() {
		return new HtmlTagDialect();
	}
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		String rootPath = Paths.get(fileProperties.rootPath()).toAbsolutePath().toString() + "/";
		log.info("rootPath : {}", rootPath);
		OsInfo osInfo = new OsInfo();
		// os 비교하여 환경에 맞게 업로드 파일 접근시 실제 파일 경로로 매핑되게 설정
		if (osInfo.getName().toLowerCase().contains("windows")) {
			// 윈도우 환경
			registry.addResourceHandler(fileProperties.resourcePath())
				.addResourceLocations("file:///" + rootPath);
		} else {
			// 유닉스, 리눅스 환경
			registry.addResourceHandler(fileProperties.resourcePath())
			.addResourceLocations("file://" + rootPath);
		}		
	}

	@Bean
	public OpenAPI openAPI() {
		Info info = new Info()
			.title("Bckend API")
			.version("1.0.0")
			.description("Fastcampus 국비지원과정에서 Spring Boot를 이용한 백엔드 API 문서 입니다.")
			.termsOfService("http://swagger.io/terms/")
			.contact(new Contact()
				.name("songjava-io")
				.url("https://github.com/songjava-io")
				.email("songdev2021@gmail.com"))
			.license(new License()
				.name("Apache License Version 2.0")
				.url("http://www.apache.org/licenses/LICENSE-2.0")
			);
		final String securitySchemeName = "bearerAuth";
		return new OpenAPI()
			.components(
				new Components()
					.addSecuritySchemes(securitySchemeName,
						new io.swagger.v3.oas.models.security.SecurityScheme()
							.type(io.swagger.v3.oas.models.security.SecurityScheme.Type.HTTP)
							.scheme("bearer")
							.bearerFormat("JWT")
							.in(SecurityScheme.In.HEADER).name(HttpHeaders.AUTHORIZATION)
					)
			)
			.security(List.of(new SecurityRequirement().addList(securitySchemeName)))
			.info(info);
	}

	
}
