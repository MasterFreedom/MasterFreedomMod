/*     */ package me.StevenLawson.TotalFreedomMod;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.InputStreamReader;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Date;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import me.StevenLawson.TotalFreedomMod.Config.TFM_ConfigEntry;
/*     */ import org.bukkit.ChatColor;
/*     */ import org.bukkit.scheduler.BukkitRunnable;
/*     */ import org.json.simple.JSONArray;
/*     */ import org.json.simple.JSONObject;
/*     */ import org.json.simple.JSONValue;
/*     */ 
/*     */ public class TFM_ServiceChecker
/*     */ {
/*     */   public static final Map<String, ServiceStatus> services;
/*     */   private static URL url;
/*     */   
/*     */   private TFM_ServiceChecker()
/*     */   {
/*  30 */     throw new AssertionError();
/*     */   }
/*     */   
/*  35 */   private static String lastCheck = "Unknown";
/*  36 */   private static String version = "1.0-Mojang";
/*     */   
/*     */   static
/*     */   {
/*  37 */     services = new HashMap();
/*  38 */     services.put("minecraft.net", new ServiceStatus("Minecraft.net"));
/*  39 */     services.put("account.mojang.com", new ServiceStatus("Mojang Account Website"));
/*  40 */     services.put("authserver.mojang.com", new ServiceStatus("Mojang Authentication"));
/*  41 */     services.put("sessionserver.mojang.com", new ServiceStatus("Mojang Multiplayer sessions"));
/*  42 */     services.put("skins.minecraft.net", new ServiceStatus("Minecraft Skins"));
/*  43 */     services.put("auth.mojang.com", new ServiceStatus("Mojang Authentiation (Legacy)"));
/*  44 */     services.put("session.minecraft.net", new ServiceStatus("Minecraft Sessions (Legacy)"));
/*     */   }
/*     */   
/*     */   public static void start()
/*     */   {
/*  49 */     String serviceCheckerURL = TFM_ConfigEntry.SERVICE_CHECKER_URL.getString();
/*  51 */     if ((serviceCheckerURL == null) || (serviceCheckerURL.isEmpty())) {
/*  53 */       return;
/*     */     }
/*     */     try
/*     */     {
/*  58 */       url = new URL(serviceCheckerURL);
/*     */     }
/*     */     catch (MalformedURLException ex)
/*     */     {
/*  62 */       TFM_Log.severe("Invalid ServiceChecker URL, disabling service checker");
/*  63 */       return;
/*     */     }
/*  66 */     getUpdateRunnable().runTaskTimerAsynchronously(TotalFreedomMod.plugin, 40L, 2400L);
/*     */   }
/*     */   
/*     */   public static BukkitRunnable getUpdateRunnable()
/*     */   {
/*  71 */     new BukkitRunnable()
/*     */     {
/*     */       public void run()
/*     */       {
/*  76 */         if (TFM_ServiceChecker.url == null) {
/*     */           return;
/*     */         }
/*     */         JSONArray statusJson;
/*     */         try
/*     */         {
/*  84 */           BufferedReader in = new BufferedReader(new InputStreamReader(TFM_ServiceChecker.url.openStream()));
/*  85 */           statusJson = (JSONArray)JSONValue.parse(in.readLine());
/*  86 */           in.close();
/*     */         }
/*     */         catch (Exception ex)
/*     */         {
/*  90 */           TFM_Log.severe("Error updating mojang services from " + TFM_ServiceChecker.url);
/*  91 */           TFM_Log.severe(ex);
/*  92 */           return;
/*     */         }
/*  95 */         Iterator status = statusJson.iterator();
/*  96 */         while (status.hasNext())
/*     */         {
/*  98 */           Iterator serviceIt = ((JSONObject)status.next()).entrySet().iterator();
/*  99 */           while (serviceIt.hasNext())
/*     */           {
/* 102 */             Map.Entry<String, String> pair = (Map.Entry)serviceIt.next();
/* 104 */             if ("lastcheck".equals(pair.getKey()))
/*     */             {
/* 106 */               TFM_ServiceChecker.access$102((String)pair.getValue());
/*     */             }
/* 110 */             else if ("version".equals(pair.getKey()))
/*     */             {
/* 112 */               TFM_ServiceChecker.access$202((String)pair.getValue());
/*     */             }
/*     */             else
/*     */             {
/* 116 */               TFM_ServiceChecker.ServiceStatus service = (TFM_ServiceChecker.ServiceStatus)TFM_ServiceChecker.services.get(pair.getKey());
/* 117 */               if (service != null) {
/* 122 */                 if (((String)pair.getValue()).contains(":"))
/*     */                 {
/* 124 */                   String[] statusString = ((String)pair.getValue()).split(":");
/* 125 */                   service.setColor(statusString[0]);
/* 126 */                   service.setMessage(statusString[1]);
/* 127 */                   service.setUptime(statusString[2]);
/*     */                 }
/*     */                 else
/*     */                 {
/* 131 */                   service.setColor((String)pair.getValue());
/* 132 */                   service.setMessage("yellow".equals(pair.getValue()) ? "Problem" : "red".equals(pair.getValue()) ? "Offline" : "Online");
/*     */                 }
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/* 136 */         if (TFM_ServiceChecker.lastCheck.equals("Unknown")) {
/* 138 */           TFM_ServiceChecker.access$102(TFM_Util.dateToString(new Date()));
/*     */         }
/*     */       }
/*     */     };
/*     */   }
/*     */   
/*     */   public static List<ServiceStatus> getAllStatuses()
/*     */   {
/* 146 */     List<ServiceStatus> servicesList = new ArrayList();
/* 147 */     for (String key : services.keySet()) {
/* 149 */       servicesList.add(services.get(key));
/*     */     }
/* 151 */     return servicesList;
/*     */   }
/*     */   
/*     */   public static String getLastCheck()
/*     */   {
/* 156 */     return lastCheck;
/*     */   }
/*     */   
/*     */   public static String getVersion()
/*     */   {
/* 161 */     return version;
/*     */   }
/*     */   
/*     */   public static class ServiceStatus
/*     */   {
/*     */     private String name;
/* 167 */     private String uptime = "100.0";
/* 168 */     private ChatColor color = ChatColor.DARK_GREEN;
/* 169 */     private String message = "Online";
/*     */     
/*     */     public ServiceStatus(String name)
/*     */     {
/* 173 */       this.name = name;
/*     */     }
/*     */     
/*     */     public String getName()
/*     */     {
/* 178 */       return name;
/*     */     }
/*     */     
/*     */     public String getUptime()
/*     */     {
/* 183 */       return uptime;
/*     */     }
/*     */     
/*     */     public float getUptimeFloat()
/*     */     {
/* 188 */       return Float.parseFloat(uptime);
/*     */     }
/*     */     
/*     */     public ChatColor getUptimeColor()
/*     */     {
/* 193 */       return getUptimeFloat() > 90.0F ? ChatColor.GOLD : getUptimeFloat() > 95.0F ? ChatColor.GREEN : ChatColor.RED;
/*     */     }
/*     */     
/*     */     public ChatColor getColor()
/*     */     {
/* 198 */       return color;
/*     */     }
/*     */     
/*     */     public String getMessage()
/*     */     {
/* 203 */       return message;
/*     */     }
/*     */     
/*     */     public String getFormattedStatus()
/*     */     {
/* 208 */       String status = ChatColor.BLUE + "- " + ChatColor.GRAY + name + ChatColor.WHITE + ": " + color + message + ChatColor.WHITE;
/* 210 */       if (!TFM_ServiceChecker.version.contains("Mojang")) {
/* 212 */         status = status + " (" + getUptimeColor() + getUptime() + ChatColor.WHITE + "%)";
/*     */       }
/* 215 */       return status;
/*     */     }
/*     */     
/*     */     public void setUptime(String uptime)
/*     */     {
/* 220 */       this.uptime = uptime;
/*     */     }
/*     */     
/*     */     public void setColor(ChatColor color)
/*     */     {
/* 225 */       this.color = color;
/*     */     }
/*     */     
/*     */     public void setColor(String color)
/*     */     {
/* 230 */       if ("green".equals(color)) {
/* 232 */         this.color = ChatColor.DARK_GREEN;
/* 234 */       } else if ("yellow".equals(color)) {
/* 236 */         this.color = ChatColor.YELLOW;
/*     */       } else {
/* 240 */         this.color = ChatColor.RED;
/*     */       }
/*     */     }
/*     */     
/*     */     public void setMessage(String message)
/*     */     {
/* 246 */       this.message = message;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\SilentBloodGaming\Desktop\MasterFreedomMod.jar!\me\StevenLawson\TotalFreedomMod\TFM_ServiceChecker.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */