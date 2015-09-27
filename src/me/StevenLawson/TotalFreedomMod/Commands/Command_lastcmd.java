/*    */ package me.StevenLawson.TotalFreedomMod.Commands;
/*    */ 
/*    */ import me.StevenLawson.TotalFreedomMod.TFM_PlayerData;
/*    */ import org.bukkit.ChatColor;
/*    */ import org.bukkit.command.Command;
/*    */ import org.bukkit.command.CommandSender;
/*    */ import org.bukkit.entity.Player;
/*    */ 
/*    */ @CommandPermissions(level=AdminLevel.SUPER, source=SourceType.BOTH)
/*    */ @CommandParameters(description="Show the last command that someone used.", usage="/<command> <player>")
/*    */ public class Command_lastcmd
/*    */   extends TFM_Command
/*    */ {
/*    */   public boolean run(CommandSender sender, Player sender_p, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
/*    */   {
/* 16 */     if (args.length == 0) {
/* 18 */       return false;
/*    */     }
/* 21 */     Player player = getPlayer(args[0]);
/* 23 */     if (player == null)
/*    */     {
/* 25 */       playerMsg(TFM_Command.PLAYER_NOT_FOUND);
/* 26 */       return true;
/*    */     }
/* 29 */     TFM_PlayerData playerdata = TFM_PlayerData.getPlayerData(player);
/* 31 */     if (playerdata != null)
/*    */     {
/* 33 */       String lastCommand = playerdata.getLastCommand();
/* 34 */       if (lastCommand.isEmpty()) {
/* 36 */         lastCommand = "(none)";
/*    */       }
/* 38 */       playerMsg(player.getName() + " - Last Command: " + lastCommand, ChatColor.GRAY);
/*    */     }
/* 41 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\SilentBloodGaming\Desktop\MasterFreedomMod.jar!\me\StevenLawson\TotalFreedomMod\Commands\Command_lastcmd.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */