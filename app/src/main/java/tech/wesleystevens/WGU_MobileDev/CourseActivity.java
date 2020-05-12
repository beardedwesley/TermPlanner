package tech.wesleystevens.WGU_MobileDev;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
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

import tech.wesleystevens.WGU_MobileDev.Entities.AssessType;
import tech.wesleystevens.WGU_MobileDev.Entities.Assessment;
import tech.wesleystevens.WGU_MobileDev.Entities.Course;
import tech.wesleystevens.WGU_MobileDev.UI.AssessmentAdapter;
import tech.wesleystevens.WGU_MobileDev.ViewModel.AssessmentViewModel;
import tech.wesleystevens.WGU_MobileDev.ViewModel.CourseViewModel;

public class CourseActivity extends AppCompatActivity {
    private AssessmentViewModel assessmentViewModel;
    private CourseViewModel courseViewModel;
    private EditText courseName;
    private TextView courseStart;
    private TextView courseEnd;
    private EditText courseStatus;
    private EditText courseContact;
    private EditText courseNotes;
    private Button saveButton;
    private boolean flagEdit = false;
    private DatePickerDialog.OnDateSetListener dateStart;
    private DatePickerDialog.OnDateSetListener dateEnd;
    final Calendar calendar = Calendar.getInstance();
    private SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy", Locale.US);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        RecyclerView recyclerView = findViewById(R.id.assessmentListView);
        final AssessmentAdapter adapter = new AssessmentAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setVisibility(View.GONE);

        assessmentViewModel = new ViewModelProvider(this).get(AssessmentViewModel.class);
        courseViewModel = new ViewModelProvider(this).get(CourseViewModel.class);
        courseName = findViewById(R.id.courseName);
        courseStart = findViewById(R.id.courseStartDate);
        courseEnd = findViewById(R.id.courseGoalDate);
        courseStatus = findViewById(R.id.courseStatus);
        courseContact = findViewById(R.id.courseContact);
        courseNotes = findViewById(R.id.courseNotes);
        saveButton = findViewById(R.id.courseSaveButton);

        //noinspection unchecked
        assessmentViewModel.getAllAssessments().observe(this, assessments -> {
            List<Assessment> associatedAssessments = new ArrayList<>();
            for (Assessment assessment : assessments) {
                if (assessment.getCourseID() == getIntent().getIntExtra("courseID", 0)) {
                    associatedAssessments.add(assessment);
                }
            }
            adapter.setAllAssessments(associatedAssessments);
        });

        if (getIntent().hasExtra("courseName")) {
            flagEdit = true;
            fab.setVisibility(View.VISIBLE);
            fab.setOnClickListener(view -> {
                Intent intent = new Intent(CourseActivity.this, AssessmentActivity.class);
                intent.putExtra("courseID", getIntent().getIntExtra("courseID", -1));
                startActivityForResult(intent, 1);
            });

            courseName.setText(getIntent().getStringExtra("courseName"));
            courseStart.setText(getIntent().getStringExtra("courseStart"));
            courseEnd.setText(getIntent().getStringExtra("courseGoal"));
            courseStatus.setText(getIntent().getStringExtra("courseStatus"));
            courseContact.setText(getIntent().getStringExtra("courseContact"));
            courseNotes.setText(getIntent().getStringExtra("courseNotes"));
        }

        dateStart = (view, year, monthOfYear, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            courseStart.setText(sdf.format(calendar.getTime()));
        };
        dateEnd = (view, year, monthOfYear, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            courseEnd.setText(sdf.format(calendar.getTime()));
        };

