import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    private static void findJavaFiles(File xlsFolder, File javaFolder, FileWriter writer) throws IOException {
        Pattern pattern = Pattern.compile("\\d+");

        // 遍历XLS文件夹
        for (File file : xlsFolder.listFiles()) {
            if (file.isDirectory()) {
                // 递归搜索子文件夹
                findJavaFiles(file, javaFolder, writer);
            } else if (file.getName().endsWith(".xls")) {
                Matcher matcher = pattern.matcher(file.getName());
                if (matcher.find()) {
                    String number = matcher.group();
                    File javaFile = new File(javaFolder, number + "Page.java");

                    // 深度遍历Java文件夹以寻找匹配的文件
                    if (findInJavaFolder(javaFile, javaFolder)) {
                        writer.write("Found: " + javaFile.getPath() + "\n");
                    } else {
                        writer.write("Not Found: " + number + "Page.java\n");
                    }
                }
            }
        }
    }

    private static boolean findInJavaFolder(File javaFile, File folder) {
        for (File file : folder.listFiles()) {
            if (file.isDirectory()) {
                // 递归搜索子文件夹
                if (findInJavaFolder(javaFile, file)) {
                    return true;
                }
            } else if (file.equals(javaFile)) {
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("XLSフォルダのパスを入力してください:");
        String xlsFolderPath = reader.readLine();  // 从用户输入获取XLS文件夹路径

        System.out.println("Javaフォルダのパスを入力してください:");
        String javaFolderPath = reader.readLine(); // 从用户输入获取Java文件夹路径

        File xlsFolder = new File(xlsFolderPath);
        File javaFolder = new File(javaFolderPath);
        FileWriter writer = new FileWriter("results.txt");

        findJavaFiles(xlsFolder, javaFolder, writer);

        writer.close();
    }
}
