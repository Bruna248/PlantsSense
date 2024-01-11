package pt.uc.dei.cm.plantsmc.model;

import java.io.Serializable;

public class SensorData implements Serializable {
    private String id;
    private Double temperature;
    private Double humidity;
    private boolean light;
    private String parentId;
    private String parentType;
    private String timestamp;

    public SensorData() {
    }

    public SensorData(String id,Double temperature, Double humidity, String parentId, String parentType, String timestamp) {
        this.id = id;
        this.temperature = temperature;
        this.humidity = humidity;
        this.parentId = parentId;
        this.parentType = parentType;
        this.timestamp = timestamp;
    }

    public SensorData(String id, String parentId, String parentType, String timestamp) {
        this.id = id;
        this.parentId = parentId;
        this.parentType = parentType;
        this.timestamp = timestamp;
    }

    public SensorData(String sensorName) {
        this.parentType=sensorName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public boolean isLight() {
        return light;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public void setLight(boolean light) {
        this.light = light;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getParentType() {
        return parentType;
    }

    public void setParentType(String parentType) {
        this.parentType = parentType;
    }
}
