package it.si3p.supwsd.inventory;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import it.si3p.supwsd.exceptions.PluginNotFound;

/**
 * @author papandrea
 *
 */
public class SenseInventoryFactory {

	private static SenseInventoryFactory instance;

	private SenseInventoryFactory() {

	}

	public static SenseInventoryFactory getInstance() {

		if (instance == null)
			instance = new SenseInventoryFactory();

		return instance;
	}

	public SenseInventory getSenseInventory(SenseInventoryType senseInventoryType,String dict,String ISO) throws IOException, PluginNotFound {

		SenseInventory senseInventory = null;

		if (senseInventoryType != null) {
			
			switch (senseInventoryType) {

			case BABELNET:
				Class<?> babelNetInventoryCls;
				Constructor babelNetInventoryConstr;
				try {
					babelNetInventoryCls = Class.forName("it.si3p.supwsd.inventory.BabelNetInventory");
				} catch (ClassNotFoundException e) {
					throw new PluginNotFound("BabelNet");
				}
				Class[] argTypes = new Class[1];
				argTypes[0] = String.class;
				try {
					babelNetInventoryConstr = babelNetInventoryCls.getDeclaredConstructor(argTypes);
				} catch (NoSuchMethodException e) {
					throw new PluginNotFound("BabelNet");
				}
				try {
					senseInventory = (SenseInventory)babelNetInventoryConstr.newInstance(ISO);
				} catch (InstantiationException e) {
					throw new PluginNotFound("BabelNet");
				} catch (IllegalAccessException e) {
					throw new PluginNotFound("BabelNet");
				} catch (InvocationTargetException e) {
					throw new PluginNotFound("BabelNet");
				}
				
				break;

			default:
				
				if(dict==null || dict.isEmpty())
					throw new IllegalArgumentException("Wordnet dict cannot be null or empty");
				
				senseInventory = new WordNetInventory(dict);
				break;
			}
		}
		
		return senseInventory;
	}
}
