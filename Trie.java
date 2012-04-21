

public class Trie {

	private Node root;
	
	public Trie() {
		root = new Node(' ');
	}
	
	public void insert(String toAdd) {
		Node currNode = root;
		if(toAdd.length() == 0) {
			currNode.word = true;
		}
		
		for(int i = 0; i < toAdd.length(); i++) {
			Node found;
			char toFindChar = toAdd.charAt(i);
			if((found = currNode.findNode(toFindChar)) == null) {
				currNode.children.add(new Node(toFindChar)); //create the character if not found
				currNode = currNode.findNode(toFindChar); //set the newly created to just found
			} else {
				currNode = found; //found the character
			}
			if(i==toAdd.length()-1) {
				currNode.word = true; //end of the word
			}
		}
	}
	
	public boolean find(String toFind) {
		Node currNode = root;
		while(currNode != null) {
			for(int i = 0; i < toFind.length(); i++) {
				char toFindChar = toFind.charAt(i);
				Node found;
				if((found = currNode.findNode(toFindChar)) == null) {
					return false; //characters not found in the trie
				} else {
					currNode = found;
				}
			}
			return currNode.word; //return if the string found was a word or not
		}
		return false; //nodes not found
	}
	
	
	public String toString() {
		return root.toString();
	}

	
	
}
