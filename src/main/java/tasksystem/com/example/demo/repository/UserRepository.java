package tasksystem.com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import tasksystem.com.example.demo.Entity.User;

public interface UserRepository extends JpaRepository<User,Long> {
    // 根據帳號查找用戶（用於登入/註冊時檢查帳號是否已存在）
    // Spring Data JPA 會自動根據方法名稱解析出 SQL 查詢：
    // SELECT * FROM user_table WHERE account = ?
    Optional<User> findByAccount(String account);
    
}




