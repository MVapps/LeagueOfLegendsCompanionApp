package com.LoLCompanionApp;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

public class DatabaseMain extends DatabaseHelper {

	public DatabaseMain(Context context) {
		super(context, "gameStats_en_US.sqlite");
	}

	public String[] getAllChampions() throws SQLiteException {
		Cursor cur;
		String[] result = null;

		SQLiteDatabase database = getReadableDatabase();

		// run the query
		cur = database.rawQuery(
				"SELECT displayName FROM champions ORDER BY displayName ASC",
				null);

		// initialize variable
		result = new String[cur.getCount()];

		// go through data and retrieve the name of drinks
		if (cur.moveToFirst()) {
			for (int i = 0; i < result.length; i += 1) {
				result[i] = cur.getString(0);
				cur.moveToNext();
			}
		}
		database.close();

		return result;
	}

	public String[] getAllChampionTitles() throws SQLiteException {
		Cursor cur;
		String[] result = null;

		SQLiteDatabase database = getReadableDatabase();

		// run the query
		cur = database.rawQuery(
				"SELECT title FROM champions ORDER BY displayName ASC", null);

		// initialize variable
		result = new String[cur.getCount()];

		// go through data and retrieve the name of drinks
		if (cur.moveToFirst()) {
			for (int i = 0; i < result.length; i += 1) {
				result[i] = cur.getString(0);
				cur.moveToNext();
			}
		}
		database.close();

		return result;
	}

	public String getChampionTitle(String champ) throws SQLiteException {
		String string = null;
		int id = getChampionId(champ);

		SQLiteDatabase database = getReadableDatabase();

		// run the query and get result
		Cursor cur = database.rawQuery(
				"SELECT title FROM champions WHERE id=?",
				new String[] { String.valueOf(id) });
		if (cur.moveToFirst()) {
			string = cur.getString(0);
		}

		database.close();

		return string;
	}

	public int getChampionId(String champ) throws SQLiteException {
		int result = 0;

		SQLiteDatabase database = getReadableDatabase();
		// run the query and get result
		Cursor cur = database.rawQuery(
				"SELECT id FROM champions WHERE displayName=?",
				new String[] { champ });
		if (cur.moveToFirst()) {
			result = cur.getInt(0);
		}
		database.close();

		return result;
	}

	public String removeSpecialChars(String name) {
		return name.replace(".", "").replace(" ", "").replace("/", "")
				.replace("'", "").replace(";", "").replace(":", "")
				.replace("-", "").replace("!", "").replace(",", "");
	}

	public String[][] getAllSkillsByChampName(String champ)
			throws SQLiteException {
		String[][] result = null;
		int id = getChampionId(champ);

		SQLiteDatabase database = getReadableDatabase();

		// run the query and get result
		Cursor cur = database.rawQuery(
				"SELECT * FROM championAbilities WHERE championId=?",
				new String[] { String.valueOf(id) });

		// go to first row
		if (cur.moveToFirst()) {
			result = new String[cur.getCount()][6];
			for (int i = 0; i < cur.getCount(); i += 1) {
				// get the two values
				result[i][0] = "" + cur.getString(cur.getColumnIndex("name"));
				result[i][1] = "" + cur.getString(cur.getColumnIndex("effect"));
				// if the effect column is empty add the text from the
				// description
				if (result[i][1].equals("null")) {
					result[i][1] = cur.getString(cur
							.getColumnIndex("description"));
				}
				result[i][2] = "" + cur.getString(cur.getColumnIndex("hotkey"));
				result[i][3] = "" + cur.getString(cur.getColumnIndex("cost"));
				result[i][4] = "" + cur.getString(cur.getColumnIndex("range"));
				result[i][5] = ""
						+ cur.getString(cur.getColumnIndex("cooldown"));
				// move to next row
				cur.moveToNext();
			}
		}

		database.close();

		return result;
	}

	public int getNumSkinsByChampName(String champ) throws SQLiteException {
		int result = 0;
		int id = getChampionId(champ);

		SQLiteDatabase database = getReadableDatabase();

		// run the query
		Cursor cur = database.rawQuery(
				"SELECT COUNT(*) FROM championSkins WHERE championId=?",
				new String[] { String.valueOf(id) });

		// go through data and retrieve the name of drinks
		if (cur.moveToFirst()) {
			// initialize variable
			result = cur.getInt(0);
		}
		database.close();

		return result;
	}

