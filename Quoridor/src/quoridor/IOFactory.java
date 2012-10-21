package quoridor;

import java.util.Dictionary;
import java.util.Hashtable;

public class IOFactory {
	
	static Dictionary d;
	
	/**
	 * Sets up the IO factory, declaring the hashtable and setting command line as default printer.
	 */
	private static void setUpIOFactory () {
		d = new Hashtable();
		d.put("default", new clIO());
	}
	
	/**
	 * @param name String name of an IO
	 * @return An implementation of the named IO
	 */
	public static IO getIO (String name) {
		if (d == null) {
			setUpIOFactory();
		}
		return (IO)d.get(name);
	}
	
	/**
	 * @return The default IO provider
	 */
	public static IO getIO () {
		if (d == null) {
			setUpIOFactory();
		}
		return (IO)d.get("default");
	}
	
    /**
     * Sets the default IO
     * @param name String name of an IO
     */
    public static void setIO (String name) {
		if (d == null) {
			setUpIOFactory();
		}
    	d.put("default", d.get(name));
    }
    
    /**
     * Adds an IO implementation to the available ones
     * @param name String name of an IO
     * @param i IO provider
     */
    public static void addIO (String name, IO i) {
		if (d == null) {
			setUpIOFactory();
		}
    	if (d.isEmpty()) {
    		d.put("default", i);
    	}
    	d.put(name, i);
    }

}
