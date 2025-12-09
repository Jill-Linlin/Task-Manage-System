package tasksystem.com.example.demo.controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tasksystem.com.example.demo.entity.Task;
import tasksystem.com.example.demo.entity.User;
import tasksystem.com.example.demo.service.TaskService;
import tasksystem.com.example.demo.service.UserService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;




@RestController
@RequestMapping("api/tasks")
public class TaskController {
    //屬性
    private final TaskService taskservice;
    private final UserService userservice;
    //建構性
    @Autowired
    public TaskController(TaskService taskservice,UserService userservice){
        this.taskservice=taskservice;
        this.userservice=userservice;
    }
    //方法
    //創建任務
    @PostMapping("/create")
    public ResponseEntity<?> createTask(@RequestBody Task task) {
        Task createTask=taskservice.createTask(task);
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(createTask);
        
    }
    //查找任務
    @GetMapping("/check")
    public ResponseEntity<?> checkTask(@RequestParam Long userId) {
        List<Task> taskList=taskservice.foundUserTasks(userId);
        return ResponseEntity
            .status(HttpStatus.OK)//200
            .body(taskList);
    }
    
    //更新任務
    @PutMapping("/updatetask")
    public ResponseEntity<?> updateTask(@RequestBody Task task) {
        Task updateTask=taskservice.updateTask(task);
        if(updateTask==null){
            return ResponseEntity
                .status(HttpStatus.NOT_FOUND) //404
                .body("{\"message\":\"Database didn't find this task or be deleted\"");
        }
        return ResponseEntity
                .status(HttpStatus.OK)//201
                .body(updateTask);
    }
    //刪除任務
    @DeleteMapping("/{taskId}")
    public ResponseEntity<?> deleteTask(@PathVariable Long taskId){
        //創建只有ID的物件
        Task taskToDel=new Task();
        //物件傳給service
        taskToDel.setId(taskId);//此物件不會被儲存只是為了id而暫存的物件

        //呼叫service進行軟刪除
        Task deletTask=taskservice.softDeleteTask(taskToDel);
        
        //刪除失敗(任務不存在)
        if(deletTask==null){
            return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body("{\"message\":\"Not Found\"");
        }else{
             //刪除成功
            return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();//主體是空的
        }

       
        
    }

    
    

}
