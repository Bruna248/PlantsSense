package pt.uc.dei.cm.plantsmc.model;

import java.io.Serializable;
import java.util.Objects;

public class Greenhouse implements Serializable {
    private String id;
    private String name;
    private String userId;
    private Double latitude;
    private Double longitude;

    public Greenhouse(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public Greenhouse(String id, String name, String userId) {
        this.id = id;
        this.name = name;
        this.userId = userId;
    }

    public Greenhouse(String name, Double latitude, Double longitude) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Greenhouse(String id, String name, String userId, Double latitude, Double longitude) {
        this.id = id;
        this.name = name;
        this.userId = userId;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Greenhouse(String name) {
        this.name = name;
    }

    public Greenhouse() {
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Greenhouse that = (Greenhouse) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
