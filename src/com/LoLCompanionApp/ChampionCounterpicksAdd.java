package com.LoLCompanionApp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class ChampionCounterpicksAdd extends Activity {

	private DatabaseMain databaseMain;
	private DatabaseExtra databaseExtra;
	private String champion;
	private static ArrayList<HashMap<String, String>> allChampions;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.champcounterpicksadd);

		databaseMain = new DatabaseMain(this);
		databaseExtra = new DatabaseExtra(this);

		// get the name of the chosen champion
		champion = getIntent().getStringExtra("name");

		createHeader();
		createChampionHashMap();
		setAutocompleteFieldNames();
	}

	public void createChampionHashMap() {
		// get all champions from the database
		String[] champions = databaseMain.getAllChampions();

		// convert the array into a hashmap.
		allChampions = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> map;
		// add the data to the list
		for (int i = 0; i < champions.length; i += 1) {
			map = new HashMap<String, String>();
			map.put("name", champions[i]);
			allChampions.add(map);
		}
	}

	public void setAutocompleteFieldNames() {
		int[] viewIds = { R.id.autoCompleteChampionName,
				R.id.autoCompleteCountersName };

		for (int i = 0; i < viewIds.length; i += 1) {
			final AutoCompleteTextView autoText = (AutoCompleteTextView) findViewById(viewIds[i]);

			autoText.setAdapter(new ChampListAdapter(new String[] { "name" },
					new int[] { R.id.champName }, R.layout.quickchamplist));

			if (viewIds[i] == R.id.autoCompleteCountersName) {
				// set default text to champion selected
				autoText.setText(champion);
			}

			autoText.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> p, View v, int pos,
						long id) {
					@SuppressWarnings("unchecked")
					Map<String, String> map = (Map<String, String>) p
							.getItemAtPosition(pos);
					String itemName = map.get("name");
					autoText.setText(itemName);
				}
			});
		}
	}

	public void addNewCounter(View view) {
		// get all values inputed into the fields
		String counter = ((AutoCompleteTextView) findViewById(R.id.autoCompleteCountersName))
				.getText().toString();
		String champ = ((AutoCompleteTextView) findViewById(R.id.autoCompleteChampionName))
				.getText().toString();
		String role = ((EditText) findViewById(R.id.editRole)).getText()
				.toString();
		String tips = ((EditText) findViewById(R.id.editTip)).getText()
				.toString();
		String description = ((EditText) findViewById(R.id.editDescription))
				.getText().toString();

		if (counter.equals("")) {
			Toast.makeText(this, "Please provide a Champion.",
					Toast.LENGTH_LONG).show();
		} else if (champ.equals("")) {
			Toast.makeText(this, "Please provide a Counter champion.",
					Toast.LENGTH_LONG).show();
		} else if (role.equals("")) {
			Toast.makeText(this, "Please provide a Role.", Toast.LENGTH_LONG)
					.show();
		} else if (description.equals("")) {
			Toast.makeText(this, "Please provide a Description.",
					Toast.LENGTH_LONG).show();
		} else {
			// add values to database
			databaseExtra
					.addNewCounter(counter, champ, role, tips, description);

			// notify user that values were added
			Toast.makeText(
					this,
					"New counter information has been added:\n" + counter
							+ " counters " + champ, Toast.LENGTH_LONG).show();

			// restart the page to clear the screen
			finish();
			startActivity(getIntent());
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

	class ChampListAdapter extends SimpleAdapter {
		ChampListAdapter(String[] parameters, int[] resourceIds, int layoutId) {
			super(ChampionCounterpicksAdd.this,
					ChampionCounterpicksAdd.allChampions, layoutId,
					parameters, resourceIds);
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			View row = super.getView(position, convertView, parent);

			ImageView icon = (ImageView) row.findViewById(R.id.champPicture);
			TextView textName = (TextView) row.findViewById(R.id.champName);

			String champion = textName.getText().toString();

			// convert name to a usable format for finding pictures
			String champImg = champion.toLowerCase();
			champImg = databaseMain.removeSpecialChars(champImg);
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
