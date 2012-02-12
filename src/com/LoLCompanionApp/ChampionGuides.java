package com.LoLCompanionApp;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class ChampionGuides extends Activity {

	// Pagelist
	static final String[] GUIDES = new String[] { "LoLPro", "CLG", "Solomid",
			"Mobafire" };
	String champion;
	DatabaseHelper database;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.champguides);

		// create database object
		database = new DatabaseHelper(this);

		// Loads Preferences
		champion = getIntent().getStringExtra("name");

		// create the header
		createHeader();

		// Creates list view
		ListView lv = (ListView) findViewById(R.id.menuList);
		lv.setAdapter(new ArrayAdapter<String>(this, R.layout.optionlist,
				GUIDES));

		// Allows searchings
		lv.setTextFilterEnabled(true);

		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String linky = null;

				// Gets champion from champion listview

				// Mobafire
				if (position == 0) {
					String newchampion = champion;
					linky = "http://lolpro.com/guides/";
					newchampion = newchampion.toLowerCase();
					newchampion = newchampion.replace("'", "").replace(".", "")
							.replace(" ", "-");
					if (newchampion == "dr-mundo") {
						newchampion = newchampion + "n";
					}
					linky = linky + newchampion;
				} else if (position == 1) {
					linky = "http://clgaming.net/guides?championID=";
					String ID = Integer.toString((database
							.getChampionId(champion)));
					linky = linky + ID + "&sort=upVotes";

				} else if (position == 2) {

					String newchampion = champion;
					linky = "http://solomid.net/guides.php?champ=";
					newchampion = newchampion.toLowerCase();
					newchampion = newchampion.replace("'", "").replace(".", "")
							.replace(" ", "");
					linky = linky + newchampion

					+ "&sort=3&submitted=0&approved=1&featured=1";
				} else if (position == 3) {

					String newchampion = champion;
					linky = "http://www.mobafire.com/league-of-legends/";

					newchampion = newchampion.toLowerCase();
					newchampion = newchampion.replace("'", "").replace(".", "")
							.replace(" ", "-");
					linky = linky + newchampion + "-guide";

				}
				// Loads up the webview
				Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri
						.parse(linky));
				startActivity(browserIntent);
			}
		});

		database.close();
	}

	private void createHeader() {
		// Creates header
		TextView champName = (TextView) findViewById(R.id.champName);
		TextView champTitle = (TextView) findViewById(R.id.champTitle);
		ImageView champImage = (ImageView) findViewById(R.id.champPicture);
		champName.setText(champion);
		String champPic = database.changeSpecialChars(champion);
		int path = getResources().getIdentifier(champPic.toLowerCase(),
				"drawable", "com.Leeg");

		champTitle.setText(database.getChampionTitle(champion));
		champImage.setImageResource(path);
	}

	public void back(View view) {
		finish();
	}
}
