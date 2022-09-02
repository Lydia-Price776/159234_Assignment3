/* This is the computer table class, it creates a table from the
ComputerTable Model class and creates the browse Products screen */


import javax.swing.*;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import static java.awt.Image.SCALE_SMOOTH;

public class ComputerTable extends JFrame {

    static JTabbedPane tabbedPane = new JTabbedPane();
    static ArrayList<String> categories;
    static ArrayList<String> types;
    private JPanel browseProducts = new JPanel();
    protected static JFrame frame;
    static ComputerTableModel computerTable;
    private static JTable table;
    UpdateProducts products;

    public ComputerTable () {
        super();
        frame = new JFrame();
        ArrayList<Device> allDevices = ReadFile.getAllItems();

        computerTable = new ComputerTableModel(allDevices);
        table = new JTable(computerTable);
        TableRowSorter<ComputerTableModel> filter = new TableRowSorter<>(computerTable);
        table.setRowSorter(filter);
        table.setGridColor(Color.BLACK);


        JPanel tablePanel = new JPanel(new BorderLayout());

        JScrollPane scrollTable = new JScrollPane(table);

        tablePanel.add(table.getTableHeader(), BorderLayout.NORTH);
        tablePanel.add(scrollTable, BorderLayout.CENTER);

        JPanel comboBoxes = new JPanel(new GridLayout(2, 2));

        JComboBox<String> typeDropBox = new JComboBox<>();
        JComboBox<String> categoryDropBox = new JComboBox<>();

        setCategories(allDevices, categoryDropBox);

        categoryDropBox.addActionListener(e -> {
            String categorySelected = String.valueOf(categoryDropBox.getSelectedItem());
            if (!categorySelected.equals("All")) {
                filter.setRowFilter(new categoryFilter(categorySelected));
            } else {
                filter.setRowFilter(null);
            }
            setTypes(allDevices, typeDropBox, categorySelected);

        });

        typeDropBox.addActionListener(e -> {
            String categorySelected = String.valueOf(categoryDropBox.getSelectedItem());
            String typeSelected = String.valueOf(typeDropBox.getSelectedItem());

            if (!typeSelected.equals("All") && !categorySelected.equals("All")) {
                filter.setRowFilter(new typeFilter(typeSelected, categorySelected));
            } else if (typeSelected.equals("All") && !categorySelected.equals("All")) {
                filter.setRowFilter(new categoryFilter(categorySelected));
            } else {
                filter.setRowFilter(null);

            }
        });
        JButton logoutButton = LogoutButton(frame);

        tabbedPane.addTab("Browse Products", null, browseProducts, BorderLayout.WEST);
        products = new UpdateProducts(null);
        tabbedPane.setBackground(new Color(178, 224, 247));

        //Disables Check/Update tab if the current user is not a manager
        if (!LoginDialog.getCurrentUser().getManager()) {
            tabbedPane.setEnabledAt(1, false);
        }

        //Displays selected product information when a product is double-clicked
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked (MouseEvent e) {
                if (e.getClickCount() == 2) {
                    tabbedPane.removeAll();
                    tabbedPane.addTab("Browse Products", null, browseProducts, BorderLayout.WEST);
                    JTable selectedRow = (JTable) e.getSource();
                    int row = selectedRow.getSelectedRow();
                    products = new UpdateProducts(table.getValueAt(row, 2));
                    tabbedPane.setSelectedIndex(1);
                }
            }
        });

        JLabel categoryDropBoxLabel = new JLabel("Computer Category");
        JLabel typeDropBoxLabel = new JLabel("Computer Type");

        comboBoxes.add(categoryDropBoxLabel);
        comboBoxes.add(categoryDropBox);
        comboBoxes.add(typeDropBoxLabel);
        comboBoxes.add(typeDropBox);

        browseProducts.setLayout(new BorderLayout());

        browseProducts.add(tablePanel, BorderLayout.CENTER);
        browseProducts.add(comboBoxes, BorderLayout.NORTH);
        browseProducts.add(logoutButton, BorderLayout.SOUTH);

        frame.add(tabbedPane);
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.setSize(800, 900);
        frame.setResizable(false);
        frame.setVisible(true);
    }

    //This function create the logout button and returns to the login screen
    public static JButton LogoutButton (JFrame frame) {
        JButton logoutButton;
        ImageIcon logo = new ImageIcon("TheComputerStore.png");
        Image image = logo.getImage();
        Image scaledImage = image.getScaledInstance(300, 175, SCALE_SMOOTH);
        logo = new ImageIcon(scaledImage);

        logoutButton = new JButton("Click to logout", logo);
        logoutButton.setHorizontalTextPosition(SwingConstants.RIGHT);
        logoutButton.setFont(new Font("helvetica", Font.BOLD, 30));
        logoutButton.setOpaque(true);
        logoutButton.setBackground(new Color(178, 224, 247));
        logoutButton.setBorder(BorderFactory.createLineBorder(new Color(178, 224, 247)));
        logoutButton.setSize(800, 100);

        logoutButton.addActionListener(e -> {
            new LoginScreen();
            frame.dispose();
            tabbedPane.removeAll();
        });
        return logoutButton;
    }

    //This function sets the categories for the Category Combo Box
    private void setCategories (ArrayList<Device> allDevices, JComboBox<String> categoryDropBox) {
        categories = findCategories(allDevices);
        categoryDropBox.addItem("All");
        for (String category : categories) {
            categoryDropBox.addItem(category);
        }
        categoryDropBox.setEditable(false);
    }

    //This function sets the types for the Type Combo Box
    private void setTypes (ArrayList<Device> allDevices, JComboBox<String> typeDropBox, String selected) {
        types = findTypes(allDevices, selected);
        typeDropBox.removeAllItems();
        typeDropBox.addItem("All");
        for (String type : types) {
            typeDropBox.addItem(type);
        }
    }

    //This function finds all the different types of each Item
    public static ArrayList<String> findTypes (ArrayList<Device> allDevices, String selected) {
        ArrayList<String> types = new ArrayList<>();
        for (Device allDevice : allDevices) {
            if (allDevice.getCategory().equals(selected)) {
                if (!types.contains(allDevice.getType())) {
                    types.add(allDevice.getType());
                }
            }
        }
        return types;
    }

    //This function finds all the different categories of each Item
    public ArrayList<String> findCategories (ArrayList<Device> devices) {
        ArrayList<String> categories = new ArrayList<>();
        for (Device device : devices) {
            if (!categories.contains(device.getCategory())) {
                categories.add(device.getCategory());
            }
        }
        return categories;
    }

    public static JTabbedPane getTabbedPane () {
        return tabbedPane;
    }

    public static ArrayList<String> getCategories () {
        return categories;
    }

    public static ComputerTableModel getComputerTable () {
        return computerTable;
    }
}









