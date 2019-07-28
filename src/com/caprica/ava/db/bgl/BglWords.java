package com.caprica.ava.db.bgl;

import java.util.ArrayList;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import com.caprica.ava.model.Index;

public class BglWords {
	protected static final int DATABASE_VERSION = 1;
	protected static final String DATABASE_TABLE = "bglwords";

	private String DATABASE_NAME = "bgl.sqlite";
	private final Context context;
	private SQLHelper sqlhelper;
	private SQLiteDatabase database;

	public static final String KEY_WORD = "word";
	public static final String KEY_SIZE = "_id";

	private class SQLHelper extends SQLiteOpenHelper {

		public SQLHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);

		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL("CREATE TABLE " + DATABASE_TABLE + " (" + KEY_WORD
					+ " TEXT, " + KEY_SIZE + " TEXT)");
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
			onCreate(db);
		}

	}

	public String getDATABASE_NAME() {
		return DATABASE_NAME;
	}

	public void setDATABASE_NAME(String dATABASE_NAME) {
		DATABASE_NAME = String.valueOf(dATABASE_NAME.hashCode());
	}

	public void MultipleInsert(ArrayList<Index> iv) {
		String sql = "INSERT INTO " + DATABASE_TABLE + " (" + KEY_WORD + ", "
				+ KEY_SIZE + ") VALUES (?, ?)";
		database.beginTransaction();
		SQLiteStatement state = database.compileStatement(sql);
		for (int i = 0; i < iv.size(); i++) {

			state.bindString(1, iv.get(i).getWord());
			state.bindString(2, iv.get(i).getSize());
			state.execute();
			state.clearBindings();
		}
		database.setTransactionSuccessful();
		database.endTransaction();
	}

	public boolean isTableNonZero() {
		Cursor cur = database.rawQuery("select * from " + DATABASE_TABLE, null);
		boolean b = cur.getCount() != 0;
		cur.close();
		return b;
	}

	public boolean queryEquals(String word) {
		Cursor cur = database.rawQuery("select * from " + DATABASE_TABLE, null);
		boolean b = cur.getCount() != 0;
		cur.close();
		return b;
	}

	public String[] queryObject(String word) {
		Cursor cur = database.rawQuery("select * from " + DATABASE_TABLE
				+ " where  " + KEY_WORD + " like " + "\""
				+ word.toLowerCase().trim() + "\"", null);
		cur.moveToFirst();
		if (cur.getCount() <= 0)

			return null;
		String[] vals = new String[2];
		vals[0] = cur.getString(0);
		vals[1] = cur.getString(1);
		cur.close();
		return vals;
	}

	public Cursor queryLike(String word) {

		String sql = "SELECT * FROM " + DATABASE_TABLE + " where " + KEY_WORD
				+ " like \"" + word.trim() + "%\"";
		Cursor c = database.rawQuery(sql, null);
		return c;
	}

	public Cursor queryAll() {

		String sql = "SELECT * FROM " + DATABASE_TABLE;
		Cursor c = database.rawQuery(sql, null);
		return c;
	}

	public void open() throws SQLiteException {
		if (database == null) {
			sqlhelper = new SQLHelper(context);
			database = sqlhelper.getWritableDatabase();
		}
	}

	public void close() {
		if (database != null) {
			database.close();
			database = null;
			sqlhelper = null;
		}
	}

	public BglWords(Context context) {
		this.context = context;
	}

	public BglWords(Context context, String dbname) {
		this.context = context;
		setDATABASE_NAME(dbname);

	}

	public void insert(String id, String word, String range) {
		ContentValues values = new ContentValues();

		values.put(KEY_WORD, word);
		values.put(KEY_SIZE, range);
		database.insert(DATABASE_TABLE, null, values);

	}

	public void deleteAll() {
		database.delete(DATABASE_TABLE, null, null);
	}

	public void dropDB() {
		context.deleteDatabase(DATABASE_NAME);
	}

}
