package io.github.gstfnk.logic;

import io.github.gstfnk.model.TaskGroup;
import io.github.gstfnk.model.TaskGroupRepository;
import io.github.gstfnk.model.TaskRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TaskGroupServiceTest {
    @Test
    @DisplayName("should return throwsIllegalStateException when undone tasks")
    void toggleGroup_undoneTasks_throwsIllegalStateException() {
        //  given
        var mockTaskRepository = mock(TaskRepository.class);
        when(mockTaskRepository.existsByDoneIsFalseAndGroup_Id(anyInt())).thenReturn(true);
        //  system under test
        var toTest = new TaskGroupService(null, mockTaskRepository);
        //  when
        var exception = catchThrowable(() -> toTest.toggleGroup(1));
        //  then
        assertThat(exception)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("has undone tasks");
    }

    @Test
    @DisplayName("should return throwsIllegalStateException when no group")
    void toggleGroup_findById_throwsIllegalArgumentException() {
        //  given
        var mockTaskRepository = mock(TaskRepository.class);
        when(mockTaskRepository.existsByDoneIsFalseAndGroup_Id(anyInt())).thenReturn(false);
        //  and
        var mockTaskGroupRepository = mock(TaskGroupRepository.class);
        when(mockTaskGroupRepository.findById(anyInt())).thenReturn(Optional.empty());
        //  system under test
        var toTest = new TaskGroupService(mockTaskGroupRepository, mockTaskRepository);
        //  when
        var exception = catchThrowable(() -> toTest.toggleGroup(1));
        //  then
        assertThat(exception)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("TaskGroup with given");
    }

    @Test
    @DisplayName("should change status for TaskGroup with finished tasks")
    void toggleGroup_ToggleAndSaveGroup() {
        //  given
        var mockTaskRepository = mock(TaskRepository.class);
        when(mockTaskRepository.existsByDoneIsFalseAndGroup_Id(anyInt())).thenReturn(false);
        //  and
        var taskGroup = new TaskGroup();
        boolean statusBefore = taskGroup.isDone();
        //  and
        var mockTaskGroupRepository = mock(TaskGroupRepository.class);
        when(mockTaskGroupRepository.findById(anyInt())).thenReturn(Optional.of(taskGroup));
        //  system under test
        var toTest = new TaskGroupService(mockTaskGroupRepository, mockTaskRepository);
        //  when
        toTest.toggleGroup(0);
        //  then
        assertThat(statusBefore).isNotEqualTo(taskGroup.isDone());
    }
}