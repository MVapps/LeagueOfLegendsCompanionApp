package com.LoLCompanionApp;

import android.content.Context;

public class DatabaseCounterPicks extends DatabaseHelper {

	public DatabaseCounterPicks(Context context) {
		super(context, "counterPicksDatabase.sqlite");
	}

	// have one database with who counters who.
	// have function to backup the values (in case they added their own values
	// they want to keep when the database is updated with new champions)
	// "Bob" counters "Steve"
	// have import function to import counters from previous versions.
	// have two tables. One with default values, one with updated user-chosen
	// values.
	// backup only user-chosen values

}
