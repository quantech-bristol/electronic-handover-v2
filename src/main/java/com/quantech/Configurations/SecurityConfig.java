package com.quantech.Configurations;


import com.quantech.entities.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableAutoConfiguration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    UserService userService;
    @Autowired
    CustomLogoutSuccessHandler customLogoutSuccessHandler;

    @Autowired
    public void configAuthentication(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/Admin", "/Admin/**", "/addWard", "/addDoctor").hasAuthority(SecurityRoles.Admin.toString())
                .antMatchers("/doctor","/createHandover","/viewHandovers", "/viewPatient*","/quantech", "/patient").hasAuthority(SecurityRoles.Doctor.toString())
                .antMatchers("/css/**", "/js/**", "/images/**").permitAll()
                .antMatchers("/**").authenticated()
                .anyRequest().authenticated().anyRequest().denyAll()
                .and()
            .formLogin()
                .loginPage("/login").permitAll()
                .defaultSuccessUrl("/", true)
                .and()
            .logout()
                .logoutSuccessHandler(customLogoutSuccessHandler).permitAll()
                .and()
            .exceptionHandling()
                .accessDeniedPage("/403");
    }
}