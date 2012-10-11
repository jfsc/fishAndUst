package re.usto.dto.helper;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import re.usto.dto.task.RequestUrl;

import android.content.Context;
import android.util.Log;

public class Proxy
{
	
	private static HttpClient mHttpClient;

	private static HttpClient getHttpClient()
	{
		if (mHttpClient == null)
		{
			mHttpClient = new DefaultHttpClient();
			final HttpParams params = mHttpClient.getParams();
			HttpConnectionParams.setConnectionTimeout(params, Constants.HTTP_TIMEOUT);
			HttpConnectionParams.setSoTimeout(params, Constants.HTTP_TIMEOUT);
			ConnManagerParams.setTimeout(params, Constants.HTTP_TIMEOUT);
		}
		return mHttpClient;
	}

	public static String post(String url,
			ArrayList<NameValuePair> params) throws Exception
	{
		BufferedReader in = null;
		try
		{
			showRequest(url, params);
			HttpClient client = getHttpClient();
			HttpPost request = new HttpPost(url);
			UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(
					params);
			request.setEntity(formEntity);
			HttpResponse response = client.execute(request);
			in = new BufferedReader(new InputStreamReader(response.getEntity()
					.getContent()));

			StringBuffer sb = new StringBuffer("");
			String line = "";
			String nl = System.getProperty("line.separator");
			while ((line = in.readLine()) != null)
			{
				sb.append(line + nl);
			}
			in.close();

			String result = sb.toString();
			return result;
		} finally
		{
			if (in != null)
			{
				try
				{
					in.close();
				} catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}
	}

	public static String postShare(String url,
			ArrayList<NameValuePair> params) throws Exception
	{
		BufferedReader in = null;
		try
		{
			showRequest(url, params);
			HttpClient client = getHttpClient();
			HttpPost request = new HttpPost(url);
			UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(
					params);
			request.setEntity(formEntity);
			ResponseHandler<String> responseHandler=new BasicResponseHandler();
			String response = client.execute(request,responseHandler);
			return response;
		} catch (Exception e) {
			throw e;
		}
	}

	public static String get(String url, ArrayList<NameValuePair> getParams) throws Exception
	{
		BufferedReader in = null;
		try
		{
			int total = getParams.size();
			if(total > 0)
			{
				for(int i = 0; i < total; i++)
				{
					NameValuePair param = getParams.get(i);
					
					url += param.getValue();
					if(i + 1 < total)
					{
						url += "/";
					}
				}
			}
			showRequest(url, null);
			HttpClient client = getHttpClient();
			HttpGet request = new HttpGet();
			request.setURI(new URI(url));
			HttpResponse response = client.execute(request);
			in = new BufferedReader(new InputStreamReader(response.getEntity()
					.getContent()));

			StringBuffer sb = new StringBuffer("");
			String line = "";
			String NL = System.getProperty("line.separator");
			while ((line = in.readLine()) != null)
			{
				sb.append(line + NL);
			}
			in.close();

			String result = sb.toString();
			return result;
		} finally
		{
			if (in != null)
			{
				try
				{
					in.close();
				} catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}
	}

	public static InputStream download(String url,
			ArrayList<NameValuePair> params, Context context)
	{
		BufferedReader in = null;
		try
		{
			showRequest(url, params);
			HttpClient client = getHttpClient();
			HttpPost request = new HttpPost(url);
			UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(params);
			request.setEntity(formEntity);

			HttpResponse response = client.execute(request);
			HttpEntity entity = response.getEntity();

			return entity.getContent();
		} catch (Throwable e)
		{
			return null;
		} finally
		{
			if (in != null)
			{
				try
				{
					in.close();
				} catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}
	}
	
	public static void showRequest(String url, ArrayList<NameValuePair> params)
	{
		Log.d(Constants.LOG_TAG, "URL: " + url);
		if(params != null)
		{
			Log.d(Constants.LOG_TAG, "PARAMS: ");
			for(NameValuePair param : params)
			{
				Log.d(Constants.LOG_TAG, "Name: " + param.getName() + " | Value: " + param.getValue());
			}
		}
		Log.d(Constants.LOG_TAG, "---------------------------");
	}

	public static String upload(RequestUrl post)
	{
		try
		{
			showRequest(post.getUrl(), post.getParams());
			HttpClient client = getHttpClient();
			HttpContext localContext = new BasicHttpContext();
			HttpPost httpPost = new HttpPost(post.getUrl());

			MultipartEntity reqEntity = new MultipartEntity(
					HttpMultipartMode.BROWSER_COMPATIBLE);

			for (NameValuePair param : post.getParams())
			{
				if (param.getName().equals(Constants.PARAM_FILE))
				{
					reqEntity.addPart(param.getName(), new FileBody(new File(
							param.getValue())));
				} else
				{
					reqEntity.addPart(param.getName(),
							new StringBody(param.getValue()));
				}

				Message.debug("UPLOADING... PARAM NAME: " + param.getName()
						+ ", PARAM VALUE: " + param.getValue());
			}

			httpPost.setEntity(reqEntity);

			HttpResponse response = client.execute(httpPost, localContext);
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent(), "UTF-8"));

			String sResponse = reader.readLine();

			return sResponse;

		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

}