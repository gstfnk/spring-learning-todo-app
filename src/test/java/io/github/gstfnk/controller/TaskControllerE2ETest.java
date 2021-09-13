package io.github.gstfnk.controller;

import io.github.gstfnk.model.Task;
import io.github.gstfnk.model.TaskRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TaskControllerE2ETest {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    TaskRepository taskRepository;

    @Test
    void httpGet_returnsAllTasks() {
        //  given
        int initial = taskRepository.findAll().size();
        taskRepository.save(new Task("foo", LocalDateTime.now()));
        taskRepository.save(new Task("bar", LocalDateTime.now()));
        //  when
        Task[] result = restTemplate.getForObject("http://localhost:" + port + "/tasks", Task[].class);
        //  then
        assertThat(result).hasSize(initial + 2);
    }

    @Test
    void httpGet_returnsGivenTask() {
        //  given
        Task toSave = new Task("givenTask", LocalDateTime.now());
        int savedTaskId = taskRepository.save(toSave).getId();
        //  when
        Task result = restTemplate.getForObject
                ("http://localhost:" + port + "/tasks/" + savedTaskId, Task.class);
        //  then
        assertThat(result.getId()).isEqualTo(savedTaskId);
        assertThat(result.getDescription()).isEqualTo(toSave.getDescription());
        assertThat(result.isDone()).isEqualTo(toSave.isDone());
    }
}