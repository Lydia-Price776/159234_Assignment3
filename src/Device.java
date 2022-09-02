/*
This class is parent class
It is the parent class of Tablet, Desktop PC and Laptop, and it holds the information
relevant to these three different types of computer including its category, type, ID,
brand, CPU and price
 */

public class Device {

    private String category;

    private String type;

    private String ID;

    private String brand;

    private String CPU;

    private float price;

    public Device (String category, String type, String ID, String brand, String CPU, float price) {
        setCategory(category);
        setType(type);
        setID(ID);
        setBrand(brand);
        setCPU(CPU);
        setPrice(price);
    }

    public String getCategory () {
        return category;
    }

    public void setCategory (String category) {
        this.category = category;
    }

    public String getType () {
        return type;
    }

    public void setType (String type) {
        this.type = type;
    }

    public String getID () {
        return ID;
    }

    public void setID (String ID) {
        this.ID = ID;
    }

    public String getBrand () {
        return brand;
    }

    public void setBrand (String brand) {
        this.brand = brand;
    }

    public String getCPU () {
        return CPU;
    }

    public void setCPU (String CPU) {
        this.CPU = CPU;
    }

    public float getPrice () {
        return price;
    }

    public void setPrice (float price) {
        this.price = price;
    }

}
