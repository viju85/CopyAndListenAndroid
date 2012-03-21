package com.vijay.copylisten;

import java.util.Locale;

import android.app.Activity;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.text.ClipboardManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class CopyListenActivity extends Activity implements TextToSpeech.OnInitListener {
    private static final String TAG = "CopyListenActivity";

    private TextToSpeech mTts;
    private String strToSpeak;
    private TextView mTv1;
    private Button mBtnSpeak;
    private Button mBtnStop;
    
    
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        mTts = new TextToSpeech(this,
                this  // TextToSpeech.OnInitListener
                );
        
        mTv1 = (TextView) findViewById(R.id.textView1);
        mBtnSpeak = (Button) findViewById(R.id.btn_speak);
        mBtnStop = (Button) findViewById(R.id.btn_stop);
        
        mBtnSpeak.setOnClickListener(mSpeakButtonListener);
        mBtnStop.setOnClickListener(mStopButtonListener);
        
        
        performSpeak();
        
    }
    
    
    
    
    OnClickListener mSpeakButtonListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			Log.d(TAG, "Calling sayHello from SpeakButtonClick");
        	if(null != strToSpeak) {
        		sayHello(strToSpeak);
        	} else {
        		sayHello("Nothing to Speak");
        		mTv1.setText("Nothing to Speak.");
        	}
			
		}
	};
	
	OnClickListener mStopButtonListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			Log.d(TAG, "Stop Speaking");
			if (mTts != null) {
	            mTts.stop();
	        }
			
		}
	};
    
    private void performSpeak() {
    	android.util.Log.d("VIJAY", "Perform Speak +");
    	mTv1.setText("Checking Clipboard...");
    	ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);

    	android.util.Log.d(TAG,"clipboardManager.hasText():" + clipboardManager.hasText());
    	
    	if (clipboardManager.hasText()) {
	    	//Toast.makeText(getApplicationContext(),"Clipboard Text = "+ clipboardManager.getText().toString(), Toast.LENGTH_LONG).show();
	    	//clipboardManager.setText(null);
	    	
	    	strToSpeak = clipboardManager.getText().toString();
	    	//Log.d(TAG, "Calling sayHello 1");
	    	//sayHello();
    	} else {
	    	Toast.makeText(getApplicationContext(), "No Content in Clipboard. So set some text in clipboard", Toast.LENGTH_LONG).show();
	    	//clipboardManager.setText("AndroidPeople.com");
	    	//Toast.makeText(getApplicationContext(), "Clipboard Text = " + clipboardManager.getText().toString(), Toast.LENGTH_LONG).show();
    	}
    	android.util.Log.d("VIJAY", "Perform Speak -");
    	

    }
    
    @Override
    public void onResume() {
    	performSpeak();
    	
        super.onResume();
    }
    

    @Override
    public void onPause() {
        // Don't forget to shutdown!
    	super.onPause();
        //finish();
        
    }

    @Override
    public void onDestroy() {
        // Don't forget to shutdown!
        if (mTts != null) {
            mTts.stop();
            mTts.shutdown();
        }

        super.onDestroy();
    }

    // Implements TextToSpeech.OnInitListener.
    public void onInit(int status) {
        // status can be either TextToSpeech.SUCCESS or TextToSpeech.ERROR.
        if (status == TextToSpeech.SUCCESS) {
            // Set preferred language to US english.
            // Note that a language may not be available, and the result will indicate this.
            int result = mTts.setLanguage(Locale.getDefault());
            // Try this someday for some interesting results.
            // int result mTts.setLanguage(Locale.FRANCE);
            if (result == TextToSpeech.LANG_MISSING_DATA ||
                result == TextToSpeech.LANG_NOT_SUPPORTED) {
               // Lanuage data is missing or the language is not supported.
                Log.e(TAG, "Language is not available.");
                mTv1.setText("Language is not available.");
            } else {
                // Check the documentation for other possible result codes.
                // For example, the language may be available for the locale,
                // but not for the specified country and variant.

                // Greet the user.
            	Log.d(TAG, "Calling sayHello 2");
            	if(null != strToSpeak) {
            		sayHello(strToSpeak);
            	} else {
            		sayHello("Nothing to Speak");
            		mTv1.setText("Nothing to Speak.");
            	}
            }
        } else {
            // Initialization failed.
            Log.d(TAG, "Could not initialize TextToSpeech.");
            mTv1.setText("Could not initialize TextToSpeech.");
        }
    }
   
    private void sayHello(String str) {
    	android.util.Log.d("VIJAY", "Reading from Clipboard +");
    	android.util.Log.d("VIJAY", "Text:" + str);
    	mTv1.setText("All Good. Speaking From Clipboard.");
    	Toast.makeText(getApplicationContext(),"Clipboard Text = "+ str, Toast.LENGTH_LONG).show();
        mTts.speak(str,
            TextToSpeech.QUEUE_FLUSH,  // Drop all pending entries in the playback queue.
            null);
        android.util.Log.d("VIJAY", "Reading from Clipboard -");
    }
}