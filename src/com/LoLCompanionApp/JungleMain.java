package com.LoLCompanionApp;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class JungleMain extends Activity {

	private DatabaseExtra database;
	private CreatureCountDown[] timers;
	private String[] creatures;
	private ArrayList<HashMap<String, String>> creatureList;
	private String defaultCreatureOrder;
	private int fadeLevel = 150;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.junglemain);

		initializeHeader();

		database = new DatabaseExtra(this);

		// set the default creature order
		defaultCreatureOrder = database.getDefaultCreatureOrder();
	}

	@Override
	public void onResume() {
		super.onResume();

		// reset the creatures
		addCreatures();
	}

	public void startNewGameCounts(View buttonView) {
		GridView gview = (GridView) findViewById(R.id.gridCreatures);

		// initialize all counters with initial values
		for (int i = 0; i < gview.getChildCount(); i += 1) {

			// if the timer is running
			if (timers[i] != null && timers[i].isRunning()) {
				timers[i].cancel();
				timers[i] = null;
			}

			// get new time, convert to milliseconds
			long spawnTime = database.getCreatureInitialSpawnTime(creatures[i]) * 1000;

			// new count down timer
			timers[i] = new CreatureCountDown(spawnTime, gview.getChildAt(i),
					R.id.textCreatureCount);

			// set new Opacity for the count text view
			gview.getChildAt(i).findViewById(R.id.textCreatureCount)
					.setBackgroundColor(Color.argb(fadeLevel, 0, 0, 0));
		}

		// start the counters at relatively the same time
		for (int i = 0; i < gview.getChildCount(); i += 1) {
			// start the counter
			timers[i].start();
		}
	}

	public void addCreatures() {
		// get the general preferences for how to view the champions
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(this);
		// get the creatures in the preferences, or get the default set
		creatures = prefs.getString("JungleCreaturePositions",
				defaultCreatureOrder).split(",");

		// initialize the timers
		timers = new CreatureCountDown[creatures.length];

		// create the creature list
		createHashMap();

		GridView gview = (GridView) findViewById(R.id.gridCreatures);
		gview.setAdapter(new CreatureListAdapter(new String[] { "name" },
				new int[] { R.id.textCreatureName },
				R.layout.junglecreatureview, creatureList));
		gview.setOnItemClickListener(new CreatureCountClick());
	}

	private void createHashMap() {
		// create a map list that stores the data for each champ
		creatureList = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> map;
		// add the data to the list
		for (int i = 0; i < creatures.length; i += 1) {
			map = new HashMap<String, String>();
			map.put("name", creatures[i]);
			creatureList.add(map);
		}
	}

	public void initializeHeader() {
		TextView title = (TextView) findViewById(R.id.HeaderTitle);
		title.setText("Jungle Timer");
	}

	public void back(View view) {
		finish();
	}

	public void editSettings(View buttonView) {
		Intent settingsPage = new Intent();
		settingsPage.setClassName("com.LoLCompanionApp",
				"com.LoLCompanionApp.JungleEdit");
		startActivity(settingsPage);
	}

	public class CreatureCountDown extends CountDownTimer {
		private TextView counter;
		private boolean running;

		public CreatureCountDown(long millisInFuture, View parent, int childId) {
			// 1000 milliseconds, therefore every second will tick
			super(millisInFuture, 1000);

			// assign the textFieldTag to the class variable
			counter = (TextView) parent.findViewById(childId);
		}

		@Override
		public void onFinish() {
			running = false;
			// colorize and notify which creature spawned
			counter.setText("!!!");
			counter.setBackgroundColor(Color.argb(fadeLevel, 255, 0, 0));

			// CREATE NOTIFICATION SYSTEM

			SharedPreferences prefs = PreferenceManager
					.getDefaultSharedPreferences(getBaseContext());

			String notification = prefs.getString("JungleCreatureNotification",
					database.getDefaultNotificationType());

			if (notification.equals("both") || notification.equals("vibrate")) {
				Vibrator vibrator;
				vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
				vibrator.vibrate(500);
			}

			if (notification.equals("both") || notification.equals("sound")) {
				
			}

			// Uri alert = RingtoneManager
			// .getDefaultUri(RingtoneManager.TYPE_ALARM);
			// if (alert == null) {
			// // alert is null, using backup
			// alert = RingtoneManager
			// .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
			// if (alert == null) {
			// // alert backup is null, using 2nd backup
			// alert = RingtoneManager
			// .getDefaultUri(RingtoneManager.TYPE_RINGTONE);
			// }
			// }
			//
			// Ringtone ring = RingtoneManager.getRingtone(
			// getApplicationContext(), alert);
			// ring.play();

		}

		@Override
		public void onTick(long millisUntilFinished) {
			// countdown timer is running
			running = true;

			// divide millis by 1000 for seconds.
			// since both ints, will be int
			int min = ((int) millisUntilFinished / 1000) / 60;
			// get remainder
			int sec = ((int) millisUntilFinished / 1000) % 60;

			// print to the textView
			counter.setText(String.format("%d:%02d", min, sec));
		}

		public boolean isRunning() {
			return running;
		}
	}

	// click listener
	public class CreatureCountClick implements OnItemClickListener {
		public void onItemClick(AdapterView<?> adapterView, View clickedView,
				int creaturePosition, long id) {

			// get the textview that was clicked
			TextView counterText = (TextView) clickedView
					.findViewById(R.id.textCreatureCount);

			// if the counter has been initialized
			if (timers[creaturePosition] != null) {
				// check if the counter is running
				if (!timers[creaturePosition].isRunning()) {
					// start counter
					startCounter(creaturePosition, clickedView);
					// add transparency
					counterText.setBackgroundColor(Color.argb(fadeLevel, 0, 0,
							0));
				}
				// if the counter is running, cancel it.
				else {
					timers[creaturePosition].cancel();
					timers[creaturePosition] = null;

					// reset text to nothing
					counterText.setText("");
					// add transparency
					counterText.setBackgroundColor(Color.argb(0, 0, 0, 0));
				}
			}
			// if the timer has not been initialized, create a new one
			else {
				startCounter(creaturePosition, clickedView);
				// add transparency
				counterText.setBackgroundColor(Color.argb(fadeLevel, 0, 0, 0));
			}
		}

		private void startCounter(int creaturePosition, View clickedView) {
			long respawnTime = database
					.getCreatureRespawnTime(creatures[creaturePosition]) * 1000;

			// new count down timer
			timers[creaturePosition] = new CreatureCountDown(respawnTime,
					clickedView, R.id.textCreatureCount);
			// start the counter
			timers[creaturePosition].start();
		}
	}

	class CreatureListAdapter extends SimpleAdapter {
		CreatureListAdapter(String[] parameters, int[] resourceIds,
				int layoutId, ArrayList<HashMap<String, String>> hashMap) {
			super(JungleMain.this, hashMap, layoutId, parameters, resourceIds);
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			View row = super.getView(position, convertView, parent);

			LinearLayout creaturePicture = (LinearLayout) row
					.findViewById(R.id.layoutJunglePcture);
			TextView creatureName = (TextView) row
					.findViewById(R.id.textCreatureName);

			int path = getResources().getIdentifier(
					"creature_"
							+ creatureName.getText().toString()
									.replace(" ", "").toLowerCase(),
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