package com.LoLCompanionApp;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class About extends Activity {
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);

		initializeHeader();

		// add text to about page
		TextView about = (TextView) findViewById(R.id.textAbout);
		about.setText("League of Legends Companion App\n\n"
				+ "Created by: \n"
				+ "Victor Vucicevich & Michaela Farova\n"
				+ "\nAll images and information is Copyright\u00A9 Riot Games. "
				+ "All guides linked in this app are Copyright\u00A9 their respective authors."
				+ "All coding is Copyright\u00A9 the creators of this app."
				+ "\n\nA special thanks to:\n" + "Shane Wilton\n"
				+ "Adam Wooton\n" + "Matus Faro\n"
				+ "reddit.com/r/leagueoflegends");
	}

	public void initializeHeader() {
		TextView title = (TextView) findViewById(R.id.HeaderTitle);
		title.setText("About");
	}

	public void back(View view) {
		finish();
	}

}
