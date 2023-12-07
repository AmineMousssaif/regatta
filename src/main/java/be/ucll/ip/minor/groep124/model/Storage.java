package be.ucll.ip.minor.groep124.model;

import be.ucll.ip.minor.groep124.exceptions.DomainException;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.util.Collections;
import java.util.Set;

@Entity
@Table(name = "Storage")
public class Storage {

    @JsonManagedReference
    @OneToMany(mappedBy = "storage")
    private Set<Boat> boatsStored;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Size(min = 5, message = "name-characters.too.few")
    @NotBlank(message = "name.missing")
    @Column(name = "name")
    private String name;

    @NotBlank(message = "postal-code.missing")
    @Pattern(regexp = "^[0-9]{4}$", message = "postal code has to be 4 numbers long")
    @Column(name = "postalCode")
    private String postalCode;

    @NotNull(message = "storage-space.missing")
    @Min(value = 2, message = "storage-space.too.small")
    @Column(name = "storageSpace")
    private int storageSpace;

    @NotNull(message = "height.missing")
    @Min(value = 1, message = "height.too.small")
    @Column(name = "height")
    private int height;

    @NotNull(message = "year.missing")
    @Min(value = 1850, message = "year.too.small")
    @Max(value = 2024, message = "year.too.big")
    @Column(name = "year")
    private int year;

    public Storage(){

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

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public int getStorageSpace() {
        return storageSpace;
    }

    public void setStorageSpace(int storageSpace) {
        this.storageSpace = storageSpace;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public StorageDto toDto() {
        StorageDto storageDto = new StorageDto();

        storageDto.setId(this.getId());
        storageDto.setName(this.getName());
        storageDto.setPostalCode(this.getPostalCode());
        storageDto.setStorageSpace(this.getStorageSpace());
        storageDto.setHeight(this.getHeight());
        storageDto.setYear(this.getYear());

        return storageDto;
    }

    public Set<Boat> getBoatsStored() {
        if(boatsStored == null){
            this.boatsStored = Collections.emptySet();
        }
        return boatsStored;
    }

    public void setBoatsStored(Set<Boat> boatsStored) {
        this.boatsStored = boatsStored;
    }

    public void addBoat(Boat boat){
        if(boat.getStorage() != null && boat.getStorage().equals(this)){
            throw new DomainException("addBoat", "boat.already.in.storage");
        }
        if(boat.getHeight() >= height){
            throw new DomainException("addBoat", "boat.too.high.for.storage");
        }
        double availableSpace = 0.8 * storageSpace;
        for(Boat b : boatsStored){
            if(b.getInsurance().equals(boat.getInsurance()) || b.getEmail().equals(boat.getEmail())){
                throw new DomainException("addBoat", "boat.owner.already.has.boat.in.storage");
            }
            availableSpace = availableSpace - b.getLength()*b.getWidth();
        }
        if(boat.getWidth()*boat.getLength() >= availableSpace){
            throw new DomainException("addBoat", "no.available.space");
        }
        boatsStored.add(boat);
        boat.setStorage(this);
    }

    public void removeBoat(Boat boat){
        if(!boatsStored.contains(boat)){
            throw new DomainException("get", "boat.not.in.storage");
        } else {
            this.boatsStored.remove(boat);
            boat.setStorage(null);
        }
    }

    public boolean storageIsEmpty(Storage storage){
       if(storage.getBoatsStored() != null && storage.getBoatsStored().size()> 0) {
           return false;
       }
       return true;
    }
}
