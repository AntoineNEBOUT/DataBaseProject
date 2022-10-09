package dataBasePackage;

import utilPackage.FileProcessorClass;

import java.util.HashMap;

public class dataBaseCreateTab
{
    public dataBaseCreateTab()
    {
    }

    public static boolean create(HashMap<String, HashMap> tabStorage, String tabName, String pathToFiles)
    {
        boolean isCreated = false;

        try
        {
            /*
            indexStorage.put(tabName, String.valueOf(index));
            index++;
            System.out.println(index);
            mapStorage.add(new HashMap<String, String>());
             */

            tabStorage.put(tabName, new HashMap<String, String>());

            FileProcessorClass.createFile(pathToFiles.concat("\\").concat(tabName).concat(".txt"));
            FileProcessorClass.writeFile(pathToFiles.concat("\\mapList.txt"), tabName.concat("-"), "append");

            isCreated = true;
        }
        catch (Exception e)
        {
            System.out.println("Exception : ".concat(e.getMessage()));
            isCreated = false;
        }

        return isCreated;
    }
}
