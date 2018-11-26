package client.GUI;

import client.CacheManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.Map;


public class CalculatorGUI extends JFrame {

    private CacheManager cache;

    private JButton[] digits;
    private HashMap<Character, JButton> symbols;
    private StringBuilder textOnScreen = new StringBuilder();
    private JTextField screen = new JTextField(textOnScreen.toString());


    public CalculatorGUI(CacheManager cache) {
        super("Calculator");
        setSize(300,300);
        digitButtonInitialize();
        symbolButtonInitialize();
        specialButtonInitialize();

        this.cache = cache;

        screen.setPreferredSize(new Dimension(210,50));
        screen.setHorizontalAlignment(JTextField.RIGHT);
        screen.setFont(new Font("Times New Roman",Font.BOLD,30));
        screen.setEditable(false);


        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int res = JOptionPane.showConfirmDialog(CalculatorGUI.this,
                                                        "Exit the calculator?",
                                                        "",
                                                        JOptionPane.YES_NO_OPTION);
                if (res == JOptionPane.YES_OPTION)
                    dispose();
            }
        });


        JPanel flowPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        flowPanel.add(screen);

        JPanel gridPanel = new JPanel(new GridLayout(0,4,10,12));

        gridPanel.add(symbols.get('\u2190'));
        gridPanel.add(symbols.get('C'));
        gridPanel.add(symbols.get('%'));
        gridPanel.add(symbols.get('='));
        for (int i = 0; i < 3; i++) {
            gridPanel.add(digits[i]);
        }
        gridPanel.add(symbols.get('/'));
        for (int i = 3; i < 6; i++) {
            gridPanel.add(digits[i]);
        }
        gridPanel.add(symbols.get('*'));
        for (int i = 6; i < 9; i++) {
            gridPanel.add(digits[i]);
        }
        gridPanel.add(symbols.get('-'));
        gridPanel.add(digits[9]);
        gridPanel.add(symbols.get('.'));
        gridPanel.add(symbols.get('+'));


        flowPanel.add(gridPanel);

        Container container = getContentPane();
        container.add(flowPanel);

        setResizable(false);
        setLocationRelativeTo(null);
    }

    private void digitButtonInitialize() {
        digits = new JButton[10];
        digits[digits.length - 1] = new JButton("0");
        digits[digits.length - 1].addActionListener(new CalcActionListener(digits[digits.length - 1].getText()));

//         Формирует кнопки с числами в порядке 7 8 9
//                                              4 5 6
//                                              1 2 3
        int[] arr = {7,1,-5};
        for (int i = 0; i < digits.length - 1 ; i++) {
            digits[i] = new JButton(Integer.toString(i + arr[i / 3]));
            digits[i].addActionListener(new CalcActionListener(digits[i].getText()));
        }
    }

    private void specialButtonInitialize() {
        JButton button;

        button = new JButton("\u2190");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textOnScreen.deleteCharAt(textOnScreen.length() - 1);
                screen.setText(textOnScreen.toString());
            }
        });
        symbols.put('\u2190',button);

        button = new JButton("C");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textOnScreen.delete(0, textOnScreen.length());
                screen.setText(textOnScreen.toString());
            }
        });
        symbols.put('C', button);

        button = new JButton("=");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if ( cache.isExpressionInCache(textOnScreen.toString()) ) {
                    textOnScreen.delete(0,textOnScreen.length());
                    textOnScreen.append(Double.toString(cache.getFirstResult()));
                    screen.setText(textOnScreen.toString());
                }
                cache.showCache();
            }
        });
        symbols.put('=',button);
    }

    private void symbolButtonInitialize() {
        symbols = new HashMap<>();

        symbols.put('/',new JButton("/"));
        symbols.put('*',new JButton("*"));
        symbols.put('-',new JButton("-"));
        symbols.put('+',new JButton("+"));
        symbols.put('.',new JButton("."));
        symbols.put('%',new JButton("%"));

        for (Map.Entry<Character, JButton> entry : symbols.entrySet()) {
            JButton a = entry.getValue();
            a.addActionListener(new CalcActionListener(a.getText()));
        }
    }

    private class CalcActionListener implements ActionListener {
        private String buttonText;

        private CalcActionListener(String buttonText) {
            this.buttonText = buttonText;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            textOnScreen.append(buttonText);
            screen.setText(textOnScreen.toString());

        }
    }


}



