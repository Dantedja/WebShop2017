package dk.cs.au.dwebtek.requests;

public class ListItemsRequest implements Request{
    @Override
    public String getPath() {
        return "/listItems?shopID=457";
    }

    @Override
    public Object parseResponse(String message) {
        return null;
    }
}
