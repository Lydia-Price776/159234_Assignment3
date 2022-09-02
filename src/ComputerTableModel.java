/*This is the computerTableModel class, it holds information
relevant to creating a table for the browse products screen*/

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

public class ComputerTableModel extends AbstractTableModel {

    private ArrayList<Device> deviceList;


    public ComputerTableModel (ArrayList<Device> devices) {
        setDeviceList(devices);
    }

    @Override
    public int getColumnCount () {
        return 6;
    }

    @Override
    public int getRowCount () {
        return deviceList.size();
    }

    public String getColumnName (int column) {
        return switch (column) {
            case 0 -> "Category";
            case 1 -> "Type";
            case 2 -> "ID";
            case 3 -> "Brand";
            case 4 -> "CPU Family";
            case 5 -> "Price($)";
            default -> null;
        };
    }

    @Override
    public Object getValueAt (int row, int col) {
        if (row < deviceList.size()) {
            return switch (col) {
                case 0 -> deviceList.get(row).getCategory();
                case 1 -> deviceList.get(row).getType();
                case 2 -> deviceList.get(row).getID();
                case 3 -> deviceList.get(row).getBrand();
                case 4 -> deviceList.get(row).getCPU();
                case 5 -> deviceList.get(row).getPrice();
                default -> null;
            };
        }
        return null;
    }


    public void setDeviceList (ArrayList<Device> deviceList) {
        this.deviceList = deviceList;
    }

    public void deleteRow(int row) {
        if (deviceList.size() > 0) {
            deviceList.remove(row);
            fireTableRowsDeleted(row, row);
            ReadFile.setAllItems(deviceList);
        }
    }

    public void addRow(Device itemToAdd) {
            deviceList.add(itemToAdd);
            fireTableDataChanged();
            ReadFile.setAllItems(deviceList);
        }
    }


