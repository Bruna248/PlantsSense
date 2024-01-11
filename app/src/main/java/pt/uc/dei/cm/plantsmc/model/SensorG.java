package pt.uc.dei.cm.plantsmc.model;

import java.io.Serializable;

public class SensorG implements Serializable {
    private String id;
    private String greenhouseId;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    private String userId;
    private String Type;
    private String description;

    public void setId(String id) {
        this.id = id;
    }

    public void setType(String type) {
        Type = type;
    }

    public SensorG() {
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return Type;
    }


    public SensorG(String id, String Type, String description) {
        this.id = id;
        this.Type = Type;
        this.description=description;
    }

    public SensorG(String Type, String description) {
        this.Type = Type;
        this.description=description;
    }


}

