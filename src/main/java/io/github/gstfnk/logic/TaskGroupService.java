package io.github.gstfnk.logic;

import io.github.gstfnk.model.TaskGroup;
import io.github.gstfnk.model.TaskGroupRepository;
import io.github.gstfnk.model.TaskRepository;
import io.github.gstfnk.model.projection.GroupReadModel;
import io.github.gstfnk.model.projection.GroupWriteModel;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequestScope
public class TaskGroupService {
    private final TaskGroupRepository repository;
    private final TaskRepository taskRepository;

    public TaskGroupService(TaskGroupRepository repository,
                            TaskRepository taskRepository) {
        this.repository = repository;
        this.taskRepository = taskRepository;
    }

    public GroupReadModel createGroup(GroupWriteModel source) {
        TaskGroup result = repository.save(source.toGroup());
        return new GroupReadModel(result);
    }

    public List<GroupReadModel> readAll() {
        return repository.findAll()
                .stream()
                .map(GroupReadModel::new)
                .collect(Collectors.toList());
    }

    public void toggleGroup(int groupId) {
        if (taskRepository.existsByDoneIsFalseAndGroup_Id(groupId)) {
            throw new IllegalStateException("Group has undone tasks. Do all the tasks first!");
        }
        TaskGroup result = repository.findById(groupId).orElseThrow(() ->
                new IllegalArgumentException("TaskGroup with given id not found!"));
        result.setDone(!result.isDone());
    }
}
