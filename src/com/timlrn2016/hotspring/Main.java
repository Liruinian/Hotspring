package com.timlrn2016.hotspring;

import java.io.File;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Main extends JavaPlugin implements Listener {
	@Override
	public void onEnable() {
		File file = new File(getDataFolder(), "config.yml");
		if (!getDataFolder().exists()) {
			getDataFolder().mkdir();
		}
		if (!file.exists()) {
			getServer().getConsoleSender().sendMessage("§6[温泉]未发现配置文件..正在新建一个配置文件");
			getServer().getConsoleSender().sendMessage("§6[HotSpring]No config Found! We'll create a new one for you!");
			this.saveDefaultConfig();
		}
		this.reloadConfig();
		
		if(!(getConfig().getBoolean("Enable"))) {
			getServer().getConsoleSender().sendMessage("§4[温泉] 您未启动本插件！！ 请将配置文件中的Enable: false 改为 true");
			getServer().getConsoleSender().sendMessage("§4[HotSpring] You haven't turn this plugin on! Please edit the config, and change Enable: false to true");
			getServer().getPluginManager().disablePlugin(this);
		}else{
			this.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
			public void run() {
				for (Player p : Bukkit.getOnlinePlayers()) {

					String Healing = getConfig().getString("Healing").replace("&", "§").replace("%player%",p.getName());
					String TooMuch = getConfig().getString("TooMuch").replace("&", "§").replace("%player%",p.getName());
					String Poison = getConfig().getString("Poison").replace("&", "§").replace("%player%", p.getName());
					String Explosion = getConfig().getString("Explosion").replace("&", "§").replace("%player%",p.getName());

					Location loc = p.getLocation();
					Block b = loc.getBlock();
					if ((b.getType() == Material.WATER) || (b.getType() == Material.STATIONARY_WATER)) {
						Block bb = b.getRelative(BlockFace.DOWN);
						Material m = bb.getType();
						if ((m == Material.GLASS) || (m == Material.STAINED_GLASS) || (m == Material.STAINED_GLASS_PANE) || (m == Material.THIN_GLASS)) {
							Block bbb = bb.getRelative(BlockFace.DOWN);
							Material mm = bbb.getType();
							if ((mm == Material.MAGMA)) {
								World w = p.getWorld();
								w.playEffect(loc, Effect.SMOKE, 20);
								w.spawnParticle(Particle.WATER_BUBBLE, loc, 500);
								if(getConfig().getBoolean("debug") == true) {
								getServer().getConsoleSender().sendMessage("§3==============================");
								getServer().getConsoleSender().sendMessage("§2HotSpring §5T§6e§8s§9t§aM§bo§cd§de");
								getServer().getConsoleSender().sendMessage("§4TestPlayer：" + p.getName());
								getServer().getConsoleSender().sendMessage("§4gethealth:"+String.valueOf(p.getHealth()));
								getServer().getConsoleSender().sendMessage("§4getmaxhealth:"+String.valueOf(p.getMaxHealth()));
								getServer().getConsoleSender().sendMessage("§4IsDead:"+p.isDead());
								getServer().getConsoleSender().sendMessage("§4IsPoisoned:"+p.hasPotionEffect(PotionEffectType.POISON));
								}
								if(p.getHealth() != 0D) {
								if (p.getHealth() < p.getMaxHealth() & p.isDead() == false) {
									p.setHealth(p.getHealth() + 1.0D);
									w.spawnParticle(Particle.VILLAGER_HAPPY, loc, 10);
									p.sendMessage(Healing);
								} else {
									p.sendMessage(TooMuch);
									Random r = new Random();
									int n = r.nextInt(100);
									if (n < 30) {
										Random r1 = new Random();
										int n1 = r1.nextInt(100);
										if (n1 < 20) {
											p.getWorld().createExplosion(loc, 10);
											Bukkit.broadcastMessage(Explosion);
										} else {
											p.getPlayer().sendMessage(Poison);
											p.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 500, 3));
											p.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 100, 3));
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}, 0L, 30L);
	}
		this.getServer().getConsoleSender().sendMessage("§2[温泉]成功加载温泉插件 Made By Tim_LRN2016");
	}

	public void onDisable() {
		getServer().getConsoleSender().sendMessage("§4[温泉]成功卸载温泉插件 Made By Tim_LRN2016");
	}

}
