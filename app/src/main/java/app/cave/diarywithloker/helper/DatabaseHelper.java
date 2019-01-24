package app.cave.diarywithloker.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import app.cave.diarywithloker.model.DataModel;


public class DatabaseHelper extends SQLiteOpenHelper {
    public static String DATABASE_NAME = "diary";
    public static Integer VERSTION = 1;
    public static String TABLE_NAME = "diary";

    private Context context;
    public static String COL_1 = "id";
    public static String COL_2 = "title";
    public static String COL_3 = "description";
    public static String COL_4 = "date";
    public static String COL_5 = "time";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSTION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_TABLE_QUERY = "CREATE TABLE "
                + TABLE_NAME + "("
                + COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
                + COL_2 + " TEXT,"
                + COL_3 + " TEXT,"
                + COL_4 + " TEXT,"
                + COL_5 + " TEXT" + ")";

        db.execSQL(CREATE_TABLE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void insertelement(DataModel dataModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COL_2, dataModel.getTitle());
        values.put(COL_3, dataModel.getDescription());
        values.put(COL_4, dataModel.getDate());
        values.put(COL_5, dataModel.getTime());
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public int editelement(DataModel dataModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();


        values.put(COL_2, dataModel.getTitle());
        values.put(COL_3, dataModel.getDescription());
        values.put(COL_4, dataModel.getDate());
        values.put(COL_5, dataModel.getTime());

        return db.update(TABLE_NAME, values, COL_1 + " = ? ",
                new String[]{String.valueOf(dataModel.getId())});
    }

    public List<DataModel> getAllitem() {
        List<DataModel> modellist = new ArrayList<>();

        String sql = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(sql, null);

        if (cursor.moveToFirst()) {
            do {
                DataModel model = new DataModel();
                model.setId(Integer.parseInt(cursor.getString(0)));
                model.setTitle(cursor.getString(1));
                model.setDescription(cursor.getString(2));
                model.setDate(cursor.getString(3));
                model.setTime(cursor.getString(4));
                modellist.add(model);

            } while (cursor.moveToNext());
        }

        return modellist;
    }

    public void deletelement(DataModel dataModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COL_1 + " = ?",
                new String[]{String.valueOf(dataModel.getId())});
        db.close();
    }

    public List<DataModel> getSelectedDiary(String searchKeyword) {

        List<DataModel> diaryModelList = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_NAME + " WHERE TITLE=?";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{searchKeyword});

        if (cursor.moveToFirst()) {

            do {

                DataModel model = new DataModel();
                model.setId(Integer.parseInt(cursor.getString(0)));
                model.setTitle(cursor.getString(1));
                model.setDescription(cursor.getString(2));
                model.setDate(cursor.getString(3));
                model.setTime(cursor.getString(4));

                diaryModelList.add(model);


            } while (cursor.moveToNext());
        }


        return diaryModelList;
    }


    public DataModel selectWithId(String id) {

        DataModel model = new DataModel();

        String selectQuery = "SELECT  * FROM " + TABLE_NAME + " WHERE ID=?";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{id});

        if (cursor != null)
            cursor.moveToFirst();


        try {


            model.setId(Integer.parseInt(cursor.getString(0)));
            model.setTitle(cursor.getString(1));
            model.setDescription(cursor.getString(2));
            model.setDate(cursor.getString(3));
            model.setTime(cursor.getString(4));


        } catch (Exception e) {

            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        return model;
    }


}
