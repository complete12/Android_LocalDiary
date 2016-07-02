/*최종 작성일 : 2015-12-16
 * 작성자 : 정혜인
 * 설명 : DB에 저장하는 dto 클래스*/
package mobile.project.ma02_20131126;

public class ContactDto {
//	db에 저장할 아이디, 장소명, 메모, 장소 정보
	private long id;
	private String name;
	private String memo;
	private String info;
	private double mapx, mapy;
	int favorite;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
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
	public int getFavorite() {
		return favorite;
	}
	public void setFavorite(int favorite) {
		this.favorite = favorite;
	}
	@Override
	public String toString() {
		return id + ". " +  name ;
	}
	
}
