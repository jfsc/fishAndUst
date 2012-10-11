package re.usto.dto.screen;

import java.util.ArrayList;

import re.usto.dto.R;
import re.usto.dto.dialog.FileDialogOptions;
import re.usto.dto.helper.Constants;
import re.usto.dto.helper.Message;
import re.usto.dto.object.Files;
import re.usto.dto.object.Folder;
import re.usto.dto.object.IFile;
import re.usto.dto.object.Path;
import re.usto.dto.object.User;
import re.usto.dto.object.IFile.FileType;
import re.usto.dto.task.DeleteRequestTask;
import re.usto.dto.task.FileListRequestTask;
import re.usto.dto.task.GetRequestTask;
import re.usto.dto.task.OnTaskComplete;
import re.usto.dto.task.PostRequestTask;
import re.usto.dto.task.RequestUrl;
import re.usto.dto.task.SearchRequestTask;
import re.usto.dto.task.ShareRequestTask;
import re.usto.dto.task.UploadFileTask;
import re.usto.dto.task.UploadFolderTask;
import re.usto.dto.task.UploadTask;
import re.usto.dto.view.FileAdapter;
import re.usto.dto.view.FileListItemClick;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class Dashboard extends Activity implements OnTaskComplete
{
	private static String CURRENT_PATH = Constants.FOLDER_ROOT;
	private FileListRequestTask mListFilesTask;
	private UploadFileTask mUploadFileTask;
	private UploadFolderTask mUploadFolderTask;
	private GetRequestTask mLogoutTask;
	private DeleteRequestTask mDeleteTask;
	private SearchRequestTask mSearchTask;
	private ShareRequestTask mShareTask;
	
	private User mUser;
	private FileDialogOptions mFileDialog;
	private FileDialogOptions mFolderDialog;
	private int currentFilesShared = 0;
	private String lastSharedLogin = "";
	
	private OnTaskComplete mOnUploadComplete;
	private OnTaskComplete mOnLogoutComplete;
	private OnTaskComplete mOnShareComplete;
	
	private ListView mListView;
	
	private Button mBtnAdd, mBtnDelete, mBtnSearch, mBtnUpload, mBtnShare,
			mBtnBack;
	private EditText mEditSearch;
	private FileAdapter mFileAdapter;
	
	@Override
	public void onCreate(Bundle bundle)
	{
		super.onCreate(bundle);
		super.setContentView(R.layout.dashboard);
		configure();
		refreshFileList();

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if(keyCode == KeyEvent.KEYCODE_BACK)
		{
			if (isCurrentPathRoot())
			{
				createLogoutDialog();
			}
			else
			{
				this.backPath();
			}
		}
		
		return false;
	}
	
	private void logout()
	{
		RequestUrl post = new RequestUrl(Constants.SERVER_URL + Constants.REQUEST_LOGOUT_URL);
		mLogoutTask = new GetRequestTask(this, Constants.DIALOG_LOGOUT_TITLE, mOnLogoutComplete);
		post.addParam(Constants.PARAM_AUTHCODE, mUser.authenticationCode);
		mLogoutTask.execute(new RequestUrl[]{ post });
	}
	
	private boolean isCurrentPathRoot()
	{
		return CURRENT_PATH.equals(Constants.FOLDER_ROOT);
	}

	private void configure()
	{
		Bundle bundle = getIntent().getExtras();
		this.mUser = (User) bundle.getSerializable(Constants.PARAM_LOGIN_USER);
		mFileAdapter = null;
		
		mOnUploadComplete = new OnTaskComplete()
		{

			public void onTaskComplete(String result)
			{

				if (result != null)
				{
					Message.debug(result);
					Dashboard.this.createErrorUploadDialog();
				}
				
				Dashboard.this.refreshFileList();
			}

			public void setListView(Folder folder)
			{
				// TODO Auto-generated method stub
				
			}
		};

		mOnLogoutComplete = new OnTaskComplete()
		{
			
			public void onTaskComplete(String result)
			{
				Dashboard.this.finish();				
			}

			public void setListView(Folder folder)
			{
				// TODO Auto-generated method stub
				
			}
		};
		
		mOnShareComplete = new OnTaskComplete()
		{
			
			public void onTaskComplete(String result)
			{
				//ironic, is not it? -- talk to fish@usto.re
				if(result.equals("success")){
					currentFilesShared++;
					ArrayList<Path> paths = FileListItemClick.getSelectedFiles();
					if(currentFilesShared<paths.size()){
						ShareFilesToLogin(lastSharedLogin,currentFilesShared);
					}else{
						createFileSharedSuc();
						FileListItemClick.clearSelectedFiles();
					}
					
				}
				
			}

			public void setListView(Folder folder)
			{
				// TODO Auto-generated method stub
				
			}
		};

		
		// DO THE ADD FILES WITH FOLDER DIALOG
		this.mBtnAdd = (Button) findViewById(R.id.btnAdd);
		this.mBtnDelete = (Button) findViewById(R.id.btnDelete);
		this.mBtnSearch = (Button) findViewById(R.id.btnSearch);
		this.mBtnUpload = (Button) findViewById(R.id.btnUpload);
		this.mBtnShare = (Button) findViewById(R.id.btnShare);
		this.mBtnBack = (Button) findViewById(R.id.btnBack);

		this.mEditSearch = (EditText) findViewById(R.id.searchText);
		
		this.mBtnSearch.setOnClickListener(new View.OnClickListener()
		{
			
			public void onClick(View v)
			{
				Dashboard.this.search();				
			}
		});
		
		this.mBtnBack.setOnClickListener(new View.OnClickListener()
		{
			
			public void onClick(View v)
			{
				if(!Dashboard.this.isCurrentPathRoot())
				{
					Dashboard.this.backPath();
				}else{
					Dashboard.this.refreshFileList();
				}
			}

		});
		
		this.mBtnAdd.setOnClickListener(new View.OnClickListener()
		{

			public void onClick(View v)
			{

				mFolderDialog = new FileDialogOptions(Dashboard.this.getIntent());
				Intent intent = mFolderDialog
						.createFolderDialogIntent(Dashboard.this);
				Dashboard.this.startActivityForResult(intent,
						Constants.REQUEST_CODE_FILE_ADD_FOLDER_DIALOG);
			}
		});

		this.mBtnUpload.setOnClickListener(new View.OnClickListener()
		{

			public void onClick(View v)
			{

				mFileDialog = new FileDialogOptions(Dashboard.this.getIntent());
				Intent intent = mFileDialog
						.createFileDialogIntent(Dashboard.this);
				Dashboard.this.startActivityForResult(intent,
						Constants.REQUEST_CODE_FILE_UPLOAD_DIALOG);
			}
		});
		
		this.mBtnDelete.setOnClickListener(new View.OnClickListener()
		{
			
			public void onClick(View v)
			{
				
				Dashboard.this.createDeleteDialog();
			}
		});
		
		this.mBtnShare.setOnClickListener(new View.OnClickListener()
		{
			
			public void onClick(View v)
			{
				
				if(FileListItemClick.getSelectedFiles().size() >0)
				{
					Intent intent = new Intent(Dashboard.this, ShareFile.class);
					
					Dashboard.this.startActivityForResult(intent, Constants.REQUEST_CODE_SHARE_FILE_DIALOG);
				}else{
					createPleaseSelectAFileDialog();
				}
				
			}
		});

		
		mListView = (ListView) findViewById(R.id.fileList);
		FileListItemClick clickListener = new FileListItemClick(this, mUser, this);
		mListView.setOnItemClickListener(clickListener);
		mListView.setOnItemLongClickListener(clickListener);
	}

	private void backPath()
	{
		String[] split = CURRENT_PATH.split(Constants.FOLDER_SLASH);
		if(split != null)
		{
			int total = split.length;
			if(total == 1)
			{
				setListView(Files.getMainFolder());
			}
			else if(total > 1)
			{
				String parentPath = "";
				for(int i = 0; i < total - 1; i++)
				{
					parentPath += split[i];
					if(i + 1 < total - 1)
					{
						parentPath += Constants.FOLDER_SLASH;
					}
				}
				setListView(Files.getFolder(parentPath));
			}
		}
	}

	private void search()
	{
		String searchText = this.mEditSearch.getText().toString();
		this.mEditSearch.getText().clear();
		if(searchText.length() > 0)
		{
			mSearchTask = new SearchRequestTask(this,
					Constants.DIALOG_LISTING_FILES_TITTLE, this);
			RequestUrl post = new RequestUrl(Constants.SERVER_URL
					+ Constants.REQUEST_SEARCH_URL);
			post.addParam(Constants.PARAM_SEARCH, searchText);
			post.addParam("", Constants.REQUEST_SEARCH_FILE_SEPARATOR);
			post.addParam(Constants.PARAM_AUTHCODE, mUser.authenticationCode);
			mSearchTask.execute(new RequestUrl[]
			{ post });
			
		}
		
	}
	
	private void createDownloadCompleteDialog()
	{

		createAlertDialog(Constants.DIALOG_DOWNLOAD_TITLE,
				Constants.DIALOG_DOWNLOAD_MESSAGE_COMPLETE,
				Constants.DIALOG_DOWNLOAD_BUTTON_OK);
	}

	private void createErrorUploadDialog()
	{
		createAlertDialog(Constants.DIALOG_ERROR_UPLOAD_TITLE,
				Constants.DIALOG_ERROR_UPLOAD_MESSAGE,
				Constants.DIALOG_ERROR_BUTTON_OK);
	}
	
	private void createDeleteDialog()
	{
		DialogInterface.OnClickListener ok = new DialogInterface.OnClickListener()
		{

			public void onClick(DialogInterface dialog, int which)
			{
				Dashboard.this.deleteSelectedFile();
				dialog.dismiss();
			}
			
		};
		
		DialogInterface.OnClickListener cancel = new DialogInterface.OnClickListener()
		{

			public void onClick(DialogInterface dialog, int which)
			{
				dialog.dismiss();
			}
			
		};
		
		createAlertDialog(Constants.DIALOG_DELETE_TITLE,
				Constants.DIALOG_DELETE_MESSAGE, Constants.DIALOG_DELETE_BUTTON_OK, Constants.DIALOG_DELETE_BUTTON_CANCEL, ok, cancel);
	}
	
	private void createLogoutDialog()
	{
		DialogInterface.OnClickListener ok = new DialogInterface.OnClickListener()
		{

			public void onClick(DialogInterface dialog, int which)
			{
				Dashboard.this.logout();
				dialog.dismiss();
			}
			
		};
		
		DialogInterface.OnClickListener cancel = new DialogInterface.OnClickListener()
		{

			public void onClick(DialogInterface dialog, int which)
			{
				dialog.dismiss();
			}
			
		};
		
		createAlertDialog(Constants.DIALOG_LOGOUT_TITLE,
				Constants.DIALOG_LOGOUT_MESSAGE, Constants.DIALOG_LOGOUT_BUTTON_OK, Constants.DIALOG_LOGOUT_BUTTON_CANCEL, ok, cancel);
	}
	
	private void createNoResultsDialog()
	{
		createAlertDialog(Constants.DIALOG_SEARCH_TITLE,
				Constants.DIALOG_SEARCH_NO_RESULTS,
				Constants.DIALOG_SEARCH_BUTTON_OK);
	}
	
	private void createPleaseSelectAFileDialog()
	{
		createAlertDialog(Constants.DIALOG_SHARE_TITLE,
				Constants.DIALOG_SHARE_SELECT_A_FILE_PLEASE,
				Constants.DIALOG_SHARE_BUTTON_OK);
	}
	
	private void createFileSharedSuc()
	{
		
		Message shareToast = new Message(this);
		shareToast.show(Constants.DIALOG_SHARE_SHARE_SUC);
		
	}

	private void deleteSelectedFile()
	{
		ArrayList<Path> paths = FileListItemClick.getSelectedFiles();
		int total = paths.size();
		Log.d("TAG", "SELECTED FILES TOTAL: " + total);
		if(total > 0)
		{
			mDeleteTask = new DeleteRequestTask(this, Constants.DIALOG_DELETE_TITLE, this);
			RequestUrl[] request = new RequestUrl[paths.size()];
			String url = Constants.SERVER_URL + Constants.REQUEST_DELETEFILE_URL;
			int count = 0;
			for(Path path : paths) 
			{
				RequestUrl post = new RequestUrl(url);
				post.addParam(Constants.PARAM_FILEID, String.valueOf(path.getId()));
				post.addParam(Constants.PARAM_AUTHCODE3,mUser.authenticationCode );
				request[count] = post;
				count++;
			}
			mDeleteTask.execute(request);
			FileListItemClick.clearSelectedFiles();
		}
	}
	private void ShareFilesToLogin(String loginShare, int currentSharedFile)
	{
		ArrayList<Path> paths = FileListItemClick.getSelectedFiles();
		int total = paths.size();
		if(total > 0)
		{
			String FirstFileId = Integer.toString(paths.get(currentSharedFile).getId());
			lastSharedLogin = loginShare;
			mShareTask = new ShareRequestTask(this, Constants.DIALOG_SHARE_TITLE, mOnShareComplete);
			RequestUrl post = new RequestUrl(Constants.SERVER_URL + Constants.REQUEST_SHAREFILE_URL);
			
			post.addParam(Constants.PARAM_AUTHCODE, mUser.authenticationCode);
			post.addParam(Constants.PARAM_FILEID, FirstFileId);
			post.addParam(Constants.PARAM_LOGIN_SHARE, lastSharedLogin);
			mShareTask.execute(new RequestUrl[]{ post });
			
		}
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

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{

		String file = FileDialogOptions.readResultFileName(data);
		if (requestCode == Constants.REQUEST_CODE_FILE_UPLOAD_DIALOG)
		{
			/*Message.debug("FILE...");
			file = mFileDialog.readResultFileName(data);
			Message.debug("UPLOADING FILE: " + file);*/
		}
		else if (requestCode == Constants.REQUEST_CODE_FILE_ADD_FOLDER_DIALOG)
		{
			/*Message.debug("FOLDER...");
			if(data != null)
				file = mFolderDialog.readResultFolder(data);*/
			
		}
		else if(requestCode == Constants.REQUEST_CODE_SHARE_FILE_DIALOG)
		{
			if(data != null){
				String loginShare = data.getExtras().getString(Constants.PARAM_LOGIN_SHARE);
				ShareFilesToLogin(loginShare,currentFilesShared);
			}
			
		}
		if(file != null)
		{
			
			RequestUrl param;
			if (requestCode == Constants.REQUEST_CODE_FILE_UPLOAD_DIALOG)
			{
				mUploadFileTask = new UploadFileTask(this,
						Constants.DIALOG_REQUEST_UPLOAD_TITLE, mOnUploadComplete);
	
				param = UploadTask.getUploadRequestUrl(file, mUser, CURRENT_PATH);
	
				mUploadFileTask.execute(new RequestUrl[]
				{ param });
			} else if (requestCode == Constants.REQUEST_CODE_FILE_ADD_FOLDER_DIALOG)
			{
				mUploadFolderTask = new UploadFolderTask(this,file,mUser, CURRENT_PATH,
						Constants.DIALOG_REQUEST_UPLOAD_TITLE, mOnUploadComplete);
				
				mUploadFolderTask.execute(new RequestUrl[]
						{ null });
			}
		}
	}
	
	private void refreshFileList()
	{
		mListFilesTask = new FileListRequestTask(this,
				Constants.DIALOG_LISTING_FILES_TITTLE, this);
		RequestUrl post = new RequestUrl(Constants.SERVER_URL
				+ Constants.REQUEST_LISTFILES_URL);
		post.addParam(Constants.PARAM_PEER_ID, mUser.id);
		mListFilesTask.execute(new RequestUrl[]
		{ post });
	}

	public void onTaskComplete(String result)
	{
		if(result != null)
		{
			Log.d("TAG", result);
			
			if(result.equals(Constants.TASK_DOWNLOAD_COMPLETE_RESULT))
			{
				Dashboard.this.createDownloadCompleteDialog();
			}
			else if(result.equals(Constants.ORDER_SET_FILE_LIST))
			{
				setListView(Files.getMainFolder());
			}
			else if(result.equals(Constants.ORDER_REFRESH_LIST))
			{
				Dashboard.this.refreshFileList();
			}else if(result.equals(Constants.EMPTY_SEARCH))
			{
				createNoResultsDialog();
			}
			
		}
	}

	public static String getCurrentPath()
	{
		return CURRENT_PATH;
	}

	public void setListView(Folder folder)
	{
		Log.d("TAG", folder.toString());
		CURRENT_PATH = folder.getName();
		Log.d("TAG", "SET FOLDER: " + CURRENT_PATH);
		mFileAdapter = new FileAdapter(this, R.layout.listview_item_row, folder.getList());
		this.mListView.setAdapter(mFileAdapter);
		
	}
}