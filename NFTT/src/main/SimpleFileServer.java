package main;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class SimpleFileServer {

	private int SOCKET_PORT;
	FileMerger fm = new FileMerger();
	FileSplitter fs = new FileSplitter();
	private boolean flip;

	public SimpleFileServer(int portNumber, boolean flip) {
		this.SOCKET_PORT = portNumber;
		this.flip = flip;
	}

	public void get() throws IOException {

		String dirPath = System.getProperty("user.dir");
		ServerSocket serverSocket = new ServerSocket(this.SOCKET_PORT);
		Socket socket = serverSocket.accept();

		BufferedInputStream bis = new BufferedInputStream(socket.getInputStream());
		DataInputStream dis = new DataInputStream(bis);

		int filesCount = dis.readInt();
		File[] files = new File[filesCount];

		for (int i = 0; i < filesCount; i++) {
			long fileLength = dis.readLong();
			String fileName = dis.readUTF();

			files[i] = new File(dirPath + "/" + fileName);

			FileOutputStream fos = new FileOutputStream(files[i]);
			BufferedOutputStream bos = new BufferedOutputStream(fos);

			for (int j = 0; j < fileLength; j++)
				bos.write(bis.read());

			bos.close();
		}

		dis.close();
	}

	public void run() throws IOException {
		get();
		fm.merge();

		File returnedFile = new File(fm.fileName);
		File cipherFile;
		if (flip == true) {
			cipherFile = new XOR(2).cipher(returnedFile);
		} else {
			cipherFile = new XOR(0).cipher(returnedFile);
		}
		String cs = new CheckSum(cipherFile).checkSum();
		System.out.println("Confirm this checksum with the intial checksum " + cs);
	}
}