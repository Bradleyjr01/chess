package Server.Results;

public class MessageResult {
    private String message;
    public MessageResult() { }

    public MessageResult(String msg){
        message = msg;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
