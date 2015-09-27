/*     */ package me.StevenLawson.TotalFreedomMod.Commands;
/*     */ 
/*     */ import java.net.HttpURLConnection;
/*     */ import java.net.InetAddress;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import me.StevenLawson.TotalFreedomMod.Config.TFM_ConfigEntry;
/*     */ import me.StevenLawson.TotalFreedomMod.TFM_Admin;
/*     */ import me.StevenLawson.TotalFreedomMod.TFM_Log;
/*     */ import me.StevenLawson.TotalFreedomMod.TotalFreedomMod;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ import org.bukkit.ChatColor;
/*     */ import org.bukkit.command.Command;
/*     */ import org.bukkit.command.CommandSender;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.scheduler.BukkitRunnable;
/*     */ 
/*     */ @CommandPermissions(level=AdminLevel.SUPER, source=SourceType.ONLY_IN_GAME)
/*     */ @CommandParameters(description="Register your connection with the TFM logviewer.", usage="/<command> [off]")
/*     */ public class Command_logs
/*     */   extends TFM_Command
/*     */ {
/*     */   public boolean run(CommandSender sender, Player sender_p, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
/*     */   {
/*  30 */     LogsRegistrationMode mode = LogsRegistrationMode.UPDATE;
/*  32 */     if (args.length == 1) {
/*  34 */       mode = "off".equals(args[0]) ? LogsRegistrationMode.DELETE : LogsRegistrationMode.UPDATE;
/*     */     }
/*  37 */     updateLogsRegistration(sender, sender_p, mode);
/*     */     
/*  39 */     return true;
/*     */   }
/*     */   
/*     */   public static void updateLogsRegistration(CommandSender sender, Player target, LogsRegistrationMode mode)
/*     */   {
/*  44 */     updateLogsRegistration(sender, target.getName(), target.getAddress().getAddress().getHostAddress().trim(), mode);
/*     */   }
/*     */   
/*     */   public static void updateLogsRegistration(CommandSender sender, final String targetName, final String targetIP, final LogsRegistrationMode mode)
/*     */   {
/*  49 */     final String logsRegisterURL = TFM_ConfigEntry.LOGS_URL.getString();
/*  50 */     final String logsRegisterPassword = TFM_ConfigEntry.LOGS_SECRET.getString();
/*  52 */     if ((logsRegisterURL == null) || (logsRegisterPassword == null) || (logsRegisterURL.isEmpty()) || (logsRegisterPassword.isEmpty())) {
/*  54 */       return;
/*     */     }
/*  57 */     new BukkitRunnable()
/*     */     {
/*     */       public void run()
/*     */       {
/*     */         try
/*     */         {
/*  64 */           if (val$sender != null) {
/*  66 */             val$sender.sendMessage(ChatColor.YELLOW + "Connecting...");
/*     */           }
/*  69 */           URL url = new Command_logs.URLBuilder(logsRegisterURL).addQueryParameter("mode", mode.toString()).addQueryParameter("password", logsRegisterPassword).addQueryParameter("name", targetName).addQueryParameter("ip", targetIP).getURL();
/*     */           
/*  76 */           HttpURLConnection connection = (HttpURLConnection)url.openConnection();
/*  77 */           connection.setConnectTimeout(5000);
/*  78 */           connection.setReadTimeout(5000);
/*  79 */           connection.setUseCaches(false);
/*  80 */           connection.setRequestMethod("HEAD");
/*     */           
/*  82 */           final int responseCode = connection.getResponseCode();
/*  84 */           if (val$sender != null) {
/*  86 */             new BukkitRunnable()
/*     */             {
/*     */               public void run()
/*     */               {
/*  91 */                 if (responseCode == 200) {
/*  93 */                   val$sender.sendMessage(ChatColor.GREEN + "Registration " + val$mode.toString() + "d.");
/*     */                 } else {
/*  97 */                   val$sender.sendMessage(ChatColor.RED + "Error contacting logs registration server.");
/*     */                 }
/*     */               }
/*  97 */             }.runTask(TotalFreedomMod.plugin);
/*     */           }
/*     */         }
/*     */         catch (Exception ex)
/*     */         {
/* 105 */           TFM_Log.severe(ex);
/*     */         }
/*     */       }
/* 105 */     }.runTaskAsynchronously(TotalFreedomMod.plugin);
/*     */   }
/*     */   
/*     */   public static void deactivateSuperadmin(TFM_Admin superadmin)
/*     */   {
/* 113 */     for (String ip : superadmin.getIps()) {
/* 115 */       updateLogsRegistration(null, superadmin.getLastLoginName(), ip, LogsRegistrationMode.DELETE);
/*     */     }
/*     */   }
/*     */   
/*     */   public static enum LogsRegistrationMode
/*     */   {
/* 121 */     UPDATE("update"),  DELETE("delete");
/*     */     
/*     */     private final String mode;
/*     */     
/*     */     private LogsRegistrationMode(String mode)
/*     */     {
/* 126 */       this.mode = mode;
/*     */     }
/*     */     
/*     */     public String toString()
/*     */     {
/* 132 */       return mode;
/*     */     }
/*     */   }
/*     */   
/*     */   private static class URLBuilder
/*     */   {
/*     */     private final String requestPath;
/* 139 */     private final Map<String, String> queryStringMap = new HashMap();
/*     */     
/*     */     public URLBuilder(String requestPath)
/*     */     {
/* 143 */       this.requestPath = requestPath;
/*     */     }
/*     */     
/*     */     public URLBuilder addQueryParameter(String key, String value)
/*     */     {
/* 148 */       queryStringMap.put(key, value);
/* 149 */       return this;
/*     */     }
/*     */     
/*     */     public URL getURL()
/*     */       throws MalformedURLException
/*     */     {
/* 154 */       List<String> pairs = new ArrayList();
/* 155 */       Iterator<Map.Entry<String, String>> it = queryStringMap.entrySet().iterator();
/* 156 */       while (it.hasNext())
/*     */       {
/* 158 */         Map.Entry<String, String> pair = (Map.Entry)it.next();
/* 159 */         pairs.add((String)pair.getKey() + "=" + (String)pair.getValue());
/*     */       }
/* 162 */       return new URL(requestPath + "?" + StringUtils.join(pairs, "&"));
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\SilentBloodGaming\Desktop\MasterFreedomMod.jar!\me\StevenLawson\TotalFreedomMod\Commands\Command_logs.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */