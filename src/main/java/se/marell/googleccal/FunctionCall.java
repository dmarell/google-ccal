/*
 * Created by Daniel Marell 15-08-09 21:55
 */
package se.marell.googleccal;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Represents a calendar command - a function call.
 */
public class FunctionCall {
    private String name;
    private List<String> parameters;

    public FunctionCall(String name, List<String> parameters) {
        this.name = name;
        this.parameters = parameters;
    }

    public static FunctionCall parse(String s) {
        String regexWithArgs = "([a-zA-Z0-9]+)\\(([ ,.a-zA-Z0-9]*)\\)";
        Pattern funcPatternWithArgs = Pattern.compile(regexWithArgs);
        Matcher m = funcPatternWithArgs.matcher(s);
        if (!m.matches()) {
            return null;
        }
        if (m.groupCount() <= 1) {
            return null;
        }
        String functionName = m.group(1);
        List<String> parameters = new ArrayList<>();
        if (m.groupCount() == 2) {
            String[] args = m.group(2).split(",");
            for (String a : args) {
                parameters.add(a.trim());
            }
        }
        return new FunctionCall(functionName, parameters);
    }

    public String getName() {
        return name;
    }

    public List<String> getParameters() {
        return parameters;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FunctionCall that = (FunctionCall) o;

        if (!name.equals(that.name)) return false;
        return parameters.equals(that.parameters);

    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + parameters.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "FunctionCall{" +
                "name='" + name + '\'' +
                ", parameters=" + parameters +
                '}';
    }
}
