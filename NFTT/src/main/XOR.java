package main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * An XOR Encryption class to encrypt files before sending.
 * Works by allowing the user to choose a file to be used
 * as the key for encrypting and must decrypt with the same key.
 */
public class XOR {
	private byte[] key;
	private byte[] message;
	File f;
	int state;

	XOR(int state) {
		System.out.println("Select Key for Cipher");
		this.key = convertFile(Application.getFile());
		this.state = state;
	}

	private byte[] convertFile(File filename) {

		byte[] bFile = new byte[(int) filename.length()];

		try {
			// convert file into array of bytes
			FileInputStream fis = new FileInputStream(filename);
			fis.read(bFile);
			fis.close();
			System.out.println("XOR is Done.");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bFile;
	}

	public void setMessage(byte[] message) {
		this.message = message;
	}

	public void setKeys(byte[] key) {
		this.key = key;
	}
	
	public void setFlip(int state){
		this.state = state;
	}

	public File cipher(File returnedFile) {
		this.message = convertFile(returnedFile);
		int size = message.length;
		byte[] new_message = new byte[size];
		if (state == 2) {
			for (int i = 0; i < message.length; i++) {
				System.out.print((char)message[i]);
				if (i % 50 == 0) {
					System.out.print("\n");
				}
			}
			new_message = new ArmorCoder().decodedManyChunks(message);

			message = new_message;
			size = new_message.length;
		}
		for (int i = 0; i < size; i++) {
			new_message[i] = (byte) (message[i] ^ key[i % key.length]);
		}

		// encoding within XOR
		if (state == 1) {
			new_message = new ArmorCoder().encodeManyChunks(new_message);
		}
		

		try {
			PrintWriter pw = new PrintWriter(returnedFile);
			pw.close();
			FileOutputStream fos = new FileOutputStream(returnedFile);
			fos.write(new_message);
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return returnedFile;
	}

	public void deleteFile() {
		f.delete();
	}
}
