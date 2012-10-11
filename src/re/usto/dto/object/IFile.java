package re.usto.dto.object;

public interface IFile
{
	enum FileType { FOLDER, FILE };
	
	String getName();
	FileType getType();
	Path getPath();
	String getExtension();
}
