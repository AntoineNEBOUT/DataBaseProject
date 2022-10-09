import dataBasePackage.*;
import utilPackage.EncryptionProcessorClass;
import utilPackage.FileProcessorClass;
import utilPackage.LogProcessorClass;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

public class Main
{
    static int attemptToReconnect = 0;
    static boolean emergencyCloseSocket = false;
    static boolean socketCanBeRunning = true;
    static int index = 0;
    // static HashMap<String, String> indexStorage = new HashMap<String, String>();
    //commentaire de texte 2
    static HashMap<String, HashMap> tabStorage = new HashMap<String, HashMap>();
    static HashMap<String, String> users = new HashMap<String, String>();
    static HashMap<String, String> admins = new HashMap<String, String>();
    static ArrayList<HashMap> mapStorage = new ArrayList<HashMap>();
    static String portString = null;
    static boolean encryption = false;
    static String pathToStorage = System.getProperty("user.dir");

    public static void main(String[] args)
    {
        ServerSocket ss = null;

        launchDataBase(encryption);

        try
        {
            if(portString != null && !portString.equals(""))
            {
                int port = Integer.parseInt(portString);
                ss = new ServerSocket(port);
            }
            else
                return;
        }
        catch (IOException e)
        {
            System.out.println("CRASH IOException : "+e.getMessage());
        }

        Main mainApp = new Main();
        mainApp.run(ss);
    }

