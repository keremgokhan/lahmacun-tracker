package net.keremgokhan.lahmacuntracker;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.TextView;

import net.keremgokhan.lahmacuntracker.db.AppDatabase;
import net.keremgokhan.lahmacuntracker.db.dao.ConsumptionDao;
import net.keremgokhan.lahmacuntracker.db.dao.LahmacunDao;
import net.keremgokhan.lahmacuntracker.db.entity.Consumption;
import net.keremgokhan.lahmacuntracker.db.entity.Lahmacun;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Home extends AppCompatActivity {

    private static final int SUN_YEAR_IN_SECONDS;
    private static final int MONTH_IN_SECONDS;
    private static final int MINUTE_IN_SECONDS;
    private static final int HOUR_IN_SECONDS;
    private static final int DAY_IN_SECONDS;

    private static final int MAX_PROGRESS = 100;

    static {
        SUN_YEAR_IN_SECONDS = 31557600;
        MONTH_IN_SECONDS = SUN_YEAR_IN_SECONDS / 12;

        MINUTE_IN_SECONDS = 60;
        HOUR_IN_SECONDS = 60 * MINUTE_IN_SECONDS;
        DAY_IN_SECONDS = 24 * HOUR_IN_SECONDS;
    }

    private AppDatabase db;
    private Consumption lastConsumption;

    private boolean updateLastConsumption = true;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.db = AppDatabase.getInstance(getApplicationContext());

        addToolbar();

        initAddConsumptionFloatingButton();

        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    while ( !isInterrupted()) {
                        updateLastConsumption();

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                updateCounter();
                            }
                        });

                        Thread.sleep(1000);
                    }
                } catch (InterruptedException e) {
                    Log.e("update_counter", e.getMessage());
                }
            }
        };
        thread.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void updateLastConsumption () {

        if ( !updateLastConsumption && lastConsumption != null ) {
            return;
        }

        ConsumptionDao consumptionDao = db.consumptionDao();

        List<Consumption> response = consumptionDao.getLast();

        if ( response.isEmpty() ) {
            return;
        }

        this.lastConsumption = response.get(0);
        this.updateLastConsumption = false;
    }

    @SuppressLint("SetTextI18n")
    private void updateCounter() {

        if ( this.lastConsumption == null ) {
            return;
        }

        Date consumedOn = this.lastConsumption.consumedOn;

        Calendar consumedOnDate=Calendar.getInstance();
        consumedOnDate.setTime(consumedOn);

        int secondDiff = (int) (System.currentTimeMillis() - consumedOnDate.getTimeInMillis()) / 1000;

        int year = secondDiff / SUN_YEAR_IN_SECONDS;
        int yearRemainder = secondDiff % SUN_YEAR_IN_SECONDS;
        int yearProgressValue = (int) (((double) yearRemainder / SUN_YEAR_IN_SECONDS) * MAX_PROGRESS);
        int month = yearRemainder / MONTH_IN_SECONDS;
        int monthRemainder = yearRemainder % MONTH_IN_SECONDS;
        int monthProgressValue = (int) (((double) monthRemainder / MONTH_IN_SECONDS) * MAX_PROGRESS);
        int day = monthRemainder / DAY_IN_SECONDS;
        int dayReminder = monthRemainder % DAY_IN_SECONDS;
        int dayProgressValue = (int) (((double) dayReminder / DAY_IN_SECONDS) * MAX_PROGRESS);
        int hour = dayReminder / HOUR_IN_SECONDS;
        int hourReminder = dayReminder % HOUR_IN_SECONDS;
        int hourProgressValue =  (int) (((double) hourReminder / HOUR_IN_SECONDS) * MAX_PROGRESS);
        int minute = hourReminder / MINUTE_IN_SECONDS;
        int second = hourReminder % MINUTE_IN_SECONDS;

        TextView secondTextView = findViewById(R.id.secondValue);
        secondTextView.setText("" + second);

        ProgressBar minuteProgress = findViewById(R.id.minuteProgressBar);
        minuteProgress.setMax(60);
        minuteProgress.setProgress(second);

        TextView minuteTextView = findViewById(R.id.minuteValue);
        minuteTextView.setText("" + minute);

        ProgressBar hourProgress = findViewById(R.id.hourProgressBar);
        hourProgress.setMax(MAX_PROGRESS);
        hourProgress.setProgress(hourProgressValue);

        TextView hourTextView = findViewById(R.id.hourValue);
        hourTextView.setText("" + hour);

        ProgressBar dayProgress = findViewById(R.id.dayProgressBar);
        dayProgress.setMax(MAX_PROGRESS);
        dayProgress.setProgress(dayProgressValue);

        TextView dayTextView = findViewById(R.id.dayValue);
        dayTextView.setText("" + day);

        ProgressBar monthProgress = findViewById(R.id.monthProgressBar);
        monthProgress.setMax(MAX_PROGRESS);
        monthProgress.setProgress(monthProgressValue);

        TextView monthTextView = findViewById(R.id.monthValue);
        monthTextView.setText("" + month);

        ProgressBar yearProgress = findViewById(R.id.yearProgressBar);
        yearProgress.setMax(MAX_PROGRESS);
        yearProgress.setProgress(yearProgressValue);

        TextView yearTextView = findViewById(R.id.yearValue);
        yearTextView.setText("" + year);
    }

    private void initAddConsumptionFloatingButton() {

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addLahmacun(db);
            }
        });
    }

    private void addLahmacun(final AppDatabase db) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                LahmacunDao lahmacunDao = db.lahmacunDao();

                List<Lahmacun> response = lahmacunDao.getLast();
                Lahmacun lahmacun;
                if ( response.isEmpty() ) {
                    lahmacun = new Lahmacun();
                    lahmacun.name = "Lahmacun";
                    lahmacunDao.insert(lahmacun);
                } else {
                    lahmacun = response.get(0);
                }

                Consumption newConsumption = new Consumption(lahmacun.id);
                ConsumptionDao consumptionDao = db.consumptionDao();
                consumptionDao.insert(newConsumption);

                updateLastConsumption = true;
            }
        }).start();
    }

    private void addToolbar() {
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }
}
