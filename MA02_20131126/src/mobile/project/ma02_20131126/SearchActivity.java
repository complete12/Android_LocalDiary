/*최종 작성일 : 2015-12-16
 * 작성자 : 정혜인
 * 설명 : 키워드로 검색한 검색결과 띄우는 액티비티*/
package mobile.project.ma02_20131126;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class SearchActivity extends Activity {

	Intent intent;
	TextView txt1;
	EditText etName;
	ListView list1;
	DataManager myManager;
	String searchName, memo;
	double mapx, mapy;
	//네이버 지역 검색API
	String url = "http://openapi.naver.com/search?"
			+ "key=bb834012f597eb5e56ea51284feef2e3"
			+ "&display=50"
			+ "&start=1"
			+ "&target=local"
			+ "&query=";
	ArrayList<ItemDto> list;
	ArrayAdapter<ItemDto> adapter;
	MyXmlParser parser;
	ProgressDialog progDlg;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		
		
		StrictMode.ThreadPolicy pol = new StrictMode.ThreadPolicy.Builder().permitNetwork().build();
		StrictMode.setThreadPolicy(pol);
		
		
		txt1 = (TextView)findViewById(R.id.textInfo);
		etName = (EditText)findViewById(R.id.etNewName);
		list1 = (ListView)findViewById(R.id.lvNaver);
		

//intent로 이전 액티비티에서 작성한 검색어를 가져온다.
		intent = getIntent();
		searchName = intent.getStringExtra("LocName");
		memo = intent.getStringExtra("writtenMemo");
		mapx = intent.getDoubleExtra("mapx", 0);
		mapy = intent.getDoubleExtra("mapy", 0);
		
		txt1.setText("\"" + searchName + "\"으로 검색한 결과입니다.");
		
		list = new ArrayList<ItemDto>();
        adapter = new ArrayAdapter<ItemDto>(this, android.R.layout.simple_list_item_1, list);
        list1.setAdapter(adapter);
        list1.setOnItemClickListener(itemClickListener);
        
        parser = new MyXmlParser();
        
        try {
			searchName = URLEncoder.encode(searchName, "UTF-8");
			Log.d("OpenApi", "query: " + searchName);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		url += searchName;
		
		progDlg = ProgressDialog.show(this, "검색중", "해당 단어를 검색중입니다.");
		
		NetworkThread thread = new NetworkThread(handler, url);
		thread.setDaemon(true);
		thread.start();
	}
	//해당 아이템을 클릭하면 아이템의 정보가 전달된다.
	AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
			intent.putExtra("id", id);
			intent.putExtra("info", parent.getItemAtPosition(pos).toString());
			intent.putExtra("LocName", intent.getStringExtra("LocName").toString());
			intent.putExtra("writtenMemo", intent.getStringExtra("writtenMemo").toString());
			intent.putExtra("mapx", ((ItemDto)parent.getItemAtPosition(pos)).getMapx());
			intent.putExtra("mapy", ((ItemDto)parent.getItemAtPosition(pos)).getMapy());
			setResult(RESULT_OK, intent);
			finish();
		} 
		
	};
	
	
	 Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				progDlg.dismiss();
				
				String xml = (String)msg.obj;
				ArrayList<ItemDto> list = parser.parse(xml);
				
//				Parsing 수행 후 결과 list를 apdater에 추가(기존의 adapter 내용을 지운 후 새로이 전체 추가)
				adapter.clear();
				adapter.addAll(list);
				
			}
	    };
}
