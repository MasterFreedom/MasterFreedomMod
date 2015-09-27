/*    */ package me.StevenLawson.TotalFreedomMod.Commands;
/*    */ 
/*    */ import me.StevenLawson.TotalFreedomMod.TFM_DepreciationAggregator;
/*    */ import me.StevenLawson.TotalFreedomMod.TFM_Util;
/*    */ import org.bukkit.OfflinePlayer;
/*    */ import org.bukkit.Server;
/*    */ import org.bukkit.command.Command;
/*    */ import org.bukkit.command.CommandSender;
/*    */ import org.bukkit.entity.Player;
/*    */ 
/*    */ @CommandPermissions(level=AdminLevel.SUPER, source=SourceType.BOTH)
/*    */ @CommandParameters(description="Deop a player.", usage="/<command> <playername>")
/*    */ public class Command_deop
/*    */   extends TFM_Command
/*    */ {
/*    */   public boolean run(CommandSender sender, Player sender_p, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
/*    */   {
/* 17 */     if (args.length != 1) {
/* 19 */       return false;
/*    */     }
/* 22 */     OfflinePlayer player = null;
/* 24 */     for (Player onlinePlayer : server.getOnlinePlayers()) {
/* 26 */       if (args[0].equalsIgnoreCase(onlinePlayer.getName())) {
/* 28 */         player = onlinePlayer;
/*    */       }
/*    */     }
/* 33 */     if (player == null) {
/* 35 */       player = TFM_DepreciationAggregator.getOfflinePlayer(server, args[0]);
/*    */     }
/* 38 */     TFM_Util.adminAction(sender.getName(), "De-opping " + player.getName(), false);
/*    */     
/* 40 */     player.setOp(false);
/*    */     
/* 42 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\SilentBloodGaming\Desktop\MasterFreedomMod.jar!\me\StevenLawson\TotalFreedomMod\Commands\Command_deop.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */