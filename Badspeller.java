import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

public class Badspeller {

	public final static int MISTAKES = 4;
	public final static int BADWORDS = 1;
	public static Random gen = new Random(System.currentTimeMillis());
	public static ArrayList<String> vowels;
	
	public static void main(String[] args) {
		vowels = new ArrayList<String>();
		vowels.add("a");
		vowels.add("o");
		vowels.add("i");
		vowels.add("e");
		vowels.add("u");
		
		//generateWords("/home/user/workspace/spellchecker/src/words.txt");
		generateWords("");
	}
	
	public static void generateWords(String fileName) {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new DataInputStream(new FileInputStream(fileName))));
			String line;
			while ((line = br.readLine()) != null) {
				if(line.length() > 0) {
					for(int i=0; i<BADWORDS; i++) {
						misspellWord(line);
					}
				}
			}
		} catch (FileNotFoundException e) {
			//System.err.println("File not found: "+fileName);
			//System.err.println("Using standard system dictionary: /usr/share/dict/words");
			generateWords("/usr/share/dict/words");
			//System.err.println("Error: "+e.getMessage());
		} catch (IOException e) {
			System.err.println("Error: "+e.getMessage());
		} catch (NullPointerException e) {
			//System.err.println("Using standard system dictionary: /usr/share/dict/words");
			generateWords("/usr/share/dict/words");
		}
	}
	
	public static void misspellWord(String word) {
		word = changeVowel(word);
		for(int i = 0; i < MISTAKES; i++) {
			word = addMistake(word);
		}
		System.out.println(word);
	}
	
	public static String addMistake(String word) {
		switch(gen.nextInt(2)) {
			//case 0:
			//	word = changeVowel(word);
			//	break;
			case 0:
				word = changeCapitalization(word);
				break;
			case 1:
				word = addRepeatedLetters(word);
				break;
		}
		return word;
	}
	
	public static String changeVowel(String word) {
		//System.out.println("Word: "+word);
		int numOfVowels = countVowels(word);
		if(numOfVowels > 0) {
			int toChange = gen.nextInt(numOfVowels);
			int vowel = gen.nextInt(vowels.size());
			
			//System.out.println("To change: "+toChange+" vowel: "+vowel);
			
			int count = 0;
			for(int i = 0; i < word.length()-1; i++) {
				for(int j = 0; j < vowels.size()-1; j++) {
					//System.out.println("i: "+i+" j: "+j);
					if((vowels.get(j)).equals(String.valueOf(word.charAt(i)).toLowerCase())) { //if it's a vowel
						if(count == toChange) {
							word = replaceLetter(word, i, vowels.get(vowel));
						} else {
							count++;
						}
					}
				}
			}
		}
		return word;
	}
	
	private static int countVowels(String word) {
		int count = 0;
		
		//System.out.println("Counting "+word+"' vowels:");
		for(int i = 0; i < word.length(); i++) {
			//System.out.println("- checking "+word.charAt(i)+ " at "+ i);
			for(int j = 0; j < vowels.size(); j++) {
				//System.out.println("-- against vowel: "+vowels.get(j));
				if(vowels.get(j).equals(String.valueOf(word.charAt(i)).toLowerCase())) { //if it's a vowel
					//System.out.println("Match");
					count++;
				}
			}
		}
		return count;
	}

	public static String changeCapitalization(String word) {
		int pos = gen.nextInt(word.length());
		int len = gen.nextInt(word.length()-pos+1);
		
		if(pos == 0) {
			word = word.substring(pos, pos+len).toUpperCase() + word.substring(len);
		} else if(pos + len == word.length()-1) {
			word = word.substring(0, pos) + word.substring(pos).toUpperCase();
		} else {
			word = word.substring(0, pos) + word.substring(pos, pos+len).toUpperCase() + word.substring(pos+len);
		}
		
		return word;		
	}
	
	public static String addRepeatedLetters(String word) {
		int pos = gen.nextInt(word.length());
		if(pos == 0) {
			word = word.charAt(0) + word;
		} else if(pos == word.length()-1) { //repeat last letter
			word = word +  word.charAt(pos);
		} else {
			word = word.substring(0, pos+1) + word.charAt(pos) + word.substring(pos+1, word.length());
		}
		return word;
		
	}
	
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
	
}
