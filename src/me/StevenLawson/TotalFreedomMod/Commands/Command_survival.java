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
/*    */ @CommandParameters(description="Quickly change your own gamemode to survival, or define someone's username to change theirs.", usage="/<command> <[partialname] | -a>", aliases="gms")
/*    */ public class Command_survival
/*    */   extends TFM_Command
/*    */ {
/*    */   public boolean run(CommandSender sender, Player sender_p, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
/*    */   {
/* 17 */     if (senderIsConsole) {
/* 19 */       if (args.length == 0)
/*    */       {
/* 21 */         playerMsg("When used from the console, you must define a target user to change gamemode on.");
/* 22 */         return true;
/*    */       }
/*    */     }
/*    */     Player player;
/* 28 */     if (args.length == 0)
/*    */     {
/* 30 */       player = sender_p;
/*    */     }
/*    */     else
/*    */     {
/* 34 */       if (args[0].equalsIgnoreCase("-a"))
/*    */       {
/* 36 */         if ((!TFM_AdminList.isSuperAdmin(sender)) || (senderIsConsole))
/*    */         {
/* 38 */           sender.sendMessage(TFM_Command.MSG_NO_PERMS);
/* 39 */           return true;
/*    */         }
/* 42 */         for (Player targetPlayer : server.getOnlinePlayers()) {
/* 44 */           targetPlayer.setGameMode(GameMode.SURVIVAL);
/*    */         }
/* 47 */         TFM_Util.adminAction(sender.getName(), "Changing everyone's gamemode to survival", false);
/* 48 */         return true;
/*    */       }
/* 51 */       if ((senderIsConsole) || (TFM_AdminList.isSuperAdmin(sender)))
/*    */       {
/* 53 */         Player player = getPlayer(args[0]);
/* 55 */         if (player == null)
/*    */         {
/* 57 */           playerMsg(TFM_Command.PLAYER_NOT_FOUND);
/* 58 */           return true;
/*    */         }
/*    */       }
/*    */       else
/*    */       {
/* 63 */         playerMsg("Only superadmins can change other user's gamemode.");
/* 64 */         return true;
/*    */       }
/*    */     }
/*    */     Player player;
/* 68 */     playerMsg("Setting " + player.getName() + " to game mode 'Survival'.");
/* 69 */     player.sendMessage(sender.getName() + " set your game mode to 'Survival'.");
/* 70 */     player.setGameMode(GameMode.SURVIVAL);
/*    */     
/* 72 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\SilentBloodGaming\Desktop\MasterFreedomMod.jar!\me\StevenLawson\TotalFreedomMod\Commands\Command_survival.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */