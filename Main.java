import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//请输入XLS文件夹的路径:
//C:\qmy_java_demo\JavaMatcherTestEnvironment\ExcelFile
//请输入Java文件夹的路径:
//C:\qmy_java_demo\JavaMatcherTestEnvironment\JavaFile
public class Main {

    private static void findJavaFiles(File xlsFolder, File javaFolder, FileWriter writer) throws IOException {
        Pattern pattern = Pattern.compile("画面定義書_([a-zA-Z0-9]+)_");

        if (xlsFolder.isDirectory()) {
            // 遍历XLS文件夹
            File[] files = xlsFolder.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        // 递归搜索子文件夹
                        findJavaFiles(file, javaFolder, writer);
                    } else if (file.getName().endsWith(".xlsx")) {
                        Matcher matcher = pattern.matcher(file.getName());
                        if (matcher.find()) {
                            String identifier = matcher.group(1);  // 获取匹配的英数字字符串
                            File foundJavaFile = findInJavaFolder(identifier + "Page.java", javaFolder);
                            if (foundJavaFile != null) {
                                writer.write("Found: " + foundJavaFile.getAbsolutePath() + "\n");
                            } else {
                                writer.write("Not Found: " + identifier + "Page.java\n");
                            }
                        }
                    }
                }
            }
        }
    }

    private static File findInJavaFolder(String javaFileName, File folder) {
        if (folder.isDirectory()) {
            File[] files = folder.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        // 递归搜索子文件夹
                        File foundFile = findInJavaFolder(javaFileName, file);
                        if (foundFile != null) {
                            return foundFile;
                        }
                    } else if (file.getName().equals(javaFileName)) {
                        return file;  // 返回匹配的文件对象
                    }
                }
            }
        }
        return null;  // 没有找到文件
    }

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("请输入XLS文件夹的路径:");
        String xlsFolderPath = reader.readLine();

        System.out.println("请输入Java文件夹的路径:");
        String javaFolderPath = reader.readLine();

        File xlsFolder = new File(xlsFolderPath);
        File javaFolder = new File(javaFolderPath);
        FileWriter writer = new FileWriter("results.txt");

        findJavaFiles(xlsFolder, javaFolder, writer);

        writer.close();
    }
}
