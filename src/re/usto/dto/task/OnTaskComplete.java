package re.usto.dto.task;

import re.usto.dto.object.Folder;

public interface OnTaskComplete
{
	void onTaskComplete(String result);
	void setListView(Folder folder);
}
