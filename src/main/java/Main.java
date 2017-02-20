import com.thoughtworks.qdox.JavaProjectBuilder;
import com.thoughtworks.qdox.model.JavaClass;
import com.thoughtworks.qdox.model.JavaField;
import org.apache.commons.lang3.StringEscapeUtils;

import java.io.File;
import java.io.FileNotFoundException;

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
        printStartArray();
        for (JavaClass javaClass : javaProjectBuilder.getClasses()) {
            printStartObject();
            printFields(javaClass);
            printMethods(javaClass);
            printEndObject();
        }
        printEndArray();
    }

    private void printMethods(JavaClass javaClass) {

    }

    private void printFields(JavaClass javaClass) {
        System.out.print(quote("vars")+":");
        printStartArray();
        for (JavaField javaField : javaClass.getFields()) {
            System.out.print(quote("name") + ":" + quote(javaField.getName()) + COMMA);
            System.out.print(quote("type") + ":" + quote(javaField.getType().getName()) + COMMA);
            System.out.print(quote("modifiers") + ":" + javaField.getModifiers() + COMMA);
            System.out.print(quote("value") + ":" + javaField.getInitializationExpression());
        }
        printEndArray();
    }

    private String quote(String s) {
        return "\"" + s + "\"";
    }

    private void printStartObject() {
        System.out.print("{");
    }

    private void printStartArray() {
        System.out.print("[");
    }

    private void printEndArray() {
        System.out.print("]");
    }

    private void printEndObject() {
        System.out.print("}");
    }
}
