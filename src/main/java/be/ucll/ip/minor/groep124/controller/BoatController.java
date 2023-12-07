package be.ucll.ip.minor.groep124.controller;

import be.ucll.ip.minor.groep124.exceptions.DomainException;
import be.ucll.ip.minor.groep124.exceptions.ServiceException;
import be.ucll.ip.minor.groep124.model.Boat;
import be.ucll.ip.minor.groep124.model.BoatDto;
import be.ucll.ip.minor.groep124.service.BoatService;
import org.springframework.beans.factory.annotation.Autowired;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/boat")
public class BoatController {

    private int currentPage = 1, currentSize = 5;
    private final BoatService boatService;

    @Autowired
    public BoatController(BoatService boatService) {
        this.boatService = boatService;
    }

    //OVERVIEW
    @GetMapping("/overview")
    public String overview(Model model,
                           @RequestParam("page") Optional<Integer> page,
                           @RequestParam("size") Optional<Integer> size) {
        currentPage = page.orElse(1);
        currentSize = size.orElse(5);

        Page<Boat> boatPage = boatService.findPaginated(PageRequest.of(currentPage - 1, currentSize), boatService.findAll());

        if (boatService.findAll().isEmpty()) {
            createSampleData();
            System.out.println("Sample data created");
        }
        else {
            System.out.println("Sample data not created");
        }

        int totalPages = boatPage.getTotalPages();
        List<Integer> pageNumbers = getPageNumbers(totalPages);
        if (pageNumbers != null){
            model.addAttribute("pageNumbers", pageNumbers);
            model.addAttribute("current", "overview");
        }
        model.addAttribute("boats", boatPage);
        return "overview-boat";
    }

    //CREATE/ADD
    @GetMapping("/add")
    public String addBoat(Model model) {
        model.addAttribute("boatDto", new BoatDto());
        return "add-boat";
    }

    @PostMapping("/add")
    public String addBoat(@Valid BoatDto boatDto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("boatDto", boatDto);
            return "add-boat";
        }
        try {
            boatService.createBoat(boatDto);
        } catch (DomainException | ServiceException e) {
            model.addAttribute("error", e.getMessage());
            return "add-boat";
        }
        return "redirect:/boat/overview";
    }

    //UPDATE
    @GetMapping("/update/id={id}")
    public String update(@PathVariable("id") long id, Model model) {
     try {
         Boat boat = boatService.getBoat(id);
         model.addAttribute("boatDto", boat.toDto());
     } catch (DomainException | ServiceException e) {
         model.addAttribute("error", e.getMessage());
     }
     return "update-boat";
    }

    @PostMapping("/update/id={id}")
    public String update(@PathVariable("id") long id,
                         @Valid BoatDto boatDto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            boatDto.setId(id);
            model.addAttribute("boatDto", boatDto);
            return "update-boat";
        }
        try {
            boatService.updateBoat(id, boatDto);
        } catch (ServiceException e){
            model.addAttribute("error", e.getMessage());
        }
        return "redirect:/boat/overview";
    }

    //DELETE
    @GetMapping("/delete/id={id}")
    public String delete_confirm(@PathVariable("id") long id, Model model) {
        try {
            Boat boat = boatService.getBoat(id);
            model.addAttribute("boatDto", boat.toDto());
        } catch (DomainException | ServiceException e){
            model.addAttribute("error", e.getMessage());
        }
        return "delete-boat-confirm";
    }

    @PostMapping("/delete/id={id}")
    public String deleteBoat(@PathVariable("id") long id, Model model) {
        try {
            boatService.deleteBoat(id);
        } catch (ServiceException e){
            model.addAttribute("error", e.getMessage());
        }
        return "redirect:/boat/overview";
    }

    //DATA INJECTION / TRANSFER
    private void createSampleData() {
        BoatDto boatUno = new BoatDto();
        boatUno.setName("UnoUno");
        boatUno.setEmail("lhwaaa@gmail.com");
        boatUno.setHeight(180);
        boatUno.setWidth(180);
        boatUno.setLength(150);
        boatUno.setInsurance("12RHBFHKDD");

        BoatDto boatDuo = new BoatDto();
        boatDuo.setName("DuoDuo");
        boatDuo.setEmail("KALLAA@mail.com");
        boatDuo.setHeight(180);
        boatDuo.setWidth(180);
        boatDuo.setLength(150);
        boatDuo.setInsurance("12JGHJTGHB");

        boatService.createBoat(boatUno);
        boatService.createBoat(boatDuo);
    }

    //PAGINATION
    //https://www.baeldung.com/spring-thymeleaf-pagination
    private List<Integer> getPageNumbers(int totalPages){
        if(totalPages > 0){
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            return pageNumbers;
        } else {
            return null;
        }
    }
}