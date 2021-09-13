package io.github.gstfnk.logic;

import io.github.gstfnk.configuration.TaskConfiguration;
import io.github.gstfnk.model.*;
import io.github.gstfnk.model.projection.GroupReadModel;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class ProjectService {
    private final ProjectRepository repository;
    private final TaskGroupRepository taskGroupRepository;
    private final TaskConfiguration taskConfiguration;

    public ProjectService(ProjectRepository repository, TaskGroupRepository taskGroupRepository, TaskConfiguration taskConfiguration) {
        this.repository = repository;
        this.taskGroupRepository = taskGroupRepository;
        this.taskConfiguration = taskConfiguration;
    }

    public List<Project> readAll() {
        return repository.findAll();
    }

    public Project save(Project toSave) {
        return repository.save(toSave);
    }

    public GroupReadModel createGroup(LocalDateTime deadline, int projectId) {
        if (!taskConfiguration.getTemplate().isAllowMultipleTasks() &&
                taskGroupRepository.existsByDoneIsFalseAndProject_Id(projectId)) {
            throw new IllegalStateException("Only one undone group from project is allowed!");
        }
        TaskGroup result = repository.findById(projectId)
                .map(project -> {
                    var targetGroup = new TaskGroup();
                    targetGroup.setDescription(project.getDescription());
                    targetGroup.setTasks(
                            project.getSteps().stream()
                                    .map(projectStep -> new Task(
                                            projectStep.getDescription(),
                                            deadline.plusDays(projectStep.getDaysToDeadline())))
                                    .collect(Collectors.toSet())
                    );
                    targetGroup.setProject(project);
                    return taskGroupRepository.save(targetGroup);
                }).orElseThrow(
                        () -> new IllegalArgumentException("Project with given id not found!"));
        return new GroupReadModel(result);
    }
}
