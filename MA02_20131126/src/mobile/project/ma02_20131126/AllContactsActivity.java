/*최종 작성일 : 2015-12-16
 * 작성자 : 정혜인
 * 설명 : 저장된 전체목록 액티비티*/
package mobile.project.ma02_20131126;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class AllContactsActivity extends Activity {
	
	ListView lvContacts = null;
	ContactDBHelper helper;
	DataManager myManager;

	
	MyCursorAdapter myAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_all_contacts);
		lvContacts = (ListView)findViewById(R.id.lvContacts);
		helper = new ContactDBHelper(this);
		
//		Custom Adapter로 작성한 MyCursorAdapter를 사용
		myAdapter = new MyCursorAdapter(this, R.layout.listview_item, null);
		
		lvContacts.setAdapter(myAdapter);
		
		lvContacts.setOnItemClickListener(itemClickListener);
		lvContacts.setOnItemLongClickListener(itemLongClickListener);
	}

	@Override
	protected void onResume() {
		super.onResume();
		SQLiteDatabase db = helper.getReadableDatabase();
		myManager = new DataManager(helper, myAdapter, db);
		myManager.getAllContacts(myAdapter);
		
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
//		현재 어댑터에 설정한 cursor를 activity 종료 시 close

//		Custom Adapter로 작성한 MyCursorAdapter를 사용할 경우		
		if (!myAdapter.getCursor().isClosed()) myAdapter.getCursor().close(); 
	}
	
	
//	클릭 시 수정을 위한 Activity 호출 - 현재 클릭한 항목에 대한 id를 전달
	AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
			Intent intent = new Intent(AllContactsActivity.this, UpdateContactActivity.class);
//			수정 항목 레코드에 해당하는 id를 전달
			intent.putExtra("id", id);
			startActivity(intent);
		} 
		
	};
	
	
	AdapterView.OnItemLongClickListener itemLongClickListener = new AdapterView.OnItemLongClickListener() {

		@Override
		public boolean onItemLongClick(AdapterView<?> parent, View view, int pos, long id) {
			
			final long targetId = id;
			
			//dialog로 삭제확인
			AlertDialog.Builder builder = new AlertDialog.Builder(AllContactsActivity.this);
			
			builder.setTitle("삭제 확인");
			builder.setMessage("삭제하시겠습니까?");
			builder.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					SQLiteDatabase db = helper.getWritableDatabase();
					myManager = new DataManager(helper, myAdapter, db);
					myManager.deleteContact(myAdapter, targetId);
				}
			});
			builder.setNegativeButton("취소", null);
			builder.setCancelable(false);
			
			builder.show();
			
			return true;
		}
	
	};

	
}




