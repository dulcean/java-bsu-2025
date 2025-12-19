package bsu.famcs.nikonchik.lab2.backend.exceptions;

public class CommandExceptions {
    public static class CommandExecutionException extends BankingException {
        public CommandExecutionException(String message, Throwable cause) { super(message, cause);}
    }
}

