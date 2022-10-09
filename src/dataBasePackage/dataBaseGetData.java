package dataBasePackage;

import utilPackage.EncryptionProcessorClass;

import java.util.HashMap;

public class dataBaseGetData
{
    public dataBaseGetData()
    {
    }

    public static String get(HashMap<String, HashMap> tabStorage, String tabName, String userName, boolean encryption)
    {
        String contentToReturn2 = "null";

        if(tabStorage.containsKey(tabName))
        {
            // String index2 = indexStorage.get(tabName);
            // System.out.println("get 3 | index : "+index2);

            // String contentToReturn = (String) mapStorage.get(Integer.parseInt("0")).get(userName);
            String contentToReturn = (String) tabStorage.get(tabName).get(userName);

            if(encryption)
                contentToReturn2 = EncryptionProcessorClass.decrypt(contentToReturn, "mo");
            else
                contentToReturn2 = contentToReturn;
        }

        return contentToReturn2;
    }
}
