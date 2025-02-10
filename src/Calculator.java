import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class Calculator implements ActionListener {
    // Componentes gráficos
    private final JFrame frame;
    private final JTextField textField;
    private final JButton[] numberButtons = new JButton[10];
    private JButton addButton, subButton, mulButton, divButton, decButton, equButton, delButton, clrButton, negButton;
    private JPanel panel;

    // Variables para cálculos
    private BigDecimal num1 = BigDecimal.ZERO, num2 = BigDecimal.ZERO, result = BigDecimal.ZERO;
    private char operator = ' ';
    private boolean isResultDisplayed = false;

    // Constructor: configura la ventana y componentes
    public Calculator() {
        frame = new JFrame("Calculator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(420, 550);
        frame.setLayout(null);

        textField = new JTextField();
        textField.setBounds(50, 25, 300, 50);
        textField.setFont(new Font("San Francisco", Font.PLAIN, 30));
        textField.setEditable(false);
        textField.setHorizontalAlignment(JTextField.RIGHT);
        textField.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        initializeButtons(); // Inicializa los botones
        configurePanel();    // Configura el panel de botones
        addComponentsToFrame(); // Agrega componentes a la ventana

        frame.setVisible(true); // Hace visible la ventana
    }

    // Inicializa los botones numéricos y de funciones
    private void initializeButtons() {
        Font buttonFont = new Font("San Francisco", Font.PLAIN, 20);

        String[] functionSymbols = {"+", "-", "×", "÷", ".", "=", "Del", "C", "±"};
        JButton[] functionButtons = new JButton[functionSymbols.length];

        Map<String, JButton> buttonMap = new HashMap<>();
        for (int i = 0; i < functionSymbols.length; i++) {
            functionButtons[i] = new JButton(functionSymbols[i]);
            functionButtons[i].setFont(buttonFont);
            functionButtons[i].setFocusable(false);
            functionButtons[i].setBackground(new Color(245, 245, 245));
            functionButtons[i].setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            functionButtons[i].addActionListener(this);
            buttonMap.put(functionSymbols[i], functionButtons[i]);
        }

        // Asigna botones de funciones
        addButton = buttonMap.get("+");
        subButton = buttonMap.get("-");
        mulButton = buttonMap.get("×");
        divButton = buttonMap.get("÷");
        decButton = buttonMap.get(".");
        equButton = buttonMap.get("=");
        delButton = buttonMap.get("Del");
        clrButton = buttonMap.get("C");
        negButton = buttonMap.get("±");

        // Configura botones numéricos
        for (int i = 0; i < 10; i++) {
            numberButtons[i] = new JButton(String.valueOf(i));
            numberButtons[i].setFont(buttonFont);
            numberButtons[i].setFocusable(false);
            numberButtons[i].setBackground(Color.WHITE);
            numberButtons[i].setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            numberButtons[i].addActionListener(this);
        }
    }

    // Organiza los botones en un panel
    private void configurePanel() {
        panel = new JPanel();
        panel.setBounds(50, 100, 300, 300);
        panel.setLayout(new GridLayout(4, 4, 10, 10));
        panel.setBackground(new Color(240, 240, 240));

        // Agrega botones al panel
        panel.add(numberButtons[1]);
        panel.add(numberButtons[2]);
        panel.add(numberButtons[3]);
        panel.add(addButton);
        panel.add(numberButtons[4]);
        panel.add(numberButtons[5]);
        panel.add(numberButtons[6]);
        panel.add(subButton);
        panel.add(numberButtons[7]);
        panel.add(numberButtons[8]);
        panel.add(numberButtons[9]);
        panel.add(mulButton);
        panel.add(decButton);
        panel.add(numberButtons[0]);
        panel.add(equButton);
        panel.add(divButton);
    }

    // Agrega componentes a la ventana
    private void addComponentsToFrame() {
        frame.add(textField);
        frame.add(panel);

        // Posiciona botones adicionales
        negButton.setBounds(50, 430, 100, 50);
        delButton.setBounds(150, 430, 100, 50);
        clrButton.setBounds(250, 430, 100, 50);

        frame.add(negButton);
        frame.add(delButton);
        frame.add(clrButton);
    }



    // Maneja eventos de los botones
    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        // Botones numéricos
        for (int i = 0; i < 10; i++) {
            if (source == numberButtons[i]) {
                if (isResultDisplayed) {
                    textField.setText("");
                    isResultDisplayed = false;
                }
                textField.setText(textField.getText() + i);
                return;
            }
        }

        // Botón de punto decimal
        if (source == decButton && !textField.getText().contains(".")) {
            textField.setText(textField.getText() + ".");
            return;
        }

        // Botones de operaciones
        if (source == addButton || source == subButton || source == mulButton || source == divButton) {
            handleOperation(((JButton) source).getText().charAt(0));
            return;
        }

        // Botón de igual
        if (source == equButton) {
            calculateResult();
            return;
        }

        // Botón de limpiar
        if (source == clrButton) {
            textField.setText("");
            num1 = BigDecimal.ZERO;
            operator = ' ';
            isResultDisplayed = false;
            return;
        }

        // Botón de borrar
        if (source == delButton) {
            String text = textField.getText();
            textField.setText(text.length() > 1 ? text.substring(0, text.length() - 1) : "");
            return;
        }

        // Botón de negación
        if (source == negButton) {
            toggleNegation();
        }
    }

    // Maneja operaciones (+, -, ×, ÷)
    private void handleOperation(char op) {
        if (!textField.getText().isEmpty()) {
            num1 = new BigDecimal(textField.getText());
            operator = op;
            textField.setText("");
        }
    }

    // Calcula el resultado
    private void calculateResult() {
        if (!textField.getText().isEmpty() && operator != ' ') {
            num2 = new BigDecimal(textField.getText());

            switch (operator) {
                case '+':
                    result = num1.add(num2);
                    break;
                case '-':
                    result = num1.subtract(num2);
                    break;
                case '×':
                    result = num1.multiply(num2);
                    break;
                case '÷':
                    if (num2.compareTo(BigDecimal.ZERO) == 0) {
                        textField.setText("Error");
                        return;
                    }
                    result = num1.divide(num2, 10, BigDecimal.ROUND_HALF_UP);
                    break;
            }

            textField.setText(result.stripTrailingZeros().toPlainString());
            num1 = result;
            isResultDisplayed = true;
            operator = ' ';
        }
    }

    // Cambia el signo del número
    private void toggleNegation() {
        if (!textField.getText().isEmpty()) {
            BigDecimal temp = new BigDecimal(textField.getText()).negate();
            textField.setText(temp.toPlainString());
        }
    }
}