package com.LoLCompanionApp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ChampionCounterpicksEditMenu extends Activity {

	private DatabaseMain databaseMain;
	private DatabaseExtra databaseExtra;
	private String champion;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.champcounterpickseditmenu);

		databaseMain = new DatabaseMain(this);
		databaseExtra = new DatabaseExtra(this);

		// get the name of the chosen champion
		champion = getIntent().getStringExtra("name");

		createHeader();
	}

	public void deleteCounters(View view) {
		// go to next page on button pressed
		Intent deletePage = new Intent();
		deletePage.setClassName("com.LoLCompanionApp",
				"com.LoLCompanionApp.ChampionCounterpicksDelete");
		deletePage.putExtra("name", champion);
		deletePage.putExtra("page", view.getTag().toString());
		startActivity(deletePage);
	}

	public void addCounters(View button) {
		Intent addNewPage = new Intent();
		addNewPage.setClassName("com.LoLCompanionApp",
				"com.LoLCompanionApp.ChampionCounterpicksAdd");
		addNewPage.putExtra("name", champion);
		startActivity(addNewPage);
	}

	public void defaultCounters(View button) {
		databaseExtra.restoreDefaultCounters();

		// inform user of changes
		Toast.makeText(this, "Counters restored to default.",
				Toast.LENGTH_SHORT).show();
	}

	public void backupCounters(View button) {
		if (databaseExtra.backupUserCounters()) {
			Toast.makeText(this, "Backup succesful to SD card.",
					Toast.LENGTH_LONG).show();
		} else {
			Toast.makeText(this, "Backup was not succesful.", Toast.LENGTH_LONG)
					.show();
		}
	}

	public void restoreCounters(View button) {
		if (databaseExtra.importUserCounters()) {
			Toast.makeText(this, "Database import sucessful.",
					Toast.LENGTH_LONG).show();
		} else {
			Toast.makeText(this,
					"There was a problem with importing the user dataabse.",
					Toast.LENGTH_LONG).show();
		}
	}

	private void createHeader() {
		// Creates header
		TextView champName = (TextView) findViewById(R.id.champName);
		TextView champTitle = (TextView) findViewById(R.id.champTitle);
		ImageView champImage = (ImageView) findViewById(R.id.champPicture);
		champName.setText(champion);
		String champPic = databaseMain.removeSpecialChars(champion);
		int path = getResources().getIdentifier(
				champPic.toLowerCase() + "_square_0", "drawable",
				"com.LoLCompanionApp");

		champTitle.setText(databaseMain.getChampionTitle(champion));
		champImage.setImageResource(path);
	}

	public void back(View view) {
		finish();
	}
}
