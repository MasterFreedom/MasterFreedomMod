/*     */ package me.StevenLawson.TotalFreedomMod;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.UUID;
/*     */ import org.bukkit.configuration.ConfigurationSection;
/*     */ 
/*     */ public class TFM_Player
/*     */ {
/*     */   private final UUID uuid;
/*     */   private String firstJoinName;
/*     */   private String lastJoinName;
/*     */   private long firstJoinUnix;
/*     */   private long lastJoinUnix;
/*     */   private final List<String> ips;
/*     */   
/*     */   protected TFM_Player(UUID uuid, ConfigurationSection section)
/*     */   {
/*  20 */     this(uuid);
/*     */     
/*  22 */     firstJoinName = section.getString("firstjoinname");
/*  23 */     lastJoinName = section.getString("lastjoinname");
/*     */     
/*  25 */     firstJoinUnix = section.getLong("firstjoinunix");
/*  26 */     lastJoinUnix = section.getLong("lastjoinunix");
/*     */     
/*  28 */     ips.addAll(section.getStringList("ips"));
/*     */   }
/*     */   
/*     */   protected TFM_Player(UUID uuid, String firstJoinName, String lastJoinName, long firstJoinUnix, long lastJoinUnix, List<String> ips)
/*     */   {
/*  33 */     this(uuid);
/*     */     
/*  35 */     this.firstJoinName = firstJoinName;
/*  36 */     this.lastJoinName = lastJoinName;
/*     */     
/*  38 */     this.firstJoinUnix = firstJoinUnix;
/*  39 */     this.lastJoinUnix = lastJoinUnix;
/*     */     
/*  41 */     this.ips.addAll(ips);
/*     */   }
/*     */   
/*     */   protected TFM_Player(UUID uuid)
/*     */   {
/*  46 */     if (uuid == null) {
/*  48 */       throw new IllegalArgumentException("UUID can not be null!");
/*     */     }
/*  51 */     this.uuid = uuid;
/*  52 */     ips = new ArrayList();
/*     */   }
/*     */   
/*     */   public UUID getUniqueId()
/*     */   {
/*  58 */     return uuid;
/*     */   }
/*     */   
/*     */   public List<String> getIps()
/*     */   {
/*  63 */     return Collections.unmodifiableList(ips);
/*     */   }
/*     */   
/*     */   public String getFirstLoginName()
/*     */   {
/*  68 */     return firstJoinName;
/*     */   }
/*     */   
/*     */   public void setFirstLoginName(String firstJoinName)
/*     */   {
/*  73 */     this.firstJoinName = firstJoinName;
/*     */   }
/*     */   
/*     */   public String getLastLoginName()
/*     */   {
/*  78 */     return lastJoinName;
/*     */   }
/*     */   
/*     */   public void setLastLoginName(String lastJoinName)
/*     */   {
/*  83 */     this.lastJoinName = lastJoinName;
/*     */   }
/*     */   
/*     */   public long getFirstLoginUnix()
/*     */   {
/*  88 */     return firstJoinUnix;
/*     */   }
/*     */   
/*     */   public void setFirstLoginUnix(long firstJoinUnix)
/*     */   {
/*  93 */     this.firstJoinUnix = firstJoinUnix;
/*     */   }
/*     */   
/*     */   public long getLastLoginUnix()
/*     */   {
/*  98 */     return lastJoinUnix;
/*     */   }
/*     */   
/*     */   public void setLastLoginUnix(long lastJoinUnix)
/*     */   {
/* 103 */     this.lastJoinUnix = lastJoinUnix;
/*     */   }
/*     */   
/*     */   public boolean addIp(String ip)
/*     */   {
/* 108 */     if (!ips.contains(ip))
/*     */     {
/* 110 */       ips.add(ip);
/* 111 */       return true;
/*     */     }
/* 113 */     return false;
/*     */   }
/*     */   
/*     */   public final boolean isComplete()
/*     */   {
/* 118 */     return (firstJoinName != null) && (lastJoinName != null) && (firstJoinUnix != 0L) && (lastJoinUnix != 0L) && (!ips.isEmpty());
/*     */   }
/*     */   
/*     */   public void save()
/*     */   {
/* 127 */     TFM_PlayerList.save(this);
/*     */   }
/*     */ }


/* Location:              C:\Users\SilentBloodGaming\Desktop\MasterFreedomMod.jar!\me\StevenLawson\TotalFreedomMod\TFM_Player.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */