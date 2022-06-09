package userInterface;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Creates the main initial User Interface for the Organisation
 *
 * @author Kalid Warsame
 */

public class MainControl extends JFrame{

    private String buyOrders[] = {"buy order 1", "buy order 2", "buy order 3", "buy order 4", "buy order 5", "buy order 6", "buy order 7", "buy order 8", "buy order 9", "buy order 10", "buy order 11", "buy order 12", "buy order 13", "buy order 14", "buy order 15"};
    private String sellOrders[] = {"sell order 1", "sell order 2", "sell order 3", "sell order 4", "sell order 5", "sell order 6", "sell order 7", "sell order 8", "sell order 9", "sell order 10", "sell order 11", "sell order 12", "sell order 13", "sell order 14", "sell order 15"};
    private String currentOrders[] = {"Offer 1: Product *** - ****** Unit(s) @ Price $*** per Unit", "Offer 2: Product ***** - ** Unit(s) @ Price $*** per Unit", "Offer 3: Product ***** - * Unit(s) @ Price $******* per Unit", "Offer 4: Product ********* - *** Unit(s) @ Price $**** per Unit", "Offer 5: Product *** - *** Unit(s) @ Price $**** per Unit", "Offer 6: Product ******** - ***** Unit(s) @ Price $** per Unit", "Offer 7: Product **** - ***** Unit(s) @ Price $*** per Unit"};
    private String users[] = {"user 1", "user 2", "user 3", "user 4"};
    private String assetTypes[] = {"Asset Type 1", "Asset Type 2","Asset Type 3","Asset Type 4"};
    private String orderTypes[] = {"Buy", "Sell"};

    public MainControl(){
        super("Control");

        FlowLayout flowLayout = new FlowLayout();
        BorderLayout borderLayout = new BorderLayout();

        JPanel mainPanel = new JPanel(borderLayout);
        mainPanel.setBorder(new TitledBorder("Organisation"));
        add(mainPanel);

        JPanel topPanel = new JPanel(flowLayout);
        topPanel.setBackground(Color.lightGray);
        topPanel.setPreferredSize(new Dimension(1000, 50));
        topPanel.setLayout(new FlowLayout());
        mainPanel.add(topPanel, BorderLayout.PAGE_START);

        JComboBox comboBox = new JComboBox(users);
        topPanel.add(comboBox, FlowLayout.LEFT);

        JPanel bottomPanel =  new JPanel();
        bottomPanel.setBackground(Color.cyan);
        bottomPanel.setPreferredSize(new Dimension(1000, 600));
        bottomPanel.setLayout(new GridLayout(2,2));
        mainPanel.add(bottomPanel, BorderLayout.CENTER);

        JTabbedPane tabbedPane = new JTabbedPane();
        JComponent innerPanel1 = new JPanel();
        tabbedPane.addTab("Buy Orders", innerPanel1);
        tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);
        innerPanel1.setLayout(new BorderLayout());
        innerPanel1.setBackground(Color.decode("#add8e6"));
        innerPanel1.setBorder(new TitledBorder("Available Buy Orders"));

        JList buyOrderList = new JList(buyOrders);
        JScrollPane buyListPane = new JScrollPane(buyOrderList);
        innerPanel1.add(buyListPane, BorderLayout.CENTER);
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new FlowLayout());
        buttonsPanel.add(new JButton("view"), FlowLayout.LEFT);
        buttonsPanel.add(new JButton("delete"), FlowLayout.LEFT);
        innerPanel1.add(buttonsPanel, BorderLayout.SOUTH);

        JComponent innerPanel3 = new JPanel();
        tabbedPane.addTab("Sell Orders", innerPanel3);
        tabbedPane.setMnemonicAt(1, KeyEvent.VK_2);
        innerPanel3.setLayout(new BorderLayout());
        innerPanel3.setBackground(Color.decode("#90EE90"));
        innerPanel3.setBorder(new TitledBorder("Available Sell Orders"));


        JList sellOrderList = new JList(sellOrders);
        JScrollPane sellListPane = new JScrollPane(sellOrderList);
        innerPanel3.add(sellListPane, BorderLayout.CENTER);
        JPanel buttonsPanel3 = new JPanel();
        buttonsPanel3.setLayout(new FlowLayout());
        buttonsPanel3.add(new JButton("view"), FlowLayout.LEFT);
        buttonsPanel3.add(new JButton("delete"), FlowLayout.LEFT);
        innerPanel3.add(buttonsPanel3, BorderLayout.SOUTH);
        bottomPanel.add(tabbedPane);

        JPanel innerPanel2 = new JPanel();
        innerPanel2.setLayout(new BorderLayout());
        innerPanel2.setBackground(Color.decode("#98FB98"));
        innerPanel2.setBorder(new TitledBorder("Your Current Orders"));

        JList currentOrdersList = new JList(currentOrders);
        JScrollPane currentListPane = new JScrollPane(currentOrdersList);
        innerPanel2.add(currentListPane, BorderLayout.CENTER);
        JPanel buttonsPanel2 = new JPanel();
        buttonsPanel2.setLayout(new FlowLayout());
        buttonsPanel2.add(new JButton("view"), FlowLayout.LEFT);
        innerPanel2.add(buttonsPanel2, BorderLayout.SOUTH);
        bottomPanel.add(innerPanel2);

        JPanel innerPanel4 = new JPanel();
        innerPanel4.setLayout(new BorderLayout());
        innerPanel4.setBackground(Color.decode("#66CCCC"));
        innerPanel4.setBorder(new TitledBorder("Create New Offer"));

        JPanel topPanel1 = new JPanel(flowLayout);
        JComboBox comboBox1 = new JComboBox(assetTypes);
        JComboBox comboBox2 = new JComboBox(orderTypes);
        innerPanel4.add(topPanel1, BorderLayout.PAGE_START);
        topPanel1.add(comboBox2, FlowLayout.LEFT);
        topPanel1.add(comboBox1, FlowLayout.LEFT);

        JPanel middlePanel = new JPanel();
        middlePanel.setLayout(new GridLayout(6,1));
        JLabel orderLabel = new JLabel("Enter order name");
        JTextField orderText = new JTextField(30);
        JLabel quantityLabel = new JLabel("Enter quantity");
        JTextField quantityText = new JTextField(10);
        JLabel priceLabel = new JLabel("Enter price");
        JTextField priceText = new JTextField(10);
        middlePanel.add(orderLabel);
        middlePanel.add(orderText);
        middlePanel.add(quantityLabel);
        middlePanel.add(quantityText);
        middlePanel.add(priceLabel);
        middlePanel.add(priceText);
        innerPanel4.add(middlePanel, BorderLayout.CENTER);

        JPanel buttonsPanel4 = new JPanel();
        buttonsPanel4.setLayout(flowLayout);
        buttonsPanel4.add(new JButton("Create order"));
        innerPanel4.add(buttonsPanel4, BorderLayout.SOUTH);
        bottomPanel.add(innerPanel4);



        setSize(1000, 650);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);



    }

    public static void main(String[] args){

        new MainControl();
    }
}
