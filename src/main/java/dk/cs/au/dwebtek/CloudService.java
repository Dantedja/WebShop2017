package dk.cs.au.dwebtek;

import dk.cs.au.dwebtek.requests.*;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Namespace;
import org.jdom2.input.SAXBuilder;
import org.jdom2.input.sax.XMLReaderJDOMFactory;
import org.jdom2.input.sax.XMLReaderXSDFactory;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class CloudService {
    int responseCode;
    private static final String SHOP_KEY = "C6DE2DB6BFC4FC6A853E276F";
    private static final Namespace NS = Namespace.getNamespace("w",
            "http://www.cs.au.dk/dWebTek/2014");
    private static HttpURLConnection conn;

    /**
     * Builds a createItem xml-element
     *
     * @param itemName String with name of new item
     * @return A OperationResult with the XML as a string in the result, if the XML is well-formed.
     */
    public OperationResult<Integer> createItem(String itemName) throws IOException, JDOMException {

        Element root = new Element("createItem", NS);
        Element key = new Element("shopKey", NS);
        key.setText(SHOP_KEY);
        root.addContent(key);
        Element name = new Element("itemName", NS);
        name.setText(itemName);
        root.addContent(name);

        Document doc = new Document(root);

        /**
         * OperationResult validation = validate(doc);
         * if (!validation.isSuccess()) {
         *return OperationResult.Fail(validation.getMessage());
         *}
         */
        StringWriter st = new StringWriter();
        XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
        outputter.output(doc,st);

        CreateItemPostRequest req = new CreateItemPostRequest(itemName, doc);
        OperationResult response = performRequest(req);
        if(response.isSuccess()){
            Document responseDoc = (Document) response.getResult();
            int itemID = Integer.parseInt(responseDoc.getRootElement()
                    .getText());
            return OperationResult.Success(itemID);
        }
        return OperationResult.Fail(response.getMessage(),null);
    }

    /**
     * Builds a modifyItem xml-element
     *
     * @param modifyItem represents an object of Item class.
     * @return A OperationResult with the XML as a string in the result, if the XML is well-formed.
     */
    public OperationResult modifyItem(Item modifyItem)
            throws Exception {


        OperationResult<Element> itemdescr = convertItemDescription
                (modifyItem.getDescription());
        setNamespace(itemdescr.getResult());
        if (!itemdescr.isSuccess()) {
            return OperationResult.Fail(itemdescr.getMessage());
        }

        Document doc = new Document(new Element("modifyItem",NS));
        Element root = doc.getRootElement();

        root.addContent(new Element("itemID").setText(modifyItem.getId()+ ""));
        root.addContent(new Element("itemName").setText(modifyItem.getName() + ""));
        root.addContent(new Element("itemURL").setText(modifyItem.getUrl() +
                ""));
        root.addContent(new Element("itemPrice").setText(modifyItem.getPrice() + ""));
        root.addContent(new Element("itemStock").setText(modifyItem
                .getStock() + ""));
        root.addContent(new Element("itemDescription").addContent(itemdescr
                .getResult()));
        setNamespace(root);

        if (!validate(doc).isSuccess()) {
            return OperationResult.Fail(validate(doc).getMessage());
        }

        ModifyItemPostRequest req = new ModifyItemPostRequest(modifyItem.getName(),
                doc);
        return performRequest(req);
    }

    /**
     * Creates a customer and posts data to cloud
     */
    public OperationResult createCustomer(String userName, String password) throws JDOMException {
        Document doc = new Document();
        Element root = new Element("createCustomer",NS);
        Element shopkey = new Element("shopKey", NS);
        shopkey.setText(SHOP_KEY);
        root.addContent(shopkey);
        Element name = new Element("customerName", NS);
        name.setText(userName);
        root.addContent(name);
        Element pass = new Element("customerPass", NS);
        pass.setText(password);
        root.addContent(pass);

        doc.setRootElement(root);

        CreateCustomerPostRequest req = new CreateCustomerPostRequest(doc);
        return performRequest(req);
    }

    /**
     * Creates a sale and posts data to cloud
     */
    public OperationResult sellItem(int itemID, String customerID, int
            saleAmount)
            throws
            JDOMException {
        Document doc = new Document();
        Element root = new Element("sellItems",NS);
        Element shopkey = new Element("shopKey", NS);
        shopkey.setText(SHOP_KEY);
        root.addContent(shopkey);
        Element name = new Element("itemID", NS);
        name.setText(String.valueOf(itemID));
        root.addContent(name);
        Element pass = new Element("customerID", NS);
        pass.setText(customerID);
        root.addContent(pass);
        Element amount = new Element("saleAmount", NS);
        amount.setText(String.valueOf(saleAmount));
        root.addContent(amount);

        doc.setRootElement(root);

        SellItemsPostRequest req = new SellItemsPostRequest(doc);
        return performRequest(req);
    }

    /**
     * Creates an adjustment to item stock and posts data to cloud
     */
    public OperationResult adjustItemStock(int itemID, int adjustment)
            throws
            JDOMException {
        Document doc = new Document();
        Element root = new Element("adjustItemStock",NS);
        Element shopkey = new Element("shopKey", NS);
        shopkey.setText(SHOP_KEY);
        root.addContent(shopkey);
        Element id = new Element("itemID", NS);
        id.setText(String.valueOf(itemID));
        root.addContent(id);
        Element adjust = new Element("adjustment", NS);
        adjust.setText(String.valueOf(adjustment));
        root.addContent(adjust);

        doc.setRootElement(root);

        AdjustItemStockPostRequest req = new AdjustItemStockPostRequest(doc);
        return performRequest(req);
    }

    /**
     * Creates an adjustment to item stock and posts data to cloud
     */
    public OperationResult login(String customerName, String customerPass)
            throws
            JDOMException {
        Document doc = new Document();
        Element root = new Element("login",NS);
        Element name = new Element("customerName", NS);
        name.setText(customerName);
        root.addContent(name);
        Element pass = new Element("customerPass", NS);
        pass.setText(customerPass);
        root.addContent(pass);

        doc.setRootElement(root);

        LoginPostRequest req = new LoginPostRequest(doc);
        return performRequest(req);
    }


    /**
     * Builds a listItems xml-element
     *
     * @return A OperationResult with the XML as a string in the result, if the XML is well-formed.
     */

    public OperationResult<ArrayList<Item>> listItems() throws Exception {
        ArrayList<Item> itemList = new ArrayList<Item>();
        OperationResult response = performRequest(new ListItemsRequest());
        if (response.isSuccess()){

            Document doc = (Document) response.getResult();
            for(Element child : doc.getRootElement().getChildren()){

                itemList.add(
                        new Item(
                                child.getChildText("itemID",NS),
                                child.getChildText("itemName",NS),
                                child.getChildText("itemPrice",NS),
                                child.getChildText("itemURL",NS),
                                child.getChildText("itemStock",NS),
                                convertItemDescriptionToHTML(child.clone()
                                        .getChild("itemDescription",NS)).getResult()
                                )
                );
            }
            return OperationResult.Success(itemList);
        }
        return OperationResult.Fail("Failed to retrieve item list");
    }

    /**
     * Converts a string of description to an description element.
     *
     * @param content the body of the document-element
     * @return A OperationResult where the result is an element if the xml is well-formed
     */
    private OperationResult<Element> convertItemDescription(String content) {
        content = "<document>" + content + "</document>";
        SAXBuilder sb = new SAXBuilder();
        Document doc;

        try {
            doc = sb.build(new StringReader(content));
        } catch (Exception e) {
            return OperationResult.Fail("Document was not well-formed");

        }
        setNamespace(doc.getRootElement());
        return OperationResult.Success(doc.getRootElement());
    }

    private OperationResult<String> convertItemDescriptionToHTML(Element
    content){
        String result = "";
        result += convertToHTML(content,true);
        result += content.getText();
        for( Element child : content.getChildren()){
            result += convertItemDescriptionToHTML(child);
        }
        result += convertToHTML(content,false);
        return OperationResult.Success(result);
    }


    /**
     * converts element names to html tags
     * @param e element to be converted
     * @param start Boolean to describe if element is start tag
     * @return A string with HTML tags converted from element names
     */

    private String convertToHTML(Element e, Boolean start){
        String result = "<";
        if (!start) {
            result += "/";
        }
        if (e.getName().equals("document")) {
            result += "div>";
        } else if (e.getName().equals("italics")) {
            result += "i>";
        } else if (e.getName().equals("bold")) {
            result += "b>";
        } else if (e.getName().equals("list")) {
            result += "ul>";
        } else if (e.getName().equals("item")) {
            result += "li>";
        } else {
            return "";
        }
        return result;

    }



    /**
     * Sets the namespace on the element - what about the children??
     *
     * @param child the xml-element to have set the namespace
     */
    private void setNamespace(Element child) {
        child.setNamespace(NS);
        for (Element c : child.getChildren()) {
            c.setNamespace(NS);
            setNamespace(c);
        }
    }

    /**
     * Sends POST/GET request to cloud
     */

    private OperationResult performRequest(Request req) throws JDOMException {


        try {
            boolean isPost = req instanceof PostRequest;

            String baseUrl = "http://webtek.cs.au.dk/cloud";
            URL url = new URL(baseUrl + req.getPath());

            SAXBuilder sb = new SAXBuilder();
            conn = (HttpURLConnection) url.openConnection();
            if(isPost){
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.connect();
                PostRequest postReq = (PostRequest) req;
                Document doc = postReq.getPostBody();
                XMLOutputter xmlOutput = new XMLOutputter(Format.getPrettyFormat());
                xmlOutput.output(doc, conn.getOutputStream());

                responseCode = conn.getResponseCode();
                if(responseCode == 200){
                    InputStream response = conn.getInputStream();
                    Document responseDoc = sb.build(response);
                    conn.disconnect();
                    return OperationResult.Success(responseDoc);
                }
                else {
                    return OperationResult.Fail(String.valueOf(conn
                            .getErrorStream()));
                }

            }
            else {
                conn.setRequestMethod("GET");
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.connect();

                responseCode = conn.getResponseCode();

                if(responseCode == 200){
                    InputStream response = conn.getInputStream();
                    Document doc = sb.build(response);

                    return OperationResult.Success(doc);
                }
                else {
                    return OperationResult.Fail(String.valueOf(conn
                            .getErrorStream()));
                }
            }

        } catch (IOException e){
            e.printStackTrace();
        }
        conn.disconnect();

        return OperationResult.Fail("Connection failure: Malformed URL");
    }

    /**
     * Validates the document according to the schema cloud.xsd
     *
     * @param doc
     * @return OperationResult with information about success or failure
     */
    private OperationResult<Document> validate(Document doc) {

        URL url = getClass().getClassLoader().getResource("resources/cloud" +
                ".xsd");
        XMLReaderJDOMFactory factory;

        try {
            factory = new XMLReaderXSDFactory(url);
        } catch (JDOMException e) {
            return OperationResult.Fail("Could not find schema");
        }

        String xml = new XMLOutputter().outputString(doc);
        SAXBuilder builder = new SAXBuilder(factory);
        try {
            builder.build(new StringReader(xml));
        } catch (JDOMException e) {
            return OperationResult.Fail("Xml is not valid: " + e.getMessage());
        } catch (IOException e) {
            return OperationResult.Fail("YIKES: " + e.getMessage());
        }

        return OperationResult.Success(doc);
    }
}
