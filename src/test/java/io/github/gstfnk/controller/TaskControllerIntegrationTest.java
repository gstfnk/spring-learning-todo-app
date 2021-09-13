package io.github.gstfnk.controller;

import io.github.gstfnk.model.Task;
import io.github.gstfnk.model.TaskRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("integration")
class TaskControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskRepository taskRepository;

    @Test
    void httpGet_returnsGivenTasks() throws Exception {
        //  given
        int id = taskRepository.save(new Task("integration", LocalDateTime.now())).getId();
        //  when + then
        mockMvc.perform(get("/tasks/" + id))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void httpPost_createsTask() throws Exception {
        //  given
        String description = "createdTestTask";
        String requestJson = "{\n" +
                "\t\"done\":false,\n" +
                "\t\"description\": \"" + description + "\",\n" +
                "    \"deadline\": \"2021-09-13T20:52:45.955097\"\n" +
                "}";
        //  when + then
        mockMvc.perform(post("/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.done").value(false))
                .andExpect(jsonPath("$.description").value(description));
    }

    @Test
    void httpPut_updatesTask() throws Exception {
        //  given
        String descriptionBefore = "beforeUpdate";
        //  and
        int id = taskRepository.save(new Task(descriptionBefore, LocalDateTime.now())).getId();
        //  and
        String descriptionAfter = "afterUpdate";
        String requestJson = "{\n" +
                "\t\"done\":false,\n" +
                "\t\"description\": \"" + descriptionAfter + "\",\n" +
                "    \"deadline\": \"2021-09-13T20:52:45.955097\"\n" +
                "}";
        //  when + then
        mockMvc.perform(put("/tasks/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.done").value(false))
                .andExpect(jsonPath("$.description").value(descriptionAfter));
    }
}
