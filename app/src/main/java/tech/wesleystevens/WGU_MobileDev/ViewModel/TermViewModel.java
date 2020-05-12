package tech.wesleystevens.WGU_MobileDev.ViewModel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import java.util.List;
import tech.wesleystevens.WGU_MobileDev.Database.PlannerRepository;
import tech.wesleystevens.WGU_MobileDev.Entities.Term;

public class TermViewModel extends AndroidViewModel {
    private PlannerRepository repository;
    private LiveData<List<Term>> allTerms;

    public TermViewModel (Application application) {
        super(application);
        repository = new PlannerRepository(application);
        allTerms = repository.getAllTerms();
    }

    public LiveData<List<Term>> getAllTerms() {
        return allTerms;
    }
    public void insert(Term term) {
        repository.insert(term);
    }
    public void delete(Term term) {
        repository.delete(term);
    }
    public void update(Term term) {
        repository.update(term);
    }

    public int lastTermID() {
        if (allTerms.getValue() == null) {
            return -1;
        } else {
            return allTerms.getValue().size();
        }
    }
}