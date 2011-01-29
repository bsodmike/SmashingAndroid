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
    private static final String SD_PATH = "/sdcard";
    private File curPath = new File(SD_PATH);
	private ArrayAdapter<String> adapter_items;
    private List<String> file_list = new ArrayList<String>();
    private ListView lv;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        adapter_items = new ArrayAdapter<String>(this,
                R.layout.file_item, file_list);
        
        lv = (ListView)findViewById(R.id.ListView);       
	    lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View v,
					int pos, long id) {
				// TODO Auto-generated method stub
				String chosenF = file_list.get(pos);
				Log.d("onItemClick","pos: "+ pos);
	
				File clickedF = null;
				
				// absolute path
				clickedF = new File(chosenF);
				Log.d("onItemClick",chosenF);

				if(clickedF != null)
					browse(clickedF);
				
			}
	    });
	    init(SD_PATH);
    }    
    
    public void updateUI(String file_path){
    	Toast.makeText(this, file_path, Toast.LENGTH_LONG).show();
    }
    
    public void init(String path){
    	browse(new File(path));
    }
    
    public void browse(File path) {
    	Log.d("browse","browsing to " + path);
    	if (path.isDirectory()){
    		curPath = path;
    		
        	file_list.clear();
    		
    		// absolute path
    		try {
    			for (File f : path.listFiles()){
    				file_list.add(f.getPath());
    				Log.d("files_array","adding: "+ f.getPath());
    			}
    		} catch (NullPointerException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
    		
        	adapter_items = new ArrayAdapter<String>(this,
        	                R.layout.file_item, file_list);
        	
        	lv.setAdapter(adapter_items);		
    		
    		
    	}else{
    		/*
    		 * shouldn't we do something more fancy when a file is clicked?
    		 */
    		updateUI("File: "+ path.getName());
    	}
    }
    
}

