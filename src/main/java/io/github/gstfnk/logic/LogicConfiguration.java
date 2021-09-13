package io.github.gstfnk.logic;

import io.github.gstfnk.configuration.TaskConfiguration;
import io.github.gstfnk.model.ProjectRepository;
import io.github.gstfnk.model.TaskGroupRepository;
import io.github.gstfnk.model.TaskRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LogicConfiguration {
    @Bean
    ProjectService projectService(
            final ProjectRepository repository,
            final TaskGroupRepository taskGroupRepository,
            final TaskConfiguration configuration
    ) {
        return new ProjectService(repository, taskGroupRepository, configuration);
    }

    @Bean
    TaskGroupService taskGroupService(
            final TaskGroupRepository repository,
            final TaskRepository taskRepository
    ) {
        return new TaskGroupService(repository, taskRepository);
    }
}
