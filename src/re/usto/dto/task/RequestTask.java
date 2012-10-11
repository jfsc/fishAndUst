package re.usto.dto.task;

import org.apache.http.conn.ConnectTimeoutException;

import re.usto.dto.helper.Message;
import re.usto.dto.helper.Proxy;

import android.content.Context;

public class RequestTask extends BaseTask
{
	public RequestTask(Context context, String dialogTitle, OnTaskComplete onComplete)
	{
		super(context, dialogTitle, onComplete);
	}

	protected String doInBackground(PostUrl... params)
	{
		for (PostUrl post : params)
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
			catch (Throwable e)
			{
				Message.debug(e.toString());
			}
		}
		return null;
	}	
	
}
