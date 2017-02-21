import com.thoughtworks.qdox.JavaProjectBuilder;
import com.thoughtworks.qdox.model.JavaClass;
import com.thoughtworks.qdox.model.JavaField;
import com.thoughtworks.qdox.model.JavaMethod;
import org.apache.commons.lang3.StringEscapeUtils;
import sun.plugin2.message.StartAppletAckMessage;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Iterator;

/**
 * Created by patrick on 2/18/17.
 */
public class Main {


    public static final String COMMA = ",";
    public int test = 0;

    public static void main(String[] args) {
        Main main = new Main();
        try {
            main.start();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void start() throws FileNotFoundException {
        JavaProjectBuilder javaProjectBuilder = new JavaProjectBuilder();
        javaProjectBuilder.addSourceTree(new File("src"));
        StringBuilder s = new StringBuilder();
        s.append(START_ARRAY);
        for (JavaClass javaClass : javaProjectBuilder.getClasses()) {
            s.append(START_OBJECT);
            getFields(javaClass, s);
            s.append(COMMA);
            getMethods(javaClass, s);
            s.append(END_OBJECT);
        }
        s.append(END_ARRAY);
        System.out.print(s.toString());
    }

    private void getMethods(JavaClass javaClass, StringBuilder s) {
        s.append(quote("methods"))
                .append(":")
                .append(START_ARRAY);
        Iterator<JavaMethod> javaMethodIterator = javaClass.getMethods().iterator();
        if (javaMethodIterator.hasNext()) {
            getMethod(javaMethodIterator.next(), s);
            while (javaMethodIterator.hasNext()) {
                s.append(COMMA);
                getMethod(javaMethodIterator.next(), s);
            }
        }
        s.append(END_ARRAY);
    }


    private void getMethod(JavaMethod javaMethod, StringBuilder s) {
        s.append(START_OBJECT)
                .append(quote("name"))
                .append(":")
                .append(quote(javaMethod.getName()))
                .append(COMMA)
                .append(quote("type"))
                .append(":")
                .append(quote(javaMethod.getReturnType().getCanonicalName()))
                .append(COMMA)
                .append(quote("modifiers"))
                .append(":")
                .append(START_ARRAY);
        Iterator<String> stringIterator = javaMethod.getModifiers().iterator();
        if (stringIterator.hasNext()) {
            s.append(quote(stringIterator.next()));
            while (stringIterator.hasNext()) {
                s.append(COMMA);
                s.append(quote(stringIterator.next()));
            }
        }
        s.append(END_ARRAY)
                .append(COMMA)
                .append(quote("source"))
                .append(":")
                .append(quote(javaMethod.getSourceCode()))
                .append(END_OBJECT);
    }

    private void getFields(JavaClass javaClass, StringBuilder s) {
        s.append(quote("vars"))
                .append(":")
                .append(START_ARRAY);
        Iterator<JavaField> javaFieldIterator = javaClass.getFields().iterator();
        if (javaFieldIterator.hasNext()) {
            getField(javaFieldIterator.next(), s);
            while (javaFieldIterator.hasNext()) {
                s.append(COMMA);
                getField(javaFieldIterator.next(), s);
            }
        }
        s.append(END_ARRAY);
    }


    private void getField(JavaField javaField, StringBuilder s) {
        s.append(START_OBJECT)
                .append(quote("name"))
                .append(":")
                .append(quote(javaField.getName()))
                .append(COMMA)
                .append(quote("type"))
                .append(":")
                .append(quote(javaField.getType().getName()))
                .append(COMMA)
                .append(quote("modifiers"))
                .append(":")
                .append(START_ARRAY);
        Iterator<String> stringIterator = javaField.getModifiers().iterator();
        if (stringIterator.hasNext()) {
            s.append(quote(stringIterator.next()));
            while (stringIterator.hasNext()) {
                s.append(COMMA);
                s.append(quote(stringIterator.next()));
            }
        }
        s.append(END_ARRAY)
                .append(COMMA)
                .append(quote("value"))
                .append(":")
                .append(javaField.getInitializationExpression())
                .append(END_OBJECT);
    }

    private String quote(String s) {
        return "\"" + StringEscapeUtils.escapeJson(s) + "\"";
    }

    private String END_OBJECT = "}";
    private String START_OBJECT = "{";
    private String END_ARRAY = "]";
    private String START_ARRAY = "[";
}
