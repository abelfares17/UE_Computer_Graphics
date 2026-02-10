package renderer.doc;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * This Class create a plantUML diagram file in FILE_ENDPOINT with fields and
 * method.
 * This class need to tmp.puml file generate from the plantUML Dependancy jar to
 * get
 * the links between Classes.
 *
 * @author tlebobe
 */
public final class CreateClassDiagram {

    /**
     * The UML directory.
     */
    private static final String UML = "doc/uml/";

    /**
     * The output file name.
     */
    private static final String FILE_ENDPOINT = UML + "classDiagram.puml";

    /**
     * The dependant file name.
     */
    private static final String SIMPLEFILE_ENDPOINT = UML
            + "ClassDiagramWithoutMembers.puml";

    /**
     * the buffer size of read.
     */
    private static final int BUFFER_SIZE = 1024;

    /**
     * The list of files to visit.
     */
    private final LinkedList<String> toSee;

    /**
     * The outputFile write.
     */
    private final FileWriter outputFile;

    /**
     * The regex to filter classes.
     */
    private String regex;

    /**
     * Creates a ClassDiagram with a regex.
     *
     * @param regex the regex
     */
    private CreateClassDiagram(final String regex) {
        this.regex = regex;

        // set up outpout file
        final File doc = new File("doc");
        doc.mkdir();
        final File uml = new File("doc/uml");
        uml.mkdir();
        try {
            final File file = new File(FILE_ENDPOINT);
            file.createNewFile();
        } catch (final IOException e) {
            e.printStackTrace();
        }

        FileWriter tmp = null;
        try {
            tmp = new FileWriter(new File(FILE_ENDPOINT));
        } catch (IOException e) {
            e.printStackTrace();
        }
        outputFile = tmp;
        toSee = new LinkedList<>();
    }

    /**
     * Writes the link it founds in the dependant file in the output file.
     *
     * @throws IOException if an error is raised.
     */
    private void makeLink() throws IOException {
        final File simpleOutpFile = new File(SIMPLEFILE_ENDPOINT);
        try (FileReader stringReader = new FileReader(simpleOutpFile)) {
            char[] cbuf = new char[BUFFER_SIZE];
            int nread = 0;
            String line = "";
            while ((nread = stringReader.read(cbuf)) != -1) {
                int i = 0;
                for (char c : cbuf) {
                    // end of buffer
                    if (i > nread) {
                        break;
                    }
                    // end of line
                    if (c == '\n') {
                        // wether the line describe a link
                        if (line.contains(">")) {
                            // paste the line int the outputfile
                            outputFile.write(line + c);
                        }
                        line = "";
                        continue;
                    }
                    line += c;
                    i++;
                }
            }
        }
        // delete the dependant file.
        simpleOutpFile.delete();

        // add hard way the agragation link between lighting and light
        outputFile.write("renderer.model.light.Lighting "
                + "*-- renderer.model.light.Light\n");

        outputFile.flush();
        outputFile.write("@enduml\n");
        outputFile.flush();
    }

    /**
     * Handles the recursive visit of a directory.
     *
     * @param dir the directory to visit.
     */
    private void visitDirectory(final File dir) {
        for (final String file : dir.list()) {
            toSee.add(dir.getPath() + "/" + file);
        }
    }

    /**
     * Compute the class scan and creation with their members.
     *
     * @throws IOException if an error is raised
     */
    private void compute() throws IOException {

        // write start balise
        outputFile.write("@startuml \"Class Diagram\"\n"
                + "title \"Class Diagram\"\n"
                + "hide empty members\n");

        // starting path
        final String endpoint = "build/cls/renderer/";

        // root file
        final File root = new File(endpoint);

        // handle it
        visitDirectory(root);

        // while file has to been seen
        while (!toSee.isEmpty()) {
            final String endpoitntoSee = toSee.pop();
            final File thefile = new File(endpoitntoSee);

            // handle it
            if (thefile.isDirectory()) {
                visitDirectory(thefile);
            } else if (thefile.getName().endsWith(".class")) {
                visitFile(thefile);
            }
        }
    }

    /**
     * Handles a class file.
     *
     * @param thefile the file visited
     * @throws IOException if an error is raised
     */
    private void visitFile(final File thefile) throws IOException {
        String path = thefile.getPath();
        path = path.replace("/", ".").substring(10, path.length() - 6);
        if (regex != null && !path.matches(regex)) {
            return;
        }
        try {
            final Class<?> class1 = Class.forName(path);

            if (class1.isAnonymousClass() || class1.isMemberClass()) {
                return;
            }

            String out = "";
            String type = "";
            if (class1.isAnnotation()) {
                type += "annotation ";
            } else if (class1.isEnum()) {
                type += "enum ";
            } else if (class1.isInterface()) {
                type += "interface ";
            } else {
                boolean exception = false;
                Class<?> superClass = class1.getSuperclass();
                while (superClass != null) {
                    if (superClass.equals(Exception.class)) {
                        exception = true;
                        break;
                    }
                    superClass = superClass.getSuperclass();

                }
                if (exception) {
                    type += "exception ";
                } else {
                    type += "class ";
                }
            }
            out += class1.getName() + " {\n";

            // handles fields
            for (final Field field : class1.getDeclaredFields()) {
                out += "\t";
                int mods = field.getModifiers();
                // accessibility
                if (Modifier.isPublic(mods)) {
                    out += "+ ";
                } else if (Modifier.isPrivate(mods)) {
                    out += "- ";
                } else if (Modifier.isProtected(mods)) {
                    out += "# ";
                } else {
                    out += "~ ";
                }
                if (Modifier.isAbstract(mods)) {
                    out += "{abstract} ";
                }
                if (Modifier.isStatic(mods)) {
                    out += "{static} ";
                }
                out += field.getName() + " : " + field.getType().getSimpleName();
                out += "\n";
            }

            // handle methods
            for (Method method : class1.getDeclaredMethods()) {
                out += "\t";
                int mods = method.getModifiers();
                // accessibility
                if (Modifier.isPublic(mods)) {
                    out += "+ ";
                } else if (Modifier.isPrivate(mods)) {
                    out += "- ";
                } else if (Modifier.isProtected(mods)) {
                    out += "# ";
                } else {
                    out += "~ ";
                }
                if (Modifier.isAbstract(mods)) {
                    out += "{abstract} ";
                    type = "abstract " + type;
                }
                if (Modifier.isStatic(mods)) {
                    out += "{static} ";
                }
                out += method.getName() + "(";
                final Iterator<Class<?>> it = Arrays.asList(method.getParameterTypes()).iterator();
                while (it.hasNext()) {
                    out += it.next().getSimpleName();
                    if (it.hasNext()) {
                        out += ", ";
                    }
                }
                out += ")" + " : " + method.getReturnType().getSimpleName();
                out += "\n";
            }
            out = type + out + "}\n";
            // write the output in the file
            outputFile.write(out);
        } catch (final ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Main entry point to create class diagram.
     *
     * @param args possibly the regex of class to considere
     * @throws IOException if error is raised
     */
    public static void main(final String[] args) throws IOException {
        if (args.length == 2 && args[0].equals("-dp")) {
            final CreateClassDiagram a = new CreateClassDiagram(args[1]);
            a.compute();
            a.makeLink();
        } else {
            System.out.println(usage());
        }
    }

    /**
     * Returns the usage strings.
     *
     * @return the usage as string.
     */
    private static String usage() {
        return "usage :\n"
                + "\t-dp regex" + "\t" + "to define a regex of the classes to considere.";
    }
}
