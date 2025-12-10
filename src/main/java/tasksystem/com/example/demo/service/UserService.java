package tasksystem.com.example.demo.service;
import java.util.Optional;

// 1. 告訴 Spring 這是一個業務服務層組件
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import tasksystem.com.example.demo.entity.User;
import tasksystem.com.example.demo.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Service
public class UserService implements UserDetailsService {

    //屬性
    private final UserRepository userRepository; //設定一個資料型態為UserRepository的變數(名稱 userRepository)
    private final BCryptPasswordEncoder passwordEncoder;
    //建構式
    @Autowired
    public UserService (UserRepository userRepository,BCryptPasswordEncoder passwordEncoder)
    {
        this.userRepository=userRepository;
        this.passwordEncoder=passwordEncoder;
    }

    //register method 回傳的資料型態會是User 

    public User registUser(User user){
        Optional<User> existingUser = userRepository.findByAccount(user.getAccount());//取的使用者的帳號並對照資料庫是否有此帳號
        if (existingUser.isPresent()) {
            return null;
        }//回傳null則表示已存在此帳號
        String hashedPassword=passwordEncoder.encode(user.getPassword());
        //取得使用者輸入的密碼後進行雜湊加密
        user.setPassword(hashedPassword);
        //將加密後的密碼放進去資料庫

        return userRepository.save(user);
        //存檔
    }

    //登入
    public User loginUser(String Account,String Password){
        Optional<User> foundUser = userRepository.findByAccount(Account);
        if (foundUser.isEmpty()){
            return null;
        }//回傳null表示資料庫無使用者
        User user=foundUser.get();//利用使用者帳號找到實體
        //對照密碼(利用)
        if (passwordEncoder.matches(Password, user.getPassword())){
            return user;
        }
        else{
            return null;
        }
        //回傳結果 
    }

    //查找使用者.
    public User findByAccount(String Account){
        Optional<User> foundUser = userRepository.findByAccount(Account);
        if(foundUser.isPresent()){
            User user=foundUser.get();
            return user;
        }else{
            return null;
        }
    }
    //編輯使用者資料
    public User updateUser(User user){
        User userToUpdate=userRepository.findById(user.getId()).orElse(null);
        if(userToUpdate==null){
            return null;
        }
        userToUpdate.setBirth(user.getBirth());
        userToUpdate.setName(user.getName());
        userToUpdate.setSex(user.getSex());

        return userRepository.save(userToUpdate);
    }
    //刪除使用者資料
    public boolean deleteUser(User user){
        User userToDel=userRepository.findById(user.getId()).orElse(null);
        if(userToDel==null){
            return false;
        }
        userRepository.delete(userToDel);
         
        return true;
    }
    // 【新增/修改】 核心方法：供 Spring Security 根據帳號名稱查找使用者
    @Override
    public UserDetails loadUserByUsername(String account) throws UsernameNotFoundException{
        return userRepository.findByAccount(account)
                            .orElseThrow(() ->
                                new UsernameNotFoundException("not found user account "+account));
    }
    // 【新增】 供 JWT Filter 根據 ID 查找使用者（JWT 中只存 ID）
    public UserDetails loadUserById(Long id)throws UsernameNotFoundException{
        return userRepository.findById(id)
                .orElseThrow(() ->
                    new UsernameNotFoundException("not found user id "+ id));
    }

    
    
}
