package be.ucll.ip.minor.groep124.service;

import be.ucll.ip.minor.groep124.exceptions.DomainException;
import be.ucll.ip.minor.groep124.exceptions.ServiceException;
import be.ucll.ip.minor.groep124.model.Regatta;
import be.ucll.ip.minor.groep124.model.RegattaRepository;
import be.ucll.ip.minor.groep124.model.Team;
import be.ucll.ip.minor.groep124.model.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import java.util.Set;

@Service
public class RegattaTeamService {

   @Autowired
   private RegattaRepository regattaRepository;

   @Autowired
   private TeamRepository teamRepository;

    public Team addTeamToRegatta(long teamId, long regattaId){
        Team team = teamRepository.findById(teamId).orElseThrow(() -> new ServiceException("get", "no.team.with.this.id"));
        Regatta regatta = findRegatta(regattaId);
        try {
            regatta.addTeam(team);
            teamRepository.save(team);
            return team;
        } catch (DomainException d){
            throw new ServiceException(d.getAction(), d.getMessage());
        } catch (DataIntegrityViolationException e){
            throw new ServiceException("addTeamToRegatta", e.getMessage());
        }
    }

    public Team removeTeamFromRegatta(long teamId, long regattaId){
        Team team = teamRepository.findById(teamId).orElseThrow(() -> new ServiceException("get", "no.team.with.this.id"));
        Regatta regatta = findRegatta(regattaId);
        try {
            regatta.removeTeam(team);
            teamRepository.save(team);
            return team;
        } catch (DomainException d){
            throw new ServiceException(d.getAction(), d.getMessage());
        } catch (DataIntegrityViolationException e){
            throw new ServiceException("removeTeamFromRegatta", e.getMessage());
        }
    }

    //Moet geen errors gooien bij empty list/null!
    public Set<Team> findTeamsByRegattasId(long regattaId) {
        return findRegatta(regattaId).getTeamsRegistered();
    }

    private Regatta findRegatta(long regattaId){
        return regattaRepository.findById(regattaId).orElseThrow(() -> new ServiceException("get", "no.regatta.with.this.id"));
    }
}
