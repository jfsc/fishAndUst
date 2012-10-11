package re.usto.dto.dialog;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

import re.usto.dto.R;
import android.app.ListActivity;
import android.os.Bundle;
import android.os.Environment;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class BaseFileDialog extends ListActivity {
	
	protected View mSelectedItem = null;
	// @see https://code.google.com/p/android-file-dialog/issues/detail?id=3
    // @see http://twigstechtips.blogspot.com.au/2011/11/for-my-app-moustachify-everything-i-was.html
    // This is purely a data storage class for saving information between rotations
    protected class LastConfiguration {
        public String m_strCurrentPath;

        public LastConfiguration(String currentPath) {
            this.m_strCurrentPath = currentPath;
        }
    }
    
    protected void selectView(View v)
	{
		if(mSelectedItem != null)
	    {
	    	mSelectedItem.setBackgroundColor(this.getResources().getColor(R.color.BackgroundFileList));
	    }
    	mSelectedItem = v;
    	mSelectedItem.setBackgroundColor(this.getResources().getColor(R.color.BackgroundFileListSelected));
	}
    
	protected static final String ITEM_KEY = "key";
	protected static final String ITEM_IMAGE = "image";
	
	public static final String PATH_ROOT = "/";
	public static final String PATH_SDCARD = Environment.getExternalStorageDirectory().getAbsolutePath();

	protected FileDialogOptions options;
	
	// TODO: This needs a cleanup
	protected List<String> path = null;
	protected TextView myPath;

	protected Button selectButton;

	protected LinearLayout layoutSelect;
	protected InputMethodManager inputManager;
	protected String parentPath;
	protected String currentPath = PATH_ROOT;

	protected File selectedFile;
	protected HashMap<String, Integer> lastPositions = new HashMap<String, Integer>();

	/**
	 * Called when the activity is first created.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setResult(RESULT_CANCELED, getIntent());

		setContentView(R.layout.file_dialog_main);
		myPath = (TextView) findViewById(R.id.path);

		// Read options
		options = new FileDialogOptions(getIntent());
		
		// Hide the titlebar if needed
		if (options.titlebarForCurrentPath) {
		    myPath.setVisibility(View.GONE);
		}

		inputManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

		selectButton = (Button) findViewById(R.id.fdButtonSelect);
		selectButton.setEnabled(false);
		selectButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (selectedFile != null) {
				    returnFilename(selectedFile.getPath());
				}
			}
		});

		layoutSelect = (LinearLayout) findViewById(R.id.fdLinearLayoutSelect);


		// If the New button is disabled and it's one click select, hide the selection layout.
		if (!options.allowCreate && options.oneClickSelect) {
		    layoutSelect.setVisibility(View.GONE);
		}


		// Try to restore current path after screen rotation
		LastConfiguration lastConfiguration = (LastConfiguration) getLastNonConfigurationInstance();

		if (lastConfiguration != null) {
		    getDir(lastConfiguration.m_strCurrentPath);
		}
		// New instance of FileDialog
		else {
		    File file = new File(options.currentPath);
		    
		    if (file.isDirectory() && file.exists()) {
		        getDir(options.currentPath);
		    }
		    else {
		        getDir(PATH_ROOT);
		    }
		}
	}

	protected void getDir(String dirPath) {

		boolean useAutoSelection = dirPath.length() < currentPath.length();

		Integer position = lastPositions.get(parentPath);

		getDirImpl(dirPath);

		if (position != null && useAutoSelection) {
			getListView().setSelection(position);
		}

	}

	protected void getDirImpl(final String dirPath) {
		currentPath = dirPath;

		path = new ArrayList<String>();
		ArrayList<HashMap<String, Object>> mList = new ArrayList<HashMap<String, Object>>();

		File f = new File(currentPath);
		File[] files = f.listFiles();
		
		// Null if file is not a directory
		if (files == null) {
			currentPath = PATH_ROOT;
			f = new File(currentPath);
			files = f.listFiles();
		}
		
		// Sort files by alphabet and ignore casing
		Arrays.sort(files);

		if (options.titlebarForCurrentPath) {
		    this.setTitle(currentPath);
		}
		else {
		    myPath.setText(getText(R.string.location) + ": " + currentPath);
		}

		/*
         * http://stackoverflow.com/questions/5090915/show-songs-from-sdcard
         * http://developer.android.com/reference/android/os/Environment.html
         * http://stackoverflow.com/questions/5453708/android-how-to-use-environment-getexternalstoragedirectory
         */
        if (currentPath.equals(PATH_ROOT)) {
            boolean mounted = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
            
            if (mounted) {
                addItem(mList, PATH_SDCARD + "(SD Card)", this.options.iconSDCard);
                path.add(PATH_SDCARD);
            }
        }
		
		if (!currentPath.equals(PATH_ROOT)) {
			addItem(mList, "/ (Root folder)", this.options.iconUp);
			path.add(PATH_ROOT);

			addItem(mList, "../ (Parent folder)", this.options.iconUp);
			path.add(f.getParent());
			parentPath = f.getParent();
		}
		
		TreeMap<String, String> dirsMap = new TreeMap<String, String>();
		TreeMap<String, String> dirsPathMap = new TreeMap<String, String>();
		TreeMap<String, String> filesMap = new TreeMap<String, String>();
		TreeMap<String, String> filesPathMap = new TreeMap<String, String>();

		for (File file : files) {
			if (file.isDirectory()) {
				String dirName = file.getName();
				dirsMap.put(dirName, dirName);
				dirsPathMap.put(dirName, file.getPath());
			} else {
				filesMap.put(file.getName(), file.getName());
				filesPathMap.put(file.getName(), file.getPath());
			}
		}
		
		path.addAll(dirsPathMap.tailMap("").values());
		path.addAll(filesPathMap.tailMap("").values());

		for (String dir : dirsMap.tailMap("").values()) {
			addItem(mList, dir, this.options.iconFolder);
		}

		for (String file : filesMap.tailMap("").values()) {
			addItem(mList, file, this.options.iconFile);
		}

		SimpleAdapter fileList = new SimpleAdapter(this, mList,
            R.layout.file_dialog_row,
            new String[] { ITEM_KEY, ITEM_IMAGE },
            new int[] { R.id.fdrowtext, R.id.fdrowimage }
        );
      
		fileList.notifyDataSetChanged();

		setListAdapter(fileList);
	}

	protected void addItem(ArrayList<HashMap<String, Object>> mList, String fileName, int imageId) {
		HashMap<String, Object> item = new HashMap<String, Object>();
		item.put(ITEM_KEY, fileName);
		item.put(ITEM_IMAGE, imageId);
		mList.add(item);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			selectButton.setEnabled(false);

				if (!currentPath.equals(PATH_ROOT)) {
					getDir(parentPath);
				} else {
					return super.onKeyDown(keyCode, event);
				}

			return true;
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}

	protected void returnFilename(String filepath) {
	    this.options.currentPath = currentPath;
	    this.options.selectedFile = filepath;

	    setResult(RESULT_OK, options.createResultIntent());
	    finish();
	}
	
	// Remember the information when the screen is just about to be rotated.
	// This information can be retrieved by using getLastNonConfigurationInstance()
	public Object onRetainNonConfigurationInstance() {
	    return new LastConfiguration(this.currentPath);
	}
}
