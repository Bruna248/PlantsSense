package pt.uc.dei.cm.plantsmc.model;

import java.io.Serializable;

public class Plant implements Serializable {
    private String id;
    private String name;
    private String greenhouseId;
    private String userId;
    private String specie;

    public Plant(String id, String name, String greenhouseId, String userId, String specie) {
        this.id = id;
        this.name = name;
        this.greenhouseId = greenhouseId;
        this.userId = userId;
        this.specie = specie;
    }

    public Plant(String name, String greenhouseId, String userId, String specie) {
        this.name = name;
        this.greenhouseId = greenhouseId;
        this.userId = userId;
        this.specie = specie;
    }

    public Plant(String name, String specie) {
        this.name = name;
        this.specie = specie;
    }

    public Plant() {
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGreenhouseId() {
        return greenhouseId;
    }

    public void setGreenhouseId(String greenhouseId) {
        this.greenhouseId = greenhouseId;
    }

    public String getSpecie() {
        return specie;
    }

    public void setSpecie(String specie) {
        this.specie = specie;
    }
}
