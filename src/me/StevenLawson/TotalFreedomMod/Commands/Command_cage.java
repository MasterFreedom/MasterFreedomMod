/*     */ package me.StevenLawson.TotalFreedomMod.Commands;
/*     */ 
/*     */ import me.StevenLawson.TotalFreedomMod.TFM_PlayerData;
/*     */ import me.StevenLawson.TotalFreedomMod.TFM_Util;
/*     */ import org.bukkit.GameMode;
/*     */ import org.bukkit.Location;
/*     */ import org.bukkit.Material;
/*     */ import org.bukkit.Server;
/*     */ import org.bukkit.command.Command;
/*     */ import org.bukkit.command.CommandSender;
/*     */ import org.bukkit.entity.Player;
/*     */ 
/*     */ @CommandPermissions(level=AdminLevel.SUPER, source=SourceType.BOTH)
/*     */ @CommandParameters(description="Place a cage around someone.", usage="/<command> <purge | off | <partialname> [outermaterial] [innermaterial]>")
/*     */ public class Command_cage
/*     */   extends TFM_Command
/*     */ {
/*     */   public boolean run(CommandSender sender, Player sender_p, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
/*     */   {
/*  19 */     if (args.length == 0) {
/*  21 */       return false;
/*     */     }
/*  24 */     if (("off".equals(args[0])) && ((sender instanceof Player)))
/*     */     {
/*  26 */       TFM_Util.adminAction(sender.getName(), "Uncaging " + sender.getName(), true);
/*  27 */       TFM_PlayerData playerdata = TFM_PlayerData.getPlayerData(sender_p);
/*     */       
/*  29 */       playerdata.setCaged(false);
/*  30 */       playerdata.regenerateHistory();
/*  31 */       playerdata.clearHistory();
/*     */       
/*  33 */       return true;
/*     */     }
/*  35 */     if ("purge".equals(args[0]))
/*     */     {
/*  37 */       TFM_Util.adminAction(sender.getName(), "Uncaging all players", true);
/*  39 */       for (Player player : server.getOnlinePlayers())
/*     */       {
/*  41 */         TFM_PlayerData playerdata = TFM_PlayerData.getPlayerData(player);
/*  42 */         playerdata.setCaged(false);
/*  43 */         playerdata.regenerateHistory();
/*  44 */         playerdata.clearHistory();
/*     */       }
/*  47 */       return true;
/*     */     }
/*  50 */     Player player = getPlayer(args[0]);
/*  52 */     if (player == null)
/*     */     {
/*  54 */       sender.sendMessage(TFM_Command.PLAYER_NOT_FOUND);
/*  55 */       return true;
/*     */     }
/*  58 */     TFM_PlayerData playerdata = TFM_PlayerData.getPlayerData(player);
/*     */     
/*  60 */     Material outerMaterial = Material.GLASS;
/*  61 */     Material innerMaterial = Material.AIR;
/*  63 */     if (args.length >= 2)
/*     */     {
/*  65 */       if ("off".equals(args[1]))
/*     */       {
/*  67 */         TFM_Util.adminAction(sender.getName(), "Uncaging " + player.getName(), true);
/*     */         
/*  69 */         playerdata.setCaged(false);
/*  70 */         playerdata.regenerateHistory();
/*  71 */         playerdata.clearHistory();
/*     */         
/*  73 */         return true;
/*     */       }
/*  77 */       if ("darth".equalsIgnoreCase(args[1])) {
/*  79 */         outerMaterial = Material.SKULL;
/*  81 */       } else if (Material.matchMaterial(args[1]) != null) {
/*  83 */         outerMaterial = Material.matchMaterial(args[1]);
/*     */       }
/*     */     }
/*  88 */     if (args.length >= 3) {
/*  90 */       if (args[2].equalsIgnoreCase("water")) {
/*  92 */         innerMaterial = Material.STATIONARY_WATER;
/*  94 */       } else if (args[2].equalsIgnoreCase("lava")) {
/*  96 */         innerMaterial = Material.STATIONARY_LAVA;
/*     */       }
/*     */     }
/* 100 */     Location targetPos = player.getLocation().clone().add(0.0D, 1.0D, 0.0D);
/* 101 */     playerdata.setCaged(true, targetPos, outerMaterial, innerMaterial);
/* 102 */     playerdata.regenerateHistory();
/* 103 */     playerdata.clearHistory();
/* 104 */     TFM_Util.buildHistory(targetPos, 2, playerdata);
/* 105 */     TFM_Util.generateHollowCube(targetPos, 2, outerMaterial);
/* 106 */     TFM_Util.generateCube(targetPos, 1, innerMaterial);
/*     */     
/* 108 */     player.setGameMode(GameMode.SURVIVAL);
/* 110 */     if (outerMaterial != Material.SKULL) {
/* 112 */       TFM_Util.adminAction(sender.getName(), "Caging " + player.getName(), true);
/*     */     } else {
/* 116 */       TFM_Util.adminAction(sender.getName(), "Caging " + player.getName() + " in PURE_DARTH", true);
/*     */     }
/* 119 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\SilentBloodGaming\Desktop\MasterFreedomMod.jar!\me\StevenLawson\TotalFreedomMod\Commands\Command_cage.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */