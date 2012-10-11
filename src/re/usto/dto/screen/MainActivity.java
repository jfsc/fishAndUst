package re.usto.dto.screen;

import java.io.Reader;
import java.io.StringReader;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import re.usto.dto.R;
import re.usto.dto.object.Folder;
import re.usto.dto.object.User;
import re.usto.dto.helper.Constants;
import re.usto.dto.helper.Message;
import re.usto.dto.helper.Preferences;
import re.usto.dto.helper.StringUtils;
import re.usto.dto.task.PostRequestTask;
import re.usto.dto.task.OnTaskComplete;
import re.usto.dto.task.RequestUrl;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import re.usto.dto.view.*;


public class MainActivity extends Activity implements OnTaskComplete
{

	private static Message mMessage;
	private Preferences mPreferences;
	protected EditText mUser, mPassword, mUrl;
	protected Button mBtnSignIn, mBtnCreateAccount;
	protected CheckBox mCheckRemember;
	private PostRequestTask mLoginTask;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		mMessage = new Message(this);
		mPreferences = Preferences.getInstance(this);
		configureScreen();
		configureListener();
	}

	private void configureScreen()
	{
			setContentView(R.layout.main);
			
			this.mUser = (EditText) findViewById(R.id.user);
			this.mPassword = (EditText) findViewById(R.id.password);
			this.mUrl = (EditText) findViewById(R.id.url);
			this.mCheckRemember = (CheckBox) findViewById(R.id.checkRemember);
			this.mBtnSignIn = (Button) findViewById(R.id.btnSignIn);
			this.mBtnCreateAccount = (Button) findViewById(R.id.btnCreateAccount);
			
			this.mUser.setOnFocusChangeListener(EditTextFocusListener.getFocusListener(Constants.LABEL_LOGIN));
			this.mPassword.setOnFocusChangeListener(EditTextFocusListener.getFocusListener(Constants.LABEL_PASSWORD));

			if (mPreferences.contains(Constants.PREFERENCE_LOGIN))
			{
				this.mUser.setText(mPreferences.getLogin());
				if(mPreferences.contains(Constants.PREFERENCE_PASS))
				{
					this.mPassword.setText(mPreferences.getPassword());
				}
			}
			if (mPreferences.contains(Constants.PREFERENCE_URL))
			{
				Constants.SERVER_URL = mPreferences.getUrl();
				this.mUrl.setText(Constants.SERVER_URL);
			}
			else
			{
				this.mUrl.setText(Constants.SERVER_URL);
			}
			
			this.mUrl.setOnFocusChangeListener(EditTextFocusListener.getFocusListener(Constants.SERVER_URL));
	}
	
	private void configureListener()
	{
		this.mBtnSignIn.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				if(mLoginTask == null)
				{
					String user = MainActivity.this.mUser.getText().toString();
					String password = MainActivity.this.mPassword.getText().toString();
					String url = MainActivity.this.mUrl.getText().toString();
					
					boolean validUrl = URLUtil.isValidUrl(url);
					if (user.length() > 0 && password.length() > 0 && validUrl)
					{					
						Message.debug(url + Constants.REQUEST_LOGIN_URL);
						RequestUrl post = new RequestUrl(url + Constants.REQUEST_LOGIN_URL);
						post.addParam(Constants.PARAM_LOGIN_USER, user);
						post.addParam(Constants.PARAM_LOGIN_PASS, password);
						mLoginTask = new PostRequestTask(MainActivity.this, Constants.DIALOG_LOGIN_TITLE,MainActivity.this);
						mLoginTask.execute(new RequestUrl[]{post});
						
						if(MainActivity.this.mCheckRemember.isChecked())
						{
							mPreferences.saveLogin(user,password,url);
						}
					}
					else
					{
						mMessage.show(Constants.LOGIN_MESSAGE_EMPTY);
					}
				}
			}
		});
	}
	
	private User getUser(String xml)
	{
		Serializer serializer = new Persister();
		Reader reader = new StringReader(xml);
		try
		{
			return serializer.read(User.class, reader, false);
		}
		catch (Exception e)
		{
			return null;
		}
	}
	
	private boolean serverUnavailable(String json)
	{
		if (!StringUtils.isEmpty(json))
		{
			return false;
		}
		return true;
	}

	private void goToDashboard(User user)
	{
		Intent intent = new Intent(this, Dashboard.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable(Constants.PARAM_LOGIN_USER, user);
		intent.putExtras(bundle);
		startActivity(intent);
	}

	public void onTaskComplete(String result)
	{
		mLoginTask = null;
		if (serverUnavailable(result))
		{
			mMessage.show(Constants.SERVER_MESSAGE_FAIL);
			return;
		}

		User userXml = getUser(result);
		if (userXml == null || userXml.isUnauthorized())
		{
			mMessage.show(Constants.LOGIN_MESSAGE_FAIL);
			return;
		}
		goToDashboard(userXml);
	}

	public void setListView(Folder folder)
	{
		// TODO Auto-generated method stub
		
	}

}
