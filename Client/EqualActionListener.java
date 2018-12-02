import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.StringReader;

abstract class EqualActionListener implements ActionListener {

    String prepareExpression(String expression) {
        expression = expression.replace("++", "+");
        expression = expression.replace("--", "+");
        expression = expression.replace("+-", "-");
        expression = expression.replace("-+", "-");
        expression = expression.replace("(-", "(0-");
        if ( expression.charAt(0) == '-' ) {
            expression = "0" + expression;
        }

        if(expression.charAt(0) == '(' && expression.charAt(expression.length() - 1) == ')') {
            expression = expression.substring(1,expression.length() - 1);
        }

        return expression;
    }

    void checkExpression(String expression) {

        if (expression.isEmpty()) {
            throw new IllegalArgumentException("Empty expression");
        }
        if (expression.contains("**") || expression.contains("//")) {
            throw new IllegalArgumentException("Syntax Error");
        }

        int openBracketCount = 0;
        int closeBracketCount = 0;
        try (StringReader stringReader = new StringReader(expression) ) {
            int symbol = stringReader.read();
            while (symbol != -1) {
                char tmp = (char) symbol;

                if (tmp == '(') openBracketCount++;
                if (tmp == ')') closeBracketCount++;
                symbol = stringReader.read();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (openBracketCount != closeBracketCount)
            throw new IllegalArgumentException("Number of close and open brackets differs");
    }
}
