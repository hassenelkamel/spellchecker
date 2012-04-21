
/**
 * Custom Dictionary Tree
 * @author Dan Moore
 * @version 1.0
 *
 */
public class Trie {

	//The Root Node, the head of the Trie
	private Node root;
	
	public Trie() {
		root = new Node(' ');
	}
	
	/**
	 * Insert a String into the Trie data structure.
	 * 
	 * 
	 * @param toAdd the String to add to the Trie data structure
	 */
	public void insert(String toAdd) {
		Node currNode = root;
		if(toAdd.length() == 0) { //Consider the root a word
			currNode.word = true;
		}
		for(int i = 0; i < toAdd.length(); i++) {
			Node found;
			char charToFind = toAdd.charAt(i);
			if((found = currNode.findChildNode(charToFind)) == null) { //if the child node based on this current char doesn't exist, create one
				currNode.children.add(new Node(charToFind)); //create the character if not found
				currNode = currNode.findChildNode(charToFind); //set the newly created current, so we can continue creating new child nodes
			} else {
				currNode = found; //found the character, advance down the tree
			}
			if(i==toAdd.length()-1) {
				currNode.word = true; //if this is the last character in the String, mark it as the end of the word
			}
		}
	}
	
	/**
	 * Check to see if a String is in the Trie
	 * @param toFind the String to look for in the Trie (Case Sensitive)
	 * @return true if the String exists in the Trie
	 * @return false if the String is not marked as a word or does not exist
	 */
	public boolean find(String toFind) {
		Node currNode = root;
		while(currNode != null) { //catch all if we run into null nodes
			for(int i = 0; i < toFind.length(); i++) { //for each character
				char toFindChar = toFind.charAt(i);
				Node found;
				if((found = currNode.findChildNode(toFindChar)) == null) {
					return false; //characters not found in the trie
				} else {
					currNode = found; //traverse the tree
				}
			}
			return currNode.word; //nodes were matched, return if a word terminates here.
		}
		return false; //nodes not found
	}
	
	/**
	 * Prints the Trie in String form
	 */
	public String toString() {
		return root.toString();
	}

	
	
}
