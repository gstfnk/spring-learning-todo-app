package io.github.gstfnk.logic;

import io.github.gstfnk.configuration.TaskConfiguration;
import io.github.gstfnk.model.ProjectRepository;
import io.github.gstfnk.model.TaskGroup;
import io.github.gstfnk.model.TaskGroupRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.Assertions.in;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.*;


class ProjectServiceTest {
    @Test
    @DisplayName("should throw IllegalStateException when configured to allow just 1 group and the other undone group exists")
    void createGroup_noMultipleGroupsConfig_and_undoneGroupExists_throwsIllegalStateException() {
        //  given
        var mockGroupRepository = groupRepositoryReturning(true);
        //  and
        TaskConfiguration mockConfiguration = configurationReturning(false);
        //  system under test
        var toTest = new ProjectService(null, mockGroupRepository, mockConfiguration);
        //  when
        var exception = catchThrowable(() -> toTest.createGroup(LocalDateTime.now(), 0));
        //  then
        assertThat(exception)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("one undone group");
    }

    @Test
    @DisplayName("should throw IllegalArgumentException when configuration ok and no projects for a given id")
    void createGroup_configurationOk_and_noProjects_throwsIllegalArgumentException() {
        //  given
        var mockRepository = mock(ProjectRepository.class);
        when(mockRepository.findById(anyInt())).thenReturn(Optional.empty());
        //  and
        TaskConfiguration mockConfiguration = configurationReturning(true);
        //  system under test
        var toTest = new ProjectService(mockRepository, null, mockConfiguration);
        //  when
        var exception = catchThrowable(() -> toTest.createGroup(LocalDateTime.now(), 0));
        //  then
        assertThat(exception)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("id not found");
    }

    @Test
    @DisplayName("should throw IllegalArgumentException when configured to allow just 1 group and the other undone group not exists")
    void createGroup_noMultipleGroupsConfig_and_noUndoneGroupExists_throwsIllegalArgumentException() {
        //  given
        var mockGroupRepository = groupRepositoryReturning(false);
        //  and
        var mockRepository = mock(ProjectRepository.class);
        when(mockRepository.findById(anyInt())).thenReturn(Optional.empty());
        //  and
        TaskConfiguration mockConfiguration = configurationReturning(true);
        //  system under test
        var toTest = new ProjectService(mockRepository, null, mockConfiguration);
        //  when
        var exception = catchThrowable(() -> toTest.createGroup(LocalDateTime.now(), 0));
        //  then
        assertThat(exception)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("id not found");
    }

    @Test
    @DisplayName("should create a new group from project")
    void createGroup_configurationOk_existingProject_createsAndSavesGroup() {
        //  given
        var mockRepository = mock(ProjectRepository.class);
        when(mockRepository.findById(anyInt())).thenReturn(Optional.empty());
        //  and
        TaskGroupRepository inMemoryGroupRepository = inMemoryGroupRepository();
        //  and
        TaskConfiguration mockConfiguration = configurationReturning(true);

    }

    private TaskGroupRepository inMemoryGroupRepository() {
        return new TaskGroupRepository() {
            private int index = 0;
            private final Map<Integer, TaskGroup> map = new HashMap<>();

            @Override
            public List<TaskGroup> findAll() {
                return new ArrayList<>(map.values());
            }

            @Override
            public Optional<TaskGroup> findById(Integer id) {
                return Optional.ofNullable(map.get(id));
            }

            @Override
            public boolean existsByDoneIsFalseAndProject_Id(Integer projectId) {
                return map.values().stream()
                        .filter(taskGroup -> !taskGroup.isDone())
                        .anyMatch(taskGroup -> taskGroup.getProject() != null && taskGroup.getProject().getId() == projectId);
            }

            @Override
            public TaskGroup save(TaskGroup entity) {
                if (entity.getId() == 0) {
                    try {
                        TaskGroup.class.getDeclaredField("id").set(entity, ++index);
                    } catch (NoSuchFieldException | IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                }
                map.put(entity.getId(), entity);
                return entity;
            }
        };
    }

    private TaskGroupRepository groupRepositoryReturning(boolean b) {
        var mockGroupRepository = mock(TaskGroupRepository.class);
        when(mockGroupRepository.existsByDoneIsFalseAndProject_Id(anyInt())).thenReturn(b);

        return mockGroupRepository;
    }

    private TaskConfiguration configurationReturning(boolean b) {
        //  given
        var mockTemplate = mock(TaskConfiguration.Template.class);
        when(mockTemplate.isAllowMultipleTasks()).thenReturn(b);
        //  and
        var mockConfiguration = mock(TaskConfiguration.class);
        when(mockConfiguration.getTemplate()).thenReturn(mockTemplate);

        return mockConfiguration;
    }
}