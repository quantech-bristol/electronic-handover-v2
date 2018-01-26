package com.quantech.Configurations;

import javax.sql.DataSource;

import com.quantech.entities.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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
    public void configAuthentication(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/Admin", "/Admin/**", "/addWard", "/addDoctor").hasAuthority(SecurityRoles.Admin.toString())
                .antMatchers("/doctor","/createHandover","/viewHandovers", "/viewPatient*","/quantech").hasAuthority(SecurityRoles.Doctor.toString())
                .antMatchers("/css/**", "/js/**", "/images/**").permitAll()
                .antMatchers("/**").authenticated()
                .anyRequest().authenticated().anyRequest().denyAll()
                .and().formLogin().loginPage("/login").permitAll()
                .and().logout().permitAll();
        http.exceptionHandling().accessDeniedPage("/403");
        http.formLogin().defaultSuccessUrl("/quantech", true);
    }

}