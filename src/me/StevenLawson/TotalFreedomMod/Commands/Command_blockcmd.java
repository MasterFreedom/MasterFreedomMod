/*    */ package me.StevenLawson.TotalFreedomMod.Commands;
/*    */ 
/*    */ import me.StevenLawson.TotalFreedomMod.TFM_AdminList;
/*    */ import me.StevenLawson.TotalFreedomMod.TFM_PlayerData;
/*    */ import me.StevenLawson.TotalFreedomMod.TFM_Util;
/*    */ import org.bukkit.Server;
/*    */ import org.bukkit.command.Command;
/*    */ import org.bukkit.command.CommandSender;
/*    */ import org.bukkit.entity.Player;
/*    */ 
/*    */ @CommandPermissions(level=AdminLevel.SUPER, source=SourceType.BOTH)
/*    */ @CommandParameters(description="Block all commands for a specific player.", usage="/<command> <purge | <partialname>>", aliases="blockcommands,blockcommand")
/*    */ public class Command_blockcmd
/*    */   extends TFM_Command
/*    */ {
/*    */   public boolean run(CommandSender sender, Player sender_p, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
/*    */   {
/* 17 */     if (args.length != 1) {
/* 19 */       return false;
/*    */     }
/* 22 */     if (args[0].equalsIgnoreCase("purge"))
/*    */     {
/* 24 */       TFM_Util.adminAction(sender.getName(), "Unblocking commands for all players", true);
/* 25 */       int counter = 0;
/* 26 */       for (Player player : server.getOnlinePlayers())
/*    */       {
/* 28 */         TFM_PlayerData playerdata = TFM_PlayerData.getPlayerData(player);
/* 29 */         if (playerdata.allCommandsBlocked())
/*    */         {
/* 31 */           counter++;
/* 32 */           playerdata.setCommandsBlocked(false);
/*    */         }
/*    */       }
/* 35 */       playerMsg("Unblocked commands for " + counter + " players.");
/* 36 */       return true;
/*    */     }
/* 39 */     Player player = getPlayer(args[0]);
/* 41 */     if (player == null)
/*    */     {
/* 43 */       playerMsg(TFM_Command.PLAYER_NOT_FOUND);
/* 44 */       return true;
/*    */     }
/* 47 */     if (TFM_AdminList.isSuperAdmin(player))
/*    */     {
/* 49 */       playerMsg(player.getName() + " is a Superadmin, and cannot have their commands blocked.");
/* 50 */       return true;
/*    */     }
/* 53 */     TFM_PlayerData playerdata = TFM_PlayerData.getPlayerData(player);
/*    */     
/* 55 */     playerdata.setCommandsBlocked(!playerdata.allCommandsBlocked());
/*    */     
/* 57 */     TFM_Util.adminAction(sender.getName(), (playerdata.allCommandsBlocked() ? "B" : "Unb") + "locking all commands for " + player.getName(), true);
/* 58 */     playerMsg((playerdata.allCommandsBlocked() ? "B" : "Unb") + "locked all commands.");
/*    */     
/* 60 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\SilentBloodGaming\Desktop\MasterFreedomMod.jar!\me\StevenLawson\TotalFreedomMod\Commands\Command_blockcmd.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */