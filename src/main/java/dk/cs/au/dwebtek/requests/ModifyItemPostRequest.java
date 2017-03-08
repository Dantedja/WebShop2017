package dk.cs.au.dwebtek.requests;

import org.jdom2.Document;

public class ModifyItemPostRequest implements PostRequest {
    private String name;
    private Document doc;

    public ModifyItemPostRequest(String name, Document doc) {
        this.name = name;
        this.doc = doc;
    }

    @Override
    public String getPath() {
        return "/modifyItem";
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
