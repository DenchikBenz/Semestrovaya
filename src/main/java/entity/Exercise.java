package entity;

public class Exercise {
    private int id;
    private int workoutId;
    private int muscleGroupId;
    private String name;
    private String description;
    private int sets;
    private int reps;
    private MuscleGroup muscleGroup;

    public Exercise() {
    }

    public Exercise(int workoutId, int muscleGroupId, String name, String description, int sets, int reps) {
        this.workoutId = workoutId;
        this.muscleGroupId = muscleGroupId;
        this.name = name;
        this.description = description;
        this.sets = sets;
        this.reps = reps;
    }

    // Геттеры и сеттеры
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getWorkoutId() {
        return workoutId;
    }

    public void setWorkoutId(int workoutId) {
        this.workoutId = workoutId;
    }

    public int getMuscleGroupId() {
        return muscleGroupId;
    }

    public void setMuscleGroupId(int muscleGroupId) {
        this.muscleGroupId = muscleGroupId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getSets() {
        return sets;
    }

    public void setSets(int sets) {
        this.sets = sets;
    }

    public int getReps() {
        return reps;
    }

    public void setReps(int reps) {
        this.reps = reps;
    }

    public MuscleGroup getMuscleGroup() {
        return muscleGroup;
    }

    public void setMuscleGroup(MuscleGroup muscleGroup) {
        this.muscleGroup = muscleGroup;
    }
}
