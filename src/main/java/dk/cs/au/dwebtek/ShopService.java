package dk.cs.au.dwebtek;

import org.jdom2.JDOMException;
import org.json.JSONArray;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.HashMap;

@Path("/shop")
public class ShopService {


    /**
     * Our Servlet session. We will need this for the shopping basket
     */
    HttpSession session;
    CloudService service = new CloudService();

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getMessage() {
        return "Hello world!";
    }

    public ShopService(@Context HttpServletRequest servletRequest) {
        session = servletRequest.getSession();
    }

    /**
     * Make the price increase per request (for the sake of example)
     */
    private static int priceChange = 0;

    @GET
    @Path("items")
    @Produces(MediaType.APPLICATION_JSON)
    public String getItems() throws Exception {
        ArrayList<Item> items = null;
        OperationResult<ArrayList<Item>> response = service.listItems();
        if(response.isSuccess()){
            items = response.getResult();
        }
        JSONArray mJSONArray = new JSONArray(items);

        return mJSONArray.toString();
    }

    @POST
    @Path("logout")
    public void logout() {
        session.setAttribute("loginSession", null); // Remove Session
    }

    @POST
    @Path("cart")
    public String addToCart(@FormParam("id") String itemId,
                            @FormParam("stock") int itemStock) {
        putInCart(itemId);
        System.out.println(session.getAttribute("cart"));

        return "";
    }

    @SuppressWarnings("unchecked")
    @POST
    @Path("putInCart")
    public void putInCart(String itemToAdd) {
        HashMap<String, Integer> cart = (HashMap<String, Integer>) session
                .getAttribute("cartMap");

        if (cart == null) {
            cart = new HashMap<String, Integer>();
        }

        if (cart.containsKey(itemToAdd)) {
            System.out.println("itemToAdd exists and is: "
                    + cart.get(itemToAdd));
            cart.put(itemToAdd, cart.get(itemToAdd) + 1);
        } else {
            System.out.println("itemToAdd is created and is: "
                    + cart.get(itemToAdd));
            cart.put(itemToAdd, 1);
        }

        session.setAttribute("cartMap", cart);
    }

    @GET
    @Path("isLoggedIn")
    public String isloggedIn() {
        if (session.getAttribute("loginSession") != null)
            return session.getAttribute("loginSession").toString();
        else
            return "User is not signed in";
    }

    @GET
    @Path("isloggedInBool")
    public boolean isLoggedInBool() {
        return session.getAttribute("loginSession") != null;
    }

    @POST
    @Path("login")
    public String login(@FormParam("user") String user,
                        @FormParam("password") String password) throws JDOMException {


        if(service.login(user,password).isSuccess()){
            if (service.responseCode == 200) {
                session.setAttribute("loginSession", user);
                return "Login succeeded";
            } else {
                System.out.println("Incorrect username or password. Response code: "
                        + service.responseCode);

                return "Login failed";
            }
        }
        return "Login failed";
    }


}
