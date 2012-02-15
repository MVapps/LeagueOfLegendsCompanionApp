package com.LoLCompanionApp;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ChampionCounterpicks extends Activity {

	DatabaseMain database;
	String champion;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.champcounterpicks);

		database = new DatabaseMain(this, "gameStats_en_US.sqlite");

		// get the name of the chosen champion
		champion = getIntent().getStringExtra("name");

		createHeader();

		// create temp message
		// *******************************************
		TextView text1 = (TextView) findViewById(R.id.textCounters);
		text1.setBackgroundResource(R.drawable.bgskills);
		text1.setTextColor(Color.BLACK);
		text1.setPadding(15, 15, 15, 15);
		text1.setGravity(Gravity.CENTER);
		text1.setText("Counterpicks is currently not implemented in this release.");
		TextView text2 = (TextView) findViewById(R.id.textCountersBy);
		text2.setText("");
		// *******************************************

		// initializeGalleries();

	}

	// private void initializeGalleries() {
	// // get the gallery and populate it with the pictures of skins
	// Gallery counters = (Gallery) findViewById(R.id.galleryCounters);
	// Gallery countered = (Gallery) findViewById(R.id.galleryCountersBy);
	//
	// // array holding the values
	// String[] countersChamps = database.getChampionCounters(champion);
	// String[] counteredChamps = database.getChampionCounteredBy(champion);
	//
	// Toast.makeText(this, countersChamps[0], Toast.LENGTH_SHORT).show();
	//
	// // Counters champs gallery
	// if (countersChamps != null) {
	// // Array adapter to display our values in the gallery control
	// counters.setAdapter(new IconicAdapter(countersChamps));
	// // set the position of the gallery
	// setPosition(counters);
	// } else {
	// // if no data found
	// countersChamps = new String[] { "Counters: \nNo data" };
	// // set the text
	// TextView text = (TextView) findViewById(R.id.textCounters);
	// text.setText(countersChamps[0]);
	// }
	//
	// // countered champs gallery
	// if (counteredChamps != null) {
	// // Array adapter to display our values in the gallery control
	// countered.setAdapter(new IconicAdapter(counteredChamps));
	// // set the position of the gallery
	// setPosition(countered);
	// } else {
	// // if no data found
	// counteredChamps = new String[] { "Countered By: \nNo data" };
	// // set the text
	// TextView text = (TextView) findViewById(R.id.textCountersBy);
	// text.setText(counteredChamps[0]);
	// }
	// }
	//
	// private void setPosition(Gallery gal) {
	// // ensure that the position of the images starts on the left side:
	// DisplayMetrics metrics = new DisplayMetrics();
	// getWindowManager().getDefaultDisplay().getMetrics(metrics);
	//
	// MarginLayoutParams mlp1 = (MarginLayoutParams) gal.getLayoutParams();
	// mlp1.setMargins(-(metrics.widthPixels / 2), mlp1.topMargin,
	// mlp1.rightMargin, mlp1.bottomMargin);
	// }
	//
	// class IconicAdapter extends ArrayAdapter<String> {
	// String[] champions;
	//
	// IconicAdapter(String[] champNames) {
	// // pass all parameters to the ArayAdapter
	// super(ChampionCounterpicks.this, R.layout.icon, R.id.champName,
	// champNames);
	//
	// champions = champNames;
	// }
	//
	// public View getView(int position, View convertView, ViewGroup parent) {
	// View row = super.getView(position, convertView, parent);
	// ImageView icon = (ImageView) row.findViewById(R.id.champPicture);
	//
	// String champ = database.changeSpecialChars(champions[position])
	// .toLowerCase();
	//
	// // get the image path based on the name of the variable being put on
	// // the screen
	// int path = getResources().getIdentifier(champ, "drawable",
	// "com.LoLCompanionApp");
	//
	// // if a picture was found
	// if (path != 0) {
	// // set the image
	// icon.setImageResource(path);
	// }
	// return (row);
	// }
	// }

	private void createHeader() {
		// Creates header
		TextView champName = (TextView) findViewById(R.id.champName);
		TextView champTitle = (TextView) findViewById(R.id.champTitle);
		ImageView champImage = (ImageView) findViewById(R.id.champPicture);
		champName.setText(champion);
		String champPic = database.removeSpecialChars(champion);
		int path = getResources().getIdentifier(champPic.toLowerCase()+"_square_0",
				"drawable", "com.LoLCompanionApp");

		champTitle.setText(database.getChampionTitle(champion));
		champImage.setImageResource(path);
	}

	public void back(View view) {
		finish();
	}

}
