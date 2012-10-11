package re.usto.dto.object;

import re.usto.dto.helper.Constants;

public class File implements IFile
{
	
	private String mName;
	private Path mPath;

	public File(String name, Path path)
	{
		this.mName = name;
		this.mPath = path;
	}

	public String getName()
	{
		return this.mName;
	}

	public FileType getType()
	{
		return FileType.FILE;
	}

	public Path getPath()
	{
		return this.mPath;
	}

	public String getExtension()
	{
		int point = this.mName.indexOf(Constants.FILENAME_POINT_EXTENSION_STRING);
		return this.mName.substring(point + 1);
	}

}
