package gov.west.divine.satcrawler.dao.repository;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import gov.west.divine.satcrawler.dao.DatabaseConstants;
import gov.west.divine.satcrawler.dao.SatellitesDatabaseHelper;
import gov.west.divine.satcrawler.model.SatBeamsEntity;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

public class SatBeamsRepository {

    private final SQLiteDatabase database;
    private static final String SATBEAMS_TABLE = DatabaseConstants.getValue(DatabaseConstants.SATBEAMS_TABLE);

    public SatBeamsRepository(SatellitesDatabaseHelper dbHelper) {
        database = dbHelper.getWritableDatabase();
    }

    public void insert(List<SatBeamsEntity> entities){
        database.beginTransaction();
        try {
            entities.forEach(this::insert);
            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
        }
    }

    private void insert(SatBeamsEntity entity){
        ContentValues values = new ContentValues();
        values.put("id", entity.getId());
        values.put("position", entity.getPosition());
        values.put("status", entity.getStatus());
        values.put("name", entity.getName());
        values.put("model", entity.getModel());
        values.put("manufacturer", entity.getManufacturer());
        values.put("operator", entity.getOperator());
        values.put("site", entity.getSite());
        values.put("mass", entity.getMass());
        values.put("launchDate", entity.getLaunchDate());
        values.put("expectedLifeTime", entity.getExpectedLifeTime());
        values.put("comments", entity.getComments());
        database.insertWithOnConflict(SATBEAMS_TABLE, null, values, 5);
    }

    public void refillTable(List<SatBeamsEntity> entities){
        database.execSQL("delete from "+SATBEAMS_TABLE);
        insert(entities);
    }

    public List<SatBeamsEntity> selectAll() {
        List<SatBeamsEntity> entities = new LinkedList<>();
        Cursor  cursor = database.rawQuery("select * from "+SATBEAMS_TABLE,null);

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {

                int id = cursor.getInt(0);
                String position = cursor.getString(1);
                String status = cursor.getString(2);
                String name = cursor.getString(3);
                String model = cursor.getString(4);
                String manufacturer = cursor.getString(5);
                String operator = cursor.getString(6);
                String site = cursor.getString(7);
                String mass = cursor.getString(8);
                String launchDate = cursor.getString(9);
                String expectedLifeTime = cursor.getString(10);
                String comments = cursor.getString(11);

                entities.add(new SatBeamsEntity(id, position, status, name, model, manufacturer, operator, site,
                        mass, launchDate, expectedLifeTime, comments));

                cursor.moveToNext();
            }
        }
        return entities;
    }
}
