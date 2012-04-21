

import java.util.LinkedList;

public class Node {
	public char data; //The character on this node
	public LinkedList<Node> children; //The list of children below it
	public boolean word; //Used to mark the end of a word
	
	public Node(char data) {
		this.data = data;
		this.children = new LinkedList<Node>();
	}
	
	public Node findNode(char data) {
		if(this.children != null) {
			for(int i = 0; i < this.children.size(); i++) {
				Node child = this.children.get(i);
				if(child.data == data) {
					return child;
				}
			}
		}
		return null;
	}
	
	public String toString() {
		String toReturn;
		if(this.children.size() > 0) {
			toReturn = "Node "+this.data+" has children:";
			for(int i = 0; i < this.children.size(); i++) {
				char child = this.children.get(i).data;
				toReturn += " "+child;
			}
			toReturn += "\n";
			for(int i = 0; i < this.children.size(); i++) {
				toReturn += this.children.get(i).toString();
			}
		} else {
			toReturn = "Node "+this.data+" has no children.\n";
		}
		return toReturn;
	}
	
}
