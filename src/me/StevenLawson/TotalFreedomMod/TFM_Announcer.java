/*     */ package me.StevenLawson.TotalFreedomMod;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import me.StevenLawson.TotalFreedomMod.Config.TFM_ConfigEntry;
/*     */ import org.bukkit.scheduler.BukkitRunnable;
/*     */ 
/*     */ public class TFM_Announcer
/*     */ {
/*  11 */   private static final List<String> ANNOUNCEMENTS = new ArrayList();
/*     */   private static boolean enabled;
/*     */   private static long interval;
/*     */   private static String prefix;
/*     */   private static BukkitRunnable announcer;
/*     */   
/*     */   private TFM_Announcer()
/*     */   {
/*  19 */     throw new AssertionError();
/*     */   }
/*     */   
/*     */   public static boolean isEnabled()
/*     */   {
/*  24 */     return enabled;
/*     */   }
/*     */   
/*     */   public static List<String> getAnnouncements()
/*     */   {
/*  29 */     return Collections.unmodifiableList(ANNOUNCEMENTS);
/*     */   }
/*     */   
/*     */   public static long getTickInterval()
/*     */   {
/*  34 */     return interval;
/*     */   }
/*     */   
/*     */   public static String getPrefix()
/*     */   {
/*  39 */     return prefix;
/*     */   }
/*     */   
/*     */   public static void load()
/*     */   {
/*  44 */     stop();
/*     */     
/*  46 */     ANNOUNCEMENTS.clear();
/*  48 */     for (Object announcement : TFM_ConfigEntry.ANNOUNCER_ANNOUNCEMENTS.getList()) {
/*  50 */       ANNOUNCEMENTS.add(TFM_Util.colorize((String)announcement));
/*     */     }
/*  53 */     enabled = TFM_ConfigEntry.ANNOUNCER_ENABLED.getBoolean().booleanValue();
/*  54 */     interval = TFM_ConfigEntry.ANNOUNCER_INTERVAL.getInteger().intValue() * 20L;
/*  55 */     prefix = TFM_Util.colorize(TFM_ConfigEntry.ANNOUNCER_PREFIX.getString());
/*  57 */     if (enabled) {
/*  59 */       start();
/*     */     }
/*     */   }
/*     */   
/*     */   public static boolean isStarted()
/*     */   {
/*  65 */     return announcer != null;
/*     */   }
/*     */   
/*     */   public static void start()
/*     */   {
/*  70 */     if (isStarted()) {
/*  72 */       return;
/*     */     }
/*  75 */     announcer = new BukkitRunnable()
/*     */     {
/*  77 */       private int current = 0;
/*     */       
/*     */       public void run()
/*     */       {
/*  82 */         current += 1;
/*  84 */         if (current >= TFM_Announcer.ANNOUNCEMENTS.size()) {
/*  86 */           current = 0;
/*     */         }
/*  89 */         TFM_Util.bcastMsg(TFM_Announcer.prefix + (String)TFM_Announcer.ANNOUNCEMENTS.get(current));
/*     */       }
/*  92 */     };
/*  93 */     announcer.runTaskTimer(TotalFreedomMod.plugin, interval, interval);
/*     */   }
/*     */   
/*     */   public static void stop()
/*     */   {
/*  98 */     if (announcer == null) {
/* 100 */       return;
/*     */     }
/*     */     try
/*     */     {
/* 105 */       announcer.cancel();
/*     */     }
/*     */     finally
/*     */     {
/* 109 */       announcer = null;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\SilentBloodGaming\Desktop\MasterFreedomMod.jar!\me\StevenLawson\TotalFreedomMod\TFM_Announcer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */