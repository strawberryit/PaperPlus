package pe.straw.paperplus.app;

import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;
import pe.straw.paperplus.PaperLogger;
import pe.straw.paperplus.PrefService;

public class KyoboEbook {
	public static final String packageName = "com.kyobo.ebook.common.b2c";
	
	static XSharedPreferences pref = PrefService.getInstance();
	static final String prefKey = "kyobo_ebook";

	public static void modCode(LoadPackageParam lparam){
		PaperLogger.log("KyoboEbook");
		if (pref.getBoolean(prefKey, false) == false)
			return;
		
		PaperLogger.log("KyoboEbook module is active");
		KyoboPlatform.modCode(lparam, packageName);
	}
}
