package com.LoLCompanionApp;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

public class JungleEdit extends Activity {

	public void initializeHeader() {
		TextView title = (TextView) findViewById(R.id.HeaderTitle);
		title.setText("Jungle Timer");
	}

	public void back(View view) {
		finish();
	}

}
