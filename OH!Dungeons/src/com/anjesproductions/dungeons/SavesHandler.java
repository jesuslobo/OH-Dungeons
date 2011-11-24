package com.anjesproductions.dungeons;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.awt.image.PixelGrabber;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * Clase encargada de leer y guardar los ficheros de datos.<br>
 * <br>
 * Changelog:
 * <ul>
 * <li>14/11/2011</li>
 * <ul>
 * <li>Clase con funcionalidad inicial finalizada.</li>
 * <li>Constructores sin y con parametros(MyMap,int,int).</li>
 * <li>Salvado y cargado de ficheros.</li>
 * <li>Setters para los datos (obsoletos) y Getters para mapa y datos.</li>
 * </ul>
 * </ul>
 * 
 * @author Jesus
 * 
 */
public class SavesHandler {

	// Constantes para conocer el modo de funcionamiento
	public final static int LOADMAP = 0;
	public final static int LOADGAME = 1;
	// Constantes para acceder al campo de settings deseado
	public final static int MONEY = 0;
	public final static int MANA = 1;
	// Constantes que indican bit "activado" (blanco) o "desactivado (negro)
	private final static int BIT1 = 0xffffffff;
	private final static int BIT0 = 0xff000000;
	// Constantes con los directorios de las imagenes de datos
	private final static String MAPSFOLDER = "maps\\";
	private final static String SAVESFOLDER = "saves\\";
	// Propiedades de la clase, almacenan el mapa y la configuraci�n leida
	private int[] map;
	private int[] settings;

	/**
	 * Constructor de la clase, inicializa las variables a valores nulos.
	 */
	public SavesHandler() {
		map = null;
		settings = null;
	}

	/**
	 * Constructor de la clase, inicializa las variables a los valores
	 * recibidos.
	 */
	public SavesHandler(MyMap m, int mon, int man) {
		map = m.toArray();
		setSettings(mon, man);
	}

