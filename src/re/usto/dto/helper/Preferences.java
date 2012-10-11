package re.usto.dto.helper;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class Preferences
{
	private static Preferences mInstance = null;
	
	private SharedPreferences mAppSharedPrefs = null;
	private Editor mPrefsEditor = null;
	
	private Preferences(Context context)
	{
		this.mAppSharedPrefs = context.getSharedPreferences(Constants.APP_SHARED_PREFS,
				Activity.MODE_PRIVATE);
		this.mPrefsEditor = mAppSharedPrefs.edit();
	}
	
	public boolean contains(String key)
	{
		return mAppSharedPrefs.contains(key);
	}
	
	public static Preferences getInstance(Context context)
	{
		if(mInstance == null)
		{
			mInstance = new Preferences(context);
		}
		
		return mInstance;
	}
	
	public String getLogin()
	{
		return mAppSharedPrefs.getString(Constants.PREFERENCE_LOGIN, "");
	}

	public String getUrl()
	{
		return mAppSharedPrefs.getString(Constants.PREFERENCE_URL, "");
	}
	
	public String getPassword()
	{
		return mAppSharedPrefs.getString(Constants.PREFERENCE_PASS, "");
	}

	public void saveLogin(String login, String pass, String url)
	{
		mPrefsEditor.putString(Constants.PREFERENCE_LOGIN, login);
		mPrefsEditor.putString(Constants.PREFERENCE_PASS, pass);
		mPrefsEditor.putString(Constants.PREFERENCE_URL, url);
		mPrefsEditor.commit();
		Constants.SERVER_URL = url;
	}
}
