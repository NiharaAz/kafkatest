package com.example;

import com.example.demo.repository.primary.repository;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@ShellComponent
public class validate_controller {
    repository repo;
    @ShellMethod
    public String validate_tdNo(){
        return repo.findAll().stream().collect(Collectors.toList()).toString();

    }
}
