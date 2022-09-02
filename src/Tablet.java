/* This class is the Tablet class.
It is a child class of the device parent class, and
holds information specific to tablets including screen size.
 */


public class Tablet extends Device {

    private float screenSize;

    public Tablet (String category, String type, String ID, String brand, String CPU, float screenSize, float price) {
        super(category, type, ID, brand, CPU, price);
        setScreenSize(screenSize);
    }

    public float getScreenSize () {
        return screenSize;
    }

    public void setScreenSize (float screenSize) {
        this.screenSize = screenSize;
    }

}
