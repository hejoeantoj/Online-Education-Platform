package com.cts.assignmentmodule.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
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
                .csrf(customizer -> customizer.disable())
                .csrf(customizer -> customizer.disable()).
                authorizeHttpRequests(request -> request
                		 .requestMatchers("/api/assignment/create",
                 		                "/api/assignment/delete",
                 		                
                 		               "/api/asubmission/assignMarks",
                 		              "/api/asubmission/viewByCourseIdandAssignmentId",
                 		             "/api/asubmission/viewFiles",
                 		               
                				         "/api/asubmission/viewAll") 
                                      .hasAuthority("INSTRUCTOR")
                                 
                 .requestMatchers("/api/asubmission/upload",
                 		         "/api/enrollment/delete",
                                "api/asubmission/verifySubmission"
                                
                  	          
                                )
                 		
                                  .hasAuthority("STUDENT")	
                 		
                 .requestMatchers("/api/assignment/view",
                		          "/api/course/verifyCourse",
                		          "/api/enrollment/verifyEnrollment",
                		          "/api/course/verifyInstructor",
                		          "/api/assignment/viewById",
                		          "api/assignment/AssignmentDetails",
                		          
                		          
                		          
                	
                		          
                		         "api/asubmission/assignmentReport", 
         				         
         				        
        				          
        				          
                		        
                		          "/api/asubmission/AssignmentSubmissionDetails")
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



