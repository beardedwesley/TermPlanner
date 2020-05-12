package tech.wesleystevens.WGU_MobileDev;

import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.text.ParseException;
import java.time.OffsetDateTime;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import tech.wesleystevens.WGU_MobileDev.Entities.Term;
import tech.wesleystevens.WGU_MobileDev.UI.TermAdapter;
import tech.wesleystevens.WGU_MobileDev.ViewModel.TermViewModel;

public class MainActivity extends AppCompatActivity {
    private TermViewModel termViewModel;
    private SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy", Locale.US);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        RecyclerView recyclerView = findViewById(R.id.termListView);
        final TermAdapter adapter = new TermAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        termViewModel = new ViewModelProvider(this).get(TermViewModel.class);
        termViewModel.getAllTerms().observe(this, terms -> adapter.setAllTerms(terms));

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, TermActivity.class);
            intent.putExtra("termID", termViewModel.lastTermID() + 1);
            startActivityForResult(intent, 1);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            int termID = termViewModel.lastTermID() + 1;
            Calendar tStart, tEnd;
            try {
                tStart = new Calendar.Builder().setInstant(sdf.parse(data.getStringExtra("termStart"))).build();
                tEnd = new Calendar.Builder().setInstant(sdf.parse(data.getStringExtra("termEnd"))).build();
            } catch (ParseException e) {
                Toast.makeText(getApplicationContext(), "Unexpected error, unable to save Term.", Toast.LENGTH_SHORT).show();
                return;
            }
            termViewModel.insert(new Term(termID, data.getStringExtra("termTitle"), tStart, tEnd));
        }
    }

}
