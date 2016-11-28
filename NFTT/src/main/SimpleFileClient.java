package main;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;
import javax.swing.JFileChooser;

public class SimpleFileClient {
	String file;
	int port;
	String address;
	static String directory = System.getProperty("user.dir");
	FileSplitter fs = new FileSplitter();
	FileMerger fm = new FileMerger();
	File toSend;

	public SimpleFileClient(int portNumber, String ipAddress, File toSend) {
		this.address = ipAddress;
		this.port = portNumber;
		this.toSend = toSend;
	}

	public void choices(File[] listOfFiles, String input) throws IOException {
		switch (input) {
		case "1":
			// SUCCESS SEND
			send(listOfFiles, true);
			break;
		case "2":
			send(listOfFiles, false);
			// Some failures but SUCCESS SEND
			break;
		case "3":
			// Complete Failure -> fail to send
			// TODO: DO THIS THING
			break;
		}
	}

	public void send(File[] files, boolean willScramble) throws IOException {
		Socket socket = new Socket(this.address, this.port);
		BufferedOutputStream bos = new BufferedOutputStream(socket.getOutputStream());
		DataOutputStream dos = new DataOutputStream(bos);
		dos.writeInt(files.length);
		boolean doOnce = willScramble;

		for (int i = 0; i < files.length; i++) {
			long length = files[i].length();
			dos.writeLong(length);

			String name = files[i].getName();
			dos.writeUTF(name);

			FileInputStream fis = new FileInputStream(files[i]);
			BufferedInputStream bis = new BufferedInputStream(fis);

			int theByte = 0;

			while ((theByte = bis.read()) != -1) {
				if (i == 0 && (doOnce == false)) {
					theByte += 1;
					doOnce = true;
				}
				bos.write(theByte);
			}
			bis.close();
		}
		dos.close();
	}

	public void run() throws IOException {
		File[] listOfFiles;
		File toList;

		fs.splitFile(toSend);
		toList = getFile();
		listOfFiles = fm.listOfFiles(toList);

		// Here we split the options
		Scanner g = new Scanner(System.in);
		System.out.println(
				"Which test case? Enter 1-3 ONLY. \n1. Success\n2. Some failure but sent\n3. Complete Failure");
		String input = g.nextLine();
		g.close();
		choices(listOfFiles, input);
	}

	public File getFile() {
		System.out.println("Select a file with the window:");
		JFileChooser chooser = new JFileChooser();
		chooser.setCurrentDirectory(new java.io.File("."));
		chooser.setDialogTitle("Upload File");
		chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		chooser.setAcceptAllFileFilterUsed(false);
		if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			return chooser.getSelectedFile();
		} else {
			return null;
		}
	}
}