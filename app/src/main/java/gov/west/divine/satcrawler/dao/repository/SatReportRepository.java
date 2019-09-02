package gov.west.divine.satcrawler.dao.repository;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import gov.west.divine.satcrawler.dao.DatabaseConstants;
import gov.west.divine.satcrawler.dao.SatellitesDatabaseHelper;
import gov.west.divine.satcrawler.model.SatReport;
import gov.west.divine.satcrawler.model.Source;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class SatReportRepository {

    private final SQLiteDatabase database;
    private static final String SATBEAMS_REPORT_TABLE = DatabaseConstants.getValue(DatabaseConstants.SAT_REPORT_TABLE);

    public SatReportRepository(SatellitesDatabaseHelper dbHelper) {
        database = dbHelper.getWritableDatabase();
    }

    public void insert(List<SatReport> reports){
        database.beginTransaction();
        try {
            for (SatReport report: reports) {
                insert(report);
            }
            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
        }
    }

    public void insert(SatReport report){
        ContentValues values = new ContentValues();
        values.put("message", report.getMessage());
        values.put("source", report.getSource().name());
        values.put("created", report.getCreated().getTime());
        database.insert(SATBEAMS_REPORT_TABLE, null, values);
    }

    public List<SatReport> selectAll() {
        List<SatReport> reports = new LinkedList<>();
        Cursor cursor = database.rawQuery("select * from "+SATBEAMS_REPORT_TABLE+" order by created desc limit 500",null);

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {

                String message = cursor.getString(0);
                String source = cursor.getString(1);
                Date created = new Date(cursor.getLong(2));

                reports.add(new SatReport(message, Source.valueOf(source), created));
                cursor.moveToNext();
            }
        }
        return reports;
    }

    public boolean isReportExistByMessage(String message){
        Cursor cursor = database.rawQuery("select * from "+SATBEAMS_REPORT_TABLE+" where TRIM(message) = '"+message.trim()+"'",null);
        return cursor.getCount() > 0;
    }
}
