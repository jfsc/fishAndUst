package re.usto.dto.task;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import re.usto.dto.helper.Message;

public class RequestUrl
{
	private String mURL;
	private ArrayList<NameValuePair> mParams;
	
	public RequestUrl(String url)
	{
		Message.debug(url);
		mURL = url;
		mParams = new ArrayList<NameValuePair>();
	}
	
	public void addParam(String name, String value)
	{
		mParams.add(new BasicNameValuePair(name, value));
	}
	
	public void removeParam(String name)
	{
		int index = -1;
		int total = this.mParams.size();
		for(int i = 0; i < total; i++)
		{
			NameValuePair param = this.mParams.get(i);
			if(param.getName().equals(name))
			{
				index = i;
				break;
			}
		}
		if(index != -1)
		{
			this.mParams.remove(index);
		}
	}
	
	public final ArrayList<NameValuePair> getParams()
	{
		return mParams;
	}
	
	public String getUrl()
	{
		return mURL;
	}
}
