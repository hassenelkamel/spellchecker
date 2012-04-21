import java.io.*;
import java.util.*;

/**
 * A custom Spell Checker program which uses a Trie as the data structure
 * 
 * The executable builds it's dictionary by reading in a defined dictionary file,
 * or "/usr/share/dict/words" as default, and then prompts the user to type a word.
 * Based on the input word, 3 rules are checked to see if a match is found or a
 * suggestion can be made. If no word is found, then NO SUGGESTION is printed.
 * 
 * The current rules this software checks for are:
 * 1. ImPROper cAPITalIZAioN. The entire dictionary is treated as lower case and all
 *    inputs are suggested as lowercase.
 * 2. Misplacad vuwals. If the input word is not found, the vowels A,E,I,O,U are
 *    substituted until a suggestion is found. Through a brute force method, all
 *    vowel combinations are attempted. For example, in the input "tost", "test",
 *    "tast", "tist", "tust" are all attempted words. While "toast" tries "toest",
 *    "toust", "teost", "teust", etc...
 * 3. Reeeepeeeated Letttters. Any repeated letters are removed and the word is checked
 *    again.
 *    
 * Any combination of these rules is acceptable. Capitalizations can occur on the same
 * section of the string as repeated letters or incorrect vowels. Unfortunately, the second
 * of a repeated vowel which is then misplaced is invalid and a substitution would not be 
 * found, so "weall" will not match "well", but "waell" will match "well".
 * 
 * @author Dan Moore
 * @version 1.0
 */
public class Spellchecker {

	public static final char PROMPT = '>'; 
	public static ArrayList<String> vowels;
	public static Trie trie = new Trie();
	public static Scanner input = new Scanner(System.in);
	
	/**
	 * The command line program which prompts a user for words, read in using new lines
	 * May read two words separate by spaces (Scanner .getNext)
	 * 
	 * @param args No CLI arguments are available at this time.
	 */
	public static void main(String[] args) {
		String word;
		
		//manually mark vowels. What about Y?!
		vowels = new ArrayList<String>();
		vowels.add("a");
		vowels.add("o");
		vowels.add("i");
		vowels.add("e");
		vowels.add("u");
		
		//buildDictionary("/home/user/workspace/spellchecker/src/words.txt");
		buildDictionary("");
		
		while(true) { //loop indefinitely
			word = getWord();
			if(trie.find(word)) {
				System.out.println(word); //A valid word was entered
			} else {
				word = word.toLowerCase(); //correct uppercase "spelling" mistakes
				if(trie.find(word)) {
					System.out.println(word); //word just had case problems
				} else {
					//System.out.print("- "+word+"   -   ");
					if((word = checkRepeatsAndVowels(word, 0)) != null) { //recursively check all repeats and vowels
						System.out.println(word);
					} else {
						System.out.println("NO SUGGESTION");
					}
				}
			}
		}
	}
	
	/**
	 * Since repeated letters and vowels can be interspersed and combined, each combination of the word 
	 * has to be checked. This brute force method would be to manually check each correction and the 
	 * recursively check all other possible corrections
	 * 
	 * Some inefficiencies:
	 * 1. each word that is finally found to be true, is checked against the tree on each level of recursion
	 *    on it's way back up.
	 * 2. There is probably a smarter way than trying each vowel and checking each repeat of character
	 * 3. This doesn't work if a repeated character is then changed to another vowel (not really one of the
	 *    rules desired, but still might be helpful)
	 * 
	 * @param word The entire word that can be checked against the Trie
	 * @param place The place in the word to start recursively checking, this increases with each layer
	 * @return the word suggested or null for NO SUGGESTION
	 */
	public static String checkRepeatsAndVowels(String word, int place) {
		String testWord; //, doubleTestWord;
		for(int i = place; i < word.length(); i++) {
			//Check Vowels First
			String checking = String.valueOf(word.charAt(i));
			if(vowels.contains(checking)) { //if this letter is a vowel
				for(int j = 0; j < vowels.size(); j++) {
					if(!vowels.get(j).equals(checking)) { //ignore the vowel it originally was
						testWord = replaceLetter(word, i, vowels.get(j)); //try a new vowel
						if(trie.find(testWord)) {
							return testWord; //that worked! the replacement word was found in the dictionary
						} else {
							//This is a test to see if we can go back without infinite looping
							//if((doubleTestWord = doubleCheckRepeats(testWord, i)) != null) {
							//	return doubleTestWord;
							//}
							testWord = checkRepeatsAndVowels(testWord, i+1); //check other vowels and repeats further down
							if((testWord != null) && trie.find(testWord)) {
								return testWord; //something down the chain worked! a replacement word was found in the dictionary
							}
						}
					}
				}
			}
			
			//Check for repeats
			if(i != word.length()-1) { //if this isn't the last character
				if(word.charAt(i) == word.charAt(i+1)) {//see if there's a repeated character after it
					testWord = replaceLetter(word, i, ""); //if there is, remove it, check the word again
					if(trie.find(testWord)) {
						return testWord; //if the word is valid, return it up the chain (each time will check the returns)
					} else { //if the word is not valid, recurse and do again
						testWord = checkRepeatsAndVowels(testWord, i);
						if((testWord != null) && trie.find(testWord)) {
							return testWord; //if the recursed word is valid, return it
						}
					}
				}
			}
		}
		return null;
	}
	
