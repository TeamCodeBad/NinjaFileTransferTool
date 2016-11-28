package main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

public class XOR {
	private byte[] key;
	private  byte[] message;
	File f;
	boolean state;
	
	XOR(boolean state){
		System.out.println("Select Key for Ciphyer");
		this.key = convertFile(Application.getFile());
		this.state = state;
	}
	

	
	private byte[] convertFile(File filename) {
		
		  byte[] bFile = new byte[(int) filename.length()];

	        try {
	        	//comment
	            //convert file into array of bytes
	        FileInputStream fis = new FileInputStream(filename);
		    fis.read(bFile);
		    fis.close();

		    for (int i = 0; i < bFile.length; i++) {
		       	System.out.print((char)bFile[i]);
	            }

		    System.out.println("Done");
	        }catch(Exception e){
	        	e.printStackTrace();
	        }
		return bFile;
	}
	public void setMessage(byte[] message){
		this.message = message;
	}
	
	public void setKeys(byte[] key){
		this.key = key;
	}
	
//	public void cipher(){
//		int size = message.length;
//		byte[] new_message = new byte[size];
//		
//		for(int i = 0; i < size; i++){
//			new_message[i] = (byte) (message[i] ^ key[i % key.length]);
//		}
//		
//		try {
//			PrintWriter pw = new PrintWriter(this.f);
//			pw.close();
//			FileOutputStream fos = new FileOutputStream(this.f);
//			fos.write(new_message);
//			fos.close();
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		
//	}
	
	public File cipher(File returnedFile){
		
		this.message = convertFile(returnedFile);
		int size = message.length;
		byte[] new_message = new byte[size];
		
		for(int i = 0; i < size; i++){
			new_message[i] = (byte) (message[i] ^ key[i % key.length]);
		}
		
		//encoding within XOR
		if(state = true){
			new_message = new ArmorCoder().encodeManyChunks(new_message);
		}
		else{
			new_message = new ArmorCoder().decodedManyChunks(new_message);
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
