package re.usto.dto.object;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.util.Log;

import re.usto.dto.helper.*;

public class Folder implements IFile
{
	private String mName;
	private ArrayList<IFile> mFileList;
	private Path mPath;
	
	public Folder(String name, String path)
	{
		this.mName = name == null ? Constants.FOLDER_ROOT : name;
		this.mFileList = new ArrayList<IFile>();
		String pathName = path != null ? path : this.mName;
		this.mPath = new Path(pathName, 0);
	}
	
	public IFile getFolder(String path)
	{
		return Files.getFolder(path);
	}

	public String getName()
	{
		return this.mName;
	}
	
	public int getFileListSize()
	{
		return this.mFileList.size();
	}

	public IFile getFile(int index)
	{
		return hasIndex(index) ? this.mFileList.get(index) : null;
	}
	
	private boolean hasIndex(int index)
	{
		return index >= 0 && index < getFileListSize();
	}

	public void addFile(IFile file)
	{
		this.mFileList.add(file);
	}
	
	public boolean removeFile(IFile file)
	{
		return this.mFileList.remove(file);
	}
	
	public IFile removeFile(int index)
	{
		return hasIndex(index) ? this.mFileList.remove(index) : null;
	}

	public FileType getType()
	{
		return FileType.FOLDER;
	}

	public Path getPath()
	{
		return this.mPath;
	}

	public ArrayList<IFile> getList()
	{
		return mFileList;
	}
	
	public String toString()
	{
		String r = "";
		String enter = "\r\n";
		r += getName() + enter;
		r += getType() == FileType.FILE ? "FILE" : "FOLDER";
		r += enter;
		if(getType() == FileType.FOLDER)
		{
			r += " ->>"  + enter;
			for(IFile file : mFileList)
			{
				r += "   " + file.getName() + " - " + (file.getType() == FileType.FILE ? "FILE" : "FOLDER");
				r += enter;
			}
			r += "------------------------"  + enter;
		}
		return r;
	}

	public String getExtension()
	{
		return null;
	}
}
