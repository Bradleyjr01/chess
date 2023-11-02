package Server.Requests;

public class HTTPRequest {
    private String requestKeyword;
    private String requestParameters;

    public String getRequestKeyword() {
        return requestKeyword;
    }

    public void setRequestKeyword(String requestKeyword) {
        this.requestKeyword = requestKeyword;
    }

    public String getRequestParameters() {
        return requestParameters;
    }

    public void setRequestParameters(String requestParameters) {
        this.requestParameters = requestParameters;
    }
}

//response objects based on the creating service
//only set services you need
//create the object in the class