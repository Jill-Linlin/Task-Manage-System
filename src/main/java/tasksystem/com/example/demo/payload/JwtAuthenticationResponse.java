package tasksystem.com.example.demo.payload;

import lombok.Data;

@Data
public class JwtAuthenticationResponse {
    private String accessToken;
    private String tokenType="Bearer";

    public JwtAuthenticationResponse
    
}
