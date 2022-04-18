package net.p02.tictacteoclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class TicTacTeoClient {
	final static String IP="127.0.0.1";
	final static int PORT = 9000;
	
	public static String[] parsePacket(String line) {
		String[] params = line.split("\\|");
		return params;
	}
	
	public static void main(String[] args) {
		try {
			//스트림 설정
			Socket socket = new Socket(IP,PORT);
			
			InputStreamReader ink = new InputStreamReader(System.in);
			BufferedReader keyboard = new BufferedReader(ink);
			
			OutputStream out = socket.getOutputStream();
			OutputStreamWriter outW = new OutputStreamWriter(out);
			PrintWriter pw = new PrintWriter(outW);
			
			InputStream in = socket.getInputStream();
			InputStreamReader inR = new InputStreamReader(in);
			BufferedReader br = new BufferedReader(inR);
			//판 초기화
			String l1="0|1|2";
			String l2="3|4|5";
			String l3="6|7|8";
			int maps[]= {-1,-1,-1,-1,-1,-1,-1,-1,-1};
			int turn=0;
			int player=0;
			System.out.println("Player 0's Turn!\n");
			while(true) {
				//판출력
				System.out.println(l1);
				System.out.println(l2);
				System.out.println(l3);
				//입력받기
				System.out.print("input position (0~8, quit)>>  ");
				String cmd = keyboard.readLine();
				if(cmd.equals("quit")) {
					System.out.println("Client Ended!");
					break;
				}
				int num=-1;
				try {//입력예외 : 숫자가 아닌것이 입력된경우, quit는 예외를 위해 위에 두었음.
					num = Integer.parseInt(cmd);
				}catch(NumberFormatException e) {
					System.out.println("숫자가 아닙니다. quit이 아닌한 지정된 숫자를 넣어주세요.");
					continue;
				}
				if (num<0|| num>8) {//0~8까지의 번호지정, 지정 범위를 벗어날시 메세지를 보낸다.
					System.out.println("숫자가 지정범위를 벗어났습니다. 다시 입력해 주십시오.");
					continue;
				}
				if (maps[num]!=-1) {// 이미 다른 사람이 놓은 공간일 경우.
					System.out.println("빈 공간이 아닙니다. 다시 입력해 주십시오.");
					continue;
				}
				//말 놓기 : 하드코딩~
				if(num==0) {
					if(player == 0 )
						l1=l1.replace("0", "O");
					else{
						l1=l1.replace("0","X");
					}
				}else if(num==1) {
					if(player == 0 )
						l1=l1.replace("1", "O");
					else{
						l1=l1.replace("1","X");
					}
				}else if(num==2) {
					if(player == 0 )
						l1=l1.replace("2", "O");
					else{
						l1=l1.replace("2","X");
					}
				}else if(num==3) {
					if(player == 0 )
						l2=l2.replace("3", "O");
					else{
						l2=l2.replace("3","X");
					}
				}else if(num==4) {
					if(player == 0 )
						l2=l2.replace("4", "O");
					else{
						l2=l2.replace("4","X");
					}
				}else if(num==5) {
					if(player == 0 )
						l2=l2.replace("5", "O");
					else{
						l2=l2.replace("5","X");
					}
				}else if(num==6) {
					if(player == 0 )
						l3=l3.replace("6", "O");
					else{
						l3=l3.replace("6","X");
					}
				}else if(num==7) {
					if(player == 0 )
						l3=l3.replace("7", "O");
					else{
						l3=l3.replace("7","X");
					}
				}else if(num==8) {
					if(player == 0 )
						l3=l3.replace("8", "O");
					else{
						l3=l3.replace("8","X");
					}
				}
				maps[num]=player;
				//서버에 보낼 패킷 만들기
				String packet = String.format("%d|%d", turn, num);	
				//System.out.println("Send Packet");
				pw.println(packet);
				pw.flush();
				
				//결과값 수신
				String result=br.readLine();
				String[] params=parsePacket(result);
				if (params[0].equals("W")) {// 이긴 플레이어가 있을떄
					System.out.printf("Player %s is win!!!",params[1]);
					break;
				}else if(params[0].equals("D")) {//무승부
					System.out.println("Draw!");
					break;
				}else {//게임이 아직 안끝남.
					turn++;
					player=turn%2;
					System.out.println();
					System.out.printf("Next! Player %d's Turn!\n", player);
					
				}
			}
			pw.close();
			br.close();
			
		} catch (IOException e) {
			// TODO 자동 생성된 catch 블록
			e.printStackTrace();
		}
		
	}
}