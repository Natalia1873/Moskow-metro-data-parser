import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileSearch {
    public List<String> findFiles(String path, String extension) {
        List<String> result = new ArrayList<>();
        File dir = new File(path);

        if (dir.exists() && dir.isDirectory()) {
            System.out.println("\nSearching for *." + extension + " files in " + path);
            searchFiles(dir, extension, result);
            System.out.println("Total " + extension + " files found: " + result.size());
        } else {
            System.out.println("Directory not found: " + path);
        }

        return result;
    }

    private void searchFiles(File dir, String extension, List<String> result) {
        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    searchFiles(file, extension, result);
                } else if (file.getName().toLowerCase().endsWith(extension.toLowerCase())) {
                    String path = file.getAbsolutePath();
                    result.add(path);
                    System.out.println("Found file: " + file.getName());
                }
            }
        }
    }
}