	public String[] getChampionCounters(String champion) {
		String[] string = null;
		int id = getChampionId(champion);

		SQLiteDatabase database = getReadableDatabase();

		// run the query and get result
		Cursor cur = database.rawQuery(
				"SELECT counters FROM champions WHERE id=?",
				new String[] { String.valueOf(id) });
		if (cur.moveToFirst()) {
			string = cur.getString(0).split(",");
		}

		database.close();

		return string;
	}

	public String getSkinName(String champ, int rank) {
		String skinName = null;

		champ = removeSpecialChars(champ).replace(" ", "");

		SQLiteDatabase database = getReadableDatabase();

		// run the query and get result
		Cursor cur = database.rawQuery(
				"SELECT displayName FROM championSkins WHERE portraitPath=?",
				new String[] { champ + "_" + rank + ".jpg" });
		if (cur.moveToFirst()) {
			skinName = cur.getString(0);
		}

		database.close();

		return skinName;
	}

	public String[] getChampionCounteredBy(String champion) {
		String[] string = null;
		int id = getChampionId(champion);

		SQLiteDatabase database = getReadableDatabase();

		// run the query and get result
		Cursor cur = database.rawQuery(
				"SELECT countered FROM champions WHERE id=?",
				new String[] { String.valueOf(id) });
		if (cur.moveToFirst() && cur.getString(0) != null) {
			string = cur.getString(0).split(",");
		}
		;

		database.close();

		return string;
	}

	public String getChampionAttributes(String champion) throws SQLiteException {
		Cursor cur;
		String result = "";
		int id = getChampionId(champion);

		SQLiteDatabase database = getReadableDatabase();

		// run the query
		cur = database.rawQuery("SELECT tags FROM champions WHERE id=?",
				new String[] { String.valueOf(id) });

		// go through data
		if (cur.moveToFirst()) {
			result = cur.getString(0).toUpperCase();
		}
		database.close();

		return result;
	}

	public String getChampionLore(String champion) {
		String result = null;
		int id = getChampionId(champion);

		SQLiteDatabase database = getReadableDatabase();

		// run the query
		Cursor cur = database.rawQuery(
				"SELECT description FROM champions WHERE id=?",
				new String[] { String.valueOf(id) });

		// go through data
		if (cur.moveToFirst()) {
			// initialize variable
			result = cur.getString(cur.getColumnIndex("description"));
		}
		database.close();

		return result;
	}

	public String[][] getChampionStats(String champion) {
		String result[][] = new String[9][2];
		int id = getChampionId(champion);

		SQLiteDatabase database = getReadableDatabase();

		// run the query
		Cursor cur = database.rawQuery("SELECT * FROM champions WHERE id=?",
				new String[] { String.valueOf(id) });

		// go through data and retrieve the name of drinks
		if (cur.moveToFirst()) {
			// initialize variable
			result[0][0] = cur.getString(cur.getColumnIndex("range"));
			result[1][0] = cur.getString(cur.getColumnIndex("moveSpeed"));
			result[2][0] = cur.getString(cur.getColumnIndex("armorBase"));
			result[3][0] = cur.getString(cur.getColumnIndex("manaBase"));
			result[4][0] = cur.getString(cur.getColumnIndex("manaRegenBase"));
			result[5][0] = cur.getString(cur.getColumnIndex("healthRegenBase"));
			result[6][0] = cur.getString(cur.getColumnIndex("magicResistBase"));
			result[7][0] = cur.getString(cur.getColumnIndex("healthBase"));
			result[8][0] = cur.getString(cur.getColumnIndex("attackBase"));
			result[2][1] = cur.getString(cur.getColumnIndex("armorBase"));
			result[3][1] = cur.getString(cur.getColumnIndex("manaLevel"));
			result[4][1] = cur.getString(cur.getColumnIndex("manaRegenLevel"));
			result[5][1] = cur
					.getString(cur.getColumnIndex("healthRegenLevel"));
			result[6][1] = cur
					.getString(cur.getColumnIndex("magicResistLevel"));
			result[7][1] = cur.getString(cur.getColumnIndex("healthLevel"));
			result[8][1] = cur.getString(cur.getColumnIndex("attackLevel"));
		}
		database.close();

		return result;
	}

}
