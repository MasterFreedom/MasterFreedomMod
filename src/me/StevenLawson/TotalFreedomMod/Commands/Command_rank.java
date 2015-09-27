/*    */ package me.StevenLawson.TotalFreedomMod.Commands;
/*    */ 
/*    */ import me.StevenLawson.TotalFreedomMod.TFM_PlayerRank;
/*    */ import org.bukkit.ChatColor;
/*    */ import org.bukkit.Server;
/*    */ import org.bukkit.command.Command;
/*    */ import org.bukkit.command.CommandSender;
/*    */ import org.bukkit.entity.Player;
/*    */ 
/*    */ @CommandPermissions(level=AdminLevel.ALL, source=SourceType.BOTH)
/*    */ @CommandParameters(description="Shows your rank.", usage="/<command>")
/*    */ public class Command_rank
/*    */   extends TFM_Command
/*    */ {
/*    */   public boolean run(CommandSender sender, Player sender_p, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
/*    */   {
/* 16 */     if ((senderIsConsole) && (args.length < 1))
/*    */     {
/* 18 */       for (Player player : server.getOnlinePlayers()) {
/* 20 */         playerMsg(player.getName() + " is " + TFM_PlayerRank.fromSender(player).getLoginMessage());
/*    */       }
/* 22 */       return true;
/*    */     }
/* 25 */     if (args.length > 1) {
/* 27 */       return false;
/*    */     }
/* 30 */     if (args.length == 0)
/*    */     {
/* 32 */       playerMsg(sender.getName() + " is " + TFM_PlayerRank.fromSender(sender).getLoginMessage(), ChatColor.AQUA);
/* 33 */       return true;
/*    */     }
/* 36 */     Player player = getPlayer(args[0]);
/* 38 */     if (player == null)
/*    */     {
/* 40 */       sender.sendMessage(TFM_Command.PLAYER_NOT_FOUND);
/* 41 */       return true;
/*    */     }
/* 44 */     playerMsg(player.getName() + " is " + TFM_PlayerRank.fromSender(player).getLoginMessage(), ChatColor.AQUA);
/*    */     
/* 46 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\SilentBloodGaming\Desktop\MasterFreedomMod.jar!\me\StevenLawson\TotalFreedomMod\Commands\Command_rank.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */