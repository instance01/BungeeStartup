package com.comze_instancelabs.executeshbungee;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
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
		
		listProcesses();
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
		
		try{
			File f1 = new File("start.sh");
			f1.setExecutable(true);
			File f2 = new File("start2.sh");
			f2.setExecutable(true);
			File f3 = new File("start3.sh");
			f3.setExecutable(true);
		}catch(Exception e){
			ProxyServer.getInstance().getLogger().info("Failed to fix File permissions.");
		}
	}

	
	public void listProcesses(){
		Process p__ = null;
		try {
			p__ = Runtime.getRuntime().exec("ps");
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
