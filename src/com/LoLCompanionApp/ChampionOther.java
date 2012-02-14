package com.LoLCompanionApp;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class ChampionOther extends Activity {

	private String champion;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.champother);

		initializeHeader();
		// get the name of the chosen champion
		champion = getIntent().getStringExtra("name");

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

				String choice = ((TextView) view.findViewById(R.id.menuOther))
						.getText().toString();

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
	}

	public void initializeHeader() {
		TextView title = (TextView) findViewById(R.id.HeaderTitle);
		title.setText("General Guides");
	}

}
