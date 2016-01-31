package pe.straw.paperplus;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;
import pe.straw.paperplus.app.BookcubeLibrary;
import pe.straw.paperplus.app.EpyrusLibrary;
import pe.straw.paperplus.app.KyoboEbook;
import pe.straw.paperplus.app.KyoboLibrary;
import pe.straw.paperplus.app.Yes24Ebook;
import pe.straw.paperplus.app.Yes24Library;

public class PaperService implements IXposedHookLoadPackage{

	@Override
	public void handleLoadPackage(LoadPackageParam lpparam) throws Throwable {
		
		if (lpparam.packageName != null){
			switch(lpparam.packageName){
				case KyoboLibrary.packageName:
					KyoboLibrary.modCode(lpparam); break;
				case EpyrusLibrary.packageName:
					EpyrusLibrary.modCode(lpparam); break;
				case Yes24Library.packageName:
					Yes24Library.modCode(lpparam); break;
				case BookcubeLibrary.packageName:
					BookcubeLibrary.modCode(lpparam); break;
					
				case KyoboEbook.packageName:
					KyoboEbook.modCode(lpparam); break;
				case Yes24Ebook.packageName:
					Yes24Ebook.modCode(lpparam); break;
			}
		}
		
		if (lpparam.packageName.equals(PaperLogger.PACKAGE_NAME)){
			String className = PaperLogger.PACKAGE_NAME + ".PrefFragment";
			XposedHelpers.findAndHookMethod(className, lpparam.classLoader, "isModuleActive", XC_MethodReplacement.returnConstant(true));
		}
	}
}