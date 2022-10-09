package dataBasePackage;

import utilPackage.FileProcessorClass;

import java.util.HashMap;

public class dataBaseSaveData
{
    public dataBaseSaveData()
    {
    }

    public static boolean save(HashMap<String, HashMap> tabStorage, String tabName, String userName, String content, String pathToFiles)
    {
        boolean isSaved = false;

        if(tabStorage.containsKey(tabName))
        {
            // FileProcessorClass.writeFile(System.getProperty("user.dir").concat("\\").concat(String.valueOf(indexTemp).concat(".txt")), userName.concat("-").concat(content).concat("-"), "append");

            // int indexTemp = Integer.parseInt(indexStorage.get(tabName));
            // mapStorage.get(indexTemp).put(userName, content);

            tabStorage.get(tabName).put(userName, content);

            FileProcessorClass.writeFile(pathToFiles.concat("\\").concat(tabName).concat(".txt"), userName.concat("-").concat(content).concat("-"), "append");

            isSaved = true;
        }

        return isSaved;
    }
}
