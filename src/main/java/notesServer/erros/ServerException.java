package notesServer.erros;

public class ServerException extends Exception {

    private ServerError serverError;

    public ServerException(ServerError serverError) {
        this.serverError = serverError;
    }

    public ServerError getServerError() {
        return serverError;
    }
}
