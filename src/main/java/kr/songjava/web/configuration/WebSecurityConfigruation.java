package kr.songjava.web.configuration;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.source.ImmutableSecret;
import com.nimbusds.jose.proc.SecurityContext;
import kr.songjava.web.configuration.properties.FileProperties;
import kr.songjava.web.configuration.properties.FrontendProperties;
import kr.songjava.web.security.userdetails.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.JdbcOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.crypto.spec.SecretKeySpec;

@Configuration
@EnableWebSecurity
public class WebSecurityConfigruation {

	private static final String JWT_SECRET_KEY = "asdfcdsr432rsdcsdfsdfdsfsdfsfdfcds";
	
	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http,
		JwtTokenAuthenticationSuccessHandler jwtTokenAuthenticationSuccessHandler,
		JwtTokenAuthenticationManager jwtTokenAuthenticationManager,
		Oauth2AuthenticationSuccessHandler oauth2AuthenticationSuccessHandler,
		SecurityOauth2Service securityOauth2Service,
		FileProperties fileProperties,
		CorsConfigurationSource corsConfigurationSource,
		UsernamePasswordAuthenticationFailureHandler usernamePasswordAuthenticationFailureHandler) throws Exception {
		http.authorizeRequests()
			// ?????? url ????????? ????????? ??????????????? ????????????
			.antMatchers(
				"/",
				"/public/**", 
				"/member/save",
				"/file/download",
				"/event/**",
				"/member/schedule",
				"/member/schedule/**",
				"/v2/api-docs",
				"/swagger-resources",
				"/swagger-resources/**",
				"/configuration/ui",
				"/configuration/security",
				"/swagger-ui.html",
				"/swagger-api.html",
				"/v3/api-docs",
				"/v3/api-docs/**",
				"/swagger-ui/**",

				fileProperties.resourcePath()
			)
			.permitAll()
			// ????????? ????????? ???????????? ?????? ????????????
			.anyRequest().hasRole("USER").and()
			.oauth2Login().successHandler(oauth2AuthenticationSuccessHandler).failureHandler(usernamePasswordAuthenticationFailureHandler)
				.userInfoEndpoint().userService(securityOauth2Service)

			.and().and()
			.sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS)		
			.and()
			.formLogin(form -> {
				form.successHandler(jwtTokenAuthenticationSuccessHandler);
				form.failureHandler(usernamePasswordAuthenticationFailureHandler);
				form.permitAll();
			})
			.csrf().disable()
			.cors().configurationSource(corsConfigurationSource)
			.and()
			// Jwt Token??? ????????? ???????????? ?????? ????????? ???????????? ?????????
			.addFilterBefore(
				new BearerTokenAuthenticationFilter(jwtTokenAuthenticationManager),
					AnonymousAuthenticationFilter.class);
		return http.build();
	}
	
	@Bean
	CorsConfigurationSource corsConfigurationSource(FrontendProperties frontendProperties) {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.addAllowedOriginPattern(frontendProperties.domain());
		configuration.addAllowedHeader("*");
		configuration.addAllowedMethod("*");
		configuration.setAllowCredentials(true);

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}
	
	/**
	 * ???????????? ????????? ??????
	 * ??????????????? There is no PasswordEncoder mapped for the id "null" ????????? ??????
	 * @return
	 */
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	JdbcOAuth2AuthorizedClientService JdbcOAuth2AuthorizedClientService(JdbcOperations operations,
			ClientRegistrationRepository clientRegistrationRepository) {
		return new JdbcOAuth2AuthorizedClientService(operations, clientRegistrationRepository);
	}
	
	@Bean
	JwtEncoder jwtEncoder() {
		return new NimbusJwtEncoder(new ImmutableSecret<SecurityContext>(JWT_SECRET_KEY.getBytes()));
	}
	
	@Bean
	JwtDecoder jwtDecoder() {
		SecretKeySpec secretKeySpec = new SecretKeySpec(JWT_SECRET_KEY.getBytes(), JWSAlgorithm.HS256.getName());
		return NimbusJwtDecoder.withSecretKey(secretKeySpec).build();
	}

}