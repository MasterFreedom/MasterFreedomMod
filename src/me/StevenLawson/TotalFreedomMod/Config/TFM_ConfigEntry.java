/*     */ package me.StevenLawson.TotalFreedomMod.Config;
/*     */ 
/*     */ import java.util.List;
/*     */ 
/*     */ public enum TFM_ConfigEntry
/*     */ {
/*   7 */   FORCE_IP_ENABLED(Boolean.class, "forceip.enabled"),  FORCE_IP_PORT(Integer.class, "forceip.port"),  FORCE_IP_KICKMSG(String.class, "forceip.kickmsg"),  ALLOW_EXPLOSIONS(Boolean.class, "allow.explosions"),  ALLOW_FIRE_PLACE(Boolean.class, "allow.fire_place"),  ALLOW_FIRE_SPREAD(Boolean.class, "allow.fire_spread"),  ALLOW_FLUID_SPREAD(Boolean.class, "allow.fluid_spread"),  ALLOW_LAVA_DAMAGE(Boolean.class, "allow.lava_damage"),  ALLOW_LAVA_PLACE(Boolean.class, "allow.lava_place"),  ALLOW_TNT_MINECARTS(Boolean.class, "allow.tnt_minecarts"),  ALLOW_WATER_PLACE(Boolean.class, "allow.water_place"),  MOB_LIMITER_ENABLED(Boolean.class, "moblimiter.enabled"),  MOB_LIMITER_MAX(Integer.class, "moblimiter.max"),  MOB_LIMITER_DISABLE_DRAGON(Boolean.class, "moblimiter.disable.dragon"),  MOB_LIMITER_DISABLE_GHAST(Boolean.class, "moblimiter.disable.ghast"),  MOB_LIMITER_DISABLE_GIANT(Boolean.class, "moblimiter.disable.giant"),  MOB_LIMITER_DISABLE_SLIME(Boolean.class, "moblimiter.disable.slime"),  HTTPD_ENABLED(Boolean.class, "httpd.enabled"),  HTTPD_PORT(Integer.class, "httpd.port"),  HTTPD_PUBLIC_FOLDER(String.class, "httpd.public_folder"),  SERVER_COLORFUL_MOTD(Boolean.class, "server.colorful_motd"),  SERVER_NAME(String.class, "server.name"),  SERVER_ADDRESS(String.class, "server.address"),  SERVER_MOTD(String.class, "server.motd"),  SERVER_OWNERS(List.class, "server.owners"),  SERVER_BAN_URL(String.class, "server.ban_url"),  SERVER_PERMBAN_URL(String.class, "server.permban_url"),  TWITTERBOT_ENABLED(Boolean.class, "twitterbot.enabled"),  TWITTERBOT_SECRET(String.class, "twitterbot.secret"),  TWITTERBOT_URL(String.class, "twitterbot.url"),  DISABLE_NIGHT(Boolean.class, "disable.night"),  DISABLE_WEATHER(Boolean.class, "disable.weather"),  ENABLE_PREPROCESS_LOG(Boolean.class, "preprocess_log"),  ENABLE_PET_PROTECT(Boolean.class, "petprotect.enabled"),  LANDMINES_ENABLED(Boolean.class, "landmines_enabled"),  TOSSMOB_ENABLED(Boolean.class, "tossmob_enabled"),  AUTOKICK_ENABLED(Boolean.class, "autokick.enabled"),  MP44_ENABLED(Boolean.class, "mp44_enabled"),  PROTECTAREA_ENABLED(Boolean.class, "protectarea.enabled"),  PROTECTAREA_SPAWNPOINTS(Boolean.class, "protectarea.auto_protect_spawnpoints"),  PROTECTAREA_RADIUS(Double.class, "protectarea.auto_protect_radius"),  NUKE_MONITOR_ENABLED(Boolean.class, "nukemonitor.enabled"),  NUKE_MONITOR_COUNT_BREAK(Integer.class, "nukemonitor.count_break"),  NUKE_MONITOR_COUNT_PLACE(Integer.class, "nukemonitor.count_place"),  NUKE_MONITOR_RANGE(Double.class, "nukemonitor.range"),  AUTOKICK_THRESHOLD(Double.class, "autokick.threshold"),  AUTOKICK_TIME(Integer.class, "autokick.time"),  LOGS_SECRET(String.class, "logs.secret"),  LOGS_URL(String.class, "logs.url"),  FLATLANDS_GENERATE(Boolean.class, "flatlands.generate"),  FLATLANDS_GENERATE_PARAMS(String.class, "flatlands.generate_params"),  ANNOUNCER_ENABLED(Boolean.class, "announcer.enabled"),  ANNOUNCER_INTERVAL(Integer.class, "announcer.interval"),  ANNOUNCER_PREFIX(String.class, "announcer.prefix"),  ANNOUNCER_ANNOUNCEMENTS(List.class, "announcer.announcements"),  EXPLOSIVE_RADIUS(Double.class, "explosive_radius"),  FREECAM_TRIGGER_COUNT(Integer.class, "freecam_trigger_count"),  SERVICE_CHECKER_URL(String.class, "service_checker_url"),  BLOCKED_COMMANDS(List.class, "blocked_commands"),  HOST_SENDER_NAMES(List.class, "host_sender_names"),  UNBANNABLE_USERNAMES(List.class, "unbannable_usernames"),  OVERLORD_IPS(List.class, "overlord_ips"),  NOADMIN_IPS(List.class, "noadmin_ips"),  ADMIN_ONLY_MODE(Boolean.class, "admin_only_mode"),  AUTO_ENTITY_WIPE(Boolean.class, "auto_wipe"),  CONSOLE_IS_SENIOR(Boolean.class, "console_is_senior");
/*     */   
/*     */   private final Class<?> type;
/*     */   private final String configName;
/*     */   
/*     */   private TFM_ConfigEntry(Class<?> type, String configName)
/*     */   {
/*  94 */     this.type = type;
/*  95 */     this.configName = configName;
/*     */   }
/*     */   
/*     */   public Class<?> getType()
/*     */   {
/* 100 */     return type;
/*     */   }
/*     */   
/*     */   public String getConfigName()
/*     */   {
/* 105 */     return configName;
/*     */   }
/*     */   
/*     */   public String getString()
/*     */   {
/* 110 */     return TFM_MainConfig.getString(this);
/*     */   }
/*     */   
/*     */   public String setString(String value)
/*     */   {
/* 115 */     TFM_MainConfig.setString(this, value);
/* 116 */     return value;
/*     */   }
/*     */   
/*     */   public Double getDouble()
/*     */   {
/* 121 */     return TFM_MainConfig.getDouble(this);
/*     */   }
/*     */   
/*     */   public Double setDouble(Double value)
/*     */   {
/* 126 */     TFM_MainConfig.setDouble(this, value);
/* 127 */     return value;
/*     */   }
/*     */   
/*     */   public Boolean getBoolean()
/*     */   {
/* 132 */     return TFM_MainConfig.getBoolean(this);
/*     */   }
/*     */   
/*     */   public Boolean setBoolean(Boolean value)
/*     */   {
/* 137 */     TFM_MainConfig.setBoolean(this, value);
/* 138 */     return value;
/*     */   }
/*     */   
/*     */   public Integer getInteger()
/*     */   {
/* 143 */     return TFM_MainConfig.getInteger(this);
/*     */   }
/*     */   
/*     */   public Integer setInteger(Integer value)
/*     */   {
/* 148 */     TFM_MainConfig.setInteger(this, value);
/* 149 */     return value;
/*     */   }
/*     */   
/*     */   public List<?> getList()
/*     */   {
/* 154 */     return TFM_MainConfig.getList(this);
/*     */   }
/*     */   
/*     */   public static TFM_ConfigEntry findConfigEntry(String name)
/*     */   {
/* 159 */     name = name.toLowerCase().replace("_", "");
/* 160 */     for (TFM_ConfigEntry entry : values()) {
/* 162 */       if (entry.toString().toLowerCase().replace("_", "").equals(name)) {
/* 164 */         return entry;
/*     */       }
/*     */     }
/* 167 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\SilentBloodGaming\Desktop\MasterFreedomMod.jar!\me\StevenLawson\TotalFreedomMod\Config\TFM_ConfigEntry.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */