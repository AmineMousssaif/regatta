package be.ucll.ip.minor.groep124.controller;

import be.ucll.ip.minor.groep124.exceptions.ServiceException;
import be.ucll.ip.minor.groep124.model.Regatta;
import be.ucll.ip.minor.groep124.model.Team;
import be.ucll.ip.minor.groep124.service.RegattaService;
import be.ucll.ip.minor.groep124.service.RegattaTeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/regatta-team")
public class RegattaTeamRESTController {

    @Autowired
    private RegattaService regattaService;

    @Autowired
    private RegattaTeamService regattaTeamService;

    //OVERVIEW
    @GetMapping("/overview")
    public Iterable<Regatta> overview() {
        return regattaService.findAll();
    }

    //OVERVIEW TEAMS VAN REGATTA MET GEGEVEN REGATTA ID
    @GetMapping("/teams")
    public Iterable<Team> getTeamsByRegattaId(@RequestParam(name = "regattaId") long regattaId) {
        return regattaTeamService.findTeamsByRegattasId(regattaId);
    }

    //ADD TEAM TO REGATTA
    @PostMapping("/add/team/{teamId}/to/regatta/{regattaId}")
    public Team addTeamToRegatta(@PathVariable("teamId") long teamId, @PathVariable("regattaId") long regattaId) {
        return regattaTeamService.addTeamToRegatta(teamId, regattaId);
    }

    //REMOVE TEAM FROM REGATTA
    @PostMapping("/remove/team/{teamId}/from/regatta/{regattaId}")
    public Team removeTeamFromRegatta(@PathVariable("teamId") long teamId, @PathVariable("regattaId") long regattaId) {
        return regattaTeamService.removeTeamFromRegatta(teamId, regattaId);
    }

    @GetMapping("/search")
    public Iterable<Regatta> search(@RequestParam(name = "club") String organiser) {
        return regattaService.findByOrganiser(organiser);
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

