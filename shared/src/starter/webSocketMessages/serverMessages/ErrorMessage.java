package webSocketMessages.serverMessages;

public class ErrorMessage extends ServerMessage {

    String errorMessage;
    public ErrorMessage(ServerMessageType type, String msg) {
        super(type);
        errorMessage = msg;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
