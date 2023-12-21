package pt.uc.dei.cm.plantsmc.model;

public class Sensor {
    private int id;
    private SensorType type;
    private boolean isNumeric;
    private String value;
    private int parentId;
    private String parentType;

    public Sensor() {
    }

    public Sensor(int id, SensorType type, boolean isNumeric, String value, int parentId, String parentType) {
        this.id = id;
        this.type = type;
        this.isNumeric = isNumeric;
        this.value = value;
        this.parentId = parentId;
        this.parentType = parentType;
    }

    public int getId() {
        return id;
    }

    public SensorType getType() {
        return type;
    }

    public void setType(SensorType type) {
        this.type = type;
    }

    public boolean isNumeric() {
        return isNumeric;
    }

    public void setNumeric(boolean numeric) {
        isNumeric = numeric;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public String getParentType() {
        return parentType;
    }

    public void setParentType(String parentType) {
        this.parentType = parentType;
    }
}
