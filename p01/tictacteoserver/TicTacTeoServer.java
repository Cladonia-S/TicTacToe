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
	public static int gameWin(int[] maps) {//-1이면 승자없음, 아니면 해당 칸숫자가 승자. 하드코딩~
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
		int[] maps= {-1,-1,-1,-1,-1,-1,-1,-1,-1};//맵초기화
		try {
			//소켓 생성, 스트림 개방
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
			BufferedReader br = new BufferedReader(inR); // 자동적으로 개행문자를 기준으로 데이터를 보냄.
			
			while(true) {
				//클라이언트가 보내는 전체 패킷을 수신
				String line = br.readLine();
				if(line == null) {
					System.out.println("Disconnect Client!");
					break;
				}
				System.out.println("Recieved Data : "+line);
				String[] params = parsePacket(line);
				int turn = Integer.parseInt(params[0]);
				maps[Integer.parseInt(params[1])]=turn%2;
				int winner=gameWin(maps);//승자가 있는지에 대한 함수임.
				String result="";
				if(turn!= 9 && winner==-1) {
					//게임 안끝났고 턴 남음
					result="N";
				}else if(winner!= -1) {
					//승자가 있음
					result="W|"+(turn%2);
				}else {
					//게임 끝났고, 승자없음
					result = "D";
				}
				// 클라이언트로 결과를 전송
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