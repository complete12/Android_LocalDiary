/*최종 작성일 : 2015-12-16
 * 작성자 : 정혜인
 * 설명 : DB 데이터 수정 액티비티*/
package mobile.project.ma02_20131126;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;
import net.daum.mf.map.api.MapPOIItem.CalloutBalloonButtonType;
import net.daum.mf.map.api.MapView.MapViewEventListener;
import net.daum.mf.map.api.MapView.OpenAPIKeyAuthenticationResultListener;
import net.daum.mf.map.api.MapView.POIItemEventListener;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class UpdateContactActivity extends Activity implements MapViewEventListener{

	//다음 맵뷰와 마커 포함
	private static final String LOG_TAG = "MapView";
	EditText etName;
	EditText etMemo;
	Button search;
	TextView txt1, txtInfo;
	DataManager myManager;
	ContactDBHelper helper;
	long targetId;
	String searchName, memo, info;
	Intent intent;
	MapView mapview;
	MapPOIItem marker;
	double x, y;
	int rated;
	static int tmp = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_insert_contact);
		
		etName = (EditText)findViewById(R.id.etNewName);
		etMemo = (EditText)findViewById(R.id.etNewMemo);
		search = (Button)findViewById(R.id.searchLoc);
		txt1 = (TextView)findViewById(R.id.textInfo);
		txtInfo = (TextView)findViewById(R.id.infoResult);
		
		helper = new ContactDBHelper(this);
		
//		insertContactActivity와 동일한 layout을 사용하므로 필요한 부분의 text 변경
		((TextView)findViewById(R.id.tvAddNewContactTitle)).setText("내용 변경");;
		((Button)findViewById(R.id.btnAddNewContact)).setText("내용 변경");


		intent = getIntent();
		targetId = intent.getLongExtra("id", 0);
		
		
		SQLiteDatabase db = helper.getReadableDatabase();

		myManager = new DataManager(helper, null, db);
		
//		id에 해당하는 레코드를 읽어와 화면에 표시
		Cursor cursor = myManager.beforeUpdate(targetId);
		cursor.moveToNext();
		
		etName.setText(cursor.getString(1));
		etMemo.setText(cursor.getString(2));
		txtInfo.setText(cursor.getString(3));
		x = cursor.getDouble(4);
		y = cursor.getDouble(5);
		rated = cursor.getInt(6);

		mapview = new MapView(this);
		mapview.setOpenAPIKeyAuthenticationResultListener(APIListener); //API key 리스너
		mapview.setDaumMapApiKey("68687dd118e4350119b8cc541f259a1f"); //API key
		mapview.setMapType(MapView.MapType.Standard);
		mapview.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading);

//		맵뷰를 레이아웃에 설정한다.
		ViewGroup mapviewContainer = (ViewGroup)findViewById(R.id.map);
		mapviewContainer.addView(mapview);

		
		marker = new MapPOIItem();
		marker.setItemName("Default Marker");
		marker.setTag(0);
		marker.setMapPoint(MapPoint.mapPointWithGeoCoord(x, y));
		marker.setMarkerType(MapPOIItem.MarkerType.BluePin); //  BluePin 마커 모양.
		marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin); // 마커를 클릭했을때,  RedPin 마커 모양.

		mapview.addPOIItem(marker);
		
	}

	

	@Override
	protected void onResume() {
		super.onResume();
		
		SQLiteDatabase db = helper.getReadableDatabase();

		myManager = new DataManager(helper, null, db);
		
//		id에 해당하는 레코드를 읽어와 화면에 표시
		Cursor cursor = myManager.beforeUpdate(targetId);
		cursor.moveToNext();
		
		etName.setText(cursor.getString(1));
		etMemo.setText(cursor.getString(2));
		txtInfo.setText(cursor.getString(3));
		
		if(tmp != 0)
			mapview.setMapCenterPointAndZoomLevel(MapPoint.mapPointWithGeoCoord(x,y), 2, true);
		else
			mapview.setMapCenterPointAndZoomLevel(MapPoint.mapPointWithGeoCoord(cursor.getDouble(4), cursor.getDouble(5)), 2, true);
		mapview.removePOIItem(marker);
		marker = new MapPOIItem();
		marker.setItemName("Default Marker");
		marker.setTag(0);
		marker.setMapPoint(MapPoint.mapPointWithGeoCoord(x, y));
		marker.setMarkerType(MapPOIItem.MarkerType.BluePin); // 기본으로 제공하는 BluePin 마커 모양.
		marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin); // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.

		mapview.addPOIItem(marker);
		
		mapview.setPOIItemEventListener(markerListener);
		
		//intent값과 원래 db값 비교하여 setText
		if((searchName!=null && memo!=null && info!=null)
				&&(searchName != cursor.getString(1) || memo != cursor.getString(2) || info != cursor.getString(3))){
			etName.setText(searchName);
			etMemo.setText(memo);
			txtInfo.setText(info);
		}
				
		cursor.close();
		helper.close();
		

	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		SQLiteDatabase db = helper.getReadableDatabase();

		myManager = new DataManager(helper, null, db);
		
