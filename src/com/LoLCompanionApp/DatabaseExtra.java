package com.LoLCompanionApp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

public class DatabaseExtra extends DatabaseHelper {

	DatabaseMain mainDB;
	final String USER_COUNTER_TABLE = "usercounteredby";
	final String DEFAULT_COUNTER_TABLE = "defaultcounteredby";

	public DatabaseExtra(Context context) {
		super(context, "extrainfo.sqlite");

		mainDB = new DatabaseMain(context);
	}

	// WHEN DEFAULT TABLE FINISHED, CLONE IT AS usercounteredby TABLE AND USE AS
	// BACKUP
	// INSETEAD OF CALLING BOTH

	public String[][] getCounteredByChampions(String champ)
			throws SQLiteException {
		// get champions that counter the chosen champion

		String[][] result = null;

		SQLiteDatabase database = getReadableDatabase();

		// run the query and get result
		Cursor cur = database.rawQuery("SELECT * FROM " + DEFAULT_COUNTER_TABLE
				+ " WHERE champid=\'" + champ.replace("'", "''") + "\'", null);

		// go to first row
		if (cur.moveToFirst()) {
			result = new String[cur.getCount()][4];
			for (int i = 0; i < cur.getCount(); i += 1) {
				// get the values
				result[i][0] = ""
						+ cur.getString(cur.getColumnIndex("counterid"));
				result[i][1] = ""
						+ cur.getString(cur.getColumnIndex("description"));
				result[i][2] = "" + cur.getString(cur.getColumnIndex("role"));
				result[i][3] = "" + cur.getString(cur.getColumnIndex("tips"));

				// move to next row
				cur.moveToNext();
			}
		}

		database.close();

		return result;

	}

	public String[][] getCounteringChampions(String champ)
			throws SQLiteException {
		// get champions that are countered by the champion

		String[][] result = null;

		SQLiteDatabase database = getReadableDatabase();

		// run the query and get result
		Cursor cur = database
				.rawQuery("SELECT * FROM " + DEFAULT_COUNTER_TABLE
						+ " WHERE counterid=\'" + champ.replace("'", "''")
						+ "\'", null);

		// go to first row
		if (cur.moveToFirst()) {
			result = new String[cur.getCount()][4];
			for (int i = 0; i < cur.getCount(); i += 1) {
				// get the values
				result[i][0] = ""
						+ cur.getString(cur.getColumnIndex("champid"));
				result[i][1] = ""
						+ cur.getString(cur.getColumnIndex("description"));
				result[i][2] = "" + cur.getString(cur.getColumnIndex("role"));
				result[i][3] = "" + cur.getString(cur.getColumnIndex("tips"));

				// move to next row
				cur.moveToNext();
			}
		}

		database.close();

		return result;
	}

	public void deleteAllCounters() throws SQLiteException {
		SQLiteDatabase database = getWritableDatabase();

		// clear all rows from table
		database.delete(USER_COUNTER_TABLE, null, null);

		database.close();
	}

	public void restoreDefaultCounters() throws SQLiteException {
		SQLiteDatabase database = getWritableDatabase();

		// clear all rows from table
		database.execSQL("DROP TABLE IF EXISTS " + USER_COUNTER_TABLE);
		database.execSQL("CREATE TABLE " + USER_COUNTER_TABLE
				+ " AS SELECT id,champid,counterid,description,role,tips FROM "
				+ DEFAULT_COUNTER_TABLE);

		database.close();
	}

	public void deleteCounter(String counter, String champ)
			throws SQLiteException {
		SQLiteDatabase database = getWritableDatabase();

		database.delete(USER_COUNTER_TABLE, "counterid=\'" + counter
				+ "\' && champid=\'" + champ + "\'", null);

		database.close();
	}

	public void addNewCounter(String counter, String champ, String role,
			String tips, String description) throws SQLiteException {
		SQLiteDatabase database = getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put("champid", champ);
		values.put("counterid", counter);
		values.put("role", role);
		values.put("tips", tips);
		values.put("description", description);

		// insert the new drink into the database
		database.insert(USER_COUNTER_TABLE, null, values);

		database.close();
	}

	public void backupUserCounters() throws SQLiteException {

	}

	public void importUserCounters() throws SQLiteException {

	}

}
