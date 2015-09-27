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
/*    */ @CommandParameters(description="Halts a player", usage="/<command> <<partialname> | all | purge | list>")
/*    */ public class Command_halt
/*    */   extends TFM_Command
/*    */ {
/*    */   public boolean run(CommandSender sender, Player sender_p, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
/*    */   {
/* 17 */     if (args.length != 1) {
/* 19 */       return false;
/*    */     }
/* 22 */     if (args[0].equalsIgnoreCase("all"))
/*    */     {
/* 24 */       TFM_Util.adminAction(sender.getName(), "Halting all non-superadmins.", true);
/* 25 */       int counter = 0;
/* 26 */       for (Player player : server.getOnlinePlayers()) {
/* 28 */         if (!TFM_AdminList.isSuperAdmin(player))
/*    */         {
/* 30 */           TFM_PlayerData.getPlayerData(player).setHalted(true);
/* 31 */           counter++;
/*    */         }
/*    */       }
/* 34 */       playerMsg("Halted " + counter + " players.");
/* 35 */       return true;
/*    */     }
/* 38 */     if (args[0].equalsIgnoreCase("purge"))
/*    */     {
/* 40 */       TFM_Util.adminAction(sender.getName(), "Unhalting all players.", true);
/* 41 */       int counter = 0;
/* 42 */       for (Player player : server.getOnlinePlayers())
/*    */       {
/* 44 */         TFM_PlayerData playerdata = TFM_PlayerData.getPlayerData(player);
/* 45 */         if (TFM_PlayerData.getPlayerData(player).isHalted())
/*    */         {
/* 47 */           playerdata.setHalted(false);
/* 48 */           counter++;
/*    */         }
/*    */       }
/* 51 */       playerMsg("Unhalted " + counter + " players.");
/* 52 */       return true;
/*    */     }
/* 55 */     if (args[0].equalsIgnoreCase("list"))
/*    */     {
/* 58 */       int count = 0;
/* 59 */       for (Player hp : server.getOnlinePlayers())
/*    */       {
/* 61 */         TFM_PlayerData info = TFM_PlayerData.getPlayerData(hp);
/* 62 */         if (info.isHalted())
/*    */         {
/* 64 */           if (count == 0) {
/* 66 */             playerMsg("Halted players:");
/*    */           }
/* 68 */           playerMsg("- " + hp.getName());
/* 69 */           count++;
/*    */         }
/*    */       }
/* 72 */       if (count == 0) {
/* 74 */         playerMsg("There are currently no halted players.");
/*    */       }
/* 76 */       return true;
/*    */     }
/* 79 */     Player player = getPlayer(args[0]);
/* 81 */     if (player == null)
/*    */     {
/* 83 */       sender.sendMessage(TFM_Command.PLAYER_NOT_FOUND);
/* 84 */       return true;
/*    */     }
/* 87 */     TFM_PlayerData playerdata = TFM_PlayerData.getPlayerData(player);
/* 88 */     if (!playerdata.isHalted())
/*    */     {
/* 90 */       TFM_Util.adminAction(sender.getName(), "Halting " + player.getName(), true);
/* 91 */       playerdata.setHalted(true);
/* 92 */       return true;
/*    */     }
/* 96 */     TFM_Util.adminAction(sender.getName(), "Unhalting " + player.getName(), true);
/* 97 */     playerdata.setHalted(false);
/* 98 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\SilentBloodGaming\Desktop\MasterFreedomMod.jar!\me\StevenLawson\TotalFreedomMod\Commands\Command_halt.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */