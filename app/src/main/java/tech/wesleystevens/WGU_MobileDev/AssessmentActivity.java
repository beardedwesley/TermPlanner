package tech.wesleystevens.WGU_MobileDev;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Locale;

import tech.wesleystevens.WGU_MobileDev.Entities.AssessType;
import tech.wesleystevens.WGU_MobileDev.Entities.Assessment;
import tech.wesleystevens.WGU_MobileDev.ViewModel.AssessmentViewModel;

public class AssessmentActivity extends AppCompatActivity {
    private AssessmentViewModel assessmentViewModel;
    private EditText assessmentName;
    private RadioButton objectiveButton;
    private RadioButton performanceButton;
    private TextView assessmentGoal;
    private EditText assessmentNotes;
    private Button assessmentSaveButton;
    private boolean flagEdit = false;
    private DatePickerDialog.OnDateSetListener date;
    final Calendar calendar = Calendar.getInstance();
    private SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy", Locale.US);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        assessmentViewModel = new ViewModelProvider(this).get(AssessmentViewModel.class);
        assessmentName = findViewById(R.id.assessmentName);
        objectiveButton = findViewById(R.id.objectiveButton);
        performanceButton = findViewById(R.id.performanceButton);
        assessmentGoal = findViewById(R.id.assessmentGoalDate);
        assessmentNotes = findViewById(R.id.assessmentNotes);
        assessmentSaveButton = findViewById(R.id.assessmentSaveButton);

        date = (view, year, monthOfYear, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            assessmentGoal.setText(sdf.format(calendar.getTime()));
        };

        assessmentGoal.setOnClickListener(v -> {
            new DatePickerDialog(AssessmentActivity.this, date, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
        });

        if (getIntent().hasExtra("assessmentName")) {
            flagEdit = true;

            assessmentName.setText(getIntent().getStringExtra("assessmentName"));
            assessmentGoal.setText(getIntent().getStringExtra("assessmentGoal"));
            assessmentNotes.setText(getIntent().getStringExtra("assessmentNotes"));
            if (AssessType.OBJECTIVE.toString().equalsIgnoreCase(getIntent().getStringExtra("assessmentType"))) {
                objectiveButton.setChecked(true);
            } else {
                performanceButton.setChecked(true);
            }
        }

        assessmentSaveButton.setOnClickListener((v -> {
            Intent reply = new Intent();
            String aName = assessmentName.getText().toString();
            int aCourseID = getIntent().getIntExtra("courseID", -1);
            AssessType assessType;
            if (objectiveButton.isChecked()) {
                assessType = AssessType.OBJECTIVE;
                //Toast.makeText(getApplicationContext(), assessType.toString(), Toast.LENGTH_SHORT).show();
            } else {
                assessType = AssessType.PERFORMANCE;
            }
            Calendar aGoal;
            try {
                aGoal = new Calendar.Builder().setInstant(sdf.parse(assessmentGoal.getText().toString())).build();
            } catch (ParseException e) {
                Toast.makeText(getApplicationContext(), "Unable to save: Invalid Date", Toast.LENGTH_SHORT).show();
                return;
            }
            String aNotes = assessmentNotes.getText().toString();
            reply.putExtra("courseID", aCourseID);
            reply.putExtra("assessmentName", aName);
            reply.putExtra("assessmentType", assessType.toString());
            reply.putExtra("assessmentGoal", assessmentGoal.getText().toString());
            reply.putExtra("assessmentNotes", aNotes);
            if (flagEdit) {
                Assessment nAssessment = new Assessment(getIntent().getIntExtra("assessmentID", -1), aCourseID, aName, assessType, aGoal, aNotes);
                assessmentViewModel.update(nAssessment);
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
            AssessType assessType;
            if (getIntent().getStringExtra("assessmentType").equalsIgnoreCase(AssessType.OBJECTIVE.toString())) {
                assessType = AssessType.OBJECTIVE;
            } else {
                assessType = AssessType.PERFORMANCE;
            }
            Calendar aGoal;
            try {
                aGoal = new Calendar.Builder().setInstant(sdf.parse(getIntent().getStringExtra("assessmentGoal"))).build();
            } catch (ParseException e) {
                Toast.makeText(getApplicationContext(), "Invalid date.", Toast.LENGTH_SHORT).show();
                return super.onOptionsItemSelected(item);
            }
            assessmentViewModel.delete(new Assessment(
                    getIntent().getIntExtra("assessmentID", -1),
                    getIntent().getIntExtra("courseID", -1),
                    getIntent().getStringExtra("assessmentName"),
                    assessType,
                    aGoal,
                    getIntent().getStringExtra("courseNotes")));
        }

        if (id == R.id.notifications) {
            Intent notifyIntent = new Intent(AssessmentActivity.this, MyReceiver.class);
            notifyIntent.putExtra("key", "Today is the goal to complete the assessment, " + assessmentName.getText().toString());
            PendingIntent sender = PendingIntent.getBroadcast(AssessmentActivity.this, 0, notifyIntent, 0);
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

            Calendar goal;
            try {
                goal = new Calendar.Builder().setInstant(sdf.parse(assessmentGoal.getText().toString())).build();
            } catch (ParseException e) {
                Toast.makeText(getApplicationContext(), "Unexpected error, unable to delete.", Toast.LENGTH_SHORT).show();
                return super.onOptionsItemSelected(item);
            }

            long date = goal.getTimeInMillis();
            alarmManager.set(AlarmManager.RTC_WAKEUP, date, sender);
        }

        if (id == R.id.sharing) {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, assessmentNotes.getText().toString());
            sendIntent.putExtra(Intent.EXTRA_TITLE, assessmentName.getText().toString() + " Notes");
            sendIntent.setType("text/plain");

            Intent shareIntent = Intent.createChooser(sendIntent, null);
            startActivity(shareIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
