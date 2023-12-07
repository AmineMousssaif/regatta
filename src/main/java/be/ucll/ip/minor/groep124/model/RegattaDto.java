package be.ucll.ip.minor.groep124.model;

import be.ucll.ip.minor.groep124.exceptions.DomainException;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class RegattaDto {

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

    public RegattaDto(){

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

    public Regatta createRegatta(){
        Regatta regatta = new Regatta();
        regatta.setName(this.getName());
        regatta.setOrganiser(this.getOrganiser());
        regatta.setDate(this.getDate());
        regatta.setMaxAantalToegelatenTeams(this.getMaxAantalToegelatenTeams());
        regatta.setCategory(this.getCategory());

        return regatta;
    }

    public Regatta updateRegatta(Regatta regatta) {
        regatta.setName(this.getName());
        regatta.setOrganiser(this.getOrganiser());
        regatta.setDate(this.getDate());
        regatta.setMaxAantalToegelatenTeams(this.getMaxAantalToegelatenTeams());
        regatta.setCategory(this.getCategory());

        return regatta;
    }
}
