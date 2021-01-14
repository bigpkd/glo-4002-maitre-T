package ca.ulaval.glo4002.reservation.reservation.repository.exceptions;

public class ChefNotFoundException extends RuntimeException {
    private static final String message = "Chef %s doesn't exist";
    private static final String error = "CHEF_NOT_FOUND";

    public ChefNotFoundException(String name) {
        super(String.format(message, name));
    }

    public String getError() {
        return error;
    }
}