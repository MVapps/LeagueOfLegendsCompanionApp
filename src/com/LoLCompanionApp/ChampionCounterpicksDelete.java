package com.LoLCompanionApp;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ChampionCounterpicksDelete extends Activity {

	DatabaseMain databaseMain;
	DatabaseExtra databaseExtra;
	String champion;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.champcounterpicksdelete);

		databaseMain = new DatabaseMain(this);
		databaseExtra = new DatabaseExtra(this);

		// get the name of the chosen champion
		champion = getIntent().getStringExtra("name");

		createHeader();
		
		
		
	}
	
	private void deleteCounterInformation(View view)
	{
		
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
