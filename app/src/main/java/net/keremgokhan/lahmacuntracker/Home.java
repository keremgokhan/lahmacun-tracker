package net.keremgokhan.lahmacuntracker;

import android.arch.persistence.room.Room;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import net.keremgokhan.lahmacuntracker.db.AppDatabase;
import net.keremgokhan.lahmacuntracker.db.dao.ConsumptionDao;
import net.keremgokhan.lahmacuntracker.db.dao.LahmacunDao;
import net.keremgokhan.lahmacuntracker.db.entity.Consumption;
import net.keremgokhan.lahmacuntracker.db.entity.Lahmacun;

import java.util.List;

public class Home extends AppCompatActivity {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //AppDatabase db = AppDatabase.getInstance(getApplicationContext());

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //NewRecordDialogFragment dialog = new NewRecordDialogFragment();
                //dialog.show(getSupportFragmentManager(), "New Record");

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        AppDatabase db = AppDatabase.getInstance(getApplicationContext());
                        LahmacunDao lahmacunDao = db.lahmacunDao();

                        List<Lahmacun> response = lahmacunDao.getLastLahmacun();
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
                    }
                }).start();
            }
        });
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
            /*new Thread(new Runnable() {
                @Override
                public void run() {
                    AppDatabase db = AppDatabase.getInstance(getApplicationContext());
                    LahmacunDao lahmacunDao = db.lahmacunDao();
                    final List<Lahmacun> lahmacuns = lahmacunDao.getAll();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            TextView t = findViewById(R.id.text_counter);
                            t.setText("Lahmacun sayisi: " + lahmacuns.size());
                        }
                    });

                }
            }).start();*/
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
