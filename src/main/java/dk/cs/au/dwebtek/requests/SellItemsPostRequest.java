package dk.cs.au.dwebtek.requests;

import org.jdom2.Document;

public class SellItemsPostRequest implements PostRequest<Integer>  {

    private Document doc;

    public SellItemsPostRequest(Document doc) {
        this.doc = doc;
    }

    @Override
    public String getPath() {
        return "/sellItems";
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
