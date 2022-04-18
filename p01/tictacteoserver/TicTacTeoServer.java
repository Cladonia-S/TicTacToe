package net.p01.tictacteoserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class TicTacTeoServer {
	final static int PORT = 9000;
	public static int gameWin(int[] maps) {//-1�̸� ���ھ���, �ƴϸ� �ش� ĭ���ڰ� ����. �ϵ��ڵ�~
		int result=-1;
		if (maps[0]!=-1 && maps[0]==maps[1] && maps[1]==maps[2])
			result= maps[0];
		else if(maps[3]!=-1 && maps[3]==maps[4]&&maps[4]==maps[5])
			result = maps[3];
		else if (maps[6] != -1 &&maps[6]==maps[7]&& maps[7]==maps[8])
			result = maps[6];
		else if (maps[0] != -1 && maps[0]==maps[3] && maps[3]==maps[6])
			result = maps[0];
		else if (maps[1]!= -1 && maps[1]==maps[4]&& maps[4]==maps[7])
			result = maps[1];
		else if (maps[2]!= -1 && maps[2]==maps[5]&& maps[5]==maps[8])
			result =maps[2];
		else if (maps[0]!= -1 && maps[0]==maps[4]&&maps[4]==maps[8])
			result = maps[0];
		else if (maps[2]!=-1 && maps[2]==maps[4]&& maps[4]==maps[6])
			result = maps[2];
		
		return result;
	}
	
	public static String[] parsePacket(String line) {
		String[] params = line.split("\\|");
		return params;
	}
	
	public static void main(String[] args) {
		int[] maps= {-1,-1,-1,-1,-1,-1,-1,-1,-1};//���ʱ�ȭ
		try {
			//���� ����, ��Ʈ�� ����
			ServerSocket serverSocket = new ServerSocket(PORT);
			
			System.out.println("Wait client....");
			Socket conSocket = serverSocket.accept();
			
			InetAddress inetAddr = conSocket.getInetAddress();
			System.out.println("Connect "+ inetAddr.getHostAddress());
			
			OutputStream out =conSocket.getOutputStream();
			OutputStreamWriter outW = new OutputStreamWriter(out);
			PrintWriter pw = new PrintWriter(outW);
			
			InputStream in = conSocket.getInputStream();
			InputStreamReader inR = new InputStreamReader(in);
			BufferedReader br = new BufferedReader(inR); // �ڵ������� ���๮�ڸ� �������� �����͸� ����.
			
			while(true) {
				//Ŭ���̾�Ʈ�� ������ ��ü ��Ŷ�� ����
				String line = br.readLine();
				if(line == null) {
					System.out.println("Disconnect Client!");
					break;
				}
				System.out.println("Recieved Data : "+line);
				String[] params = parsePacket(line);
				int turn = Integer.parseInt(params[0]);
				maps[Integer.parseInt(params[1])]=turn%2;
				int winner=gameWin(maps);//���ڰ� �ִ����� ���� �Լ���.
				String result="";
				if(turn!= 9 && winner==-1) {
					//���� �ȳ����� �� ����
					result="N";
				}else if(winner!= -1) {
					//���ڰ� ����
					result="W|"+(turn%2);
				}else {
					//���� ������, ���ھ���
					result = "D";
				}
				// Ŭ���̾�Ʈ�� ����� ����
				pw.println(result);
				pw.flush();
			}
			pw.close();
			br.close();
					
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}