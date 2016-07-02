/*최종 작성일 : 2015-12-17
 * 작성자 : 정혜인
 * 설명 : 저장 목록 가져오는 커스텀 커서 어댑터*/
package mobile.project.ma02_20131126;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.RatingBar;
import android.widget.Toast;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;

public class MyCursorAdapter extends CursorAdapter  implements RatingBar.OnRatingBarChangeListener{

	LayoutInflater inflater; 
	int layout;
	String str1, str2, str3;
	long itemId;
	double d1, d2;
	float seted;
	DataManager myManager;
	ContactDBHelper helper;
	Context con;
	Integer position;
	Intent intent;
	
	public MyCursorAdapter(Context context, int layout, Cursor c) {
		super(context, c, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
		this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.layout = layout;

		helper = new ContactDBHelper(context);
	}
	
	@Override
	public void bindView(View view, Context context, Cursor cursor) {
//		layout에서 각 view를 찾은 후 cursor가 갖고 있는 데이터 설정


		TextView tvId = (TextView)view.findViewById(R.id.tvId);
		TextView tvName = (TextView)view.findViewById(R.id.tvName);
		TextView tvInfo = (TextView)view.findViewById(R.id.tvInfo);
		RatingBar fav = (RatingBar)view.findViewById(R.id.ratingBar1);
        
		tvId.setText(String.format("%d", cursor.getLong(0)));
		tvName.setText(cursor.getString(1));
		tvInfo.setText(cursor.getString(3));
		fav.setRating((float)cursor.getInt(6));
		
		fav.setFocusable(false);
		fav.setFocusableInTouchMode(false);
		fav.setTag(cursor.getPosition());
        fav.setOnRatingBarChangeListener(this);
        
	}
	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
//		layout에 해당하는 view를 생성하여 반환
		con = context;
		return inflater.inflate(layout, parent, false);
	}

	@Override
	public void onRatingChanged(RatingBar ratingBar, float rating,
			boolean fromUser) {
		// TODO Auto-generated method stub
		position = (Integer) ratingBar.getTag();
        Cursor c = getCursor();
        c.moveToFirst();
        c.move(position);
        if(fromUser){
	        Log.i("rating", "at id: " + position + " rating seted: " + (int)rating);
	        
	        ContactDto dto = new ContactDto();
	        dto.setId(c.getLong(0));
	        dto.setName(c.getString(1));
	        dto.setMemo(c.getString(2));
	        dto.setInfo(c.getString(3));
	        dto.setMapx(c.getDouble(4));
	        dto.setMapy(c.getDouble(5));
	        dto.setFavorite((int)rating);
	        
	        SQLiteDatabase db = helper.getWritableDatabase();
			myManager = new DataManager(helper, null, db);
			myManager.updateContact(dto);
			switch((int)rating){
			case 1:
				Toast.makeText(con, "즐겨찾기가 설정되었습니다.", Toast.LENGTH_SHORT).show();
				return;
			case 0: 
				Toast.makeText(con, "즐겨찾기가 해제되었습니다.", Toast.LENGTH_SHORT).show();
				return;
			}
        }
	}

}
