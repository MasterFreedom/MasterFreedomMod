/*    */ package me.StevenLawson.TotalFreedomMod;
/*    */ 
/*    */ import java.util.Collection;
/*    */ import me.StevenLawson.TotalFreedomMod.Bridge.TFM_EssentialsBridge;
/*    */ import me.StevenLawson.TotalFreedomMod.Config.TFM_ConfigEntry;
/*    */ import me.StevenLawson.TotalFreedomMod.World.TFM_AdminWorld;
/*    */ import me.StevenLawson.TotalFreedomMod.World.TFM_AdminWorld.WeatherMode;
/*    */ import org.bukkit.Server;
/*    */ import org.bukkit.World;
/*    */ import org.bukkit.entity.Player;
/*    */ import org.bukkit.scheduler.BukkitRunnable;
/*    */ 
/*    */ public class TFM_Heartbeat
/*    */   extends BukkitRunnable
/*    */ {
/* 13 */   private static final long AUTO_KICK_TIME = TFM_ConfigEntry.AUTOKICK_TIME.getInteger().intValue() * 1000L;
/*    */   private final TotalFreedomMod plugin;
/*    */   private final Server server;
/* 16 */   private static Long lastRan = null;
/*    */   
/*    */   public TFM_Heartbeat(TotalFreedomMod instance)
/*    */   {
/* 20 */     plugin = instance;
/* 21 */     server = plugin.getServer();
/*    */   }
/*    */   
/*    */   public static Long getLastRan()
/*    */   {
/* 26 */     return lastRan;
/*    */   }
/*    */   
/*    */   public void run()
/*    */   {
/* 32 */     lastRan = Long.valueOf(System.currentTimeMillis());
/*    */     
/* 34 */     boolean doAwayKickCheck = (TFM_ConfigEntry.AUTOKICK_ENABLED.getBoolean().booleanValue()) && (TFM_EssentialsBridge.isEssentialsEnabled()) && (server.getOnlinePlayers().size() / server.getMaxPlayers() > TFM_ConfigEntry.AUTOKICK_THRESHOLD.getDouble().doubleValue());
/* 38 */     for (Player player : server.getOnlinePlayers())
/*    */     {
/* 40 */       TFM_PlayerData playerdata = TFM_PlayerData.getPlayerData(player);
/* 41 */       playerdata.resetMsgCount();
/* 42 */       playerdata.resetBlockDestroyCount();
/* 43 */       playerdata.resetBlockPlaceCount();
/* 45 */       if (doAwayKickCheck)
/*    */       {
/* 47 */         long lastActivity = TFM_EssentialsBridge.getLastActivity(player.getName());
/* 48 */         if ((lastActivity > 0L) && (lastActivity + AUTO_KICK_TIME < System.currentTimeMillis())) {
/* 50 */           player.kickPlayer("Automatically kicked by server for inactivity.");
/*    */         }
/*    */       }
/*    */     }
/* 55 */     if (TFM_ConfigEntry.AUTO_ENTITY_WIPE.getBoolean().booleanValue()) {
/* 57 */       TFM_Util.TFM_EntityWiper.wipeEntities(!TFM_ConfigEntry.ALLOW_EXPLOSIONS.getBoolean().booleanValue(), false);
/*    */     }
/* 60 */     if (TFM_ConfigEntry.DISABLE_WEATHER.getBoolean().booleanValue()) {
/* 62 */       for (World world : server.getWorlds())
/*    */       {
/*    */         try
/*    */         {
/* 66 */           if ((world == TFM_AdminWorld.getInstance().getWorld()) && (TFM_AdminWorld.getInstance().getWeatherMode() != TFM_AdminWorld.WeatherMode.OFF)) {
/*    */             continue;
/*    */           }
/*    */         }
/*    */         catch (Exception ex) {}
/* 75 */         if (world.getWeatherDuration() > 0)
/*    */         {
/* 77 */           world.setThundering(false);
/* 78 */           world.setWeatherDuration(0);
/*    */         }
/* 80 */         else if (world.getThunderDuration() > 0)
/*    */         {
/* 82 */           world.setStorm(false);
/* 83 */           world.setThunderDuration(0);
/*    */         }
/*    */       }
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\SilentBloodGaming\Desktop\MasterFreedomMod.jar!\me\StevenLawson\TotalFreedomMod\TFM_Heartbeat.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */