package Modelo;

import java.util.List;


public class ListaPersonaWrapper {
	private static List<Persona> personas;

	
	public static List<Persona> getPersonas() {
		return personas;
	}

	public void setPersonas(List<Persona> personas) {
		ListaPersonaWrapper.personas = personas;
	}
}
