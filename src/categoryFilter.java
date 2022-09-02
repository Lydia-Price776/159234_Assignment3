/*This class enables the rows to be filtered by category*/

import javax.swing.*;

public class categoryFilter extends RowFilter {

    private String category;

   public categoryFilter (String category){
        setCategory(category);
    }

        @Override
    public boolean include (Entry entry) {
        return entry.getStringValue(0).equals(category);
    }

    public void setCategory (String category) {
        this.category = category;
    }

}
