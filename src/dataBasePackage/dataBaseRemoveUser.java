package dataBasePackage;

import utilPackage.FileProcessorClass;

import java.util.HashMap;
import java.util.StringTokenizer;

public class dataBaseRemoveUser
{
    public dataBaseRemoveUser()
    {
    }

    public static boolean remove(HashMap<String, String> usersList, HashMap<String, String> adminsList, String tabName, String userName, String pathToFiles)
    {
        boolean isRemoved = false;

        try
        {
            if(tabName.equals("admin"))
            {
                String usersListFileContent = FileProcessorClass.openFile(pathToFiles.concat("\\adminsList.txt"));
                StringTokenizer st = new StringTokenizer(usersListFileContent, "-");
                FileProcessorClass.deleteFile(pathToFiles.concat("\\adminsList.txt"));
                FileProcessorClass.createFile(pathToFiles.concat("\\adminsList.txt"));

                while(st.hasMoreTokens())
                {
                    String userTemp = st.nextToken().trim();

                    if(!userTemp.equals("") && !userTemp.equals(userName) && !userTemp.equals(adminsList.get(userName)))
                    {
                        FileProcessorClass.writeFile(pathToFiles.concat("\\adminsList.txt"), userTemp.concat("-"), "append");
                    }
                }

                adminsList.remove(userName);
                // System.out.println("admin ok 2");

                isRemoved = true;
            }
            else
            {
                String usersListFileContent = FileProcessorClass.openFile(pathToFiles.concat("\\usersList.txt"));
                StringTokenizer st = new StringTokenizer(usersListFileContent, "-");
                FileProcessorClass.deleteFile(pathToFiles.concat("\\usersList.txt"));
                FileProcessorClass.createFile(pathToFiles.concat("\\usersList.txt"));

                while(st.hasMoreTokens())
                {
                    String userTemp = st.nextToken().trim();

                    if(!userTemp.equals("") && !userTemp.equals(userName) && !userTemp.equals(usersList.get(userName)))
                    {
                        FileProcessorClass.writeFile(pathToFiles.concat("\\usersList.txt"), userTemp.concat("-"), "append");
                    }
                }

                usersList.remove(userName);

                isRemoved = true;
            }
        }
        catch (Exception e)
        {
            System.out.println("Exception : ".concat(e.getMessage()));
            isRemoved = false;
        }

        return isRemoved;
    }
}
