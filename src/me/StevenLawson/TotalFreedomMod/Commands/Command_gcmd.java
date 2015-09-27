/*    */ package me.StevenLawson.TotalFreedomMod.Commands;
/*    */ 
/*    */ import me.StevenLawson.TotalFreedomMod.TFM_CommandBlocker;
/*    */ import org.apache.commons.lang3.StringUtils;
/*    */ import org.bukkit.Server;
/*    */ import org.bukkit.command.Command;
/*    */ import org.bukkit.command.CommandSender;
/*    */ import org.bukkit.entity.Player;
/*    */ 
/*    */ @CommandPermissions(level=AdminLevel.SUPER, source=SourceType.BOTH)
/*    */ @CommandParameters(description="Send a command as someone else.", usage="/<command> <fromname> <outcommand>")
/*    */ public class Command_gcmd
/*    */   extends TFM_Command
/*    */ {
/*    */   public boolean run(CommandSender sender, Player sender_p, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
/*    */   {
/* 16 */     if (args.length < 2) {
/* 18 */       return false;
/*    */     }
/* 21 */     Player player = getPlayer(args[0]);
/* 23 */     if (player == null)
/*    */     {
/* 25 */       sender.sendMessage(TFM_Command.PLAYER_NOT_FOUND);
/* 26 */       return true;
/*    */     }
/* 29 */     String outCommand = StringUtils.join(args, " ", 1, args.length);
/* 31 */     if (TFM_CommandBlocker.isCommandBlocked(outCommand, sender)) {
/* 33 */       return true;
/*    */     }
/*    */     try
/*    */     {
/* 38 */       playerMsg("Sending command as " + player.getName() + ": " + outCommand);
/* 39 */       if (server.dispatchCommand(player, outCommand)) {
/* 41 */         playerMsg("Command sent.");
/*    */       } else {
/* 45 */         playerMsg("Unknown error sending command.");
/*    */       }
/*    */     }
/*    */     catch (Throwable ex)
/*    */     {
/* 50 */       playerMsg("Error sending command: " + ex.getMessage());
/*    */     }
/* 53 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\SilentBloodGaming\Desktop\MasterFreedomMod.jar!\me\StevenLawson\TotalFreedomMod\Commands\Command_gcmd.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */