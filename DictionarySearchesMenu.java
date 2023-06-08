
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

/**
 * All methods are static
 * Loads a dictionary from a file called user.dict
 * Asks the user if they want to search the dictionary for a word (in a loop)
 * Asks the user if they want to search for all words containing a letter of their choice (in a loop)
 * Asks the user if they want to add a word (in a loop)
 * Shows the dictionary
 * Saves the dictionary back to the user.dict file and ends
 */

public class DictionarySearchesMenu {
	private Scanner userInput;
	private  List<String> dictionary;
	private static final String DICTIONARY_FILENAME = "user.dict";



	/********************************************************************************************/
											/* main method */
	/********************************************************************************************/

	//main method
	public static void main(String[] args) {
		DictionarySearchesMenu userOption = new DictionarySearchesMenu();
		printMenuForUserSelection();
		userOption.run();
	}

	public DictionarySearchesMenu() {
		init();
	}

	/********************************************************************************************/
											/* Initialise */
	/********************************************************************************************/ 	
	
	private void init() {
		dictionary = new ArrayList<String>();
		userInput = new Scanner(System.in);
		readDictionary();
    }

	/********************************************************************************************/
									/* User interaction methods */
	/********************************************************************************************/

	private static void printMenuForUserSelection() {
		//show menu
		System.out.println("Welcome to Dictionary Searches");
		System.out.println("Available commands:");
		System.out.println("0: show this menu again");
		System.out.println("1: show the dictionary");
		System.out.println("2: add a word to the dictionary");
		System.out.println("3: show all words that contain a certain char");
		System.out.println("4. show all words that contain a certain number of vowels (vowels: a, e, i, o, u)");
		System.out.println("5. search the dictionary for a particular word");
		System.out.println("6. exit Dictionary Searches");

	}

	public void run() {

		while(true) {

			System.out.println(" ");
			System.out.print("Please select the number of your preferred command: ");
			String convertFromStringToInt = getUserInput();
			int userChoice = parseToInt(convertFromStringToInt);

			if( userChoice < -1 || userChoice > 6){
				System.out.println("I don’t know how to do menu choice " +userChoice+ ". Please enter a number between 0 and 6");
			}

			else if(userChoice == 0){
				printMenuForUserSelection();
			}

			else if(userChoice == 1){
				showWords();
			}

			else if(userChoice == 2){
				addWords();
			}

			else if(userChoice == 3){
				findAllWordsContainingACertainLetter();
			}

			else if(userChoice == 4){
				numberOfVowelsInAWord();
			}

			else if(userChoice == 5){
				searchWord();
			}

			else if (userChoice == 6){
				saveDictionary();
				System.out.println("Bye!");
				System.exit(0);
			}

			else {
				System.out.println(convertFromStringToInt+ " is not a valid choice. Please enter a number between 0 and 6");
			}
		}
	}

	private void searchWord(){
		do {
			System.out.print("Enter a word to search the dictionary for:> ");
			String word = sanitise(getUserInput());
			if (wordIsInDictionary(word)) System.out.println(word+" is in the dictionary");
			else System.out.println(word+" not found in the dictionary");
		}
		while (getUserDecision("Search for another word? y/n:> "));
	}


	//Add an arbitrary number of words to the dictionary taken from user input
	private void addWords() {
		//loop until the user doesn't want to add any more words
		do {
			System.out.print("Enter a word to add to the dictionary:> ");
			String newWord = sanitise(getUserInput());

			if (dictionary.contains(newWord)){
				System.out.println("That word is already in the dictionary!");
			}

			else if (getUserDecision("Would you like to add "+newWord+" to the dictionary? y/n:> "))
			addWord(newWord);

			else System.out.println("Word not added.");
			
		}

		while (getUserDecision("Add another word? y/n:> "));
		System.out.println(" ");
		//now we are done adding words
		saveDictionary();
	}


	//Find and show all words that contain a certain letter, chosen by the user
	private void findAllWordsContainingACertainLetter() {
		//loop until the user doesn't want to find any more words
		do {
			System.out.print("Enter a letter and I will find you all words");
			System.out.print(" in the dictionary with that letter in them:> ");
			String input = sanitise(getUserInput());
			char c = input.charAt(0);
			String word;
			int countOfWordsFound = 0;
			for (int i = 0; i < dictionary.size(); i++) {
				word = dictionary.get(i); 
				if (wordContainsThisChar(word, c)) {
					System.out.println(word);//if the right number of vowels display word
					countOfWordsFound++; //count the words displayed
				}	
			}	
			if (countOfWordsFound == 0) System.out.println("No words found containing the letter "+c);
		} while (getUserDecision("Find words in the dictionary with a particular letter? y/n:> "));
	}


	//user request for the number of vowels in a word
	private int requestUserInputForNumberOfVowelsInAWord(){

		int stringToNumber = 0;

		while(true){
			System.out.print("Enter a number and I will show you all words in the dictionary with that number of vowels:> ");
			String userRequest = getUserInput();
			stringToNumber = parseToInt(userRequest);

			if(stringToNumber <= -1){
				System.out.println(userRequest + " is not a valid choice. Please enter a whole number greater than -1");
				System.out.println(" ");
			}
			else break;
		}

		return stringToNumber;
	}


	// print out all the words with the the number of vowels the user requested
	private void printAllWordsWithUserRequestedNumberOfVowels(int userRequestedInteger){
		int probabilityOfAtLeastOneVowel = 0;
		for(int i = 0; i < dictionary.size(); i++){
			String word = dictionary.get(i);
			int numberOfVowelsInAWord = countVowels(word);
			if(userRequestedInteger==numberOfVowelsInAWord){
				System.out.println(word);
				probabilityOfAtLeastOneVowel++;
				}
		}
		if(probabilityOfAtLeastOneVowel==0){
			System.out.println("There are no words with " + userRequestedInteger + " number of vowels." );

		}
	}


	private  void numberOfVowelsInAWord(){
		do{
			printAllWordsWithUserRequestedNumberOfVowels(requestUserInputForNumberOfVowelsInAWord());
		}

		while (getUserDecision("Do you want to print more words with another number of vowels? y/n: "));

	}
	
	/********************************************************************************************/
								/* Helper methods for user interaction*/
	/********************************************************************************************/
	
	//if the user's decision is yes return true, if no return false
	private boolean getUserDecision(String prompt) {
		//loop until we understand the input given to us by the user
		while (true) {
			System.out.print(prompt);
			String answer = sanitise(getUserInput());
			if (answer.startsWith("y")) {
				return true;
			} else if (answer.startsWith("n")) {
				return false;
			} else {
				System.out.println("Huh?");
				//repeat the question
			}
		}
	}

	//Get the next line of input from the user
	private String getUserInput() {
		return userInput.nextLine();
	}		
	
	//Standardise words to lower case
	//Trim word to get rid of any leading and trailing spaces
	private static String sanitise(String word) {
		return word.trim().toLowerCase();
	}

	//Find if a particular char is in a word   
	private static boolean wordContainsThisChar(String word, char thisChar) {
		if (word.indexOf(thisChar) >= 0) return true;
		return false;
	}
	
	//Count vowels in a word
	private static int countVowels(String word) {
		String vowels = "aeiouAEIOU"; //checking both uppercase and lowercase
		char c;
		int countOfVowels = 0;
		for (int i = 0; i < word.length(); i++) {
			c = word.charAt(i);
			if (vowels.indexOf(c) >= 0) { //if character is a vowel
				countOfVowels++;
			}
		}
		return countOfVowels;
	}
	
	//Parse a String to an int, return -1 if parsing fails
	private static int parseToInt(String input){
		while (true){
			try{				
				int parsedInt = Integer.parseInt(input);
				return parsedInt;
			}
			catch (Exception e){
				return -1;
			}
		}//end of while
	}
	

	/********************************************************************************************/
										/* Dictionary methods */
	/********************************************************************************************/
	
	//Does 'word' already exist in the dictionary?
	private boolean wordIsInDictionary(String word) {
		return dictionary.contains(sanitise(word));
	}
	
	//Add a new word to the dictionary
	private void addWord(String word) {
		dictionary.add(sanitise(word));
	}

	//Sort the dictionary into alphabetical order
	private void sortDictionary() {
		Collections.sort(dictionary);
	}
	
	//Show all dictionary words in alphabetical order.
	private void showWords() {
		sortDictionary();
		for (int i = 0; i < dictionary.size(); i++) {
			System.out.println(dictionary.get(i));
		}
	}
	
	
	/********************************************************************************************/
							/* File reading and writing methods */
	/********************************************************************************************/

	//Write ArrayList out to the file
	private void saveDictionary() {
		FileWriter writer = null;
		sortDictionary();
		try {
			writer =  new FileWriter(DICTIONARY_FILENAME);
			for (int i = 0; i < dictionary.size(); i++) {
				writer.write(dictionary.get(i)+System.lineSeparator());
				//System.lineSeparator() used instead of "\n" as it will work on all operating systems
			}
			writer.close();
		} catch (IOException e) {
			System.err.println("Could not write to dictionary file");
		}
	}

	//Read the dictionary into the ArrayList from the file.
	private void readDictionary() {
		Scanner in = null;
		try {
			in = new Scanner(new FileReader(DICTIONARY_FILENAME));
		} catch (IOException e) {
			System.err.println("Dictionary file does not exist! Aborting.");
		}
		while(in.hasNextLine()) {
			String word = in.nextLine();
			addWord(word);
		}
		in.close();
		sortDictionary();
	}


}
