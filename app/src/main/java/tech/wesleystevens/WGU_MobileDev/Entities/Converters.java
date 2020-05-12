package tech.wesleystevens.WGU_MobileDev.Entities;

import android.icu.text.SimpleDateFormat;
import androidx.room.TypeConverter;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Locale;

public class Converters {
    //private static SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy", Locale.US);

    @TypeConverter
    public static Calendar toCalendar(long milis) {
        return  new Calendar.Builder().setInstant(milis).build();
    }
    @TypeConverter
    public static long fromCalendar(Calendar calendar) {
        return calendar.getTimeInMillis();
    }

    @TypeConverter
    public static AssessType toAssessType(String dataString) {
        if (dataString.equalsIgnoreCase("Objective")) {
            return AssessType.OBJECTIVE;
        } else {
            return AssessType.PERFORMANCE;
        }
    }
    @TypeConverter
    public static String fromAssessType(AssessType assessType) {
        return assessType.toString();
    }
}
