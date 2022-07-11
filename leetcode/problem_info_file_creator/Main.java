package leetcode.problem_info_file_creator;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.safari.SafariDriver;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    static String url, title, difficulty, questionContent;
    public static void main(String[] args) throws IOException, InterruptedException {
        Scanner scanner = new Scanner(System.in);

        // Asks for LeetCode URL to improve program usability
        System.out.println("Please enter the LeetCode problem URL: ");
        String url = scanner.next();
        if(url.contains("https://leetcode")) {
            fetchWebData(url);
        } else{System.out.println("You didn't enter a valid URL!");}


        // Asks for desired directory where to insert the new file
        System.out.println("Please enter where you want to create this file?");
        String directory = scanner.next();
        createNewFile("/Users/aryansajith/IdeaProjects/DataStructures-Algorithms/src", "README.md");

        scanner.close();
    }
    public static void fetchWebData(String link) throws InterruptedException {
        // Browser Setup = Safari because I am using a macbook
        url = link;
        WebDriver webDriver = new SafariDriver();
        webDriver.get(url);

        // Title
        title = webDriver.getTitle();
        title = title.replaceAll(" - LeetCode","");

        // Difficulty
        WebElement questionDifficulty = webDriver.findElement(new By.ByClassName("css-14oi08n"));
        difficulty = questionDifficulty.getText();

        // Question Content =  We are using a XPath(since Compound classes are no longer supported)
        WebElement questionElement = webDriver.findElement(new By.ByCssSelector("#app > div > div.main__2_tD > div.content__3fR6 > div > div.side-tools-wrapper__1TS9 > div > div.css-1gd46d6-Container.e5i1odf0 > div.css-jtoecv > div > div.tab-pane__ncJk.css-1eusa4c-TabContent.e5i1odf5 > div > div.content__u3I1.question-content__JfgR"));
        questionContent = questionElement.getAttribute("outerHTML");

        webDriver.close();
    }
    public static void createNewFile(String pathName,String filename) {
        try {
            File template = new File("/Users/aryansajith/IdeaProjects/Utilities/leetcode/problem_description_creator/ReadMeFileCreator/Template_README_File.md");
            Scanner templateReader = new Scanner(template);
            String templateText = "";
            String finalizedText = "";

            // Here we read in data from the template file into templateText
            while(templateReader.hasNext()) {
                templateText += templateReader.next();
            }
            templateReader.close();

            // Here we replace all template values with appropriate values obtained from the URL
            finalizedText = templateText.replaceAll("URL",url);
            finalizedText = finalizedText.replaceAll("Title",title);
            finalizedText = finalizedText.replaceAll("Difficulty",difficulty);
            finalizedText = finalizedText.replaceAll("QuestionContent",questionContent);
            // We have some errors in HTML formatting, which I fixed here
            finalizedText = finalizedText.replaceAll("</div></div>","");
            finalizedText = finalizedText.replaceAll("ahref","a href");

            // Here we write in the finalizedText string into the specified pathname
            FileWriter fileWriter = new FileWriter(pathName+"/"+filename);
            fileWriter.write(finalizedText);

            fileWriter.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}
