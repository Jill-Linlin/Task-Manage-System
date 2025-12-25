package tasksystem.com.example.demo.controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tasksystem.com.example.demo.entity.Task;
import tasksystem.com.example.demo.entity.User;
import tasksystem.com.example.demo.service.TaskService;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;




@RestController
@RequestMapping("api/tasks")
public class TaskController {
    //屬性
    private final TaskService taskservice;
    
    //建構性
    @Autowired
    public TaskController(TaskService taskservice){
        this.taskservice=taskservice;
        
    }
    //方法
    //取得UserId方法
    private Long getCurrentUserId() {
        // 1. 從 SecurityContextHolder 獲取認證物件 (Authentication)
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        // 2. 檢查認證物件是否存在且其 Principal (主體) 是我們定義的 User 實體
        if (authentication != null && authentication.getPrincipal() instanceof User) {
            // 3. 類型轉換：將 Principal 轉為 User 實體
            User userDetails = (User) authentication.getPrincipal();
            
            // 4. 返回 User ID
            return userDetails.getId(); 
        }

        // 5. 如果無法獲取 ID，拋出錯誤，由 Spring Security 統一處理
        throw new AccessDeniedException("無法從安全上下文中獲取使用者 ID，請確認登入狀態。");
    }
    //創建任務
    @PostMapping("/create")
    public ResponseEntity<?> createTask(@RequestBody Task task) {
        Long userId=getCurrentUserId();
        Task createTask=taskservice.createTask(task,userId);
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(createTask);
        
    }
    //查找任務
    @GetMapping("/check")
    public ResponseEntity<?> checkTask() {
        Long userId=getCurrentUserId();
        List<Task> taskList=taskservice.foundUserTasks(userId);
        return ResponseEntity
            .status(HttpStatus.OK)//200
            .body(taskList);
    }
    
    //更新任務
   @PutMapping("/{taskId}")
    public ResponseEntity<Task> updateTask(
    @PathVariable Long taskId,
    @RequestBody Task updatedTask,  // 接收前端傳來的新資料
    @AuthenticationPrincipal User user) {
        Task updated = taskservice.updateTask(taskId, user.getId(), updatedTask);
        return ResponseEntity.ok(updated);
        }
    //刪除任務
    @DeleteMapping("/{taskId}")
    public ResponseEntity<?> deleteTask(@PathVariable Long taskId){
        Long userId=getCurrentUserId();
        //創建只有ID的物件
        Task taskToDel=new Task();
        //物件傳給service
        taskToDel.setId(taskId);//此物件不會被儲存只是為了id而暫存的物件
        //呼叫service進行軟刪除
        taskservice.softDeleteTask(taskToDel,userId);
             //刪除成功
        return ResponseEntity
            .status(HttpStatus.NO_CONTENT)
            .build();//主體是空的

       
        
    }

    
    

}
