package be.ucll.ip.minor.groep124.controller;

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
import be.ucll.ip.minor.groep124.exceptions.ServiceException;
import be.ucll.ip.minor.groep124.model.Boat;
import be.ucll.ip.minor.groep124.model.BoatDto;
import be.ucll.ip.minor.groep124.service.BoatService;

@RestController
@RequestMapping("/api/boat")
public class BoatRESTController {

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
    private BoatService boatService;

    //OVERVIEW
    @GetMapping("/overview")
    public Iterable<Boat> overview() {
        return boatService.findAll();
    }

    //ADD
    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public Boat add(@Valid @RequestBody BoatDto boatDto) {
        return boatService.createBoat(boatDto);
    }

    //UPDATE
    @PutMapping("/update")
    @ResponseStatus(HttpStatus.OK)
    public Boat update(@RequestParam("id") long id, @Valid @RequestBody BoatDto boatDto){
        return boatService.updateBoat(id, boatDto);
    }

    //DELETE
    @DeleteMapping("/delete")
    @ResponseStatus(HttpStatus.OK)
    public Boat delete(@RequestParam("id") long id){
        return boatService.deleteBoat(id);
    }

    //METHODS SEARCH
    //alle boats met een insurance number te zoeken:
    //CASE insensitive want verzekeringsnummers zijn niet case sensitive
    //ook beschikbaar op website --> output is in JSON
    @GetMapping("search")
    @ResponseStatus(HttpStatus.OK)
    public List<Boat> search(@RequestParam(name = "insurance") String insuranceNumber) {
        return boatService.findAllByBoatInsuranceNumber(insuranceNumber);
    }

    //alle boats kunnen zoeken op hoogte-breedte:
    //Enkel te testen met Postman
    @GetMapping("search/{height}/{width}")
    @ResponseStatus(HttpStatus.OK)
    public List<Boat> search(@PathVariable("height") int height,
                              @PathVariable("width") int width) {
        return boatService.findAllByBoatHeightAndWidth(height, width);
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
