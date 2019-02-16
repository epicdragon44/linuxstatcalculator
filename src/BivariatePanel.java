import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

public class BivariatePanel extends JPanel implements ActionListener {
    private JTextField field1;
    private JTextField field2;
    private JButton enter;
    //CONSTRUCTOR
    public BivariatePanel(boolean expanded) {
        //set size
        if (expanded)
            setPreferredSize( new Dimension( 1900, 500) );
        else
            setPreferredSize( new Dimension(1900, 15) );
        //init variables
        field1 = new JTextField("Enter first variable numbers here, separated by spaces");
        field1.setPreferredSize(new Dimension(1607, 25));
        field1.addFocusListener(new ClearFieldAction(field1));
        field2 = new JTextField("Enter second variable numbers here, separated by spaces");
        field2.setPreferredSize(new Dimension(1500, 25));
        field2.addFocusListener(new ClearFieldAction(field2));
        enter = new JButton("Enter");
        enter.setPreferredSize(new Dimension(100, 25));
        enter.setBackground(Color.LIGHT_GRAY);
        enter.addActionListener(this);

        //draw border
        TitledBorder title = BorderFactory.createTitledBorder("Bivariate Analysis");
        this.setBorder(title);

        //set layout
        setLayout(new FlowLayout());

        //add init variables
        add(field1);
        add(field2);
        add(enter);
    }

    //MOUSE ACTION METHODS
    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }

    //DRAW THE GRAPHS
    public void paint(Graphics g) {
        super.paint(g);
        //draw a scatterplot
        g.drawString("Scatterplot", 10, 100);
        g.drawRect(10, 110, 500, 350);
        //draw a residual plot
        g.drawString("Residual Plot", 550, 100);
        g.drawRect(550, 110, 500, 350);
        //draw the numbers
        g.drawString("Statistics", 1100, 100);
        g.drawRect(1100, 110, 500, 350);
        //draw stuff inside it if the user typed stuff in and hit enter
        if (!(field1.getText().contains("separated by spaces") || field2.getText().contains("separated by spaces"))) {
            //get arrays
            ArrayList<Double> input1list = new ArrayList<>();
            String typedInput1 = field1.getText();
            StringTokenizer st = new StringTokenizer(typedInput1);
            while (st.hasMoreTokens())
                input1list.add(Double.parseDouble(st.nextToken()));
            double[] input1 = new double[input1list.size()];
            for (int i = 0; i < input1.length; i++)
                input1[i] = input1list.get(i);
            Arrays.sort(input1);

            ArrayList<Double> input2list = new ArrayList<>();
            String typedInput2 = field2.getText();
            st = new StringTokenizer(typedInput2);
            while (st.hasMoreTokens())
                input2list.add(Double.parseDouble(st.nextToken()));
            double[] input2 = new double[input2list.size()];
            for (int i = 0; i < input2.length; i++)
                input2[i] = input2list.get(i);
            Arrays.sort(input2);

            //draw statistics inside box
            g.drawString("Correlation Coefficient: "+Functions.calcCorrelCoef(input1, input2), 1110, 125);
            g.drawString("Coefficient of Determination:"+Functions.calcDetermCoef(input1, input2), 1110, 140);

            //draw scatterplot inside box
            double input1Min = Functions.calcMinimum(input1); //input 1 drawn along x axis
            double input2Min = Functions.calcMinimum(input2);
            double input1Max = Functions.calcMaximum(input1); //input 2 drawn along y axis
            double input2Max = Functions.calcMaximum(input2);

            int yMinPixel = 125;
            int yMaxPixel = 445;
            int xMinPixel = 25;
            int xMaxPixel = 495;

            g.drawLine(xMinPixel, yMinPixel, xMinPixel, yMaxPixel); //y axis
            g.drawLine(xMinPixel, yMaxPixel, xMaxPixel, yMaxPixel); //x axis
            g.setFont(new Font("Serif", Font.BOLD, 10));
            for (int i = 0; i < 11; i++) { //draw 10 tick marks along y axis
                g.drawLine(xMinPixel-5, yMinPixel+i*((yMaxPixel-yMinPixel)/10), xMinPixel, yMinPixel+i*((yMaxPixel-yMinPixel)/10));
                String toDraw = input2Max-i*((input2Max-input2Min)/10)+"";
                if (toDraw.length()>4)
                    toDraw = toDraw.substring(0, 4);
                g.drawString(toDraw, xMinPixel-12, yMinPixel+i*((yMaxPixel-yMinPixel)/10)-7);
            }
            for (int i = 0; i < 11; i++) { //draw 10 tick marks along the x axis
                g.drawLine(xMinPixel+i*((xMaxPixel-xMinPixel)/10), yMaxPixel+5, xMinPixel+i*((xMaxPixel-xMinPixel)/10), yMaxPixel);
                String toDraw = input1Min+i*((input1Max-input1Min)/10)+"";
                if (toDraw.length()>4)
                    toDraw = toDraw.substring(0, 4);
                g.drawString(toDraw, xMinPixel+i*((xMaxPixel-xMinPixel)/10)-7, yMaxPixel+15);
            }
            int minLength = Math.min(input1.length, input2.length);
            for (int i = 0; i < minLength; i++) {
                int x = (int)(xMinPixel + (xMaxPixel-xMinPixel)*((input1[i]-input1Min)/(input1Max-input1Min)));
                int y = (int)(yMaxPixel - (yMaxPixel-yMinPixel)*((input2[i]-input2Min)/(input2Max-input2Min)));
                g.fillOval(x-5, y-5, 10, 10);
            }

            //draw residual plot inside box


        }
    }
}
