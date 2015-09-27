/*     */ package me.StevenLawson.TotalFreedomMod.HTTPD;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.IOException;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.Callable;
/*     */ import java.util.concurrent.Future;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import me.StevenLawson.TotalFreedomMod.Config.TFM_ConfigEntry;
/*     */ import me.StevenLawson.TotalFreedomMod.TFM_Log;
/*     */ import me.StevenLawson.TotalFreedomMod.TotalFreedomMod;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ import org.apache.commons.lang3.exception.ExceptionUtils;
/*     */ import org.bukkit.Bukkit;
/*     */ import org.bukkit.scheduler.BukkitScheduler;
/*     */ 
/*     */ public class TFM_HTTPD_Manager
/*     */ {
/*     */   private TFM_HTTPD_Manager()
/*     */   {
/*  30 */     throw new AssertionError();
/*     */   }
/*     */   
/*  35 */   public static String MIME_DEFAULT_BINARY = "application/octet-stream";
/*  36 */   private static final Pattern EXT_REGEX = Pattern.compile("\\.([^\\.\\s]+)$");
/*  37 */   public static final int PORT = TFM_ConfigEntry.HTTPD_PORT.getInteger().intValue();
/*  38 */   private static final TFM_HTTPD HTTPD = new TFM_HTTPD(PORT);
/*     */   
/*     */   public static void start()
/*     */   {
/*  43 */     if (!TFM_ConfigEntry.HTTPD_ENABLED.getBoolean().booleanValue()) {
/*  45 */       return;
/*     */     }
/*     */     try
/*     */     {
/*  50 */       HTTPD.start();
/*  52 */       if (HTTPD.isAlive()) {
/*  54 */         TFM_Log.info("TFM HTTPd started. Listening on port: " + HTTPD.getListeningPort());
/*     */       } else {
/*  58 */         TFM_Log.info("Error starting TFM HTTPd.");
/*     */       }
/*     */     }
/*     */     catch (IOException ex)
/*     */     {
/*  63 */       TFM_Log.severe(ex);
/*     */     }
/*     */   }
/*     */   
/*     */   public static void stop()
/*     */   {
/*  69 */     if (!TFM_ConfigEntry.HTTPD_ENABLED.getBoolean().booleanValue()) {
/*  71 */       return;
/*     */     }
/*  74 */     HTTPD.stop();
/*     */     
/*  76 */     TFM_Log.info("TFM HTTPd stopped.");
/*     */   }
/*     */   
/*     */   private static enum ModuleType
/*     */   {
/*  81 */     DUMP(new ModuleExecutable(false, "dump")),  HELP(new ModuleExecutable(true, "help")),  LIST(new ModuleExecutable(true, "list")),  FILE(new ModuleExecutable(false, "file")),  SCHEMATIC(new ModuleExecutable(false, "schematic")),  PERMBANS(new ModuleExecutable(false, "permbans")),  PLAYERS(new ModuleExecutable(true, "players")),  LOGS(new ModuleExecutable(false, "logs"));
/*     */     
/*     */     private final ModuleExecutable moduleExecutable;
/*     */     
/*     */     private ModuleType(ModuleExecutable moduleExecutable)
/*     */     {
/* 150 */       this.moduleExecutable = moduleExecutable;
/*     */     }
/*     */     
/*     */     private static abstract class ModuleExecutable
/*     */     {
/*     */       private final boolean runOnBukkitThread;
/*     */       private final String name;
/*     */       
/*     */       public ModuleExecutable(boolean runOnBukkitThread, String name)
/*     */       {
/* 160 */         this.runOnBukkitThread = runOnBukkitThread;
/* 161 */         this.name = name;
/*     */       }
/*     */       
/*     */       public NanoHTTPD.Response execute(final NanoHTTPD.HTTPSession session)
/*     */       {
/*     */         try
/*     */         {
/* 168 */           if (runOnBukkitThread) {
/* 170 */             (NanoHTTPD.Response)Bukkit.getScheduler().callSyncMethod(TotalFreedomMod.plugin, new Callable()
/*     */             {
/*     */               public NanoHTTPD.Response call()
/*     */                 throws Exception
/*     */               {
/* 175 */                 return getResponse(session);
/*     */               }
/*     */             }).get();
/*     */           }
/* 181 */           return getResponse(session);
/*     */         }
/*     */         catch (Exception ex)
/*     */         {
/* 186 */           TFM_Log.severe(ex);
/*     */         }
/* 188 */         return null;
/*     */       }
/*     */       
/*     */       public abstract NanoHTTPD.Response getResponse(NanoHTTPD.HTTPSession paramHTTPSession);
/*     */       
/*     */       public String getName()
/*     */       {
/* 195 */         return name;
/*     */       }
/*     */     }
/*     */     
/*     */     public ModuleExecutable getModuleExecutable()
/*     */     {
/* 201 */       return moduleExecutable;
/*     */     }
/*     */     
/*     */     private static ModuleType getByName(String needle)
/*     */     {
/* 206 */       for (ModuleType type : ) {
/* 208 */         if (type.getModuleExecutable().getName().equalsIgnoreCase(needle)) {
/* 210 */           return type;
/*     */         }
/*     */       }
/* 213 */       return FILE;
/*     */     }
/*     */   }
/*     */   
/*     */   private static class TFM_HTTPD
/*     */     extends NanoHTTPD
/*     */   {
/*     */     public TFM_HTTPD(int port)
/*     */     {
/* 221 */       super();
/*     */     }
/*     */     
/*     */     public TFM_HTTPD(String hostname, int port)
/*     */     {
/* 226 */       super(port);
/*     */     }
/*     */     
/*     */     public NanoHTTPD.Response serve(NanoHTTPD.HTTPSession session)
/*     */     {
/*     */       NanoHTTPD.Response response;
/*     */       try
/*     */       {
/* 236 */         String[] args = StringUtils.split(session.getUri(), "/");
/* 237 */         TFM_HTTPD_Manager.ModuleType moduleType = args.length >= 1 ? TFM_HTTPD_Manager.ModuleType.access$000(args[0]) : TFM_HTTPD_Manager.ModuleType.FILE;
/* 238 */         response = moduleType.getModuleExecutable().execute(session);
/*     */       }
/*     */       catch (Exception ex)
/*     */       {
/* 242 */         response = new NanoHTTPD.Response(NanoHTTPD.Response.Status.INTERNAL_ERROR, "text/plain", "Error 500: Internal Server Error\r\n" + ex.getMessage() + "\r\n" + ExceptionUtils.getStackTrace(ex));
/*     */       }
/* 245 */       if (response == null) {
/* 247 */         response = new NanoHTTPD.Response(NanoHTTPD.Response.Status.NOT_FOUND, "text/plain", "Error 404: Not Found - The requested resource was not found on this server.");
/*     */       }
/* 250 */       return response;
/*     */     }
/*     */   }
/*     */   
/*     */   public static NanoHTTPD.Response serveFileBasic(File file)
/*     */   {
/* 256 */     NanoHTTPD.Response response = null;
/* 258 */     if ((file != null) && (file.exists())) {
/*     */       try
/*     */       {
/* 262 */         String mimetype = null;
/*     */         
/* 264 */         Matcher matcher = EXT_REGEX.matcher(file.getCanonicalPath());
/* 265 */         if (matcher.find()) {
/* 267 */           mimetype = (String)Module_file.MIME_TYPES.get(matcher.group(1));
/*     */         }
/* 270 */         if ((mimetype == null) || (mimetype.trim().isEmpty())) {
/* 272 */           mimetype = MIME_DEFAULT_BINARY;
/*     */         }
/* 275 */         response = new NanoHTTPD.Response(NanoHTTPD.Response.Status.OK, mimetype, new FileInputStream(file));
/* 276 */         response.addHeader("Content-Length", "" + file.length());
/*     */       }
/*     */       catch (IOException ex)
/*     */       {
/* 280 */         TFM_Log.severe(ex);
/*     */       }
/*     */     }
/* 284 */     return response;
/*     */   }
/*     */ }


/* Location:              C:\Users\SilentBloodGaming\Desktop\MasterFreedomMod.jar!\me\StevenLawson\TotalFreedomMod\HTTPD\TFM_HTTPD_Manager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */