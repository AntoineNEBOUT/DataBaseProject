package dataBasePackage;

import utilPackage.FileProcessorClass;

import java.util.HashMap;

public class dataBaseAddUser
{
    public dataBaseAddUser()
    {
    }

    public static boolean add(HashMap<String, String> usersList, HashMap<String, String> adminsList, String tabName, String userName, String password, String pathToFiles)
    {
        boolean isCreated = false;

        try
        {
            if(tabName.equals("admin"))
            {
                adminsList.put(userName, password);

                FileProcessorClass.writeFile(pathToFiles.concat("\\adminsList.txt"), userName.concat("-").concat(password).concat("-"), "append");
                // System.out.println("admin ok");

                isCreated = true;
            }
            else
            {
                usersList.put(userName, password);

                FileProcessorClass.writeFile(pathToFiles.concat("\\usersList.txt"), userName.concat("-").concat(password).concat("-"), "append");

                isCreated = true;
            }
        }
        catch (Exception e)
        {
            System.out.println("Exception : ".concat(e.getMessage()));
        }

        return isCreated;
    }
}
