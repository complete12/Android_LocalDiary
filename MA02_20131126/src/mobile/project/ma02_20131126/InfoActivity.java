/*최종 작성일 : 2015-12-16
 * 작성자 : 정혜인
 * 설명 : 개발자 정보 액티비티*/
package mobile.project.ma02_20131126;

import android.app.Activity;
import android.os.Bundle;
import android.provider.MediaStore.Images;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class InfoActivity extends Activity {

	TextView t1, t2;
	ImageView img1;
	Button btn1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_info);
		
		t1 = (TextView)findViewById(R.id.infoName);
		t2 = (TextView)findViewById(R.id.infoUniv);
		img1 = (ImageView)findViewById(R.id.infoImg);
		btn1 = (Button)findViewById(R.id.infoCancel);
	}
	
	public void onClick(View v){
		if(v.getId() == R.id.infoCancel)
			finish();
	}
}
