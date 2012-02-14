package com.LoLCompanionApp;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class ChampionList extends Activity {

	private static ArrayList<HashMap<String, String>> champList;
	private static String[] champs, champTitles;
	private DatabaseHelper database;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.champlistlayout);

		initializeHeader();

		database = new DatabaseHelper(this);
		champs = database.getAllChampions();

		// Creates the listview
		final GridView lv = (GridView) findViewById(R.id.champList);
		lv.setTextFilterEnabled(true);

		// create a map list that stores the data for each champ
		champList = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> map;
		// add the data to the list
		for (int i = 0; i < champs.length; i += 1) {
			map = new HashMap<String, String>();
			map.put("name", champs[i]);
			champList.add(map);
		}
		// add custom adapter to display data
		lv.setAdapter(new ChampListAdapter());

		// set a on-click listener to each row
		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> adapterView, View view,
					int position, long id) {
				// //
				// LinearLayout clickedList = (LinearLayout)
				// view.findViewById(R.id.listChamps);
				// clickedList.setBackgroundColor(Color.BLUE);
				//
				// from the view of each row, get the text view and get the name
				// of the champion
				TextView text = (TextView) view.findViewById(R.id.champName);
				String champion = text.getText().toString();

				// Goes to next page
				Intent champInfo = new Intent();
				champInfo.putExtra("name", champion);
				champInfo.setClassName("com.LoLCompanionApp",
						"com.LoLCompanionApp.ChampionOptions");
				startActivity(champInfo);
			}
		});

		database.close();
	}

	public void keyboard(View view) {
		// open/close keyboard when click button
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
	}

	public void initializeHeader() {
		TextView title = (TextView) findViewById(R.id.HeaderTitle);
		title.setText("Champion List");
	}

	public void back(View view) {
		finish();
	}

	class ChampListAdapter extends SimpleAdapter {
		ChampListAdapter() {
			// pass all parameters to the ArayAdapter
			super(ChampionList.this, ChampionList.champList,
					R.layout.champlist, new String[] { "name" },
					new int[] { R.id.champName });
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			View row = super.getView(position, convertView, parent);

			ImageView icon = (ImageView) row.findViewById(R.id.champPicture);
			TextView textName = (TextView) row.findViewById(R.id.champName);

			String champion = textName.getText().toString();

			// convert name to a usable format for finding pictures
			String champImg = champion.toLowerCase();
			champImg = database.removeSpecialChars(champImg);
			// get the image path based on the name of the variable being put on
			// the screen
			int path = getResources().getIdentifier(champImg + "_square_0",
					"drawable", "com.LoLCompanionApp");

			// if a picture was found
			if (path != 0) {
				// set the image
				icon.setImageResource(path);
			}

			return row;
		}
	}
}