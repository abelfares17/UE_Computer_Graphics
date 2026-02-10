package renderer.controller;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import renderer.core.shader.Shader;

@SuppressWarnings("unchecked")
public final class ShaderFactory {

    /**
     * The set of Class in Shader Package that implements Shader abstract class.
     */
    private static final Set<Class<? extends Shader>> SHADER_SET = new HashSet<>();

    private ShaderFactory() {
    }

    /**
     * Initialize the shader factory : launch classes in shader package which
     * implements
     * Shader abstract class.
     */
    public static void init() {
        // Get the classloader
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        // the package name in which the ShadeFactory is
        String packageName = Shader.class.getPackage().getName();
        String path = packageName.replace('.', '/');

        // get all resources with that path --> it should be just one directory
        Enumeration<URL> resources = null;
        try {
            resources = classLoader.getResources(path);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        // some annoying boilerplate code
        List<File> dirs = new ArrayList<>();

        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            dirs.add(new File(resource.getFile()));
        }
        // dirs now should contain a single directory (even if it is a list) where the
        // .class for Shader are

        System.out.println("Scanning " + packageName + " for Shader implementations...");
        final String[] files = dirs.get(0).list();
        for (String file : files) {
            //System.out.println("Found file: " + files[i]);
            Class<? extends Shader> shader;
            if (!file.endsWith(".class")) {
                System.out.println("Skipping non .class file: " + file);
                continue;
            }
            String classname = file.substring(0, file.lastIndexOf("."));
            try {
                final String shaderPackageName = Shader.class.getPackage().getName();
                final String fullClassName = shaderPackageName + "." + classname;
                shader = (Class<Shader>) Class.forName(fullClassName);
                if (shader.getSuperclass() == Shader.class) {
                    SHADER_SET.add(shader);
                    System.out.println("Found Shader implementation: " + classname);
                } else {
                    System.out.println("Class " + classname + " does not extend Shader");
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                shader = null;
            }
        }
    }

    /**
     * Returns the set of Class in Shader Package that implements Shader abstract
     * class.
     *
     * @return a set
     */
    public static Set<Class<? extends Shader>> getShaderSet() {
        return SHADER_SET;
    }

    /**
     * Create a instance of the given shaderName and return it if it is a success.
     * If the shaderName isn't in the ShaderSet, returns a empty Optional.
     *
     * @param shaderName the name of the shader we want to instantiate.
     * @return a optional of Shader
     */
    public static Optional<Shader> create(String shaderName) {
        for (Class<? extends Shader> class1 : SHADER_SET) {
            if (class1.getSimpleName().equals(shaderName)) {
                try {
                    return Optional.of(class1.getConstructor().newInstance());
                } catch (InstantiationException | IllegalAccessException
                        | IllegalArgumentException | InvocationTargetException
                        | NoSuchMethodException | SecurityException e) {
                    e.printStackTrace();
                }
            }
        }
        return Optional.empty();
    }

    /**
     * Returns the names of classes that implements Shader in a array.
     *
     * @return a array of String
     */
    public static String[] getShaderSetAsStringArray() {
        final String[] res = new String[SHADER_SET.size()];
        int i = 0;
        Iterator<Class<? extends Shader>> iterator = SHADER_SET.iterator();
        while (iterator.hasNext()) {
            res[i++] = iterator.next().getSimpleName();
        }
        return res;
    }
}
