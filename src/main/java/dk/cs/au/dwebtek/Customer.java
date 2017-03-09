package dk.cs.au.dwebtek;


public class Customer {
    private int customerID;
    private String customerName;

    Customer(int ID, String name){
        this.customerID = ID;
        this.customerName = name;
    }

    public int getID(){
        return customerID;
    }
    public String getName(){
        return customerName;
    }
}
