package tasksystem.com.example.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import tasksystem.com.example.demo.security.JwtAuthenticationFilter;

@Configuration
@EnableWebSecurity // 啟用 Spring Security 的 Web 安全功能
public class SecurityConfig {

    //step C才會實作Filter
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    @Autowired
    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter){
        this.jwtAuthenticationFilter=jwtAuthenticationFilter;
    }

    // 1. 保留這個 Bean，供 UserService 注入
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    // 2. 認證管理器 Bean (AuthenticationManager)
    // 這是執行使用者登入（驗證使用者名稱/密碼）的核心組件。
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception{
        return authenticationConfiguration.getAuthenticationManager();
    }

    // 3. 配置安全過濾鏈：定義哪些 URL 需要保護，哪些是公開的
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        
        http
            .csrf(csrf -> csrf.disable()) // // 禁用 CSRF：因為我們是 RESTful API 並使用 JWT，所以不需要 CSRF 保護

            // 設定 Session 管理策略為 STATELESS (無狀態)
            // 告訴 Spring Security 不要使用 Session，因為 JWT 已經包含了所有狀態資訊
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .authorizeHttpRequests(authorize -> authorize
                // 允許任何人存取註冊和登入 API
                .requestMatchers("/api/users/register", "/api/users/login").permitAll()
                // 所有其他請求都必須經過身份驗證
                .anyRequest().authenticated()
            )
            // 禁用預設的表單登入和 HTTP Basic 認證，因為我們使用 JWT
            .formLogin(form -> form.disable())
            .httpBasic(basic -> basic.disable());
       // 將我們自定義的 JWT 過濾器添加到 Spring Security 的過濾器鏈中
        // 確保在 Spring 嘗試進行使用者名稱/密碼認證之前，先執行 JWT 驗證
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        
        return http.build();
    }
}
