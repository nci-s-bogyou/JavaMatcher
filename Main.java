import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("XLSフォルダのパスを入力してください:");
        String xlsFolderPath = reader.readLine();  // 从用户输入获取XLS文件夹路径

        System.out.println("Javaフォルダのパスを入力してください:");
        String javaFolderPath = reader.readLine(); // 从用户输入获取Java文件夹路径

        File xlsFolder = new File(xlsFolderPath);
        File javaFolder = new File(javaFolderPath);
        FileWriter writer = new FileWriter("results.txt");

        Pattern pattern = Pattern.compile("\\d+");

        for (File file : xlsFolder.listFiles()) {
            if (file.getName().endsWith(".xls")) {
                Matcher matcher = pattern.matcher(file.getName());
                if (matcher.find()) {
                    String number = matcher.group();
                    File javaFile = new File(javaFolder, number + "Page.java");
                    if (javaFile.exists()) {
                        writer.write("Found: " + javaFile.getPath() + "\n");
                    } else {
                        writer.write("Not Found: " + number + "Page.java\n");
                    }
                }
            }
        }

        writer.close();
    }
}
