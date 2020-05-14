package tech.wesleystevens.WGU_MobileDev.Entities;

import java.util.Calendar;
import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "assessment_table")

public class Assessment {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "ID")
    private int assessmentID;

    @NonNull
    @ColumnInfo(name = "CourseID")
    private int courseID;

    @NonNull
    @ColumnInfo(name = "Name")
    private String name;

    @NonNull
    @ColumnInfo(name = "Type")
    private AssessType assessType;

    @ColumnInfo(name = "Goal")
    private Calendar goal;

    @ColumnInfo(name = "Notes")
    private String notes;

    public Assessment(@NonNull int assessmentID, @NonNull int courseID, @NonNull String name, @NonNull AssessType assessType, Calendar goal, String notes) {
        this.assessmentID = assessmentID;
        this.courseID = courseID;
        this.name = name;
        this.assessType = assessType;
        this.goal = goal;
        this.notes = notes;
    }

    public int getAssessmentID() {
        return assessmentID;
    }
    public void setAssessmentID(int assessmentID) {
        this.assessmentID = assessmentID;
    }

    public int getCourseID() {
        return courseID;
    }
    public void setCourseID(int courseID) {
        this.courseID = courseID;
    }

    public String getName() {
        return name;
    }
    public void setName(@NonNull String name) {
        this.name = name;
    }

    @NonNull
    public AssessType getAssessType() {
        return assessType;
    }
    public void setAssessType(@NonNull AssessType assessType) {
        this.assessType = assessType;
    }

    public Calendar getGoal() {
        return goal;
    }
    public void setGoal(Calendar goal) {
        this.goal = goal;
    }

    public String getNotes() {
        return notes;
    }
    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public String toString() {
        return "AssessmentEntity{assessmentID = " + assessmentID +
                ", assessmentType = " + assessType.toString() +
                ", goal = " + goal.toString() +
                "}";
    }
}
