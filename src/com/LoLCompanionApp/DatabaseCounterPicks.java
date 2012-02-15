package com.LoLCompanionApp;

import android.content.Context;

public class DatabaseCounterPicks extends DatabaseHelper {

	public DatabaseCounterPicks(Context context, String dbName) {
		super(context, dbName);
	}

	// have one table with who counters who.
	// have function to backup the values (in case they added their own values
	// they want to keep when the databse is updated with nw champions)
	// "Bob" counters "Steve"
	// have import function to import counters from previous versions.
	// have two tables. One with default values, one with updated user-chosen
	// values.
	// backup only user-chosen values

}
