package userInterface;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

/**
 * Creates the main initial User Interface for the Organisation
 *
 * @author Kalid Warsame
 */

public class MainControl extends JFrame{


    public MainControl(){
        super("Control");

        FlowLayout flowLayout = new FlowLayout();
        BorderLayout borderLayout = new BorderLayout();
        GridLayout gridLayout = new GridLayout(2, 2);

        JPanel mainPanel = new JPanel(borderLayout);
        mainPanel.setBorder(new TitledBorder("Organisation"));
        add(mainPanel);

        JPanel topPanel = new JPanel(flowLayout);
        topPanel.setBackground(Color.lightGray);
        topPanel.setPreferredSize(new Dimension(1000, 50));
        topPanel.setLayout(new FlowLayout());
        mainPanel.add(topPanel, BorderLayout.PAGE_START);

        String[] users = {"user 1", "user 2", "user 3", "user 4"};
        JComboBox comboBox = new JComboBox(users);
        topPanel.add(comboBox, FlowLayout.LEFT);

        JPanel bottomPanel =  new JPanel(gridLayout);
        bottomPanel.setBackground(Color.cyan);
        bottomPanel.setPreferredSize(new Dimension(1000, 600));
        bottomPanel.setLayout(new GridLayout(2,2));
        mainPanel.add(bottomPanel, BorderLayout.CENTER);

        JPanel innerPanel1 = new JPanel();
        innerPanel1.setBackground(Color.decode("#add8e6"));
        innerPanel1.setBorder(new TitledBorder("Available Buy Orders"));
        bottomPanel.add(innerPanel1);

        JPanel innerPanel2 = new JPanel();
        innerPanel2.setBackground(Color.decode("#98FB98"));
        innerPanel2.setBorder(new TitledBorder("Your Current Orders"));
        bottomPanel.add(innerPanel2);

        JPanel innerPanel3 = new JPanel();
        innerPanel3.setBackground(Color.decode("#90EE90"));
        innerPanel3.setBorder(new TitledBorder("Available Sell Orders"));
        bottomPanel.add(innerPanel3);

        JPanel innerPanel4 = new JPanel();
        innerPanel4.setBackground(Color.decode("#66CCCC"));
        innerPanel4.setBorder(new TitledBorder("Create New Offer"));
        bottomPanel.add(innerPanel4);



        setSize(1000, 650);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);



    }

    public static void main(String[] args){

        new MainControl();
    }
}
