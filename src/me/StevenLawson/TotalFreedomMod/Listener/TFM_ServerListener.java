/*    */ package me.StevenLawson.TotalFreedomMod.Listener;
/*    */ 
/*    */ import java.net.InetAddress;
/*    */ import java.util.Collection;
/*    */ import me.StevenLawson.TotalFreedomMod.Config.TFM_ConfigEntry;
/*    */ import me.StevenLawson.TotalFreedomMod.TFM_BanManager;
/*    */ import me.StevenLawson.TotalFreedomMod.TFM_ServerInterface;
/*    */ import me.StevenLawson.TotalFreedomMod.TFM_Util;
/*    */ import org.bukkit.Bukkit;
/*    */ import org.bukkit.ChatColor;
/*    */ import org.bukkit.event.EventHandler;
/*    */ import org.bukkit.event.EventPriority;
/*    */ import org.bukkit.event.Listener;
/*    */ import org.bukkit.event.server.ServerListPingEvent;
/*    */ 
/*    */ public class TFM_ServerListener
/*    */   implements Listener
/*    */ {
/*    */   @EventHandler(priority=EventPriority.HIGHEST)
/*    */   public void onServerPing(ServerListPingEvent event)
/*    */   {
/* 19 */     String ip = event.getAddress().getHostAddress();
/* 21 */     if (TFM_BanManager.isIpBanned(ip))
/*    */     {
/* 23 */       event.setMotd(ChatColor.RED + "You are banned.");
/* 24 */       return;
/*    */     }
/* 27 */     if (TFM_ConfigEntry.ADMIN_ONLY_MODE.getBoolean().booleanValue())
/*    */     {
/* 29 */       event.setMotd(ChatColor.RED + "Server is closed.");
/* 30 */       return;
/*    */     }
/* 33 */     if (Bukkit.hasWhitelist())
/*    */     {
/* 35 */       event.setMotd(ChatColor.RED + "Whitelist enabled.");
/* 36 */       return;
/*    */     }
/* 39 */     if (Bukkit.getOnlinePlayers().size() >= Bukkit.getMaxPlayers())
/*    */     {
/* 41 */       event.setMotd(ChatColor.RED + "Server is full.");
/* 42 */       return;
/*    */     }
/* 45 */     if (!TFM_ConfigEntry.SERVER_COLORFUL_MOTD.getBoolean().booleanValue())
/*    */     {
/* 47 */       event.setMotd(TFM_Util.colorize(TFM_ConfigEntry.SERVER_MOTD.getString().replace("%mcversion%", TFM_ServerInterface.getVersion())));
/*    */       
/* 49 */       return;
/*    */     }
/* 53 */     StringBuilder motd = new StringBuilder();
/* 55 */     for (String word : TFM_ConfigEntry.SERVER_MOTD.getString().replace("%mcversion%", TFM_ServerInterface.getVersion()).split(" ")) {
/* 57 */       motd.append(TFM_Util.randomChatColor()).append(word).append(" ");
/*    */     }
/* 60 */     event.setMotd(TFM_Util.colorize(motd.toString()));
/*    */   }
/*    */ }


/* Location:              C:\Users\SilentBloodGaming\Desktop\MasterFreedomMod.jar!\me\StevenLawson\TotalFreedomMod\Listener\TFM_ServerListener.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */