import java.util.*;


public class MathEngine {
    private static final String OPERATORS = "+-*/";


    public double calculate(String expression) {

        LinkedList<String> rpn = toRPN(expression);
        return calcResult(rpn);
    }

    /**
     * Метод преобразует мат. выражение в польскую обратную запись (англ. Reverse Polish notation, RPN).
     * Используется алгоритм сортировочной станции.
     *
     * @param expression - математическое выражение
     * @return Польская обратная запись в виде LinkedList<String></String>
     */
    private static LinkedList<String> toRPN(String expression) {
        LinkedList<String> stack = new LinkedList<>();
        LinkedList<String> result = new LinkedList<>();

        StringTokenizer stringTok = new StringTokenizer(expression, OPERATORS + "()", true);

        while (stringTok.hasMoreTokens()) {
            String token = stringTok.nextToken();

            if (isNumber(token)) {
                result.push(token);

            } else if (isOperator(token)) {
                if (stack.size() == 0 || (getPrecedence(token) > getPrecedence(stack.peekFirst()))) {
                    stack.push(token);
                } else {
                    while ( (stack.size() != 0) && getPrecedence(token) <= getPrecedence(stack.peekFirst())) {
                        result.push(stack.pop());
                    }
                    stack.push(token);
                }

            } else if (isOpenBracket(token)) {
                stack.push(token);

            } else if (isClosedBracket(token)) {
                while (!isOpenBracket(stack.peekFirst()) ) {
                    result.push(stack.pop());
                }
                stack.pop();
            }
        }

        while (stack.size() != 0) {
            result.push(stack.pop());
        }
        Collections.reverse(result);

        return result;
    }

    /**
     * Метод вычисляет результат на основе обратной польской записи.
     *
     * @param rpn  Обратная польская запись в виде LinkedList<String></String>
     * @return Результат вычисления
     */
    private static double calcResult(LinkedList<String> rpn) {
        ListIterator<String> iter = rpn.listIterator();
        while (iter.hasNext()) {
            String tmp = iter.next();
            if(OPERATORS.contains(tmp)) {
                double[] lastTwo = getLastTwo(iter);
                switch (tmp) {
                    case "+":
                        iter.add(String.valueOf(lastTwo[0] + lastTwo[1]));
                        break;
                    case "-":
                        iter.add(String.valueOf(lastTwo[0] - lastTwo[1]));
                        break;
                    case "*":
                        iter.add(String.valueOf(lastTwo[0] * lastTwo[1]));
                        break;
                    case "/":
                        iter.add(String.valueOf(lastTwo[0] / lastTwo[1]));
                        break;
                }
            }
        }
        return Double.parseDouble(iter.previous());
    }

    private static double[] getLastTwo(ListIterator<String> iter) {
        double[] result = new double[2];
        iter.previous();
        iter.remove();
        result[1] = Double.parseDouble(iter.previous());
        iter.remove();
        result[0] = Double.parseDouble(iter.previous());
        iter.remove();
        return result;
    }

    private static boolean isClosedBracket(String token) {
        return token.equals(")");
    }

    private static boolean isOpenBracket(String token) {
        return token.equals("(");
    }

    private static boolean isOperator(String token) {
        return OPERATORS.contains(token);
    }

    private static boolean isNumber(String token) {
        try {
            Double.parseDouble(token);
        } catch (NumberFormatException ex) {
            return false;
        }
        return true;
    }

    /**
     * Получаем приоритет операции
     *
     * @param token оператор
     * @return   3 - наивысший приоритет (* и /)
     *           2 - средний приоритет (+ и -)
     *           1 - низкий приоритет  (скобки)
     */
    private static int getPrecedence(String token) {
        if(token.equals("*") || token.equals("/")) {
            return 3;
        } else if (token.equals("+") || token.equals("-")) {
            return 2;
        }
        return 1;
    }
}