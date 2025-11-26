package org.oclick.services.vacancy.controller;

import org.oclick.libs.spi.jobboard.JobboardProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@DependsOn("pluginManager")
public class TestPlugin {
    private final List<JobboardProvider> extensions;

    @Autowired
    public TestPlugin(List<JobboardProvider> extensions) {
        this.extensions = extensions;
    }

    @GetMapping("/test")
    public String test() {
        return extensions.stream().map(JobboardProvider::getAlias).collect(Collectors.joining(","));
    }
}