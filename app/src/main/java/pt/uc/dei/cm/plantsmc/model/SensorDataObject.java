package pt.uc.dei.cm.plantsmc.model;

public class SensorDataObject {
    private String id;
    private Double measurement;
    private String timestamp;
    private SensorType sensorType;

    public SensorDataObject() {
    }

    public SensorDataObject(String id, Double measurement, String timestamp, SensorType sensorType) {
        this.id = id;
        this.measurement = measurement;
        this.timestamp = timestamp;
        this.sensorType = sensorType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Double getMeasurement() {
        return measurement;
    }

    public void setMeasurement(Double measurement) {
        this.measurement = measurement;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public SensorType getSensorType() {
        return sensorType;
    }

    public void setSensorType(SensorType sensorType) {
        this.sensorType = sensorType;
    }
}
