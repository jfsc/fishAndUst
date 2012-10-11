package re.usto.dto.helper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class Message {

	private Context mContext;
	
	public Message(Context context)
	{
		mContext = context;
	}
	
	public void show(String text)
	{
		show(text, Toast.LENGTH_LONG);
	}
	
	@SuppressLint("ShowToast")
	public void show(String text, int duration)
	{
		Toast.makeText(mContext, text, duration);
	}
	
	public static void debug(String msg)
	{
		Log.d(Constants.LOG_TAG, msg);
	}
}
