package be.ucll.ip.minor.groep124.controller;

import be.ucll.ip.minor.groep124.exceptions.DomainException;
import be.ucll.ip.minor.groep124.exceptions.ServiceException;
import be.ucll.ip.minor.groep124.model.Regatta;
import be.ucll.ip.minor.groep124.model.RegattaDto;
import be.ucll.ip.minor.groep124.service.RegattaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("regatta")
public class RegattaController {

    private int currentPage = 1, currentSize = 5;

    @Autowired
    private RegattaService regattaService;

    public RegattaController(RegattaService regattaService) {
        this.regattaService = regattaService;
    }

    //EVERYTHING OVERVIEW - SORT - SEARCH
    //https://www.baeldung.com/spring-thymeleaf-pagination
    @GetMapping("/overview")
    public String overview(Model model,
                           @RequestParam("page")Optional<Integer> page,
                           @RequestParam("size") Optional<Integer> size) {
        List<Regatta> regattas = regattaService.findAll();
        setPagination(regattas, "overview", model, page, size);
        if (regattas.isEmpty()) {
            createSampleData();
            System.out.println("Sample data created");
        } else {
            System.out.println("Sample data not created");
        }
        return "overview-regatta";
    }



    @GetMapping("/sort/{column}")
    public String overviewByColumn(@PathVariable String column, Model model,
                                   @RequestParam("page") Optional<Integer> page,
                                   @RequestParam("size") Optional<Integer> size) {
        List<Regatta> result;
        Comparator<Regatta> comparator;

        if (column.equals("club")) {
            comparator = Comparator.comparing(regatta -> regatta.getOrganiser().toLowerCase());
            result = regattaService.findAllIgnoreCase(comparator, true);
        } else if (column.equals("date")) {
            //comparator = Comparator.comparing(Regatta::getDate).reversed();
            result = regattaService.findAllCaseSensitive(column, false);
        } else {
            // Handle invalid column parameter
            // For example, redirect to an error page or show an error message
            return "error/404";
        }

        setPagination(result, column, model, page, size);
        return "overview-regatta";
    }

    @GetMapping("/search")
    public String search(@RequestParam(name = "category", required = false) String category,
                         @RequestParam(name = "dateAfter", required = false) LocalDate dateAfter,
                         @RequestParam(name = "dateBefore", required = false) LocalDate dateBefore,
                         @RequestParam("page") Optional<Integer> page,
                         @RequestParam("size") Optional<Integer> size,
                         Model model) {
        List<Regatta> all = Collections.emptyList();
        try {
            all =  regattaService.findAllByCategoryAndOrPeriodIgnoreCase(category, dateAfter, dateBefore);
        } catch (ServiceException e){
            model.addAttribute("error", e.getMessage());
        }
        String dateAfterString = "";
        String dateBeforeString = "";
        if(dateAfter != null){
            dateAfterString = dateAfter.toString();
        }
        if(dateBefore != null){
            dateBeforeString = dateBefore.toString();
        }

        String currentNav = "search?dateAfter=" +
                dateAfterString +
                "&dateBefore=" +
                dateBeforeString +
                "&category=" +
                category;
        setPagination(all, currentNav, model, page, size);
        model.addAttribute("afterDate", dateAfter);
        model.addAttribute("beforeDate", dateBefore);
        model.addAttribute("category", category);
        return "overview-regatta";
    }

    //ADD
    @GetMapping("/add")
    public String add(Model model) {
        model.addAttribute("regattaDto", new RegattaDto());
        return "add-regatta";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute @Valid RegattaDto regattaDto, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("regattaDto", regattaDto);
            return "add-regatta";
        }

        try {
            regattaService.createRegatta(regattaDto);
        } catch (ServiceException e) {
            model.addAttribute("error", e.getMessage());
            return "add-regatta";
        }
        return "redirect:/regatta/overview";
    }

    //UPDATE
    @GetMapping("/update")
    public String update(@RequestParam("id") long id, Model model) {
        try {
            Regatta regatta = regattaService.getRegatta(id);
            model.addAttribute("regattaDto", regatta.toDto());
        } catch (DomainException | ServiceException exc) {
            model.addAttribute("error", exc.getMessage());
        }
        return "update-regatta";
    }

    @PostMapping("/update")
    public String update(@RequestParam("id") long id,
                         @Valid RegattaDto regattaDto, BindingResult result, Model model) {
        if (result.hasErrors()) {
            regattaDto.setId(id);
            model.addAttribute("regattaDto", regattaDto);
            return "update-regatta";
        }
        try {
            regattaService.updateRegatta(id, regattaDto);
        } catch (ServiceException e){
            model.addAttribute("error", e.getMessage());
            return "update-regatta";
        }

        return "redirect:/regatta/overview";
    }

    //DELETE
    @GetMapping("/delete/{id}")
    public String delete_confirm(@PathVariable("id") long id, Model model) {
        try {
            Regatta regatta = regattaService.getRegatta(id);
            model.addAttribute("regattaDto", regatta.toDto());
        } catch (DomainException | ServiceException e){
            model.addAttribute("error", e.getMessage());
        }
        return "delete-regatta-confirm";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable("id") long id, Model model) {
        try {
            regattaService.deleteRegatta(id);
        } catch (ServiceException e){
            model.addAttribute("error", e.getMessage());
        }
        return "redirect:/regatta/overview";
    }

    //DATA INJECTION & TRANSFER
    private void createSampleData() {
        RegattaDto bram = new RegattaDto();
        bram.setName("Bramske");
        bram.setOrganiser("lhwaake");
        LocalDate date = LocalDate.of(2023, Month.OCTOBER, 15);
        bram.setDate(date);
        bram.setMaxAantalToegelatenTeams(6);
        bram.setCategory("bootjes");

        RegattaDto elke = new RegattaDto();
        elke.setName("Elkenaar");
        elke.setOrganiser("Kaaske");
        elke.setDate("2023-07-15");
        elke.setMaxAantalToegelatenTeams(4);
        elke.setCategory("bootje");

        RegattaDto regattaCategorie = new RegattaDto();
        regattaCategorie.setName("CATEGOR");
        regattaCategorie.setOrganiser("IEEEEE");
        regattaCategorie.setDate("2023-10-28");
        regattaCategorie.setMaxAantalToegelatenTeams(4);
        regattaCategorie.setCategory("categor");

        regattaService.createRegatta(bram);
        regattaService.createRegatta(elke);
        regattaService.createRegatta(regattaCategorie);
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

    private void setPagination(List<Regatta> desiredResult, String currentNav, Model model, Optional<Integer> page, Optional<Integer> size){
        currentPage = page.orElse(1);
        currentSize = size.orElse(5);

        Page<Regatta> regattaPage = regattaService.findPaginated(PageRequest.of(currentPage - 1, currentSize), desiredResult);

        int totalPages = regattaPage.getTotalPages();
        List<Integer> pageNumbers = getPageNumbers(totalPages);
        if(pageNumbers != null){
            model.addAttribute("pageNumbers", pageNumbers);
            model.addAttribute("current", currentNav);
        }
        model.addAttribute("regattas", regattaPage);
    }
}