        courseStart.setOnClickListener(v -> {
            new DatePickerDialog(CourseActivity.this, dateStart, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
        });
        courseEnd.setOnClickListener(v -> {
            new DatePickerDialog(CourseActivity.this, dateEnd, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
        });

        saveButton.setOnClickListener((v -> {
            Intent reply = new Intent();
            String cName = courseName.getText().toString();
            int cTermID = getIntent().getIntExtra("termID", -1);
            Calendar cStart, cGoal;
            try {
                cStart = new Calendar.Builder().setInstant(sdf.parse(courseStart.getText().toString())).build();
                cGoal = new Calendar.Builder().setInstant(sdf.parse(courseEnd.getText().toString())).build();
            } catch (ParseException e) {
                Toast.makeText(getApplicationContext(), "Unable to save: Invalid Date", Toast.LENGTH_SHORT).show();
                return;
            }
            String cStatus = courseStatus.getText().toString();
            String cContact = courseContact.getText().toString();
            String cNotes = courseNotes.getText().toString();
            reply.putExtra("termID", cTermID);
            reply.putExtra("courseName", cName);
            reply.putExtra("courseStart", courseStart.getText().toString());
            reply.putExtra("courseGoal", courseEnd.getText().toString());
            reply.putExtra("courseStatus", cStatus);
            reply.putExtra("courseContact", cContact);
            reply.putExtra("courseNotes", cNotes);
            if (flagEdit) {
                Course nCourse = new Course(getIntent().getIntExtra("courseID", -1), cTermID, cName, cStart, cGoal, cStatus, cContact, cNotes);
                courseViewModel.update(nCourse);
            } else {
                setResult(RESULT_OK, reply);
                finish();
            }
        }));
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_dns, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.delete) {
            boolean flagHasAssociatedAssessment = false;
            int courseID = getIntent().getIntExtra("courseID", -1);
            List<Assessment> assessments = assessmentViewModel.getAllAssessments().getValue();
            if (assessments != null) {
                for (Assessment assessment : assessments) {
                    if (assessment.getCourseID() == courseID) {
                        flagHasAssociatedAssessment = true;
                        break;
                    }
                }
            }

            if (flagHasAssociatedAssessment) {
                Toast.makeText(getApplicationContext(), "Can't delete a course with associated assessments.", Toast.LENGTH_SHORT).show();
                return super.onOptionsItemSelected(item);
            } else {
                Calendar cStart, cEnd;
                try {
                    cStart = new Calendar.Builder().setInstant(sdf.parse(getIntent().getStringExtra("courseStart"))).build();
                    cEnd = new Calendar.Builder().setInstant(sdf.parse(getIntent().getStringExtra("courseGoal"))).build();
                } catch (ParseException e) {
                    Toast.makeText(getApplicationContext(), "Unexpected error, unable to delete.", Toast.LENGTH_SHORT).show();
                    return super.onOptionsItemSelected(item);
                }
                courseViewModel.delete(new Course(courseID,
                        getIntent().getIntExtra("termID", -1),
                        getIntent().getStringExtra("courseName"),
                        cStart,
                        cEnd,
                        getIntent().getStringExtra("courseStatus"),
                        getIntent().getStringExtra("courseContact"),
                        getIntent().getStringExtra("courseNotes")));
            }
        }

        if (id == R.id.notifications) {
            Intent endNotifyIntent = new Intent(CourseActivity.this, MyReceiver.class);
            Intent startNotifyIntent = new Intent(CourseActivity.this, MyReceiver.class);
            endNotifyIntent.putExtra("key", "Today is the goal to complete the course, " + courseName.getText().toString());
            startNotifyIntent.putExtra("key", "Today is the scheduled start of the course, " + courseName.getText().toString());
            PendingIntent endSender = PendingIntent.getBroadcast(CourseActivity.this, 0, endNotifyIntent, 0);
            PendingIntent startSender = PendingIntent.getBroadcast(CourseActivity.this, 0, startNotifyIntent, 0);
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

            Calendar goal, start;
            try {
                goal = new Calendar.Builder().setInstant(sdf.parse(courseEnd.getText().toString())).build();
                start = new Calendar.Builder().setInstant(sdf.parse(courseStart.getText().toString())).build();
            } catch (ParseException e) {
                Toast.makeText(getApplicationContext(), "Invalid date(s).", Toast.LENGTH_SHORT).show();
                return super.onOptionsItemSelected(item);
            }
            long goalDate = goal.getTimeInMillis();
            long startDate = start.getTimeInMillis();
            alarmManager.set(AlarmManager.RTC_WAKEUP, goalDate, endSender);
            alarmManager.set(AlarmManager.RTC_WAKEUP, startDate, startSender);
        }

        if (id == R.id.sharing) {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, courseNotes.getText().toString());
            sendIntent.putExtra(Intent.EXTRA_TITLE, courseName.getText().toString() + " Notes");
            sendIntent.setType("text/plain");

            Intent shareIntent = Intent.createChooser(sendIntent, null);
            startActivity(shareIntent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            int assessmentID = assessmentViewModel.lastAssessmentID() + 1;
            Calendar cEnd;
            try {
                cEnd = new Calendar.Builder().setInstant(sdf.parse(data.getStringExtra("assessmentGoal"))).build();
            } catch (ParseException e) {
                Toast.makeText(getApplicationContext(), "Unexpected error, unable to save Assessment.", Toast.LENGTH_SHORT).show();
                return;
            }
            AssessType type;
            if (data.getStringExtra("assessmentType").equalsIgnoreCase(AssessType.OBJECTIVE.toString())) {
                type = AssessType.OBJECTIVE;
            } else {
                type = AssessType.PERFORMANCE;
            }
            assessmentViewModel.insert(new Assessment(
                    assessmentID,
                    data.getIntExtra("courseID", -1),
                    data.getStringExtra("assessmentName"),
                    type,
                    cEnd,
                    data.getStringExtra("assessmentNotes")
            ));
        }
    }
}
