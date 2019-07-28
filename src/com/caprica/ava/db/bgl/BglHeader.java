package com.caprica.ava.db.bgl;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

public class BglHeader {
	protected static final String DATABASE_NAME = "header.sqlite";
	protected static final int DATABASE_VERSION = 2;
	private static final String DATABASE_TABLE = "bgls";
	private final Context context;
	private SQLHelper sqlhelper;
	private SQLiteDatabase database;
	public static final String KEY_ID = "_id";
	public static final String KEY_TITLE = "title";
	public static final String KEY_AUTHOR = "author";
	public static final String KEY_SRCLNG = "srclng";
	public static final String KEY_DSTLNG = "dstlng";
	public static final String KEY_SRCENC = "srcenc";
	public static final String KEY_SRCENCNAME = "srcencname";
	public static final String KEY_DSTENC = "dstenc";
	public static final String KEY_DSTENCNAME = "dstencname";
	public static final String KEY_DEFCHARSET = "defcharset";
	public static final String KEY_FILEPATH = "filepath";
	public static final String KEY_ABOUT = "about";
	public static final String KEY_NEW = "new";

	public BglHeader(Context context) {
		this.context = context;

	}

	private class SQLHelper extends SQLiteOpenHelper {

		public SQLHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);

		}

		@Override
		public void onCreate(SQLiteDatabase db) {

			db.execSQL("CREATE TABLE " + DATABASE_TABLE + " (" + KEY_ID
					+ "  PRIMARY KEY  UNIQUE , " + "" + KEY_TITLE + " TEXT, "
					+ KEY_AUTHOR + " TEXT, " + KEY_SRCLNG + " TEXT, "
					+ KEY_DSTLNG + " TEXT, " + KEY_SRCENC + " TEXT, "
					+ KEY_SRCENCNAME + " TEXT, " + KEY_DSTENC + " TEXT, "
					+ KEY_DSTENCNAME + " TEXT, " + KEY_DEFCHARSET + " TEXT,"
					+ KEY_FILEPATH + " TEXT," + KEY_ABOUT + " TEXT," + KEY_NEW
					+ " TEXT)");
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

			db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
			onCreate(db);

		}
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
	public ArrayList<String > queryAllBglsByKey(String val,boolean all){
		ArrayList<String> bs = null;
		String sql ="select * from " + DATABASE_TABLE;
		
		if (all==false)
			sql ="select * from " + DATABASE_TABLE+ " where "+KEY_NEW + " ='0'";
		Cursor cur = database.rawQuery(sql, null);
		if (cur == null || cur.getCount() == 0)
 	return bs;
			bs = new ArrayList<String>();
		cur.moveToFirst();
		for (int j = 0; j < cur.getCount(); j++) {

			String value = cur.getString(cur.getColumnIndex(val));
			bs.add(value);
			cur.moveToNext();
		}
		cur.close();
		return bs;
		
	}
	public ArrayList<BaseBgl> queryAlls(boolean all) {
		ArrayList<BaseBgl> bs = null;
		String sql ="select * from " + DATABASE_TABLE;
				
		if (all==false)
			sql ="select * from " + DATABASE_TABLE+ " where "+KEY_NEW + " ='0'";
		Cursor cur = database.rawQuery(sql, null);
		if (cur == null || cur.getCount() == 0)
			return bs;
		cur.moveToFirst();

		bs = new ArrayList<BaseBgl>();
		cur.moveToFirst();
		for (int j = 0; j < cur.getCount(); j++) {
			BaseBgl b = new BaseBgl();
			for (int i = 0; i < cur.getColumnCount(); i++) {
				if (cur.getColumnName(i).equals(KEY_ID)) {
					b.setId(cur.getString(i));
					continue;
				}

				if (cur.getColumnName(i).equals(KEY_TITLE)) {
					b.setTitle(cur.getString(i));
					continue;
				}
				if (cur.getColumnName(i).equals(KEY_AUTHOR)) {
					b.setAuthor(cur.getString(i));
					continue;
				}
				if (cur.getColumnName(i).equals(KEY_DSTENC)) {
					b.setDstEnc(cur.getString(i));
					continue;
				}
				if (cur.getColumnName(i).equals(KEY_FILEPATH)) {
					b.setFilePath(cur.getString(i));
					continue;
				}
				if (cur.getColumnName(i).equals(KEY_DSTENCNAME)) {
					b.setDstEncName(cur.getString(i));
					continue;
				}
				if (cur.getColumnName(i).equals(KEY_DSTLNG)) {
					b.setDstLng(cur.getString(i));
					continue;
				}
				if (cur.getColumnName(i).equals(KEY_SRCENC)) {
					b.setSrcEnc(cur.getString(i));
					continue;
				}
				if (cur.getColumnName(i).equals(KEY_SRCENCNAME)) {
					b.setSrcEncName(cur.getString(i));
					continue;
				}
				if (cur.getColumnName(i).equals(KEY_DEFCHARSET)) {
					b.setSrcEncName(cur.getString(i));
					continue;
				}
				if (cur.getColumnName(i).equals(KEY_SRCLNG)) {
					b.setSrcLng(cur.getString(i));
					continue;
				}
				if (cur.getColumnName(i).equals(KEY_ABOUT)) {
					b.setAbout(cur.getString(i));
					continue;
				}
				if (cur.getColumnName(i).equals(KEY_NEW)) {
					b.setNew(cur.getString(i).equals("1") ? true : false);
					continue;
				}

			}
			bs.add(b);
			cur.moveToNext();
		}
		cur.close();
		return bs;

	}
	public Cursor queryAllCursor() {
		ArrayList<BaseBgl> bs = new ArrayList<BaseBgl>();;
		Cursor cur = database.rawQuery("select * from " + DATABASE_TABLE + " where "+KEY_NEW + " ='0'", null);

		return cur;

	}

	public BaseBgl select(String title) {
		BaseBgl b = null;

		Cursor cur = database.rawQuery("select * from " + DATABASE_TABLE
				+ " where " + KEY_TITLE + "=\"" + title + "\"", null);
		if (cur != null) {
			if (cur.getCount() > 0) {
				b = new BaseBgl();
				cur.moveToFirst();
				for (int i = 0; i < cur.getColumnCount(); i++) {
					if (cur.getColumnName(i).equals(KEY_ID)) {
						b.setId(cur.getString(i));
						continue;
					}

					if (cur.getColumnName(i).equals(KEY_TITLE)) {
						b.setTitle(cur.getString(i));
						continue;
					}
					if (cur.getColumnName(i).equals(KEY_AUTHOR)) {
						b.setAuthor(cur.getString(i));
						continue;
					}
					if (cur.getColumnName(i).equals(KEY_DSTENC)) {
						b.setDstEnc(cur.getString(i));
						continue;
					}
					if (cur.getColumnName(i).equals(KEY_FILEPATH)) {
						b.setFilePath(cur.getString(i));
						continue;
					}
					if (cur.getColumnName(i).equals(KEY_DSTENCNAME)) {
						b.setDstEncName(cur.getString(i));
						continue;
					}
					if (cur.getColumnName(i).equals(KEY_DSTLNG)) {
						b.setDstLng(cur.getString(i));
						continue;
					}
					if (cur.getColumnName(i).equals(KEY_SRCENC)) {
						b.setSrcEnc(cur.getString(i));
						continue;
					}
					if (cur.getColumnName(i).equals(KEY_SRCENCNAME)) {
						b.setSrcEncName(cur.getString(i));
						continue;
					}
					if (cur.getColumnName(i).equals(KEY_DEFCHARSET)) {
						b.setSrcEncName(cur.getString(i));
						continue;
					}
					if (cur.getColumnName(i).equals(KEY_SRCLNG)) {
						b.setSrcLng(cur.getString(i));
						continue;
					}
					if (cur.getColumnName(i).equals(KEY_ABOUT)) {
						b.setAbout(cur.getString(i));
						continue;
					}
					if (cur.getColumnName(i).equals(KEY_NEW)) {
						b.setNew(cur.getString(i).equals("1") ? true : false);
						continue;
					}

				}

			}
		}
		cur.close();
		return b;
	}

	public long insert(BaseBgl b) {
		ContentValues values = new ContentValues();
		values.put(KEY_AUTHOR, b.getAuthor());
		values.put(KEY_DSTENC, b.getDstEnc());
		values.put(KEY_DEFCHARSET, b.getDefCharset());
		values.put(KEY_DSTENCNAME, b.getDstEncName());
		values.put(KEY_DSTLNG, b.getDstLng());

		values.put(KEY_SRCENC, b.getSrcEnc());
		values.put(KEY_SRCENCNAME, b.getSrcEncName());
		values.put(KEY_SRCLNG, b.getSrcLng());
		values.put(KEY_TITLE, b.getTitle());
		values.put(KEY_ID, b.getId());
		values.put(KEY_FILEPATH, b.getFilePath());
		values.put(KEY_ABOUT, b.getAbout());
		values.put(KEY_NEW, b.isNew() ? "1" : "0");
		return database.insert(DATABASE_TABLE, null, values);
	}
 

	public void delete(String title) {
		database.delete(DATABASE_TABLE, KEY_TITLE + "='" + title + "'", null);
	}
	public void updateNewColumn(String title,BaseBgl updatedBgl,boolean newVal) {
		ContentValues values = new ContentValues();
		values.put(KEY_AUTHOR, updatedBgl.getAuthor());
		values.put(KEY_DSTENC, updatedBgl.getDstEnc());
		values.put(KEY_DEFCHARSET, updatedBgl.getDefCharset());
		values.put(KEY_DSTENCNAME, updatedBgl.getDstEncName());
		values.put(KEY_DSTLNG, updatedBgl.getDstLng());
		values.put(KEY_SRCENC, updatedBgl.getSrcEnc());
		values.put(KEY_SRCENCNAME, updatedBgl.getSrcEncName());
		values.put(KEY_SRCLNG, updatedBgl.getSrcLng());
		values.put(KEY_TITLE, updatedBgl.getTitle());
		values.put(KEY_ABOUT, updatedBgl.getAbout());
		values.put(KEY_NEW, newVal ? "1" : "0");
		
		database.update(DATABASE_TABLE, values, KEY_TITLE + "='" + title + "'", null);
	}
	public void deleteAll() {
		database.delete(DATABASE_TABLE, null, null);

	}

	 
}