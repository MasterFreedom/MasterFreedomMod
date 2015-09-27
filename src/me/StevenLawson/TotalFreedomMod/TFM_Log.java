/*     */ package me.StevenLawson.TotalFreedomMod;
/*     */ 
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ 
/*     */ public class TFM_Log
/*     */ {
/*   8 */   private static final Logger FALLBACK_LOGGER = Logger.getLogger("Minecraft-Server");
/*   9 */   private static Logger serverLogger = null;
/*  10 */   private static Logger pluginLogger = null;
/*     */   
/*     */   private TFM_Log()
/*     */   {
/*  14 */     throw new AssertionError();
/*     */   }
/*     */   
/*     */   public static void info(String message)
/*     */   {
/*  20 */     info(message, Boolean.valueOf(false));
/*     */   }
/*     */   
/*     */   public static void info(String message, Boolean raw)
/*     */   {
/*  25 */     log(Level.INFO, message, raw.booleanValue());
/*     */   }
/*     */   
/*     */   public static void info(Throwable ex)
/*     */   {
/*  30 */     log(Level.INFO, ex);
/*     */   }
/*     */   
/*     */   public static void warning(String message)
/*     */   {
/*  36 */     warning(message, Boolean.valueOf(false));
/*     */   }
/*     */   
/*     */   public static void warning(String message, Boolean raw)
/*     */   {
/*  41 */     log(Level.WARNING, message, raw.booleanValue());
/*     */   }
/*     */   
/*     */   public static void warning(Throwable ex)
/*     */   {
/*  46 */     log(Level.WARNING, ex);
/*     */   }
/*     */   
/*     */   public static void severe(String message)
/*     */   {
/*  52 */     severe(message, Boolean.valueOf(false));
/*     */   }
/*     */   
/*     */   public static void severe(String message, Boolean raw)
/*     */   {
/*  57 */     log(Level.SEVERE, message, raw.booleanValue());
/*     */   }
/*     */   
/*     */   public static void severe(Throwable ex)
/*     */   {
/*  62 */     log(Level.SEVERE, ex);
/*     */   }
/*     */   
/*     */   private static void log(Level level, String message, boolean raw)
/*     */   {
/*  68 */     getLogger(raw).log(level, message);
/*     */   }
/*     */   
/*     */   private static void log(Level level, Throwable throwable)
/*     */   {
/*  73 */     getLogger(false).log(level, null, throwable);
/*     */   }
/*     */   
/*     */   public static void setServerLogger(Logger logger)
/*     */   {
/*  78 */     serverLogger = logger;
/*     */   }
/*     */   
/*     */   public static void setPluginLogger(Logger logger)
/*     */   {
/*  83 */     pluginLogger = logger;
/*     */   }
/*     */   
/*     */   private static Logger getLogger(boolean raw)
/*     */   {
/*  88 */     if ((raw) || (pluginLogger == null)) {
/*  90 */       return serverLogger != null ? serverLogger : FALLBACK_LOGGER;
/*     */     }
/*  94 */     return pluginLogger;
/*     */   }
/*     */   
/*     */   public static Logger getPluginLogger()
/*     */   {
/* 100 */     return pluginLogger != null ? pluginLogger : FALLBACK_LOGGER;
/*     */   }
/*     */   
/*     */   public static Logger getServerLogger()
/*     */   {
/* 105 */     return serverLogger != null ? serverLogger : FALLBACK_LOGGER;
/*     */   }
/*     */ }


/* Location:              C:\Users\SilentBloodGaming\Desktop\MasterFreedomMod.jar!\me\StevenLawson\TotalFreedomMod\TFM_Log.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */