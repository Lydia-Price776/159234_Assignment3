/* This class is the update products class.
It takes care of the update products tab*/


import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

import static java.awt.BorderLayout.WEST;

public class UpdateProducts extends JPanel {

    Device selectedRow;

    JButton addButton;
    JButton updateButton;
    JButton deleteButton;
    JButton clearButton;

    JPanel productInfo;
    JLabel modelIDLabel;
    JLabel categoryLabel;
    JLabel typeLabel;
    JLabel brandLabel;
    JLabel CPUFamilyLabel;
    JLabel priceLabel;

    JLabel memorySizeLabel;
    JLabel SSDCapacityLabel;
    JTextField memoryField;
    JTextField SSDField;

    JLabel screenSizeLabel;
    JTextField screenSizeField;

    JTextField modelField;
    JComboBox<String> categoryCombo;
    JComboBox<String> typeCombo;
    JTextField brandField;
    JTextField CPUField;
    JTextField priceField;

    public UpdateProducts (Object givenValue) {
        AtomicReference<ArrayList<Device>> devices = new AtomicReference<>(ReadFile.getAllItems());

        if (givenValue != null) {
            for (Device device : devices.get()) {
                if (device.getID().equals(givenValue)) {
                    setSelectedRow(device);
                    break;
                }
            }
        } else {
            Device empty = new Device("", "", "", "", "", 0);
            setSelectedRow(empty);
        }

        JPanel updateProducts = new JPanel();
        updateProducts.setLayout(new BoxLayout(updateProducts, BoxLayout.PAGE_AXIS));

        JPanel managerButtons = new JPanel(new GridLayout(2, 2));
        setNewInfo();

        ArrayList<String> categories = ComputerTable.getCategories();

        // If the current user is a manager, then the additional categories are added on the Update Screen
        if (LoginDialog.getCurrentUser().getManager()) {
            for (String category : categories) {
                if (!category.equals(selectedRow.getCategory())) {
                    categoryCombo.addItem(category);
                }
            }
            categoryCombo.addActionListener(e -> {
                ArrayList<String> types;
                typeCombo.removeAllItems();
                types = ComputerTable.findTypes(devices.get(), (String) categoryCombo.getSelectedItem());
                for (String type : types) {
                    typeCombo.addItem(type);
                }

                if (Objects.requireNonNull(categoryCombo.getSelectedItem()).equals("Desktop PC")) {
                    productInfoAddDesktop();

                } else if (categoryCombo.getSelectedItem().equals("Laptop")) {
                    productInfoAddLaptop();

                } else if (categoryCombo.getSelectedItem().equals("Tablet")) {
                    productInfoAddTablet();
                }
                updateProducts.repaint();
                ComputerTable.frame.repaint();

            });
        }

        productInfoAddDevice();
        productInfoAddPrice();


        switch (selectedRow.getCategory()) {
            case "Desktop PC" -> {
                ArrayList<DesktopPC> desktopPC = ReadFile.getDesktopPC();
                DesktopPC selectedItem = null;
                for (DesktopPC pc : desktopPC) {
                    if (selectedRow.getID().equals(pc.getID())) {
                        selectedItem = pc;
                        break;
                    }
                }
                if (selectedItem != null) {
                    memoryField.setText(String.valueOf(selectedItem.getMemorySize()));
                }
                memoryField.setEditable(LoginDialog.getCurrentUser().getManager());

                if (selectedItem != null) {
                    SSDField.setText(String.valueOf(selectedItem.getSSD()));
                }
                SSDField.setEditable(LoginDialog.getCurrentUser().getManager());

                productInfoAddDesktop();

            }
            case "Laptop" -> {
                ArrayList<Laptop> laptop = ReadFile.getLaptop();
                Laptop selectedItem = null;
                for (Laptop value : laptop) {
                    if (selectedRow.getID().equals(value.getID())) {
                        selectedItem = value;
                        break;
                    }
                }

                assert selectedItem != null;

                memoryField.setText(String.valueOf(selectedItem.getMemorySize()));
                memoryField.setEditable(LoginDialog.getCurrentUser().getManager());

                SSDField.setText(String.valueOf(selectedItem.getSSD()));
                SSDField.setEditable(LoginDialog.getCurrentUser().getManager());

                screenSizeField.setText(String.valueOf(selectedItem.getScreenSize()));
                screenSizeField.setEditable(LoginDialog.getCurrentUser().getManager());

                productInfoAddLaptop();
            }
            case "Tablet" -> {
                ArrayList<Tablet> tablets = ReadFile.getTablet();
                Tablet selectedItem = null;
                for (Tablet tablet : tablets) {
                    if (selectedRow.getID().equals(tablet.getID())) {
                        selectedItem = tablet;
                        break;
                    }
                }
                assert selectedItem != null;
                screenSizeField.setText(String.valueOf(selectedItem.getScreenSize()));
                screenSizeField.setEditable(LoginDialog.getCurrentUser().getManager());

                productInfoAddTablet();
            }
        }

        clearButton.addActionListener(e -> {
            productInfoAddLaptop();
            clearProductScreen(updateProducts);
        });

        deleteButton.addActionListener(e -> {
            boolean foundMatch = false;
            for (int i = 0; i < devices.get().size(); i++) {
                if (modelField.getText().equals(devices.get().get(i).getID())) {
                    foundMatch = true;
                    ComputerTable.getComputerTable().deleteRow(i);
                    devices.set(ReadFile.getAllItems());
                    break;
                }
            }

            if (foundMatch) {
                String currentItem = (String) categoryCombo.getSelectedItem();
                assert currentItem != null;
                switch (currentItem) {
                    case "Desktop":
                        for (int i = 0; i < ReadFile.getDesktopPC().size(); i++) {
                            if (ReadFile.getDesktopPC().get(i).getID().equals(modelField.getText())) {
                                ReadFile.getDesktopPC().remove(i);
                                break;
                            }
                        }
                    case "Laptop":
                        for (int i = 0; i < ReadFile.getLaptop().size(); i++) {
                            if (ReadFile.getLaptop().get(i).getID().equals(modelField.getText())) {
                                ReadFile.getLaptop().remove(i);
                                break;
                            }
                        }

                    case "Tablet":
                        for (int i = 0; i < ReadFile.getTablet().size(); i++) {
                            if (ReadFile.getTablet().get(i).getID().equals(modelField.getText())) {
                                ReadFile.getTablet().remove(i);
                                break;
                            }
                        }
                }
                productInfoAddLaptop();
                clearProductScreen(updateProducts);
                JOptionPane.showMessageDialog(null,
                        "The record for this computer has been deleted successfully");

                productInfo.repaint();
                updateProducts.repaint();
                ComputerTable.frame.repaint();
            } else {
                JOptionPane.showMessageDialog(null,
                        "No Deletion occurred. Please check product details");
            }
        });

        addButton.addActionListener(e -> {
            boolean isUnique = true;

            for (Device device : devices.get()) {
                if (device.getID().equals(modelField.getText())) {
                    JOptionPane.showMessageDialog(null,
                            "A computer matching this ID is already in the system. Please enter a unique ID.");
                    isUnique = false;
                    break;
                }
            }
            if (isUnique && !anyFieldEmpty() && checkInputFloat(priceField.getText())) {

                if (Objects.equals(categoryCombo.getSelectedItem(), "Desktop PC")
                        && checkInputInt(memoryField.getText())
                        && checkInputInt(SSDField.getText())) {
                    ArrayList<DesktopPC> desktopPC = ReadFile.getDesktopPC();
                    desktopPC.add(new DesktopPC((String) categoryCombo.getSelectedItem(),
                            (String) typeCombo.getSelectedItem(), modelField.getText(),
                            brandField.getText(), CPUField.getText(), Integer.parseInt(memoryField.getText()),
                            Integer.parseInt(SSDField.getText()), Float.parseFloat(priceField.getText())));
                    ReadFile.setDesktopPC(desktopPC);
                    addNewComputer();
                    devices.set(ReadFile.getAllItems());

                } else if (Objects.equals(categoryCombo.getSelectedItem(), "Laptop")
                        && checkInputFloat(screenSizeField.getText())
                        && checkInputInt(memoryField.getText())
                        && checkInputInt(SSDField.getText())) {

                    ArrayList<Laptop> laptop = ReadFile.getLaptop();
                    laptop.add(new Laptop((String) categoryCombo.getSelectedItem(),
                            (String) typeCombo.getSelectedItem(), modelField.getText(),
                            brandField.getText(), CPUField.getText(), Integer.parseInt(memoryField.getText()),
                            Integer.parseInt(SSDField.getText()), Float.parseFloat(screenSizeField.getText()),
                            Float.parseFloat(priceField.getText())));
                    ReadFile.setLaptop(laptop);
                    addNewComputer();
                    devices.set(ReadFile.getAllItems());
                } else if (Objects.equals(categoryCombo.getSelectedItem(), "Tablet") && checkInputFloat(screenSizeField.getText())) {
                    ArrayList<Tablet> tablet = ReadFile.getTablet();
                    tablet.add(new Tablet((String) categoryCombo.getSelectedItem(),
                            (String) typeCombo.getSelectedItem(), modelField.getText(),
                            brandField.getText(), CPUField.getText(), Float.parseFloat(screenSizeField.getText()),
                            Float.parseFloat(priceField.getText())));
                    ReadFile.setTablet(tablet);
                    addNewComputer();
                    devices.set(ReadFile.getAllItems());
                }
                updateProducts.repaint();
            } else if (anyFieldEmpty()) {
                JOptionPane.showMessageDialog(null,
                        "One or more fields are empty");
            }
        });

        updateButton.addActionListener(e -> {
            boolean idMatch = false;

            int i;
            for (i = 0; i < devices.get().size(); i++) {
                if (devices.get().get(i).getID().equals(modelField.getText())) {
                    idMatch = true;
                    break;
                }
            }

            if ((idMatch && !anyFieldEmpty() && checkInputFloat(priceField.getText()))) {

                String originalCategory = devices.get().get(i).getCategory();
                String newCategory = (String) categoryCombo.getSelectedItem();

                //If the New category equals the old category it is a simple update
                if (originalCategory.equals(newCategory)) {
                    if ("Desktop PC".equals(newCategory) && checkInputInt(memoryField.getText())
                            && checkInputInt(SSDField.getText())) {
                        ArrayList<DesktopPC> desktopPC = ReadFile.getDesktopPC();
                        for (int j = 0; j < desktopPC.size(); j++) {
                            if (desktopPC.get(j).getID().equals(modelField.getText())) {
                                desktopPC.get(j).setType((String) typeCombo.getSelectedItem());
                                desktopPC.get(j).setBrand(brandField.getText());
                                desktopPC.get(j).setCPU(CPUField.getText());
                                desktopPC.get(j).setPrice(Float.parseFloat(priceField.getText()));
                                desktopPC.get(j).setSSD(Integer.parseInt(SSDField.getText()));
                                desktopPC.get(j).setMemorySize(Integer.parseInt(memoryField.getText()));
                                ReadFile.setDesktopPC(desktopPC);
                                break;
                            }
                        }
                    } else if ("Laptop".equals(newCategory) && checkInputFloat(screenSizeField.getText())
                            && checkInputInt(memoryField.getText())
                            && checkInputInt(SSDField.getText())) {
                        ArrayList<Laptop> laptops = ReadFile.getLaptop();
                        for (int j = 0; j < laptops.size(); j++) {
                            if (laptops.get(j).getID().equals(modelField.getText())) {
                                laptops.get(j).setType((String) typeCombo.getSelectedItem());
                                laptops.get(j).setBrand(brandField.getText());
                                laptops.get(j).setCPU(CPUField.getText());
                                laptops.get(j).setPrice(Float.parseFloat(priceField.getText()));
                                laptops.get(j).setSSD(Integer.parseInt(SSDField.getText()));
                                laptops.get(j).setMemorySize(Integer.parseInt(memoryField.getText()));
                                laptops.get(j).setScreenSize(Float.parseFloat(screenSizeField.getText()));
                                ReadFile.setLaptop(laptops);
                                break;
                            }
                        }
                    } else if ("Tablet".equals(newCategory) && checkInputFloat(screenSizeField.getText())) {
                        ArrayList<Tablet> tablets = ReadFile.getTablet();
                        for (int j = 0; j < tablets.size(); j++) {
                            if (tablets.get(j).getID().equals(modelField.getText())) {
                                tablets.get(j).setType((String) typeCombo.getSelectedItem());
                                tablets.get(j).setBrand(brandField.getText());
                                tablets.get(j).setCPU(CPUField.getText());
                                tablets.get(j).setPrice(Float.parseFloat(priceField.getText()));
                                tablets.get(j).setScreenSize(Float.parseFloat(screenSizeField.getText()));
                                ReadFile.setTablet(tablets);
                                break;
                            }
                        }
                    }
                    //The below accounts for the original category and the new category being different
                } else if (checkInputFloat(priceField.getText())) {
                    if ("Desktop PC".equals(originalCategory) && checkInputInt(memoryField.getText())
                            && checkInputInt(SSDField.getText())) {
                        ArrayList<DesktopPC> desktopPC = ReadFile.getDesktopPC();
                        for (int j = 0; j < desktopPC.size(); j++) {
                            if (desktopPC.get(j).getID().equals(devices.get().get(i).getID())) {
                                desktopPC.remove(j);
                                ReadFile.setDesktopPC(desktopPC);
                                break;
                            }
                        }
                    } else if ("Laptop".equals(originalCategory) && checkInputFloat(screenSizeField.getText())
                            && checkInputInt(memoryField.getText())
                            && checkInputInt(SSDField.getText())) {
                        ArrayList<Laptop> laptops = ReadFile.getLaptop();
                        for (int j = 0; j < laptops.size(); j++) {
                            if (laptops.get(j).getID().equals(devices.get().get(i).getID())) {
                                laptops.remove(j);
                                ReadFile.setLaptop(laptops);
                                break;
                            }
                        }
                    } else if ("Tablet".equals(originalCategory) && checkInputFloat(screenSizeField.getText())) {
                        ArrayList<Tablet> tablets = ReadFile.getTablet();
                        for (int j = 0; j < tablets.size(); j++) {
                            if (tablets.get(j).getID().equals(devices.get().get(i).getID())) {
                                tablets.remove(j);
                                ReadFile.setTablet(tablets);
                                break;
                            }
                        }
                    }

                    String s = Objects.requireNonNull(newCategory);
                    if ("Desktop PC".equals(s) && checkInputInt(memoryField.getText())) {
                        ArrayList<DesktopPC> desktopPC = ReadFile.getDesktopPC();
                        desktopPC.add(new DesktopPC(newCategory, (String) typeCombo.getSelectedItem(),
                                modelField.getText(), brandField.getText(), CPUField.getText(),
                                Integer.parseInt(memoryField.getText()), Integer.parseInt(SSDField.getText()),
                                Float.parseFloat(priceField.getText())));
                        ReadFile.setDesktopPC(desktopPC);
                    } else if ("Laptop".equals(s) && checkInputFloat(screenSizeField.getText())
                            && checkInputInt(memoryField.getText())
                            && checkInputInt(SSDField.getText())) {
                        ArrayList<Laptop> laptops = ReadFile.getLaptop();
                        laptops.add(new Laptop(newCategory, (String) typeCombo.getSelectedItem(),
                                modelField.getText(), brandField.getText(), CPUField.getText(),
                                Integer.parseInt(memoryField.getText()), Integer.parseInt(SSDField.getText()),
                                Float.parseFloat(screenSizeField.getText()),
                                Float.parseFloat(priceField.getText())));
                        ReadFile.setLaptop(laptops);
                    } else if ("Tablet".equals(s) && checkInputFloat(screenSizeField.getText())) {
                        ArrayList<Tablet> tablets = ReadFile.getTablet();
                        tablets.add(new Tablet(newCategory, (String) typeCombo.getSelectedItem(),
                                modelField.getText(), brandField.getText(), CPUField.getText(),
                                Float.parseFloat(screenSizeField.getText()),
                                Float.parseFloat(priceField.getText())));
                        ReadFile.setTablet(tablets);
                    }

                    devices.get().get(i).setCategory((String) categoryCombo.getSelectedItem());
                    devices.get().get(i).setType((String) typeCombo.getSelectedItem());
                    devices.get().get(i).setBrand(brandField.getText());
                    devices.get().get(i).setCPU(CPUField.getText());
                    devices.get().get(i).setPrice(Float.parseFloat(priceField.getText()));
                    ReadFile.setAllItems(devices.get());
                    ComputerTable.getComputerTable().setDeviceList(devices.get());

                    JOptionPane.showMessageDialog(null,
                            "The record for the computer has been updated");
                    productInfo.repaint();
                    updateProducts.repaint();
                    ComputerTable.frame.repaint();
                }

            } else if (anyFieldEmpty()) {
                JOptionPane.showMessageDialog(null,
                        "One or more fields are empty");
            } else {
                JOptionPane.showMessageDialog(null,
                        "There is no match for a computer with that ID");
            }
        });

        if (!LoginDialog.getCurrentUser().getManager()) {
            clearButton.setEnabled(false);
            addButton.setEnabled(false);
            updateButton.setEnabled(false);
            deleteButton.setEnabled(false);
        }
        managerButtons.add(addButton);
        managerButtons.add(updateButton);
        managerButtons.add(deleteButton);
        managerButtons.add(clearButton);
        managerButtons.setSize(200, 100);
        JButton logoutButton = ComputerTable.LogoutButton(ComputerTable.frame);
        logoutButton.setSize(800, 100);
        updateProducts.add(productInfo, CENTER_ALIGNMENT);
        updateProducts.add(managerButtons, CENTER_ALIGNMENT);
        updateProducts.add(logoutButton, LEFT_ALIGNMENT);
        updateProducts.setSize(700, 800);
        updateProducts.setBackground(new Color(178, 224, 247));

        JTabbedPane tabbedPane = ComputerTable.getTabbedPane();
        tabbedPane.addTab("Check/Update Product Details", null, updateProducts, WEST);
    }

    //This function checks if there is an empty field, and returns true if there is
    private boolean anyFieldEmpty () {
        Object currentItem = Objects.requireNonNull(categoryCombo.getSelectedItem());

        if (modelField.getText().isEmpty()) {
            return true;
        }
        if (brandField.getText().isEmpty()) {
            return true;
        }
        if (CPUField.getText().isEmpty()) {
            return true;
        }
        if (priceField.getText().isEmpty()) {
            return true;
        }
        if (SSDField.getText().isEmpty() && (currentItem.equals("Desktop PC") || currentItem.equals("Laptop"))) {
            return true;
        }
        if (memoryField.getText().isEmpty() && (currentItem.equals("Desktop PC") || currentItem.equals("Laptop"))) {
            return true;
        }
        if (screenSizeField.getText().isEmpty() && (currentItem.equals("Tablet") || currentItem.equals("Laptop"))) {
            return true;
        }

        return false;
    }

    //This function clears the product screen
    private void clearProductScreen (JPanel updateProducts) {
        modelField.setText("");
        brandField.setText("");
        CPUField.setText("");
        memoryField.setText("");
        SSDField.setText("");
        screenSizeField.setText("");
        priceField.setText("");
        productInfo.repaint();
        updateProducts.repaint();
        ComputerTable.frame.repaint();
    }

    //This function adds the product information regarding desktops to the Product Info Panel
    private void productInfoAddDesktop () {
        productInfoAddDevice();
        productInfoAddMemorySSD();
        productInfoAddPrice();
    }

    //This function adds the product information regarding Tablets to the Product Info Panel
    private void productInfoAddTablet () {
        productInfoAddDevice();
        productInfoAddScreenSize();
        productInfoAddPrice();
    }

    //This function adds the price to the Product Info Panel
    private void productInfoAddPrice () {
        productInfo.add(priceLabel);
        productInfo.add(priceField);
    }

    //This function adds the product information relevant to all devices to the Product Info Panel
    private void productInfoAddDevice () {
        productInfo.removeAll();

        productInfo.add(modelIDLabel);
        productInfo.add(modelField);

        productInfo.add(categoryLabel);
        productInfo.add(categoryCombo);

        productInfo.add(typeLabel);
        productInfo.add(typeCombo);

        productInfo.add(brandLabel);
        productInfo.add(brandField);

        productInfo.add(CPUFamilyLabel);
        productInfo.add(CPUField);
    }

    //This function adds the product information regarding laptops to the Product Info Panel
    private void productInfoAddLaptop () {
        productInfoAddDevice();
        productInfoAddMemorySSD();
        productInfoAddScreenSize();
        productInfoAddPrice();
    }

    //This function adds the Screen Size to the Product Info Panel

    private void productInfoAddScreenSize () {
        productInfo.add(screenSizeLabel);
        productInfo.add(screenSizeField);
    }

    //This function adds the Memory Size and SSD Capacity to the Product Info Panel

    private void productInfoAddMemorySSD () {
        productInfo.add(memorySizeLabel);
        productInfo.add(memoryField);

        productInfo.add(SSDCapacityLabel);
        productInfo.add(SSDField);
    }

    //This function adds a new computer to the database and to the table
    private void addNewComputer () {
        Device currentItem = new Device((String) categoryCombo.getSelectedItem(),
                (String) typeCombo.getSelectedItem(), modelField.getText(),
                brandField.getText(), CPUField.getText(), Float.parseFloat(priceField.getText()));
        ComputerTable.getComputerTable().addRow(currentItem);

        JOptionPane.showMessageDialog(null,
                "New Computer added successfully");
        productInfo.repaint();
        ComputerTable.frame.repaint();
    }


    public void setSelectedRow (Device selectedRow) {
        this.selectedRow = selectedRow;
    }

    //This function sets up the info
    private void setNewInfo () {
        addButton = new JButton("Add");
        updateButton = new JButton("Update");
        deleteButton = new JButton("Delete");
        clearButton = new JButton("Clear");

        productInfo = new JPanel(new GridLayout(9, 2));
        modelIDLabel = new JLabel("Model ID:");
        categoryLabel = new JLabel("Category:");
        typeLabel = new JLabel("Type:");
        brandLabel = new JLabel("Brand:");
        CPUFamilyLabel = new JLabel("CPU Family");
        priceLabel = new JLabel("Price:");

        memorySizeLabel = new JLabel("Memory Size:");
        SSDCapacityLabel = new JLabel("SSD Capacity:");
        memoryField = new JTextField();
        SSDField = new JTextField();

        screenSizeLabel = new JLabel("Screen Size:");
        screenSizeField = new JTextField();

        modelField = new JTextField();
        categoryCombo = new JComboBox<>();
        typeCombo = new JComboBox<>();
        brandField = new JTextField();
        CPUField = new JTextField();
        priceField = new JTextField();

        modelField.setText(selectedRow.getID());
        brandField.setText(selectedRow.getBrand());
        CPUField.setText(selectedRow.getCPU());
        priceField.setText(String.valueOf(selectedRow.getPrice()));
        categoryCombo.addItem(selectedRow.getCategory());
        typeCombo.addItem(selectedRow.getType());

        categoryCombo.setEditable(false);
        typeCombo.setEditable(false);
        modelField.setEditable(LoginDialog.getCurrentUser().getManager());
        brandField.setEditable(LoginDialog.getCurrentUser().getManager());
        CPUField.setEditable(LoginDialog.getCurrentUser().getManager());
        priceField.setEditable(LoginDialog.getCurrentUser().getManager());

        setTextTip();
    }

    //This function sets tips for each of the Text Fields if the current user is a manager
    private void setTextTip () {
        if (LoginDialog.getCurrentUser().getManager()) {
            modelField.setToolTipText("ID");
            brandField.setToolTipText("Brand");
            CPUField.setToolTipText("CPU Family");
            priceField.setToolTipText("Price as a floating point number");
            SSDField.setToolTipText("SSD Capacity as an Integer");
            memoryField.setToolTipText("Memory Size as an Integer");
            screenSizeField.setToolTipText("Screen Size as a floating point number");
        }
    }

    //This function checks if a given string is a Floating point number
    private boolean checkInputFloat (String input) {
        try {
            Float.parseFloat(input);
            return true;
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null,
                    "Invalid Input. Please check product details");
        }
        return false;
    }

    //This function checks if a given string is an Integer number
    private boolean checkInputInt (String input) {
        try {
            Integer.parseInt(input);
            return true;
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null,
                    "Invalid Input. Please check product details");
        }
        return false;
    }
}
