package tasksystem.com.example.demo.service;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
// 1. 告訴 Spring 這是一個業務服務層組件
import org.springframework.stereotype.Service;

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
    //方法
    //創建任務
    public Task createTask(Task task){
        task.setDeleted(false);//新增的任務一定要是未被軟刪除的
        return taskRepository.save(task);
        //儲存
    }
    //查找任務
    public List<Task> foundUserTasks(Long userId){
        return taskRepository.findByUserIdAndIsDeletedFalse(userId);
    }
    //更新任務
    public Task updateTask(Task task){
        //查找、確認任務是否被刪除
        Task existingTask=taskRepository.findById(task.getId()).orElse(null);
        if(existingTask==null||existingTask.isDeleted()){
            return null;
        }
        //顯示可更改的部分
        existingTask.setTitle(task.getTitle());
        existingTask.setDescription(task.getDescription());
        existingTask.setCategory(task.getCategory());
        existingTask.setLimited(task.getLimited());
        return taskRepository.save(existingTask);
    }

    //軟刪除任務
    public Task softDeleteTask(Task task){
        Task taskToDel=taskRepository.findById(task.getId()).orElse(null);
         if(taskToDel==null||taskToDel.isDeleted()){
            return null;
        }
        taskToDel.setDeleted(true);
        return taskRepository.save(taskToDel);
    }
    
}
