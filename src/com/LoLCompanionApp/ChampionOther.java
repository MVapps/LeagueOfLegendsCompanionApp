package com.LoLCompanionApp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class ChampionOther extends Activity {

	private String champion;
	private DatabaseMain database;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.champother);
		
		// get the name of the chosen champion
		champion = getIntent().getStringExtra("name");
		database = new DatabaseMain(this);

		createHeader();
		
		String[] otherInfo = { "Lore", "Skins" };

		// create the list
		// Creates list view
		ListView lv = (ListView) findViewById(R.id.menuOther);
		lv.setAdapter(new ArrayAdapter<String>(this, R.layout.optionlist,
				otherInfo));

		// Allows searchings
		lv.setTextFilterEnabled(true);

		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				String choice = (String) ((TextView) view).getText();

				// go to next page based on button pressed
				Intent Page2 = new Intent();
				Page2.setClassName("com.LoLCompanionApp",
						"com.LoLCompanionApp.Champion" + choice);
				Page2.putExtra("name", champion);
				startActivity(Page2);

				// String linky = null;
				//
				// if (position == 0) {
				// linky = "";
				// } else if (position == 1) {
				// linky = "";
				// } else if (position == 2) {
				// linky = "";
				// } else if (position == 3) {
				// linky = "";
				// }
				//
				// // Loads up the page
				// Intent newPage = new Intent();
				// newPage.putExtra("name", champion);
				// newPage.setClassName("com.LoLCompanionApp", linky);
				// startActivity(newPage);
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
		String champPic = database.removeSpecialChars(champion);
		int path = getResources().getIdentifier(
				champPic.toLowerCase() + "_square_0", "drawable",
				"com.LoLCompanionApp");

		champTitle.setText(database.getChampionTitle(champion));
		champImage.setImageResource(path);
	}
}
