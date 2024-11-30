package entity;

public class Progress {
    private int id;
    private int userId;
    private int programId;
    private int day;
    private boolean status;

    public Progress(int userId, int programId, int day, boolean status) {
        this.userId = userId;
        this.programId = programId;
        this.day = day;
        this.status = status;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setProgramId(int programId) {
        this.programId = programId;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public boolean isStatus() {
        return status;
    }

    public int getDay() {
        return day;
    }

    public int getProgramId() {
        return programId;
    }

    public int getUserId() {
        return userId;
    }

    public int getId() {
        return id;
    }
}