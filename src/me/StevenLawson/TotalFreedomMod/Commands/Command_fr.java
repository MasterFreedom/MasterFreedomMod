/*    */ package me.StevenLawson.TotalFreedomMod.Commands;
/*    */ 
/*    */ import java.util.Map;
/*    */ import me.StevenLawson.TotalFreedomMod.TFM_AdminList;
/*    */ import me.StevenLawson.TotalFreedomMod.TFM_PlayerData;
/*    */ import me.StevenLawson.TotalFreedomMod.TFM_Util;
/*    */ import org.bukkit.Bukkit;
/*    */ import org.bukkit.ChatColor;
/*    */ import org.bukkit.command.Command;
/*    */ import org.bukkit.command.CommandSender;
/*    */ import org.bukkit.entity.Player;
/*    */ 
/*    */ @CommandPermissions(level=AdminLevel.SUPER, source=SourceType.BOTH)
/*    */ @CommandParameters(description="Freeze players (toggles on and off).", usage="/<command> [target | purge]")
/*    */ public class Command_fr
/*    */   extends TFM_Command
/*    */ {
/* 16 */   private static boolean allFrozen = false;
/*    */   
/*    */   public boolean run(CommandSender sender, Player sender_p, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
/*    */   {
/* 21 */     if (args.length == 0)
/*    */     {
/* 23 */       allFrozen = !allFrozen;
/* 25 */       if (allFrozen)
/*    */       {
/* 27 */         TFM_Util.adminAction(sender.getName(), "Freezing all players", false);
/*    */         
/* 29 */         setAllFrozen(true);
/* 30 */         playerMsg("Players are now frozen.");
/* 32 */         for (Player player : Bukkit.getOnlinePlayers()) {
/* 34 */           if (!TFM_AdminList.isSuperAdmin(player)) {
/* 36 */             playerMsg(player, "You have been frozen due to rulebreakers, you will be unfrozen soon.", ChatColor.RED);
/*    */           }
/*    */         }
/*    */       }
/*    */       else
/*    */       {
/* 42 */         TFM_Util.adminAction(sender.getName(), "Unfreezing all players", false);
/* 43 */         setAllFrozen(false);
/* 44 */         playerMsg("Players are now free to move.");
/*    */       }
/*    */     }
/* 49 */     else if (args[0].toLowerCase().equals("purge"))
/*    */     {
/* 51 */       setAllFrozen(false);
/* 52 */       TFM_Util.adminAction(sender.getName(), "Unfreezing all players", false);
/*    */     }
/*    */     else
/*    */     {
/* 56 */       Player player = getPlayer(args[0]);
/* 58 */       if (player == null)
/*    */       {
/* 60 */         playerMsg(TFM_Command.PLAYER_NOT_FOUND, ChatColor.RED);
/* 61 */         return true;
/*    */       }
/* 64 */       TFM_PlayerData playerdata = TFM_PlayerData.getPlayerData(player);
/* 65 */       playerdata.setFrozen(!playerdata.isFrozen());
/*    */       
/* 67 */       playerMsg(player.getName() + " has been " + (playerdata.isFrozen() ? "frozen" : "unfrozen") + ".");
/* 68 */       playerMsg(player, "You have been " + (playerdata.isFrozen() ? "frozen" : "unfrozen") + ".", ChatColor.AQUA);
/*    */     }
/* 72 */     return true;
/*    */   }
/*    */   
/*    */   public static void setAllFrozen(boolean freeze)
/*    */   {
/* 77 */     allFrozen = freeze;
/* 78 */     for (TFM_PlayerData data : TFM_PlayerData.PLAYER_DATA.values()) {
/* 80 */       data.setFrozen(freeze);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\SilentBloodGaming\Desktop\MasterFreedomMod.jar!\me\StevenLawson\TotalFreedomMod\Commands\Command_fr.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */