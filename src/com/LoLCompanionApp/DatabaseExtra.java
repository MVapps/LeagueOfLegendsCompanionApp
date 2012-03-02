package com.LoLCompanionApp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Environment;

public class DatabaseExtra extends DatabaseHelper {

	DatabaseMain mainDB;
	final String USER_COUNTER_TABLE = "usercounteredby";
	final String DEFAULT_COUNTER_TABLE = "defaultcounteredby";
	final String BACKUP_PATH, BACKUP_FILE;
	final Context context;

	public DatabaseExtra(Context context) {
		super(context, "extrainfo.sqlite");

		// set main variables for database
		mainDB = new DatabaseMain(context);
		this.context = context;

		// set backup variables
		BACKUP_PATH = Environment.getExternalStorageDirectory().toString()
				+ "/LoLCompanionApp Backup/";
		BACKUP_FILE = USER_COUNTER_TABLE + ".txt";
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		backupUserCounters();
		super.onUpgrade(db, oldVersion, newVersion);
		importUserCounters();
	}

	// WHEN finished with the default table,add new column "visible" default
	// will all be set to true. When use wants to delete a entry with label
	// "visible", it will instead change the status in the default table rather
	// then deleting it. If use deletes a value that is from user counters,
	// delete it normally.
	//
	// MAKE SURE TO UNCOMMENT LINE IN GETCOUNTERCHAMPIONS

	public String[][] getCounteredByChampions(String champ) {

		return getCounterChampions(champ, "champid", "counterid");
	}

	public String[][] getCounteringChampions(String champ) {

		return getCounterChampions(champ, "counterid", "champid");
	}

	private String[][] getCounterChampions(String champ, String searchColumn,
			String champColumn) {
		// get champions that counter the chosen champion

		String[][] result = null;

		SQLiteDatabase database = getReadableDatabase();

		// create cursor array to hold the two result sets (ne for default
		// values and one for user values)
		Cursor[] curArray = {
				database.rawQuery(
						"SELECT * FROM " + USER_COUNTER_TABLE + " WHERE "
								+ searchColumn + "=\'"
								+ champ.replace("'", "''") + "\'", null),
				database.rawQuery(
						"SELECT * FROM " + DEFAULT_COUNTER_TABLE + " WHERE "
								+ searchColumn + "=\'"
								+ champ.replace("'", "''")
								+ "\' AND visible=\'true\'", null) };

		// create a result array based on how many rows returned
		result = new String[curArray[0].getCount() + curArray[1].getCount()][6];

		// create a new result counter to keep track of number of results
		int counter = 0;

		// go through array of cursors and put in values.
		for (int j = 0; j < curArray.length; j += 1) {
			// go through the data and fill the array
			if (curArray[j].moveToFirst()) {
				for (int i = 0; i < curArray[j].getCount(); i += 1) {
					// get the values
					result[counter][0] = ""
							+ curArray[j].getString(curArray[j]
									.getColumnIndex(champColumn));
					result[counter][1] = ""
							+ curArray[j].getString(curArray[j]
									.getColumnIndex("description"));
					result[counter][2] = ""
							+ curArray[j].getString(curArray[j]
									.getColumnIndex("role"));
					result[counter][3] = ""
							+ curArray[j].getString(curArray[j]
									.getColumnIndex("tips"));
					result[counter][4] = ""
							+ curArray[j].getString(curArray[j]
									.getColumnIndex("id"));

					// first pass is user table, second is default table.
					if (counter == 0) {
						result[counter][5] = "user";
					} else {
						result[counter][5] = "default";
					}

					// move to next row
					curArray[j].moveToNext();
					// Increment the resultCounter
					counter += 1;
				}
			}
		}

		database.close();

		return result;
	}

	public void deleteAllUserCounters() throws SQLiteException {
		SQLiteDatabase database = getWritableDatabase();

		// clear all rows from table
		database.delete(USER_COUNTER_TABLE, null, null);

		database.close();
	}

	public void restoreDefaultCounters() throws SQLiteException {
		// delete all user counters
		deleteAllUserCounters();

		SQLiteDatabase database = getWritableDatabase();

		// create new values for default database
		ContentValues values = new ContentValues();
		values.put("visible", "true");

		// update the database with new values. (make rows disappear)
		database.update(DEFAULT_COUNTER_TABLE, values, null, null);

		database.close();
	}

	public void deleteCounter(String id, String type) throws SQLiteException {
		SQLiteDatabase database = getWritableDatabase();

		// if deleting a row that is added by the user
		if (type.equals("user")) {
			database.delete(USER_COUNTER_TABLE, "id='" + id + "'", null);
		}
		// else deleting a default row. Only hide default rows.
		else {
			// create new values
			ContentValues values = new ContentValues();
			values.put("visible", "false");

			// update the database with new values. (make rows disappear)
			database.update(DEFAULT_COUNTER_TABLE, values, "id='" + id + "'",
					null);
		}

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

	public boolean backupUserCounters() throws SQLiteException {
		SQLiteDatabase database = getReadableDatabase();

		Cursor cur = database.rawQuery("SELECT * FROM " + USER_COUNTER_TABLE,
				null);

		// if (cur.moveToFirst()) {
		// do {
		// // ........
		// } while (!cur.isLast());
		// }

		database.close();
		return false;
	}

	public boolean importUserCounters() throws SQLiteException {

		return false;
	}

}
