/* This class is the DesktopPC class.
It is a child class of the device parent class, and
holds information specific to Desktop Pc's including memory size and SSD.
 */

public class DesktopPC extends Device {

    private int memorySize;
    private int SSD;

    public DesktopPC (String category, String type, String ID, String brand, String CPU, int memorySize, int SSD, float price) {
        super(category, type, ID, brand, CPU, price);
        setMemorySize(memorySize);
        setSSD(SSD);
    }

    public int getMemorySize () {
        return memorySize;
    }

    public void setMemorySize (int memorySize) {
        this.memorySize = memorySize;
    }

    public int getSSD () {
        return SSD;
    }

    public void setSSD (int SSD) {
        this.SSD = SSD;
    }


}
