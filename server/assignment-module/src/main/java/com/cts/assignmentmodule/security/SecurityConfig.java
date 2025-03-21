package com.cts.assignmentmodule.security;

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
                		 .requestMatchers("/api/assignment/create",
                 		                "/api/assignment/delete",
                 		                
                				         "/api/asubmission/viewAll",
                				          "/api/asubmission/viewFiles",
                				          "/api/asubmission/assignMarks") 
                                      .hasAuthority("INSTRUCTOR")
                                 
                 .requestMatchers("/api/asubmission/upload",
                 		         "/api/enrollment/delete")
                 		
                 		
                                  .hasAuthority("STUDENT")	
                 		
                 .requestMatchers("/api/assignment/view",
                		          "/api/course/verifyCourse",
                		          "/api/enrollment/verifyEnrollment",
                		          "/api/course/verifyInstructor",
                		          "/api/assignment/viewById",
                		          "api/assignment/AssignmentDetails",
                		        
                		          "/api/asubmission/AssignmentSubmissionDetails")
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


