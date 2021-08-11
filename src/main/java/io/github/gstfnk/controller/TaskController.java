package io.github.gstfnk.controller;

import io.github.gstfnk.model.Task;
import io.github.gstfnk.model.TaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@Controller
@RequestMapping(path = "/tasks")
class TaskController {
    private static final Logger logger = LoggerFactory.getLogger(TaskController.class);
    private final TaskRepository repository;

    TaskController(final TaskRepository repository) {
        this.repository = repository;
    }

    @GetMapping(params = {"!sort", "!page", "!size"})
    ResponseEntity<List<Task>> readAllTasks() {
        logger.warn("Exposing all the tasks!");
        return ResponseEntity.ok(repository.findAll());
    }

    //@RequestMapping(method = RequestMethod.GET)
    @GetMapping
    ResponseEntity<List<Task>> readAllTasks(Pageable pageable) {
        logger.info("Custom pageable");
        return ResponseEntity.ok(repository.findAll(pageable).getContent());
    }

    @GetMapping(path = "/{id}")
    ResponseEntity<Task> readTask(@PathVariable int id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    ResponseEntity<Task> createTask(@RequestBody @Valid Task createdTask) {
        Task task = repository.save(createdTask);
        return ResponseEntity.created(URI.create("/" + task.getId())).body(task);
    }

    @PutMapping(path = "/{id}")
    ResponseEntity<Task> updateTask(@RequestBody @Valid Task updatedTask, @PathVariable int id) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        updatedTask.setId(id);
        return ResponseEntity.ok(repository.save(updatedTask));
    }

    @DeleteMapping("/{id}")
    ResponseEntity<?> deleteTask(@PathVariable int id) {
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }


}
