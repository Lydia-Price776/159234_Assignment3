/*This class enables the rows to be filtered by type*/


import javax.swing.*;

public class typeFilter extends RowFilter {

    private String type;
    private String category;

    public typeFilter (String type, String category){
        setType(type);
        setCategory(category);
    }

    @Override
    public boolean include (Entry entry) {
        if (entry.getStringValue(0).equals(category)) {
            return entry.getStringValue(1).equals(type);
        }
        return false;
    }

    public void setType (String type) {
        this.type = type;
    }

    public void setCategory (String category) {
        this.category = category;
    }
}
