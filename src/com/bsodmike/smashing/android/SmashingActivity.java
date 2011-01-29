package com.bsodmike.smashing.android;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class SmashingActivity extends Activity {
	private ArrayAdapter<String> adapter_items;
    private List<String> file_list = new ArrayList<String>();
    private ListView lv;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        lv = (ListView)findViewById(R.id.ListView);       
	    lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View v,
					int pos, long id) {
				// TODO Auto-generated method stub				
			}
	    });
	    
		/*
		 * @todo: initialise listview
		 */
        adapter_items = new ArrayAdapter<String>(this,
                R.layout.file_item, file_list);
		lv.setAdapter(adapter_items); 
    }
}

