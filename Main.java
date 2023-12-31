import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.io.*;

public class Main {

    public static List<CodeFile> flexibleFileFinder(String path){
        List<CodeFile> codeFileList = new ArrayList<>();

        String pathPattern = path.replace("\\", "\\\\").replace("*", ".*");
        String startDir;

        if (pathPattern.contains("*")) {
            startDir = pathPattern.substring(0, pathPattern.indexOf("*")).replace(".*", "");
        } else {
            startDir = pathPattern;
        }

        Path startPath = Paths.get(startDir);
        if (!Files.exists(startPath)) {
            System.out.println("The start path does not exist: " + startPath);
            return null;
        }

        try {
            Files.walkFileTree(startPath, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                    if (file.toString().matches(pathPattern)) {
                        CodeFile codeFile = new CodeFile();
                        codeFile.FileName = file.getFileName().toString();
                        codeFile.Path = file.toAbsolutePath().normalize().toString();
                        codeFile.Code = file.getFileName().toString().replace("Page.java","");
                        codeFileList.add(codeFile);
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        return codeFileList;
    }

    public static void matchCodeFiles(String targetDir, List<CodeFile> codeFiles) throws IOException {
        Path targetPath = Paths.get(targetDir);
        if (!Files.exists(targetPath)) {
            System.out.println("The target path does not exist: " + targetPath);
            return;
        }

        try (FileWriter writer = new FileWriter("results.txt")) {
            for (CodeFile codeFile : codeFiles) {
                boolean matchFound = Files.walk(targetPath)
                        .map(Path::getFileName)
                        .map(Path::toString)
                        .anyMatch(fileName -> fileName.contains(codeFile.Code));

                if (matchFound) {
                    writer.write("Match found for " + codeFile.Code + ": " + codeFile.Path + "\n");
                } else {
                    writer.write("No match found for " + codeFile.Code + "\n");
                }
            }
        } catch (IOException e) {
            throw e;
        }
    }


    public static void main(String[] args) throws IOException {
        List<CodeFile> codeFiles = flexibleFileFinder("C:\\qmy_java_demo\\JavaMatcherTestEnvironment\\JavaFile\\*\\web\\*Page.java");

        if (codeFiles != null) {
            matchCodeFiles("C:\\path\\to\\target\\folder", codeFiles);
        }
    }
}
