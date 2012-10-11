package com.LoLCompanionApp;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

public class Main extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
	}

	public void championList(View button) {
		Intent Page2 = new Intent();
		Page2.setClassName("com.LoLCompanionApp", "com.LoLCompanionApp.ChampionList");
		startActivity(Page2);
	}

	public void about(View button) {
		Intent Page2 = new Intent();
		Page2.setClassName("com.LoLCompanionApp", "com.LoLCompanionApp.About");
		startActivity(Page2);
	}

	public void patches(View button) {
		Intent browserIntent = new Intent(
				Intent.ACTION_VIEW,
				Uri.parse("http://na.leagueoflegends.com/news/release-notes"));
		startActivity(browserIntent);
	}

	public void otherFunctions(View button) {
		Intent Page2 = new Intent();
		Page2.setClassName("com.LoLCompanionApp", "com.LoLCompanionApp.MainOther");
		startActivity(Page2);
	}
}