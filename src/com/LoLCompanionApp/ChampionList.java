package com.LoLCompanionApp;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class ChampionList extends Activity {

	private static ArrayList<HashMap<String, String>> champList;
	private static String[] champs, champTitles;
	private DatabaseMain database;
	private String viewType;
	SharedPreferences prefs;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.champlistlayout);

		// get the general preferences for how to view the champions
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		viewType = prefs.getString("ViewType", "grid");

		initializeHeader();

		database = new DatabaseMain(this);

		champs = database.getAllChampions();
		champTitles = database.getAllChampionTitles();

		// create the hash map to be used to populate the list
		createHashMap();

		GridView gview;
		ListView lview;

		// Creates the list/grid view
		if (viewType.equals("list")) {
			lview = (ListView) findViewById(R.id.champList);
			lview.setTextFilterEnabled(true);
			lview.setAdapter(new ChampListAdapter(new String[] { "name",
					"title" }, new int[] { R.id.champName, R.id.champTitle },
					R.layout.champlistrow));
			lview.setOnItemClickListener(new ChampItemClick());
		} else {
			gview = (GridView) findViewById(R.id.champGrid);
			gview.setTextFilterEnabled(true);
			gview.setAdapter(new ChampListAdapter(new String[] { "name" },
					new int[] { R.id.champName }, R.layout.champlistgrid));
			gview.setOnItemClickListener(new ChampItemClick());
		}

		database.close();
	}

	private void createHashMap() {
		// create a map list that stores the data for each champ
		champList = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> map;
		// add the data to the list
		for (int i = 0; i < champs.length; i += 1) {
			map = new HashMap<String, String>();
			map.put("name", champs[i]);
			map.put("title", champTitles[i]);
			champList.add(map);
		}
	}

	// click listener
	public class ChampItemClick implements OnItemClickListener {
		public void onItemClick(AdapterView<?> adapterView, View view,
				int position, long id) {
			TextView text = (TextView) view.findViewById(R.id.champName);
			String champion = text.getText().toString();

			// Goes to next page
			Intent champInfo = new Intent();
			champInfo.putExtra("name", champion);
			champInfo.setClassName("com.LoLCompanionApp",
					"com.LoLCompanionApp.ChampionOptions");
			startActivity(champInfo);
		}
	}

	public void keyboard(View view) {
		// open/close keyboard when click button
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
	}

	public void back(View view) {
		finish();
	}

	public void initializeHeader() {
		TextView title = (TextView) findViewById(R.id.HeaderTitle);
		title.setText("Champion List");

		// if grid view, change button to display list view
		if (viewType.equals("grid")) {
			Button changeView = (Button) findViewById(R.id.buttonListType);
			changeView.setBackgroundResource(R.drawable.viewlist);
		}
	}

	public void changeListType(View view) {

		SharedPreferences.Editor editor = prefs.edit();

		if (viewType.equals("list")) {
			// change prefs
			editor.putString("ViewType", "grid");
		} else {
			// change prefs
			editor.putString("ViewType", "list");
		}

		editor.commit();

		// restart screen with new view type
		finish();
		startActivity(getIntent());
	}

	class ChampListAdapter extends SimpleAdapter {
		ChampListAdapter(String[] parameters, int[] resourceIds, int layoutId) {
			super(ChampionList.this, ChampionList.champList, layoutId,
					parameters, resourceIds);
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