package com.bgym.undauntednormandy.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.bgym.undauntednormandy.MainGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Undaunted:Normandy(Beta)";
		config.width = 1920;
		config.height = 1080;
		new LwjglApplication(new MainGame(), config);
	}
}
