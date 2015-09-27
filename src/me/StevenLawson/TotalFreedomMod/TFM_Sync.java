/*    */ package me.StevenLawson.TotalFreedomMod;
/*    */ 
/*    */ import org.bukkit.ChatColor;
/*    */ import org.bukkit.command.CommandSender;
/*    */ import org.bukkit.entity.Player;
/*    */ import org.bukkit.scheduler.BukkitRunnable;
/*    */ 
/*    */ public class TFM_Sync
/*    */ {
/*    */   public static void playerMsg(Player player, final String message)
/*    */   {
/* 13 */     new BukkitRunnable()
/*    */     {
/*    */       public void run()
/*    */       {
/* 19 */         TFM_Util.playerMsg(val$player, message);
/*    */       }
/* 19 */     }.runTask(TotalFreedomMod.plugin);
/*    */   }
/*    */   
/*    */   public static void playerKick(Player player, final String reason)
/*    */   {
/* 27 */     new BukkitRunnable()
/*    */     {
/*    */       public void run()
/*    */       {
/* 33 */         val$player.kickPlayer(reason);
/*    */       }
/* 33 */     }.runTask(TotalFreedomMod.plugin);
/*    */   }
/*    */   
/*    */   public static void adminChatMessage(CommandSender sender, final String message, final boolean isRed)
/*    */   {
/* 41 */     new BukkitRunnable()
/*    */     {
/*    */       public void run()
/*    */       {
/* 47 */         TFM_Util.adminChatMessage(val$sender, message, isRed);
/*    */       }
/* 47 */     }.runTask(TotalFreedomMod.plugin);
/*    */   }
/*    */   
/*    */   public static void autoEject(Player player, final String kickMessage)
/*    */   {
/* 55 */     new BukkitRunnable()
/*    */     {
/*    */       public void run()
/*    */       {
/* 61 */         TFM_Util.autoEject(val$player, kickMessage);
/*    */       }
/* 61 */     }.runTask(TotalFreedomMod.plugin);
/*    */   }
/*    */   
/*    */   public static void bcastMsg(String message, final ChatColor color)
/*    */   {
/* 69 */     new BukkitRunnable()
/*    */     {
/*    */       public void run()
/*    */       {
/* 75 */         TFM_Util.bcastMsg(val$message, color);
/*    */       }
/* 75 */     }.runTask(TotalFreedomMod.plugin);
/*    */   }
/*    */ }


/* Location:              C:\Users\SilentBloodGaming\Desktop\MasterFreedomMod.jar!\me\StevenLawson\TotalFreedomMod\TFM_Sync.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */