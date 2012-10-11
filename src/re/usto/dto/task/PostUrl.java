package re.usto.dto.task;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import re.usto.dto.helper.Message;

public class PostUrl
{
	private String mURL;
	private ArrayList<NameValuePair> mParams;
	
	public PostUrl(String url)
	{
		Message.debug(url);
		mURL = url;
		mParams = new ArrayList<NameValuePair>();
	}
	
	public void addParam(String name, String value)
	{
		mParams.add(new BasicNameValuePair(name, value));
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
