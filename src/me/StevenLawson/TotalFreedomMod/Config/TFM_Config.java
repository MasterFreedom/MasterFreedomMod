/*     */ package me.StevenLawson.TotalFreedomMod.Config;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStreamReader;
/*     */ import java.util.logging.Logger;
/*     */ import me.StevenLawson.TotalFreedomMod.TFM_Util;
/*     */ import org.apache.commons.lang3.exception.ExceptionUtils;
/*     */ import org.bukkit.configuration.InvalidConfigurationException;
/*     */ import org.bukkit.configuration.file.YamlConfiguration;
/*     */ import org.bukkit.plugin.Plugin;
/*     */ 
/*     */ public class TFM_Config
/*     */   extends YamlConfiguration
/*     */ {
/*     */   private final Plugin plugin;
/*     */   private final File configFile;
/*     */   private final boolean copyDefaults;
/*     */   
/*     */   public TFM_Config(Plugin plugin, String fileName, boolean copyDefaults)
/*     */   {
/*  38 */     this(plugin, TFM_Util.getPluginFile(plugin, fileName), copyDefaults);
/*     */   }
/*     */   
/*     */   public TFM_Config(Plugin plugin, File file, boolean copyDefaults)
/*     */   {
/*  56 */     this.plugin = plugin;
/*  57 */     configFile = file;
/*  58 */     this.copyDefaults = copyDefaults;
/*     */   }
/*     */   
/*     */   public boolean exists()
/*     */   {
/*  68 */     return configFile.exists();
/*     */   }
/*     */   
/*     */   public void save()
/*     */   {
/*     */     try
/*     */     {
/*  80 */       super.save(configFile);
/*     */     }
/*     */     catch (Exception ex)
/*     */     {
/*  84 */       plugin.getLogger().severe("Could not save configuration file: " + configFile.getName());
/*  85 */       plugin.getLogger().severe(ExceptionUtils.getStackTrace(ex));
/*     */     }
/*     */   }
/*     */   
/*     */   public void load()
/*     */   {
/*     */     try
/*     */     {
/* 100 */       if (copyDefaults)
/*     */       {
/* 102 */         if (!configFile.exists())
/*     */         {
/* 104 */           configFile.getParentFile().mkdirs();
/*     */           try
/*     */           {
/* 107 */             TFM_Util.copy(plugin.getResource(configFile.getName()), configFile);
/*     */           }
/*     */           catch (IOException ex)
/*     */           {
/* 111 */             plugin.getLogger().severe("Could not write default configuration file: " + configFile.getName());
/* 112 */             plugin.getLogger().severe(ExceptionUtils.getStackTrace(ex));
/*     */           }
/* 114 */           plugin.getLogger().info("Installed default configuration " + configFile.getName());
/*     */         }
/* 117 */         super.addDefaults(getDefaultConfig());
/*     */       }
/* 120 */       if (configFile.exists()) {
/* 122 */         super.load(configFile);
/*     */       }
/*     */     }
/*     */     catch (Exception ex)
/*     */     {
/* 127 */       plugin.getLogger().severe("Could not load configuration file: " + configFile.getName());
/* 128 */       plugin.getLogger().severe(ExceptionUtils.getStackTrace(ex));
/*     */     }
/*     */   }
/*     */   
/*     */   public YamlConfiguration getConfig()
/*     */   {
/* 140 */     return this;
/*     */   }
/*     */   
/*     */   public YamlConfiguration getDefaultConfig()
/*     */   {
/* 149 */     YamlConfiguration DEFAULT_CONFIG = new YamlConfiguration();
/*     */     try
/*     */     {
/* 152 */       InputStreamReader isr = new InputStreamReader(plugin.getResource(configFile.getName()));
/* 153 */       DEFAULT_CONFIG.load(isr);
/* 154 */       isr.close();
/*     */     }
/*     */     catch (IOException ex)
/*     */     {
/* 158 */       plugin.getLogger().severe("Could not load default configuration: " + configFile.getName());
/* 159 */       plugin.getLogger().severe(ExceptionUtils.getStackTrace(ex));
/* 160 */       return null;
/*     */     }
/*     */     catch (InvalidConfigurationException ex)
/*     */     {
/* 164 */       plugin.getLogger().severe("Could not load default configuration: " + configFile.getName());
/* 165 */       plugin.getLogger().severe(ExceptionUtils.getStackTrace(ex));
/* 166 */       return null;
/*     */     }
/* 168 */     return DEFAULT_CONFIG;
/*     */   }
/*     */ }


/* Location:              C:\Users\SilentBloodGaming\Desktop\MasterFreedomMod.jar!\me\StevenLawson\TotalFreedomMod\Config\TFM_Config.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */