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

@Configuration
public class SecurityConfig {
	
  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationProvider authenticationProvider) throws Exception {
    http
    .authorizeHttpRequests(authz -> authz
            .requestMatchers(
            		"/register", "/doRegister", 
            		"/login", "/doLogin",
            		"/css/**","/js/**"
            	).permitAll() 
            .requestMatchers("/order/**").authenticated()
            .anyRequest().authenticated() //一時的に認証OFF　.authenticated→CSS適用後これに変更
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
  public AuthenticationProvider authenticationProvider(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
	  DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
	  provider.setUserDetailsService(userDetailsService);
	  provider.setPasswordEncoder(passwordEncoder);
	  
	  return provider;
  }
  
  @Bean
  public PasswordEncoder passwordEncoder() {
	  return new BCryptPasswordEncoder();
  }
}