/*최종 작성일 : 2015-12-16
 * 작성자 : 정혜인
 * 설명 : 네이버 지역검색 API - 검색결과 파싱*/
package mobile.project.ma02_20131126;

import java.io.StringReader;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import android.util.Log;

public class MyXmlParser {

//	Parsing 대상 태그를 구분용 상수
	final int NO_TAG = 0;
	final int TITLE = 1;
	final int CATEGORY = 2;
	final int ADDRESS = 3;
	final int MAPX = 4;
	final int MAPY = 5;
	
	public ArrayList<ItemDto> parse(String xml) {
		
//		Parsing 결과 생성한 dto 를 저장하기 위한 list
		ArrayList<ItemDto> list = new ArrayList<ItemDto>();
		try {
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			XmlPullParser parser = factory.newPullParser();
			
			ItemDto dto = null;
			int tagType = NO_TAG;
			String x = null , y = null;
			ChangeLocation newLoc = new ChangeLocation();
			
			parser.setInput(new StringReader(xml));
			
			for (int eventType = parser.getEventType(); 
					eventType != XmlPullParser.END_DOCUMENT;
					eventType = parser.next()) {
				
				switch(eventType) {
				case XmlPullParser.START_DOCUMENT:
					break;
				case XmlPullParser.END_DOCUMENT:
					break;
				case XmlPullParser.START_TAG:
//					네이버 지역 검색 후 조건에 맞는 경우 dto에 데이터 삽입
					if (parser.getName().equals("item")) {
						dto = new ItemDto();
					} else if (dto != null && parser.getName().equals("title")) {	
						tagType = TITLE;
					} else if (parser.getName().equals("category")) {
						tagType = CATEGORY;
					} else if (parser.getName().equals("address")) {
						tagType = ADDRESS;
					} 
					else if (parser.getName().equals("mapx")) {
						tagType = MAPX;
					} 
					else if (parser.getName().equals("mapy")) {
						tagType = MAPY;
					}else {
						tagType = NO_TAG;		// Parsing 대상 이외의 tag를 읽었을 경우
					}
					
					break;
				case XmlPullParser.END_TAG:
//					닫는 item 태그가 나올 경우 dto 에 저장할 항목들이 다 완료되었음을 의미
//					리스트에 저장 후 새로운 dto 생성, 새로운 tag를 읽도록 초기화
					if (parser.getName().equals("item")) {
						newLoc.parse(x, y);
						dto.setMapx(newLoc.getX());
						dto.setMapy(newLoc.getY());
						list.add(dto);
						dto = new ItemDto();
						tagType = NO_TAG;
					}
					break;
				case XmlPullParser.TEXT:
					if (tagType == TITLE) {
						dto.setTitle(parser.getText());
					} else if (tagType == CATEGORY) {
						dto.setCategory(parser.getText());
					} else if (tagType == ADDRESS) {
						dto.setAddress(parser.getText());
					}
					else if (tagType == MAPX) {
						Log.i("LocCoords", "x"+ parser.getText());
						x = parser.getText();
					}
					else if (tagType == MAPY) {
						Log.i("LocCoords", "y"+ parser.getText());
						y = parser.getText();
					}
					break;
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return list;
	}
	
}
