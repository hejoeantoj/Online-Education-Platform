package com.cts.quizmodule.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        return http.csrf(customizer -> customizer.disable()).
                authorizeHttpRequests(request -> request
                		 .requestMatchers("/api/quiz/create",
                 		                "/api/quiz/delete",
                 		                "/api/quiz/update",
                				         "/api/question/create",
                				         "/api/squiz/viewAll"
                				         )
                				                               
                                      .hasAuthority("INSTRUCTOR")
                                 
                 .requestMatchers("/api/squiz/submit",
                		          "/api/squiz/result"
                 		        )
                 		
                 		
                                  .hasAuthority("STUDENT")	
                 		
                 .requestMatchers("/api/quiz/viewAll",
                		           "/api/squiz/viewAll",
                		          "/api/course/verifyCourse",
                		          "/api/question/view",
                		          "/api/enrollment/verifyEnrollment",
                		          "/api/course/verifyInstructor",
                		          "/api/quiz/QuizDetails",
                		          "/api/squiz/QuizSubmissionDetails")
                         .permitAll()
                        .anyRequest().authenticated())
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();

    }


//
//    @Bean
//    public AuthenticationProvider authenticationProvider() {
//        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
//        provider.setPasswordEncoder(new BCryptPasswordEncoder(12));
//        provider.setUserDetailsService(userDetailsService);
//
//
//        return provider;
//    }

//    @Bean
//    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
//        return config.getAuthenticationManager();
//
//    }
}


