package dk.cs.au.dwebtek.requests;

import org.jdom2.Document;

public class CreateCustomerPostRequest implements PostRequest<Integer>  {

    private Document doc;

    public CreateCustomerPostRequest(Document doc) {
        this.doc = doc;
    }

    @Override
    public String getPath() {
        return "/createCustomer";
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
