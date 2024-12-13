package entity;

public class Program {
    private int id;
    private String title;
    private String description;
    private int duration;
    private int createdBy;
    public Program(int id, String title, String description, int duration) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.duration = duration;
    }
    public Program(int id, String title, String description, int duration, int createdBy) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.duration = duration;
        this.createdBy = createdBy;
    }

    public Program(String title, String description, int duration, int createdBy) {
        this.title = title;
        this.description = description;
        this.duration = duration;
        this.createdBy = createdBy;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getDuration() {
        return duration;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }
}
