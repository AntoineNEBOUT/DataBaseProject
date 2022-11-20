package dataBasePackage;

import utilPackage.EncryptionProcessorClass;
import utilPackage.FileProcessorClass;

import java.util.HashMap;
import java.util.StringTokenizer;

public class dataBaseGetAll {
    public dataBaseGetAll()
    {
    }

    public static String get(HashMap<String, HashMap> tabStorage, String tabName, boolean encryption, String pathToFiles)
    {
        String contentToReturn2 = "null";

        if(tabStorage.containsKey(tabName))
        {
            String mapContent = FileProcessorClass.openFile(pathToFiles.concat("\\").concat(tabName).concat(".txt"));
            StringTokenizer st = new StringTokenizer(mapContent, "-");

            String mapContentToReturn = tabName.concat(" content : ");

            while(st.hasMoreTokens())
            {
                String name = st.nextToken();

                if(encryption)
                    name = EncryptionProcessorClass.decrypt(name, "mo");

                if(st.hasMoreTokens())
                {
                    String content = st.nextToken();
                    if(encryption)
                        content = EncryptionProcessorClass.decrypt(content, "mo");

                    mapContentToReturn = mapContentToReturn.concat("\n> ").concat(name).concat(" => ");
                    mapContentToReturn = mapContentToReturn.concat(content);
                }
            }
            contentToReturn2 = mapContentToReturn;
        }

        return contentToReturn2;
    }
}
