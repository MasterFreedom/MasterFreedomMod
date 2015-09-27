/*     */ package me.StevenLawson.TotalFreedomMod.Commands;
/*     */ 
/*     */ import me.StevenLawson.TotalFreedomMod.TFM_AdminList;
/*     */ import me.StevenLawson.TotalFreedomMod.TFM_PlayerData;
/*     */ import me.StevenLawson.TotalFreedomMod.TFM_Util;
/*     */ import org.bukkit.Server;
/*     */ import org.bukkit.command.Command;
/*     */ import org.bukkit.command.CommandSender;
/*     */ import org.bukkit.entity.Player;
/*     */ 
/*     */ @CommandPermissions(level=AdminLevel.SUPER, source=SourceType.BOTH)
/*     */ @CommandParameters(description="Mutes a player with brute force.", usage="/<command> [<player> [-s] | list | purge | all]", aliases="mute")
/*     */ public class Command_stfu
/*     */   extends TFM_Command
/*     */ {
/*     */   public boolean run(CommandSender sender, Player sender_p, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
/*     */   {
/*  17 */     if ((args.length == 0) || (args.length > 2)) {
/*  19 */       return false;
/*     */     }
/*  22 */     if (args[0].equalsIgnoreCase("list"))
/*     */     {
/*  24 */       playerMsg("Muted players:");
/*     */       
/*  26 */       int count = 0;
/*  27 */       for (Player mp : server.getOnlinePlayers())
/*     */       {
/*  29 */         TFM_PlayerData info = TFM_PlayerData.getPlayerData(mp);
/*  30 */         if (info.isMuted())
/*     */         {
/*  32 */           playerMsg("- " + mp.getName());
/*  33 */           count++;
/*     */         }
/*     */       }
/*  36 */       if (count == 0) {
/*  38 */         playerMsg("- none");
/*     */       }
/*     */     }
/*  41 */     else if (args[0].equalsIgnoreCase("purge"))
/*     */     {
/*  43 */       TFM_Util.adminAction(sender.getName(), "Unmuting all players.", true);
/*     */       
/*  45 */       int count = 0;
/*  46 */       for (Player mp : server.getOnlinePlayers())
/*     */       {
/*  48 */         TFM_PlayerData info = TFM_PlayerData.getPlayerData(mp);
/*  49 */         if (info.isMuted())
/*     */         {
/*  51 */           info.setMuted(false);
/*  52 */           count++;
/*     */         }
/*     */       }
/*  55 */       playerMsg("Unmuted " + count + " players.");
/*     */     }
/*  57 */     else if (args[0].equalsIgnoreCase("all"))
/*     */     {
/*  59 */       TFM_Util.adminAction(sender.getName(), "Muting all non-Superadmins", true);
/*     */       
/*  62 */       int counter = 0;
/*  63 */       for (Player player : server.getOnlinePlayers()) {
/*  65 */         if (!TFM_AdminList.isSuperAdmin(player))
/*     */         {
/*  67 */           TFM_PlayerData playerdata = TFM_PlayerData.getPlayerData(player);
/*  68 */           playerdata.setMuted(true);
/*  69 */           counter++;
/*     */         }
/*     */       }
/*  73 */       playerMsg("Muted " + counter + " players.");
/*     */     }
/*     */     else
/*     */     {
/*  77 */       Player player = getPlayer(args[0]);
/*  79 */       if (player == null)
/*     */       {
/*  81 */         sender.sendMessage(TFM_Command.PLAYER_NOT_FOUND);
/*  82 */         return true;
/*     */       }
/*  85 */       TFM_PlayerData playerdata = TFM_PlayerData.getPlayerData(player);
/*  86 */       if (playerdata.isMuted())
/*     */       {
/*  88 */         TFM_Util.adminAction(sender.getName(), "Unmuting " + player.getName(), true);
/*  89 */         playerdata.setMuted(false);
/*  90 */         playerMsg("Unmuted " + player.getName());
/*     */       }
/*  94 */       else if (!TFM_AdminList.isSuperAdmin(player))
/*     */       {
/*  96 */         TFM_Util.adminAction(sender.getName(), "Muting " + player.getName(), true);
/*  97 */         playerdata.setMuted(true);
/*  99 */         if ((args.length == 2) && (args[1].equalsIgnoreCase("-s"))) {
/* 101 */           Command_smite.smite(player);
/*     */         }
/* 104 */         playerMsg("Muted " + player.getName());
/*     */       }
/*     */       else
/*     */       {
/* 108 */         playerMsg(player.getName() + " is a superadmin, and can't be muted.");
/*     */       }
/*     */     }
/* 113 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\SilentBloodGaming\Desktop\MasterFreedomMod.jar!\me\StevenLawson\TotalFreedomMod\Commands\Command_stfu.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */