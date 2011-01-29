package com.bsodmike.smashing.android;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class SmashingActivity<MyActivity> extends Activity {
    private static final String SD_PATH = "/sdcard";
    private File curPath = new File(SD_PATH);
	protected static final int REQ_CODE = 0;    
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
				
				if (chosenF.equals(".")) {
					//reload same path
					browse(curPath);
				} else if(chosenF.equals("..")){
					//browse one level up
					browseUpOne();
				} else {
					File clickedF = null;
					
					// absolute path
					clickedF = new File(chosenF);
					Log.d("onItemClick", chosenF);

					if(clickedF != null){
						if (clickedF.isHidden()){
							updateUI(clickedF.getName() + " is a hidden file!");
						} else if (!clickedF.canRead()) { 
							updateUI(clickedF.getName() + " cannot be read!");							
						} else {
							browse(clickedF);
						}
					}
				}
			}
	    });
	    init(SD_PATH);
    }    

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        // See which child activity is calling us back.
        switch (resultCode) {
            case REQ_CODE:
                // This is the standard resultCode that is sent back if the
                // activity crashed or didn't doesn't supply an explicit result.
            	init(SD_PATH);
            default:
                break;
        }
    }
    
    public void updateUI(String text){
    	Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }
    
    public void init(String path){
    	browse(new File(path));
    }
    
    public void browse(File path) {
    	Log.d("browse","browsing to " + path);
		curPath = path;
    	if (path.isDirectory()){
    		files_array(path.listFiles());
    	}else{
    		Log.d("browse","Creating AlertDialog.Builder: " + curPath.getPath());
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("Do you want to open " + curPath.getAbsolutePath() + "?")
			       .setCancelable(false)
			       .setPositiveButton("OK", new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
							try {
								// Lets start an intent to View the file, that was clicked...
								Intent fileOpenIntent = 
									new Intent(android.content.Intent.ACTION_VIEW)
										.setDataAndType(Uri.fromFile(curPath), "text/plain")
										.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
								startActivityForResult(fileOpenIntent, REQ_CODE);
							} catch (Exception e) {
								e.printStackTrace();
							}			        	   
			           }
			       })
			       .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
			                dialog.cancel();
			           }
			       }).create().show();
		}
    }

	private void browseUpOne(){
		if(curPath.getParent() != null)
			browse(curPath.getParentFile());
	}
    
    private void files_array(File[] files) {
    	file_list.clear();
    	
    	// add ".", i.e. refresh current directory
    	file_list.add(".");
    	
    	// add  ".." == 'Up one level'
    	if (!curPath.getAbsolutePath().equalsIgnoreCase(SD_PATH)){
    		if(curPath.getParent() != null){
    			Log.d("files_array", curPath.getAbsolutePath());
	    		file_list.add("..");
	    	}
    	}
    	
		try {
			// absolute path
			for (File f : files){
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
    }    
    
}

