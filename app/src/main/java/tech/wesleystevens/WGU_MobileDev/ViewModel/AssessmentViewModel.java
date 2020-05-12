package tech.wesleystevens.WGU_MobileDev.ViewModel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import java.util.List;
import tech.wesleystevens.WGU_MobileDev.Database.PlannerRepository;
import tech.wesleystevens.WGU_MobileDev.Entities.Assessment;

public class AssessmentViewModel extends AndroidViewModel {
    private PlannerRepository repository;
    private LiveData<List<Assessment>> allAssessments;

    public AssessmentViewModel (Application application) {
        super(application);
        repository = new PlannerRepository(application);
        allAssessments = repository.getAllAssessments();
    }

    public LiveData<List<Assessment>> getAllAssessments() {
        return allAssessments;
    }
    public void insert(Assessment assessment) {
        repository.insert(assessment);
    }
    public void delete(Assessment assessment) {
        repository.delete(assessment);
    }
    public void update(Assessment assessment) {
        repository.update(assessment);
    }

    public int lastAssessmentID() {
        if (allAssessments.getValue() == null) {
            return -1;
        } else {
            return allAssessments.getValue().size();
        }
    }
}