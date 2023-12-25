import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class CodeFileFinder {
    private static List<String> codes = new ArrayList<>();

    public void findFilesInPath(String parentDirectory) throws IOException {
        File dir = new File(parentDirectory);
        File[] directories = dir.listFiles(File::isDirectory);

        if (directories != null) {
            for (File subDir : directories) {
                Path webPath = Paths.get(subDir.getAbsolutePath(), "web");
                if (Files.exists(webPath)) {
                    Files.walk(webPath)
                            .filter(Files::isRegularFile)
                            .filter(p -> p.toString().endsWith("Page.java"))
                            .forEach(this::extractCode);
                }
            }
        }

    }

    private void extractCode(Path filePath) {
        Pattern pattern = Pattern.compile("code:([a-zA-Z0-9]+)");
        try {
            List<String> lines = Files.readAllLines(filePath);
            for (String line : lines) {
                Matcher matcher = pattern.matcher(line);
                if (matcher.find()) {
                    codes.add(matcher.group(1));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void findFilesWithCode(String directory) throws IOException {
        for (String code : codes) {
            Files.walk(Paths.get(directory))
                    .filter(Files::isRegularFile)
                    .filter(p -> p.getFileName().toString().contains(code))
                    .forEach(p -> System.out.println("File: " + p.getFileName() + ", Path: " + p));
        }
    }

    private void flexibleFileFinder(String path){
        String pathPattern = path.replace("\\", "\\\\").replace("*", ".*");
        String startDir;

        if (pathPattern.contains("*")) {
            startDir = pathPattern.substring(0, pathPattern.indexOf("*")).replace(".*", "");
        } else {
            startDir = pathPattern;
        }

        Path startPath = Paths.get(startDir);
        // 检查路径是否存在
        if (!Files.exists(startPath)) {
            System.out.println("The start path does not exist: " + startPath);
            return;
        }

        try {
            Files.walkFileTree(startPath, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                    if (file.toString().matches(pathPattern)) {
                        System.out.println(file.toAbsolutePath().normalize());
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        CodeFileFinder finder = new CodeFileFinder();
        finder.flexibleFileFinder("C:\\qmy_java_demo\\JavaMatcherTestEnvironment\\JavaFile\\*\\web\\*Page.java");
//        System.out.println(codes.toString());
//        finder.findFilesWithCode("其他目录"); // 替换为实际的目录路径

//        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
//        System.out.println("请输入Java文件夹的路径:");
//        String javaFolderPath = reader.readLine(); //C:\qmy_java_demo\JavaMatcherTestEnvironment\JavaFile\*\web\*1Page.java
//        finder.flexibleFileFinder(javaFolderPath);
    }
}
