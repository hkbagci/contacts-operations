package tr.com.hkerembagci.contactsoperations.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ContactOperationsException extends Exception {

    public ContactOperationsException() {
        super();
    }

    public ContactOperationsException(String message, Throwable cause) {
        super(message, cause);
    }

    public ContactOperationsException(String message) {
        super(message);
    }

    public ContactOperationsException(Throwable cause) {
        super(cause);
    }

}
