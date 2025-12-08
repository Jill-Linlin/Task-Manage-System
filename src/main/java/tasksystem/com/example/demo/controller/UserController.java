package tasksystem.com.example.demo.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tasksystem.com.example.demo.entity.User;
import tasksystem.com.example.demo.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("api/users")
public class UserController {
    //屬性
    private final UserService userservice;
    //建構式
    @Autowired
    public UserController(UserService userservice){
        this.userservice=userservice;
    }
    //方法
    //實作註冊API
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        User registerUser=userservice.registUser(user);
        if(registerUser==null){
            return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body("{\"message:\"\"此帳號已經存在，請使用其他帳號註冊。\"}");

        }else{
            return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(registerUser);

        }
    }
    

    //實作登入API
    @PostMapping("/login")
    public ResponseEntity loginUser(@RequestBody User user) {
        User loginUser=userservice.loginUser(user.getAccount(), user.getPassword());
        if (loginUser==null) {
            return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body("{\"message:\"\"此帳號不存在，或密碼輸入錯誤。\"}");
            
        }else{
            return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(loginUser);

        }
    }
    


    
}
