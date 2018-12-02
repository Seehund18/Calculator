public class CacheData {

    private String expression;
    private double result;

    public CacheData(String expression, double result) {
        this.expression = expression;
        this.result = result;
    }

    public String getExpression() {
        return expression;
    }

    public double getResult() {
        return result;
    }

    public void show() {
        System.out.println(expression + "=" + result);
    }
}