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
			//��Ʈ�� ����
			Socket socket = new Socket(IP,PORT);
			
			InputStreamReader ink = new InputStreamReader(System.in);
			BufferedReader keyboard = new BufferedReader(ink);
			
			OutputStream out = socket.getOutputStream();
			OutputStreamWriter outW = new OutputStreamWriter(out);
			PrintWriter pw = new PrintWriter(outW);
			
			InputStream in = socket.getInputStream();
			InputStreamReader inR = new InputStreamReader(in);
			BufferedReader br = new BufferedReader(inR);
			//�� �ʱ�ȭ
			String l1="0|1|2";
			String l2="3|4|5";
			String l3="6|7|8";
			int maps[]= {-1,-1,-1,-1,-1,-1,-1,-1,-1};
			int turn=0;
			int player=0;
			System.out.println("Player 0's Turn!\n");
			while(true) {
				//�����
				System.out.println(l1);
				System.out.println(l2);
				System.out.println(l3);
				//�Է¹ޱ�
				System.out.print("input position (0~8, quit)>>  ");
				String cmd = keyboard.readLine();
				if(cmd.equals("quit")) {
					System.out.println("Client Ended!");
					break;
				}
				int num=-1;
				try {//�Է¿��� : ���ڰ� �ƴѰ��� �ԷµȰ��, quit�� ���ܸ� ���� ���� �ξ���.
					num = Integer.parseInt(cmd);
				}catch(NumberFormatException e) {
					System.out.println("���ڰ� �ƴմϴ�. quit�� �ƴ��� ������ ���ڸ� �־��ּ���.");
					continue;
				}
				if (num<0|| num>8) {//0~8������ ��ȣ����, ���� ������ ����� �޼����� ������.
					System.out.println("���ڰ� ���������� ������ϴ�. �ٽ� �Է��� �ֽʽÿ�.");
					continue;
				}
				if (maps[num]!=-1) {// �̹� �ٸ� ����� ���� ������ ���.
					System.out.println("�� ������ �ƴմϴ�. �ٽ� �Է��� �ֽʽÿ�.");
					continue;
				}
				//�� ���� : �ϵ��ڵ�~
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
				//������ ���� ��Ŷ �����
				String packet = String.format("%d|%d", turn, num);	
				//System.out.println("Send Packet");
				pw.println(packet);
				pw.flush();
				
				//����� ����
				String result=br.readLine();
				String[] params=parsePacket(result);
				if (params[0].equals("W")) {// �̱� �÷��̾ ������
					System.out.printf("Player %s is win!!!",params[1]);
					break;
				}else if(params[0].equals("D")) {//���º�
					System.out.println("Draw!");
					break;
				}else {//������ ���� �ȳ���.
					turn++;
					player=turn%2;
					System.out.println();
					System.out.printf("Next! Player %d's Turn!\n", player);
					
				}
			}
			pw.close();
			br.close();
			
		} catch (IOException e) {
			// TODO �ڵ� ������ catch ���
			e.printStackTrace();
		}
		
	}
}