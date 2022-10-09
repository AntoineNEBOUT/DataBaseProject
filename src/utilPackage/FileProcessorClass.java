package utilPackage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class FileProcessorClass
{
    public FileProcessorClass(String path)
    {
        filePath = Paths.get(path);
        filePathString = path;
    }

    public static String openFile(String path)
    {
        String fileContent = "";
        String fileContentLine = "";
        boolean canReadLine = true;

        try
        {
            BufferedReader bfr = Files.newBufferedReader(Paths.get(path));

            while(canReadLine)
            {
                fileContentLine = bfr.readLine();

                if(fileContentLine != null && fileContentLine != "")
                    fileContent += fileContentLine.concat("\n");
                else
                    canReadLine = false;
            }
            bfr.close();
        }
        catch(IOException e)
        {
            System.out.println("IOException : "+e.getMessage());
        }
        catch(Exception e)
        {
            System.out.println("Exception : "+e.getMessage());
        }

        return fileContent;
    }

    public String openFile()
    {
        return openFile(filePathString);
    }

    public static String getLine(String path)
    {
        String fileContentLine = "";
        boolean canReadLine = true;

        try
        {
            BufferedReader bfr = Files.newBufferedReader(Paths.get(path));

            while(canReadLine)
            {
                fileContentLine = bfr.readLine();

                if(fileContentLine != null && fileContentLine != "")
                    canReadLine = false;
            }
            bfr.close();
        }
        catch(IOException e)
        {
            System.out.println("IOException : "+e.getMessage());
        }
        catch(Exception e)
        {
            System.out.println("Exception : "+e.getMessage());
        }

        return fileContentLine.concat("\n");
    }

    public static void writeFile(String path, String text, String mode)
    {
        StandardOpenOption option;

        if(mode.equals("write"))
        {
            try
            {
                option= StandardOpenOption.WRITE; // mode
                Charset c = Charset.forName("UTF-8"); // encodage
                BufferedWriter bfw = Files.newBufferedWriter(Paths.get(path), c, option);

                bfw.write(text);

                bfw.close();
            }
            catch(IOException e)
            {
                System.out.println("IOException : "+e.getMessage());
            }
        }
        else if(mode.equals("append"))
        {
            try
            {
                option= StandardOpenOption.APPEND; // mode
                Charset c = Charset.forName("UTF-8"); // encodage
                BufferedWriter bfw = Files.newBufferedWriter(Paths.get(path), c, option);

                bfw.write(text);

                bfw.close();
            }
            catch(IOException e)
            {
                System.out.println("IOException : "+e.getMessage());
            }
        }
    }

    public void writeFile(String text, String mode)
    {
        writeFile(filePathString, text, mode);
    }

    public static boolean createFile(String path)
    {
        try
        {
            Files.createFile(Paths.get(path));
            return true;
        }
        catch (IOException e)
        {
            System.out.println("FileProcessorClass Error on 'createFile' : IOException : "+e);
            return false;
        }
    }

    public boolean createFile()
    {
        return createFile(filePathString);
    }

    public static boolean deleteFile(String path)
    {
        try
        {
            Files.deleteIfExists(Paths.get(path));
            return true;
        }
        catch (IOException e)
        {
            System.out.println("FileProcessorClass Error on 'deleteFile' : IOException : "+e);
            return false;
        }
    }

    public boolean deleteFile()
    {
        return deleteFile(filePathString);
    }

    public static boolean isFileExist(String path)
    {
        return Files.exists(Paths.get(path));
    }

    public boolean isFileExist()
    {
        return Files.exists(filePath);
    }

    private static Path filePath;
    private static String filePathString;
}
