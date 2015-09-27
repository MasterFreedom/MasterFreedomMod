/*    */ package me.StevenLawson.TotalFreedomMod.Commands;
/*    */ 
/*    */ import me.StevenLawson.TotalFreedomMod.TFM_AdminList;
/*    */ import me.StevenLawson.TotalFreedomMod.TFM_DepreciationAggregator;
/*    */ import me.StevenLawson.TotalFreedomMod.TFM_Util;
/*    */ import org.bukkit.ChatColor;
/*    */ import org.bukkit.OfflinePlayer;
/*    */ import org.bukkit.Server;
/*    */ import org.bukkit.command.Command;
/*    */ import org.bukkit.command.CommandSender;
/*    */ import org.bukkit.entity.Player;
/*    */ 
/*    */ @CommandPermissions(level=AdminLevel.OP, source=SourceType.BOTH)
/*    */ @CommandParameters(description="Makes a player operator", usage="/<command> <playername>")
/*    */ public class Command_op
/*    */   extends TFM_Command
/*    */ {
/*    */   public boolean run(CommandSender sender, Player sender_p, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
/*    */   {
/* 19 */     if (args.length != 1) {
/* 21 */       return false;
/*    */     }
/* 24 */     if ((args[0].equalsIgnoreCase("all")) || (args[0].equalsIgnoreCase("everyone")))
/*    */     {
/* 26 */       playerMsg("Correct usage: /opall");
/* 27 */       return true;
/*    */     }
/* 30 */     OfflinePlayer player = null;
/* 31 */     for (Player onlinePlayer : server.getOnlinePlayers()) {
/* 33 */       if (args[0].equalsIgnoreCase(onlinePlayer.getName())) {
/* 35 */         player = onlinePlayer;
/*    */       }
/*    */     }
/* 40 */     if (player == null) {
/* 42 */       if ((TFM_AdminList.isSuperAdmin(sender)) || (senderIsConsole))
/*    */       {
/* 44 */         player = TFM_DepreciationAggregator.getOfflinePlayer(server, args[0]);
/*    */       }
/*    */       else
/*    */       {
/* 48 */         playerMsg("That player is not online.");
/* 49 */         playerMsg("You don't have permissions to OP offline players.", ChatColor.YELLOW);
/* 50 */         return true;
/*    */       }
/*    */     }
/* 54 */     TFM_Util.adminAction(sender.getName(), "Opping " + player.getName(), false);
/* 55 */     player.setOp(true);
/*    */     
/* 57 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\SilentBloodGaming\Desktop\MasterFreedomMod.jar!\me\StevenLawson\TotalFreedomMod\Commands\Command_op.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */