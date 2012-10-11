package re.usto.dto.task;

import re.usto.dto.helper.Proxy;
import re.usto.dto.helper.StringUtils;
import android.content.Context;

public class UploadFileTask extends UploadTask {

	public UploadFileTask(Context context, String dialogTitle,
			OnTaskComplete onComplete) {
		super(context, dialogTitle, onComplete);
	}
	
	
	@Override
	protected String doInBackground(RequestUrl... params)
	{
		for(RequestUrl post : params)
		{
			String resp = Proxy.upload(post);
			if(StringUtils.hasError(resp))
			{
				return resp;
			}
			
		}
		return null;
	}

}
