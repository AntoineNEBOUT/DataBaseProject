package utilPackage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LogProcessorClass
{
    public LogProcessorClass(String path)
    {
        toLogFile = Paths.get(path);;
    }

    public void writeLog(String line, boolean printInTerminal)
    {
        try
        {
            String spaceInLog = "\n-------------------------------";

            Charset c = Charset.forName("UTF-8"); // encodage
            StandardOpenOption mode = StandardOpenOption.APPEND; // mode

            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();

            if(!Files.isRegularFile(toLogFile))
                Files.createFile(toLogFile);

            BufferedWriter bfw = Files.newBufferedWriter(toLogFile, c, mode);

            String finalLine = line.concat(" | TIME : ").concat(dtf.format(now));

            bfw.write(finalLine.concat("\n"));
            if(printInTerminal)
                System.out.println(finalLine);

            bfw.close();
        }
        catch(IOException e)
        {
            System.out.println("IOException : "+e.getMessage());
        }
        catch(Exception e)
        {
            System.out.println("Exception : "+e.getMessage());
        }
    }

    public Path getPath()
    {
        return toLogFile;
    }

    public String getPathToString()
    {
        return toLogFile.toString();
    }

    public boolean isThereALogFile()
    {
        return Files.exists(toLogFile);
    }

    public static boolean isThereALogFileAt(Path path)
    {
        return Files.exists(path);
    }

    public String getLog()
    {
        String logContent = "";
        String logContentLine = "";
        boolean canReadLine = true;

        try
        {
            BufferedReader bfr = Files.newBufferedReader(toLogFile);

            while(canReadLine)
            {
                logContentLine = bfr.readLine();

                if(logContentLine != null)
                    logContent += logContentLine.concat("\n");
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

        return logContent;
    }

    public boolean createLogFile()
    {
        try
        {
            Files.createFile(toLogFile);
            return true;
        }
        catch (IOException e)
        {
            System.out.println("LogProcessorClass Error on 'createLogFile' : IOException : "+e);
            return false;
        }
    }

    public boolean deleteLogFile()
    {
        try
        {
            Files.deleteIfExists(toLogFile);
            return true;
        }
        catch (IOException e)
        {
            System.out.println("LogProcessorClass Error on 'deleteLogFile' : IOException : "+e);
            return false;
        }
    }

    private static Path toLogFile;
}
