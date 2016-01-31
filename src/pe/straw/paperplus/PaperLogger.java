package pe.straw.paperplus;

import android.util.Log;

public class PaperLogger {
	private static final boolean DEBUG = true;
	private static final String TAG = "PaperPlus";
	
	public static final String PACKAGE_NAME = "pe.straw.paperplus";
	
	public static void log(String msg){
		if (DEBUG)
			Log.d(TAG, msg);
	}

	public static void log(int value){
		if (DEBUG)
			Log.d(TAG, Integer.toString(value));
	}

	public static void log(boolean value){
		if (DEBUG)
			Log.d(TAG, Boolean.toString(value));
	}
}
