import java.util.LinkedList;

/**
 * A Simple Trie Node
 * Modified 
 * 
 * @author Dan Moore
 * @version 1.0
 *
 */
public class DictionaryNode {
	public char data; //The character on this node
	public LinkedList<DictionaryNode> children; //The list of children Nodes
	/*
	 * Used to mark the end of the word. Also used to mark the real spelling of the word
	 */
	public String word;
	
	public DictionaryNode(char data) {
		this.data = data;
		this.children = new LinkedList<DictionaryNode>();
	}
	
	/**
	 * Checks to see if any of this Node's children match the char
	 * @param data the char to match
	 * @return the child Node or null if none found
	 */
	public DictionaryNode findChildNode(char data) {
		if(this.children != null) {
			for(int i = 0; i < this.children.size(); i++) {
				DictionaryNode child = this.children.get(i);
				if(child.data == data) {
					return child;
				}
			}
		}
		return null;
	}
	
	/**
	 * A simple way of seeing a list of characters that this node calls children
	 * @return the String to print
	 */
	public String toString() {
		String toReturn;
		if(this.children.size() > 0) {
			toReturn = "Node "+this.data+" has children:";
			for(int i = 0; i < this.children.size(); i++) {
				char child = this.children.get(i).data;
				toReturn += " "+child;
			}
			if(this.word != null) toReturn += " and gives word: "+this.word;
			toReturn += "\n";
			for(int i = 0; i < this.children.size(); i++) {
				toReturn += this.children.get(i).toString();
			}
		} else {
			toReturn = "Node "+this.data+" has no children.";
			if(this.word != null) toReturn += " and gives word: "+this.word;
			toReturn += "\n";
		}
		return toReturn;
	}
	
}
