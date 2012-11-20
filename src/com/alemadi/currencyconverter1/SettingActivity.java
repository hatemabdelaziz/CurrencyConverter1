package com.alemadi.currencyconverter1;

import android.os.Bundle;
import android.app.Activity;
import android.app.ListActivity;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class SettingActivity extends Activity {

	private ListView lView;
	private String lv_items[] = { "Android", "iPhone", "BlackBerry",
	"AndroidPeople", "J2ME", "Listview", "ArrayAdapter", "ListItem",
	"Us", "UK", "India" };

	@Override
	public void onCreate(Bundle icicle) {
	super.onCreate(icicle);
	setContentView(R.layout.activity_setting);
	lView = (ListView) findViewById(R.id.ListView01);
	// Set option as Multiple Choice. So that user can able to select more the one option from list
	lView.setAdapter(new ArrayAdapter<String>(this,
	android.R.layout.simple_list_item_multiple_choice, lv_items));
	lView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_setting, menu);
		return true;
	}

}
