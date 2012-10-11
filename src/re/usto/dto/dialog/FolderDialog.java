package re.usto.dto.dialog;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.TreeMap;

import re.usto.dto.R;
import re.usto.dto.helper.Constants;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Environment;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class FolderDialog extends BaseFileDialog
{
	private long mLastTimeMillis = 0;

	@Override
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

		for (File file : files) {
			if (file.isDirectory()) {
				String dirName = file.getName();
				dirsMap.put(dirName, dirName);
				dirsPathMap.put(dirName, file.getPath());
			}
		}
		
		path.addAll(dirsPathMap.tailMap("").values());

		for (String dir : dirsMap.tailMap("").values()) {
			addItem(mList, dir, this.options.iconFolder);
		}

		SimpleAdapter fileList = new SimpleAdapter(this, mList,
            R.layout.file_dialog_row,
            new String[] { ITEM_KEY, ITEM_IMAGE },
            new int[] { R.id.fdrowtext, R.id.fdrowimage }
        );
      
		fileList.notifyDataSetChanged();

		setListAdapter(fileList);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id)
	{
		File file = new File(path.get(position));

		if (!file.exists())
		{
			new AlertDialog.Builder(this)
					.setIcon(R.drawable.ic_launcher)
					.setTitle("Does not exist.")
					.setMessage(file.getName())
					.setPositiveButton("OK",
							new DialogInterface.OnClickListener()
							{
								public void onClick(DialogInterface dialog,
										int which)
								{
								}
							}).show();
			return;
		}

		selectView(v);

		selectedFile = null;
		v.setSelected(false);
		selectButton.setEnabled(false);

		if (file.isDirectory())
		{
			if (file.canRead())
			{
				// Save the scroll position so users don't get confused when
				// they come back
				lastPositions.put(currentPath, this.getListView()
						.getFirstVisiblePosition());
				
				if(hasDoubleClick())
				{
					getDir(path.get(position));
					selectedFile = null;
					v.setSelected(false);
					selectButton.setEnabled(false);
				}
				else
				{
					selectedFile = file;
					v.setSelected(true);
					selectButton.setEnabled(true);
	
					if (options.oneClickSelect)
					{
						selectButton.performClick();
					}
				}

			} else
			{
				new AlertDialog.Builder(this)
						.setIcon(R.drawable.ic_launcher)
						.setTitle(
								"[" + file.getName() + "] "
										+ getText(R.string.cant_read_folder))
						.setPositiveButton("OK",
								new DialogInterface.OnClickListener()
								{

									public void onClick(DialogInterface dialog,
											int which)
									{

									}
								}).show();
			}
		}
	}

	private boolean hasDoubleClick()
	{
		boolean doubleClick = (System.currentTimeMillis() - mLastTimeMillis ) < Constants.DOUBLE_CLICK_INTERVAL;
		mLastTimeMillis = System.currentTimeMillis();
		return doubleClick;
	}

}