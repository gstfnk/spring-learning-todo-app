package io.github.gstfnk.model;


import io.github.gstfnk.model.event.TaskEvent;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "TASKS")
public class Task extends BaseTask {
    private LocalDateTime deadline;
    private boolean done;
    @ManyToOne
    @JoinColumn(name = "task_group_id")
    private TaskGroup group;
    @Embedded
    private final Audit audit = new Audit();

    public Task(String description, LocalDateTime deadline) {
        this(description, deadline, null);
    }

    public Task(String description, LocalDateTime deadline, TaskGroup group) {
        this.setDescription(description);
        this.deadline = deadline;
        if (group != null) this.group = group;
    }

    public Task() {

    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }

    public boolean isDone() {
        return done;
    }

    public TaskEvent toggle() {
        this.done = !this.done;
        return TaskEvent.changed(this);
    }

    TaskGroup getGroup() {
        return group;
    }

    void setGroup(TaskGroup group) {
        this.group = group;
    }


    public void updateFrom(final Task source) {
        updateFromBase(source);
        this.done = source.done;
        this.deadline = source.deadline;
        this.group = source.group;
    }
}
