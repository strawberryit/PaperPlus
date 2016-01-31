package pe.straw.paperplus;

import android.app.Activity;
import android.os.Bundle;

public class PaperActivity extends Activity {
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		getActionBar().setIcon(R.drawable.ic_launcher_white);
		getFragmentManager().beginTransaction()
				.replace(android.R.id.content, new PrefFragment())
				.commit();
	}
}