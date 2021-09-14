package io.github.gstfnk.controller;

import io.github.gstfnk.configuration.TaskConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/info")
class InfoController {
    private final DataSourceProperties datasource;
    private final TaskConfiguration myProp;

    public InfoController(DataSourceProperties datasource, TaskConfiguration myProp) {
        this.datasource = datasource;
        this.myProp = myProp;
    }

    @GetMapping(path = "/url")
    String url() {
        return datasource.getUrl();
    }

    @GetMapping(path = "/prop")
    boolean myProp() {
        return myProp.getTemplate().isAllowMultipleTasks();
    }
}
