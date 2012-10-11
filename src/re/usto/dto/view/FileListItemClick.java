package re.usto.dto.view;


import java.util.ArrayList;

import re.usto.dto.R;
import re.usto.dto.helper.Constants;
import re.usto.dto.object.Folder;
import re.usto.dto.object.Files;
import re.usto.dto.object.IFile;
import re.usto.dto.object.IFile.FileType;
import re.usto.dto.object.Path;
import re.usto.dto.object.User;
import re.usto.dto.task.DownloadTask;
import re.usto.dto.task.OnTaskComplete;
import re.usto.dto.task.RequestUrl;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

public class FileListItemClick implements OnItemClickListener,
		OnItemLongClickListener {
	private Context mContext;
	private OnTaskComplete mCallback;
	private DownloadTask mDownloadTask;
	private User mUser;
	private long mLastTimeClick;
	private static ArrayList<Path> SELECTED_FILES = new ArrayList<Path>();
	
	public enum SelectFileAction {ADD, DELETE};
	
	public static ArrayList<Path> getSelectedFiles()
	{
		return SELECTED_FILES;
	}
	
	public static void clearSelectedFiles()
	{
		SELECTED_FILES.clear();
	}

	public FileListItemClick(Context context, User user, OnTaskComplete callback) {
		mContext = context;
		mCallback = callback;
		mUser = user;
		mLastTimeClick = System.currentTimeMillis();
	}

	public void onItemClick(AdapterView<?> adapter, View layout,
			int hierarchyPosition, long position) {
		
		IFile fileRow = (IFile) adapter.getItemAtPosition(hierarchyPosition);
		
		if(fileRow.getType() == FileType.FILE)
		{
			markSelectPath(fileRow);
		}
		else
		{
			long thisTime = System.currentTimeMillis();
			if(thisTime - mLastTimeClick < Constants.DOUBLE_CLICK_INTERVAL)
			{
				mCallback.setListView((Folder) fileRow);
			}
			else
			{
				markSelectPath(fileRow);
			}
			mLastTimeClick = thisTime;
		}
	}
	
	private void markSelectPath(IFile fileRow)
	{
		Path path = fileRow.getPath();
		if(path.getView() != null)
		{
			if(path.isSelected())
			{
				path.setSelected(false);
				sendToSelectedFiles(fileRow, SelectFileAction.DELETE);
				path.getView().setBackgroundColor(mContext.getResources().getColor(R.color.BackgroundFileList));
			}
			else
			{
				path.setSelected(true);
				sendToSelectedFiles(fileRow, SelectFileAction.ADD);
				path.getView().setBackgroundColor(mContext.getResources().getColor(R.color.BackgroundFileListSelected));
			}
		}
	}
	
	

	private void sendToSelectedFiles(IFile fileRow, SelectFileAction action)
	{
		if(fileRow.getType() == FileType.FOLDER)
		{
			Folder folder = (Folder)fileRow;
			ArrayList<IFile> list = folder.getList();
			for(IFile file : list)
			{
				sendToSelectedFiles(file, action);
					
			}
		}
		else
		{
			if(action == SelectFileAction.ADD)
			{
				Log.d("TAG", "SELECTING FILE - ADD: " + fileRow.getName());
				SELECTED_FILES.add(fileRow.getPath());
			}
			else
			{
				Log.d("TAG", "SELECTING FILE - DELETE: " + fileRow.getName());
				SELECTED_FILES.remove(fileRow.getPath());
			}
		}
	}

	public boolean onItemLongClick(AdapterView<?> adapter, View layout,
			int hierarchyPosition, long position) {

		IFile file = (IFile)adapter.getItemAtPosition(hierarchyPosition);
		createDownloadDialog(file);
		return false;
	}

	private void createDownloadDialog(final IFile fileRow) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this.mContext);
		builder.setTitle(Constants.DIALOG_DOWNLOAD_TITLE)
				.setMessage(
						Constants.DIALOG_DOWNLOAD_MESSAGE + "("
								+ fileRow.getName() + ")")
				.setCancelable(false)
				.setPositiveButton(Constants.DIALOG_DOWNLOAD_BUTTON_OK,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {

								FileListItemClick.this.downloadFile(fileRow);

							}
						})
				.setNegativeButton(Constants.DIALOG_DOWNLOAD_BUTTON_CANCEL,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.dismiss();
							}
						});
		AlertDialog alert = builder.create();
		alert.show();
	}

	protected void downloadFile(IFile fileRow) 
	{
		mDownloadTask = new DownloadTask(
				mContext,
				Constants.DIALOG_DOWNLOAD_TITLE_EXECUTING,
				mCallback);
		
		RequestUrl post;
		String url = Constants.SERVER_URL + Constants.REQUEST_DOWNLOADFILE_URL;
		if(fileRow.getType() == FileType.FOLDER)
		{
			ArrayList<Path> pathList = Files.getAllFilesFromFolder((Folder)fileRow);
			Log.d("TAG", "TOTAL PATHLIST: " + pathList.size());
			RequestUrl[] postList = new RequestUrl[pathList.size()];
			int count = 0;
			for(Path p : pathList)
			{
				post = new RequestUrl(url);
				addPathParams(post, p);
				postList[count] = post;
				count++;
			}
			mDownloadTask.execute(postList);
		}
		else
		{
			post = new RequestUrl(url);
			addPathParams(post, fileRow.getPath());
			mDownloadTask.execute(new RequestUrl[]{ post });
		}
		
	}
	
	private void addPathParams(RequestUrl post, Path path)
	{
		post.addParam(Constants.PARAM_LOGIN, mUser.login);
		post.addParam(Constants.PARAM_FILENAME, path.getFileName());
		post.addParam(Constants.PARAM_FILEID, Integer.toString(path.getId()));
		post.addParam(Constants.PARAM_FILEPATH, path.getDirectoryName());
		post.addParam(Constants.PARAM_AUTHCODE2,mUser.authenticationCode);
		post.addParam(Constants.PARAM_AUTHCODE3,mUser.authenticationCode);
	}
}
