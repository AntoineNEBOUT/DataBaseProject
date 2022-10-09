package dataBasePackage;

import utilPackage.FileProcessorClass;

import java.util.HashMap;
import java.util.StringTokenizer;

public class dataBaseDeleteTab
{
    public dataBaseDeleteTab()
    {
    }

    public static boolean delete(HashMap<String, HashMap> tabStorage, String tabName, String pathToFiles)
    {
        boolean isDeleted = false;

        try
        {
            String tabsListFileContent = FileProcessorClass.openFile(pathToFiles.concat("\\mapList.txt"));
            StringTokenizer st = new StringTokenizer(tabsListFileContent, "-");
            FileProcessorClass.deleteFile(pathToFiles.concat("\\mapList.txt"));
            FileProcessorClass.createFile(pathToFiles.concat("\\mapList.txt"));

            while(st.hasMoreTokens())
            {
                String mapTemp = st.nextToken().trim();

                if(!mapTemp.equals("") && !mapTemp.equals(tabName))
                {
                    FileProcessorClass.writeFile(pathToFiles.concat("\\mapList.txt"), mapTemp.concat("-"), "append");
                }
            }

            // mapStorage.remove(indexStorage.get(tabName));
            // indexStorage.remove(tabName);

            tabStorage.remove(tabName);
            FileProcessorClass.deleteFile(pathToFiles.concat("\\").concat(tabName).concat(".txt"));

            isDeleted = true;
        }
        catch (Exception e)
        {
            System.out.println("Exception : ".concat(e.getMessage()));
            isDeleted = false;
        }

        return isDeleted;
    }
}
