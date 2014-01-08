package sheet2.exerciseA;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.select.Elements;

public class FileCrawler {

    private final static String SEPERATOR = " -> ";
    private static final String NEWLINE = "\n";
    // Number of files processed.
    public int filecount = -1;
    // Number of links extracted
    public int totalLinks = -1;
    // Stores links: 'source -> dest\n' (without ')
    public StringBuilder builder = new StringBuilder();
    List<File> htmlFiles = new ArrayList<>();

    // Exercise 10.1
    public void crawl(File dir) throws IOException {
        collectHtmlFiles(dir);
        extractLinks(dir);
    }

    // Exercise 10.2
    public int extractLinks(File file) throws IOException {

        int numLinksOfPage = 0;

        for (File htmlFile : htmlFiles) {
            Document d = Jsoup.parse(htmlFile, "UTF-8");
            Elements links = d.select("a[href]");
            for (Element link : links) {
                String fileName = trimFileName(htmlFile);
                String url = trimUrl(link.attr("abs:href"));
                if (!url.isEmpty()) {
                    appendToOutputFileString(fileName, url);
                    totalLinks++;
                    numLinksOfPage++;
                }
            }
        }

        return numLinksOfPage;
    }

    // Main-Method for testing
    public static void main(String[] args) throws IOException {

        // Name of root directory of the 4 universities datasets.
        // TODO change this path if necessary!!!
        String root = "./data/webkb";

        // Crawling and extracting links.
        FileCrawler crawler = new FileCrawler();
        crawler.crawl(new File(root));

        // check for exercise 10.1
        if (crawler.filecount == -1 || crawler.totalLinks == -1) {
            throw new RuntimeException("Implement exercise 10.1!");
        }

        System.out.println("Processed " + crawler.filecount + " files with "
                + crawler.totalLinks + " links.");

        // check for exercise 10.2
        if (crawler.builder == null) {
            throw new RuntimeException("Implement exercise 10.2!");
        }

        // Write result to file.
        try {
            FileWriter fw = new FileWriter("linkage.txt");
            fw.write(crawler.builder.toString());
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void collectHtmlFiles(File dir) throws IOException {
        FileVisitor<Path> visitor = new FileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                if (attrs.isRegularFile() && file.getFileName().toString().endsWith(".html")) {
                    htmlFiles.add(file.toFile());
                    filecount++;
                }
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                return FileVisitResult.CONTINUE;
            }
        };

        Files.walkFileTree(dir.toPath(), visitor);
    }

    private String trimFileName(File file) {
        String fileName = file.getName();
        fileName = fileName.replace("_^^", "/");
        fileName = fileName.replace("://", "/");
        fileName = fileName.replace("^^", "/");
        fileName = fileName.replace("^", "/");
        return fileName;
    }

    private String trimUrl(String attr) {
        String validUrl = "";
        if (attr.startsWith("http://") && !validUrl.contains("\\n")
                && !validUrl.contains(">") && !validUrl.contains("<")) {
            validUrl = attr;
        }
        return validUrl;
    }

    private void appendToOutputFileString(String fileName, String url) {
        builder.append(fileName);
        builder.append(SEPERATOR);
        builder.append(url);
        builder.append(NEWLINE);
    }
}
