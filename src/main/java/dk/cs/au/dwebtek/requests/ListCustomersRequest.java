package dk.cs.au.dwebtek.requests;

public class ListCustomersRequest implements Request{
    @Override
    public String getPath() {
        return "/listCustomers";
    }

    @Override
    public Object parseResponse(String message) {
        return null;
    }
}
