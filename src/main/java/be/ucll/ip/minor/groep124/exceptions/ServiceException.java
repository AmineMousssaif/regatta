package be.ucll.ip.minor.groep124.exceptions;

public class ServiceException extends RuntimeException {

    private String action;

    public ServiceException(String action, String message) {
        super(message);
        this.action = action;
    }

    public ServiceException(String message){
        super(message);
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
