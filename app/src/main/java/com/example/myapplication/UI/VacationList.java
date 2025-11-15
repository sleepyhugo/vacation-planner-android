package com.example.myapplication.UI;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.database.Repository;
import com.example.myapplication.entities.Vacation;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class VacationList extends AppCompatActivity {
    private Repository repository;
    private VacationAdapter vacationAdapter;
    private RecyclerView recyclerView;
    private TextView textEmptyVacations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_vacation_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        FloatingActionButton fab = findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VacationList.this, VacationDetails.class);
                startActivity(intent);
            }
        });
        // Views
        recyclerView = findViewById(R.id.recyclerview);
        repository = new Repository(getApplication());

        // Repository and adapter
        vacationAdapter = new VacationAdapter(this);
        recyclerView.setAdapter(vacationAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_vacation_list, menu);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Reload vacations when returning to screen
        loadVacationsIntoList();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if (item.getItemId() == R.id.mysample) {
            repository.insert(new com.example.myapplication.entities.Vacation(
                    0, "Bermuda Trip", "Coral Beach Hotel", "01/10/2026", "01/15/2026"));
            repository.insert(new com.example.myapplication.entities.Vacation(
                    0, "Spring Break", "Sunset Resort", "03/20/2026", "03/25/2026"));
            repository.insert(new com.example.myapplication.entities.Vacation(
                    0, "London Trip", "The Royal Inn", "06/05/2026", "06/12/2026"));
            loadVacationsIntoList();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadVacationsIntoList() {
        List<Vacation> items = repository.getmAllVacations();
        Log.d("VAC_LIST", "loadVacationsIntoList(): fetched count = " + (items == null ? "null" : items.size()));
        vacationAdapter.setVacations(items);
        vacationAdapter.notifyDataSetChanged();
    }

    private void updateVacationsEmptyState(List<Vacation> vacations) {
        if (vacations == null || vacations.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            textEmptyVacations.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            textEmptyVacations.setVisibility(View.GONE);
        }
    }
}