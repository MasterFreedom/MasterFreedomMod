/*     */ package me.StevenLawson.TotalFreedomMod;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.UUID;
/*     */ import me.StevenLawson.TotalFreedomMod.Config.TFM_Config;
/*     */ import me.StevenLawson.TotalFreedomMod.Config.TFM_ConfigEntry;
/*     */ import org.bukkit.entity.Player;
/*     */ 
/*     */ public class TFM_BanManager
/*     */ {
/*  22 */   private static final List<TFM_Ban> ipBans = new ArrayList();
/*  23 */   private static final List<TFM_Ban> uuidBans = new ArrayList();
/*  24 */   private static final List<UUID> unbannableUUIDs = new ArrayList();
/*     */   
/*     */   private TFM_BanManager()
/*     */   {
/*  29 */     throw new AssertionError();
/*     */   }
/*     */   
/*     */   public static void load()
/*     */   {
/*  34 */     ipBans.clear();
/*  35 */     uuidBans.clear();
/*  36 */     unbannableUUIDs.clear();
/*     */     
/*  38 */     TFM_Config config = new TFM_Config(TotalFreedomMod.plugin, "bans.yml", true);
/*  39 */     config.load();
/*  41 */     for (String banString : config.getStringList("ips")) {
/*     */       try
/*     */       {
/*  45 */         addIpBan(new TFM_Ban(banString, TFM_Ban.BanType.IP));
/*     */       }
/*     */       catch (RuntimeException ex)
/*     */       {
/*  49 */         TFM_Log.warning("Could not load IP ban: " + banString);
/*     */       }
/*     */     }
/*  53 */     for (String banString : config.getStringList("uuids")) {
/*     */       try
/*     */       {
/*  57 */         addUuidBan(new TFM_Ban(banString, TFM_Ban.BanType.UUID));
/*     */       }
/*     */       catch (RuntimeException ex)
/*     */       {
/*  61 */         TFM_Log.warning("Could not load UUID ban: " + banString);
/*     */       }
/*     */     }
/*  66 */     save();
/*  67 */     TFM_Log.info("Loaded " + ipBans.size() + " IP bans and " + uuidBans.size() + " UUID bans");
/*     */     
/*  70 */     TFM_UuidManager.TFM_UuidResolver resolver = new TFM_UuidManager.TFM_UuidResolver(TFM_ConfigEntry.UNBANNABLE_USERNAMES.getList());
/*  72 */     for (UUID uuid : resolver.call().values()) {
/*  74 */       unbannableUUIDs.add(uuid);
/*     */     }
/*  77 */     TFM_Log.info("Loaded " + unbannableUUIDs.size() + " unbannable UUIDs");
/*     */   }
/*     */   
/*     */   public static void save()
/*     */   {
/*  82 */     TFM_Config config = new TFM_Config(TotalFreedomMod.plugin, "bans.yml", true);
/*  83 */     config.load();
/*     */     
/*  85 */     List<String> newIpBans = new ArrayList();
/*  86 */     List<String> newUuidBans = new ArrayList();
/*  88 */     for (TFM_Ban savedBan : ipBans) {
/*  90 */       if (!savedBan.isExpired()) {
/*  92 */         newIpBans.add(savedBan.toString());
/*     */       }
/*     */     }
/*  96 */     for (TFM_Ban savedBan : uuidBans) {
/*  98 */       if ((!savedBan.isExpired()) && (!unbannableUUIDs.contains(UUID.fromString(savedBan.getSubject())))) {
/* 100 */         newUuidBans.add(savedBan.toString());
/*     */       }
/*     */     }
/* 104 */     config.set("ips", newIpBans);
/* 105 */     config.set("uuids", newUuidBans);
/*     */     
/* 108 */     config.save();
/*     */   }
/*     */   
/*     */   public static List<TFM_Ban> getIpBanList()
/*     */   {
/* 113 */     return Collections.unmodifiableList(ipBans);
/*     */   }
/*     */   
/*     */   public static List<TFM_Ban> getUuidBanList()
/*     */   {
/* 118 */     return Collections.unmodifiableList(uuidBans);
/*     */   }
/*     */   
/*     */   public static TFM_Ban getByIp(String ip)
/*     */   {
/* 123 */     for (TFM_Ban ban : ipBans) {
/* 125 */       if (!ban.isExpired())
/*     */       {
/* 131 */         if (ban.getSubject().contains("*"))
/*     */         {
/* 133 */           String[] subjectParts = ban.getSubject().split("\\.");
/* 134 */           String[] ipParts = ip.split("\\.");
/* 136 */           for (int i = 0; i < 4; i++) {
/* 138 */             if ((!subjectParts[i].equals("*")) && (!subjectParts[i].equals(ipParts[i]))) {
/*     */               break label115;
/*     */             }
/*     */           }
/* 144 */           return ban;
/*     */         }
/* 147 */         if (ban.getSubject().equals(ip)) {
/* 149 */           return ban;
/*     */         }
/*     */       }
/*     */     }
/*     */     label115:
/* 152 */     return null;
/*     */   }
/*     */   
/*     */   public static TFM_Ban getByUuid(UUID uuid)
/*     */   {
/* 157 */     for (TFM_Ban ban : uuidBans) {
/* 159 */       if (ban.getSubject().equalsIgnoreCase(uuid.toString())) {
/* 161 */         if (!ban.isExpired()) {
/* 166 */           return ban;
/*     */         }
/*     */       }
/*     */     }
/* 169 */     return null;
/*     */   }
/*     */   
/*     */   public static void unbanIp(String ip)
/*     */   {
/* 174 */     TFM_Ban ban = getByIp(ip);
/* 176 */     if (ban == null) {
/* 178 */       return;
/*     */     }
/* 181 */     removeBan(ban);
/* 182 */     save();
/*     */   }
/*     */   
/*     */   public static void unbanUuid(UUID uuid)
/*     */   {
/* 187 */     TFM_Ban ban = getByUuid(uuid);
/* 189 */     if (ban == null) {
/* 191 */       return;
/*     */     }
/* 194 */     removeBan(ban);
/*     */   }
/*     */   
/*     */   public static boolean isIpBanned(String ip)
/*     */   {
/* 199 */     return getByIp(ip) != null;
/*     */   }
/*     */   
/*     */   public static boolean isUuidBanned(UUID uuid)
/*     */   {
/* 204 */     return getByUuid(uuid) != null;
/*     */   }
/*     */   
/*     */   public static void addUuidBan(Player player)
/*     */   {
/* 209 */     addUuidBan(new TFM_Ban(TFM_UuidManager.getUniqueId(player), player.getName()));
/*     */   }
/*     */   
/*     */   public static void addUuidBan(TFM_Ban ban)
/*     */   {
/* 214 */     if (!ban.isComplete()) {
/* 216 */       throw new RuntimeException("Could not add UUID ban, Invalid format!");
/*     */     }
/* 219 */     if (ban.isExpired()) {
/* 221 */       return;
/*     */     }
/* 224 */     if (uuidBans.contains(ban)) {
/* 226 */       return;
/*     */     }
/* 229 */     if (unbannableUUIDs.contains(UUID.fromString(ban.getSubject()))) {
/* 231 */       return;
/*     */     }
/* 234 */     uuidBans.add(ban);
/* 235 */     save();
/*     */   }
/*     */   
/*     */   public static void addIpBan(Player player)
/*     */   {
/* 240 */     addIpBan(new TFM_Ban(TFM_Util.getIp(player), player.getName()));
/*     */   }
/*     */   
/*     */   public static void addIpBan(TFM_Ban ban)
/*     */   {
/* 245 */     if (!ban.isComplete()) {
/* 247 */       throw new RuntimeException("Could not add IP ban, Invalid format!");
/*     */     }
/* 250 */     if (ban.isExpired()) {
/* 252 */       return;
/*     */     }
/* 255 */     if (ipBans.contains(ban)) {
/* 257 */       return;
/*     */     }
/* 260 */     ipBans.add(ban);
/* 261 */     save();
/*     */   }
/*     */   
/*     */   public static void removeBan(TFM_Ban ban)
/*     */   {
/* 266 */     Iterator<TFM_Ban> ips = ipBans.iterator();
/* 267 */     while (ips.hasNext()) {
/* 269 */       if (((TFM_Ban)ips.next()).getSubject().equalsIgnoreCase(ban.getSubject())) {
/* 271 */         ips.remove();
/*     */       }
/*     */     }
/* 275 */     Iterator<TFM_Ban> uuids = uuidBans.iterator();
/* 276 */     while (uuids.hasNext()) {
/* 278 */       if (((TFM_Ban)uuids.next()).getSubject().equalsIgnoreCase(ban.getSubject())) {
/* 280 */         uuids.remove();
/*     */       }
/*     */     }
/* 284 */     save();
/*     */   }
/*     */   
/*     */   public static void purgeIpBans()
/*     */   {
/* 289 */     ipBans.clear();
/* 290 */     save();
/*     */   }
/*     */   
/*     */   public static void purgeUuidBans()
/*     */   {
/* 295 */     uuidBans.clear();
/* 296 */     save();
/*     */   }
/*     */ }


/* Location:              C:\Users\SilentBloodGaming\Desktop\MasterFreedomMod.jar!\me\StevenLawson\TotalFreedomMod\TFM_BanManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */