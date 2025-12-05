package tasksystem.com.example.demo.Entity;
import java.time.LocalDate;

import jakarta.persistence.*;

@Entity
@Table(name="task_table")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name="description")
    private String description;

    @Column(name="category")
    private String category;

    @Column(name="completed")
    private Boolean completed;

    // 改成駝峰式命名
    @Column(name="isdeleted")  // 資料庫欄位名稱保持不變
    private Boolean isDeleted;  // Java 屬性改成駝峰式

    @Column(name="limited")
    private LocalDate limited;
    
    @ManyToOne
    @JoinColumn(name="u_id")
    private User user;

    public Task(){}

    public Long getId(){
        return id;
    }
    
    public String getTitle(){
        return title;
    }
    
    public void setTitle(String title){
        this.title = title;
    }
    
    public String getDescription(){
        return description;
    }
    
    public void setDescription(String description){
        this.description = description;
    }
    
    public String getCategory(){
        return category;
    }
    
    public void setCategory(String category){
        this.category = category;
    }
    
    public Boolean isCompleted() {
        return completed;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }
    
    // 修改這裡：統一使用 isDeleted
    public Boolean isDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public LocalDate getLimited(){
        return limited;
    }
    
    public void setLimited(LocalDate limited){
        this.limited = limited;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}