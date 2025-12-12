package tasksystem.com.example.demo.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;


import tasksystem.com.example.demo.entity.Task;

public interface TaskRepository extends JpaRepository<Task, Long> {
    
    List<Task> findByUserIdAndIsDeletedFalse(Long userId);

    List<Task> findByUserId(Long userId);
}
/*
     * 💡【查詢方法選擇：方法命名慣例 (Method Name Convention)】
     * * 1. 簡潔性：Spring Data JPA 會自動解析方法名：
     * -> 翻譯為 WHERE userId = ?1 AND isDeleted = false
     * 2. 可維護性：如果實體屬性名稱變動，IDE 會提醒修改此方法名，比手動維護 JPQL 字串更安全。
     * * @param userId 當前登入者的 ID
     * @return 該使用者未刪除的任務列表
     */