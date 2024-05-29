package application;

public class Node implements Comparable<Node> {
	Node left;
	Node right;
	int freq;
	byte value;
	boolean isUsed;

	// Default constructor
	public Node() {

	}

	// Non-default constructor
	public Node(Node left, Node right, byte value, boolean isUsed, int freq) {
		this.left = left;
		this.right = right;
		this.value = value;
		this.isUsed = isUsed;
		this.freq = freq;
	}

	// Non-default constructor
	public Node(Node left, Node right, int freq) {
		this.left = left;
		this.right = right;
		this.freq = freq;
	}

	// Non-default constructor
	public Node(int freq, int value, boolean isUsed) {
		this.value = (byte) value;
		this.freq = freq;
		this.isUsed = isUsed;
	}

	// Turn node into string
	@Override
	public String toString() {
		return this.freq + " " + this.value + " " + isUsed + "\n";
	}

	@Override
	public int compareTo(Node o) {
		if (freq > o.freq) {
			return 1;
		}
		return -1;
	}
}
