/*최종 작성일 : 2015-12-16
 * 작성자 : 정혜인
 * 설명 : 처음화면 액티비티		새로 입력, 저장 목록, 목록 검색*/
package mobile.project.ma02_20131126;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}
	
	public void onClick(View v) {
		Intent intent = null;
		
		switch (v.getId()) {
		case R.id.btnOpenAllContact:
			intent = new Intent(this, AllContactsActivity.class);
			break;
		case R.id.btnAddNewContact:
			intent = new Intent(this, InsertContactActivity.class);
			break;
		case R.id.btnSearchContact:
			intent = new Intent(this, SearchContactActivity.class);
			break;
		case R.id.btnFavorite:
			intent = new Intent(this, FavoriteActivity.class);
		}
		
		if (intent != null) startActivity(intent);
	}
	
}
