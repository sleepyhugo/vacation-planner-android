package com.example.myapplication.UI;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.database.Repository;
import com.example.myapplication.entities.Excursion;
import com.example.myapplication.entities.Vacation;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class VacationDetails extends AppCompatActivity {
    String name;
    int vacationID;
    EditText editName;
    EditText editHotel;
    EditText editStartDate;
    EditText editEndDate;
    Repository repository;
    Vacation currentVacation;
    int numExcursions;
    private ExcursionAdapter excursionAdapter;
    private RecyclerView excursionRecycler;
    private TextView textEmptyExcursions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Request notification permission (Android 13+)
        if (android.os.Build.VERSION.SDK_INT >= 33) {
            if (checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[] {android.Manifest.permission.POST_NOTIFICATIONS},1001);
            }
        }

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_vacation_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        FloatingActionButton fab = findViewById(R.id.floatingActionButton2);

        editName = findViewById(R.id.titletext);
        editHotel = findViewById(R.id.hoteltext);
        editStartDate = findViewById(R.id.startdatetext);
        editEndDate = findViewById(R.id.enddatetext);

        vacationID = getIntent().getIntExtra("id", -1);
        name = getIntent().getStringExtra("name");

        editName.setText(name);

        // READ EXTRAS FROM INTENT AND POPULATE HOTEL/DATE FIELDS
        String hotelExtra = getIntent().getStringExtra("hotel");
        String startDateExtra = getIntent().getStringExtra("startDate");
        String endDateExtra = getIntent().getStringExtra("endDate");

        if (hotelExtra != null)     editHotel.setText(hotelExtra);
        if (startDateExtra != null) editStartDate.setText(startDateExtra);
        if (endDateExtra != null)   editEndDate.setText(endDateExtra);

        fab.setOnClickListener(v -> {
            Intent intent = new Intent(VacationDetails.this, ExcursionDetails.class);
            intent.putExtra("vacationID", vacationID);
            startActivity(intent);
        });

        repository = new Repository(getApplication());
        // Displays excursions associated with this vacation
        excursionRecycler = findViewById(R.id.excursionrecyclerview);
        textEmptyExcursions = findViewById(R.id.textEmptyExcursions);

        excursionAdapter  = new ExcursionAdapter(this);
        excursionRecycler.setAdapter(excursionAdapter);
        excursionRecycler.setLayoutManager(new LinearLayoutManager(this));
        loadExcursions();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_vacationdetails, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.vacationsave) {
            // Read latest field values
            String nameText     = editName.getText().toString().trim();
            String hotelText    = editHotel.getText().toString().trim();
            String startDate    = editStartDate.getText().toString().trim();
            String endDate      = editEndDate.getText().toString().trim();

            // Validate date format (MM/DD/YYYY) strictly
            if (!isValidDate(startDate) || !isValidDate(endDate)) {
                Toast.makeText(this, "Please enter dates in MM/DD/YYYY format.", Toast.LENGTH_LONG).show();
                return true; // stop save
            }

            // Validate that end date is after start date
            try {
                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MM/dd/yyyy", java.util.Locale.US);
                java.util.Date start = sdf.parse(startDate);
                java.util.Date end   = sdf.parse(endDate);

                if (end.before(start)) {
                    Toast.makeText(this, "End date must be after start date.", Toast.LENGTH_LONG).show();
                    return true; // stop save
                }
            } catch (java.text.ParseException e) {
                Toast.makeText(this, "Invalid date format.", Toast.LENGTH_LONG).show();
                return true;
            }

            Vacation vacation;

            if (vacationID == -1) {
                // Create a new ID
                List<Vacation> all = repository.getmAllVacations();
                if (all == null || all.size() == 0) {
                    vacationID = 1;
                } else {
                    vacationID = all.get(all.size() - 1).getVacationID() + 1;
                }

                vacation = new Vacation(
                        vacationID,
                        nameText,
                        hotelText,
                        startDate,
                        endDate
                );
                repository.insert(vacation);
                finish();
            } else {
                // Update existing
                vacation = new Vacation(
                        vacationID,
                        nameText,
                        hotelText,
                        startDate,
                        endDate
                );
                repository.update(vacation);
                finish();
            }
            return true;
        }

        if (item.getItemId() == R.id.vacationdelete) {
            List<Vacation> all = repository.getmAllVacations();
            if (all != null) {
                for (Vacation v : all) {
                    if (v.getVacationID() == vacationID) {
                        currentVacation = v;
                        break;
                    }
                }
            }

            if (currentVacation == null) {
                Toast.makeText(this, "Vacation not found.", Toast.LENGTH_LONG).show();
                return true;
            }
            boolean ok = repository.deleteVacationSafely(currentVacation);
            if (ok) {
                Toast.makeText(
                        this,
                        currentVacation.getVacationName() + " was deleted",
                        Toast.LENGTH_LONG
                ).show();
                finish();
            } else {
                new androidx.appcompat.app.AlertDialog.Builder(this)
                        .setTitle("Cannot Delete")
                        .setMessage("This vacation has excursions. Delete the excursions first.")
                        .setPositiveButton("OK", null)
                        .show();
            }
            return true;
        }
        // Alert for vacation start date
        if (item.getItemId() == R.id.vacation_alert_start) {
            if (vacationID == -1) {
                Toast.makeText(this, "Save the vacation first, then set alerts.", Toast.LENGTH_LONG).show();
                return true;
            }
            String titleText = editName.getText().toString().trim();
            String startDate = editStartDate.getText().toString().trim();

            if (!isValidDate(startDate)) {
                Toast.makeText(this, "Enter a valid Start date (MM/dd/yyyy).", Toast.LENGTH_LONG).show();
                return true;
            }
            scheduleVacationAlert(titleText, startDate, true);
            return true;
        }

        // Alert for vacation end date
        if (item.getItemId() == R.id.vacation_alert_end) {
            if (vacationID == -1) {
                Toast.makeText(this, "Save the vacation first, then set alerts.", Toast.LENGTH_LONG).show();
                return true;
            }
            String titleText = editName.getText().toString().trim();
            String startDate = editStartDate.getText().toString().trim();
            String endDate   = editEndDate.getText().toString().trim();

            if (!isValidDate(endDate)) {
                Toast.makeText(this, "Enter a valid End date (MM/dd/yyyy).", Toast.LENGTH_LONG).show();
                return true;
            }
            try {
                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MM/dd/yyyy", java.util.Locale.US);
                java.util.Date s = sdf.parse(startDate);
                java.util.Date e = sdf.parse(endDate);
                if (e.before(s)) {
                    Toast.makeText(this, "End date must be after start date.", Toast.LENGTH_LONG).show();
                    return true;
                }
            } catch (java.text.ParseException ignore) {}
            scheduleVacationAlert(titleText, endDate, false);
            return true;
        }
        // Share Vacation details via chooser (email/SMS/any app)
        if (item.getItemId() == R.id.vacation_share) {
            String shareBody = buildVacationShareText();
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain"); // lets the chooser include SMS, email, etc.
            intent.putExtra(Intent.EXTRA_SUBJECT, "Vacation Details");
            intent.putExtra(Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(intent, "Share vacation with…"));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private void loadExcursions() {
        List<Excursion> items = repository.getAssociatedExcursions(vacationID);
        excursionAdapter.setExcursion(items);
        excursionAdapter.notifyDataSetChanged();
        updateExcursionsEmptyState(items);
    }

    private void updateExcursionsEmptyState(List<Excursion> excursions) {
        if (excursions == null || excursions.isEmpty()) {
            excursionRecycler.setVisibility(android.view.View.GONE);
            textEmptyExcursions.setVisibility(android.view.View.VISIBLE);
        } else {
            excursionRecycler.setVisibility(android.view.View.VISIBLE);
            textEmptyExcursions.setVisibility(android.view.View.GONE);
        }
    }

    // Schedules an exact alarm if allowed, otherwise requests permission or uses a fallback.
    private void scheduleExactOrFallback(long triggerAtMillis, android.app.PendingIntent pi) {
        android.app.AlarmManager am =
                (android.app.AlarmManager) getSystemService(android.content.Context.ALARM_SERVICE);

        try {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
                // Android 12+ requires permission for exact alarms
                if (am.canScheduleExactAlarms()) {
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                        am.setExactAndAllowWhileIdle(android.app.AlarmManager.RTC_WAKEUP, triggerAtMillis, pi);
                    } else {
                        am.setExact(android.app.AlarmManager.RTC_WAKEUP, triggerAtMillis, pi);
                    }
                } else {
                    // Prompt the user to allow exact alarms
                    android.content.Intent i = new android.content.Intent(
                            android.provider.Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM,
                            android.net.Uri.parse("package:" + getPackageName())
                    );
                    startActivity(i);

                    // Fallback to inexact alarm so something still fires
                    am.setAndAllowWhileIdle(android.app.AlarmManager.RTC_WAKEUP, triggerAtMillis, pi);
                    android.widget.Toast.makeText(this, "Exact alarms not allowed. Scheduled an inexact alert.", android.widget.Toast.LENGTH_LONG).show();
                }
            } else {
                // Older Android versions: exact alarms are always allowed
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    am.setExactAndAllowWhileIdle(android.app.AlarmManager.RTC_WAKEUP, triggerAtMillis, pi);
                } else {
                    am.setExact(android.app.AlarmManager.RTC_WAKEUP, triggerAtMillis, pi);
                }
            }
        } catch (SecurityException se) {
            // Final safety net — use inexact alarm if permission denied
            am.setAndAllowWhileIdle(android.app.AlarmManager.RTC_WAKEUP, triggerAtMillis, pi);
            android.widget.Toast.makeText(this, "Exact alarms blocked by the OS. Scheduled an inexact alert instead.", android.widget.Toast.LENGTH_LONG).show();
            android.util.Log.w("VacationDetails", "Exact alarm denied; used inexact fallback", se);
        }
    }
    // Schedules an exact alarm for a given date string (MM/dd/yyyy) at 9:00 AM local time.
    private void scheduleVacationAlert(String title, String dateStr, boolean isStart) {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MM/dd/yyyy", java.util.Locale.US);
        sdf.setLenient(false);
        try {
            java.util.Calendar cal = java.util.Calendar.getInstance();
            cal.setTime(sdf.parse(dateStr));
            cal.set(java.util.Calendar.HOUR_OF_DAY, 9);
            cal.set(java.util.Calendar.MINUTE, 0);
            cal.set(java.util.Calendar.SECOND, 0);
            cal.set(java.util.Calendar.MILLISECOND, 0);

            long triggerAt = cal.getTimeInMillis();
            // Makes it so that if the time 9:00am has already passed for the current date the alert will still trigger 1 minute from now
            long now = System.currentTimeMillis();
            if (triggerAt <= now) {
                triggerAt = now + 60_000L; // 1 minute from now
            }

            String kind = isStart ? "STARTING" : "ENDING";

            android.app.AlarmManager am = (android.app.AlarmManager) getSystemService(android.content.Context.ALARM_SERVICE);
            android.content.Intent intent = new android.content.Intent(this, com.example.myapplication.util.AlertReceiver.class);
            intent.putExtra(com.example.myapplication.util.AlertReceiver.EXTRA_TITLE, title);
            intent.putExtra(com.example.myapplication.util.AlertReceiver.EXTRA_KIND, kind);

            int reqCode = vacationID * 10 + (isStart ? 1 : 2);

            android.app.PendingIntent pi = android.app.PendingIntent.getBroadcast(this, reqCode, intent, android.app.PendingIntent.FLAG_UPDATE_CURRENT | android.app.PendingIntent.FLAG_IMMUTABLE);

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                scheduleExactOrFallback(triggerAt, pi);
            } else {
                am.setExact(android.app.AlarmManager.RTC_WAKEUP, triggerAt, pi);
            }

            android.widget.Toast.makeText(this,"Alert set for " + dateStr + " (" + kind.toLowerCase() + ")", android.widget.Toast.LENGTH_LONG).show();

        } catch (java.text.ParseException e) {
            android.widget.Toast.makeText(this, "Invalid date format.", android.widget.Toast.LENGTH_LONG).show();
        }
    }

    // Strict MM/DD/YYYY validation (rejects 02/30/2026 etc.)
    private boolean isValidDate(String s) {
        if (s == null) return false;
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MM/dd/yyyy", java.util.Locale.US);
        sdf.setLenient(false);
        try { sdf.parse(s); return true; }
        catch (java.text.ParseException e) { return false; }
    }

    // Build share text for vacation
    private String buildVacationShareText() {
        String titleText = editName.getText().toString().trim();
        String hotelText = editHotel.getText().toString().trim();
        String startDate = editStartDate.getText().toString().trim();
        String endDate   = editEndDate.getText().toString().trim();

        StringBuilder sb = new StringBuilder();
        sb.append("Vacation: ").append(titleText.isEmpty() ? "Untitled" : titleText).append("\n");
        if (!hotelText.isEmpty()) sb.append("Hotel: ").append(hotelText).append("\n");
        if (!startDate.isEmpty()) sb.append("Start: ").append(startDate).append("\n");
        if (!endDate.isEmpty())   sb.append("End: ").append(endDate).append("\n");
        return sb.toString();
    }
    @Override
    protected void onResume() {
        super.onResume();
        loadExcursions();
    }
}