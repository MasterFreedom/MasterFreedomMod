/*     */ package me.StevenLawson.TotalFreedomMod.Config;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.util.EnumMap;
/*     */ import java.util.List;
/*     */ import me.StevenLawson.TotalFreedomMod.TFM_Log;
/*     */ import me.StevenLawson.TotalFreedomMod.TotalFreedomMod;
/*     */ import org.apache.commons.io.FileUtils;
/*     */ import org.bukkit.configuration.InvalidConfigurationException;
/*     */ import org.bukkit.configuration.file.YamlConfiguration;
/*     */ 
/*     */ public class TFM_MainConfig
/*     */ {
/*  18 */   public static final File CONFIG_FILE = new File(TotalFreedomMod.plugin.getDataFolder(), "config.yml");
/*  25 */   private static final EnumMap<TFM_ConfigEntry, Object> ENTRY_MAP = new EnumMap(TFM_ConfigEntry.class);
/*     */   private static final TFM_Defaults DEFAULTS;
/*     */   
/*     */   static
/*     */   {
/*  27 */     TFM_Defaults tempDefaults = null;
/*     */     try
/*     */     {
/*     */       try
/*     */       {
/*  32 */         InputStream defaultConfig = getDefaultConfig();
/*  33 */         tempDefaults = new TFM_Defaults(defaultConfig, null);
/*  34 */         for (TFM_ConfigEntry entry : TFM_ConfigEntry.values()) {
/*  36 */           ENTRY_MAP.put(entry, tempDefaults.get(entry.getConfigName()));
/*     */         }
/*  38 */         defaultConfig.close();
/*     */       }
/*     */       catch (IOException ex)
/*     */       {
/*  42 */         TFM_Log.severe(ex);
/*     */       }
/*  45 */       copyDefaultConfig(CONFIG_FILE);
/*     */       
/*  47 */       load();
/*     */     }
/*     */     catch (Exception ex)
/*     */     {
/*  51 */       TFM_Log.severe(ex);
/*     */     }
/*  54 */     DEFAULTS = tempDefaults;
/*     */   }
/*     */   
/*     */   private TFM_MainConfig()
/*     */   {
/*  59 */     throw new AssertionError();
/*     */   }
/*     */   
/*     */   public static void load()
/*     */   {
/*     */     try
/*     */     {
/*  66 */       YamlConfiguration config = new YamlConfiguration();
/*     */       
/*  68 */       config.load(CONFIG_FILE);
/*  70 */       for (TFM_ConfigEntry entry : TFM_ConfigEntry.values())
/*     */       {
/*  72 */         String path = entry.getConfigName();
/*  73 */         if (config.contains(path))
/*     */         {
/*  75 */           Object value = config.get(path);
/*  76 */           if ((value == null) || (entry.getType().isAssignableFrom(value.getClass()))) {
/*  78 */             ENTRY_MAP.put(entry, value);
/*     */           } else {
/*  82 */             TFM_Log.warning("Value for " + entry.getConfigName() + " is of type " + value.getClass().getSimpleName() + ". Needs to be " + entry.getType().getSimpleName() + ". Using default value.");
/*     */           }
/*     */         }
/*     */         else
/*     */         {
/*  87 */           TFM_Log.warning("Missing configuration entry " + entry.getConfigName() + ". Using default value.");
/*     */         }
/*     */       }
/*     */     }
/*     */     catch (FileNotFoundException ex)
/*     */     {
/*  93 */       TFM_Log.severe(ex);
/*     */     }
/*     */     catch (IOException ex)
/*     */     {
/*  97 */       TFM_Log.severe(ex);
/*     */     }
/*     */     catch (InvalidConfigurationException ex)
/*     */     {
/* 101 */       TFM_Log.severe(ex);
/*     */     }
/*     */   }
/*     */   
/*     */   public static String getString(TFM_ConfigEntry entry)
/*     */   {
/*     */     try
/*     */     {
/* 109 */       return (String)get(entry, String.class);
/*     */     }
/*     */     catch (IllegalArgumentException ex)
/*     */     {
/* 113 */       TFM_Log.severe(ex);
/*     */     }
/* 115 */     return null;
/*     */   }
/*     */   
/*     */   public static void setString(TFM_ConfigEntry entry, String value)
/*     */   {
/*     */     try
/*     */     {
/* 122 */       set(entry, value, String.class);
/*     */     }
/*     */     catch (IllegalArgumentException ex)
/*     */     {
/* 126 */       TFM_Log.severe(ex);
/*     */     }
/*     */   }
/*     */   
/*     */   public static Double getDouble(TFM_ConfigEntry entry)
/*     */   {
/*     */     try
/*     */     {
/* 134 */       return (Double)get(entry, Double.class);
/*     */     }
/*     */     catch (IllegalArgumentException ex)
/*     */     {
/* 138 */       TFM_Log.severe(ex);
/*     */     }
/* 140 */     return null;
/*     */   }
/*     */   
/*     */   public static void setDouble(TFM_ConfigEntry entry, Double value)
/*     */   {
/*     */     try
/*     */     {
/* 147 */       set(entry, value, Double.class);
/*     */     }
/*     */     catch (IllegalArgumentException ex)
/*     */     {
/* 151 */       TFM_Log.severe(ex);
/*     */     }
/*     */   }
/*     */   
/*     */   public static Boolean getBoolean(TFM_ConfigEntry entry)
/*     */   {
/*     */     try
/*     */     {
/* 159 */       return (Boolean)get(entry, Boolean.class);
/*     */     }
/*     */     catch (IllegalArgumentException ex)
/*     */     {
/* 163 */       TFM_Log.severe(ex);
/*     */     }
/* 165 */     return null;
/*     */   }
/*     */   
/*     */   public static void setBoolean(TFM_ConfigEntry entry, Boolean value)
/*     */   {
/*     */     try
/*     */     {
/* 172 */       set(entry, value, Boolean.class);
/*     */     }
/*     */     catch (IllegalArgumentException ex)
/*     */     {
/* 176 */       TFM_Log.severe(ex);
/*     */     }
/*     */   }
/*     */   
/*     */   public static Integer getInteger(TFM_ConfigEntry entry)
/*     */   {
/*     */     try
/*     */     {
/* 184 */       return (Integer)get(entry, Integer.class);
/*     */     }
/*     */     catch (IllegalArgumentException ex)
/*     */     {
/* 188 */       TFM_Log.severe(ex);
/*     */     }
/* 190 */     return null;
/*     */   }
/*     */   
/*     */   public static void setInteger(TFM_ConfigEntry entry, Integer value)
/*     */   {
/*     */     try
/*     */     {
/* 197 */       set(entry, value, Integer.class);
/*     */     }
/*     */     catch (IllegalArgumentException ex)
/*     */     {
/* 201 */       TFM_Log.severe(ex);
/*     */     }
/*     */   }
/*     */   
/*     */   public static List getList(TFM_ConfigEntry entry)
/*     */   {
/*     */     try
/*     */     {
/* 209 */       return (List)get(entry, List.class);
/*     */     }
/*     */     catch (IllegalArgumentException ex)
/*     */     {
/* 213 */       TFM_Log.severe(ex);
/*     */     }
/* 215 */     return null;
/*     */   }
/*     */   
/*     */   public static <T> T get(TFM_ConfigEntry entry, Class<T> type)
/*     */     throws IllegalArgumentException
/*     */   {
/* 220 */     Object value = ENTRY_MAP.get(entry);
/*     */     try
/*     */     {
/* 223 */       return (T)type.cast(value);
/*     */     }
/*     */     catch (ClassCastException ex)
/*     */     {
/* 227 */       throw new IllegalArgumentException(entry.name() + " is not of type " + type.getSimpleName());
/*     */     }
/*     */   }
/*     */   
/*     */   public static <T> void set(TFM_ConfigEntry entry, T value, Class<T> type)
/*     */     throws IllegalArgumentException
/*     */   {
/* 233 */     if (!type.isAssignableFrom(entry.getType())) {
/* 235 */       throw new IllegalArgumentException(entry.name() + " is not of type " + type.getSimpleName());
/*     */     }
/* 237 */     if ((value != null) && (!type.isAssignableFrom(value.getClass()))) {
/* 239 */       throw new IllegalArgumentException("Value is not of type " + type.getSimpleName());
/*     */     }
/* 241 */     ENTRY_MAP.put(entry, value);
/*     */   }
/*     */   
/*     */   private static void copyDefaultConfig(File targetFile)
/*     */   {
/* 246 */     if (targetFile.exists()) {
/* 248 */       return;
/*     */     }
/* 251 */     TFM_Log.info("Installing default configuration file template: " + targetFile.getPath());
/*     */     try
/*     */     {
/* 255 */       InputStream defaultConfig = getDefaultConfig();
/* 256 */       FileUtils.copyInputStreamToFile(defaultConfig, targetFile);
/* 257 */       defaultConfig.close();
/*     */     }
/*     */     catch (IOException ex)
/*     */     {
/* 261 */       TFM_Log.severe(ex);
/*     */     }
/*     */   }
/*     */   
/*     */   private static InputStream getDefaultConfig()
/*     */   {
/* 267 */     return TotalFreedomMod.plugin.getResource("config.yml");
/*     */   }
/*     */   
/*     */   public static TFM_Defaults getDefaults()
/*     */   {
/* 272 */     return DEFAULTS;
/*     */   }
/*     */   
/*     */   public static class TFM_Defaults
/*     */   {
/* 277 */     private YamlConfiguration defaults = null;
/*     */     
/*     */     private TFM_Defaults(InputStream defaultConfig)
/*     */     {
/*     */       try
/*     */       {
/* 283 */         defaults = new YamlConfiguration();
/* 284 */         InputStreamReader isr = new InputStreamReader(defaultConfig);
/* 285 */         defaults.load(isr);
/* 286 */         isr.close();
/*     */       }
/*     */       catch (IOException ex)
/*     */       {
/* 290 */         TFM_Log.severe(ex);
/*     */       }
/*     */       catch (InvalidConfigurationException ex)
/*     */       {
/* 294 */         TFM_Log.severe(ex);
/*     */       }
/*     */     }
/*     */     
/*     */     public Object get(String path)
/*     */     {
/* 300 */       return defaults.get(path);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\SilentBloodGaming\Desktop\MasterFreedomMod.jar!\me\StevenLawson\TotalFreedomMod\Config\TFM_MainConfig.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */