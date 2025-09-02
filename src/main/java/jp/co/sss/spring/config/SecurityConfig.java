package jp.co.sss.spring.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import jp.co.sss.spring.security.UserDetailsServiceImpl;

@Configuration
public class SecurityConfig {
	
	private final UserDetailsService userDetailsService;

	
	public SecurityConfig(UserDetailsServiceImpl userDetailsService, PasswordEncoder passwordEncoder) {
	    this.userDetailsService = userDetailsService;
    }
	
  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationProvider authenticationProvider) throws Exception {
    http
    .authorizeHttpRequests(auth -> auth
            .requestMatchers("/register", "/doRegister", "/css/**").permitAll() // 新規登録画面は認証不要
            .anyRequest().authenticated() 
        )
        .formLogin(form -> form
            .loginPage("/login")
            .loginProcessingUrl("/doLogin") 
            .usernameParameter("email")
            .passwordParameter("password")
            .defaultSuccessUrl("/top", true) 
            .failureUrl("/login?error=true")
            .permitAll()
        )
        .logout(logout -> logout
            .logoutUrl("/logout")
            .logoutSuccessUrl("/login?logout=true")
            .invalidateHttpSession(true)
            .permitAll()
        )
        .authenticationProvider(authenticationProvider);
        return http.build();
  }
  
  @Bean
  public AuthenticationProvider authenticationProvider(PasswordEncoder passwordEncoder) {
	  DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
	  provider.setUserDetailsService(userDetailsService);
	  provider.setPasswordEncoder(passwordEncoder());
	  
	  return provider;
  }
  
  @Bean
  public PasswordEncoder passwordEncoder() {
	  return new BCryptPasswordEncoder();
  }
}