package dev.mvc.team3;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import dev.mvc.tool.Security;

// 환경 설정 클래스로 자동 실행되어 객체를 생성한후 이용됨.
@Configuration
public class SecurityConfig {
  @Bean
  Security security() {
    return new Security();
  }
  
}