package be.ucll.ip.minor.groep124.controller;

import be.ucll.ip.minor.groep124.exceptions.DomainException;
import be.ucll.ip.minor.groep124.exceptions.ServiceException;
import be.ucll.ip.minor.groep124.model.Team;
import be.ucll.ip.minor.groep124.model.TeamDto;
import be.ucll.ip.minor.groep124.service.TeamService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/team")
public class TeamController {

    private int currentPage = 1, currentSize = 5;

    private final TeamService teamService;

    @Autowired
    public TeamController(TeamService teamService){
        this.teamService = teamService;
    }

    //OVERVIEW
    @GetMapping("/overview")
    public String overview(Model model,
                           @RequestParam("page") Optional<Integer> page,
                           @RequestParam("size") Optional<Integer> size) {
        currentPage = page.orElse(1);
        currentSize = size.orElse(5);

        Page<Team> teamPage = teamService.findPaginated(PageRequest.of(currentPage - 1, currentSize), teamService.findAll());

        if(teamService.findAll().isEmpty()){
            createSampleData();
            System.out.println("Sample data created");
        }
        else {
            System.out.println("Sample data not created");
        }

        int totalPages = teamPage.getTotalPages();
        List<Integer> pageNumbers = getPageNumbers(totalPages);
        if (pageNumbers != null){
            model.addAttribute("pageNumbers", pageNumbers);
            model.addAttribute("current", "overview");
        }
        model.addAttribute("teams", teamPage);
        return "overview-team";
    }

    //ADD/CREATE
    @GetMapping("/add")
    public String add(Model model) {
        model.addAttribute("teamDto", new TeamDto());
        return "add-team";
    }

    @PostMapping("/add")
    public String add(@Valid TeamDto teamDto, BindingResult bindingResult, Model model){
        if(bindingResult.hasErrors()){
            model.addAttribute("teamDto", teamDto);
            return "add-team";
        }
        try {
            teamService.createTeam(teamDto);
        } catch (DomainException | ServiceException e) {
            model.addAttribute("error", e.getMessage());
            return "add-team";
        }
        return "redirect:/team/overview";
    }

    //UPDATE
    @GetMapping("/update/{id}")
    public String update(@PathVariable("id") long id, Model model) {
        try {
            Team team = teamService.getTeam(id);
            model.addAttribute("teamDto", team.toDto());
        } catch (DomainException | ServiceException e){
            model.addAttribute("error", e.getMessage());
        }
        return "update-team";
    }

    @PostMapping("/update/{id}")
    public String update (@PathVariable("id") long id,
                          @Valid TeamDto teamDto, BindingResult bindingResult, Model model){
        if(bindingResult.hasErrors()){
            teamDto.setId(id);
            model.addAttribute("teamDto", teamDto);
            return "update-team";
        }
        try {
            teamService.updateTeam(id, teamDto);
        } catch (ServiceException e){
            model.addAttribute("error", e.getMessage());
        }
        return "redirect:/team/overview";
    }

    //DELETE
    @GetMapping("/delete/{id}")
    public String delete_confirm(@PathVariable("id") long id, Model model) {
        try {
            Team team = teamService.getTeam(id);
            model.addAttribute("teamDto", team.toDto());
        } catch (DomainException | ServiceException e){
            model.addAttribute("error", e.getMessage());
        }
        return "delete-team-confirm";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable("id") long id, Model model) {
        try {
            teamService.deleteTeam(id);
        } catch (ServiceException e){
            model.addAttribute("error", e.getMessage());
        }
        return "redirect:/team/overview";
    }

    //DATA INJECTION / TRANSFER
    private void createSampleData() {
        TeamDto teamUno = new TeamDto();
        teamUno.setName("UnoUno");
        teamUno.setCategory("categor");
        teamUno.setClub("IKHEBEENBOOT");
        teamUno.setPassengers(8);

        TeamDto teamDuo = new TeamDto();
        teamDuo.setName("DuoDuo");
        teamDuo.setCategory("Weetikv");
        teamDuo.setClub("club");
        teamDuo.setPassengers(10);

        TeamDto teamTrio = new TeamDto();
        teamTrio.setName("KAYAK");
        teamTrio.setCategory("categor");
        teamTrio.setClub("clubber");
        teamTrio.setPassengers(10);

        teamService.createTeam(teamUno);
        teamService.createTeam(teamDuo);
        teamService.createTeam(teamTrio);
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
