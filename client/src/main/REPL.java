import ui.EscapeSequences;

import java.util.Scanner;

import static ui.EscapeSequences.*;

public class REPL {
    public ChessClient client;

    public REPL(String serverUrl) {
        client = new ChessClient(serverUrl);
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

    /*public void notify(Notification notification) {
        System.out.println(RED + notification.message());
        printPrompt();
    }*/

    private void printPrompt() {
        System.out.print("\n" + client.getState() + " >>> ");
    }

}