package com.example.demo.config;

import com.example.demo.filter.JwtRequestFilter;
import com.example.demo.service.UserDetailService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        securedEnabled = true
)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtRequestFilter jwtRequestFilter;

    private final UserDetailService userDetailService;

    public WebSecurityConfig(JwtRequestFilter jwtRequestFilter, UserDetailService userDetailService) {
        this.jwtRequestFilter = jwtRequestFilter;
        this.userDetailService = userDetailService;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    // cuz java EE, need to create bean AuthenticationManager
    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    // configure web security
    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        // disable Cross Site Request Forgery attack
        httpSecurity.csrf().disable()
                // with this path - no need to authenticate
                .authorizeRequests().antMatchers("/authenticate").permitAll()
                // authenticate all others request
                .anyRequest().authenticated()
                // if exception occur => redirect to denied page
                .and().exceptionHandling().accessDeniedPage("/403")
                // to tell spring disable session
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // add filter (request need validated jwt to pass over the security)
        httpSecurity.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        super.configure(auth);
        auth.userDetailsService(userDetailService).passwordEncoder(passwordEncoder());
    }

}
