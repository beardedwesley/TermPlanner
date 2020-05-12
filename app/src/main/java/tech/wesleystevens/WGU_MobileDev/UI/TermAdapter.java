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

import tech.wesleystevens.WGU_MobileDev.Entities.Term;
import tech.wesleystevens.WGU_MobileDev.R;
import tech.wesleystevens.WGU_MobileDev.TermActivity;

public class TermAdapter extends RecyclerView.Adapter<TermAdapter.TermViewHolder> {
    private final LayoutInflater inflater;
    private final Context context;
    private List<Term> allTerms; // Cached copy of terms
    private SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy", Locale.US);

    /* Inner Class*/
    class TermViewHolder extends RecyclerView.ViewHolder {
        private final TextView termNameView;

        private TermViewHolder(View itemView) {
            super(itemView);
            termNameView = itemView.findViewById(R.id.termListTitle);
            itemView.setOnClickListener((v -> {
                int position = getAdapterPosition();
                final Term current = allTerms.get(position);
                Intent intent = new Intent(context, TermActivity.class);
                intent.putExtra("termID", current.getTermID());
                intent.putExtra("termTitle", current.getTitle());
                intent.putExtra("termStart", sdf.format(current.getStart().getTime()));
                intent.putExtra("termEnd", sdf.format(current.getEnd().getTime()));
                intent.putExtra("position", position);
                context.startActivity(intent);
            }));
        }
    }

    /* Main Class Methods*/
    public TermAdapter(Context context) {
        inflater = LayoutInflater.from(context);
        this.context = context;
    }

    @Override
    public TermViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.term_list_item, parent, false);
        return new TermViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TermViewHolder holder, int position) {
        if (allTerms != null) {
            Term current = allTerms.get(position);
            holder.termNameView.setText(current.getTitle());
        } else {
            // Covers the case of data not being ready yet.
            holder.termNameView.setText("Title Unavailable");
        }
    }

    public void setAllTerms(List<Term> terms){
        allTerms = terms;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (allTerms != null)
            return allTerms.size();
        else return 0;
    }
}
