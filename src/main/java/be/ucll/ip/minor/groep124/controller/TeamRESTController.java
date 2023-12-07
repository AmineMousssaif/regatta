package be.ucll.ip.minor.groep124.controller;

import be.ucll.ip.minor.groep124.exceptions.ServiceException;
import be.ucll.ip.minor.groep124.model.Team;
import be.ucll.ip.minor.groep124.model.TeamDto;
import be.ucll.ip.minor.groep124.service.TeamService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/team")
public class TeamRESTController {

    // GET           => @GetMapping
    // POST          => @Postmapping
    // UPDATE (PUT)  => @PutMapping
    // DELETE        => @DeleteMapping

    // URL (GET, UPDATE, DELETE)
    //  @PathVariable -> /api/patient/delete/3
    //  @RequestParam -> /api/patient/delete?id=3

    // POST
    //  @Valid
    //  @RequestBody

    @Autowired
    private TeamService teamService;

    //OVERVIEW
    @GetMapping("/overview")
    public Iterable<Team> overview() {
        return teamService.findAll();
    }

    //ADD
    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public Team add(@Valid @RequestBody TeamDto teamDto) {
        return teamService.createTeam(teamDto);
    }

    //UPDATE
    @PutMapping("/update/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Team update(@PathVariable("id") long id, @Valid @RequestBody TeamDto teamDto){
        return teamService.updateTeam(id, teamDto);
    }

    //DELETE
    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Team delete(@PathVariable("id") long id) {
        return teamService.deleteTeam(id);
    }

    //METHODS SEARCH
    //alle teams van een categorie te zoeken (niet case sensitive):
    //ook beschikbaar op website
    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public List<Team> search(@RequestParam(name = "category") String category) {
        return teamService.findAllByTeamCategoryIgnoreCase(category);
    }

    //alle teams kunnen tonen met minder passenger dan aangegeven
    //gesorteerd op aantal deelnemers:
    //Enkel te testen met Postman
    @GetMapping("/search/{passengers}")
    @ResponseStatus(HttpStatus.OK)
    public List<Team> search(@PathVariable(name = "passengers") int passengers){
        return teamService.findAllByTeamMembersLessThanOrOrderByTeamMembersAsc(passengers);
    }

    //ERROR HANDLING
    //when not @Valid team --> BAD_REQUEST and is redirected to this method (MethodArgumentNotValidException)
    // ServiceException are also redirected to this method
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({MethodArgumentNotValidException.class, ServiceException.class, ResponseStatusException.class})
    public Map<String, String> handleValidationExceptions(Exception exception) {
        Map<String, String> errors = new HashMap<>();
        if (exception instanceof MethodArgumentNotValidException) {
            ((MethodArgumentNotValidException)exception).getBindingResult().getAllErrors().forEach((error) -> {
                String fieldName = ((FieldError) error).getField();
                String errorMessage = error.getDefaultMessage();
                errors.put(fieldName, errorMessage);
            });
        } else if (exception instanceof ServiceException) {
            errors.put(((ServiceException)exception).getAction(), exception.getMessage());
        }
        else {
            errors.put(exception.getClass().getSimpleName(), exception.getMessage());
        }
        return errors;
    }
}
