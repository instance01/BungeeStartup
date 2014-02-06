package com.comze_instancelabs.executeshbungee;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermission;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;

public class Main extends Plugin {
	@Override
    public void onEnable() {
		
		/*
		 * 
		 * This will start the first server after 5 seconds and proceed with each next after another second.
		 * 
		 * You don't need to think about starting all the other servers, just start your Bungee proxy.
		 * 
		 */
		
		fixPerms();
		
		ProxyServer.getInstance().getScheduler().schedule(this, new Runnable(){
			public void run(){
				startServer("start"); // this will execute start.sh
			}
		}, 5L, TimeUnit.SECONDS);
		
		ProxyServer.getInstance().getScheduler().schedule(this, new Runnable(){
			public void run(){
				startServer("start2");
			}
		}, 6L, TimeUnit.SECONDS);
		
		ProxyServer.getInstance().getScheduler().schedule(this, new Runnable(){
			public void run(){
				startServer("start3");
			}
		}, 7L, TimeUnit.SECONDS);
		
		listProcesses(false);
    }
	
	
	public void startServer(String sh_filename){
		Process p = null;
		
		File dir = new File(System.getProperty("user.dir"));
		String parentpath = dir.getParent();
		
		
		try {
			File folder = new File(".");
			ProxyServer.getInstance().getLogger().info(folder.getAbsolutePath());
			//ProxyServer.getInstance().getLogger().info(folder.getParentFile().getPath());
			p = Runtime.getRuntime().exec("./" + sh_filename + ".sh",null, new File(parentpath));
		} catch (IOException e) {
			ProxyServer.getInstance().getLogger().info("Failed Process " + sh_filename + "!");
			//e.printStackTrace();
		}
	}

	
	public void fixPerms(){
		File dir = new File(System.getProperty("user.dir"));
		String parentpath = dir.getParent();
		
		File dir_ = new File(".");
		File parent = dir_.getParentFile();
		
		Set<PosixFilePermission> perms = new HashSet<PosixFilePermission>();

        perms.add(PosixFilePermission.OWNER_READ);
        perms.add(PosixFilePermission.OWNER_WRITE);
        perms.add(PosixFilePermission.OWNER_EXECUTE);

        perms.add(PosixFilePermission.GROUP_READ);
        perms.add(PosixFilePermission.GROUP_WRITE);
        perms.add(PosixFilePermission.GROUP_EXECUTE);

        perms.add(PosixFilePermission.OTHERS_READ);
        perms.add(PosixFilePermission.OTHERS_WRITE);
        perms.add(PosixFilePermission.OTHERS_EXECUTE);
        
		try{
			File f1 = new File(dir.getParentFile(), "start.sh");
			f1.setExecutable(true);
			Files.setPosixFilePermissions(Paths.get(f1.getAbsolutePath()), perms);
			File f2 = new File(dir.getParentFile(), "start2.sh");
			f2.setExecutable(true);
			Files.setPosixFilePermissions(Paths.get(f2.getAbsolutePath()), perms);
			File f3 = new File(dir.getParentFile(), "start3.sh");
			f3.setExecutable(true);
			Files.setPosixFilePermissions(Paths.get(f3.getAbsolutePath()), perms);
		}catch(Exception e){
			ProxyServer.getInstance().getLogger().info("Failed to fix File permissions.");
			e.printStackTrace();
		}
	}

	
	public void listProcesses(boolean all){
		Process p__ = null;
		try {
			if(all){
				p__ = Runtime.getRuntime().exec("ps -A");
			}else{
				p__ = Runtime.getRuntime().exec("ps");
			}
		} catch (IOException e) {
			
		}
		
		if(p__ != null){
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(p__.getInputStream()));
			String line = null;
			try {
				while ((line = reader.readLine()) != null) {
					ProxyServer.getInstance().getLogger().info(line);
				}
			} catch (IOException e) {
				ProxyServer.getInstance().getLogger().info(e.getMessage());
			}	
		}
	}
}
