/*    */ package me.StevenLawson.TotalFreedomMod.Commands;
/*    */ 
/*    */ import me.StevenLawson.TotalFreedomMod.TFM_Util;
/*    */ import org.bukkit.ChatColor;
/*    */ import org.bukkit.Server;
/*    */ import org.bukkit.command.Command;
/*    */ import org.bukkit.command.CommandSender;
/*    */ import org.bukkit.entity.Player;
/*    */ 
/*    */ @CommandPermissions(level=AdminLevel.SUPER, source=SourceType.BOTH)
/*    */ @CommandParameters(description="Kicks everyone and stops the server.", usage="/<command>")
/*    */ public class Command_stop
/*    */   extends TFM_Command
/*    */ {
/*    */   public boolean run(CommandSender sender, Player sender_p, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
/*    */   {
/* 16 */     TFM_Util.bcastMsg("Server is going offline!", ChatColor.LIGHT_PURPLE);
/* 18 */     for (Player player : server.getOnlinePlayers()) {
/* 20 */       player.kickPlayer("Server is going offline, come back in about 20 seconds.");
/*    */     }
/* 23 */     server.shutdown();
/*    */     
/* 25 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\SilentBloodGaming\Desktop\MasterFreedomMod.jar!\me\StevenLawson\TotalFreedomMod\Commands\Command_stop.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */