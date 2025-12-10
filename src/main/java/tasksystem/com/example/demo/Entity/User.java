package tasksystem.com.example.demo.entity;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.*;

@Entity //JPA映射這是一個實體
@Table(name="user_table")//對應table名稱為user_table

public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)//會隨機生成一個遞增數字
    private Long id;//u-id

    @Column(name = "account",nullable = false,unique = true)//帳號獨一且不可為空
    private String account;

    @Column(name="password",nullable = false)//password之後要有hash
    private String password;

    @Column(name="name")//姓名
    private String name;

    @Column(name="sex")//性別
    private String sex;

    @Column(name="birth")//出生日期
    private LocalDate birth;

   @Column(name="created_at") //創建時間 
   private LocalDateTime createdAt;
   @PrePersist
    protected void onCreate() {
    this.createdAt = LocalDateTime.now();
    }

   public User(){}

   // ID
    public Long getId() {
        return id;
    }

    public void setId(Long id){
        this.id=id;
    }
    // Account
    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    // Password
    @Override 
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // Name
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Sex
    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    // Birth
    public LocalDate getBirth() {
        return birth;
    }

    public void setBirth(LocalDate birth) {
        this.birth = birth;
    }

    // CreatedAt
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override 
    public Collection<? extends GrantedAuthority> getAuthorities(){
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }
    //獲取權限
    @Override
    public String getUsername(){
        return account;
    }
    //獲取帳號，利用account欄位作為UserName

    @Override
    public boolean isAccountNonExpired(){
        return true;
    }

    @Override
    public boolean isAccountNonLocked(){
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired(){
        return true;
    }

    @Override
    public boolean isEnabled(){
        return true;
    }


}