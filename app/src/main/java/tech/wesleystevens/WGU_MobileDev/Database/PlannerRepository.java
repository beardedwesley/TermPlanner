package tech.wesleystevens.WGU_MobileDev.Database;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import java.util.List;

import tech.wesleystevens.WGU_MobileDev.DAO.*;
import tech.wesleystevens.WGU_MobileDev.Entities.*;

public class PlannerRepository {
    private TermDAO termDAO;
    private CourseDAO courseDAO;
    private AssessmentDAO assessmentDAO;

    private LiveData<List<Term>> allTerms;
    private LiveData<List<Course>> allCourses;
    private LiveData<List<Course>> associatedCourses;
    private LiveData<List<Assessment>> allAssessments;
    private LiveData<List<Assessment>> associatedAssessments;

    public PlannerRepository(Application application) {
        PlannerDatabase db = PlannerDatabase.getDatabase(application);
        termDAO = db.termDAO();
        courseDAO = db.courseDAO();
        assessmentDAO = db.assessmentDAO();

        allTerms = termDAO.getAllTerms();
        allCourses = courseDAO.getAllCourses();
        allAssessments = assessmentDAO.getAllAssessments();
    }

    public LiveData<List<Term>> getAllTerms() {
        return allTerms;
    }
    public LiveData<List<Course>> getAllCourses() {
        return allCourses;
    }
    public LiveData<List<Assessment>> getAllAssessments() {
        return allAssessments;
    }

    public LiveData<List<Course>> getAssociatedCourses(int termID) {
        associatedCourses = courseDAO.getAssociatedCourses(termID);
        return associatedCourses;
    }
    public LiveData<List<Assessment>> getAssociatedAssessments(int courseID) {
        associatedAssessments = assessmentDAO.getAssociatedAssessments(courseID);
        return associatedAssessments;
    }

    public void insert(Term term) {
        new insertTermAsync(termDAO).execute(term);
    }
    public void insert(Course course) {
        new insertCourseAsync(courseDAO).execute(course);
    }
    public void insert(Assessment assessment) {
        new insertAssessmentAsync(assessmentDAO).execute(assessment);
    }

    public void delete(Term term) {
        new deleteTermAsync(termDAO).execute(term);
    }
    public void delete(Course course) {
        new deleteCourseAsync(courseDAO).execute(course);
    }
    public void delete(Assessment assessment) {
        new deleteAssessmentAsync(assessmentDAO).execute(assessment);
    }

    public void update(Term term) {
        new updateTermAsync(termDAO).execute(term);
    }
    public void update(Course course) {
        new updateCourseAsync(courseDAO).execute(course);
    }
    public void update(Assessment assessment) {
        new updateAssessmentAsync(assessmentDAO).execute(assessment);
    }

    private static class insertTermAsync extends AsyncTask<Term, Void, Void> {
        private TermDAO termDAOAsync;

        insertTermAsync(TermDAO dao) {
            termDAOAsync = dao;
        }

        @Override
        protected Void doInBackground(final Term... params) {
            termDAOAsync.insert(params[0]);
            return null;
        }
    }
    private static class insertCourseAsync extends AsyncTask<Course, Void, Void> {
        private CourseDAO courseDAOAsync;

        insertCourseAsync(CourseDAO dao) {
            courseDAOAsync = dao;
        }

        @Override
        protected Void doInBackground(final Course... params) {
            courseDAOAsync.insert(params[0]);
            return null;
        }
    }
    private static class insertAssessmentAsync extends AsyncTask<Assessment, Void, Void> {
        private AssessmentDAO assessmentDAOAsync;

        insertAssessmentAsync(AssessmentDAO dao) {
            assessmentDAOAsync = dao;
        }

        @Override
        protected Void doInBackground(final Assessment... params) {
            assessmentDAOAsync.insert(params[0]);
            return null;
        }
    }

    private static class deleteTermAsync extends AsyncTask<Term, Void, Void> {
        private TermDAO termDAOAsync;

        deleteTermAsync(TermDAO dao) {
            termDAOAsync = dao;
        }

        @Override
        protected Void doInBackground(final Term... params) {
            termDAOAsync.delete(params[0]);
            return null;
        }
    }
    private static class deleteCourseAsync extends AsyncTask<Course, Void, Void> {
        private CourseDAO courseDAOAsync;

        deleteCourseAsync(CourseDAO dao) {
            courseDAOAsync = dao;
        }

        @Override
        protected Void doInBackground(final Course... params) {
            courseDAOAsync.delete(params[0]);
            return null;
        }
    }
    private static class deleteAssessmentAsync extends AsyncTask<Assessment, Void, Void> {
        private AssessmentDAO assessmentDAOAsync;

        deleteAssessmentAsync(AssessmentDAO dao) {
            assessmentDAOAsync = dao;
        }

        @Override
        protected Void doInBackground(final Assessment... params) {
            assessmentDAOAsync.delete(params[0]);
            return null;
        }
    }

    private static class updateTermAsync extends AsyncTask<Term, Void, Void> {
        private TermDAO termDAOAsync;

        updateTermAsync(TermDAO dao) {
            termDAOAsync = dao;
        }

        @Override
        protected Void doInBackground(final Term... params) {
            termDAOAsync.update(params[0]);
            return null;
        }
    }
    private static class updateCourseAsync extends AsyncTask<Course, Void, Void> {
        private CourseDAO courseDAOAsync;

        updateCourseAsync(CourseDAO dao) {
            courseDAOAsync = dao;
        }

        @Override
        protected Void doInBackground(final Course... params) {
            courseDAOAsync.update(params[0]);
            return null;
        }
    }
    private static class updateAssessmentAsync extends AsyncTask<Assessment, Void, Void> {
        private AssessmentDAO assessmentDAOAsync;

        updateAssessmentAsync(AssessmentDAO dao) {
            assessmentDAOAsync = dao;
        }

        @Override
        protected Void doInBackground(final Assessment... params) {
            assessmentDAOAsync.update(params[0]);
            return null;
        }
    }


    /*
    public void deleteAllTerms() {
        PlannerDatabase.databaseWriteExecutor.execute(() -> {
            termDAO.deleteAll();
        });
    }
    public void deleteAllCourses() {
        PlannerDatabase.databaseWriteExecutor.execute(() -> {
            courseDAO.deleteAll();
        });
    }
    public void deleteAllAssessments() {
        PlannerDatabase.databaseWriteExecutor.execute(() -> {
            assessmentDAO.deleteAll();
        });
    }
     */
}
