package tasksystem.com.example.demo.service;
import org.springframework.security.access.AccessDeniedException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import tasksystem.com.example.demo.entity.Task;
import tasksystem.com.example.demo.repository.TaskRepository;
@Service
public class TaskService {
    //屬性
    private final TaskRepository taskRepository;
    //建構式
    @Autowired
    public TaskService(TaskRepository taskRepository){
        this.taskRepository=taskRepository;
    }
    //共用方法基本檢查
    private Task taskFindAndValidate(Long taskId,Long userId){
        //任務ID查找
        Task task=taskRepository.findById(taskId).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Not found task by taskId: "+taskId));
         //軟刪除
        if(task.isDeleted()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"the task is deleted task id : "+taskId);
        }
         //權限檢查
        if(!task.getUserID().equals(userId)){
            throw new AccessDeniedException("Identify access denied.");
        }
        return task;

       
    }
 

    


    //創建任務
    
    public Task createTask(Task task,Long userId){
        task.setUserId(userId);
        task.setDeleted(false);//新增的任務一定要是未被軟刪除的
        return taskRepository.save(task);
        //儲存
    }
    //查找任務
    public List<Task> foundUserTasks(Long userId){
        return taskRepository.findByUserIdAndIsDeletedFalse(userId);
    }
    public Task getByTaskId(Long taskId,Long userId){
        return taskFindAndValidate(taskId, userId);
    }
    //更新任務
 public Task updateTask(Long taskId, Long userId, Task updatedTask){
    // 1. 查找並驗證任務存在且屬於該用戶
    Task existingTask = taskFindAndValidate(taskId, userId);
    
    // 2. 更新可修改的欄位（用前端傳來的新資料）
    existingTask.setTitle(updatedTask.getTitle());
    existingTask.setDescription(updatedTask.getDescription());
    existingTask.setCategory(updatedTask.getCategory());
    existingTask.setLimited(updatedTask.getLimited());
    
    // 3. 儲存並返回
    return taskRepository.save(existingTask);
}

    //軟刪除任務
    public Task softDeleteTask(Task task,Long userId){
        Task taskToDel=taskFindAndValidate(task.getId(), userId);
        taskToDel.setDeleted(true);
        return taskRepository.save(taskToDel);
    }
    
}