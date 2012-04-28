import java.util.ArrayList;


/**
 * Custom Dictionary Tree
 * @author Dan Moore
 * @version 1.0
 *
 */
public class Trie {

	//The Root Node, the head of the Trie
	private DictionaryNode root;
	private ArrayList<String> vowels;
	
	public Trie() {
		root = new DictionaryNode(' ');
		vowels = new ArrayList<String>();
		vowels.add("a");
		vowels.add("e");
		vowels.add("o");
		vowels.add("i");
		vowels.add("u");
	}
	
	/**
	 * Insert a String into the Trie data structure.
	 * Inserts multiple strings for a single one containing vowels.
	 * Example: "best" will also insert:
	 * 	bost, bast, bist, bust, which will all point to "best" as the correct spelled word
	 * If "bust" is added later, it will overwrite the correct spelling of "best" to be "bust" 
	 * without the need to add extra nodes
	 * 
	 * @param toAdd the String to add to the Trie data structure
	 */
	public void insert(DictionaryNode loc, String toAdd, int letterPos, String properSpelling) {
		if(properSpelling == null) properSpelling = toAdd;
		if(loc == null) loc = root;
		char vowelCheck;
		
		DictionaryNode currNode = loc, found, newlyCreated;
		char charToFind = toAdd.charAt(letterPos);
		if((found = currNode.findChildNode(charToFind)) == null) { //if the child node based on this current char doesn't exist, create one
			if(isVowel(charToFind)) { //child doesn't exist, is a vowel
				for(int i = 0; i < vowels.size(); i++) {
					vowelCheck = vowels.get(i).charAt(0);
					//if(vowelCheck != charToFind) {
						newlyCreated = new DictionaryNode(vowelCheck);
						currNode.children.add(newlyCreated);
						if(letterPos < toAdd.length()-1) {
							insert(newlyCreated, replaceLetter(toAdd,letterPos,vowels.get(i)), letterPos+1, properSpelling);
						} else {
							newlyCreated.word = properSpelling;
						}
					//}
				}
			} else { //child doesn't exist, but not a vowel
				newlyCreated = new DictionaryNode(charToFind);
				currNode.children.add(newlyCreated); //create the non-vowel character if not found
				if(letterPos < toAdd.length()-1) { //not the last letter, keep recursing
					insert(newlyCreated, toAdd, letterPos+1, properSpelling); //recursively continue creating trie
				} else { //the last letter, did not exist until just now.
					currNode.word = properSpelling;
				}
			}
		} else { //if the node already did exist
			if(letterPos < toAdd.length()-1) { //not the last letter, but already existed
				insert(found, toAdd, letterPos+1, properSpelling); //keep recursing
			} else if(toAdd.equals(properSpelling)) { //last letter, already existed, if this is the one and only node
				currNode.word = properSpelling; //reset the proper spelling suggestion to the same word that this is in the Trie
			}
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
	
	
	private static boolean isVowel(char toCheck) {
		if(toCheck == 'a' || toCheck == 'e' || toCheck == 'i' || toCheck == 'o' || toCheck == 'u') {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Check to see if a String is in the Trie
	 * @param toFind the String to look for in the Trie (Case Sensitive)
	 * @return true if the String exists in the Trie
	 * @return false if the String is not marked as a word or does not exist
	 */
	public String find(String toFind) {
		DictionaryNode currNode = root, found;
		while(currNode != null) { //catch all if we run into null nodes
			for(int i = 0; i < toFind.length(); i++) { //for each character
				char toFindChar = toFind.charAt(i);
				found = currNode.findChildNode(toFindChar);
				if(found == null) {
					return null; //characters not found in the trie
				} else {
					currNode = found; //traverse the tree
				}
			}
			return currNode.word; //nodes were matched, return the proper spelling of the word that terminates here.
		}
		return null; //nodes not found
	}
	
	/**
	 * Prints the Trie in String form
	 */
	public String toString() {
		return root.toString();
	}

	
	
}