//		id에 해당하는 레코드를 읽어와 화면에 표시
		Cursor cursor = myManager.beforeUpdate(targetId);
		cursor.moveToNext();
		x = cursor.getDouble(4);
		y = cursor.getDouble(5);
		if(data != null){
			searchName = data.getStringExtra("LocName").toString();
			memo = data.getStringExtra("writtenMemo").toString();
			info = data.getStringExtra("info").toString();
			x = data.getDoubleExtra("mapx", resultCode);
			y = data.getDoubleExtra("mapy", resultCode);
			if(x != cursor.getDouble(4) || y != cursor.getDouble(5) && data.getStringExtra("info").toString()!= null) tmp = 1;
			mapview.setMapCenterPointAndZoomLevel(MapPoint.mapPointWithGeoCoord(x,y), 2, true);
			
			mapview.removePOIItem(marker);
			marker = new MapPOIItem();
			marker.setItemName("Default Marker");
			marker.setTag(0);
			marker.setMapPoint(MapPoint.mapPointWithGeoCoord(x, y));
			marker.setMarkerType(MapPOIItem.MarkerType.BluePin); // 기본으로 제공하는 BluePin 마커 모양.
			marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin); // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.

			mapview.addPOIItem(marker);
			
			mapview.setPOIItemEventListener(markerListener);
			
			etName.setText(data.getStringExtra("LocName").toString());
			etMemo.setText(data.getStringExtra("writtenMemo").toString());
			txtInfo.setText(data.getStringExtra("info").toString());
		}
	}

	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.searchLoc:
			Intent intent = new Intent(this, SearchActivity.class);
			intent.putExtra("LocName", etName.getText().toString());
			intent.putExtra("writtenMemo", etMemo.getText().toString());
			intent.putExtra("activity", "update");
			intent.putExtra("mapx", x);
			intent.putExtra("mapy", y);
			if (intent != null)
				startActivityForResult(intent, 200);
			break;
		case R.id.btnAddNewContact:
			
			String name = etName.getText().toString();
			String memo = etMemo.getText().toString();
			String info = txtInfo.getText().toString();
			
			ContactDto dto = new ContactDto();
			dto.setId(targetId);
			dto.setName(name);
			dto.setMemo(memo);
			dto.setInfo(info);
			dto.setMapx(x);
			dto.setMapy(y);
			dto.setFavorite(rated);

			SQLiteDatabase db = helper.getWritableDatabase();
			myManager = new DataManager(helper, null, db);
			myManager.updateContact(dto);
			Toast.makeText(this, "수정하였습니다.", Toast.LENGTH_SHORT).show();
			
			break;
		case R.id.btnAddNewContactClose:
			finish();
			break;
		}
	}
	
	

	OpenAPIKeyAuthenticationResultListener APIListener = new OpenAPIKeyAuthenticationResultListener(){

		
//		API key 인증
		@Override
		public void onDaumMapOpenAPIKeyAuthenticationResult(MapView mapView, int resultCode, String resultMessage) {
			Log.i(LOG_TAG,	String.format("Open API Key Authentication Result : code=%d, message=%s", resultCode, resultMessage));
		}
		
	};
	
 	 //맵뷰 등록
    	public void onMapViewInitialized(MapView mapView) {
            Log.i(LOG_TAG, "MapView had loaded. Now, MapView APIs could be called safely");
            mapView.setMapCenterPointAndZoomLevel(MapPoint.mapPointWithGeoCoord(x,y), 2, true);
        }

    	
    	//중심점 옮겨졌을때 로그 생성
        @Override
        public void onMapViewCenterPointMoved(MapView mapView, MapPoint mapCenterPoint) {
            MapPoint.GeoCoordinate mapPointGeo = mapCenterPoint.getMapPointGeoCoord();
            Log.i(LOG_TAG, String.format("MapView onMapViewCenterPointMoved (%f,%f)", mapPointGeo.latitude, mapPointGeo.longitude));
        }

        
        //더블클릭 시 alertDialog
        @Override
        public void onMapViewDoubleTapped(MapView mapView, MapPoint mapPoint) {

            MapPoint.GeoCoordinate mapPointGeo = mapPoint.getMapPointGeoCoord();

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(UpdateContactActivity.this);
            alertDialog.setTitle("mobile.project.ma02_20131126");
            alertDialog.setMessage(String.format("Double-Tap on (%f,%f)", mapPointGeo.latitude, mapPointGeo.longitude));
            alertDialog.setPositiveButton("OK", null);
            alertDialog.show();
        }

        
        //롱클릭 시 alertDialog
        @Override
        public void onMapViewLongPressed(MapView mapView, MapPoint mapPoint) {

            MapPoint.GeoCoordinate mapPointGeo = mapPoint.getMapPointGeoCoord();

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(UpdateContactActivity.this);
            alertDialog.setTitle("mobile.project.ma02_20131126");
            alertDialog.setMessage(String.format("Long-Press on (%f,%f)", mapPointGeo.latitude, mapPointGeo.longitude));
            alertDialog.setPositiveButton("OK", null);
            alertDialog.show();
        }

        //한번 클릭 시 로그 생성
        @Override
        public void onMapViewSingleTapped(MapView mapView, MapPoint mapPoint) {
            MapPoint.GeoCoordinate mapPointGeo = mapPoint.getMapPointGeoCoord();
            Log.i(LOG_TAG, String.format("MapView onMapViewSingleTapped (%f,%f)", mapPointGeo.latitude, mapPointGeo.longitude));
        }

        //맵뷰 드래그 시작 시 로그생성
        @Override
        public void onMapViewDragStarted(MapView mapView, MapPoint mapPoint) {
            MapPoint.GeoCoordinate mapPointGeo = mapPoint.getMapPointGeoCoord();
            Log.i(LOG_TAG, String.format("MapView onMapViewDragStarted (%f,%f)", mapPointGeo.latitude, mapPointGeo.longitude));
        }

        
        //맵뷰 드래그 끝날 때 로그생성
        @Override
        public void onMapViewDragEnded(MapView mapView, MapPoint mapPoint) {
            MapPoint.GeoCoordinate mapPointGeo = mapPoint.getMapPointGeoCoord();
            Log.i(LOG_TAG, String.format("MapView onMapViewDragEnded (%f,%f)", mapPointGeo.latitude, mapPointGeo.longitude));
        }

        
        //맵뷰 이동 종료 시 로그생성
        @Override
        public void onMapViewMoveFinished(MapView mapView, MapPoint mapPoint) {
            MapPoint.GeoCoordinate mapPointGeo = mapPoint.getMapPointGeoCoord();
            Log.i(LOG_TAG, String.format("MapView onMapViewMoveFinished (%f,%f)", mapPointGeo.latitude, mapPointGeo.longitude));
        }

        
        //맵뷰 줌 레벨 변경 시 로그생성
        @Override
        public void onMapViewZoomLevelChanged(MapView mapView, int zoomLevel) {
            Log.i(LOG_TAG, String.format("MapView onMapViewZoomLevelChanged (%d)", zoomLevel));
        }

   
    
    POIItemEventListener markerListener = new POIItemEventListener(){

		@Override
		public void onCalloutBalloonOfPOIItemTouched(MapView arg0,
				MapPOIItem arg1) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onCalloutBalloonOfPOIItemTouched(MapView arg0,
				MapPOIItem arg1, CalloutBalloonButtonType arg2) {
			// TODO Auto-generated method stub
			
		}

		@Override//마커 드래그로 이동
		public void onDraggablePOIItemMoved(MapView arg0, MapPOIItem arg1,
				MapPoint arg2) {
			// TODO Auto-generated method stub
			
		}

		@Override//마커 터치
		public void onPOIItemSelected(MapView arg0, MapPOIItem arg1) {
			// TODO Auto-generated method stub
			
		}
		
	};

}



