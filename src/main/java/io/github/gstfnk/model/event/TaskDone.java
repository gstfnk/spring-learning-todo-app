package io.github.gstfnk.model.event;

import io.github.gstfnk.model.Task;

import java.time.Clock;

public class TaskDone extends TaskEvent {
     TaskDone(Task source) {
        super(source.getId(), Clock.systemDefaultZone());
    }
}
