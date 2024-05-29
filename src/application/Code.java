package application;

public class Code {
	String prefix;
	byte value;

	public Code(String prefix, byte value) {
		this.prefix = prefix;
		this.value = value;

	}

	public String toString() {
		return prefix + " " + (value & 0xff);
	}
}
