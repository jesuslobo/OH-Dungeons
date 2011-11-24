package com.anjesproductions.dungeons;

import java.util.Random;

public class StartingPoint {

	public static void main(String[] args) {

		System.out.println(" Inicio de la ejecución\n"
				+ "========================");

		SavesHandler sh = new SavesHandler();
		sh.loadFile(SavesHandler.LOADMAP, "test");

		sh.setSettings(new Random().nextInt(5000), new Random().nextInt(5000));

		sh.saveFile("test1");

		Menu m = new Menu();

		m.jLabel1.setText(sh.mapToString());

		sh.clear();

		System.out.println("========================\n"
				+ "  Fin de la ejecución.");

	}
}
