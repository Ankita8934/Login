package com.service.login.auth.config;
import com.service.login.auth.jwt.AuthEntryPointJwt;
import com.service.login.auth.jwt.AuthTokenFilter;
import com.service.login.auth.jwt.UserDetailsServiceImpl;
import com.service.login.auth.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Autowired(required = true)
    private AuthEntryPointJwt unauthorizedHandler;

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }

    @Override
    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http.cors().and().csrf().disable()
//                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
//                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
//                .authorizeRequests(authorizeRequest ->
//                                authorizeRequest
//                                .antMatchers("/").permitAll()
//                                .antMatchers("/login/auth").permitAll()
//                                .antMatchers("/auth").permitAll()
//                                .antMatchers("/signup").permitAll()
////                                .antMatchers("/oauth2/code/google").permitAll()
////                                .anyRequest().authenticated().and().exceptionHandling().and().sessionManagement()
////                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                ) .oauth2Login(oauth2Login ->
//                oauth2Login
//                        .loginPage("/login") // Specify the custom login page
//                        .defaultSuccessUrl("/oauth2/code/google", true) // Specify the custom success URL
//        )
//                .logout(logout -> logout
//                        .logoutSuccessUrl("/") // Specify the logout success URL
//                        .clearAuthentication(true) // Clear the authentication after logout
//                );
//    }
        @Override
        public void configure(HttpSecurity http) throws Exception {
            http.cors().and().csrf().disable()
                    .authorizeRequests(authorizeRequests ->
                            authorizeRequests
                                        .antMatchers("/").permitAll()
                                        .antMatchers("/login/auth").permitAll()
                                        .antMatchers("/auth").permitAll()
                                        .antMatchers("/signup").permitAll()
                                    .antMatchers("/oauth2/code/google").permitAll()// Allow access to the home page without authentication
                                    .anyRequest().authenticated()
                    )
                    .oauth2Login(oauth2Login ->
                            oauth2Login
                                    .loginPage("/login") // Specify the custom login page
                                    .defaultSuccessUrl("/oauth2/code/google", true) // Specify the custom success URL
                    )
                    .logout(logout -> logout
                            .logoutSuccessUrl("/") // Specify the logout success URL
                            .clearAuthentication(true) // Clear the authentication after logout
                    );
        }

}
