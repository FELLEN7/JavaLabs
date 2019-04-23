import javafx.util.Pair;

import javax.annotation.processing.SupportedSourceVersion;
import java.io.*;
import java.util.concurrent.Callable;

public class FileCallable implements Callable<Pair<String, Integer>>{

    private File file = null;

    public File getFile(){
        return file;
    }

    public FileCallable setFile(File file){
        this.file = file;
        return this;
    }

    private int CountForInFile() throws IOException{
        if (file == null){
            System.out.println("Before you must set directory with file!");
            return 0;
        }
        if (file.isDirectory()){
            System.out.println("It must be not directory!");
            return 0;
        }
        int count = 0;
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line;
        StringBuffer textFile = new StringBuffer();
        while ((line = reader.readLine()) != null) {
            /*MY REGEXP!!!!*/
            count += (line + "\0").split("(?s)(?>\\/\\*(?>(?:(?>[^*]+)|\\*(?!\\/))*)\\*\\/)").length - 1;
            textFile.append(line.replaceAll("(?s)(?>\\/\\*(?>(?:(?>[^*]+)|\\*(?!\\/))*)\\*\\/)", ""));
        }
        String filename = file.getName();
        writeNewFile(filename, "DirWithoutComments", textFile);


        reader.close();
        return count;
    }




    private void writeNewFile(String filename, String dirname, StringBuffer buf){
        File dir = new File(dirname);

        if(!dir.exists()){
            System.out.println("creating directory: " + dir.getName());
            boolean result = false;
            try{
                dir.mkdir();
                result = true;
            }catch (SecurityException e){

            }
            if (result) System.out.println("DIR created");
        }

        String path = "C:/Users/kalin/TestJavaLab1/" + dirname;
        String fileName = path + File.separator + filename;
        File f = new File(fileName);

        try{
            f.createNewFile();
        } catch (IOException e){
            e.printStackTrace();
        }

        try(FileOutputStream fos=new FileOutputStream(fileName))
        {
            byte[] buffer = buf.toString().getBytes();
            fos.write(buffer, 0, buffer.length);
        }
        catch(IOException ex){

            System.out.println(ex.getMessage());
        }
        System.out.println("The file has been written");
    }


    @Override
    public Pair<String, Integer> call() throws Exception{
        return new Pair<String, Integer>(file.getPath(), CountForInFile());
    }
}
