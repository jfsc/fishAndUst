package re.usto.dto.task;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import org.apache.http.NameValuePair;

import re.usto.dto.helper.Constants;
import re.usto.dto.helper.Proxy;
import android.content.Context;
import android.os.Environment;
import android.util.Log;

public class DownloadTask extends BaseTask
{

	private OutputStream mOutputStream;

	public DownloadTask(Context context, String dialogTitle,
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
				String fileName = getParamValue(post.getParams(), Constants.PARAM_FILENAME);
				String directory = Environment.DIRECTORY_DOWNLOADS + Constants.FOLDER_SLASH + getParamValue(post.getParams(), Constants.PARAM_FILEPATH);
				post.removeParam(Constants.PARAM_FILEPATH);
				
				Log.d("TAG", "FILENAME: " + fileName);
				Log.d("TAG","DIRECTORY: " + directory);

				InputStream is = Proxy.download(post.getUrl(),
						post.getParams(), this.mContext);
				
				File path = Environment
						.getExternalStoragePublicDirectory(directory);
				
				path.mkdirs();
				
				File file = new File(path, fileName);

				mOutputStream = new FileOutputStream(file);
				byte[] buffer = new byte[1024];
				int size = 0;
				while ((size = is.read(buffer)) > 0)
				{
					mOutputStream.write(buffer, 0, size);
				}
				mOutputStream.flush();
			} catch (Exception e)
			{
				e.printStackTrace();
			}

		}
		return Constants.TASK_DOWNLOAD_COMPLETE_RESULT;
	}
	
	private String getParamValue(ArrayList<NameValuePair> params, String paramName)
	{
		String value = "";
		for (NameValuePair param : params)
		{
			if (param.getName().equals(paramName))
			{
				value = param.getValue();
				break;
			}
		}
		return value;
	}
}
