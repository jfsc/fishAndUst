package re.usto.dto.view;

import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;

public class EditTextFocusListener implements OnFocusChangeListener
{
	private String mFocusOutText;
	
	private EditTextFocusListener(String text)
	{
		mFocusOutText = text;
	}
	
	public void onFocusChange(View v, boolean hasFocus)
	{
		if(mFocusOutText != null)
		{
			EditText edit = (EditText) v;
			String text = edit.getText().toString();
			if(text.equals(mFocusOutText) || text.length() == 0)
			{
				if(hasFocus)
				{
					edit.setText("");
				}
				else
				{
					edit.setText(mFocusOutText);
				}
			}
		}
		
	}
	
	public static EditTextFocusListener getFocusListener(String text)
	{
		return new EditTextFocusListener(text);
	}

}
