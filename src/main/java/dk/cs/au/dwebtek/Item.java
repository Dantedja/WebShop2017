package dk.cs.au.dwebtek;

import javax.lang.model.element.Element;

/**
 * Created by mortenkrogh-jespersen on 28/02/2017.
 */
public class Item {

    private String id;
    private String name;
    private String price;
    private String url;
    private String stock;
    private String description;

    public Item(String id, String name, String price, String url, String stock,
                String description) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.url = url;
        this.stock = stock;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
