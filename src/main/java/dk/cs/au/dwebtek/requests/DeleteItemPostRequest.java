package dk.cs.au.dwebtek.requests;

import org.jdom2.Document;

public class DeleteItemPostRequest implements PostRequest{
    private Document doc;

    public DeleteItemPostRequest( Document doc) {
        this.doc = doc;
    }

    @Override
    public String getPath() {
        return "/deleteItem";
    }

    @Override
    public Integer parseResponse(String message) {
        return Integer.parseInt(message);
    }

    @Override
    public Document getPostBody() {
        return doc;
    }
}
