package be.ucll.ip.minor.groep124.controller;

import be.ucll.ip.minor.groep124.exceptions.ServiceException;
import be.ucll.ip.minor.groep124.model.Boat;
import be.ucll.ip.minor.groep124.model.Storage;
import be.ucll.ip.minor.groep124.service.StorageBoatService;
import be.ucll.ip.minor.groep124.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/storage-boat")
public class StorageBoatRESTController {

    @Autowired
    private StorageBoatService storageBoatService;

    @Autowired
    private StorageService storageService;

    //OVERVIEW
    @GetMapping("/overview")
    public Iterable<Storage> overview(){
        return storageService.findAll();
    }

    //OVERVIEW BOATS IN STORAGE MET GEGEVEN STORAGE ID
    @GetMapping("/boats")
    public Iterable<Boat> getBoatsByStorageId(@RequestParam(name = "storageId") long storageId) {
        return storageBoatService.findBoatsByStorageId(storageId);
    }

    //ADD BOAT TO STORAGE
    @PostMapping("/add/boat/{boatId}/to/storage/{storageId}")
    public Boat addBoatToStorage(@PathVariable("boatId") long boatId, @PathVariable("storageId") long storageId){
        return storageBoatService.addBoatToStorage(boatId, storageId);
    }

    //REMOVE BOAT FROM STORAGE
    @PostMapping("/remove/boat/{boatId}/from/storage/{storageId}")
    public Boat removeBoatFromStorage(@PathVariable("boatId") long boatId, @PathVariable("storageId") long storageId) {
        return storageBoatService.removeBoatFromStorage(boatId, storageId);
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
