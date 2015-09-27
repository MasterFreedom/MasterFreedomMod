/*     */ package me.StevenLawson.TotalFreedomMod;
/*     */ 
/*     */ import com.google.common.base.Function;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.util.Date;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Properties;
/*     */ import me.StevenLawson.TotalFreedomMod.Bridge.TFM_BukkitTelnetListener;
/*     */ import me.StevenLawson.TotalFreedomMod.Bridge.TFM_WorldEditListener;
/*     */ import me.StevenLawson.TotalFreedomMod.Commands.TFM_Command;
/*     */ import me.StevenLawson.TotalFreedomMod.Commands.TFM_CommandHandler;
/*     */ import me.StevenLawson.TotalFreedomMod.Commands.TFM_CommandLoader;
/*     */ import me.StevenLawson.TotalFreedomMod.Config.TFM_ConfigEntry;
/*     */ import me.StevenLawson.TotalFreedomMod.HTTPD.TFM_HTTPD_Manager;
/*     */ import me.StevenLawson.TotalFreedomMod.Listener.TFM_BlockListener;
/*     */ import me.StevenLawson.TotalFreedomMod.Listener.TFM_EntityListener;
/*     */ import me.StevenLawson.TotalFreedomMod.Listener.TFM_PlayerListener;
/*     */ import me.StevenLawson.TotalFreedomMod.Listener.TFM_ServerListener;
/*     */ import me.StevenLawson.TotalFreedomMod.Listener.TFM_WeatherListener;
/*     */ import me.StevenLawson.TotalFreedomMod.World.TFM_AdminWorld;
/*     */ import me.StevenLawson.TotalFreedomMod.World.TFM_Flatlands;
/*     */ import org.bukkit.Server;
/*     */ import org.bukkit.World;
/*     */ import org.bukkit.command.Command;
/*     */ import org.bukkit.command.CommandSender;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.plugin.PluginDescriptionFile;
/*     */ import org.bukkit.plugin.PluginManager;
/*     */ import org.bukkit.plugin.ServicePriority;
/*     */ import org.bukkit.plugin.ServicesManager;
/*     */ import org.bukkit.plugin.java.JavaPlugin;
/*     */ import org.bukkit.scheduler.BukkitRunnable;
/*     */ import org.bukkit.scheduler.BukkitScheduler;
/*     */ import org.mcstats.Metrics;
/*     */ 
/*     */ public class TotalFreedomMod
/*     */   extends JavaPlugin
/*     */ {
/*     */   public static final long HEARTBEAT_RATE = 5L;
/*     */   public static final long SERVICE_CHECKER_RATE = 120L;
/*     */   public static final int MAX_USERNAME_LENGTH = 20;
/*     */   public static final String CONFIG_FILENAME = "config.yml";
/*     */   public static final String SUPERADMIN_FILENAME = "superadmin.yml";
/*     */   public static final String PERMBAN_FILENAME = "permban.yml";
/*     */   public static final String UUID_FILENAME = "uuids.db";
/*     */   public static final String PROTECTED_AREA_FILENAME = "protectedareas.dat";
/*     */   public static final String SAVED_FLAGS_FILENAME = "savedflags.dat";
/*     */   @Deprecated
/*  49 */   public static final String YOU_ARE_NOT_OP = TFM_Command.YOU_ARE_NOT_OP;
/*  51 */   public static String buildNumber = "1";
/*  52 */   public static String buildDate = buildDate = TFM_Util.dateToString(new Date());
/*  53 */   public static String buildCreator = "Unknown";
/*     */   public static Server server;
/*     */   public static TotalFreedomMod plugin;
/*     */   public static String pluginName;
/*     */   public static String pluginVersion;
/*  60 */   public static boolean lockdownEnabled = false;
/*  61 */   public static Map<Player, Double> fuckoffEnabledFor = new HashMap();
/*     */   
/*     */   public void onLoad()
/*     */   {
/*  66 */     plugin = this;
/*  67 */     server = plugin.getServer();
/*  68 */     pluginName = plugin.getDescription().getName();
/*  69 */     pluginVersion = plugin.getDescription().getVersion();
/*     */     
/*  71 */     TFM_Log.setPluginLogger(plugin.getLogger());
/*  72 */     TFM_Log.setServerLogger(server.getLogger());
/*     */     
/*  74 */     setAppProperties();
/*     */   }
/*     */   
/*     */   public void onEnable()
/*     */   {
/*  80 */     TFM_Log.info("Made by Madgeek1450 and Prozza");
/*  81 */     TFM_Log.info("Compiled " + buildDate + " by " + buildCreator);
/*     */     
/*  83 */     TFM_Util.MethodTimer timer = new TFM_Util.MethodTimer();
/*  84 */     timer.start();
/*  86 */     if (!"v1_8_R2".equals(TFM_Util.getNmsVersion()))
/*     */     {
/*  88 */       TFM_Log.warning(pluginName + " is compiled for " + "v1_8_R2" + " but the server is running " + "version " + TFM_Util.getNmsVersion() + "!");
/*     */       
/*  90 */       TFM_Log.warning("This might result in unexpected behaviour!");
/*     */     }
/*  93 */     TFM_Util.deleteCoreDumps();
/*  94 */     TFM_Util.deleteFolder(new File("./_deleteme"));
/*     */     
/*  97 */     TFM_Util.createBackups("config.yml", true);
/*  98 */     TFM_Util.createBackups("superadmin.yml");
/*  99 */     TFM_Util.createBackups("permban.yml");
/*     */     
/* 102 */     TFM_UuidManager.load();
/* 103 */     TFM_AdminList.load();
/* 104 */     TFM_PermbanList.load();
/* 105 */     TFM_PlayerList.load();
/* 106 */     TFM_BanManager.load();
/* 107 */     TFM_Announcer.load();
/* 108 */     TFM_ProtectedArea.load();
/*     */     
/* 111 */     server.getServicesManager().register(Function.class, TFM_AdminList.SUPERADMIN_SERVICE, plugin, ServicePriority.Normal);
/*     */     
/* 113 */     PluginManager pm = server.getPluginManager();
/* 114 */     pm.registerEvents(new TFM_EntityListener(), plugin);
/* 115 */     pm.registerEvents(new TFM_BlockListener(), plugin);
/* 116 */     pm.registerEvents(new TFM_PlayerListener(), plugin);
/* 117 */     pm.registerEvents(new TFM_WeatherListener(), plugin);
/* 118 */     pm.registerEvents(new TFM_ServerListener(), plugin);
/*     */     
/* 121 */     pm.registerEvents(new TFM_BukkitTelnetListener(), plugin);
/* 122 */     pm.registerEvents(new TFM_WorldEditListener(), plugin);
/*     */     try
/*     */     {
/* 126 */       TFM_Flatlands.getInstance().getWorld();
/*     */     }
/*     */     catch (Exception ex)
/*     */     {
/* 130 */       TFM_Log.warning("Could not load world: Flatlands");
/*     */     }
/*     */     try
/*     */     {
/* 135 */       TFM_AdminWorld.getInstance().getWorld();
/*     */     }
/*     */     catch (Exception ex)
/*     */     {
/* 139 */       TFM_Log.warning("Could not load world: AdminWorld");
/*     */     }
/* 143 */     TFM_GameRuleHandler.setGameRule(TFM_GameRuleHandler.TFM_GameRule.DO_DAYLIGHT_CYCLE, !TFM_ConfigEntry.DISABLE_NIGHT.getBoolean().booleanValue(), false);
/* 144 */     TFM_GameRuleHandler.setGameRule(TFM_GameRuleHandler.TFM_GameRule.DO_FIRE_TICK, TFM_ConfigEntry.ALLOW_FIRE_SPREAD.getBoolean().booleanValue(), false);
/* 145 */     TFM_GameRuleHandler.setGameRule(TFM_GameRuleHandler.TFM_GameRule.DO_MOB_LOOT, false, false);
/* 146 */     TFM_GameRuleHandler.setGameRule(TFM_GameRuleHandler.TFM_GameRule.DO_MOB_SPAWNING, !TFM_ConfigEntry.MOB_LIMITER_ENABLED.getBoolean().booleanValue(), false);
/* 147 */     TFM_GameRuleHandler.setGameRule(TFM_GameRuleHandler.TFM_GameRule.DO_TILE_DROPS, false, false);
/* 148 */     TFM_GameRuleHandler.setGameRule(TFM_GameRuleHandler.TFM_GameRule.MOB_GRIEFING, false, false);
/* 149 */     TFM_GameRuleHandler.setGameRule(TFM_GameRuleHandler.TFM_GameRule.NATURAL_REGENERATION, true, false);
/* 150 */     TFM_GameRuleHandler.commitGameRules();
/* 153 */     if (TFM_ConfigEntry.DISABLE_WEATHER.getBoolean().booleanValue()) {
/* 155 */       for (World world : server.getWorlds())
/*     */       {
/* 157 */         world.setThundering(false);
/* 158 */         world.setStorm(false);
/* 159 */         world.setThunderDuration(0);
/* 160 */         world.setWeatherDuration(0);
/*     */       }
/*     */     }
/* 165 */     new TFM_Heartbeat(plugin).runTaskTimer(plugin, 100L, 100L);
/*     */     
/* 168 */     TFM_ServiceChecker.start();
/* 169 */     TFM_HTTPD_Manager.start();
/* 170 */     TFM_FrontDoor.start();
/*     */     
/* 172 */     timer.update();
/*     */     
/* 174 */     TFM_Log.info("Version " + pluginVersion + " for " + "v1_8_R2" + " enabled in " + timer.getTotal() + "ms");
/*     */     try
/*     */     {
/* 179 */       Metrics metrics = new Metrics(plugin);
/* 180 */       metrics.start();
/*     */     }
/*     */     catch (IOException ex)
/*     */     {
/* 184 */       TFM_Log.warning("Failed to submit metrics data: " + ex.getMessage());
/*     */     }
/* 187 */     new BukkitRunnable()
/*     */     {
/*     */       public void run()
/*     */       {
/* 192 */         TFM_CommandLoader.scan();
/* 193 */         TFM_CommandBlocker.load();
/*     */         
/* 196 */         TFM_ProtectedArea.autoAddSpawnpoints();
/*     */       }
/* 196 */     }.runTaskLater(plugin, 20L);
/*     */   }
/*     */   
/*     */   public void onDisable()
/*     */   {
/* 204 */     TFM_HTTPD_Manager.stop();
/* 205 */     TFM_BanManager.save();
/* 206 */     TFM_UuidManager.close();
/* 207 */     TFM_FrontDoor.stop();
/*     */     
/* 209 */     server.getScheduler().cancelTasks(plugin);
/*     */     
/* 211 */     TFM_Log.info("Plugin disabled");
/*     */   }
/*     */   
/*     */   public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args)
/*     */   {
/* 217 */     return TFM_CommandHandler.handleCommand(sender, cmd, commandLabel, args);
/*     */   }
/*     */   
/*     */   private static void setAppProperties()
/*     */   {
/*     */     try
/*     */     {
/* 224 */       InputStream in = plugin.getResource("appinfo.properties");
/* 225 */       Properties props = new Properties();
/*     */       
/* 228 */       props.load(in);
/* 229 */       in.close();
/*     */       
/* 231 */       buildNumber = props.getProperty("program.buildnumber");
/* 232 */       buildDate = props.getProperty("program.builddate");
/* 233 */       buildCreator = props.getProperty("program.buildcreator");
/*     */     }
/*     */     catch (Exception ex)
/*     */     {
/* 237 */       TFM_Log.severe("Could not load App properties!");
/* 238 */       TFM_Log.severe(ex);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\SilentBloodGaming\Desktop\MasterFreedomMod.jar!\me\StevenLawson\TotalFreedomMod\TotalFreedomMod.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */