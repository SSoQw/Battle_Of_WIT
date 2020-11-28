package Testing;

import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
	static ServerSocket serverSocket;
	static Vector<ClientHandler> clients = new Vector<ClientHandler>();
	static boolean hasHost = false;

	public static void main(String[] args) throws IOException {
		serverSocket = new ServerSocket(25565);
		System.out.println("Waiting for users...");

		while (true) {
			try {
				Socket s = serverSocket.accept();
				clients.add(new ClientHandler(s));
			} catch (Exception x) {
				System.out.println(x);
			}
		}
	}

	public static void sendToAll(String[] s) {
		for (ClientHandler x : clients) {
			if (s[1].contains("{quit}")) {
				x.w.printf("%s has left the game.%n", s[0]);
			} else if (s[1].contains("has joined the game!")) {
				x.w.printf("%s %s%n", s[0], s[1]);
			} else {
				x.w.printf("%s: %s%n", s[0], s[1]);
			}
		}
	}

	public static void arithmeticGenerator(String Op) {
		Random rand = new Random();
		int num1;
		int num2;
		int solution;

		switch (Op) {
		case "+":
			num1 = rand.nextInt(100) + 1;
			num2 = rand.nextInt(100) + 1;
			solution = num1 + num2;
			break;

		case "-":
			num1 = rand.nextInt(100) + 1;
			num2 = rand.nextInt(100) + 1;
			solution = num1 - num2;
			break;

		case "*":
			num1 = rand.nextInt(20) + 1;
			num2 = rand.nextInt(20) + 1;
			solution = num1 * num2;
			break;

		case "%":
			num1 = rand.nextInt(400) + 1;
			num2 = rand.nextInt(20) + 1;
			solution = num1 % num2;
			break;
		}
	}
}

class ClientHandler extends Thread {
	Socket connectionSocket;
	PrintStream out;
	PrintWriter w;

	public ClientHandler(Socket s) throws IOException {
		connectionSocket = s;
		start();
	}

	public void run() {
		try {
			boolean isHost = false;

			if (!Server.hasHost) {
				isHost = true;
				Server.hasHost = false;
			}

			boolean done = false;
			out = new PrintStream(new BufferedOutputStream(connectionSocket.getOutputStream()));
			w = new PrintWriter(out, true);
			BufferedReader in = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
			
			String message;
			String[] messageSplit;
			String str = in.readLine();
			
			System.out.println(str + " has joined the game!");

			String[] welcome = new String[2];
			welcome[0] = str;
			welcome[1] = "has joined the game!";
			Server.sendToAll(welcome);
			
			if(isHost) {
				w.write("Welcome "+ str + "! You are the host of the game. Please follow the instructions to get your game setup.");
				w.write("First, would you like to play with Random Aritmetic, Choose a Preset, or Use Custom questions?");
				w.write("Enter which mode you want to play (Random, Preset, Custom) case insensitive: ");
				String gameMode = in.readLine().toLowerCase();
				
				while(!gameMode.equals("random") && !gameMode.equals("preset") && !gameMode.equals("custom")) {
					w.write("No valid gamemode slected, please enter which mode you want to play (Random, Preset, Custom) case insensitive:");
					gameMode = in.readLine().toLowerCase();
				}
				switch(gameMode) {
					case "random":
						w.write("Please enter the operation you would like to use for your aritmetic set (+, -, *, %)");
						String Operation = in.readLine(); 
						
						while(!Operation.equals("+") && !Operation.equals("-") && !Operation.equals("*") && !Operation.equals("%")) {
						w.write("Please enter enter a valid operation(+, -, *, %)");
						Operation = in.readLine(); 
						}
						
						switch(Operation) {
						case "+":
							
						break;
						
						case "-":
							
						break;
						
						case "*":
							
						break;
						
						case "%":
							
						break;
						}
					break;
					
					case "preset":
						w.write("Would you like to use the computer science set, or math set?");
						w.write("Please entere math or compsci, case insesitive: ");
						String set = in.readLine().toLowerCase();
						while(!set.equals("math") && !set.equals("compsci")) {
							w.write("No valid set selected, please entere math or compsci, case insesitive: ");
							set = in.readLine().toLowerCase();
						}
						switch(set) {
						case "math":
						
						break;
						
						case "compsci":
						
						break;
						}
					break;
					
					case "custom":
						w.write("If you haven't already, add your csv file with your question/answer set to the file directory, then restart the server.");
						w.write("If you have already added the file, please enter the name of the file, otherwise enter quit to restrat.");
						w.write("Please enter the file name: );
						String file = in.readLine();
						if(file.toLowerCase().equals("quit")) {
							System.exit(0);
						}
					break;
				}
			}
		

			while (!done) {
				message = in.readLine();
				if (message == null) {
					done = true;
					continue;
				}
				messageSplit = message.split(":::");
				if (messageSplit.length > 1) {
					Server.sendToAll(messageSplit);
				}
			}
		} catch (IOException x) {
			System.out.println(x);
		}
	}
}
