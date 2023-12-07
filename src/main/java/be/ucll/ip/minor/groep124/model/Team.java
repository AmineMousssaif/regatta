package be.ucll.ip.minor.groep124.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;

@Entity
@Table(name = "Team" , uniqueConstraints = {@UniqueConstraint(columnNames = {"name", "category"})})
public class Team {

    @JsonBackReference
    @ManyToMany(mappedBy = "teamsRegistered")
    private Set<Regatta> registeredFor;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "name.missing")
    @Size(min = 5, message = "name-characters.too.few")
    @Column(name = "name")
    private String name;

    @Pattern(regexp = "[a-zA-Z0-9]{7}", message = "category.invalid")
    @NotBlank(message = "category.missing")
    @Column(name = "category")
    private String category;

    @Column(name = "club")
    private String club;


    @Min(value = 1, message = "too-few-passengers")
    @Max(value = 12, message = "too-many-passengers")
    @Column(name = "passengers")
    private int passengers;

    public long getId() {
        return id;
    }

    public Team() {
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getClub() {
        return club;
    }

    public void setClub(String club) {
        this.club = club;
    }

    public int getPassengers() {
        return passengers;
    }

    public void setPassengers(int passengers) {
        this.passengers = passengers;
    }

    public TeamDto toDto() {
        TeamDto teamDto = new TeamDto();

        teamDto.setId(this.getId());
        teamDto.setName(this.getName());
        teamDto.setCategory(this.getCategory());
        teamDto.setClub(this.getClub());
        teamDto.setPassengers(this.getPassengers());

        return teamDto;
    }

    public Set<Regatta> getRegisteredFor() {
        if(registeredFor == null){
            this.registeredFor = Collections.emptySet();
        }
        return registeredFor;
    }

    public void setRegisteredFor(Set<Regatta> regattas) {
        if(regattas == null || regattas.size() == 0){
            this.registeredFor = Collections.emptySet();
        }
        else {
            this.registeredFor = regattas;
        }
    }

    public void addRegatta(Regatta regatta) {
        registeredFor.add(regatta);
    }

    public void removeRegatta(Regatta regatta) {
        this.registeredFor.remove(regatta);
    }

    public ArrayList<String> getRegattaNames(){
        ArrayList<String> regattaNames = new ArrayList<>();
        if(registeredFor != null){
            for(Regatta regatta : registeredFor){
                regattaNames.add(regatta.getName());
            }
        }
        return regattaNames;
    }

    public boolean inRegatta(Team team) {
        if(team.getRegisteredFor() != null && team.getRegisteredFor().size() > 0) {
            return true;
        }
        return false;
    }
}
