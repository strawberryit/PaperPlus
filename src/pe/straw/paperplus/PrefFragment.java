package pe.straw.paperplus;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

public class PrefFragment extends PreferenceFragment{
    public static final String ARG_SIDEMENU_NUMBER = "sidemenu_number";

    @SuppressWarnings("deprecation")
	@Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	
    	PreferenceManager prefManager = getPreferenceManager();
    	prefManager.setSharedPreferencesMode(PreferenceActivity.MODE_WORLD_READABLE);
    	addPreferencesFromResource(R.xml.pref_application);
    	if (isModuleActive()){
			PaperLogger.log("PaperPlus Activated");
    		Preference pref = findPreference("isModuleNotActive");
    		getPreferenceScreen().removePreference(pref);
    	}
    }
    
	public static boolean isModuleActive(){
		return false;
	}
}