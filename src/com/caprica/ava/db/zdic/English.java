package com.caprica.ava.db.zdic;

import java.util.ArrayList;

import com.caprica.ava.db.Base;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

public class English extends Base {
	private static final String DATABASE_TABLE = "english";
	private final Context context;
	private SQLHelper sqlhelper;
	private SQLiteDatabase database;
	public static final String KEY_WORD = "word";
	public static final String KEY_MEANING = "meaning";

	private class SQLHelper extends SQLiteAssetHelper {

		public SQLHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
			 
		}

		/*
		 * public SQLHelper(Context context) { super(context, DATABASE_NAME,
		 * null, DATABASE_VERSION); }
		 * 
		 * @Override public void onCreate(SQLiteDatabase db) {  
		 * Auto-generated method stub db.execSQL("CREATE TABLE \"" +
		 * DATABASE_TABLE + "\" (\"" + KEY_WORD +
		 * "\" TEXT PRIMARY KEY  NOT NULL , \"" + KEY_MEANING + "\" TEXT)"); }
		 * 
		 * @Override public void onUpgrade(SQLiteDatabase db, int oldVersion,
		 * int newVersion) { 
		 * db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE); onCreate(db); }
		 */
		
	}

	public English(Context context) {
		 
		this.context = context;
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
		}
	}

	public void insert(com.caprica.ava.model.English eng) {
		ContentValues values = new ContentValues();
		values.put(KEY_WORD, eng.getWord());
		values.put(KEY_MEANING, eng.getMeaning());
		database.insert(DATABASE_TABLE, null, values);
	}

	public ArrayList<com.caprica.ava.model.English> select(String word) {
		ArrayList<com.caprica.ava.model.English> rows = new ArrayList<com.caprica.ava.model.English>();
		Cursor cur = database.rawQuery("SELECT * FROM  " + DATABASE_TABLE
				+ " WHERE  " +KEY_WORD+ " LIKE '" + word + "%'", null);
		if (cur != null) {
			for (cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext()) {
				com.caprica.ava.model.English eng = new com.caprica.ava.model.English(
						cur.getString(0), cur.getString(1));
				rows.add(eng);
			}
		}
		cur.close();
		return rows;
	}

	public void insert(String word, String meaning) {
		ContentValues values = new ContentValues();
		values.put(KEY_WORD, word);
		values.put(KEY_MEANING, meaning);
		database.insert(DATABASE_TABLE, null, values);
	}

	public void update(String word, String newmeaning) {
		ContentValues content = new ContentValues();
		content.put(KEY_WORD, word);
		content.put(KEY_MEANING, newmeaning);
		database.update(DATABASE_TABLE, content, KEY_WORD + "=" + word, null);
	}

	public void delete(String word) {
		database.delete(DATABASE_TABLE, KEY_WORD + "=" + word, null);
	}

}
