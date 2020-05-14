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

import tech.wesleystevens.WGU_MobileDev.AssessmentActivity;
import tech.wesleystevens.WGU_MobileDev.Entities.Assessment;
import tech.wesleystevens.WGU_MobileDev.R;

public class AssessmentAdapter extends RecyclerView.Adapter<AssessmentAdapter.AssessmentViewHolder>{
    private final LayoutInflater inflater;
    private final Context context;
    private List<Assessment> allAssessments; // Cached copy of assessments
    private SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy", Locale.US);

    /* Inner Class */
    class AssessmentViewHolder extends RecyclerView.ViewHolder {
        private final TextView assessmentNameView;
        private final TextView assessmentTypeView;

        private AssessmentViewHolder(View itemView) {
            super(itemView);
            assessmentNameView = itemView.findViewById(R.id.assessmentListName);
            assessmentTypeView = itemView.findViewById(R.id.assessmentListType);
            itemView.setOnClickListener((v -> {
                int position = getAdapterPosition();
                final Assessment current = allAssessments.get(position);
                Intent intent = new Intent(context, AssessmentActivity.class);
                intent.putExtra("assessmentID", current.getAssessmentID());
                intent.putExtra("courseID", current.getCourseID());
                intent.putExtra("assessmentName", current.getName());
                intent.putExtra("assessmentType", current.getAssessType().toString());
                intent.putExtra("assessmentGoal", sdf.format(current.getGoal().getTime()));
                intent.putExtra("assessmentNotes", current.getNotes());
                intent.putExtra("position", position);
                context.startActivity(intent);
            }));
        }
    }


    public AssessmentAdapter(Context context) {
        inflater = LayoutInflater.from(context);
        this.context = context;
    }
    @Override
    public AssessmentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.assessment_list_item, parent, false);
        return new AssessmentViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(AssessmentViewHolder holder, int position) {
        if (allAssessments != null) {
            Assessment current = allAssessments.get(position);
            holder.assessmentNameView.setText(current.getName());
            holder.assessmentTypeView.setText(current.getAssessType().toString());
        } else {
            // Covers the case of data not being ready yet.
            holder.assessmentNameView.setText("Name Unavailable");
            holder.assessmentTypeView.setText("Type unavailable");
        }
    }

    public void setAllAssessments(List<Assessment> assessments){
        allAssessments = assessments;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (allAssessments != null)
            return allAssessments.size();
        else return 0;
    }
}
