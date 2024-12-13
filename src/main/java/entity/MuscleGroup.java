package entity;

public class MuscleGroup {
    private int id;
    private String name;
    public MuscleGroup() {
    }

    public MuscleGroup(String name) {
        this.name = name;
    }

    public MuscleGroup(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
