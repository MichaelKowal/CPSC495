/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package unbc.ca.distributed.library;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.uncommons.maths.random.AESCounterRNG;
import org.uncommons.maths.random.SeedException;
import unbc.ca.distributed.distributions.BinomialDistribution;
import unbc.ca.distributed.distributions.ExponentialDistribution;
import unbc.ca.distributed.distributions.GaussianDistribution;
import unbc.ca.distributed.distributions.Generator;
import unbc.ca.distributed.distributions.PoissonDistribution;
import unbc.ca.distributed.distributions.UniformDistribution;
import unbc.ca.distributed.simDistributed.Dist_Algorithm;

/**
 *
 * @author behnish
 */
public class Utilites {

    public static List<Class> getClassesForPackage(String pkgname) {
        List<Class> classes = new ArrayList<>();

        // Get a File object for the package
        File directory = null;
        String fullPath;
        String relPath = pkgname.replace('.', '/');

        URL resource = ClassLoader.getSystemClassLoader().getResource(relPath);

        if (resource == null) {
            throw new RuntimeException("No resource for " + relPath);
        }
        fullPath = resource.getFile();

        try {
            directory = new File(resource.toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(pkgname + " (" + resource + ") does not appear to be a valid URL / URI.  Strange, since we got it from the system...", e);
        } catch (IllegalArgumentException e) {
            directory = null;
        }

        if (directory != null && directory.exists()) {

            // Get the list of the files contained in the package
            String[] files = directory.list();
            for (int i = 0; i < files.length; i++) {

                // we are only interested in .class files
                if (files[i].endsWith(".class")) {

                    // removes the .class extension
                    String className = pkgname + '.' + files[i].substring(0, files[i].length() - 6);
                    try {
                        classes.add(Class.forName(className));
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException("ClassNotFoundException loading " + className);
                    }
                }
            }
        } else {
            try {
                String jarPath = fullPath.replaceFirst("[.]jar[!].*", ".jar").replaceFirst("file:", "");
                JarFile jarFile = new JarFile(jarPath);
                Enumeration<JarEntry> entries = jarFile.entries();
                while (entries.hasMoreElements()) {
                    JarEntry entry = entries.nextElement();
                    String entryName = entry.getName();
                    if (entryName.startsWith(relPath) && entryName.length() > (relPath.length() + "/".length())) {
                        String className = entryName.replace('/', '.').replace('\\', '.').replace(".class", "");

                        try {
                            classes.add(Class.forName(className));
                        } catch (ClassNotFoundException e) {
                            throw new RuntimeException("ClassNotFoundException loading " + className);
                        }
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(pkgname + " (" + directory + ") does not appear to be a valid package", e);
            }
        }
        return classes;
    }

    public static ArrayList<String> returnNames() {
        Utilites u = new Utilites();
        ArrayList<String> classNames = new ArrayList<>();

        List<Class> classes = Utilites.getClassesForPackage("unbc.ca.distributed.algorithms");

        for (Iterator<Class> it = classes.iterator(); it.hasNext();) {
            Class class1 = it.next();

            if (!class1.getName().contains("$")) {
                String name = class1.getName();

                name = name.replace('.', '/');

                String[] names;
                names = name.split("/");
                classNames.add(names[4]);
            }
        }

        return classNames;
    }

    public static String[] names() {
        String[] namesCl = new String[Utilites.returnNames().size()];

        for (int i = 0; i < Utilites.returnNames().size(); i++) {
            namesCl[i] = Utilites.returnNames().get(i);
        }
        return namesCl;
    }

    public static Algorithm returnObject(String name, int nodeId) {
        name = "unbc.ca.distributed.algorithms." + name;
        Algorithm algorithmObject;
        try {
            try {
                algorithmObject = (Algorithm) Class.forName(name).newInstance();
                return algorithmObject;
            } catch (InstantiationException | IllegalAccessException ex) {
                Logger.getLogger(Utilites.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Utilites.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static Dist_Algorithm returnObject_dist(String name) throws ClassNotFoundException{
        name = "unbc.ca.distributed.algorithms." + name;
        Dist_Algorithm algorithmObject;
        try {
            try {
                System.out.println(name);
                algorithmObject = (Dist_Algorithm) Class.forName(name).newInstance();
                return algorithmObject;
            } catch (InstantiationException | IllegalAccessException ex) {
                Logger.getLogger(Utilites.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Utilites.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static Generator returnDistribution(String type, double mean, double variance) {
        Generator u;
        switch (type) {
            case "Binomial":
                u = new BinomialDistribution((int)mean, variance);
                u.setGenerator(new Random());
                //new JavaRNG()
                
                return u;
            case "Poisson":
                u = new PoissonDistribution(mean);
                u.setGenerator(new Random());
                return u;
            case "Uniform":
                u = new UniformDistribution((int)mean, (int)variance);
                u.setGenerator(new Random());
                return u;
            case "Exponential":
                u = new ExponentialDistribution(mean);
                u.setGenerator(new Random());
                return u;
            case "Gaussian":
                u = new GaussianDistribution(mean, variance);
                u.setGenerator(new Random());
                return u;
        }
        return null;
    }

    public static int sizeOf(Serializable obj) {
        try {
            java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
            new java.io.ObjectOutputStream(baos).writeObject(obj);
            return baos.size();
        } catch (IOException ignore) {
            System.out.println("Error: " + ignore);
        }
        return 0;
    }

    /**
     * Returns a data set for a moving average on the data set passed in.
     *
     * @param xData an array of the x data.
     * @param yData an array of the y data.
     * @param period the number of data points to average
     *
     * @return A double[][] the length of the data set in the first dimension,
     * with two doubles for x and y in the second dimension
     */
    public static double[][] getMovingAverage(Number[] xData, Number[] yData,
            int period) {

        // check arguments...
        if (xData.length != yData.length) {
            throw new IllegalArgumentException("Array lengths must be equal.");
        }

        if (period > xData.length) {
            throw new IllegalArgumentException(
                    "Period can't be longer than dataset.");
        }

        double[][] result = new double[xData.length - period][2];
        for (int i = 0; i < result.length; i++) {
            result[i][0] = xData[i + period].doubleValue();
            // holds the moving average sum
            double sum = 0.0;
            for (int j = 0; j < period; j++) {
                sum += yData[i + j].doubleValue();
            }
            sum = sum / period;
            result[i][1] = sum;
        }
        return result;

    }

    public static Object deepCopy(Object oldObj) throws Exception {
        ObjectOutputStream oos = null;
        ObjectInputStream ois = null;
        try {
            ByteArrayOutputStream bos
                    = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(bos);
            oos.writeObject(oldObj);
            oos.flush();
            ByteArrayInputStream bin
                    = new ByteArrayInputStream(bos.toByteArray());
            ois = new ObjectInputStream(bin);
            return ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Exception in ObjectCloner = " + e);
            throw (e);
        } finally {
            oos.close();
            ois.close();
        }
    }

    public static int stringMessageSize(String message) {
        int charSize = 16;
        return message.length() * charSize;
    }
}
