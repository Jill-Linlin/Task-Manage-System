package tasksystem.com.example.demo.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import tasksystem.com.example.demo.entity.User;
import tasksystem.com.example.demo.payload.JwtAuthenticationResponse;
import tasksystem.com.example.demo.security.JwtTokenProvider;
import tasksystem.com.example.demo.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMethod;





@RestController
@RequestMapping("api/users")
@CrossOrigin(origins = "http://localhost:5173")
public class UserController {
    //屬性
    private final UserService userservice;
    private final JwtTokenProvider tokenProvider;
    private final AuthenticationManager authenticationManager;
    //建構式
    @Autowired
    public UserController(UserService userservice,JwtTokenProvider tokenProvider,AuthenticationManager authenticationManager){
        this.userservice=userservice;
        this.tokenProvider=tokenProvider;
        this.authenticationManager=authenticationManager;
    }
    @Autowired
    private PasswordEncoder passwordEncoder;

    //方法
    //實作註冊API
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody User user) {
        User registerUser=userservice.registUser(user);
        if(registerUser==null){
            return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body("{\"message\":\"此帳號已經存在，請使用其他帳號註冊。\"}");

        }

        return ResponseEntity.status(HttpStatus.CREATED).body(registerUser);

        
    }
    

    //實作登入API
    @PostMapping("/login")
    public ResponseEntity loginUser(@RequestBody User user) {
        try{
            //使用 AuthenticationManager 進行認證
            Authentication authentication=authenticationManager.authenticate(
                // 傳遞使用者名稱（帳號）和密碼
                new UsernamePasswordAuthenticationToken(user.getAccount(), user.getPassword())
            );
            // 2. 將認證結果設置到 Spring Security 上下文中
            SecurityContextHolder.getContext().setAuthentication(authentication);
            // 3. 從認證結果中獲取使用者的 ID
            // UserDetails 實體就是您的 User 實體，我們可以直接轉換
            User userDetails=(User)authentication.getPrincipal();
            // 4. 使用 JwtTokenProvider 生成 Token
            String jwt=tokenProvider.generateToken(userDetails.getId());
            // 5. 返回包含 JWT 的 DTO，狀態碼使用 200 OK
            return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));

        }catch(Exception ex){
            return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body("{\"Message\":\"Invaild account or password.\"");
        }
    }

    //查找User
    @GetMapping("/finduser")
    public ResponseEntity<?> findUser(@RequestParam String account) {
        User user=userservice.findByAccount(account);
        if(user==null){
            return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body("{\"message\":\"此帳號不存在。\"}");
        }else{
            return ResponseEntity
                .status(HttpStatus.OK)
                .body(user);
        }
    }
    @GetMapping("/userdata")
    public ResponseEntity<?> getuserdata(@AuthenticationPrincipal UserDetails userDetails) {
        String username=userDetails.getUsername();
        User user=userservice.findByAccount(username);
        if(user==null){
            return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body("{\"message\":\"此帳號不存在。\"}");
        }else{
            return ResponseEntity
                .status(HttpStatus.OK)
                .body(user);
        }
    }
    
    

    //更新User
    @PutMapping("/update")
    public ResponseEntity updateUser(@AuthenticationPrincipal UserDetails userDetails, @RequestBody User user) {
        User userToUpdate=userservice.updateUser(userDetails.getUsername(),user,passwordEncoder);
        if(userToUpdate==null){
            return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body("{\"message\":\"找不到此帳號資料\"}");
        }else{
            return ResponseEntity
                .status(HttpStatus.OK)
                .body(userToUpdate);
        }
    }

    //刪除user
    @PutMapping("/delete/{userid}")
    public ResponseEntity deleteUser(@RequestBody User user) {
        Boolean userToDel=userservice.deleteUser(user);
        if (userToDel==true) {
            return ResponseEntity
                .status(HttpStatus.OK)
                .body("{\"message\":\"刪除成功\"}"); 
        }else{
            return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body("{\"message\":\"找不到此帳號資料\"}");
        }
        
    }
    
    


    
}
