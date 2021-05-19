package Util;

import java.io.File;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import Modelo.Persona;

public class BD {

	private static Connection conn = null;

	public BD(String uri, String usuario, String password) throws SQLException {

		PreparedStatement ps;

		String sql;

		conn = DriverManager.getConnection(uri, usuario, password);
		System.out.println("Base de datos conectada");
	}

	public void cerrar() throws SQLException {
		if (conn != null) {
			conn.close();
			System.out.println("Base de datos desconectada");
		}
	}

	public static List<Persona> getPersonas() throws SQLException {

		PreparedStatement ps = conn.prepareStatement("SELECT * FROM personas");
		ResultSet rs = ps.executeQuery();

		List<Persona> personas = new ArrayList<>();
		while (rs.next()) {
			String nombre = rs.getString("nombre");
			String apellidos = rs.getString("apellidos");
			String direccion = rs.getString("direccion");
			String ciudad = rs.getString("ciudad");
			int codigoPostal = rs.getInt("codigoPostal");
			Date fechaDeNacimiento = rs.getDate("fechaDeNacimiento");
			Persona persona = new Persona(nombre, apellidos, direccion, ciudad, codigoPostal,
					fechaDeNacimiento.toLocalDate());
			personas.add(persona);
		}
		rs.close();
		ps.close();
		return personas;

	}

	public static void putPersonas(List<Persona> personas) throws SQLException {

		// Borro todas
		PreparedStatement ps = null;
		String sql;
		sql = "DELETE FROM personas ";
		try {
			ps = conn.prepareStatement(sql);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

		// Preparo el statement
		String query = "INSERT INTO personas (nombre,apellidos,direccion,ciudad,codigoPostal,fechaDeNacimiento) VALUES (?,?,?,?,?,?)";
		ps = conn.prepareStatement(query);
		
		// Añado cada persona
		for (Persona persona : personas) {
			ps.setString(1, persona.getFirstName());
			ps.setString(2, persona.getLastName());
			ps.setString(3, persona.getStreet());
			ps.setString(4, persona.getCity());
			ps.setInt(5, persona.getPostalCode());
			ps.setDate(6, java.sql.Date.valueOf(persona.getBirthday()));
			ps.execute();
		}
		ps.close();
		System.out.println("Datos guardados en Base de datos");

	}

	public Connection getConnection() {

		return conn;
	}

}
