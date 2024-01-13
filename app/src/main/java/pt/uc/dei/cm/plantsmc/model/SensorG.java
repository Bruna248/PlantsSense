package pt.uc.dei.cm.plantsmc.model;

import java.io.Serializable;

public class SensorG implements Serializable {
    private String id;
    private String timestamp;
    private Double temperature;
private String measure;

    public SensorG(String id, String type, String description, String measure, String timestamp) {
        this.id=id;
        this.Type=type;
        this.description=description;
        this.measure=measure;
        this.timestamp=timestamp;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public Double getHumidity() {
        return humidity;
    }

    public void setHumidity(Double humidity) {
        this.humidity = humidity;
    }

    private Double humidity;

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    private String value;
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

