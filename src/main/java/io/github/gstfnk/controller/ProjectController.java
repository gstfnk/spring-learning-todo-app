package io.github.gstfnk.controller;

import io.github.gstfnk.logic.ProjectService;
import io.github.gstfnk.model.Project;
import io.github.gstfnk.model.ProjectStep;
import io.github.gstfnk.model.projection.ProjectWriteModel;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping(path = "/projects")
public class ProjectController {
    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping
    String showProjects(Model model) {
        model.addAttribute("project", new ProjectWriteModel());
        return "projects";
    }

    @PostMapping(params = "addStep")
    String addProjectStep(@ModelAttribute("project") ProjectWriteModel current) {
        current.getSteps().add(new ProjectStep());
        return "projects";
    }

    @PostMapping
    String addProject(@ModelAttribute("project") @Valid ProjectWriteModel current,
                      BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "projects";
        }
        projectService.save(current);
        model.addAttribute("project", new ProjectWriteModel());
        model.addAttribute("projects", getProjects());
        model.addAttribute("message", "Project has been added");
        return "projects";
    }

    @PostMapping(path = "/{id}")
    String createGroup(@ModelAttribute("project") ProjectWriteModel current,
                       Model model,
                       @PathVariable int id,
                       @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime deadline) {
        try {
            projectService.createGroup(deadline, id);
            model.addAttribute("message", "Group has been added");
        } catch (IllegalStateException | IllegalArgumentException e) {
            model.addAttribute("message", "Error creating group");
        }
        return "projects";
    }

    @ModelAttribute("projects")
    List<Project> getProjects() {
        return projectService.readAll();
    }
}
