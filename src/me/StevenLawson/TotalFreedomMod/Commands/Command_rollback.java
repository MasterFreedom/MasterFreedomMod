/*    */ package me.StevenLawson.TotalFreedomMod.Commands;
/*    */ 
/*    */ import me.StevenLawson.TotalFreedomMod.TFM_RollbackManager;
/*    */ import me.StevenLawson.TotalFreedomMod.TFM_Util;
/*    */ import org.bukkit.command.Command;
/*    */ import org.bukkit.command.CommandSender;
/*    */ import org.bukkit.entity.Player;
/*    */ 
/*    */ @CommandPermissions(level=AdminLevel.SUPER, source=SourceType.BOTH, blockHostConsole=true)
/*    */ @CommandParameters(description="Issues a rollback on a player", usage="/<command> <[partialname] | undo [partialname] purge [partialname] | purgeall>", aliases="rb")
/*    */ public class Command_rollback
/*    */   extends TFM_Command
/*    */ {
/*    */   public boolean run(CommandSender sender, Player sender_p, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
/*    */   {
/* 16 */     if ((args.length == 0) || (args.length > 2)) {
/* 18 */       return false;
/*    */     }
/* 21 */     if (args.length == 1)
/*    */     {
/* 23 */       if ("purgeall".equals(args[0]))
/*    */       {
/* 25 */         TFM_Util.adminAction(sender.getName(), "Purging all rollback history", false);
/* 26 */         playerMsg("Purged all rollback history for " + TFM_RollbackManager.purgeEntries() + " players.");
/*    */       }
/*    */       else
/*    */       {
/* 30 */         String playerName = TFM_RollbackManager.findPlayer(args[0]);
/* 32 */         if (playerName == null)
/*    */         {
/* 34 */           playerMsg("That player has no entries stored.");
/* 35 */           return true;
/*    */         }
/* 38 */         if (TFM_RollbackManager.canUndoRollback(playerName)) {
/* 40 */           playerMsg("That player has just been rolled back.");
/*    */         }
/* 43 */         TFM_Util.adminAction(sender.getName(), "Rolling back player: " + playerName, false);
/* 44 */         playerMsg("Rolled back " + TFM_RollbackManager.rollback(playerName) + " edits for " + playerName + ".");
/* 45 */         playerMsg("If this rollback was a mistake, use /rollback undo " + playerName + " within 40 seconds to reverse the rollback.");
/*    */       }
/* 47 */       return true;
/*    */     }
/* 50 */     if (args.length == 2)
/*    */     {
/* 52 */       if ("purge".equalsIgnoreCase(args[0]))
/*    */       {
/* 54 */         String playerName = TFM_RollbackManager.findPlayer(args[1]);
/* 56 */         if (playerName == null)
/*    */         {
/* 58 */           playerMsg("That player has no entries stored.");
/* 59 */           return true;
/*    */         }
/* 62 */         playerMsg("Purged " + TFM_RollbackManager.purgeEntries(playerName) + " rollback history entries for " + playerName + ".");
/* 63 */         return true;
/*    */       }
/* 66 */       if ("undo".equalsIgnoreCase(args[0]))
/*    */       {
/* 68 */         String playerName = TFM_RollbackManager.findPlayer(args[1]);
/* 70 */         if (playerName == null)
/*    */         {
/* 72 */           playerMsg("That player hasn't been rolled back recently.");
/* 73 */           return true;
/*    */         }
/* 76 */         TFM_Util.adminAction(sender.getName(), "Reverting rollback for player: " + playerName, false);
/* 77 */         playerMsg("Reverted " + TFM_RollbackManager.undoRollback(playerName) + " edits for " + playerName + ".");
/* 78 */         return true;
/*    */       }
/*    */     }
/* 82 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\SilentBloodGaming\Desktop\MasterFreedomMod.jar!\me\StevenLawson\TotalFreedomMod\Commands\Command_rollback.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */