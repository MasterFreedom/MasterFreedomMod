/*    */ package me.StevenLawson.TotalFreedomMod.Commands;
/*    */ 
/*    */ import java.io.File;
/*    */ import me.StevenLawson.TotalFreedomMod.TFM_Log;
/*    */ import me.StevenLawson.TotalFreedomMod.TFM_Util;
/*    */ import org.bukkit.command.Command;
/*    */ import org.bukkit.command.CommandSender;
/*    */ import org.bukkit.entity.Player;
/*    */ import org.bukkit.scheduler.BukkitRunnable;
/*    */ 
/*    */ @CommandPermissions(level=AdminLevel.SENIOR, source=SourceType.ONLY_CONSOLE, blockHostConsole=true)
/*    */ @CommandParameters(description="Update server files.", usage="/<command>")
/*    */ public class Command_tfupdate
/*    */   extends TFM_Command
/*    */ {
/* 15 */   public static final String[] FILES = new String[0];
/*    */   
/*    */   public boolean run(CommandSender sender, Player sender_p, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
/*    */   {
/* 22 */     if (FILES.length == 0)
/*    */     {
/* 24 */       playerMsg("This command is disabled.");
/* 25 */       return true;
/*    */     }
/* 28 */     if (!sender.getName().equalsIgnoreCase("madgeek1450"))
/*    */     {
/* 30 */       playerMsg(TFM_Command.MSG_NO_PERMS);
/* 31 */       return true;
/*    */     }
/* 34 */     for (final String url : FILES) {
/* 36 */       new BukkitRunnable()
/*    */       {
/*    */         public void run()
/*    */         {
/*    */           try
/*    */           {
/* 43 */             TFM_Log.info("Downloading: " + url);
/*    */             
/* 45 */             File file = new File("./updates/" + url.substring(url.lastIndexOf("/") + 1));
/* 46 */             if (file.exists()) {
/* 48 */               file.delete();
/*    */             }
/* 50 */             if (!file.getParentFile().exists()) {
/* 52 */               file.getParentFile().mkdirs();
/*    */             }
/* 55 */             TFM_Util.downloadFile(url, file, true);
/*    */           }
/*    */           catch (Exception ex)
/*    */           {
/* 59 */             TFM_Log.severe(ex);
/*    */           }
/*    */         }
/* 59 */       }.runTaskAsynchronously(plugin);
/*    */     }
/* 65 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\SilentBloodGaming\Desktop\MasterFreedomMod.jar!\me\StevenLawson\TotalFreedomMod\Commands\Command_tfupdate.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */