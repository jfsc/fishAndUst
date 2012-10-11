package re.usto.dto.task;

import org.apache.http.conn.ConnectTimeoutException;

import re.usto.dto.helper.Constants;
import re.usto.dto.helper.Message;
import re.usto.dto.helper.Proxy;
import android.content.Context;

public class DeleteRequestTask extends BaseTask
{

	public DeleteRequestTask(Context context, String dialogTitle,
			OnTaskComplete onComplete)
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
				Proxy.post(post.getUrl(), post.getParams());
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
		return Constants.ORDER_REFRESH_LIST;
	}	

}