	/**
	 * Metodo publico que guarda la imagen de la partida actual con el nombre
	 * indicado por el parametro filename.
	 * 
	 * @param filename
	 *            - Nombre del fichero a guardar, sin extension.
	 */
	public void saveFile(String filename) {
		if (map == null) {
			return;
		}
		
		BufferedImage image = new BufferedImage(MyMap.WIDTH, MyMap.HEIGHT,
				BufferedImage.TYPE_INT_ARGB);
		int[] t = new int[settings.length + map.length];

		for (int i = 0; i < settings.length; i++) {
			t[i] = settings[i];
		}
		for (int i = 0; i < map.length; i++) {
			t[i + settings.length] = map[i];
		}

		image.setRGB(0, 0, MyMap.WIDTH, MyMap.HEIGHT, t, 0, MyMap.WIDTH);

		File imageFile = new File(SAVESFOLDER + filename + ".png");
		try {
			ImageIO.write(image, "png", imageFile);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Metodo que carga el fichero indicado por <i><b>filename</b></i> seg�n el
	 * modo de apertura indicado por <i><b>mode</b></i>.
	 * 
	 * @param filename
	 *            - Nombre del fichero a cargar, sin extension.
	 * @param mode
	 *            - Modo de apertura, mapa (<i><b>LOADMAP</b></i>) o partida
	 *            guardada (<i><b>LOADGAME</b></i>).
	 */
	public void loadFile(int mode, String filename) {

		String folder = null;
		switch (mode) {
		case LOADMAP:
			folder = MAPSFOLDER;
			break;
		case LOADGAME:
			folder = SAVESFOLDER;
			break;
		}

		BufferedImage img = null;
		try {
			img = ImageIO.read(new File(folder + filename + ".png"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		map = maploader(img, 0, 0, MyMap.WIDTH, MyMap.HEIGHT);
		settings = maploader(img, 0, 0, MyMap.WIDTH, 2);

		// Descomentar en caso necesario.
		//
		// switch (folder) {
		// case MAPSFOLDER:
		// map = maploader(img, 0, 0, MyMap.WIDTH, MyMap.HEIGHT);
		// settings = maploader(img, 0, 0, MyMap.WIDTH, 2);
		// break;
		// case SAVESFOLDER:
		// map = maploader(img, 0, 2, MyMap.WIDTH, MyMap.HEIGHT - 2);
		// settings = maploader(img, 0, 0, MyMap.WIDTH, 2);
		// break;
		// }

	}

	/**
	 * Metodo publico que devuelve el mapa cargado por <i><b>loadFile(int,
	 * String)</b></i> o con el constructor <i><b>SavesHandler(MyMap, int,
	 * int)</b></i>.
	 * 
	 * @return - MyMapa almacenado.
	 */
	public MyMap getMyMap() {
		if (map != null)
			return new MyMap(map);
		return null;
	}

	/**
	 * Metodo publico que devuelve el valor numerico del campo
	 * <i><b>settings[MONEY]</b></i>. <br>
	 * <br>
	 * Para ello utiliza el metodo privado <i><b>getSettings()</b></i>.
	 * 
	 * @return - Entero con el valor numerico de <i><b>settings[MONEY]</b></i>.
	 */
	public int getMoney() {
		if (settings != null)
			return getSettings()[MONEY];
		return (Integer) null;
	}

	/**
	 * Metodo publico que devuelve el valor numerico del campo
	 * <i><b>settings[MANA]</b></i>. <br>
	 * <br>
	 * Para ello utiliza el metodo privado <i><b>getSettings()</b></i>.
	 * 
	 * @return - Entero con el valor numerico de <i><b>settings[MANA]</b></i>.
	 */
	public int getMana() {
		if (settings != null)
			return getSettings()[MANA];
		return (Integer) null;
	}

	/**
	 * Metodo privado que devuelve un array con los valores numericos de
	 * <i><b>settings</b></i>.
	 * 
	 * @return - Array con los valores numericos de <b>settings</b>.
	 */
	private int[] getSettings() {
		int[] sets = { 0, 0 };

		for (int i = 0; i < MyMap.WIDTH; i++) {
			if (settings[i] == BIT1) {
				sets[0] += Math.pow(2, MyMap.WIDTH - 1 - i);
			}
			if (settings[i + (MyMap.WIDTH - 1) + 1] == BIT1) {
				sets[1] += Math.pow(2, MyMap.WIDTH - 1 - i);
			}
		}

		return sets;
	}

	/**
	 * Metodo publico y <u>obsoleto</u> que configura los valores binarios de
	 * <i><b>settings[MONEY]</b></i> y <i><b>settings[MANA]</b></i>. <br>
	 * <br>
	 * Actualmente se cargan los valores mediante
	 * <i><b>loadFile(int,String)</b></i> o el constructor
	 * <i><b>SavesHandler(MyMap,int,int)</b></i>.
	 * 
	 * @param money
	 *            - Entero con el valor a almacenar en
	 *            <i><b>settings[MONEY]</b></i>.
	 * @param mana
	 *            - Entero con el valor a almacenar en
	 *            <i><b>settings[MANA]</b</i>.
	 */
	public void setSettings(int money, int mana) {
		int[] sets = new int[MyMap.WIDTH * 2];

		for (int i = 0; i < MyMap.WIDTH; i++) {
			if (Math.pow(2, MyMap.WIDTH - 1 - i) <= money) {
				sets[i] = BIT1;
				money = (int) (money - Math.pow(2, MyMap.WIDTH - 1 - i));
			} else
				sets[i] = BIT0;

			if (Math.pow(2, MyMap.WIDTH - 1 - i) <= mana) {
				sets[MyMap.WIDTH + i] = BIT1;
				mana = (int) (mana - Math.pow(2, MyMap.WIDTH - 1 - i));
			} else
				sets[MyMap.WIDTH + i] = BIT0;
		}

		settings = sets;

	}

	/**
	 * Metodo privado que crea el array con la informacion del fichero leido por
	 * <i><b>loadFile(int,String)</b></i>.
	 * 
	 * @param img
	 *            - Imagen leida en <i><b>loadFile(int,String)</b></i>.
	 * @param x
	 *            - Origen x de los datos a leer de <i><b>img</b></i>.
	 * @param y
	 *            - Origen y de los datos a leer de <i><b>img</b></i>.
	 * @param w
	 *            - Ancho de <i><b>img</b></i>.
	 * @param h
	 *            - Alto de <i><b>img</b></i>.
	 * @return - Array unidimensional con los datos binarios y/o hexadecimales
	 *         contenidos en la imagen y en las cordenadas y tama�os indicados
	 *         por los parametros.
	 */
	private static int[] maploader(Image img, int x, int y, int w, int h) {
		int[] pixels = new int[w * h];
		PixelGrabber pg = new PixelGrabber(img, x, y, w, h, pixels, 0, w);

		try {
			pg.grabPixels();
		} catch (InterruptedException e) {
			System.err.println("interrupted waiting for pixels!");
			return null;
		}
		if ((pg.getStatus() & ImageObserver.ABORT) != 0) {
			System.err.println("image fetch aborted or errored");
			return null;
		}

		return pixels;
	}
        
        public String mapToString(){
            String s = "<html>";
            for(int i = 0; i < MyMap.HEIGHT; i++){
                for(int j = 0; j < MyMap.WIDTH;j++){
                	int x = 0;
                	if (map[(i*MyMap.HEIGHT)+j] == BIT1) x = 1; 
                    s = s.concat(" " + Integer.toString(x));
                }
                s = s.concat("<br>");
            }
            return s.concat("</html>");
        }

	/**
	 * Destructor de la clase.
	 */
	public void clear() {
		try {
			map = null;
			settings = null;
			this.finalize();
		} catch (Throwable ex) {
			ex.printStackTrace();
		}
	}
}
