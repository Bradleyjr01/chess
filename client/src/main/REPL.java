import Resources.Game;
import WebSocket.MessageHandler;
import ui.EscapeSequences;
import webSocketMessages.serverMessages.Notification;
import webSocketMessages.serverMessages.ServerMessage;

import java.util.Scanner;

import static ui.EscapeSequences.*;

public class REPL implements MessageHandler {
    public ChessClient client;

    public REPL(String serverUrl) {
        client = new ChessClient(serverUrl, this);
    }

    public void run() {
        System.out.println("Greetings challenger, welcome to Chess.");
        System.out.println("Please \"register\" or \"login\" to start " +
                "\n or type \"help\" to see all commands");
        //System.out.print(client.help());

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            printPrompt();
            String line = scanner.nextLine();

            try {
                result = client.eval(line);
                System.out.print(result);
            } catch (Throwable e) {
                System.out.print(e.getMessage());
            }
        }
        System.out.println();
    }

    private void printPrompt() {
        System.out.print("\n" + client.getState() + " >>> ");
    }

    @Override
    public void notify(ServerMessage notification) {
        ServerMessage.ServerMessageType type = notification.getServerMessageType();

        printPrompt();
    }

    @Override
    public void updateBoard(Game load) {
        ChessClient.displayBoard(load, "WHITE");
    }

    @Override
    public void message(String notification) {
        System.out.println(notification);
    }

    @Override
    public void error(String err) {
        System.out.println(err);
    }
}