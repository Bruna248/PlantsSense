package pt.uc.dei.cm.plantsmc.model;

public class History {
    private int sensorId;
    private String value;
    private boolean isNumeric;
    private String timestamp;

    public History(int sensorId, String value, boolean isNumeric, String timestamp) {
        this.sensorId = sensorId;
        this.value = value;
        this.isNumeric = isNumeric;
        this.timestamp = timestamp;
    }

    public History() {
    }

    public int getSensorId() {
        return sensorId;
    }

    public void setSensorId(int sensorId) {
        this.sensorId = sensorId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isNumeric() {
        return isNumeric;
    }

    public void setNumeric(boolean numeric) {
        isNumeric = numeric;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
