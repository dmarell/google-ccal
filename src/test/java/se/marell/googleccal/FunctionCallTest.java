/*
 * Created by Daniel Marell 15-08-09 21:16
 */
package se.marell.googleccal;

import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class FunctionCallTest {
    @Test
    public void shouldParseNoParameters() {
        FunctionCall fc = FunctionCall.parse("myFunction()");
        assertThat(fc.getName(), is("myFunction"));
    }

    @Test
    public void shouldParseOneParameter() {
        FunctionCall fc = FunctionCall.parse("myFunction(a)");
        assertThat(fc.getName(), is("myFunction"));
        assertThat(fc.getParameters().size(), is(1));
        assertThat(fc.getParameters().get(0), is("a"));
    }

    @Test
    public void shouldParseTwoParameters() {
        FunctionCall fc = FunctionCall.parse("myFunction(a,b)");
        assertThat(fc.getName(), is("myFunction"));
        assertThat(fc.getParameters().size(), is(2));
        assertThat(fc.getParameters().get(0), is("a"));
        assertThat(fc.getParameters().get(1), is("b"));
    }

    @Test
    public void shouldParseTwoParametersWithExtraSpace() {
        FunctionCall fc = FunctionCall.parse("myFunction(a, b)");
        assertThat(fc.getName(), is("myFunction"));
        assertThat(fc.getParameters().size(), is(2));
        assertThat(fc.getParameters().get(0), is("a"));
        assertThat(fc.getParameters().get(1), is("b"));
    }

    @Test
    public void shouldNotParse1() {
        FunctionCall fc = FunctionCall.parse("");
        assertThat(fc, nullValue());
    }

    @Test
    public void shouldNotParse2() {
        FunctionCall fc = FunctionCall.parse("()");
        assertThat(fc, nullValue());
    }

    @Test
    public void shouldNotParse3() {
        FunctionCall fc = FunctionCall.parse("function");
        assertThat(fc, nullValue());
    }

    @Test
    public void shouldNotParse4() {
        FunctionCall fc = FunctionCall.parse("FOO:BAR");
        assertThat(fc, nullValue());
    }

    //@Test
    public void printInfoAbout() {
        printInfo("properCase(ARG1, ARG2, class.otherArg)");
        printInfo("myFunction(a,b,c)");
        printInfo("myFunction(a)");
        printInfo("myFunction()");
    }

    private void printInfo(String s) {
        String regex = "([a-zA-Z0-9]+)\\(([ ,.a-zA-Z0-9]*)\\)";
        Pattern funcPattern = Pattern.compile(regex);
        Matcher m = funcPattern.matcher(s);
        System.out.println("Match found: " + m.matches());
        System.out.println("Total matches are: " + m.groupCount());
        if (m.matches()) {
            for (int index = 0; index <= m.groupCount(); index++) {
                System.out.println("Group number " + index + " is: " + m.group(index));
            }
        }
        System.out.println("Arguments: ");
        if (m.groupCount() == 2) {
            String[] args = m.group(2).split(",");
            for (String a : args) {
                System.out.println("   " + a.trim());
            }
        }
        System.out.println();
    }
}
