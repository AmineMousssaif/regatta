package be.ucll.ip.minor.groep124.model;

import jakarta.persistence.Column;
import jakarta.validation.constraints.*;

public class TeamDto {

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

    public TeamDto() {
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

    public Team createTeam(){
        Team team = new Team();
        team.setName(this.getName());
        team.setCategory(this.getCategory());
        team.setClub(this.getClub());
        team.setPassengers(this.getPassengers());
        return team;
    }

    public Team updateTeam(Team team){
        team.setName(this.getName());
        team.setCategory(this.getCategory());
        team.setClub(this.getClub());
        team.setPassengers(this.getPassengers());

        return team;
    }
}
