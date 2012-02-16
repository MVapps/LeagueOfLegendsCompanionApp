package com.LoLCompanionApp;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

public class DatabaseExtra extends DatabaseHelper {

	DatabaseMain mainDB;

	public DatabaseExtra(Context context) {
		super(context, "extrainfo.sqlite");

		mainDB = new DatabaseMain(context);
	}

	// have one database with who counters who.
	// have function to backup the values (in case they added their own values
	// they want to keep when the database is updated with new champions)
	// "Bob" counters "Steve"
	// have import function to import counters from previous versions.
	// have two tables. One with default values, one with updated user-chosen
	// values.
	// backup only user-chosen values

	
	
	// WHEN DEFAULT TABLE FINISHED, CLONE IT AS usercounteredby TABLE AND USE AS BACKUP
	// INSETEAD OF CALLING BOTH

	
	
	public String[][] getCounteredByChampions(String champ)
			throws SQLiteException {
		// get champions that counter the chosen champion

		String[][] result = null;

		SQLiteDatabase database = getReadableDatabase();

		// run the query and get result
		Cursor cur = database.rawQuery(
				"SELECT * FROM defaultcounteredby WHERE champid=\'"
						+ champ.replace("'", "''") + "\'", null);

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
		Cursor cur = database.rawQuery(
				"SELECT * FROM defaultcounteredby WHERE counterid=\'"
						+ champ.replace("'", "''") + "\'", null);

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

}