	/**
	 * In the extreme corner case that a vowel is duplicated, and the 2nd of the two vowels is the one changed,
	 * checkRepeatsAndVowels does not catch this. Therefore this function was written to go back a place in the string
	 * to make sure that the immediate change does not create a prior repeat
	 * 
	 * @param word
	 * @param place 
	 * @return
	 */
	public static String doubleCheckRepeats(String word, int place) {
		if(word.length() > 1 && place > 0 && place < word.length() && word.charAt(place) == word.charAt(place-1)) { //being really careful, check the previous position to see if it's a repeated character
			word = replaceLetter(word, place, "");
			if(trie.find(word)) {
				return word;
			}
			return null; //you would call the check repeat and vowels here, but that causes an infinite loop in some cases as well, checking forwards then backwards
		} else {
			return null;
		}
	}
	
	
	
	/**
	 * This is a small helper function to replacing letters in a word.
	 * It is also used to delete letters by sending in an empty string in the place of a character in toAdd.
	 * @param word The word
	 * @param place The position (0-word.length()) to replace in the word
	 * @param toAdd The character to put in that place, or an empty string to delete a character
	 * @return the modified word
	 */
	public static String replaceLetter(String word, int place, String toAdd) {
		if(word.length() == 1) {
			if(place == 0) {
				return toAdd; //if we're replacing the first letter of a 1 letter word, just return the new letter
			}
		} else if(place == 0) {
			word = toAdd + word.substring(1); //replace the first character
		} else if(place == word.length()-1) {
			word = word.substring(0,word.length()-2) + toAdd; //replace the last character
		} else {
			word = word.substring(0, place) + toAdd + word.substring(place+1, word.length()); //replace a character in the middle of the word
		}
		return word;
	}

	/**
	 * Prompt and get the next word from the user
	 * @return the word entered by the user
	 */
	public static String getWord() {
		System.out.print(PROMPT+"");
		return input.next();
	}
	
	
	/**
	 * A simple Trie run to test a few words
	 */
	public static void trieTesting() {
		buildDictionary("/home/user/workspace/spellchecker/src/com/toobulkeh/spellchecker/words.txt");
		printTrie();
		lookFor("a");
		lookFor("an");
		lookFor("and");
		lookFor("sheeple");
	}
	
	/**
	 * A simple output to look for words
	 * Prints the result to System.out
	 * @param word the word to look for in the dictionary
	 * 
	 */
	public static void lookFor(String word) {
		if(trie.find(word)) {
			System.out.println("Found word: "+word);
		} else {
			System.out.println("Did not find word: "+word);
		}
	}
	
	/**
	 * Builds a dictionary file into a Trie
	 * @param fileName the dictionary file
	 */
	public static void buildDictionary(String fileName) {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new DataInputStream(new FileInputStream(fileName))));
			String line;
			while ((line = br.readLine()) != null) {
				trie.insert(line.toLowerCase()); //read in all lines of the file as lower case, Trie will throw out blank lines
				//System.out.println("Added to dictionary: "+line);
			}
		} catch (FileNotFoundException e) { //File not found, rebuild dictionary with default dictionary file
			System.err.println("File not found: "+fileName);
			System.err.println("Using standard system dictionary: /usr/share/dict/words");
			buildDictionary("/usr/share/dict/words");
			//System.err.println("Error: "+e.getMessage());
		} catch (IOException e) { //General IO error
			System.err.println("Error: "+e.getMessage());
		} catch (NullPointerException e) { //Incomplete file? try again with default dictionary file
			System.err.println("Using standard system dictionary: /usr/share/dict/words");
			buildDictionary("/usr/share/dict/words");
		}
	}
	
	public static void printTrie() {
		System.out.println(trie.toString());
	}
	
	public static void printDictionary() {
		//TODO: build functionality into Trie to print only words
	}
	
}
