package dk.cs.au.dwebtek.requests;


import org.jdom2.Document;

public class AdjustItemStockPostRequest implements PostRequest<Integer>  {

    private Document doc;

    public AdjustItemStockPostRequest(Document doc) {
        this.doc = doc;
    }

    @Override
    public String getPath() {
        return "/adjustItemStock";
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
