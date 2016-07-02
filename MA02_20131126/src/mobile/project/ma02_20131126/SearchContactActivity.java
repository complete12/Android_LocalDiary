/*최종 작성일 : 2015-12-16
 * 작성자 : 정혜인
 * 설명 : DB의 데이터 검색 액티비티		옵션메뉴로 선택된 데이터의 내용 공유 가능*/
package mobile.project.ma02_20131126;

import java.util.ArrayList;

import android.R.menu;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class SearchContactActivity extends Activity {

	ContactDBHelper helper;
	EditText etSearchName;
	ListView ResultList;
	ArrayList<String> strlist;
	ArrayAdapter<String> aa;
	String strContent;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_contact);
		
		helper = new ContactDBHelper(this);
		etSearchName = (EditText)findViewById(R.id.etSearchName);
		ResultList = (ListView)findViewById(R.id.lvSearch);
		strlist = new ArrayList<String>();
		aa = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_single_choice, strlist); //라디오버튼으로 선택가능
		ResultList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		ResultList.setItemsCanFocus(false);
		ResultList.setAdapter(aa);
		ResultList.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int pos,
					long id) {
				// TODO Auto-generated method stub
				 strContent = parent.getItemAtPosition(pos).toString();
				 
				 
					Toast.makeText(SearchContactActivity.this, strContent, Toast.LENGTH_SHORT).show();//토스트
			}
			
		});
	}
	
	
	
	 @Override
	    public boolean onCreateOptionsMenu(Menu menu) {
		 //옵션메뉴 생성
		 MenuItem item1 = menu.add(0, 1, 100, "공유");
	        menu.add(0,2,200, "개발자 정보");
	        
	        return true;
	    }
	 
	    @Override
	    public boolean onOptionsItemSelected(MenuItem item) {
	        // Handle action bar item clicks here. The action bar will
	        // automatically handle clicks on the Home/Up button, so long
	        // as you specify a parent activity in AndroidManifest.xml.
	        int id = item.getItemId();
	       switch (id) {
	       case 1://공유		인텐트로 공유 액티비티 띄움.
	    	   Intent shared = new Intent(Intent.ACTION_SEND)
	    		.addCategory(Intent.CATEGORY_DEFAULT)
	    		.setType("text/plain")
	    		.putExtra(Intent.EXTRA_SUBJECT, "주제")
	    		.putExtra(Intent.EXTRA_TITLE, "제목")
	    		.putExtra(Intent.EXTRA_TEXT, this.strContent);
	    	   this.startActivity(Intent.createChooser(shared, "공유"));
	    	   return true;
		case 2://개발자정보	액티비티 전환
			this.startActivity(new Intent(SearchContactActivity.this, InfoActivity.class));
	    	   return true;
	    	   default:
	    		   return super.onOptionsItemSelected(item); 
	        } 
	    }
	
	
	
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.btnSearchContact:
			strlist.removeAll(strlist);
			aa.notifyDataSetChanged();
			
			String searchName = etSearchName.getText().toString();
			
//			전용 메소드를 사용할 경우
//			query 실행 준비
			SQLiteDatabase db = helper.getReadableDatabase();
			DataManager myManager = new DataManager(helper, null, db);

//			설정한 정보를 사용하여 query 실행
			Cursor cursor =
					myManager.searchContact(searchName);
			
//			검색 결과가 있는지 cursor가 갖고 있는 row 갯수 확인. 0일 경우 결과 없음
			if (cursor.getCount() != 0) {
	//			query 결과를 Dto 객체에 저장
				ContactDto item = new ContactDto();
				int i = 0;
				while(cursor.moveToNext()) {
					i++;
					item.setId(cursor.getInt(0));
					item.setName(cursor.getString(1)); 
					item.setMemo(cursor.getString(2));
					item.setInfo(cursor.getString(3));
					Log.i("data", "selected query: " + item.getName());
					//			출력 및 DB 관련 객체 close
					strlist.add( item.getName()+ "\n" + item.getInfo());
				}
				aa.notifyDataSetChanged();

			} else {
				Toast.makeText(SearchContactActivity.this,"검색 결과가 없습니다.\n", Toast.LENGTH_SHORT).show();
			}
				
			cursor.close();
			helper.close();
			
			break;
		case R.id.btnClose:
			finish();
			break;
		}
	}
	
	
	
}
