package com.leclercb.taskunifier.gui.main.main;

import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;

import com.leclercb.taskunifier.gui.resources.Resources;
import java.util.Properties;

public class DBUtils {

	public static Connection getConnection() {
		Connection con = null;
		Properties prop = new Properties();
		System.out.println("Connection info...........");
		try {
			prop.load(Resources.class.getResourceAsStream("madrone.properties"));
			System.out.println(prop.getProperty("DB_CONNECTION"));
			System.out.println(prop.getProperty("DB_USER"));
			System.out.println(prop.getProperty("DB_PASSWORD"));

			con = DriverManager.getConnection(
					prop.getProperty(DBConstants.DB_CONNECTION),
					prop.getProperty(DBConstants.DB_USER),
					prop.getProperty(DBConstants.DB_PASSWORD));

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return con;
		
	}
	
	
	public static void main(String agrs[]) throws UnknownHostException {
		
		String username = System.getProperty("user.name");
		
		System.out.println("usrname===="  + username.toUpperCase());

		java.net.InetAddress localMachine = java.net.InetAddress.getLocalHost();
		System.out.println("Hostname of local machine: " + localMachine.getHostName());
	}

}
