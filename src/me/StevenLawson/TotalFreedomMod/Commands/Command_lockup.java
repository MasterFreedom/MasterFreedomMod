/*     */ package me.StevenLawson.TotalFreedomMod.Commands;
/*     */ 
/*     */ import me.StevenLawson.TotalFreedomMod.TFM_PlayerData;
/*     */ import me.StevenLawson.TotalFreedomMod.TFM_Util;
/*     */ import org.bukkit.Server;
/*     */ import org.bukkit.command.Command;
/*     */ import org.bukkit.command.CommandSender;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.scheduler.BukkitRunnable;
/*     */ import org.bukkit.scheduler.BukkitTask;
/*     */ 
/*     */ @CommandPermissions(level=AdminLevel.SENIOR, source=SourceType.ONLY_CONSOLE, blockHostConsole=true)
/*     */ @CommandParameters(description="Block target's minecraft input. This is evil, and I never should have wrote it.", usage="/<command> <all | purge | <<partialname> on | off>>")
/*     */ public class Command_lockup
/*     */   extends TFM_Command
/*     */ {
/*     */   public boolean run(CommandSender sender, Player sender_p, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
/*     */   {
/*  18 */     if (args.length == 1)
/*     */     {
/*  20 */       if (args[0].equalsIgnoreCase("all"))
/*     */       {
/*  22 */         TFM_Util.adminAction(sender.getName(), "Locking up all players", true);
/*  24 */         for (Player player : server.getOnlinePlayers()) {
/*  26 */           startLockup(player);
/*     */         }
/*  28 */         playerMsg("Locked up all players.");
/*     */       }
/*  30 */       else if (args[0].equalsIgnoreCase("purge"))
/*     */       {
/*  32 */         TFM_Util.adminAction(sender.getName(), "Unlocking all players", true);
/*  33 */         for (Player player : server.getOnlinePlayers()) {
/*  35 */           cancelLockup(player);
/*     */         }
/*  38 */         playerMsg("Unlocked all players.");
/*     */       }
/*     */       else
/*     */       {
/*  42 */         return false;
/*     */       }
/*     */     }
/*  45 */     else if (args.length == 2)
/*     */     {
/*  47 */       if (args[1].equalsIgnoreCase("on"))
/*     */       {
/*  49 */         Player player = getPlayer(args[0]);
/*  51 */         if (player == null)
/*     */         {
/*  53 */           sender.sendMessage(TFM_Command.PLAYER_NOT_FOUND);
/*  54 */           return true;
/*     */         }
/*  57 */         TFM_Util.adminAction(sender.getName(), "Locking up " + player.getName(), true);
/*  58 */         startLockup(player);
/*  59 */         playerMsg("Locked up " + player.getName() + ".");
/*     */       }
/*  61 */       else if ("off".equals(args[1]))
/*     */       {
/*  63 */         Player player = getPlayer(args[0]);
/*  65 */         if (player == null)
/*     */         {
/*  67 */           sender.sendMessage(TFM_Command.PLAYER_NOT_FOUND);
/*  68 */           return true;
/*     */         }
/*  71 */         TFM_Util.adminAction(sender.getName(), "Unlocking " + player.getName(), true);
/*  72 */         cancelLockup(player);
/*  73 */         playerMsg("Unlocked " + player.getName() + ".");
/*     */       }
/*     */       else
/*     */       {
/*  77 */         return false;
/*     */       }
/*     */     }
/*     */     else {
/*  82 */       return false;
/*     */     }
/*  85 */     return true;
/*     */   }
/*     */   
/*     */   private void cancelLockup(TFM_PlayerData playerdata)
/*     */   {
/*  90 */     BukkitTask lockupScheduleID = playerdata.getLockupScheduleID();
/*  91 */     if (lockupScheduleID != null)
/*     */     {
/*  93 */       lockupScheduleID.cancel();
/*  94 */       playerdata.setLockupScheduleID(null);
/*     */     }
/*     */   }
/*     */   
/*     */   private void cancelLockup(Player player)
/*     */   {
/* 100 */     cancelLockup(TFM_PlayerData.getPlayerData(player));
/*     */   }
/*     */   
/*     */   private void startLockup(final Player player)
/*     */   {
/* 105 */     final TFM_PlayerData playerdata = TFM_PlayerData.getPlayerData(player);
/*     */     
/* 107 */     cancelLockup(playerdata);
/*     */     
/* 109 */     playerdata.setLockupScheduleID(new BukkitRunnable()
/*     */     {
/*     */       public void run()
/*     */       {
/* 114 */         if (player.isOnline()) {
/* 116 */           player.openInventory(player.getInventory());
/*     */         } else {
/* 120 */           Command_lockup.this.cancelLockup(playerdata);
/*     */         }
/*     */       }
/* 120 */     }.runTaskTimer(plugin, 0L, 5L));
/*     */   }
/*     */ }


/* Location:              C:\Users\SilentBloodGaming\Desktop\MasterFreedomMod.jar!\me\StevenLawson\TotalFreedomMod\Commands\Command_lockup.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */