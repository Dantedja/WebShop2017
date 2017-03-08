package dk.cs.au.dwebtek.requests;

import org.jdom2.Document;


public interface PostRequest<E> extends Request<E> {

    Document getPostBody();

}
