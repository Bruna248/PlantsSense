package pt.uc.dei.cm.plantsmc.model;

public class Settings {
    private int userId;
    private int sensorId;
    private boolean shouldAlert;
    private String value;
    private boolean isNumeric;

    public Settings(int userId, int sensorId, boolean shouldAlert, String value, boolean isNumeric) {
        this.userId = userId;
        this.sensorId = sensorId;
        this.shouldAlert = shouldAlert;
        this.value = value;
        this.isNumeric = isNumeric;
    }

    public Settings() {
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getSensorId() {
        return sensorId;
    }

    public void setSensorId(int sensorId) {
        this.sensorId = sensorId;
    }

    public boolean isShouldAlert() {
        return shouldAlert;
    }

    public void setShouldAlert(boolean shouldAlert) {
        this.shouldAlert = shouldAlert;
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
}
