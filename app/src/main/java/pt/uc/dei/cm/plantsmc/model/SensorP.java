package pt.uc.dei.cm.plantsmc.model;

import java.io.Serializable;

public class SensorP implements Serializable {
    private String id;
    private String Type;
    private String greenhouseId;
    private String plantId;

    public String getPlantId() {
        return plantId;
    }

    public void setPlantId(String plantId) {
        this.plantId = plantId;
    }

    private String userId;

    public String getGreenhouseId() {
        return greenhouseId;
    }

    public void setGreenhouseId(String greenhouseId) {
        this.greenhouseId = greenhouseId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    private String description;

    public void setId(String id) {
        this.id = id;
    }

    public void setType(String type) {
        this.Type = type;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public SensorP() {
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return Type;
    }


    public SensorP(String id, String Type, String description) {
        this.id = id;
        this.Type = Type;
        this.description=description;
    }

    public SensorP(String Type, String description) {
        this.Type = Type;
        this.description=description;
    }


}

