package re.usto.dto.view;

import java.util.List;

import re.usto.dto.R;
import re.usto.dto.helper.*;
import re.usto.dto.object.IFile;
import re.usto.dto.object.IFile.FileType;
import re.usto.dto.object.Path;
import re.usto.dto.screen.Dashboard;
import android.app.Activity;
import android.content.Context;
import android.graphics.Path.FillType;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class FileAdapter extends ArrayAdapter<IFile>
{

	private Context mContext;
	private int mTextViewResourceId;
	private List<IFile> mFileRowObjects;
	private Display mDisplay;

	public FileAdapter(Context context, int textViewResourceId,
			List<IFile> objects)
	{
		super(context, textViewResourceId, objects);

		mContext = context;
		mTextViewResourceId = textViewResourceId;
		mFileRowObjects = objects;
		mDisplay = ((WindowManager) this.mContext.getSystemService(this.mContext.WINDOW_SERVICE)).getDefaultDisplay();

	}
	
	@Override
	public int getCount() 
	{
		return mFileRowObjects != null ? mFileRowObjects.size() : 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		View row = convertView;
		FileRowHolder holder = null;

		if (row == null)
		{
			LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
			row = inflater.inflate(mTextViewResourceId, parent, false);

			holder = new FileRowHolder();
			holder.imgIcon = (ImageView) row.findViewById(R.id.imgIcon);
			holder.txtTitle = (TextView) row.findViewById(R.id.txtTitle);
			holder.txtSize = (TextView) row.findViewById(R.id.txtSize);
			holder.txtModification = (TextView) row
					.findViewById(R.id.txtModification);

			row.setTag(holder);
		}
		else
		{
			holder = (FileRowHolder) row.getTag();
		}
		
		IFile fileRow = mFileRowObjects.get(position);
		fileRow.getPath().setView(row);
		
		int width = mDisplay.getWidth() - Constants.FILE_NAME_LENGTH_OF_SIZE_MORE_DATE;
		String fileName = StringUtils.getLimitedFileName(fileRow.getPath().getFileName(), width);
		holder.txtTitle.setText(fileName);
		if(fileRow.getType() == FileType.FILE)
		{
			holder.txtSize.setText(fileRow.getPath().getSize());
			holder.txtModification.setText(fileRow.getPath().getLastUpdate());
		}
		holder.imgIcon.setImageResource(getFileTypeImage(fileRow.getExtension()));

		return row;
	}
	
	private int getFileTypeImage(String extension)
	{
		if(extension != null)
		{
			if(Constants.FILE_EXTENSIONS_IMAGE.contains(extension))
			{
				return R.drawable.subfolder_img;
			}
			else if(Constants.FILE_EXTENSIONS_MUSIC.contains(extension))
			{
				return R.drawable.subfolder_mp3;
			}
			else if(Constants.FILE_EXTENSIONS_VIDEO.contains(extension))
			{
				return R.drawable.subfolder_video;
			}
			else if(Constants.FILE_EXTENSIONS_PDF.contains(extension))
			{
				return R.drawable.subfolder_pdf;
			}
			else if(Constants.FILE_EXTENSIONS_ZIPFILE.contains(extension))
			{
				return R.drawable.zipfolder;
			}
			else if(Constants.FILE_EXTENSIONS_FILE.contains(extension))
			{
				return R.drawable.file;
			}
			else
			{
				return R.drawable.subfolder_txt;
			}
		}
		
		return R.drawable.folder;
	}
	
	public List<IFile> getFileRowObjects()
	{
		return mFileRowObjects;
	}

	static class FileRowHolder
	{
		ImageView imgIcon;
		TextView txtTitle;
		TextView txtSize;
		TextView txtModification;
	}
}
