package com.sentrypay.backend.config;



import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


// declare configuration class to enable CORS for all origins and methods in the application
// this is used to communicate with kotlin frontend without CORS issues

@Configuration

// implement WebMvcConfigurer to override addCorsMappings method
public class ConfigKotlin implements WebMvcConfigurer{


    // define addCorsMappings method to allow CORS for all origins and methods
    @Override
    public void addCorsMappings(CorsRegistry registry){
        registry.addMapping("/**")
        .allowedOrigins("*")
        .allowedMethods("GET" , "POST" , "PUT" , "DELETE" , "OPTIONS" ); 
    }
    
}
