package be.ucll.ip.minor.groep124.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.persistence.Column;
import jakarta.validation.constraints.*;

public class StorageDto {
    private long id;

    @Size(min = 5, message = "name-characters.too.few")
    @NotBlank(message = "name.missing")
    @Column(name = "name")
    private String name;

    @NotBlank(message = "postal-code.missing")
    @Pattern(regexp = "^[0-9]{4}$", message = "postal-code.wrong")
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
    @Column(name = "year")
    private int year;

    public StorageDto(){

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

    public Storage createStorage() {
        Storage storage = new Storage();
        storage.setName(this.getName());
        storage.setPostalCode(this.getPostalCode());
        storage.setStorageSpace(this.getStorageSpace());
        storage.setHeight(this.getHeight());
        storage.setYear(this.getYear());

        return storage;
    }

    public Storage updateStorage(Storage storage){
        storage.setName(this.getName());
        storage.setPostalCode(this.getPostalCode());
        storage.setStorageSpace(this.getStorageSpace());
        storage.setHeight(this.getHeight());
        storage.setYear(this.getYear());

        return storage;
    }
}
