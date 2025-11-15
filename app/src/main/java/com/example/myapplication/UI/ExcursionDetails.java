package com.example.myapplication.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.R;
import com.example.myapplication.database.Repository;
import com.example.myapplication.entities.Excursion;
import com.example.myapplication.entities.Vacation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ExcursionDetails extends AppCompatActivity {

    String name;
    int excursionID;
    int vacationID;
    EditText editName;
    TextView editDate;
    DatePickerDialog.OnDateSetListener datePickerListener;
    final Calendar cal = Calendar.getInstance();
    Repository repository;
    private static final String DATE_PATTERN = "MM/dd/yyyy"; // Date validation helper (checks MM/dd/yyyy format)
    DatePickerDialog.OnDateSetListener startDate;
    final Calendar myCalenderStart = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_excursion_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // Fields for entering/editing excursion info
        repository = new Repository(getApplication());
        // Input fields for Excursions CRUD
        editName = findViewById(R.id.excursionName);
        name = getIntent().getStringExtra("name");
        if (name != null && !name.isEmpty()) {
            editName.setText(name);
        } else {
            editName.setHint("Enter name");
        }

        //Identify which excursion/vacation we are editing/creating
        excursionID = getIntent().getIntExtra("id", -1);
        vacationID = getIntent().getIntExtra("vacationID", -1);

        editDate = findViewById(R.id.excursionDate);

        // DatePicker that writes MM/dd/yyyy into editDate
        editDate.setOnClickListener(v -> {
            final java.util.Calendar cal = java.util.Calendar.getInstance();
            android.app.DatePickerDialog dlg = new android.app.DatePickerDialog(
                    this,
                    (view, y, m, d) -> {
                        String mm = String.format(java.util.Locale.US, "%02d", m + 1); // m is 0-based
                        String dd = String.format(java.util.Locale.US, "%02d", d);
                        editDate.setText(mm + "/" + dd + "/" + y);
                    },
                    cal.get(java.util.Calendar.YEAR),
                    cal.get(java.util.Calendar.MONTH),
                    cal.get(java.util.Calendar.DAY_OF_MONTH)
            );
            dlg.show();
        });

        // Read incoming values (make sure you read "id" the same key you sent from adapter)
        excursionID = getIntent().getIntExtra("id", -1);
        String incomingDate = getIntent().getStringExtra("date");

        // If the date was passed in the Intent, show it.
        // Otherwise, if we have an ID, load from the Repository as a fallback.
        if (incomingDate != null && !incomingDate.trim().isEmpty()) {
            editDate.setText(incomingDate);
        }
        else if (excursionID != -1) {
            // fallback: load from DB if not passed
            Excursion loaded = null;
            for (Excursion e : repository.getAllExcursions()) {
                if (e.getExcursionID() == excursionID) { loaded = e; break; }
            }
            if (loaded != null && loaded.getExcursionDate() != null) {
                editDate.setText(loaded.getExcursionDate());
            }
        }

        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        editDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Date date;
                String info=editDate.getText().toString();
                if(info.equals(""))info="11/08/25";
                try{
                    myCalenderStart.setTime(sdf.parse(info));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                new DatePickerDialog(ExcursionDetails.this, startDate, myCalenderStart
                        .get(Calendar.YEAR), myCalenderStart.get(Calendar.MONTH),
                        myCalenderStart.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        startDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                myCalenderStart.set(Calendar.YEAR, year);
                myCalenderStart.set(Calendar.MONTH, month);
                myCalenderStart.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabelStart();
            }
        };
        editDate = findViewById(R.id.excursionDate);

        // If editing an existing excursion, prefill:
        incomingDate = getIntent().getStringExtra("date");
        if (incomingDate != null) {
            editDate.setText(incomingDate);
        }

        // Date picker: tap the date field to choose a date
        editDate.setOnClickListener(v -> {
            int y = cal.get(Calendar.YEAR);
            int m = cal.get(Calendar.MONTH);
            int d = cal.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog dp = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
                cal.set(Calendar.YEAR, year);
                cal.set(Calendar.MONTH, month);
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                editDate.setText(format(cal.getTime())); // "MM/dd/yyyy"
            }, y, m, d);
            dp.show();
        });
    }

    private String format(Date date) {
        return new java.text.SimpleDateFormat("MM/dd/yyyy", java.util.Locale.US).format(date);
    }
    private void updateLabelStart() {
        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        editDate.setText(sdf.format(myCalenderStart.getTime()));
    }
    // Menu with save/delete actions
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_excursiondetails, menu); // Menu with save/delete actions
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Menu handler for setting an alert on the excursion date
        if (item.getItemId() == R.id.excursion_alert) {
            if (excursionID == -1) {
                Toast.makeText(this, "Save the excursion first, then set the alert.", Toast.LENGTH_LONG).show();
                return true;
            }

            String titleText = editName.getText().toString().trim();
            String dateText  = editDate.getText().toString().trim();

            if (!isValidDate(dateText)) {
                Toast.makeText(this, "Enter a valid date (MM/dd/yyyy) first.", Toast.LENGTH_LONG).show();
                return true;
            }

            scheduleExcursionAlert(titleText, dateText);
            return true;
        }

        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        // Add/Update Excursion
        if (item.getItemId() == R.id.excursionsave) {
            String title = editName.getText().toString().trim();
            String date  = editDate.getText().toString().trim();

            // Ensure excursion date is within its parent vacation range
            Vacation parent = repository.getVacationById(vacationID);
            if (parent == null) {
                Toast.makeText(this, "Parent vacation not found.", Toast.LENGTH_LONG).show();
                return true;
            }
            String vacationStart = parent.getStartDate();
            String vacationEnd   = parent.getEndDate();

            // Validate parent dates themselves
            if (!isValidDate(vacationStart) || !isValidDate(vacationEnd)) {
                Toast.makeText(this, "Vacation dates are invalid. Fix the vacation first.", Toast.LENGTH_LONG).show();
                return true;
            }

            if (!isDateWithinVacation(date, vacationStart, vacationEnd)) {
                editDate.setError("Excursion date must be between " + vacationStart + " and " + vacationEnd);
                Toast.makeText(this, "Excursion date must be within the vacation dates.", Toast.LENGTH_LONG).show();
                return true; // block save
            }
            // Validate date format strictly (MM/dd/yyyy)
            if (!isValidDate(date)) {
                editDate.setError("Use format: " + DATE_PATTERN);
                Toast.makeText(this, "Enter a valid date (MM/dd/yyyy).", Toast.LENGTH_LONG).show();
                return true; // stop save
            }

            if (title.isEmpty()) {
                Toast.makeText(this, "Enter a title.", Toast.LENGTH_LONG).show();
                return true;
            }
            if (date.isEmpty()) {
                Toast.makeText(this, "Pick a date.", Toast.LENGTH_LONG).show();
                return true;
            }

            // create object (constructor doesn’t take date), then set date
            Excursion excursion = new Excursion((excursionID == -1 ? repository.nextExcursionId() : excursionID), title, vacationID);
            excursion.setExcursionDate(date); // 👈 B4: persist the date (use setExcursionDate(...) if that’s your field name)

            if (excursionID == -1) {
                excursionID = excursion.getExcursionID();
                repository.insert(excursion);
            } else {
                repository.update(excursion);
            }

            finish();
            return true;
        }

        // Delete Excursion
        if (item.getItemId() == R.id.excursiondelete) {
            if (excursionID == -1) {
                finish();
                return true;
            }

            new androidx.appcompat.app.AlertDialog.Builder(this)
                    .setTitle("Delete this excursion?")
                    .setMessage("This cannot be undone.")
                    .setPositiveButton("Delete", (d, which) -> {
                        Excursion toDelete = null;
                        for (Excursion e : repository.getAllExcursions()) {
                            if (e.getExcursionID() == excursionID) {
                                toDelete = e;
                                break;
                            }
                        }
                        if (toDelete != null) {
                            repository.delete(toDelete);
                        }
                        finish();
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    // Date validation helper (checks MM/dd/yyyy format)
    private boolean isValidDate(String value) {
        if (value == null) return false;
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(DATE_PATTERN, java.util.Locale.US);
        sdf.setLenient(false); // strict validation
        try {
            sdf.parse(value.trim());
            return true;
        } catch (Exception ex) {
            return false;
        }
    }
    // Schedule an alert for the excursion date with the excursion title
    private void scheduleExcursionAlert(String title, String date) {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MM/dd/yyyy", java.util.Locale.US);
        try {
            java.util.Date when = sdf.parse(date);
            if (when == null) {
                Toast.makeText(this, "Invalid date.", Toast.LENGTH_LONG).show();
                return;
            }
            long triggerAtMillis = when.getTime();

            // Build the broadcast that will fire at the excursion date
            Intent intent = new Intent(this, MyReceiver.class);
            intent.putExtra("key", "Excursion: " + title + " starts today!");

            // Unique request code: use excursionID (stable)
            PendingIntent sender = PendingIntent.getBroadcast(
                    this,
                    excursionID, // unique per excursion
                    intent,
                    PendingIntent.FLAG_IMMUTABLE
            );

            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            if (alarmManager != null) {
                alarmManager.set(AlarmManager.RTC_WAKEUP, triggerAtMillis, sender);
                Toast.makeText(this, "Alert set for " + date, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Alarm service unavailable.", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Error setting alert.", Toast.LENGTH_LONG).show();
        }
    }
    // Verifies excursion date is within [vacationStart, vacationEnd]
    private boolean isDateWithinVacation(String excursionDate, String vacationStart, String vacationEnd) {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(DATE_PATTERN, java.util.Locale.US);
        sdf.setLenient(false);
        try {
            long e = sdf.parse(excursionDate).getTime();
            long s = sdf.parse(vacationStart).getTime();
            long n = sdf.parse(vacationEnd).getTime();
            return e >= s && e <= n;
        } catch (Exception ex) {
            return false;
        }
    }
}