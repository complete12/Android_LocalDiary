/*최종 작성일 : 2015-12-17
 * 작성자 : 정혜인
 * 설명 : 즐겨찾기 목록 가져오는 액티비티*/
package mobile.project.ma02_20131126;

import java.util.ArrayList;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class FavoriteActivity extends Activity {

	ContactDBHelper helper;
	ListView ResultList;
	ArrayList<String> strlist;
	ArrayAdapter<String> aa;
	String strContent;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_favorite);
		
		ResultList = (ListView)findViewById(R.id.lvFav);
		strlist = new ArrayList<String>();
		aa = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, strlist);

		helper = new ContactDBHelper(this);
		SQLiteDatabase db = helper.getReadableDatabase();
		DataManager myManager = new DataManager(helper, null, db);

//		설정한 정보를 사용하여 query 실행
		Cursor cursor =
				myManager.getFavoriteContacts();
		
//		검색 결과가 있는지 cursor가 갖고 있는 row 갯수 확인. 0일 경우 결과 없음
		if (cursor.getCount() != 0) {
//			query 결과를 Dto 객체에 저장
			ContactDto item = new ContactDto();
			int i = 0;
			while(cursor.moveToNext()) {
				i++;
				item.setId(cursor.getInt(0));
				item.setName(cursor.getString(1)); 
				Log.i("data", "selected query: " + item.getName());
				//			출력 및 DB 관련 객체 close
				strlist.add( item.getId() + ". " + item.getName());
			}
			aa.notifyDataSetChanged();

		} else {
			Toast.makeText(FavoriteActivity.this,"검색 결과가 없습니다.\n", Toast.LENGTH_SHORT).show();
		}
			
		cursor.close();
		helper.close();
		
		
		
		ResultList.setAdapter(aa);
		ResultList.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int pos,
					long id) {
				// TODO Auto-generated method stub
				 strContent = parent.getItemAtPosition(pos).toString();
				 
				 
					Toast.makeText(FavoriteActivity.this, strContent, Toast.LENGTH_SHORT).show();//토스트
			}
			
		});
	}

}
