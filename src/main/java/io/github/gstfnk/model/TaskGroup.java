package io.github.gstfnk.model;


import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "TASKS_GROUPS")
public class TaskGroup extends BaseTask {
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "group")
    private Set<Task> tasks;

    public TaskGroup() {
    }

    public Set<Task> getTasks() {
        return tasks;
    }

    void setTasks(Set<Task> tasks) {
        this.tasks = tasks;
    }
}
