package gov.west.divine.satcrawler;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import gov.west.divine.satcrawler.dao.SatellitesDatabaseHelper;
import gov.west.divine.satcrawler.dao.repository.SatReportRepository;
import gov.west.divine.satcrawler.model.RecyclerViewAdapter;
import gov.west.divine.satcrawler.model.SatReport;
import gov.west.divine.satcrawler.service.SatelliteReceiver;

import java.util.*;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.reportListId);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        runBackgroundService();

        List<SatReport> satBeamsReports = new SatReportRepository(SatellitesDatabaseHelper.getInstance(this)).selectAll();
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(this, satBeamsReports.size() > 0 ? satBeamsReports : Collections.singletonList(SatReport.emptyReport()));
        recyclerView.setAdapter(adapter);
    }

    private void runBackgroundService() {
        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(this, SatelliteReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 13);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.setTimeZone(TimeZone.getTimeZone("Europe/Kiev"));

        am.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pi);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
