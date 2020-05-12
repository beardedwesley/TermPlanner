package tech.wesleystevens.WGU_MobileDev.UI;

import android.content.Context;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import java.util.Locale;

import tech.wesleystevens.WGU_MobileDev.CourseActivity;
import tech.wesleystevens.WGU_MobileDev.Entities.Course;
import tech.wesleystevens.WGU_MobileDev.R;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseViewHolder> {
    private final LayoutInflater inflater;
    private final Context context;
    private List<Course> allCourses; // Cached copy of courses
    private SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy", Locale.US);

    /* Inner Class */
    class CourseViewHolder extends RecyclerView.ViewHolder {
        private final TextView courseNameView;
        private final TextView courseStatusView;

        private CourseViewHolder(View itemView) {
            super(itemView);
            courseNameView = itemView.findViewById(R.id.courseListName);
            courseStatusView = itemView.findViewById(R.id.courseListStatus);
            itemView.setOnClickListener((v -> {
                int position = getAdapterPosition();
                final Course current = allCourses.get(position);
                Intent intent = new Intent(context, CourseActivity.class);
                intent.putExtra("courseID", current.getCourseID());
                intent.putExtra("termID", current.getTermID());
                intent.putExtra("courseName", current.getName());
                intent.putExtra("courseStart", sdf.format(current.getStart().getTime()));
                intent.putExtra("courseGoal", sdf.format(current.getEnd().getTime()));
                intent.putExtra("courseStatus", current.getStatus());
                intent.putExtra("courseContact", current.getInstructInfo());
                intent.putExtra("courseNotes", current.getNotes());
                intent.putExtra("position", position);
                context.startActivity(intent);
            }));
        }
    }

    public CourseAdapter(Context context) {
        inflater = LayoutInflater.from(context);
        this.context = context;
    }
    @Override
    public CourseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.course_list_item, parent, false);
        return new CourseViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CourseViewHolder holder, int position) {
        if (allCourses != null) {
            Course current = allCourses.get(position);
            holder.courseNameView.setText(current.getName());
            holder.courseStatusView.setText(current.getStatus());
        } else {
            // Covers the case of data not being ready yet.
            holder.courseNameView.setText("Name Unavailable");
            holder.courseStatusView.setText("Status Unavailable");
        }
    }

    public void setAllCourses(List<Course> courses){
        allCourses = courses;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (allCourses != null)
            return allCourses.size();
        else return 0;
    }
}
