package tasksystem.com.example.demo.service;
import java.util.List;

import org.springframework.data.repository.Repository;
// 1. 告訴 Spring 這是一個業務服務層組件
import org.springframework.stereotype.Service;
import tasksystem.com.example.demo.Entity.Task;
import tasksystem.com.example.demo.Entity.User;
import tasksystem.com.example.demo.repository.TaskRepository;
@Service
public class TaskService {
    //屬性
    private final TaskRepository taskRepository;
    //建構式
    public TaskService(TaskRepository taskRepository){
        this.taskRepository=taskRepository;
    }
    //方法
    //創建任務
    public Task createTask(Task task){
        task.setIsDeleted(false);//新增的任務一定要是未被軟刪除的
        return taskRepository.save(task);
        //儲存
    }
    //查找任務
    public List<Task> foundUserTasks(User user){
        return taskRepository.findByUserIdAndIsDeletedFalse(user.getId());
    }
    
}
