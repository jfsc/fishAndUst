package re.usto.dto.task;

import org.apache.http.conn.ConnectTimeoutException;

import re.usto.dto.helper.Constants;
import re.usto.dto.helper.Message;
import re.usto.dto.helper.Proxy;
import re.usto.dto.object.Files;
import android.content.Context;

public class SearchRequestTask extends BaseTask
{
	public SearchRequestTask(Context context, String dialogTitle,
			OnTaskComplete onComplete) {
		super(context, dialogTitle, onComplete);
		// TODO Auto-generated constructor stub
	}


	@Override
	protected String doInBackground(RequestUrl... params)
	{
		for (RequestUrl post : params)
		{
			try
			{
				String response = Proxy.get(post.getUrl(), post.getParams());
				Message.debug(response.toString());
				
				Files.createFolders(parseFileDataList(response.toString()));
				return Constants.ORDER_SET_FILE_LIST;
			}
			catch (ConnectTimeoutException e)
			{
				Message.debug(e.toString());
				
			}
			catch (NullPointerException e)
			{
				return Constants.EMPTY_SEARCH;
				
			}
			catch (Exception e)
			{
				Message.debug(e.toString());
			}
		}
		return null;
	}	
	
}