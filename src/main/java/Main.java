import com.thoughtworks.qdox.JavaProjectBuilder;
import com.thoughtworks.qdox.model.*;
import org.apache.commons.lang3.StringEscapeUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Iterator;

interface testing {
    String quote(String s);
}

public class Main implements testing {

    private static final String COMMA = ",";
    private String END_OBJECT = "}";
    private String START_OBJECT = "{";
    private String END_ARRAY = "]";
    private String START_ARRAY = "[";

    private Main(int t) {
        this.START_ARRAY = "";
    }

    private Main() {

    }

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
        for (JavaSource javaSource : javaProjectBuilder.getSources()) {
            s.append(START_OBJECT);
            getImports(javaSource, s);
            s.append(COMMA);
            getClasses(javaSource, s);
            s.append(END_OBJECT);
        }
        s.append(END_ARRAY);
        System.out.print(s.toString());
    }

    private void getClasses(JavaSource javaSource, StringBuilder s) {
        s.append(quote("classes"))
                .append(":")
                .append(START_ARRAY);
        Iterator<JavaClass> javaClassIterator = javaSource.getClasses().iterator();
        if (javaClassIterator.hasNext()) {
            getClass(s, javaClassIterator.next());
            while (javaClassIterator.hasNext()) {
                s.append(COMMA);
                getClass(s, javaClassIterator.next());
            }
        }
        s.append(END_ARRAY);
    }

    private void getClass(StringBuilder s, JavaClass javaClass) {
        s.append(START_OBJECT);

        s.append(quote("name"))
                .append(":")
                .append(quote(javaClass.getName()));
        s.append(COMMA);

        System.out.println(javaClass.getConstructors());

        s.append(quote("constructors"))
                .append(":")
                .append(START_ARRAY);
        Iterator<JavaConstructor> javaConstructorIterator = javaClass.getConstructors().iterator();
        if (javaConstructorIterator.hasNext()) {
            s.append(quote(javaConstructorIterator.next().getCodeBlock()));
            while (javaConstructorIterator.hasNext()) {
                s.append(COMMA);
                s.append(quote(javaConstructorIterator.next().getCodeBlock()));
            }
        }
        s.append(END_ARRAY);
        s.append(COMMA);

        s.append(quote("implements"))
                .append(":")
                .append(START_ARRAY);
        Iterator<JavaType> javaTypeIterator = javaClass.getImplements().iterator();
        if (javaTypeIterator.hasNext()) {
            s.append(quote(javaTypeIterator.next().getFullyQualifiedName()));
            while (javaTypeIterator.hasNext()) {
                s.append(COMMA);
                s.append(quote(javaTypeIterator.next().getFullyQualifiedName()));
            }
        }
        s.append(END_ARRAY);
        s.append(COMMA);

        s.append(quote("interfaces"))
                .append(":")
                .append(START_ARRAY);
        Iterator<JavaClass> javaClassIterator = javaClass.getInterfaces().iterator();
        if (javaClassIterator.hasNext()) {
            s.append(quote(javaClassIterator.next().getFullyQualifiedName()));
            while (javaClassIterator.hasNext()) {
                s.append(COMMA);
                s.append(quote(javaClassIterator.next().getFullyQualifiedName()));
            }
        }
        s.append(END_ARRAY);
        s.append(COMMA);

        s.append(quote("modifiers"))
                .append(":")
                .append(START_ARRAY);
        Iterator<String> stringIterator = javaClass.getModifiers().iterator();
        if (stringIterator.hasNext()) {
            s.append(quote(stringIterator.next()));
            while (stringIterator.hasNext()) {
                s.append(COMMA);
                s.append(quote(stringIterator.next()));
            }
        }
        s.append(END_ARRAY);
        s.append(COMMA);

        getFields(javaClass, s);
        s.append(COMMA);

        getMethods(javaClass, s);
        s.append(END_OBJECT);
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

    private void getImports(JavaSource javaSource, StringBuilder s) {
        s.append(quote("imports"))
                .append(":")
                .append(START_ARRAY);
        Iterator<String> stringIterator = javaSource.getImports().iterator();
        if (stringIterator.hasNext()) {
            s.append(quote(stringIterator.next()));
            while (stringIterator.hasNext()) {
                s.append(COMMA);
                s.append(quote(stringIterator.next()));
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

    public String quote(String s) {
        return "\"" + StringEscapeUtils.escapeJson(s) + "\"";
    }
}
