/*     */ package me.StevenLawson.TotalFreedomMod;
/*     */ 
/*     */ import com.google.common.collect.Sets;
/*     */ import java.io.File;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.UUID;
/*     */ import me.StevenLawson.TotalFreedomMod.Config.TFM_Config;
/*     */ import org.bukkit.Bukkit;
/*     */ import org.bukkit.entity.Player;
/*     */ 
/*     */ public class TFM_PlayerList
/*     */ {
/*  16 */   private static final Map<UUID, TFM_Player> PLAYER_LIST = new HashMap();
/*     */   
/*     */   private TFM_PlayerList()
/*     */   {
/*  19 */     throw new AssertionError();
/*     */   }
/*     */   
/*     */   public static Set<TFM_Player> getAllPlayers()
/*     */   {
/*  23 */     return Collections.unmodifiableSet(Sets.newHashSet(PLAYER_LIST.values()));
/*     */   }
/*     */   
/*     */   public static void load()
/*     */   {
/*  27 */     PLAYER_LIST.clear();
/*  30 */     for (Player player : Bukkit.getOnlinePlayers()) {
/*  31 */       getEntry(player);
/*     */     }
/*  34 */     TFM_Log.info("Loaded playerdata for " + PLAYER_LIST.size() + " players");
/*     */   }
/*     */   
/*     */   public static void saveAll()
/*     */   {
/*  38 */     for (TFM_Player entry : PLAYER_LIST.values()) {
/*  39 */       save(entry);
/*     */     }
/*     */   }
/*     */   
/*     */   public static TFM_Player getEntry(UUID uuid)
/*     */   {
/*  45 */     if (PLAYER_LIST.containsKey(uuid)) {
/*  46 */       return (TFM_Player)PLAYER_LIST.get(uuid);
/*     */     }
/*  49 */     File configFile = getConfigFile(uuid);
/*  51 */     if (!configFile.exists()) {
/*  52 */       return null;
/*     */     }
/*  55 */     TFM_Player entry = new TFM_Player(uuid, getConfig(uuid));
/*  57 */     if (entry.isComplete())
/*     */     {
/*  58 */       PLAYER_LIST.put(uuid, entry);
/*  59 */       return entry;
/*     */     }
/*  61 */     TFM_Log.warning("Could not load entry: Entry is not complete!");
/*  62 */     configFile.delete();
/*     */     
/*  65 */     return null;
/*     */   }
/*     */   
/*     */   public static TFM_Player getEntry(Player player)
/*     */   {
/*  69 */     UUID uuid = TFM_UuidManager.getUniqueId(player);
/*  70 */     TFM_Player entry = getEntry(uuid);
/*  72 */     if (entry != null) {
/*  73 */       return entry;
/*     */     }
/*  76 */     long unix = TFM_Util.getUnixTime();
/*  77 */     entry = new TFM_Player(uuid);
/*  78 */     entry.setFirstLoginName(player.getName());
/*  79 */     entry.setLastLoginName(player.getName());
/*  80 */     entry.setFirstLoginUnix(unix);
/*  81 */     entry.setLastLoginUnix(unix);
/*  82 */     entry.addIp(TFM_Util.getIp(player));
/*     */     
/*  84 */     save(entry);
/*  85 */     PLAYER_LIST.put(uuid, entry);
/*     */     
/*  87 */     return entry;
/*     */   }
/*     */   
/*     */   public static void removeEntry(Player player)
/*     */   {
/*  91 */     UUID uuid = TFM_UuidManager.getUniqueId(player);
/*  93 */     if (!PLAYER_LIST.containsKey(uuid)) {
/*  94 */       return;
/*     */     }
/*  97 */     save((TFM_Player)PLAYER_LIST.get(uuid));
/*     */     
/*  99 */     PLAYER_LIST.remove(uuid);
/*     */   }
/*     */   
/*     */   public static boolean existsEntry(Player player)
/*     */   {
/* 103 */     return existsEntry(TFM_UuidManager.getUniqueId(player));
/*     */   }
/*     */   
/*     */   public static boolean existsEntry(UUID uuid)
/*     */   {
/* 107 */     return getConfigFile(uuid).exists();
/*     */   }
/*     */   
/*     */   public static void setUniqueId(TFM_Player entry, UUID newUuid)
/*     */   {
/* 111 */     if (entry.getUniqueId().equals(newUuid))
/*     */     {
/* 112 */       TFM_Log.warning("Not setting new UUID: UUIDs match!");
/* 113 */       return;
/*     */     }
/* 117 */     TFM_Player newEntry = new TFM_Player(newUuid, entry.getFirstLoginName(), entry.getLastLoginName(), entry.getFirstLoginUnix(), entry.getLastLoginUnix(), entry.getIps());
/*     */     
/* 124 */     newEntry.save();
/* 125 */     PLAYER_LIST.put(newUuid, newEntry);
/*     */     
/* 128 */     PLAYER_LIST.remove(entry.getUniqueId());
/* 129 */     File oldFile = getConfigFile(entry.getUniqueId());
/* 130 */     if ((oldFile.exists()) && (!oldFile.delete())) {
/* 131 */       TFM_Log.warning("Could not delete config: " + getConfigFile(entry.getUniqueId()).getName());
/*     */     }
/*     */   }
/*     */   
/*     */   public static void purgeAll()
/*     */   {
/* 136 */     for (File file : getConfigFolder().listFiles()) {
/* 137 */       file.delete();
/*     */     }
/* 141 */     load();
/*     */   }
/*     */   
/*     */   public static File getConfigFolder()
/*     */   {
/* 145 */     return new File(TotalFreedomMod.plugin.getDataFolder(), "players");
/*     */   }
/*     */   
/*     */   public static File getConfigFile(UUID uuid)
/*     */   {
/* 149 */     return new File(getConfigFolder(), uuid + ".yml");
/*     */   }
/*     */   
/*     */   public static TFM_Config getConfig(UUID uuid)
/*     */   {
/* 153 */     TFM_Config config = new TFM_Config(TotalFreedomMod.plugin, getConfigFile(uuid), false);
/* 154 */     config.load();
/* 155 */     return config;
/*     */   }
/*     */   
/*     */   public static void save(TFM_Player entry)
/*     */   {
/* 159 */     if (!entry.isComplete()) {
/* 160 */       throw new IllegalArgumentException("Entry is not complete!");
/*     */     }
/* 163 */     TFM_Config config = getConfig(entry.getUniqueId());
/* 164 */     config.set("firstjoinname", entry.getFirstLoginName());
/* 165 */     config.set("lastjoinname", entry.getLastLoginName());
/* 166 */     config.set("firstjoinunix", Long.valueOf(entry.getFirstLoginUnix()));
/* 167 */     config.set("lastjoinunix", Long.valueOf(entry.getLastLoginUnix()));
/* 168 */     config.set("ips", entry.getIps());
/* 169 */     config.save();
/*     */   }
/*     */ }


/* Location:              C:\Users\SilentBloodGaming\Desktop\MasterFreedomMod.jar!\me\StevenLawson\TotalFreedomMod\TFM_PlayerList.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */