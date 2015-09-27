/*    */ package me.StevenLawson.TotalFreedomMod;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.Collections;
/*    */ import java.util.List;
/*    */ import me.StevenLawson.TotalFreedomMod.Config.TFM_Config;
/*    */ 
/*    */ public class TFM_PermbanList
/*    */ {
/* 16 */   private static final List<String> PERMBANNED_PLAYERS = new ArrayList();
/* 17 */   private static final List<String> PERMBANNED_IPS = new ArrayList();
/*    */   
/*    */   private TFM_PermbanList()
/*    */   {
/* 22 */     throw new AssertionError();
/*    */   }
/*    */   
/*    */   public static List<String> getPermbannedPlayers()
/*    */   {
/* 27 */     return Collections.unmodifiableList(PERMBANNED_PLAYERS);
/*    */   }
/*    */   
/*    */   public static List<String> getPermbannedIps()
/*    */   {
/* 32 */     return Collections.unmodifiableList(PERMBANNED_IPS);
/*    */   }
/*    */   
/*    */   public static void load()
/*    */   {
/* 37 */     PERMBANNED_PLAYERS.clear();
/* 38 */     PERMBANNED_IPS.clear();
/*    */     
/* 40 */     TFM_Config config = new TFM_Config(TotalFreedomMod.plugin, "permban.yml", true);
/* 41 */     config.load();
/* 43 */     for (String playername : config.getKeys(false))
/*    */     {
/* 45 */       PERMBANNED_PLAYERS.add(playername.toLowerCase().trim());
/*    */       
/* 47 */       List<String> playerIps = config.getStringList(playername);
/* 48 */       for (String ip : playerIps)
/*    */       {
/* 50 */         ip = ip.trim();
/* 51 */         if (!PERMBANNED_IPS.contains(ip)) {
/* 53 */           PERMBANNED_IPS.add(ip);
/*    */         }
/*    */       }
/*    */     }
/* 58 */     TFM_Log.info("Loaded " + PERMBANNED_PLAYERS.size() + " permanently banned players and " + PERMBANNED_IPS.size() + " permanently banned IPs.");
/*    */   }
/*    */ }


/* Location:              C:\Users\SilentBloodGaming\Desktop\MasterFreedomMod.jar!\me\StevenLawson\TotalFreedomMod\TFM_PermbanList.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */