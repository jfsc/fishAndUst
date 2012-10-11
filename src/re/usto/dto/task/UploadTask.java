package re.usto.dto.task;

import re.usto.dto.dialog.FileDialogOptions;
import re.usto.dto.helper.Constants;
import re.usto.dto.object.User;

import android.content.Context;

public class UploadTask extends BaseTask
{
	public UploadTask(Context context, String dialogTitle, OnTaskComplete onComplete)
	{
		super(context, dialogTitle, onComplete);
	}

	public static RequestUrl getUploadRequestUrl(String filePath, User user, String uploadPath)
	{
		RequestUrl param = new RequestUrl(Constants.SERVER_URL + Constants.REQUEST_UPLOADFILE_URL);
		param = new RequestUrl(Constants.SERVER_URL + Constants.REQUEST_UPLOADFILE_URL);
		param.addParam(Constants.PARAM_FILE, filePath);
		int indexLastSlash = filePath.lastIndexOf("/");
		String fileName = filePath.substring(indexLastSlash + 1);
		param.addParam(Constants.PARAM_FILENAME, fileName);
		param.addParam(Constants.PARAM_UPLOAD_PATH, uploadPath);
		param.addParam(Constants.PARAM_LOGIN, user.login);
		param.addParam(Constants.PARAM_AUTHCODE, user.authenticationCode);
		param.addParam(Constants.PARAM_LENGTH, String.valueOf(FileDialogOptions.readResultFileLength(filePath)));
		return param;
	}
}
