package tasksystem.com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity // 啟用 Spring Security 的 Web 安全功能
public class SecurityConfig {

    // 1. 保留這個 Bean，供 UserService 注入
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 2. 配置安全過濾鏈：定義哪些 URL 需要保護，哪些是公開的
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        
        http
            .csrf(csrf -> csrf.disable()) // 禁用 CSRF (對 RESTful API 來說通常是必要的)
            .authorizeHttpRequests(authorize -> authorize
                // 允許任何人存取註冊和登入 API
                .requestMatchers("/api/users/register", "/api/users/login").permitAll()
                // 所有其他請求都必須經過身份驗證
                .anyRequest().authenticated()
            );
        
        // 由於我們還沒有實現登入機制，先將預設的表單登入也禁用
        http.httpBasic(basic -> basic.disable()); 
        
        return http.build();
    }
}