package pt.uc.dei.cm.plantsmc.model;

public class Greenhouse {
    private int id;
    private String name;

    public Greenhouse(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Greenhouse() {
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
