package cs125.noteninjas;

import java.io.File;
import java.io.IOException;

import org.puredata.android.service.PdService;
import org.puredata.core.PdBase;
import org.puredata.core.utils.IoUtils;
import org.puredata.core.utils.PdDispatcher;
import org.puredata.core.utils.PdListener;

import com.lamerman.FileDialog;
import com.lamerman.SelectionMode;

import cs125.noteninjas.Power;
import cs125.noteninjas.R;
import cs125.noteninjas.R.drawable;
import cs125.noteninjas.R.id;
import cs125.noteninjas.R.layout;
import cs125.noteninjas.R.raw;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioGroup;


public class NoteNinjasActivity extends Activity {
	private String TAG = "NoteNinjas";
	private int REQUEST_SAVE = 0;


	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG,"Starting application.");
		setContentView(R.layout.menu);
		
		final Button openButton = (Button) findViewById(R.id.OPEN_FILE_BUTTON);
        openButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	openFileDialog();
            } 
        });
	}

	protected void openFileDialog(){
		Intent intent = new Intent(this.getBaseContext(),
                FileDialog.class);
		intent.putExtra(FileDialog.START_PATH, "/mnt/sdcard");
		intent.putExtra(FileDialog.SELECTION_MODE, SelectionMode.MODE_OPEN);
		this.startActivityForResult(intent, REQUEST_SAVE);
	}
	
    public synchronized void onActivityResult(final int requestCode,
            int resultCode, final Intent data) {

            if (resultCode == Activity.RESULT_OK) {

                    if (requestCode == REQUEST_SAVE) {
                            System.out.println("Saving...");
                    }
                    
                    String filePath = data.getStringExtra(FileDialog.RESULT_PATH);
            		Intent intent = new Intent(this.getBaseContext(),
                            AndroidActivity.class);
            		intent.putExtra("filename", filePath);
            		this.startActivity(intent);
            		
            } else if (resultCode == Activity.RESULT_CANCELED) {
                    Log.w(TAG, "file not selected");
            }
    }	
}
