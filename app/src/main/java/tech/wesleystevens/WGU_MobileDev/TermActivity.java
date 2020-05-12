package tech.wesleystevens.WGU_MobileDev;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import tech.wesleystevens.WGU_MobileDev.Entities.Course;
import tech.wesleystevens.WGU_MobileDev.Entities.Term;
import tech.wesleystevens.WGU_MobileDev.UI.CourseAdapter;
import tech.wesleystevens.WGU_MobileDev.ViewModel.CourseViewModel;
import tech.wesleystevens.WGU_MobileDev.ViewModel.TermViewModel;


public class TermActivity extends AppCompatActivity {
    private CourseViewModel courseViewModel;
    private TermViewModel termViewModel;
    private EditText termTitle;
    private TextView termStart;
    private TextView termEnd;
    private Button saveButton;
    private boolean flagEdit = false;
    private DatePickerDialog.OnDateSetListener date;
    final Calendar calendar = Calendar.getInstance();
    private SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy", Locale.US);
    private int termID = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        courseViewModel = new ViewModelProvider(this).get(CourseViewModel.class);
        termViewModel = new ViewModelProvider(this).get(TermViewModel.class);

        termTitle = findViewById(R.id.termTitle);
        termStart = findViewById(R.id.termStartDate);
        termEnd = findViewById(R.id.termEndDate);
        saveButton = findViewById(R.id.termSaveButton);

        RecyclerView recyclerView = findViewById(R.id.courseListView);
        final CourseAdapter adapter = new CourseAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setVisibility(View.GONE);

        date = (view, year, monthOfYear, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateTermStartEnd();
        };

        termStart.setOnClickListener(v -> {
            new DatePickerDialog(TermActivity.this, date, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
        });

        if (getIntent().hasExtra("termTitle")) {
            flagEdit = true;
            fab.setVisibility(View.VISIBLE);
            fab.setOnClickListener(view -> {
                Intent intent = new Intent(TermActivity.this, CourseActivity.class);
                intent.putExtra("termID", getIntent().getIntExtra("termID", -1));
                startActivityForResult(intent, 1);
            });

            termTitle.setText(getIntent().getStringExtra("termTitle"));
            termStart.setText(getIntent().getCharSequenceExtra("termStart"));
            termEnd.setText(getIntent().getCharSequenceExtra("termEnd"));

            termID = getIntent().getIntExtra("termID", -1);
        }

        courseViewModel.getAllCourses().observe(this, courses -> {
            List<Course> associatedCourses = new ArrayList<>();
            for (Course course : courses) {
                if (course.getTermID() == termID) {
                    associatedCourses.add(course);
                }
            }
            adapter.setAllCourses(associatedCourses);
        });

        saveButton.setOnClickListener((v -> {
            Intent reply = new Intent();
            String tTitle = termTitle.getText().toString();
            Calendar tStart, tEnd;
            try {
                tStart = new Calendar.Builder().setInstant(sdf.parse(termStart.getText().toString())).build();
                tEnd = new Calendar.Builder().setInstant(sdf.parse(termEnd.getText().toString())).build();
            } catch (ParseException e) {
                Toast.makeText(getApplicationContext(), "Invalid date", Toast.LENGTH_SHORT);
                return;
            }
            reply.putExtra("termTitle", tTitle);
            reply.putExtra("termStart", termStart.getText().toString());
            reply.putExtra("termEnd", termEnd.getText().toString());
            if (flagEdit) {
                Term nTerm = new Term(getIntent().getIntExtra("termID", -1), tTitle, tStart, tEnd);
                termViewModel.update(nTerm);
            } else {
                //Term nTerm = new Term(termViewModel.lastTermID() + 1, tTitle, tStart, tEnd);
                //termViewModel.insert(nTerm);
                setResult(RESULT_OK, reply);
                finish();
            }
        }));
    }

    private void updateTermStartEnd() {
        termStart.setText(sdf.format(calendar.getTime()));
        calendar.add(Calendar.MONTH, 6);
        termEnd.setText(sdf.format(calendar.getTime()));
        calendar.add(Calendar.MONTH, -6);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_delete, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.delete) {
            boolean flagHasAssociatedCourses = false;
            int termID = getIntent().getIntExtra("termID", -1);
            List<Course> courses = courseViewModel.getAllCourses().getValue();
            if (courses != null) {
                for (Course course : courses){
                    if (course.getTermID() == termID) {
                        flagHasAssociatedCourses = true;
                        break;
                    }
                }
            }

            if (flagHasAssociatedCourses) {
                Toast.makeText(getApplicationContext(), "Can't delete a term with associated courses.", Toast.LENGTH_SHORT).show();
                return super.onOptionsItemSelected(item);
            } else {
                Calendar tStart, tEnd;
                try {
                    tStart = new Calendar.Builder().setInstant(sdf.parse(getIntent().getStringExtra("termStart"))).build();
                    tEnd = new Calendar.Builder().setInstant(sdf.parse(getIntent().getStringExtra("termEnd"))).build();
                } catch (ParseException e) {
                    Toast.makeText(getApplicationContext(), "Unexpected error, unable to delete.", Toast.LENGTH_SHORT).show();
                    return super.onOptionsItemSelected(item);
                }
                termViewModel.delete(new Term(termID, getIntent().getStringExtra("termTitle"), tStart, tEnd));
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            int courseID = courseViewModel.lastCourseID() + 1;
            Calendar cStart, cEnd;
            try {
                cStart = new Calendar.Builder().setInstant(sdf.parse(data.getStringExtra("courseStart"))).build();
                cEnd = new Calendar.Builder().setInstant(sdf.parse(data.getStringExtra("courseGoal"))).build();
            } catch (ParseException e) {
                Toast.makeText(getApplicationContext(), "Unexpected error, unable to save Course.", Toast.LENGTH_SHORT).show();
                return;
            }
            courseViewModel.insert(new Course(
                    courseID,
                    data.getIntExtra("termID", -1),
                    data.getStringExtra("courseName"),
                    cStart,
                    cEnd,
                    data.getStringExtra("courseStatus"),
                    data.getStringExtra("courseContact"),
                    data.getStringExtra("courseNotes")
            ));
        }
    }
}