package webSocketMessages.serverMessages;

public class Notification extends  ServerMessage{

    String message;

    public Notification(ServerMessageType type, String msg) {
        super(type);
        message = msg;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
