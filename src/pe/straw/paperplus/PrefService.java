package pe.straw.paperplus;

import de.robv.android.xposed.XSharedPreferences;

public class PrefService {
	static XSharedPreferences pref = null;
	
	public static XSharedPreferences getInstance(){
		if (pref == null){
			pref = new XSharedPreferences("pe.straw.paperplus");
			pref.makeWorldReadable();
		} else {
			pref.reload();
		}
		
		return pref;
	}

}
