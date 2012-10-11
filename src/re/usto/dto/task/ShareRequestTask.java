package re.usto.dto.task;

import org.apache.http.conn.ConnectTimeoutException;

import re.usto.dto.helper.Message;
import re.usto.dto.helper.Proxy;
import re.usto.dto.helper.StringUtils;

import android.content.Context;

public class ShareRequestTask extends BaseTask
{
	public ShareRequestTask(Context context, String dialogTitle, OnTaskComplete onComplete)
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
				String response = Proxy.postShare(post.getUrl(), post.getParams());
				
				if(response==null){
					return "success";
				}
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
