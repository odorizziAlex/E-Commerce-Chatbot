package com.company;

import com.company.Response.QuestionGenerator;
import com.company.Response.StandardResponse;
import com.company.Tools.*;

import javax.sound.midi.Soundbank;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.regex.Pattern;

import static com.company.utils.Config.*;

public class Main {

    // Init tools:
    // Input tools:
    private static Tokenizer tokenizer = new Tokenizer();
    private static Scanner scanner = new Scanner(System.in);
    private static StopWordRemover stopWordRemover = new StopWordRemover();
    private static Lemmatizer lemmatizer = new Lemmatizer();
    // Processing tools:
    private static DemandAnalyzer demandAnalyzer = new DemandAnalyzer();

    // init response tools:
    private static StandardResponse standardResponse = new StandardResponse();
    private static QuestionGenerator questionGenerator = new QuestionGenerator();

    //
    private static ArrayList<String> userInput = new ArrayList<>();
    private static boolean firstIteration = true;

    // Cancellation condition
    private static Boolean isFinished = false;

    public static void main(String[] args) {
        // Standard Introduction of Bot
        System.out.println(GREETING);

        // As long as needed information is not fullfilled
        while(!isFinished) {

            // Get formatted user input
            userInput = formattedInput();
            demandAnalyzer.setComponent(userInput);

            // Get demand of user and reasure give answer
            ArrayList<String> items = demandAnalyzer.getDemandComponents();
            System.out.println(Arrays.toString(items.toArray()));
            String response = standardResponse.generateStandardResponse(items);
            System.out.println(response);

            // Ask for missing information
            ArrayList<Integer> missingItems = demandAnalyzer.getAllEmptyDemandComponents();
            String botMessage = questionGenerator.generateQuestion(missingItems);
            System.out.println(botMessage);
        }
    }

    private static ArrayList<String> formattedInput(){
        String isPriceRegEx = "([\\d]+([€$]|))",
        // 1. Read user input
        rawUserInput = scanner.nextLine(),
        //1.2 Remove special characters
        removeSpecialCharacters= rawUserInput.replaceAll("[-+^;,.!?()=§%&/]",""),
        //1.3 Every character to lower case
        finalUserInput = removeSpecialCharacters.toLowerCase();
        // 2. Tokenize, Lemmatize, Remove Stop words of user Input
        ArrayList<String> formattedInput = tokenizer.tokenize(finalUserInput),
        finalList = new ArrayList<>();

        formattedInput = stopWordRemover.removeStopWords(formattedInput);

        // numeric information mustn't be lemmatized!
        for (int i=0;i<formattedInput.size();i++){
            if(Pattern.matches(isPriceRegEx,formattedInput.get(i))){
                finalList.add(formattedInput.get(i));
            }else{
                finalList.add(lemmatizer.lemmatizeWord(formattedInput.get(i)));
            }
        }
        return finalList;
    }
}
