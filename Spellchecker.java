

import java.io.*;
import java.util.*;

public class Spellchecker {

	static final char PROMPT = '>'; 
	static ArrayList<String> vowels;
	public static Trie trie = new Trie();
	public static Scanner input = new Scanner(System.in);
	public static void main(String[] args) {
		String word;
		
		vowels = new ArrayList<String>();
		vowels.add("a");
		vowels.add("o");
		vowels.add("i");
		vowels.add("e");
		vowels.add("u");
		
		//buildDictionary("/home/user/workspace/spellchecker/src/words.txt");
		buildDictionary("");
		
		while(true) {
			word = getWord();
			if(trie.find(word)) {
				System.out.println(word); //A valid word was entered;
			} else {
				word = word.toLowerCase(); //correct uppercase "spelling" mistakes
				if(trie.find(word)) {
					System.out.println(word); //word just had case problems
				} else {
					//System.out.print("- "+word+"   -   ");
					if((word = checkRepeatsAndVowels(word, 0)) != null) {
						System.out.println(word);
					} else {
						System.out.println("NO SUGGESTION");
					}
				}
			}
		}
	}
	
	/**
	 * Since repeated letters and vowels can be interspersed and combined, each combination of the word has to be checked
	 * A brute force way would be to manually check each correction, with all the corrections that could be made after it.
	 * 
	 * @param word The entire word that can be checked against the Trie
	 * @param place The place in the word to start recursively checking, this increases with each layer
	 * @return
	 */
	public static String checkRepeatsAndVowels(String word, int place) {
		//System.out.println("CRAV: "+word);
		//System.out.println("t- "+word+"@"+place);
		String testWord; //, doubleTestWord;
		for(int i = place; i < word.length(); i++) {
			String checking = String.valueOf(word.charAt(i));
			//System.out.println("Trying "+word+" char at position "+i);
			if(vowels.contains(checking)) { //if this letter is a vowel
				//System.out.println("Character at "+i+" is a vowel");
				for(int j = 0; j < vowels.size(); j++) {
					if(!vowels.get(j).equals(checking)) { //ignore the vowel it already was
						//System.out.println("Replacing character at "+i+" with: "+vowels.get(j));
						testWord = replaceLetter(word, i, vowels.get(j));
						//System.out.println("Testing word looks like: "+testWord);
						if(trie.find(testWord)) {
							return testWord;
						} else {
							//if((doubleTestWord = doubleCheckRepeats(testWord, i)) != null) {
							//	return doubleTestWord;
							//}
							testWord = checkRepeatsAndVowels(testWord, i+1); //check other vowels and repeats further down
							if((testWord != null) && trie.find(testWord)) {
								return testWord;
							}
						}
					}
				}
			}
			
			if(i != word.length()-1) { //if i isn't the last character
				if(word.charAt(i) == word.charAt(i+1)) {//see if there's a repeated character after it
					testWord = replaceLetter(word, i, ""); //if there is, remove it, check the word
					if(trie.find(testWord)) {
						return testWord; //if the word is valid, return it
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
		//System.out.println("w: "+word+" p: "+place);
		if(word.length() > 1 && place > 0 && place < word.length() && word.charAt(place) == word.charAt(place-1)) {
			word = replaceLetter(word, place, "");
			if(trie.find(word)) {
				return word;
			}
			return null;
		} else {
			return null;
		}
	}
	
	
	
	/**
	 * This is a small helper function to catch corner cases of replacing letters in a word
	 * @param word
	 * @param place
	 * @param toAdd
	 * @return
	 */
	public static String replaceLetter(String word, int place, String toAdd) {
		//System.out.println("Replacing "+word+"' "+place+" position with "+toAdd);
		if(word.length() == 1) {
			//System.out.println("Word is length 1");
			if(place == 0) {
				return toAdd;
			}
		} else if(place == 0) {
			//System.out.println("Replacement letter is the first letter of the word");
			word = toAdd + word.substring(1);
		} else if(place == word.length()-1) {
			//System.out.println("Replacement letter is the last letter of the word");
			word = word.substring(0,word.length()-2) + toAdd;
		} else {
			//System.out.println("Normal replacement: Pre: "+word.substring(0, place) +" toAdd: "+toAdd+" Post: "+word.substring(place+1, word.length()));
			word = word.substring(0, place) + toAdd + word.substring(place+1, word.length());
		}
		return word;
	}

	public static String getWord() {
		System.out.print(PROMPT+"");
		return input.next();
	}
	
	public static void trieTesting() {
		buildDictionary("/home/user/workspace/spellchecker/src/com/toobulkeh/spellchecker/words.txt");
		printTrie();
		lookFor("a");
		lookFor("an");
		lookFor("and");
		lookFor("sheeple");
	}
	
	public static void lookFor(String word) {
		if(trie.find(word)) {
			System.out.println("Found word: "+word);
		} else {
			System.out.println("Did not find word: "+word);
		}
	}
	
	public static void buildDictionary(String fileName) {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new DataInputStream(new FileInputStream(fileName))));
			String line;
			while ((line = br.readLine()) != null) {
				trie.insert(line.toLowerCase());
				//System.out.println("Added to dictionary: "+line);
			}
		} catch (FileNotFoundException e) {
			System.err.println("File not found: "+fileName);
			System.err.println("Using standard system dictionary: /usr/share/dict/words");
			buildDictionary("/usr/share/dict/words");
			//System.err.println("Error: "+e.getMessage());
		} catch (IOException e) {
			System.err.println("Error: "+e.getMessage());
		} catch (NullPointerException e) {
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
