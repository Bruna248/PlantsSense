package pt.uc.dei.cm.plantsmc.model;

import java.io.Serializable;

public class SensorData implements Serializable {
    private String id;
    private Double temperature;
    private Double humidity;
    private boolean light;
    private String parentId;
    private SensorHolderType parentType;
    private String timestamp;
    private String userId;

    public SensorData() {
    }

    public SensorData(String id,Double temperature, Double humidity, String parentId, SensorHolderType parentType, String timestamp, String userId) {
        this.id = id;
        this.temperature = temperature;
        this.humidity = humidity;
        this.parentId = parentId;
        this.parentType = parentType;
        this.timestamp = timestamp;
        this.userId = userId;
    }

    public SensorData(String id, String parentId, SensorHolderType parentType, String timestamp) {
        this.id = id;
        this.parentId = parentId;
        this.parentType = parentType;
        this.timestamp = timestamp;
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

    public SensorHolderType getParentType() {
        return parentType;
    }

    public void setParentType(SensorHolderType parentType) {
        this.parentType = parentType;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public SensorDataObject toSensorDataObject(SensorType sensorType) {
        switch (sensorType) {
            case TEMPERATURE:
                return new SensorDataObject(this.id, this.temperature, this.timestamp, sensorType);
            case HUMIDITY:
                return new SensorDataObject(this.id, this.humidity, this.timestamp, sensorType);
            case LIGHT:
                return new SensorDataObject(this.id, this.light ? 1.0 : 0.0, this.timestamp, sensorType);
            default:
                return null;
        }
    }
}
