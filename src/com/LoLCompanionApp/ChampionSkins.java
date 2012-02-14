package com.LoLCompanionApp;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TextView;

public class ChampionSkins extends Activity {

	DatabaseHelper database;
	static String champion;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.champskins);

		database = new DatabaseHelper(this);

		// get the name of the chosen champion
		champion = getIntent().getStringExtra("name");

		createHeader();

		// get how many
		int skins = database.getNumSkinsByChampName(champion);

		String[] texts = new String[skins];
		for (int i = 0; i < skins; i += 1) {
			texts[i] = database.getSkinName(champion,i);
		}

		if (skins > 0) {
			// get the gallery and populate it with the pictures of skins
			Gallery gallery = (Gallery) findViewById(R.id.skinGallery);

			gallery.setAdapter(new SkinAdapter(texts));

			// // set an on-click listener to display a hi-res image
			// gallery.setOnItemClickListener(new OnItemClickListener() {
			// public void onItemClick(AdapterView<?> v, View view,
			// int position, long id) {
			//
			// // go to page with full hi-res image
			//
			// }
			// });
		} else {
			// if nto skins were found in database
			TextView text = (TextView) findViewById(R.id.textSkinsTitle);
			text.setText("Champion Skins: \nNone found.");
		}
	}

	class SkinAdapter extends ArrayAdapter<String> {

		SkinAdapter(String[] text) {
			// pass all parameters to the ArayAdapter
			super(ChampionSkins.this, R.layout.skinview, R.id.textGallery, text);
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			View row = super.getView(position, convertView, parent);
			ImageView icon = (ImageView) row.findViewById(R.id.champSkin);

			String champ = database.removeSpecialChars(champion.toLowerCase());

			// get the image path based on the name of the variable being put on
			// the screen
			int path = getResources().getIdentifier(
					champ + "_" + String.valueOf(position), "drawable",
					"com.LoLCompanionApp");

			// if a picture was found
			if (path != 0) {
				// set the image
				icon.setImageResource(path);
			}
			return (row);
		}
	}

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
