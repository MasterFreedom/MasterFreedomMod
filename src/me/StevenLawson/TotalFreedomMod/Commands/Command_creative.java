/*    */ package me.StevenLawson.TotalFreedomMod.Commands;
/*    */ 
/*    */ import me.StevenLawson.TotalFreedomMod.TFM_AdminList;
/*    */ import me.StevenLawson.TotalFreedomMod.TFM_Util;
/*    */ import org.bukkit.GameMode;
/*    */ import org.bukkit.Server;
/*    */ import org.bukkit.command.Command;
/*    */ import org.bukkit.command.CommandSender;
/*    */ import org.bukkit.entity.Player;
/*    */ 
/*    */ @CommandPermissions(level=AdminLevel.OP, source=SourceType.BOTH)
/*    */ @CommandParameters(description="Quickly change your own gamemode to creative, or define someone's username to change theirs.", usage="/<command> [partialname]", aliases="gmc")
/*    */ public class Command_creative
/*    */   extends TFM_Command
/*    */ {
/*    */   public boolean run(CommandSender sender, Player sender_p, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
/*    */   {
/* 17 */     if (senderIsConsole) {
/* 19 */       if (args.length == 0)
/*    */       {
/* 21 */         sender.sendMessage("When used from the console, you must define a target user to change gamemode on.");
/* 22 */         return true;
/*    */       }
/*    */     }
/*    */     Player player;
/*    */     Player player;
/* 27 */     if (args.length == 0)
/*    */     {
/* 29 */       player = sender_p;
/*    */     }
/*    */     else
/*    */     {
/* 33 */       if (args[0].equalsIgnoreCase("-a"))
/*    */       {
/* 35 */         if (!TFM_AdminList.isSuperAdmin(sender))
/*    */         {
/* 37 */           sender.sendMessage(TFM_Command.MSG_NO_PERMS);
/* 38 */           return true;
/*    */         }
/* 41 */         for (Player targetPlayer : server.getOnlinePlayers()) {
/* 43 */           targetPlayer.setGameMode(GameMode.CREATIVE);
/*    */         }
/* 46 */         TFM_Util.adminAction(sender.getName(), "Changing everyone's gamemode to creative", false);
/* 47 */         return true;
/*    */       }
/* 50 */       if ((!senderIsConsole) && (!TFM_AdminList.isSuperAdmin(sender)))
/*    */       {
/* 52 */         playerMsg("Only superadmins can change other user's gamemode.");
/* 53 */         return true;
/*    */       }
/* 56 */       player = getPlayer(args[0]);
/* 58 */       if (player == null)
/*    */       {
/* 60 */         sender.sendMessage(TFM_Command.PLAYER_NOT_FOUND);
/* 61 */         return true;
/*    */       }
/*    */     }
/* 66 */     playerMsg("Setting " + player.getName() + " to game mode 'Creative'.");
/* 67 */     playerMsg(player, sender.getName() + " set your game mode to 'Creative'.");
/* 68 */     player.setGameMode(GameMode.CREATIVE);
/*    */     
/* 70 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\SilentBloodGaming\Desktop\MasterFreedomMod.jar!\me\StevenLawson\TotalFreedomMod\Commands\Command_creative.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */