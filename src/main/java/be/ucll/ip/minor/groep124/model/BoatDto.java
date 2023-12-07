package be.ucll.ip.minor.groep124.model;

import jakarta.persistence.Column;
import jakarta.validation.constraints.*;

public class BoatDto {
    private long id;

    @NotBlank(message = "name.missing")
    @Size(min = 5, message = "name-characters.too.few")
    @Column(name = "name")
    private String name;

    @NotBlank(message = "email.missing")
    @Column(name = "email")
    private String email;

    @Min(value = 1, message = "length.too.small")
    @Column(name = "length")
    private int length;

    @Min(value = 1, message = "height.too.small")
    @Column(name = "height")
    private int height;

    @Min(value = 1, message = "width.too.small")
    @Column(name = "width")
    private int width;

    @Pattern(regexp = "[a-zA-Z0-9]{10}", message = "insurance.invalid")
    @NotBlank(message = "insurance.missing")
    @Column(name = "insurance")
    private String  insurance;

    public BoatDto() {
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public String getInsurance() {
        return insurance;
    }

    public void setInsurance(String insurance) {
        this.insurance = insurance;
    }

    public Boat createBoat(){
        Boat boat = new Boat();
        boat.setName(this.getName());
        boat.setEmail(this.getEmail());
        boat.setLength(this.getLength());
        boat.setWidth(this.getWidth());
        boat.setHeight(this.getHeight());
        boat.setInsurance(this.getInsurance());

        return boat;
    }

    public Boat updateBoat(Boat boat){
        boat.setName(this.getName());
        boat.setEmail(this.getEmail());
        boat.setLength(this.getLength());
        boat.setWidth(this.getWidth());
        boat.setHeight(this.getHeight());
        boat.setInsurance(this.getInsurance());

        return boat;
    }

}
