package tech.wesleystevens.WGU_MobileDev.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;
import tech.wesleystevens.WGU_MobileDev.Entities.Course;

@Dao
public interface CourseDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Course course);

    @Delete
    void delete(Course course);

    @Query("SELECT * FROM course_table ORDER BY ID ASC")
    LiveData<List<Course>> getAllCourses();

    @Query("SELECT * FROM course_table WHERE termID = :termID ORDER BY ID ASC")
    LiveData<List<Course>> getAssociatedCourses(int termID);

    @Query("DELETE FROM course_table")
    void deleteAll();

    @Update
    void update(Course course);

}
