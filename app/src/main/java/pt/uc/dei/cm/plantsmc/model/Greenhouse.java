package pt.uc.dei.cm.plantsmc.model;

import java.io.Serializable;

public class Greenhouse implements Serializable {
    private String id;
    private String name;
    private String userId;

    public Greenhouse(String id, String name) {
        this.id = id;
        this.name = name;
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
}
