package dev.mvc.team3;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import dev.mvc.contents.Contents;
import dev.mvc.survey.Survey;
import dev.mvc.tool.Tool;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer{
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Windows: path = "C:/kd/deploy/resort/contents/storage";
        // ▶ file:///C:/kd/deploy/resort/contents/storage
      
        // Ubuntu: path = "/home/ubuntu/deploy/resort/contents/storage";
        // ▶ file:////home/ubuntu/deploy/resort/contents/storage
      
        // C:/kd/deploy/resort/contents/storage -> /contents/storage -> http://localhost:9091/contents/storage";
        registry.addResourceHandler("/survey/storage/**").addResourceLocations("file:///" +  Survey.getUploadDir());
        
        // C:/kd/deploy/resort/food/storage -> /food/storage -> http://localhost:9091/food/storage";
        registry.addResourceHandler("/contents/storage/**").addResourceLocations("file:///" +  Contents.getUploadDir());
        
        // C:/kd/deploy/resort/trip/storage -> /trip/storage -> http://localhost:9091/trip/storage";
        // registry.addResourceHandler("/trip/storage/**").addResourceLocations("file:///" +  Trip.getUploadDir());
    }
 
}