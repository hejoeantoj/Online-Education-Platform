package com.cts.coursemodule.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.Arrays;



@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        return http.cors(cors -> cors.configurationSource(corsConfigurationSource()))
        		.csrf(customizer -> customizer.disable()).
                authorizeHttpRequests(request -> request
                        .requestMatchers("/api/course/create",
                        		        "/api/course/update",
                        		        "/api/course/delete",
                        		        
                        		        "/api/lesson/add",
                        		        
                        		        "/api/lesson/delete",
                        		        
                        		         
                        		          "/api/enrollment/course")
                                        .hasAuthority("INSTRUCTOR")
                                        
                        .requestMatchers("/api/enrollment/student",
                        		 "/api/enrollemt/enrollment-date"
                        		
                        		 )
                        		
                        		
                                         .hasAuthority("STUDENT")	
                        		
                        .requestMatchers("/api/course/courseDetails",
                        		          "/api/course/get",
                        		           "/api/course/verifyInstructor",
                        		           "/api/course/verifyCourse",
                        		           "/api/course/getInstructorId",
                        		          
                        		           
                        		           "/api/enrollment/create",
                        		           "/api/lesson/update",
                           		      
                        		           
                             		        
                           		        "/api/course/instructorId",
                        		           
                        		           "/api/enrollment/getAll",
                        		           "/api/enrollment/verifyEnrollment",
                        		           "/api/enrollment/studentList",
                        		          
                          		          "/api/enrollment/studentList/**",
                          		          
                          		          "/api/forum/send",
                          		        
                          		          "api/user/checkInstructor",
                          		        "api/user/checkStudent",
                        		
                        		            "api/lesson/getAll",
                        		            "api/lesson/view")
                        .permitAll()
                        
                        .anyRequest().authenticated())
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();

    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000")); // Allow requests from React app
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "X-Requested-With", "Accept"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // Apply CORS to all endpoints
        return source;
    }
}





