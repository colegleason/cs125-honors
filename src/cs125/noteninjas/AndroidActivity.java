package cs125.noteninjas;

import java.io.File;
import java.io.IOException;

import org.puredata.android.service.PdService;
import org.puredata.core.PdBase;
import org.puredata.core.utils.IoUtils;
import org.puredata.core.utils.PdDispatcher;
import org.puredata.core.utils.PdListener;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.ProgressBar;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.awesome.Home;

public class AndroidActivity extends AndroidApplication {
	private PdService pd;
	/* synchronize on this lock whenever you access pdService */
	private final Object lock = new Object();
	/* this is where we'll save the handle of the Pd patch */
	int patch = 0;
	
	/* Activity callback */
	private int REQUEST_SAVE = 0;
	
	/* Constants */
	private String TAG = "NoteNinjas";
	private int SAMPLE_RATE = 44100;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		initPdService(this);
		Bundle extras = this.getIntent().getExtras();
		String filePath = extras.getString("filename");
		playSong(filePath);
        initialize(new Home(), false);
    }
    
    @Override
    public void onDestroy() {
    	super.onDestroy();
    	finish();
    }
    
    // PdService stuff:

	
 	private final ServiceConnection serviceConnection = new ServiceConnection() {  
 		  /* This gets called when our service is bound and sets up */  
 		  @Override  
 		  public void onServiceConnected(ComponentName name, IBinder service) {  
 		    synchronized(lock) {  
 		    	Log.d(TAG, "connecting service");
 		      pd = ((PdService.PdBinder)service).getService();  
 		      initPd();
 		    }  
 		  }

 		  @Override  
 		  public void onServiceDisconnected(ComponentName name) {  
 		    /* this method will never be called */  
 		  }  
 		}; 
 		
 	/* actually bind the service, which triggers the code above;  
 	   this is the method you should call to launch Pd */  
 	private void initPdService(Context context) {  
 	  /* a separate thread is not strictly necessary,  
 	     but it improves responsiveness */  
 	  new Thread() {  
 	    @Override  
 	    public void run() {  
 	    	Log.d(TAG, "Binding PdService...");
 	      bindService(new Intent(AndroidActivity.this, PdService.class),  
 	             serviceConnection, BIND_AUTO_CREATE);  
 	    }  
 	  }.start();  
 	}

 	/* this is how we initialize Pd */  
 	private void initPd() {
 	Log.d(TAG, "initializing Pd");
 	  /* here is where we bind the print statement catcher defined below */  
 	  PdBase.setReceiver(myDispatcher);  
 	  myDispatcher.addListener("bass", bassListener);  
 	  myDispatcher.addListener("mid", midListener);  
 	  myDispatcher.addListener("treble", trebleListener);  
 	  startAudio();  
 	}
 	
 	/* We'll use this to catch print statements from Pd
 	   when the user has a [print] object */  
 	private final PdDispatcher myDispatcher = new PdDispatcher() {  
 	  @Override  
 	  public void print(String s) {  
 	    Log.i(TAG, s);  
 	  }  
 	};
 	  
 	private final PdListener bassListener = new PdListener() {  
 	  @Override  
 	  public void receiveMessage(String symbol, Object... args) {  
 	    Log.i(TAG, symbol);
 	    for (Object arg: args) {  
 	      Log.i(TAG, arg.toString());
 	    }  
 	  }

 	  @Override  
 	  public void receiveList(Object... args) {  
 	    for (Object arg: args) {  
 	      Log.i(TAG, arg.toString());  
 	    }  
 	  }
  
 	  @Override public void receiveSymbol(String symbol) {  
 	    Log.i(TAG, symbol);  
 	  }  
 	  @Override public void receiveFloat(float x) {  
 	    //TODO
 	  }  

 	  @Override public void receiveBang() {  
 	    Log.i(TAG, "bang!");  
 	  }  
 	  
 	};
 	private final PdListener trebleListener = new PdListener() {  
 		  @Override  
 		  public void receiveMessage(String symbol, Object... args) {  
 		    Log.i(TAG, symbol);
 		    for (Object arg: args) {  
 		      Log.i(TAG, arg.toString());
 		    }  
 		  }

 		  @Override  
 		  public void receiveList(Object... args) {  
 		    for (Object arg: args) {  
 		      Log.i(TAG, arg.toString());  
 		    }  
 		  }
 	 
 		  @Override public void receiveSymbol(String symbol) {  
 		    Log.i(TAG, symbol);  
 		  }  
 		  @Override public void receiveFloat(float x) {  
 		    //TODO
 		  }  

 		  @Override public void receiveBang() {  
 		    Log.i(TAG, "bang!");  
 		  }  
 		  
 		};
 		private final PdListener midListener = new PdListener() {  
 			  @Override  
 			  public void receiveMessage(String symbol, Object... args) {  
 			    Log.i(TAG, symbol);
 			    for (Object arg: args) {  
 			      Log.i(TAG, arg.toString());
 			    }  
 			  }

 			  @Override  
 			  public void receiveList(Object... args) {  
 			    for (Object arg: args) {  
 			      Log.i(TAG, arg.toString());  
 			    }  
 			  }
 		 
 			  @Override public void receiveSymbol(String symbol) {  
 			    Log.i(TAG, symbol);  
 			  }  
 			  @Override public void receiveFloat(float x) {  
 			    //TODO
 			  }  

 			  @Override public void receiveBang() {  
 			    Log.i(TAG, "bang!");  
 			  }  
 			  
 			};

 	private void startAudio() {  
 	  synchronized (lock) {  
 		  Log.d(TAG, "starting audio with patch: "+patch);
 	    if (pd == null) return;  
 	    if (!initAudio(2, 2) && !initAudio(1, 2)) {
 	      if (!initAudio(0, 2)) {  
 	        Log.e(TAG, "Unable to initialize audio interface");  
 	        finish();  
 	        return;  
 	      } else {  
 	        Log.w(TAG, "No audio input available");  
 	      }  
 	    }  
 	    if (patch == 0) {  
 	      try {  
 	        /* sleep for one second to give Pd a chance to load samples and such;  
 	           this is not always necessary, but not doing this may give rise to  
 	           obscure glitches when the patch contains audio files */
 	 		unpackAndOpenPatch(cs125.noteninjas.R.raw.patch, "noteninja.pd");
 	        Thread.sleep(3000);  
 	      } catch (InterruptedException e) {  
 	        Log.e(TAG, e.toString());
 	    }  
 	    pd.startAudio(new Intent(AndroidActivity.this, AndroidActivity.class),  
 	         R.drawable.notification_icon, "Pure Data", "Return to Pure Data.");  
 	  }  
 	  }
 	}

 	/* helper method for startAudio();  
 	   try to initialize Pd audio for the given number of input/output channels,  
 	   return true on success */  
 	private boolean initAudio(int nIn, int nOut) {  
 	  try {  
 	    pd.initAudio(SAMPLE_RATE, nIn, nOut, -1);  
 	    /* negative values default to PdService preferences */  
 	  } catch (IOException e) {  
 	    Log.e(TAG, e.toString());  
 	    return false;  
 	  }  
 	  return true;  
 	}

 	private void stopAudio() {  
 	  synchronized (lock) {  
 	    if (pd == null) return;  
 	    /* consider ramping down the volume here to avoid clicks */  
 	    pd.stopAudio();  
 	  }  
 	}
 	
      public void finish() {
     	 pd.release();
      }
 	
 	public void unpackAndOpenPatch(int zipId, String patchname) {
 		try {  
 			IoUtils.extractZipResource(getResources().openRawResource(zipId), getFilesDir(), true); 
 			PdBase.addToSearchPath(getFilesDir().toString());
 			File patch_f = new File(getFilesDir(), patchname);
 			patch = PdBase.openPatch(patch_f);
 		} catch (IOException e) {  
 		  Log.e(TAG, e.toString());  
 		}   
 	}
 	
 	public void playSong(String filename) {
 		Log.d(TAG, "Playing file:" + filename);
 		startAudio();
 		PdBase.sendSymbol("filename", filename);
 		PdBase.sendBang("start");
 	}
 	
 	public void stop() {
 		PdBase.sendBang("stop");
 		stopAudio();
 	}
}