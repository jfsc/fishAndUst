package re.usto.dto.task;

import java.io.File;
import java.util.ArrayList;

import re.usto.dto.helper.Message;
import re.usto.dto.helper.Proxy;
import re.usto.dto.helper.StringUtils;
import re.usto.dto.object.User;
import android.content.Context;

public class UploadFolderTask extends UploadTask {

	private String mFolderPath;
	private User mUser;
	private String mCurrentPath;
	
	public UploadFolderTask(Context context, String folderPath, User user, String currentPath, String dialogTitle,
			OnTaskComplete onComplete) {
		super(context, dialogTitle, onComplete);
		
		mFolderPath = folderPath;
		mUser = user;
		mCurrentPath = currentPath;
	}
	
	@Override
	protected String doInBackground(RequestUrl... nullParams)
	{
		if(mFolderPath != null && mUser != null && mCurrentPath != null)
		{
			ArrayList<String> paths = getAllFilePaths(new ArrayList<String>(), mFolderPath);
			ArrayList<RequestUrl> params = new ArrayList<RequestUrl>();
			for(String path : paths)
			{
				String fileName=StringUtils.strip(path, "._");
				params.add(UploadTask.getUploadRequestUrl(fileName, mUser, mCurrentPath));
			}
			for(RequestUrl post : params)
			{
				String resp = Proxy.upload(post);
				if(StringUtils.hasError(resp))
				{
					return resp;
				}
				
			}
		}
		return null;
	}

	private ArrayList<String> getAllFilePaths(ArrayList<String> arrayList, String path) {
		
		File file = new File(path);
		File[] list = file.listFiles();
		for(File f : list)
		{
			if(f.isDirectory())
			{
				ArrayList<String> tempList = getAllFilePaths(new ArrayList<String>(), f.getPath());
				for(String pf : tempList)
				{
					System.out.println("PATH DOS FOLDER"+f.getPath());
					arrayList.add(pf);
				}
			}
			else
			{
				System.out.println("PATH DOS FILES"+f.getPath());
				arrayList.add(f.getPath());
			}
		}
		
		return arrayList;
	}
}
