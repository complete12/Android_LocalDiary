/*최종 작성일 : 2015-12-16
 * 작성자 : 정혜인
 * 설명 : 삽입, 수정, 삭제 등 DB 관리 매니저*/
package mobile.project.ma02_20131126;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.ArrayAdapter;

public class DataManager{
	
	private final static String DB_NAME = "contact_db";
	public final static String TABLE_NAME = "contact_table";
	
	ContactDBHelper helper;
	MyCursorAdapter myAdapter;
	Cursor cursor;
	SQLiteDatabase db;
	ContactDto item;
	
	public DataManager(ContactDBHelper helper, MyCursorAdapter myAdapter, SQLiteDatabase db){
		this.helper = helper;
		this.myAdapter = myAdapter;
		this.db = db;
	}
	
	public void addContact(String name, String memo, String info, double mapx, double mapy){
		
//		데이터 삽입		이름, 메모, 주소정보, 좌표값들
		ContentValues row = new ContentValues();
		row.put("name", name);
		row.put("memo", memo);
		row.put("info", info);
		row.put("mapx", mapx);
		row.put("mapy", mapy);
		row.put("favorite", 0);
				
		db.insert(ContactDBHelper.TABLE_NAME, null, row);

	}
	
	public Cursor beforeUpdate(Long targetId) {
		// TODO Auto-generated method stub
		Cursor cursor = db.rawQuery("select * from " + ContactDBHelper.TABLE_NAME + " where _id =" + targetId, null);
		return cursor;
	}
	//데이터 수정
	public void updateContact(ContactDto dto){
		Long targetId = dto.getId();
		String name = dto.getName();
		String memo = dto.getMemo();
		String info = dto.getInfo();
		double mapx = dto.getMapx();
		double mapy = dto.getMapy();
		int favorite = dto.getFavorite();
		
		ContentValues row = new ContentValues();
		row.put("name", name);
		row.put("memo", memo);
		row.put("info", info);
		row.put("mapx", mapx);
		row.put("mapy", mapy);
		row.put("favorite", favorite);
		
		String where = "_id=?";
//		targetId는 long 타입이므로 string으로 변환한 후 설정 
		String[] whereArgs = new String[] { Long.valueOf(targetId).toString() };
		
		db.update(ContactDBHelper.TABLE_NAME, row, where, whereArgs);
				
		helper.close();
	}
	
	public void deleteContact(MyCursorAdapter myAdapter, long targetId){
//		선택한 항목에 해당하는 레코드 삭제
		String whereClause = "_id=?";
		String whereArgs[] = new String[] { Long.valueOf(targetId).toString() };
		db.delete(ContactDBHelper.TABLE_NAME, whereClause, whereArgs);
		
//		테이블에서 다시 전체 레코드를 읽어옴
		Cursor cursor = db.rawQuery("select * from " + ContactDBHelper.TABLE_NAME, null);
		
		myAdapter.changeCursor(cursor);
		helper.close();
	}
	
	public Cursor searchContact(String searchName){
		
		String[] selectionArgs = new String[]{ "%" + searchName + "%"}; 
		
//		설정한 정보를 사용하여 query 실행
		Cursor cursor =
				db.rawQuery("select * from " + ContactDBHelper.TABLE_NAME + " WHERE name LIKE ?", selectionArgs);
		return cursor;
	}
	
	public void getAllContacts(MyCursorAdapter myAdapter) {
		// TODO Auto-generated method stub
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from " + ContactDBHelper.TABLE_NAME, null);

//		Custom Adapter로 작성한 MyCursorAdapter를 사용
		myAdapter.changeCursor(cursor);
		
		helper.close();
	}
	
	public Cursor getFavoriteContacts() {
		// TODO Auto-generated method stub
		Cursor cursor = db.rawQuery("select * from " + ContactDBHelper.TABLE_NAME + " WHERE favorite = " + 1, null);

//		Custom Adapter로 작성한 MyCursorAdapter를 사용
		return cursor;
	}

}
