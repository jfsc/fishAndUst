package re.usto.dto.task;

import java.io.Reader;
import java.io.StringReader;
import java.util.List;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import re.usto.dto.helper.Constants;
import re.usto.dto.helper.Message;
import re.usto.dto.object.Path;
import re.usto.dto.object.PathContext;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

public class BaseTask extends AsyncTask<RequestUrl, Integer, String> {

	protected ProgressDialog mProgressDialog;
	protected Context mContext;
	protected OnTaskComplete mCallback;
	protected String mDialogTitle;
	
	protected BaseTask(Context context, String dialogTitle, OnTaskComplete onComplete)
	{
		super();
		mContext = context;
		mCallback = onComplete;
		mDialogTitle = dialogTitle;
	}
	
	@Override
	protected void onPreExecute()
	{
		super.onPreExecute();
		mProgressDialog = new ProgressDialog(mContext);
		mProgressDialog.setTitle(mDialogTitle);
		mProgressDialog.setMessage(Constants.REQUEST_DIALOG_MESSAGE);
		mProgressDialog.setIndeterminate(true);
		mProgressDialog.show();
	}
	
	@Override
	protected String doInBackground(RequestUrl... params)
	{
		return null;
	}
	
	@Override
	protected void onProgressUpdate(Integer... values)
	{
		super.onProgressUpdate(values);
		mProgressDialog.setMessage(values[0] + "%");
	}

	@Override
	protected void onPostExecute(String result)
	{
		super.onPostExecute(result);
		mProgressDialog.dismiss();
		if(mCallback != null)
		{
			mCallback.onTaskComplete(result);
		}
	}
	
	protected List<Path> parseFileDataList(String json)
	{
		Serializer serializer = new Persister();
		Reader reader = new StringReader(json);
		try
		{
			PathContext context = serializer.read(PathContext.class, reader,
					false);
			return context.paths;
		} catch (Exception e)
		{
			Message.debug(e.getMessage());
		}

		return null;
	}
}
