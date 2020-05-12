package tech.wesleystevens.WGU_MobileDev.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import tech.wesleystevens.WGU_MobileDev.DAO.*;
import tech.wesleystevens.WGU_MobileDev.Entities.Assessment;
import tech.wesleystevens.WGU_MobileDev.Entities.Converters;
import tech.wesleystevens.WGU_MobileDev.Entities.Course;
import tech.wesleystevens.WGU_MobileDev.Entities.Term;

@Database(entities = {Term.class, Course.class, Assessment.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class PlannerDatabase extends RoomDatabase {

    public abstract TermDAO termDAO();
    public abstract CourseDAO courseDAO();
    public abstract AssessmentDAO assessmentDAO();

    private static volatile PlannerDatabase INSTANCE;

    static PlannerDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (PlannerDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), PlannerDatabase.class, "planner_database")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
