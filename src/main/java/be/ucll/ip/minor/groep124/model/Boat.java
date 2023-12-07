package be.ucll.ip.minor.groep124.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "Boat", uniqueConstraints = {@UniqueConstraint(columnNames = {"name", "email"})})
public class Boat {

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "storage_id")
    private Storage storage;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotNull(message = "name.missing")
    @NotBlank(message = "name.missing")
    @Size(min = 5, message = "name-characters.too.few")
    @Column(name = "name")
    private String name;

    @NotNull(message = "email.missing")
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
    @NotNull(message = "insurance.missing")
    @NotBlank(message = "insurance.missing")
    @Column(name = "insurance")
    private String  insurance;

    public Boat() {
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

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getInsurance() {
        return insurance;
    }

    public void setInsurance(String insurance) {
        this.insurance = insurance;
    }

    public BoatDto toDto(){
        BoatDto boatDto = new BoatDto();

        boatDto.setId(this.getId());
        boatDto.setName(this.getName());
        boatDto.setEmail(this.getEmail());
        boatDto.setLength(this.getLength());
        boatDto.setWidth(this.getWidth());
        boatDto.setHeight(this.getHeight());
        boatDto.setInsurance(this.getInsurance());

        return boatDto;
    }

    public Storage getStorage() {
        return storage;
    }

    public void setStorage(Storage storage) {
        this.storage = storage;
    }

    public String getStorageName(){
        if(storage == null){
            return "unknown";
        }
        else {
            return storage.getName();
        }
    }
    public boolean inStorage(Boat boat){
        return !boat.getStorageName().equals("unknown");
    }
}