    public void run(ServerSocket serversocket)
    {
        System.out.println("====================================\nDataBase is started".concat("\nListen on port ")
                .concat(portString).concat("\nSend <user name>;<user password>;help for help with requests").concat("\nStorage on ").concat(pathToStorage));

        if(encryption)
            System.out.println("Encryption enabled\n====================================");
        else
            System.out.println("Encryption disabled\n====================================");

        while(true)
        {
            try
            {
                if(emergencyCloseSocket)
                    serversocket.close();

                if(socketCanBeRunning)
                {
                    Socket soc = serversocket.accept();

                    InputStream inputStream = soc.getInputStream();
                    BufferedReader inReader = new BufferedReader(new InputStreamReader(inputStream));

                    OutputStream output = soc.getOutputStream();

                    PrintWriter writer = new PrintWriter(output, true);

                    LogProcessorClass log = new LogProcessorClass(pathToStorage.concat("\\log.txt"));

                    while(true)
                    {
                        if(soc.isBound())
                        {
                            String receivedMsg =  inReader.readLine();
                            StringTokenizer st = new StringTokenizer(receivedMsg, ";");
                            String mUserName = "";
                            String mPassword = "";
                            String mTypeOfRequest = "";
                            String mMapName = "";
                            String content = "";
                            String content2 = "";

                            // to add or remove user, request can be : adminUserName;adminPassword;aUser;admin/notAdmin;userNameToAdd/Remove;passwordToAdd/Remove

                            if(encryption)
                            {
                                mUserName = EncryptionProcessorClass.encrypt(st.nextToken(), "mo");
                                mPassword = EncryptionProcessorClass.encrypt(st.nextToken(), "mo");
                                mTypeOfRequest = st.nextToken();
                                mMapName = "";
                                content = "";
                                content2 = "";

                                if(st.hasMoreTokens())
                                    mMapName = EncryptionProcessorClass.encrypt(st.nextToken(), "mo");

                                if(st.hasMoreTokens())
                                    content = EncryptionProcessorClass.encrypt(st.nextToken(), "mo");

                                if(st.hasMoreTokens())
                                    content2 = EncryptionProcessorClass.encrypt(st.nextToken(), "mo");
                            }
                            else
                            {
                                mUserName = st.nextToken();
                                mPassword = st.nextToken();
                                mTypeOfRequest = st.nextToken();
                                mMapName = "";
                                content = "";
                                content2 = "";

                                if(st.hasMoreTokens())
                                    mMapName = st.nextToken();

                                if(st.hasMoreTokens())
                                    content = st.nextToken();

                                if(st.hasMoreTokens())
                                    content2 = st.nextToken();
                            }

                            if(mTypeOfRequest.equals("isOnline"))
                            {
                                writer.println("dataBaseIsOnline;".concat(portString));
                                soc.close();
                            }
                            else if(mTypeOfRequest.equals("get"))
                            {
                                if(dataBaseCheckIdentity.check(users, mUserName, mPassword) || dataBaseCheckIdentity.check(admins, mUserName, mPassword))
                                {
                                    String returnedContent = "null";

                                    if(dataBaseCheckIdentity.check(admins, mUserName, mPassword) && !content.equals("") && !content2.equals(""))
                                    {
                                        if(dataBaseCheckIdentity.check(users, content, content2) || dataBaseCheckIdentity.check(admins, content, content2))
                                            returnedContent = dataBaseGetData.get(tabStorage, mMapName, content, encryption);
                                    }
                                    else
                                        returnedContent = dataBaseGetData.get(tabStorage, mMapName, mUserName, encryption);

                                    if(!returnedContent.equals("null"))
                                    {
                                        writer.println("successfulGetting:".concat(returnedContent));
                                        soc.close();

                                        log.writeLog("request:GET by '".concat(mUserName).concat("' -> SUCCESS"), true);
                                    }
                                    else
                                    {
                                        writer.println("gettingFail:probably 'tab name' or 'user name' are wrong");
                                        soc.close();

                                        log.writeLog("request:GET by '".concat(mUserName).concat("' -> FAIL"), true);
                                    }
                                }
                                else
                                {
                                    writer.println("User is not identified");
                                    soc.close();
                                }
                            }
                            else if(mTypeOfRequest.equals("save"))
                            {
                                if(dataBaseCheckIdentity.check(users, mUserName, mPassword) || dataBaseCheckIdentity.check(admins, mUserName, mPassword))
                                {
                                    if(dataBaseSaveData.save(tabStorage, mMapName, mUserName, content, pathToStorage))
                                    {
                                        writer.println("dataSaved:".concat(content));
                                        soc.close();

                                        log.writeLog("request:SAVE by '".concat(mUserName).concat("' -> SUCCESS | data : ".concat(content)), true);
                                    }
                                    else
                                    {
                                        writer.println("dataNotSaved:probably 'tab name' is wrong or unknown");
                                        soc.close();

                                        log.writeLog("request:SAVE by '".concat(mUserName).concat("' -> FAIL | data : ".concat(content)), true);
                                    }
                                }
                                else
                                {
                                    writer.println("User is not identified");
                                    soc.close();
                                }
                            }
                            else if(mTypeOfRequest.equals("aUser"))
                            {
                                if(dataBaseCheckIdentity.check(admins, mUserName, mPassword))
                                {
                                    if(dataBaseAddUser.add(users, admins, mMapName, content, content2, pathToStorage))
                                    {
                                        writer.println("userAdded:".concat(content).concat(":").concat(content2));
                                        soc.close();

                                        log.writeLog("request:ADD USER by '".concat(mUserName).concat("' -> SUCCESS | user : ".concat(content)).concat(" / ").concat(content2), true);
                                    }
                                    else
                                    {
                                        writer.println("UserNotAdded:probably 'users file' is missing");
                                        soc.close();

                                        log.writeLog("request:ADD USER by '".concat(mUserName).concat("' -> FAIL | user : ".concat(content)).concat(" / ").concat(content2), true);
                                    }
                                }
                                else
                                {
                                    writer.println("User already exist");
                                    soc.close();
                                }
                            }
                            else if(mTypeOfRequest.equals("rUser"))
                            {
                                if(dataBaseCheckIdentity.check(admins, mUserName, mPassword))
                                {
                                    if(dataBaseRemoveUser.remove(users, admins, mMapName, content, pathToStorage))
                                    {
                                        writer.println("userRemoved:".concat(mUserName).concat(":").concat(mPassword));
                                        soc.close();

                                        log.writeLog("request:REMOVE USER by '".concat(mUserName).concat("' -> SUCCESS | user : ".concat(content).concat(" / ").concat(content2)), true);
                                    }
                                    else
                                    {
                                        writer.println("UserNotRemoved:probably 'users file' is missing or user is wrong");
                                        soc.close();

                                        log.writeLog("request:REMOVE USER by '".concat(mUserName).concat("' -> FAIL | user : ".concat(content).concat(" / ").concat(content2)), true);                                    }
                                }
                                else
                                {
                                    writer.println("User is not identified");
                                    soc.close();
                                }
                            }
                            else if(mTypeOfRequest.equals("create"))
                            {
                                if(dataBaseCheckIdentity.check(admins, mUserName, mPassword))
                                {
                                    if(dataBaseCreateTab.create(tabStorage, mMapName, pathToStorage))
                                    {
                                        index++;
                                        writer.println("mapCreated:".concat(mMapName));
                                        soc.close();

                                        log.writeLog("request:CREATE by '".concat(mUserName).concat("' -> SUCCESS | tab : ".concat(mMapName)), true);
                                    }
                                    else
                                    {
                                        writer.println("mapNotCreated:probably 'tab name' is incorrect or 'map list' file is missing");
                                        soc.close();

                                        log.writeLog("request:CREATE by '".concat(mUserName).concat("' -> FAIL | tab : ".concat(mMapName)), true);
                                    }
                                }
                                else
                                {
                                    writer.println("User is not identified");
                                    soc.close();
                                }
                            }
                            else if(mTypeOfRequest.equals("delete"))
                            {
                                if(dataBaseCheckIdentity.check(admins, mUserName, mPassword))
                                {
                                    if(dataBaseDeleteTab.delete(tabStorage, mMapName, pathToStorage))
                                    {
                                        writer.println("mapDeleted:".concat(mMapName));
                                        soc.close();

                                        log.writeLog("request:DELETE by '".concat(mUserName).concat("' -> SUCCESS | tab : ".concat(mMapName)), true);
                                    }
                                    else
                                    {
                                        writer.println("mapNotDeleted:probably 'tab name' is incorrect or 'map list' file is missing");
                                        soc.close();

                                        log.writeLog("request:DELETE by '".concat(mUserName).concat("' -> FAIL | tab : ".concat(mMapName)), true);
                                    }
                                }
                                else
                                {
                                    writer.println("User is not identified");
                                    soc.close();
                                }
                            }
                            else if(mTypeOfRequest.equals("help"))
                            {
                                /*
                                - users and admins :
		                            * get data : <userName>;<password>;get;<tabName>
		                            * save data : <userName>;<password>;save;<tabName>;<text to save>
		                            * help : <userName>;<password>;help

	                            - admins only :
		                            * add user (classic user / admin) : <adminName>;<adminPassword>;aUser;notAdmin;<userNameToAdd>;<passwordToAdd>
		                            / <adminName>;<adminPassword>;aUser;admin;<adminNameToAdd>;<adminPasswordToAdd>
		                            * remove user (classic user / admin) : <adminName>;<adminPassword>;rUser;notAdmin;<userNameToRemove>;<passwordToRemove>
		                            / <adminName>;<adminPassword>;rUser;admin;<adminNameToRemove>;<adminPasswordToRemove>
		                            * create tab : <adminName>;<adminPassword>;create;<tabNameToCreate>
		                            * delete tab : <adminName>;<adminPassword>;delete;<tabNameToDelete>
                                */
                                writer.println("help:\n\t- users and admins :\n\t\t* get data : <userName>;<password>;get;<tabName>\n\t\t* save data : <userName>;<password>;save;<tabName>;<text to save>" +
                                        "\n\t\t* help : <userName>;<password>;help\n\n\t- admins only :\n\t\t* add user (classic user / admin) : <adminName>;<adminPassword>;aUser;notAdmin;<userNameToAdd>;<passwordToAdd>" +
                                        "\n\t\t/ <adminName>;<adminPassword>;aUser;admin;<adminNameToAdd>;<adminPasswordToAdd>\n\t\t* remove user (classic user / admin) : " +
                                        "<adminName>;<adminPassword>;rUser;notAdmin;<userNameToRemove>;<passwordToRemove>\n\t\t/ <adminName>;<adminPassword>;rUser;admin;<adminNameToRemove>;<adminPasswordToRemove>" +
                                        "\n\t\t* create tab : <adminName>;<adminPassword>;create;<tabNameToCreate>\n\t\t* delete tab : <adminName>;<adminPassword>;delete;<tabNameToDelete>");
                                soc.close();
                            }
                            else if(mTypeOfRequest.equals("stop"))
                            {

                                if(dataBaseCheckIdentity.check(admins, mUserName, mPassword))
                                {
                                    writer.println("DataBase is offline");
                                    soc.close();

                                    log.writeLog("request:STOP by '".concat(mUserName).concat("' -> SUCCESS"), true);

                                    System.exit(0);
                                }
                                else
                                {
                                    writer.println("User is not identified");
                                    soc.close();

                                    log.writeLog("request:STOP by '".concat(mUserName).concat("' -> FAIL"), true);
                                }
                            }
                            else if(mTypeOfRequest.equals("getLog"))
                            {
                                if(dataBaseCheckIdentity.check(admins, mUserName, mPassword))
                                {
                                    writer.println("successfulLogGetting:\n".concat(log.getLog()));
                                    soc.close();

                                    log.writeLog("request:GET LOG by '".concat(mUserName).concat("' -> SUCCESS"), true);
                                }
                                else
                                {
                                    writer.println("User is not identified");
                                    soc.close();

                                    log.writeLog("request:GET LOG by '".concat(mUserName).concat("' -> FAIL"), true);
                                }
                            }
                            else
                            {
                                System.out.println("Non !");
                            }
                        }
                        else
                        {
                            System.out.println("Disconected ! ");
                        }
                    }
                }
                else
                {
                    emergencyCloseSocket = false;
                    socketCanBeRunning = true;
                    run(serversocket);
                }
            }
            catch (Exception e)
            {
                if(attemptToReconnect <= 2 && attemptToReconnect >= 0)
                {
                    attemptToReconnect = 1;
                }
                else if(attemptToReconnect > 2)
                {
                    emergencyCloseSocket = true;
                    socketCanBeRunning = false;;
                }
            }
        }
    }

