package pt.uc.dei.cm.plantsmc.model;

public class Plant {
    private int id;
    private String name;
    private int greenhouseId;
    private String specie;

    public Plant(int id, String name, int greenhouseId, String specie) {
        this.id = id;
        this.name = name;
        this.greenhouseId = greenhouseId;
        this.specie = specie;
    }

    public Plant() {
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getGreenhouseId() {
        return greenhouseId;
    }

    public void setGreenhouseId(int greenhouseId) {
        this.greenhouseId = greenhouseId;
    }

    public String getSpecie() {
        return specie;
    }

    public void setSpecie(String specie) {
        this.specie = specie;
    }
}
