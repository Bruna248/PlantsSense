package pt.uc.dei.cm.plantsmc.model;

public class Alert {
    private int id;
    private int sensorId;
    private int greenhouseId;
    private String description;
    private String timestamp;

    public Alert(int id, int sensorId, int greenhouseId, String description, String timestamp) {
        this.id = id;
        this.sensorId = sensorId;
        this.greenhouseId = greenhouseId;
        this.description = description;
        this.timestamp = timestamp;
    }

    public Alert() {
    }

    public int getId() {
        return id;
    }

    public int getSensorId() {
        return sensorId;
    }

    public void setSensorId(int sensorId) {
        this.sensorId = sensorId;
    }

    public int getGreenhouseId() {
        return greenhouseId;
    }

    public void setGreenhouseId(int greenhouseId) {
        this.greenhouseId = greenhouseId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
