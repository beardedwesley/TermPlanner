package tech.wesleystevens.WGU_MobileDev.Entities;

import java.util.Calendar;
import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "course_table")
public class Course {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "ID")
    private int courseID;

    @NonNull
    @ColumnInfo(name = "TermID")
    private int termID;

    @NonNull
    @ColumnInfo(name = "Name")
    private String name;

    @NonNull
    @ColumnInfo(name = "Start")
    private Calendar start;

    @NonNull
    @ColumnInfo(name = "End")
    private Calendar end;

    @ColumnInfo(name = "Status")
    private String status;

    @ColumnInfo(name = "Contact")
    private String instructInfo;

    @ColumnInfo(name = "Notes")
    private String notes;

    public Course(@NonNull int courseID, @NonNull int termID, @NonNull String name, @NonNull Calendar start, @NonNull Calendar end, String status, String instructInfo, String notes) {
        this.courseID = courseID;
        this.termID = termID;
        this.name = name;
        this.start = start;
        this.end = end;
        this.status = status;
        this.instructInfo = instructInfo;
        this.notes = notes;
    }

    public int getCourseID() {
        return courseID;
    }
    public void setCourseID(int courseID) {
        this.courseID = courseID;
    }

    public int getTermID() {
        return termID;
    }
    public void setTermID(int termID) {
        this.termID = termID;
    }

    @NonNull
    public String getName() {
        return name;
    }
    public void setName(@NonNull String title) {
        this.name = title;
    }

    @NonNull
    public Calendar getStart() {
        return start;
    }
    public void setStart(@NonNull Calendar start) {
        this.start = start;
    }

    @NonNull
    public Calendar getEnd() {
        return end;
    }
    public void setEnd(@NonNull Calendar end) {
        this.end = end;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    public String getInstructInfo() {
        return instructInfo;
    }
    public void setInstructInfo(String instructInfo) {
        this.instructInfo = instructInfo;
    }

    public String getNotes() {
        return notes;
    }
    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public String toString() {
        return "CourseEntity{courseID = " + courseID +
                ", courseName = " + name +
                ", courseStart = " + start.toString() +
                ", courseEnd = " + end.toString() +
                "}";
    }
}
