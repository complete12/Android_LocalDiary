/*최종 작성일 : 2015-12-16
 * 작성자 : 정혜인
 * 설명 : 네트워크 스레드 클래스*/
package mobile.project.ma02_20131126;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import android.os.Handler;
import android.os.Message;

public class NetworkThread extends Thread {

	Handler handler;
	String addr;
	
//	네트워크 접속 결과를 전달할 Handler 와 접속 주소를 생성자의 매개변수로 전달
	public NetworkThread(Handler handler, String addr) {
		this.handler = handler;
		this.addr = addr;
	}
	
	
	@Override
	public void run() {
		String resultHtml = downloadHtml(addr);
		
		Message msg = new Message();
		
		msg.obj = resultHtml;
		
		handler.sendMessage(msg);
	}
	
	
	
	String downloadHtml(String addr) {
		StringBuilder receivedData = new StringBuilder(); 
		try {
			URL url = new URL(addr);
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();
			if (conn != null) {
				conn.setConnectTimeout(10000);
				conn.setUseCaches(false);
				if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
					BufferedReader br = new BufferedReader(
							new InputStreamReader(conn.getInputStream()));
					for (;;) {
						String line = br.readLine();
						if (line == null) break;
						receivedData.append(line + '\n'); 
					}
					br.close();
				}
				conn.disconnect();
			}
		} catch (Exception e) {
			return "Error : " + e.getMessage();
		}
		return receivedData.toString();
	}
	
}
