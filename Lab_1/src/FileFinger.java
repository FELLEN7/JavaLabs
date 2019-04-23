import javafx.util.Pair;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class FileFinger {
    ExecutorService executor = Executors.newFixedThreadPool(5);
    List<Future<Pair<String, Integer>>> listFuture = new ArrayList<Future<Pair<String, Integer>>>();

    private void findJavaFiles(File[] listOfFiles){
        if (listOfFiles != null){
            for (File file : listOfFiles){
                if(file.isFile() &&
                        (file.getName().endsWith(".java"))){
                    Callable<Pair<String, Integer>> callable = new FileCallable();
                    ((FileCallable) callable).setFile(file);
                    Future<Pair<String, Integer>> future = executor.submit(callable);
                    listFuture.add(future);
                }
                if(file.isDirectory()){/*MY REGEXP!!!!*/
                    findJavaFiles(file.listFiles());/*MY REGEXP!!!!*/
                    /*MY REGEXP!!!!*/
                }
            }
        }
    }


    public void doing(String path){
        File folder = new File(path);
        if (folder.isFile()){
            System.out.println("Is file");
            return;
        }
        File[] listOfFiles = folder.listFiles();

        findJavaFiles(listOfFiles);

        if (listFuture.isEmpty())
            System.out.println("Is empty");
        for(Future<Pair<String, Integer>> fut : listFuture){
            try{
                System.out.println(fut.get().getKey() + " - " + fut.get().getValue());
            }catch (InterruptedException | ExecutionException e){
                e.printStackTrace();
            }
        }
    }

    public void stopping(){
        executor.shutdown();
        listFuture.clear();
    }
}
