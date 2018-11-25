package client;

import java.util.Objects;

public class CacheData {

    private String expression;
    private double result;

    CacheData(String expression, double result) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CacheData cache = (CacheData) o;
        return Double.compare(cache.result, result) == 0 && this.expression.equals(cache.expression);
    }

    @Override
    public int hashCode() {
        return Objects.hash(expression, result);
    }
}