package io.github.gstfnk.model;


import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "TASKS")
public class Task extends BaseTask {
    private LocalDateTime deadline;
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

    TaskGroup getGroup() {
        return group;
    }

    void setGroup(TaskGroup group) {
        this.group = group;
    }

    public void updateFrom(final Task source) {
        updateFromBase(source);
        this.deadline = source.deadline;
        this.group = source.group;
    }
}
