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

public class GeneralGuides extends Activity {
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.generalguides);

		initializeHeader();

		String[] guides = { "Buff Control", "How to Support", "Last Hitting",
				"Ward Placement", "Advanced Ward Placement",
				"Teemo mushroom guide" };

		// create the list
		// Creates list view
		ListView lv = (ListView) findViewById(R.id.menuList);
		lv.setAdapter(new ArrayAdapter<String>(this, R.layout.optionlist,
				guides));

		// Allows searchings
		lv.setTextFilterEnabled(true);

		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				String linky = null;
				if (position == 0) {
					linky = "http://www.lolpro.com/guides/game-play/172-game-play-controlling-buffs";
				} else if (position == 1) {
					linky = "http://www.lolpro.com/guides/game-play/198-how-to-support";
				} else if (position == 2) {
					linky = "http://www.lolpro.com/guides/game-play/166-game-play-last-hitting";
				} else if (position == 3) {
					linky = "http://i.imgur.com/9Z6xUh.jpg";
				} else if (position == 4) {
					linky = "http://img688.imageshack.us/img688/9762/wards.jpg";
				} else if (position == 5) {
					linky = "http://i.imgur.com/9GZ7f.jpg";
				} else if (position == 6) {
					linky = "";
				} else if (position == 7) {
					linky = "";
				} else if (position == 8) {
					linky = "";
				}

				// Loads up the webview
				Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri
						.parse(linky));
				startActivity(browserIntent);
			}
		});
	}

	public void initializeHeader() {
		TextView title = (TextView) findViewById(R.id.HeaderTitle);
		title.setText("General Guides");
	}

	public void back(View view) {
		finish();
	}

}
