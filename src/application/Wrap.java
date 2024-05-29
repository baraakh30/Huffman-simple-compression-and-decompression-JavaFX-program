package application;

import java.io.OutputStream;

//Wrapper class for return statement while writing into file
public class Wrap {
	String s;
	OutputStream output;
	byte[] streamOut;
	int currentBit;
	int avBytes;
	long expBits;

	public Wrap(String s, OutputStream output, byte[] streamOut, int currentBit, int avBytes, long expBits) {
		this.s = s;
		this.output = output;
		this.streamOut = streamOut;
		this.currentBit = currentBit;
		this.avBytes = avBytes;
		this.expBits = expBits;
	}
}
