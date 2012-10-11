package re.usto.dto.object;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.util.Log;

import re.usto.dto.helper.Constants;
import re.usto.dto.object.IFile.FileType;

public class Files
{
	private static HashMap<String, Folder> FOLDER_LIST = new HashMap<String, Folder>();
	private static Folder MAIN_FOLDER = new Folder(Constants.FOLDER_ROOT, null);
	
	public static ArrayList<Path> getPaths(String folderPath)
	{
		ArrayList<Path> pathList = new ArrayList<Path>();
		Folder folder = FOLDER_LIST.get(folderPath);
		if(folder != null)
		{
			for(IFile f : folder.getList())
			{
				pathList.add(f.getPath());
			}
		}
		return pathList;
	}
	
	public static Folder getFolder(String path)
	{
		return FOLDER_LIST.get(path);
	}
	
	public static Folder getMainFolder()
	{
		return MAIN_FOLDER;
	}
	
	public static Folder createFolders(List<Path> paths)
	{
		MAIN_FOLDER = new Folder(Constants.FOLDER_ROOT, null);
		FOLDER_LIST = new HashMap<String, Folder>();
		for (Path path : paths)
		{
			Folder folder = createFolders(MAIN_FOLDER, path.getDirectoryName());
			folder.addFile(new File(path.getExibitionName(), path));
		}
		
		return MAIN_FOLDER;
	}
	
	private static Folder createFolders(final Folder mainFolder, String path)
	{
		Folder folder = FOLDER_LIST.get(path);
		if(folder == null)
		{
			boolean root = mainFolder.getName().equals(path) || path == null || path != null && path.length() == 0; 
			if(root)
			{
				FOLDER_LIST.put(path, mainFolder);
				return mainFolder;
			}
			if(path != null && path.length() > 0)
			{
				String pathTemp = null;
				String[] split = path.split(Constants.FOLDER_SLASH);
				if(split == null || split != null && split.length == 0)
				{
					split = new String[]{path};
				}
				for(String name : split)
				{
					if(pathTemp != null)
					{
						pathTemp += Constants.FOLDER_SLASH + name;
					}
					else
					{
						pathTemp = name;
					}
					Log.d("TAG", "FOLDER: " + pathTemp);
					if(folder == null)
					{
						folder = FOLDER_LIST.get(pathTemp);
						if(folder == null)
						{
							folder = new Folder(name, pathTemp);
							mainFolder.addFile(folder);
							FOLDER_LIST.put(pathTemp, folder);
						}
					}
					else
					{
						Folder subFolder = FOLDER_LIST.get(pathTemp);
						if(subFolder == null)
						{
							subFolder = new Folder(name, pathTemp);
							folder.addFile(subFolder);
							FOLDER_LIST.put(pathTemp, subFolder);
						}
						folder = subFolder;
					}
				}
			}
		}
		return folder;
	}
	
	public static ArrayList<Path> getAllFilesFromFolder(Folder folder)
	{
		ArrayList<Path> list = new ArrayList<Path>();
		ArrayList<IFile> files = folder.getList();
		for(IFile file : files)
		{
			if(file.getType() == FileType.FILE)
			{
				list.add(file.getPath());
			}
			else
			{
				ArrayList<Path> tempList = getAllFilesFromFolder((Folder)file);
				for(Path p : tempList)
				{
					list.add(p);
				}
			}
		}
		return list;
	}
}
