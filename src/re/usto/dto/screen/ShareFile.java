package re.usto.dto.screen;

import re.usto.dto.R;
import re.usto.dto.helper.Constants;
import re.usto.dto.helper.StringUtils;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ShareFile  extends Activity{
	
	private EditText mEditLoginShare;
	private Button mBtnShare, mBtnCancel;
	@Override
	public void onCreate(Bundle bundle)
	{
		super.onCreate(bundle);
		super.setContentView(R.layout.sharefile);
		configure();

	}
	
	public void configure(){
		this.mBtnShare = (Button) findViewById(R.id.btnShareFile);
		this.mBtnCancel = (Button) findViewById(R.id.btnShareCancel);
		//this.mEditLoginShare = (EditText) findViewById(R.id.shareUser);
		
		this.mBtnShare.setOnClickListener(new View.OnClickListener()
		{
			
			public void onClick(View v)
			{
				String loginShare;
				mEditLoginShare = (EditText) findViewById(R.id.shareUser);
				loginShare = mEditLoginShare.getText().toString();
				if(!StringUtils.isEmpty(loginShare)){
					sendContentBack(loginShare);
				}else{
					createPleaseInserALogin();
				}
			}

		});
		
		this.mBtnCancel.setOnClickListener(new View.OnClickListener()
		{
			
			public void onClick(View v)
			{
				finish();
			}

		});
		
	}
	
	private void createPleaseInserALogin()
	{
		createAlertDialog(Constants.DIALOG_SHARE_TITLE,
				Constants.DIALOG_SHARE_SELECT_A_LOGIN,
				Constants.DIALOG_SHARE_BUTTON_OK);
	}
	private void createAlertDialog(CharSequence dialogTitle,
			CharSequence dialogMessage,
			CharSequence dialogOkButton)
	{
		DialogInterface.OnClickListener ok = new DialogInterface.OnClickListener()
		{

			public void onClick(DialogInterface dialog, int which)
			{
				dialog.dismiss();
			}
			
		};
		
		createAlertDialog(dialogTitle, dialogMessage, dialogOkButton, null, ok, null);
	}
	private void createAlertDialog(CharSequence dialogTitle,
			CharSequence dialogMessage,
			CharSequence dialogOkButton,
			CharSequence dialogCancelButton,
			OnClickListener ok,
			OnClickListener cancel)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(dialogTitle)
				.setMessage(dialogMessage)
				.setCancelable(false)
				.setPositiveButton(dialogOkButton,ok);
		if(dialogCancelButton != null)
		{
			builder.setNegativeButton(dialogCancelButton, cancel);
		}
		AlertDialog alert = builder.create();
		alert.show();
	}


	
	
	public void sendContentBack(String loginShare){
		Intent intentSendBack = new Intent();
		intentSendBack.putExtra(Constants.PARAM_LOGIN_SHARE, loginShare);
		setResult(Constants.REQUEST_CODE_SHARE_FILE_DIALOG,intentSendBack);
		finish();
	}


}