    private static void launchDataBase(boolean encryption2)
    {
        String optionsList = FileProcessorClass.openFile(pathToStorage.concat("\\options.txt"));

        if(!optionsList.equals(""))
        {
            StringTokenizer st4 = new StringTokenizer(optionsList, "-");

            pathToStorage = st4.nextToken();
            portString = st4.nextToken();
            if(st4.nextToken().equals("encryption"))
                encryption = true;
            else
                encryption = false;
        }

        index = 0;
        String mapList = FileProcessorClass.openFile(pathToStorage.concat("\\mapList.txt"));
        int tempIndex = 0;

        if(!mapList.equals(""))
        {
            StringTokenizer st = new StringTokenizer(mapList, "-");

            while(st.hasMoreTokens())
            {
                String mapNameTemp = st.nextToken().trim();

                if(!mapNameTemp.equals("") && mapNameTemp != null)
                {
                    /*
                    indexStorage.put(mapNameTemp, String.valueOf(index));
                    index++;
                    mapStorage.add(new HashMap<String, String>());
                    */

                    tabStorage.put(mapNameTemp, new HashMap<String, String>());

                    // tempIndex = Integer.parseInt(indexStorage.get(mapNameTemp));
                    // String fileContent = FileProcessorClass.openFile(System.getProperty("user.dir").concat("\\").concat(String.valueOf(tempIndex).concat(".txt")));

                    String fileContent = FileProcessorClass.openFile(pathToStorage.concat("\\").concat(mapNameTemp).concat(".txt"));

                    StringTokenizer st2 = new StringTokenizer(fileContent, "-");

                    while(st2.hasMoreTokens())
                    {
                        String name = st2.nextToken();

                        if(st2.hasMoreTokens())
                        {
                            String content = st2.nextToken();

                            // mapStorage.get(tempIndex).put(name, content);

                            tabStorage.get(mapNameTemp).put(name, content);
                        }
                    }

                    tempIndex++;
                }
            }
        }

        String usersList = FileProcessorClass.openFile(pathToStorage.concat("\\usersList.txt"));

        if(!usersList.equals(""))
        {
            StringTokenizer st3 = new StringTokenizer(usersList, "-");

            while(st3.hasMoreTokens())
            {
                String name = st3.nextToken();

                if(st3.hasMoreTokens())
                {
                    String password = st3.nextToken();

                    users.put(name, password);
                }
            }
        }

        String adminsList = FileProcessorClass.openFile(pathToStorage.concat("\\adminsList.txt"));

        if(!adminsList.equals(""))
        {
            StringTokenizer st4 = new StringTokenizer(adminsList, "-");

            while(st4.hasMoreTokens())
            {
                String name = st4.nextToken();

                if(st4.hasMoreTokens())
                {
                    String password = st4.nextToken();

                    admins.put(name, password);
                    if(name.equals("admin") && password.equals("1234"))
                        System.out.println("!!!\nWARNING : default admin user was not removed !\n!!!");
                    // System.out.println("Admin name/password : "+name+" / "+password);
                }
            }
        }
    }
}