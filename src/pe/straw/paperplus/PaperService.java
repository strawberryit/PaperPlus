package pe.straw.paperplus;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;
import pe.straw.paperplus.app.BookcubeLibrary;
import pe.straw.paperplus.app.EpyrusLibrary;
import pe.straw.paperplus.app.KakaoPage;
import pe.straw.paperplus.app.KyoboEbook;
import pe.straw.paperplus.app.KyoboLibrary;
import pe.straw.paperplus.app.Mekia;
import pe.straw.paperplus.app.Yes24Ebook;
import pe.straw.paperplus.app.Yes24Library;

public class PaperService implements IXposedHookZygoteInit, IXposedHookLoadPackage{

	public static String MODULE_PATH;
	@Override
	public void initZygote(StartupParam startupParam) throws Throwable {
		MODULE_PATH = startupParam.modulePath;	
	}
	
	@Override
	public void handleLoadPackage(LoadPackageParam lpparam) throws Throwable {
		
		/*
		if (lpparam.packageName.equals("com.ridi.paper")){
			XposedHelpers.findAndHookMethod(
					"com.initialcoms.ridi.common.activity.WebViewActivity",
					lpparam.classLoader,
					"onCreate",
					Bundle.class,
					new XC_MethodHook() {
						@Override
						protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
							Activity activity = (Activity) param.thisObject;
							String url = activity.getIntent().getStringExtra("url");
							Log.d("PaperPlus", url);
							if (url != null && url.contains("generate_204"))
								activity.finish();
						}
					});
		}
		*/
		
		if (lpparam.packageName != null){
			switch(lpparam.packageName){
				case KyoboLibrary.packageName:
					KyoboLibrary.modCode(lpparam); break;
				case EpyrusLibrary.packageName:
					EpyrusLibrary.modCode(lpparam); break;
				case Yes24Library.packageName:
				case Yes24Library.packagename2:
					Yes24Library.modCode(lpparam); break;
				case BookcubeLibrary.packageName:
				case BookcubeLibrary.packageName2:
					BookcubeLibrary.modCode(lpparam); break;
				case Mekia.packageName:
					Mekia.modCode(lpparam); break;
					
				case KyoboEbook.packageName:
					KyoboEbook.modCode(lpparam); break;
				case Yes24Ebook.packageName:
					Yes24Ebook.modCode(lpparam); break;
				case KakaoPage.packageName:
					KakaoPage.modCode(lpparam); break;
			}
		}
		
		if (lpparam.packageName.equals(PaperLogger.PACKAGE_NAME)){
			String className = PaperLogger.PACKAGE_NAME + ".PrefFragment";
			XposedHelpers.findAndHookMethod(className, lpparam.classLoader, "isModuleActive", XC_MethodReplacement.returnConstant(true));
		}
	}

	/*
	@Override
	public void handleInitPackageResources(InitPackageResourcesParam resparam){
		if (resparam.packageName != null){
			switch(resparam.packageName){
			case Mekia.packageName:
				Mekia.modRes(resparam); break;
			}
		}
	}*/



}