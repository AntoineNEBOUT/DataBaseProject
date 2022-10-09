package dataBasePackage;

import java.util.HashMap;

public class dataBaseCheckIdentity
{
    public dataBaseCheckIdentity()
    {
    }

    public static boolean check(HashMap<String, String> usersList, String userName, String password)
    {
        boolean isIdentified = false;

        // System.out.println(usersList.get(userName));

        if(usersList.containsKey(userName))
            if(usersList.get(userName).equals(password))
                isIdentified = true;

        return isIdentified;
    }
}
