package crudapplication.crud.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import crudapplication.crud.enums.Roles;

/**
 * SecurityConfiguration allows customization to the {@link WebSecurity}
 */
@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	UserDetailsService userDetailsService;

	/**
	 * Configure to specify the {@link AuthenticationManager}.
	 *
	 * @param auth the {@link AuthenticationManagerBuilder} to use
	 * @throws Exception
	 */
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(getPasswordEncoder());
	}

	/**
	 * Method to configure the {@link HttpSecurity}.
	 * @param auth the {@link AuthenticationManagerBuilder} to use
	 * @throws Exception
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
				.antMatchers("/signup/**").hasRole(Roles.ADMIN.toString())
				.antMatchers("/users/**").hasRole(Roles.ADMIN.toString())
				.antMatchers("/employee/**").hasAnyRole(Roles.ADMIN.toString(), Roles.USER.toString())
				.antMatchers("/employees/**").hasAnyRole(Roles.ADMIN.toString(), Roles.USER.toString())
				.antMatchers("/monitor/**").hasAnyRole(Roles.SERVER_ADMIN.toString())
				.antMatchers("/").permitAll()
			.and()
				.formLogin()
					.permitAll()
			.and()
				.logout()
					.invalidateHttpSession(true)
					.clearAuthentication(true)
					.deleteCookies("JSESSIONID")
					.logoutUrl("/logout")
					.logoutSuccessUrl("/")
					.permitAll()
			.and()
				.csrf()
					.disable()
				.httpBasic();
	}

	/**
	 * Gets the bcrypt password encoder.
	 *
	 * @return the bcrypt password encoder
	 */
	@Bean
	public PasswordEncoder getPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

}
