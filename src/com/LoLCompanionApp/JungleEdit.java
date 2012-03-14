package com.LoLCompanionApp;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class JungleEdit extends Activity {

	DatabaseExtra database;
	String defaultCreatureOrder, defaultNotificationType;
	String[] creatures;
	ArrayList<HashMap<String, String>> creatureHashMap;
	GridView gview;
	SharedPreferences prefs;

	//
	// types of settings:
	// - order of creatures
	// - alarm type (sound/vibrate/both)
	//

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.jungleedit);

		initializeHeader();

		database = new DatabaseExtra(this);

		prefs = PreferenceManager.getDefaultSharedPreferences(this);

		// set the default creature order
		defaultCreatureOrder = database.getDefaultCreatureOrder();
		defaultNotificationType = database.getDefaultNotificationType();

		gview = (GridView) findViewById(R.id.gridCreaturesEdit);
	}

	@Override
	public void onResume() {
		super.onResume();

		// reset the creatures
		addCreatures();
		getSettings();
	}

	public void getSettings() {
		// get the current settings
		String notification = prefs.getString("JungleCreatureNotification",
				defaultNotificationType);

		RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioGroup);

		for (int i = 0; i < radioGroup.getChildCount(); i += 1) {
			RadioButton curButton = (RadioButton) radioGroup.getChildAt(i);

			// go through buttons and check the right one
			if (curButton.getTag().toString().equals(notification)) {
				curButton.setChecked(true);
			} else {
				curButton.setChecked(false);
			}
		}
	}

	public void addCreatures() {
		// get the creatures in the preferences, or get the default set
		creatures = prefs.getString("JungleCreaturePositions",
				defaultCreatureOrder).split(",");

		// create the creature list
		createHashMap();

		gview.setAdapter(new CreatureEditListAdapter(new String[] { "name" },
				new int[] { R.id.textCreatureName },
				R.layout.junglecreatureeditview, creatureHashMap));
		gview.setOnItemClickListener(new EditCreatureClick());
	}

	private void createHashMap() {
		// create a map list that stores the data for each champ
		creatureHashMap = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> map;
		// add the data to the list
		for (int i = 0; i < creatures.length; i += 1) {
			map = new HashMap<String, String>();
			map.put("name", creatures[i]);
			creatureHashMap.add(map);
		}
	}

	public void changeNotificationType(View radioButton) {
		// add temporary text to a hidden field
		TextView notification = (TextView) findViewById(R.id.textNotificationType);
		notification.setText(radioButton.getTag().toString());
	}

	public void saveSettings(View buttonSave) {
		String creatureOrder = "";

		// go through the gridview and get the names of the creatures in order
		for (int i = 0; i < gview.getChildCount(); i += 1) {
			LinearLayout creatureLayout = (LinearLayout) gview.getChildAt(i);
			TextView creatureView = (TextView) creatureLayout
					.findViewById(R.id.textCreatureName);

			// append creature name to the list
			creatureOrder += creatureView.getText().toString();

			// if not on the last pass, append a deliminer
			if ((i + 1) != gview.getChildCount()) {
				creatureOrder += ",";
			}
		}
		// put in the creature order into the general prefs
		prefs.edit().putString("JungleCreaturePositions", creatureOrder);

		// get the notification type selected
		TextView creatureNotification = (TextView) findViewById(R.id.textNotificationType);

		// if non eselected make it the default one
		if (creatureNotification.getText().toString() == "") {
			creatureNotification.setText(defaultNotificationType);
		}

		// add the new settings to the shared preferences
		prefs.edit().putString("JungleCreatureNotification",
				creatureNotification.getText().toString());

		// commit both additions and notify user
		prefs.edit().commit();
		Toast.makeText(this, "Settings saved!", Toast.LENGTH_SHORT).show();
	}

	public void defaultSettings(View buttonDefault) {
		// remove the prefs for the jungle timer
		prefs.edit().remove("JungleCreaturePositions");
		prefs.edit().remove("JungleCreatureNotification");

		prefs.edit().commit();

		// tell user of status
		Toast.makeText(this, "Default settings resotred.", Toast.LENGTH_SHORT)
				.show();

		// restart screen so changes take effect
		finish();
		startActivity(getIntent());

	}

	public void initializeHeader() {
		TextView title = (TextView) findViewById(R.id.HeaderTitle);
		title.setText("Jungle Timer - Settings");
	}

	public String getCreatureImageName(String creature) {
		return "creature_" + creature.replace(" ", "").toLowerCase();
	}

	public void back(View view) {
		finish();
	}

	private class EditCreatureClick implements OnItemClickListener {

		private String[] creaturesPick = { "Golems", "Wraiths", "Wolves",
				"Aincent Golem", "Lizard Elder", "Dragon", "Baron Nashor" };
		private AlertDialog.Builder builder;
		private LinearLayout clickedLayout;

		EditCreatureClick() {
			// initialize object
			builder = new AlertDialog.Builder(JungleEdit.this);

			// add values to the alert dialog
			builder.setTitle("Pick a creature");
			builder.setItems(creaturesPick, new OnCreatureDialogSelect());
		}

		public void onItemClick(AdapterView<?> adapterView, View clickedView,
				int creaturePosition, long id) {

			clickedLayout = (LinearLayout) clickedView;

			// create the alert dialog
			AlertDialog alert = builder.create();
			// show dialog when clicked
			alert.show();
		}

		private class OnCreatureDialogSelect implements
				DialogInterface.OnClickListener {

			public void onClick(DialogInterface dialog, int item) {

				Toast.makeText(getApplicationContext(), creaturesPick[item],
						Toast.LENGTH_SHORT).show();

				TextView creatureText = (TextView) clickedLayout
						.findViewById(R.id.textCreatureName);
				ImageView creatureImage = (ImageView) clickedLayout
						.findViewById(R.id.imageCreaturePcture);

				creatureText.setText(creaturesPick[item]);

				int path = getResources()
						.getIdentifier(
								getCreatureImageName(creatureText.getText()
										.toString()), "drawable",
								"com.LoLCompanionApp");

				creatureImage.setBackgroundResource(path);
			}
		}
	}

	private class CreatureEditListAdapter extends SimpleAdapter {
		CreatureEditListAdapter(String[] parameters, int[] resourceIds,
				int layoutId, ArrayList<HashMap<String, String>> hashMap) {
			super(JungleEdit.this, hashMap, layoutId, parameters, resourceIds);
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			View row = super.getView(position, convertView, parent);

			ImageView creaturePicture = (ImageView) row
					.findViewById(R.id.imageCreaturePcture);
			TextView creatureName = (TextView) row
					.findViewById(R.id.textCreatureName);

			int path = getResources().getIdentifier(
					getCreatureImageName(creatureName.getText().toString()),
					"drawable", "com.LoLCompanionApp");

			// if a picture was found
			if (path != 0) {
				// set the image
				creaturePicture.setBackgroundResource(path);
			}

			return row;
		}
	}

}
