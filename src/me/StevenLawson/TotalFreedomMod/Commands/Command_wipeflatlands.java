/*    */ package me.StevenLawson.TotalFreedomMod.Commands;
/*    */ 
/*    */ import me.StevenLawson.TotalFreedomMod.TFM_Util;
/*    */ import org.bukkit.ChatColor;
/*    */ import org.bukkit.Server;
/*    */ import org.bukkit.command.Command;
/*    */ import org.bukkit.command.CommandSender;
/*    */ import org.bukkit.entity.Player;
/*    */ 
/*    */ @CommandPermissions(level=AdminLevel.SENIOR, source=SourceType.ONLY_CONSOLE, blockHostConsole=true)
/*    */ @CommandParameters(description="Wipe the flatlands map. Requires manual restart after command is used.", usage="/<command>")
/*    */ public class Command_wipeflatlands
/*    */   extends TFM_Command
/*    */ {
/*    */   public boolean run(CommandSender sender, Player sender_p, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
/*    */   {
/* 16 */     TFM_Util.setSavedFlag("do_wipe_flatlands", true);
/*    */     
/* 18 */     TFM_Util.bcastMsg("Server is going offline for flatlands wipe.", ChatColor.GRAY);
/* 20 */     for (Player player : server.getOnlinePlayers()) {
/* 22 */       player.kickPlayer("Server is going offline for flatlands wipe, come back in a few minutes.");
/*    */     }
/* 25 */     server.shutdown();
/*    */     
/* 27 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\SilentBloodGaming\Desktop\MasterFreedomMod.jar!\me\StevenLawson\TotalFreedomMod\Commands\Command_wipeflatlands.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */