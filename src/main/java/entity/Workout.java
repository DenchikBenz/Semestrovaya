package entity;


import java.util.ArrayList;
import java.util.List;

public class Workout {
    private int id;
    private int programId;
    private String title;
    private String description;
    private int dayNumber;
    private List<Exercise> exercises;
    public Workout() {
        this.exercises = new ArrayList<>();
    }
    public Workout(String title, String description, int dayNumber, int programId) {
        this.title = title;
        this.description = description;
        this.dayNumber = dayNumber;
        this.programId = programId;
        this.exercises = new ArrayList<>();
    }

    public Workout(int id, String title, String description, int dayNumber, int programId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.dayNumber = dayNumber;
        this.programId = programId;
        this.exercises = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public int getProgramId() {
        return programId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getDayNumber() {
        return dayNumber;
    }

    // Сеттеры
    public void setId(int id) {
        this.id = id;
    }

    public void setProgramId(int programId) {
        this.programId = programId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDayNumber(int dayNumber) {
        this.dayNumber = dayNumber;
    }

    public List<Exercise> getExercises() {
        return exercises;
    }

    public void setExercises(List<Exercise> exercises) {
        this.exercises = exercises;
    }

    public void addExercise(Exercise exercise) {
        if (this.exercises == null) {
            this.exercises = new ArrayList<>();
        }
        this.exercises.add(exercise);
    }

    public void removeExercise(Exercise exercise) {
        if (this.exercises != null) {
            this.exercises.remove(exercise);
        }
    }
}