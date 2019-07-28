package com.caprica.ava.db.zdic;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
 
import com.caprica.ava.db.Base;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class Persian extends Base {
	private static final String DATABASE_TABLE = "persian";
	private final Context context;
	private SQLHelper sqlhelper;
	private SQLiteDatabase database;
	public static final String KEY_WORD = "word";
	public static final String KEY_MEANING = "meaning";
	public static final String KEY_SYNONYM= "syn";

	private class SQLHelper extends SQLiteAssetHelper {

		public SQLHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		 
		}

	 
	}

	public Persian(Context context) {
	 
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

	public void insert(com.caprica.ava.model.Persian per) {
		ContentValues values = new ContentValues();
		values.put(KEY_WORD, per.getWord());
		values.put(KEY_MEANING, per.getMeaning());
		values.put(KEY_SYNONYM, per.getSynonym());
		
		database.insert(DATABASE_TABLE, null, values);
	}

	public ArrayList<com.caprica.ava.model.Persian> select(String word) {
		ArrayList<com.caprica.ava.model.Persian> rows = new ArrayList<com.caprica.ava.model.Persian>();
		Cursor cur = database.rawQuery("SELECT * FROM  " + DATABASE_TABLE
				+ " WHERE  " +KEY_WORD+ " LIKE '" + word + "%'", null);
		if (cur != null) {
			for (cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext()) {
				com.caprica.ava.model.Persian eng = new com.caprica.ava.model.Persian(
						cur.getString(0), cur.getString(1),cur.getString(2));
				rows.add(eng);
			}
		}
		cur.close();
		return rows;
	}

	public void insert(String word, String meaning,String synonym) {
		ContentValues values = new ContentValues();
		values.put(KEY_WORD, word);
		values.put(KEY_MEANING, meaning);
		values.put(KEY_SYNONYM, synonym);
		
		database.insert(DATABASE_TABLE, null, values);
	}

	public void update(String word, String newmeaning,String newsynonym) {
		ContentValues content = new ContentValues();
		content.put(KEY_WORD, word);
		content.put(KEY_MEANING, newmeaning);
		content.put(KEY_MEANING, newsynonym);
		database.update(DATABASE_TABLE, content, KEY_WORD + "=" + word, null);
	}

	public void delete(String word) {
		database.delete(DATABASE_TABLE, KEY_WORD + "=" + word, null);
	}
}
