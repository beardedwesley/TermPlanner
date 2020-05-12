package tech.wesleystevens.WGU_MobileDev.Entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.util.Calendar;

@Entity(tableName = "term_table")
public class Term {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "ID")
    private int termID;

    @NonNull
    @ColumnInfo(name = "Title")
    private String title;

    @NonNull
    @ColumnInfo(name = "Start")
    private Calendar start;

    @NonNull
    @ColumnInfo(name = "End")
    private Calendar end;

    public Term(@NonNull int termID, @NonNull String title, @NonNull Calendar start, @NonNull Calendar end) {
        this.termID = termID;
        this.title = title;
        this.start = start;
        this.end = end;
    }

    public int getTermID() {
        return termID;
    }
    public void setTermID(int termID) {
        this.termID = termID;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public Calendar getStart() {
        return start;
    }
    public void setStart(Calendar start) {
        this.start = start;
    }

    public Calendar getEnd() {
        return end;
    }
    public void setEnd(Calendar end) {
        this.end = end;
    }

    @Override
    public String toString() {
        return "TermEntity{termID = " + termID +
                ", termTitle = " + title +
                ", termStart = " + start.toString() +
                ", termEnd = " + end.toString() +
                "}";
    }
}
