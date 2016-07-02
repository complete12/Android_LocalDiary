/*최종 작성일 : 2015-12-16
 * 작성자 : 정혜인
 * 설명 : 다음API - 좌표 변환 파싱*/
package mobile.project.ma02_20131126;

import java.io.StringReader;
import java.net.URL;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.os.StrictMode;
import android.util.Log;

public class ChangeLocation {
	//Parsing 대상 태그를 구분용 상수
	final int NO_TAG = 0;

	double mapx, mapy;
	XmlPullParser parser;
	String x, y;
	
	//검색결과의 카텍 좌표값 x, y 받아와서 파싱
	public void parse(String x1, String y1) {
		
		
		StrictMode.ThreadPolicy pol = new StrictMode.ThreadPolicy.Builder().permitNetwork().build();
		StrictMode.setThreadPolicy(pol);
		
//		Parsing 결과 생성한 dto 를 저장하기 위한 list
		try {
			URL text = new URL("http://apis.daum.net/maps/transcoord?apikey="
					+ "1b540a4386366e5a13e4a8782c886905" 
					+ "&x=" + x1
					+ "&y=" + y1 
					+ "&fromCoord=KTM&toCoord=WGS84&output=xml");
			Log.i("ChangeLoc", "x1: " + x1 + " y1: " + y1);
			Log.i("ChangeLoc", "url " + text);	
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			parser = factory.newPullParser();
			int tagType = NO_TAG;
			
			parser.setInput(text.openStream(),null);
			int parserEvent = parser.getEventType();

			   while(parserEvent != XmlPullParser.END_DOCUMENT)
			   {
				   switch (parserEvent) {
				   case XmlPullParser.START_TAG:
					   String tag = parser.getName();
					   if (tag.equals("result"))
					   {
						   checkAttribute();
			     }
			     break;
			    }
			    parserEvent = parser.next();
			   }
			  }catch (Exception e) {
			   // TODO: handle exception
				  e.printStackTrace();
			  }
	}
	//각각 x, y 반환
	public double getX(){
		Log.i("ChangeLoc","changed x: "+ mapx);
		return mapx;
	}
	public double getY(){
		Log.i("ChangeLoc","changed y:"+ mapy);
		return mapy;
	}
	
	
//	START_TAG 이벤트 시 호출하여 태그에 포함한 속성을 확인
	private void checkAttribute() throws XmlPullParserException {
//		현재 태그가 갖고 있는 속성의 개수 확인
		int count = parser.getAttributeCount();
//		속성 개수만큼 반복하여 속성명과 속성값 출력
		for (int i=0; i < count; i++){
			Log.i("ChangeLocation", "Attribute: " + parser.getAttributeName(i) + "=" + parser.getAttributeValue(i));
			if(parser.getAttributeName(i).equals("x"))
				mapy = Double.parseDouble(parser.getAttributeValue(i));
			else if(parser.getAttributeName(i).equals("y"))
					mapx = Double.parseDouble(parser.getAttributeValue(i));
		}
	}
}
