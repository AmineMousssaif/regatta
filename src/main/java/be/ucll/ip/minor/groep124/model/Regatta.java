package be.ucll.ip.minor.groep124.model;

import be.ucll.ip.minor.groep124.exceptions.DomainException;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Collections;
import java.util.Set;

@Entity
@Table(name = "Regatta", uniqueConstraints = {@UniqueConstraint(columnNames = {"name", "organiser"})})
public class Regatta {

    @JsonManagedReference
    @ManyToMany
    @JoinTable(
            name = "teams_registered",
            joinColumns = @JoinColumn(name = "regatta_id"),
            inverseJoinColumns = @JoinColumn(name = "team_id")
    )
    private Set<Team> teamsRegistered;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotBlank(message = "name.missing")
    @Size(min = 5, message = "name-characters.too.few")
    @Column(name = "name")
    private String name;

    @NotBlank(message = "organiser.missing")
    @Size(min = 5, message = "organiser-characters.too.few")
    @Column(name = "organiser")
    private String organiser;

    @NotNull(message = "date.missing")
    @Future(message = "date.should.be.in.the.future")
    @Column(name = "date")
    private LocalDate date;

    @NotNull(message = "max_teams.missing")
    @Min(value = 2, message = "too-few-teams")
    @Max(value = 7, message = "too-many-teams")
    @Column(name = "max_teams")
    private int maxAantalToegelatenTeams;

    @NotBlank(message = "category.missing")
    @Size(min = 5, message = "category-characters.too.few")
    @Column(name = "category")
    private String category;

/*    @AssertTrue(message = "A club can only have one contest in a day")
    private boolean isUniqueClubContest() {
        List<Regatta> contestsOnSameDay = null;// code to retrieve all contests on the same day
        for (Regatta contest : contestsOnSameDay) {
            if (contest.getOrganiser().equals(this.organiser)) {
                return false;
            }
        }
        return true;
    }*/

    public Regatta(){

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrganiser() {
        return organiser;
    }

    public void setOrganiser(String organiser) {
        this.organiser = organiser;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setDate(String date){
        checkValidDate(date);
        LocalDate oldDate = null;
        if(this.date != null){
            oldDate = this.date;
        } else {
            oldDate = LocalDate.now();
        }
        this.date = LocalDate.parse(date);
        try {
            if(this.date.isBefore(oldDate)){
                throw new DomainException();
            }
        } catch (NullPointerException nullPointerException){
            throw new DomainException("Invalid date given");
        } catch (RuntimeException runtimeException){
            throw new DomainException("Date of the match needs to be in the future");
        }
    }

    /**
     * Checks if the given date string can be parsed.
     * Should only be used within this class or other classes in domain.model
     * @param date: String
     * @throws DomainException when date has incorrect format
     * @see LocalDate
     * */
    public static void checkValidDate(String date){
        try {
            LocalDate.parse(date);
        } catch (DateTimeParseException dtp){
            throw new DomainException("Date has incorrect format. It should be dd-mm-yyyy");
        }
    }

    public int getMaxAantalToegelatenTeams() {
        return maxAantalToegelatenTeams;
    }

    public void setMaxAantalToegelatenTeams(int maxAantalToegelatenTeams) {
        this.maxAantalToegelatenTeams = maxAantalToegelatenTeams;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public RegattaDto toDto(){
        RegattaDto regattaDto = new RegattaDto();

        regattaDto.setId(this.getId());
        regattaDto.setOrganiser(this.getOrganiser());
        regattaDto.setName(this.getName());
        regattaDto.setDate(this.getDate());
        regattaDto.setMaxAantalToegelatenTeams(this.getMaxAantalToegelatenTeams());
        regattaDto.setCategory(this.getCategory());

        return regattaDto;
    }

    public Set<Team> getTeamsRegistered() {
        if(teamsRegistered == null) {
            this.teamsRegistered = Collections.emptySet();
        }
        return teamsRegistered;
    }

    public void setTeamsRegistered(Set<Team> teamsRegistered) {
        this.teamsRegistered = teamsRegistered;
    }

    public void addTeam(Team team) {
        if(team.getRegisteredFor().contains(this) || teamsRegistered.contains(team)){
            throw new DomainException("addTeam", "team.already.in.regatta");
        }
        if(teamsRegistered.size() >= maxAantalToegelatenTeams) {
            throw new DomainException("addTeam" , "regatta.is.full");
        }
        if(!(team.getCategory().equals(category))){
            throw new DomainException("addTeam", "team.does.not.match.regatta.category");
        }
        if(team.getRegisteredFor() != null && team.getRegisteredFor().size() > 0) {
            for(Regatta regatta : team.getRegisteredFor()){
                if(regatta.getDate().equals(date)){
                    throw new DomainException("addTeam", "team.already.has.regatta.on.this.date");
                }
            }
        }
        teamsRegistered.add(team);
        team.getRegisteredFor().add(this);
    }

    public void removeTeam(Team team) {
        if(!teamsRegistered.contains(team)){
            throw new DomainException("get", "team.not.in.regatta");
        }
        else {
            this.teamsRegistered.remove(team);
            team.removeRegatta(this);
        }
    }
    public boolean hasNoTeams(Regatta regatta) {
        if(regatta.getTeamsRegistered() != null && regatta.getTeamsRegistered().size() > 0){
            return false;
        }
        return true;
    }
}

