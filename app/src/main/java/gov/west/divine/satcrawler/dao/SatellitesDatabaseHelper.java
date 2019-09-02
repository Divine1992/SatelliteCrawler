package gov.west.divine.satcrawler.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.Serializable;

public class SatellitesDatabaseHelper extends SQLiteOpenHelper implements Serializable {
    private static SatellitesDatabaseHelper databaseHelper;

    private static final String SATBEAMS_TABLE = DatabaseConstants.getValue(DatabaseConstants.SATBEAMS_TABLE);
    private static final String SAT_REPORT_TABLE = DatabaseConstants.getValue(DatabaseConstants.SAT_REPORT_TABLE);

    private static final String SATBEAMS_CREATE = "create table "+ SATBEAMS_TABLE +" ( id integer primary key, " +
            "position text , " +
            "status text, " +
            "name text, " +
            "model text, " +
            "manufacturer text, " +
            "operator text, " +
            "site text, " +
            "mass text, " +
            "launchDate text, " +
            "expectedLifeTime text, " +
            "comments text);";

    private static final String SAT_REPORT_CREATE = "create table "+ SAT_REPORT_TABLE +" ( message text , " +
            "source text, " +
            "created integer);";

    private SatellitesDatabaseHelper(Context context) {
        super(context, DatabaseConstants.getValue(DatabaseConstants.DATABASE_NAME), null,
                Integer.parseInt(DatabaseConstants.getValue(DatabaseConstants.DATABASE_VERSION)));
    }

    public static synchronized SatellitesDatabaseHelper getInstance(Context context) {
        if (databaseHelper == null) {
            databaseHelper = new SatellitesDatabaseHelper(context.getApplicationContext());
        }
        return databaseHelper;
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(SATBEAMS_CREATE);
        database.execSQL(SAT_REPORT_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion){
        database.execSQL("DROP TABLE IF EXISTS "+ SATBEAMS_TABLE);
        database.execSQL("DROP TABLE IF EXISTS "+ SAT_REPORT_TABLE);
        onCreate(database);
    }
}
