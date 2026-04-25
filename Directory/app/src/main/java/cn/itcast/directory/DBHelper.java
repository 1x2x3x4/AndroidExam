package cn.itcast.directory;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "contacts.db";
    public static final String TABLE_NAME = "contact";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_PHONE = "phone";
    private static final String CREATE_TABLE_SQL = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_NAME + " TEXT, "
            + COLUMN_PHONE + " TEXT)";

    private static final int DATABASE_VERSION = 1;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean contactExists(String name) {
        SQLiteDatabase db = getReadableDatabase();
        try (Cursor cursor = db.query(
                TABLE_NAME,
                new String[]{COLUMN_ID},
                COLUMN_NAME + "=?",
                new String[]{name},
                null,
                null,
                null
        )) {
            return cursor.moveToFirst();
        }
    }

    public long addContact(String name, String phone) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_PHONE, phone);
        return db.insert(TABLE_NAME, null, values);
    }

    public Cursor queryContacts(String name) {
        SQLiteDatabase db = getReadableDatabase();
        if (name == null || name.trim().isEmpty()) {
            return db.query(
                TABLE_NAME,
                    null,
                    null,
                    null,
                    null,
                    null,
                COLUMN_ID + " ASC"
            );
        }
        return db.query(
            TABLE_NAME,
            null,
            COLUMN_NAME + "=?",
            new String[]{name},
            null,
            null,
            COLUMN_ID + " ASC"
        );
    }

    public int updateContact(String name, String phone) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PHONE, phone);
        return db.update(
                TABLE_NAME,
                values,
                COLUMN_NAME + "=?",
                new String[]{name}
        );
    }

    public int deleteContact(String name) {
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(
                TABLE_NAME,
                COLUMN_NAME + "=?",
                new String[]{name}
        );
    }
}
