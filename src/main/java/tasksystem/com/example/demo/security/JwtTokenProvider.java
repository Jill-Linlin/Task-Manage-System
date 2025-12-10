package tasksystem.com.example.demo.security;

import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.*;

@Component
public class JwtTokenProvider {
    //對應application.properties 
    @Value("${app.jwt.secret}")
    private String jwtSecret; 
    //密鑰字串
    @Value("${app.jwt.expiration-ms}")
    private int jwtExpirationMs;
    //讀取有效期(ms)

    //Key物件是加密/解密的核心
    private Key getSigningKey(){
        //使用Keys.hmashaKeyFor根據密鑰字串生成HS512要求的Key 
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    //生成Jwt令牌
    public String generateToken(Long userId){
        Date now =new Date();//建立Date物件

        Date expireDate = new Date(now.getTime()+jwtExpirationMs);//計算Token過期時間

        Key key =getSigningKey();//獲取加密密鑰

        return Jwts.builder()
                .setSubject(Long.toString(userId))//設置token的主體是使用者的id
                .setIssuedAt(now)//設置token發行時間
                .setExpiration(expireDate)//設置token過期時間
                .signWith(key,SignatureAlgorithm.HS512)//使用密鑰和HS512算法簽名
                .compact();//建構並壓縮成字串
    }
    //從token中獲取使用者ID
    public Long getUserIdFromToken(String token){
        Key key =getSigningKey();
        //解析token並獲取claims
        Claims claims=Jwts.parserBuilder()
                .setSigningKey(key)//驗證Token的簽名是否正確
                .build()
                .parseClaimsJws(token)//解析JWT
                .getBody();//獲取主體內容
        return Long.parseLong(claims.getSubject());//claims.getSubjext()返回的是我們存入的syring格式的userId
    }

    //驗證Token是否有效
    public boolean vaildateToken(String token){
        try{
            Key key =getSigningKey();
            //嘗試解析 TOKEN 如果成功則代表有效
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        }catch(SignatureException ex){
            //密碼不對
            System.err.println("Invalid JWT signature");
        }catch(MalformedJwtException ex){
            //JWT格式不對
            System.err.println("Invaild JWT token");
        }catch(ExpiredJwtException ex){
            //JWT 已過期
            System.err.println("Expired JWT token");
        }catch(UnsupportedJwtException ex){
            //不支援的jwt
            System.err.println("Unsupported Jwt token");
        }catch(IllegalArgumentException ex){
            //JWT字符為空
            System.err.println("Jwt claims string is empty.");
        }
        return false;

    }
    

    

}
