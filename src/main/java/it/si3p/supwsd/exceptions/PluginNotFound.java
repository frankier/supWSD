package it.si3p.supwsd.exceptions;

import java.lang.Exception;

public class PluginNotFound extends Exception {
	private String plugin;

	public PluginNotFound(String plugin) {
		super("Plugin not found: " + plugin);
	}
}
