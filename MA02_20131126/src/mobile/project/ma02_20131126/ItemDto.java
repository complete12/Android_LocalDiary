/*최종 작성일 : 2015-12-16
 * 작성자 : 정혜인
 * 설명 : 검색 결과를 저장하는 dto 클래스*/
package mobile.project.ma02_20131126;

import java.io.StringReader;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import android.util.Log;

public class ItemDto {

//	XML 대상의 parsing 대상에 따라 멤버 변수 지정 & getter/setter 생성
//	네이버 지역 검색 API 참조.
	int start, display;
	double mapx, mapy;
	String category, title, link, description;
	String telephone, address, roadAddress;
	
	final int NO_TAG = 0;
	final int TITLE = 1;

		
	public int getStart() {
		return start;
	}


	public void setStart(int start) {
		this.start = start;
	}


	public int getDisplay() {
		return display;
	}


	public void setDisplay(int display) {
		this.display = display;
	}


	public double getMapx() {
		return mapx;
	}


	public void setMapx(double mapx) {
		this.mapx = mapx;
	}


	public double getMapy() {
		return mapy;
	}


	public void setMapy(double mapy) {
		this.mapy = mapy;
	}

	public void getLocation(String xml){
		
	}
	

	public String getCategory() {
		return category;
	}


	public void setCategory(String category) {
		this.category = category;
	}


	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}


	public String getLink() {
		return link;
	}


	public void setLink(String link) {
		this.link = link;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public String getTelephone() {
		return telephone;
	}


	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}


	public String getAddress() {
		return address;
	}


	public void setAddress(String address) {
		this.address = address;
	}


	public String getRoadAddress() {
		return roadAddress;
	}


	public void setRoadAddress(String roadAddress) {
		this.roadAddress = roadAddress;
	}


	//	String으로 간단히 출력할 필요가 있을 경우를 위해 overriding
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return title + "\n" + category
				+ "\n" + address; 
	}
}
