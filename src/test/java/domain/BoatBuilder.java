package domain;

import be.ucll.ip.minor.groep124.model.Boat;

public class BoatBuilder {
    private String name;
    private String email;
    private int length;
    private int height;
    private int width;
    private String insurance;

    private BoatBuilder() {
    }

    public static BoatBuilder aBoat() {
        return new BoatBuilder();
    }

    //VALID BOATS
    public static BoatBuilder aValidBoat() {
        return new BoatBuilder()
                .withName("BoatName")
                .withEmail("email@email.com")
                .withLength(1)
                .withHeight(1)
                .withWidth(1)
                .withInsurance("RFA2347885");
    }

    public static BoatBuilder boat1(){
        return new BoatBuilder()
                .withName("BoatName1")
                .withEmail("email1@email.com")
                .withLength(2)
                .withHeight(2)
                .withWidth(2)
                .withInsurance("123456ABCD");
    }

    public static BoatBuilder boat2(){
        return new BoatBuilder()
                .withName("BoatName2")
                .withEmail("email2@email.com")
                .withLength(3)
                .withHeight(3)
                .withWidth(3)
                .withInsurance("ABCD123456");
    }

    //INVALID BOATS
    public static BoatBuilder anInvalidBoatWithNoName() {
        return new BoatBuilder()
                .withName(null)
                .withEmail("email@email")
                .withLength(1)
                .withHeight(1)
                .withWidth(1)
                .withInsurance("RFA2347885");

    }

    public static BoatBuilder anInvalidBoatWithNoEmail() {
        return new BoatBuilder()
                .withName("BoatName")
                .withEmail(null)
                .withLength(1)
                .withHeight(1)
                .withWidth(1)
                .withInsurance("RFA2347885");

    }

    public static BoatBuilder anInvalidBoatWithNoInsurance() {
        return new BoatBuilder()
                .withName("BoatName")
                .withEmail("email@email")
                .withLength(1)
                .withHeight(1)
                .withWidth(1)
                .withInsurance(null);

    }

    public static BoatBuilder anInvalidBoatWithNoLength() {
        return new BoatBuilder()
                .withName("BoatName")
                .withEmail("email@email")
                .withLength(0)
                .withHeight(1)
                .withWidth(1)
                .withInsurance("RFA2347885");

    }

    public static BoatBuilder anInvalidBoatWithNoHeight() {
        return new BoatBuilder()
                .withName("BoatName")
                .withEmail("email@email")
                .withLength(1)
                .withHeight(0)
                .withWidth(1)
                .withInsurance("RFA2347885");

    }

    public static BoatBuilder anInvalidBoatWithNoWidth() {
        return new BoatBuilder()
                .withName("BoatName")
                .withEmail("email@email")
                .withLength(1)
                .withHeight(1)
                .withWidth(0)
                .withInsurance("RFA2347885");

    }


    //PARAMETERBUILDERS
    public BoatBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public BoatBuilder withEmail(String email) {
        this.email = email;
        return this;
    }

    public BoatBuilder withLength(int length) {
        this.length = length;
        return this;
    }

    public BoatBuilder withHeight(int height) {
        this.height = height;
        return this;
    }

    public BoatBuilder withWidth(int width) {
        this.width = width;
        return this;
    }

    public BoatBuilder withInsurance(String insurance) {
        this.insurance = insurance;
        return this;
    }

    public Boat build() {
        Boat boat = new Boat();
        boat.setName(name);
        boat.setEmail(email);
        boat.setLength(length);
        boat.setHeight(height);
        boat.setWidth(width);
        boat.setInsurance(insurance);
        return boat;
    }
}
