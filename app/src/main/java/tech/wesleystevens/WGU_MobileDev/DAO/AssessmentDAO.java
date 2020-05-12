package tech.wesleystevens.WGU_MobileDev.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;
import tech.wesleystevens.WGU_MobileDev.Entities.Assessment;

@Dao
public interface AssessmentDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Assessment assessment);

    @Delete
    void delete(Assessment assessment);

    @Query("SELECT * FROM assessment_table ORDER BY ID ASC")
    LiveData<List<Assessment>> getAllAssessments();

    @Query("SELECT * FROM assessment_table WHERE CourseID = :courseID ORDER BY ID ASC")
    LiveData<List<Assessment>> getAssociatedAssessments(int courseID);

    @Query("DELETE FROM assessment_table")
    void deleteAll();

    @Update
    void update(Assessment assessment);
}
