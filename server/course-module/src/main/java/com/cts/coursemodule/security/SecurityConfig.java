package com.cts.coursemodule.security;

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
                        .requestMatchers("/api/course/create",
                        		        "/api/course/instructorId",
                        		        "/api/course/update",
                        		        "/api/course/delete",
                        		        
                        		        "/api/lesson/add",
                        		        "/api/lesson/update",
                        		        "/api/lesson/delete",
                        		        
                        		        
                        		         
                        		          "/api/enrollment/course")
                                        .hasAuthority("INSTRUCTOR")
                                        
                        .requestMatchers("/api/enrollment/create",
                        		         "/api/enrollment/student")
                        		
                        		
                                         .hasAuthority("STUDENT")	
                        		
                        .requestMatchers("/api/course/courseDetails",
                        		          "/api/course/get",
                        		           "/api/course/verifyInstructor",
                        		           "/api/course/verifyCourse",
                        		           "/api/course/getInstructorId",
                        		          
                        		            
                        		           
                        		           "/api/enrollment/getAll",
                        		           "/api/enrollment/verifyEnrollment",
                        		           "/api/enrollment/studentList",
                          		          "/api/enrollment/studentList/**",
                          		          
                          		          "/api/forum/send",
                        		
                        		            "api/lesson/getAll",
                        		            "api/lesson/view")
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


