/*     */ package me.StevenLawson.TotalFreedomMod.Bridge;
/*     */ 
/*     */ import com.earth2me.essentials.Essentials;
/*     */ import com.earth2me.essentials.User;
/*     */ import com.earth2me.essentials.UserMap;
/*     */ import me.StevenLawson.TotalFreedomMod.TFM_Log;
/*     */ import me.StevenLawson.TotalFreedomMod.TFM_Util;
/*     */ import org.bukkit.Bukkit;
/*     */ import org.bukkit.Server;
/*     */ import org.bukkit.plugin.Plugin;
/*     */ import org.bukkit.plugin.PluginManager;
/*     */ 
/*     */ public class TFM_EssentialsBridge
/*     */ {
/*  12 */   private static Essentials essentialsPlugin = null;
/*     */   
/*     */   private TFM_EssentialsBridge()
/*     */   {
/*  16 */     throw new AssertionError();
/*     */   }
/*     */   
/*     */   public static Essentials getEssentialsPlugin()
/*     */   {
/*  21 */     if (essentialsPlugin == null) {
/*     */       try
/*     */       {
/*  25 */         Plugin essentials = Bukkit.getServer().getPluginManager().getPlugin("Essentials");
/*  26 */         if (essentials != null) {
/*  28 */           if ((essentials instanceof Essentials)) {
/*  30 */             essentialsPlugin = (Essentials)essentials;
/*     */           }
/*     */         }
/*     */       }
/*     */       catch (Exception ex)
/*     */       {
/*  36 */         TFM_Log.severe(ex);
/*     */       }
/*     */     }
/*  39 */     return essentialsPlugin;
/*     */   }
/*     */   
/*     */   public static User getEssentialsUser(String username)
/*     */   {
/*     */     try
/*     */     {
/*  46 */       Essentials essentials = getEssentialsPlugin();
/*  47 */       if (essentials != null) {
/*  49 */         return essentials.getUserMap().getUser(username);
/*     */       }
/*     */     }
/*     */     catch (Exception ex)
/*     */     {
/*  54 */       TFM_Log.severe(ex);
/*     */     }
/*  56 */     return null;
/*     */   }
/*     */   
/*     */   public static void setNickname(String username, String nickname)
/*     */   {
/*     */     try
/*     */     {
/*  63 */       User user = getEssentialsUser(username);
/*  64 */       if (user != null)
/*     */       {
/*  66 */         user.setNickname(nickname);
/*  67 */         user.setDisplayNick();
/*     */       }
/*     */     }
/*     */     catch (Exception ex)
/*     */     {
/*  72 */       TFM_Log.severe(ex);
/*     */     }
/*     */   }
/*     */   
/*     */   public static String getNickname(String username)
/*     */   {
/*     */     try
/*     */     {
/*  80 */       User user = getEssentialsUser(username);
/*  81 */       if (user != null) {
/*  83 */         return user.getNickname();
/*     */       }
/*     */     }
/*     */     catch (Exception ex)
/*     */     {
/*  88 */       TFM_Log.severe(ex);
/*     */     }
/*  90 */     return null;
/*     */   }
/*     */   
/*     */   public static long getLastActivity(String username)
/*     */   {
/*     */     try
/*     */     {
/*  97 */       User user = getEssentialsUser(username);
/*  98 */       if (user != null) {
/* 100 */         return ((Long)TFM_Util.getField(user, "lastActivity")).longValue();
/*     */       }
/*     */     }
/*     */     catch (Exception ex)
/*     */     {
/* 105 */       TFM_Log.severe(ex);
/*     */     }
/* 107 */     return 0L;
/*     */   }
/*     */   
/*     */   public static boolean isEssentialsEnabled()
/*     */   {
/*     */     try
/*     */     {
/* 114 */       Essentials essentials = getEssentialsPlugin();
/* 115 */       if (essentials != null) {
/* 117 */         return essentials.isEnabled();
/*     */       }
/*     */     }
/*     */     catch (Exception ex)
/*     */     {
/* 122 */       TFM_Log.severe(ex);
/*     */     }
/* 124 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\SilentBloodGaming\Desktop\MasterFreedomMod.jar!\me\StevenLawson\TotalFreedomMod\Bridge\TFM_EssentialsBridge.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */