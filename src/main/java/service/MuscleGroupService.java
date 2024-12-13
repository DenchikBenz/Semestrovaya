package service;
import dao.MuscleGroupDao;
import entity.MuscleGroup;
import java.util.List;
public class MuscleGroupService {
    private final MuscleGroupDao muscleGroupDao = new MuscleGroupDao();
    public List<MuscleGroup> getAllMuscleGroups() {
        return muscleGroupDao.findAll();
    }
    public MuscleGroup getMuscleGroupById(int id) {
        return muscleGroupDao.findById(id);
    }
}
