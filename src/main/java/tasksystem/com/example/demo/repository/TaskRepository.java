package tasksystem.com.example.demo.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tasksystem.com.example.demo.Entity.Task;

public interface TaskRepository extends JpaRepository<Task, Long> {
    
    @Query("SELECT t FROM Task t WHERE t.user.id = :userId AND t.isDeleted = false")
    List<Task> findByUserIdAndIsDeletedFalse(@Param("userId") Long userId);
}