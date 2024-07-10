package Database.model;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.util.stream.Collectors;

@ShellComponent
public class validate_controller {
    repository repo;
    @ShellMethod
    public String validate_tdNo(){
        return repo.findAll().stream().collect(Collectors.toList()).toString();

    }
}
