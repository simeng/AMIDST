package amidst.version;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Stack;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;

import com.google.gson.JsonSyntaxException;

import MoF.FinderWindow;
import amidst.Amidst;
import amidst.Util;
import amidst.json.InstallInformation;
import amidst.json.LauncherProfile;
import amidst.logging.Log;
import amidst.minecraft.Minecraft;
import amidst.utilties.ProgressMeter;

public class VersionFactory {
	private MinecraftVersion[] localVersions;
	private MinecraftProfile[] profiles;
	public VersionFactory() {
		
	}
	
	public void scanForLocalVersions() {
		File versionPath = new File(Util.minecraftDirectory + "/versions");
		if (!versionPath.exists()) {
			Log.e("Cannot find version directory.");
			return;
		} else if (!versionPath.isDirectory()) {
			Log.e("Attempted to open version directory but found file.");
			return;
		}
		File[] versionDirectories = versionPath.listFiles();
		Stack<MinecraftVersion> versionStack = new Stack<MinecraftVersion>();
		for (int i = 0; i < versionDirectories.length; i++) {
			MinecraftVersion version = MinecraftVersion.fromVersionPath(versionDirectories[i]);
			if (version != null)
				versionStack.add(version);
		}
		if (versionStack.size() == 0)
			return;
		
		localVersions = new MinecraftVersion[versionStack.size()];
		versionStack.toArray(localVersions);
	}
	
	public void scanForProfiles() {
		Log.i("Scanning for profiles.");
		File profileJsonFile = new File(Util.minecraftDirectory + "/launcher_profiles.json");
		LauncherProfile profile = null;
		try {
			profile = Util.readObject(profileJsonFile, LauncherProfile.class);
		} catch (JsonSyntaxException e) {
			Log.crash(e, "Syntax exception thrown from launch_profiles.json");
			return;
		} catch (IOException e) {
			Log.crash(e, "Unable to open launch_profiles.json");
			return;
		}
		Log.i("Successfully loaded profile list.");
	
	}
	public MinecraftProfile[] getProfiles() {
		return profiles;
	}
	public MinecraftVersion[] getLocalVersions() {
		return localVersions;
	}
}
