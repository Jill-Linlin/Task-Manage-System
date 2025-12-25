package tasksystem.com.example.demo.entity;
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

    @Column(name="user_id", nullable = false)
    private Long userId;

    // @ManyToOne
    // @JoinColumn(name="u_id")
    // private User user;
    /*
 * 💡【安全與性能考量：JWT 專用外鍵】
 * * 採用 Long userId 而非 User user 映射 (ManyToOne) 的原因：
 * * 1. 性能優先：避免 JPA 的 N+1 查詢問題。在創建/查詢 Task 時，
 * 不需要額外執行 SELECT 語句來載入完整的 User 物件，直接操作 ID 速度更快。
 * 2. 安全簡潔：JWT 認證流程中，Security Context Holder 直接提供 Long userId。
 * 使用 Long 類型可直接綁定，是處理權限驗證和數據隔離的最佳選擇。
 * 3. 數據隔離：用於 TaskService 中，檢查 Task.userId 是否等於當前登入者 ID，
 * 確保每個用戶只能存取自己的數據。
 */
    public Task(){}

     public void setId(Long taskId) {
        this.id=taskId;
    }

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

    public void setDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public LocalDate getLimited(){
        return limited;
    }
    
    public void setLimited(LocalDate limited){
        this.limited = limited;
    }

    // public User getUser() {
    //     return user;
    // }

    // public void setUser(User user) {
    //     this.user = user;
    // }
    public Long getUserID(){
        return userId;
    }
    public void setUserId(Long userId){
        this.userId=userId;
    }
   
}