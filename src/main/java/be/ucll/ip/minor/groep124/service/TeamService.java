package be.ucll.ip.minor.groep124.service;

import be.ucll.ip.minor.groep124.exceptions.DomainException;
import be.ucll.ip.minor.groep124.exceptions.ServiceException;
import be.ucll.ip.minor.groep124.model.Team;
import be.ucll.ip.minor.groep124.model.TeamDto;
import be.ucll.ip.minor.groep124.model.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TeamService {

    @Autowired
    private TeamRepository teamRepository;

    public List<Team> findAll(){
        return teamRepository.findAll();
    }

    private List<Team> findAll(String sortString, boolean ascending) {
        if(!ascending){
            return teamRepository.findAll(Sort.by(sortString).ascending());
        } else {
            return teamRepository.findAll(Sort.by(sortString).descending());
        }
    }

    public List<Team> findAllByTeamCategoryIgnoreCase(String category) {
        List<Team> allTeams = this.findAll();
        allTeams = allTeams.stream().filter(o -> o.getCategory().toLowerCase().startsWith(category.toLowerCase())).collect(Collectors.toList());
        return validateEmptyList(allTeams);
    }

    public List<Team> findAllByTeamMembersLessThanOrOrderByTeamMembersAsc(int passengers) {
        List<Team> allTeams = this.findAll("passengers", true);
        allTeams = allTeams.stream().filter(o -> o.getPassengers() < passengers).collect(Collectors.toList());
        return validateEmptyList(allTeams);
    }

    public List<Team> validateEmptyList(List<Team> teams){
        if(teams == null || teams.isEmpty()){
            throw new ServiceException("search", "No teams were found for your search.");
        } else {
            return teams;
        }
    }

    //PAGINATION
    public Page<Team> findPaginated(Pageable pageable, List<Team> teamList){
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<Team> pageList;

        if (teamList.size() < startItem) {
            pageList = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, teamList.size());
            pageList = teamList.subList(startItem, toIndex);
        }

        Page<Team> teamPage
                = new PageImpl<Team>(pageList, PageRequest.of(currentPage, pageSize), teamList.size());

        return teamPage;
    }

    //TEAM METHODS
    public Team getTeam(long id){
        return  teamRepository.findById(id)
                .orElseThrow(() -> new ServiceException("get", "no.team.with.this.id"));
    }

    public Team createTeam(TeamDto teamDto){
        try {
            Team team = teamDto.createTeam();
            return teamRepository.save(team);
        } catch (DomainException d){
            throw new ServiceException(d.getAction(), d.getMessage());
        } catch (DataIntegrityViolationException e){
            throw new ServiceException("create", "Error creating team: " + e.getMessage());
        }
    }

    public Team addTeam(Team team){
        try{
            return teamRepository.save(team);
        } catch(IllegalArgumentException | DataIntegrityViolationException exc){
            throw new ServiceException("create", "Could not add team because: " + exc.getMessage());
        }
    }

    public Team updateTeam(long id, TeamDto teamDto) {
        Team team = getTeam(id);

        if (team.inRegatta(team)) {
            throw new ServiceException("update", "Team is registered for a regatta, and can't be updated: ");
        }
        else {
            try {
                team = teamDto.updateTeam(team);
                return teamRepository.save(team);
            } catch (DomainException d){
                throw new ServiceException(d.getAction(), d.getMessage());
            } catch (DataIntegrityViolationException e) {
                throw new ServiceException("update", "Error updating team: " + e.getMessage());
            }
        }
    }

    public Team deleteTeam(long id) {
        if (getTeam(id).inRegatta(getTeam(id))) {
            throw new ServiceException("delete", "Team is registered for a regatta, and can't be deleted.");
        } else {
            Team team = getTeam(id);
            try {
                teamRepository.delete(getTeam(id));
                return team;
            } catch (DataIntegrityViolationException e){
                throw new ServiceException("delete", "Could not delete team because: " + e.getMessage());
            }
        }
    }



    public Team updateTeam1(long id, Team teamDto) {
        Team team = getTeam(id);

        team.setName(teamDto.getName());
        team.setCategory(teamDto.getCategory());
        team.setClub(teamDto.getClub());
        team.setPassengers(teamDto.getPassengers());

        try {
            return teamRepository.save(team);
        } catch (RuntimeException e){
            throw new ServiceException("update", "Error updating team: " + e.getMessage());
        }
    }
}
