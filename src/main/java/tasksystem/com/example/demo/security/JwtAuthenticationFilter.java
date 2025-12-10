package tasksystem.com.example.demo.security;

import java.io.IOException;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import tasksystem.com.example.demo.service.UserService;

@Component
//繼承OncePerRequestFilter 確保每個請求只會經過此過濾器一次
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtTokenProvider tokenProvider;
    private final UserService userService;
    
    @Autowired
    public JwtAuthenticationFilter(JwtTokenProvider tokenProvider,UserService userService){
        this.tokenProvider=tokenProvider;
        this.userService=userService;//用來加載使用者數據
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,HttpServletResponse response,FilterChain filterChain)throws ServletException,IOException{
        try {
            //1.嘗試從請求header中獲取JWT令牌
            String jwt = getJwtFromRequest(request);
            //2.檢查Token是否存在且有效
            if(jwt != null && tokenProvider.vaildateToken(jwt)){
                //3.從token中取出userId
                Long userId=tokenProvider.getUserIdFromToken(jwt);
                //4.使用userservice根據userid加載spring security要求的userdetails
                UserDetails userDetails=userService.loadUserById(userId);
                //5.創建一個認證物件(通行證)
                //參數:使用者資料.憑證.權限
                UsernamePasswordAuthenticationToken authentication=new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                //6.設定認證細節
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                //7.請求通過放回sping security
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }catch(Exception ex){
            //異常通知
            logger.error("Could not set user authentication in security context",ex);
        }
         filterChain.doFilter(request, response);

       
    }
    private String getJwtFromRequest(HttpServletRequest request){
        String bearerToken = request.getHeader("Authorization");
        if(bearerToken != null && bearerToken.startsWith("Bearer ")){
            return bearerToken.substring(7);
        }
        return null;
    }
}
