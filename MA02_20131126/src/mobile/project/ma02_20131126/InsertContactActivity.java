/*최종 작성일 : 2015-12-16
 * 작성자 : 정혜인
 * 설명 : 새로운 데이터 입력 액티비티*/
package mobile.project.ma02_20131126;

import java.util.ArrayList;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPOIItem.CalloutBalloonButtonType;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;
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
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
//	네이버 검색 API와 다음 지도 API 이용
public class InsertContactActivity extends Activity{

    private static final String LOG_TAG = "MapView";
    
	
	ContactDBHelper helper;
	
	//다음 지도 API의 맵뷰, 마커 포함
	EditText etName;
	EditText etMemo;
	Button search;
	TextView txt1, txtInfo;
	String searchName, memo, info;
	ArrayList<ItemDto> list;
	MapView mapview;
	MapPOIItem marker;
	double x = 37.0, y = 127.0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_insert_contact);
		
		helper = new ContactDBHelper(this);
		
		etName = (EditText)findViewById(R.id.etNewName);
		etMemo = (EditText)findViewById(R.id.etNewMemo);
		search = (Button)findViewById(R.id.searchLoc);
		txt1 = (TextView)findViewById(R.id.textInfo);
		txtInfo = (TextView)findViewById(R.id.infoResult);
		
//		맵뷰 객체 생성 후 API key 인증, 이벤트리스너와 맵 타입 설정 
		mapview = new MapView(this);
		mapview.setOpenAPIKeyAuthenticationResultListener(APIListener);
		mapview.setDaumMapApiKey("68687dd118e4350119b8cc541f259a1f");
		mapview.setMapType(MapView.MapType.Standard);

//		맵뷰를 레이아웃에 설정한다.
		ViewGroup mapviewContainer = (ViewGroup)findViewById(R.id.map);
		mapviewContainer.addView(mapview);

		
		marker = new MapPOIItem();
		marker.setItemName("Default Marker");
		marker.setTag(0);
		marker.setMapPoint(MapPoint.mapPointWithGeoCoord(x, y));
		marker.setMarkerType(MapPOIItem.MarkerType.BluePin); //  BluePin 마커 모양.
		marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin); // 마커를 클릭했을때, RedPin 마커 모양.

		mapview.addPOIItem(marker);
		
		
		txtInfo.setText("정보없음");
		mapview.setMapViewEventListener(mapListener);
		mapview.setPOIItemEventListener(markerListener);
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(data != null){
			info = data.getStringExtra("info");
			searchName = data.getStringExtra("LocName");
			memo = data.getStringExtra("writtenMemo");
			x = data.getDoubleExtra("mapx", resultCode);
			y = data.getDoubleExtra("mapy", resultCode);
			etName.setText(searchName);
			etMemo.setText(memo);
			txtInfo.setText(info);
			mapview.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(x, y), true);
			
			mapview.removePOIItem(marker);
			marker = new MapPOIItem();
			marker.setItemName("Default Marker");
			marker.setTag(0);
			marker.setMapPoint(MapPoint.mapPointWithGeoCoord(x, y));
			marker.setMarkerType(MapPOIItem.MarkerType.BluePin); //  BluePin 마커 모양.
			marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin); // 마커를 클릭했을때, RedPin 마커 모양.

			mapview.addPOIItem(marker);
			
			mapview.setMapViewEventListener(mapListener); //맵뷰 리스너
			mapview.setPOIItemEventListener(markerListener);//마커 리스너
		}
	}

	public void onPause(){
		super.onPause();
	}

	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.searchLoc:
// 			검색버튼 누르면 인텐트로 작성했던 내용 전달
			Intent intent = new Intent(this, SearchActivity.class);
			intent.putExtra("LocName", etName.getText().toString());
			intent.putExtra("writtenMemo", etMemo.getText().toString());
			intent.putExtra("activity", "insert");
			intent.putExtra("mapx", x);
			intent.putExtra("mapy", y);
			if (intent != null)
				startActivityForResult(intent, 100);
			break;
		case R.id.btnAddNewContact:
			//저장버튼 누르면 저장
			etName = (EditText)findViewById(R.id.etNewName);
			etMemo = (EditText)findViewById(R.id.etNewMemo);
			search = (Button)findViewById(R.id.searchLoc);
			txtInfo = (TextView)findViewById(R.id.infoResult);
			
			String name = etName.getText().toString();
			String memo = etMemo.getText().toString();
			String info = txtInfo.getText().toString();
			
			SQLiteDatabase db = helper.getWritableDatabase();
			
			DataManager myManager = new DataManager(helper, null, db);
			myManager.addContact(name, memo, info, x, y);
		
			helper.close();
			
			Toast.makeText(this, "저장하였습니다.", Toast.LENGTH_SHORT).show();
			finish();
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
	


    MapViewEventListener mapListener = new MapViewEventListener(){

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

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(InsertContactActivity.this);
            alertDialog.setTitle("mobile.project.ma02_20131126");
            alertDialog.setMessage(String.format("Double-Tap on (%f,%f)", mapPointGeo.latitude, mapPointGeo.longitude));
            alertDialog.setPositiveButton("OK", null);
            alertDialog.show();
        }

        
        //롱클릭 시 alertDialog
        @Override
        public void onMapViewLongPressed(MapView mapView, MapPoint mapPoint) {

            MapPoint.GeoCoordinate mapPointGeo = mapPoint.getMapPointGeoCoord();

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(InsertContactActivity.this);
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

        public void onCurrentLocationUpdate(MapView mapView, MapPoint mapCenterPoint, float arg2){
        	
        }
    	
    };
    
    
    
    
    POIItemEventListener markerListener = new POIItemEventListener(){

		@Override
		public void onCalloutBalloonOfPOIItemTouched(MapView arg0,
				MapPOIItem arg1) {
			// TODO Auto-generated method stub
			
		}

		@Deprecated
		public void onCalloutBalloonOfPOIItemTouched(MapView arg0,
				MapPOIItem arg1, CalloutBalloonButtonType arg2) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onDraggablePOIItemMoved(MapView arg0, MapPOIItem arg1,
				MapPoint arg2) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onPOIItemSelected(MapView arg0, MapPOIItem arg1) {
			// TODO Auto-generated method stub
			
		}
		
	};

	
}
