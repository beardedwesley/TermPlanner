package tech.wesleystevens.WGU_MobileDev.ViewModel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import java.util.List;
import tech.wesleystevens.WGU_MobileDev.Database.PlannerRepository;
import tech.wesleystevens.WGU_MobileDev.Entities.Course;

public class CourseViewModel extends AndroidViewModel {
    private PlannerRepository repository;
    private LiveData<List<Course>> allCourses;

    public CourseViewModel (Application application) {
        super(application);
        repository = new PlannerRepository(application);
        allCourses = repository.getAllCourses();
    }

    public LiveData<List<Course>> getAllCourses() {
        return allCourses;
    }
    public void insert(Course course) {
        repository.insert(course);
    }
    public void delete(Course course) {
        repository.delete(course);
    }
    public void update(Course course) {
        repository.update(course);
    }

    public int lastCourseID() {
        if (allCourses.getValue() == null) {
            return -1;
        } else {
            return allCourses.getValue().size();
        }
    }
}