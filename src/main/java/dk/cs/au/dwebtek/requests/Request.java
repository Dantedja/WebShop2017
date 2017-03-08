package dk.cs.au.dwebtek.requests;

public interface Request<E> {

    String getPath();
    E parseResponse(String message);

}
