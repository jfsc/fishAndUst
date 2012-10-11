package re.usto.dto.task;

import org.apache.http.conn.ConnectTimeoutException;

import re.usto.dto.helper.Message;
import re.usto.dto.helper.Proxy;

import android.content.Context;

public class PostRequestTask extends BaseTask
{
	public PostRequestTask(Context context, String dialogTitle, OnTaskComplete onComplete)
	{
		super(context, dialogTitle, onComplete);
	}
	
	@Override
	protected String doInBackground(RequestUrl... params)
	{
		for (RequestUrl post : params)
		{
			try
			{
				String response = Proxy.post(post.getUrl(), post.getParams());
				return response.toString();
			}
			catch (ConnectTimeoutException e)
			{
				Message.debug(e.toString());
			}
			catch (Exception e)
			{
				Message.debug(e.toString());
			}
		}
		return null;
	}	
	
}
