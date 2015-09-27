/*     */ package me.StevenLawson.TotalFreedomMod.Commands;
/*     */ 
/*     */ import me.StevenLawson.TotalFreedomMod.TFM_AdminList;
/*     */ import me.StevenLawson.TotalFreedomMod.TFM_DepreciationAggregator;
/*     */ import me.StevenLawson.TotalFreedomMod.TFM_ServerInterface;
/*     */ import me.StevenLawson.TotalFreedomMod.TFM_Util;
/*     */ import org.bukkit.OfflinePlayer;
/*     */ import org.bukkit.Server;
/*     */ import org.bukkit.command.Command;
/*     */ import org.bukkit.command.CommandSender;
/*     */ import org.bukkit.entity.Player;
/*     */ 
/*     */ @CommandPermissions(level=AdminLevel.OP, source=SourceType.BOTH)
/*     */ @CommandParameters(description="Manage the whitelist.", usage="/<command> <on | off | list | count | add <player> | remove <player> | addall | purge>")
/*     */ public class Command_whitelist
/*     */   extends TFM_Command
/*     */ {
/*     */   public boolean run(CommandSender sender, Player sender_p, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
/*     */   {
/*  19 */     if (args.length < 1) {
/*  21 */       return false;
/*     */     }
/*  25 */     if (args[0].equalsIgnoreCase("list"))
/*     */     {
/*  27 */       playerMsg("Whitelisted players: " + TFM_Util.playerListToNames(server.getWhitelistedPlayers()));
/*  28 */       return true;
/*     */     }
/*  32 */     if (args[0].equalsIgnoreCase("count"))
/*     */     {
/*  34 */       int onlineWPs = 0;
/*  35 */       int offlineWPs = 0;
/*  36 */       int totalWPs = 0;
/*  38 */       for (OfflinePlayer player : server.getWhitelistedPlayers())
/*     */       {
/*  40 */         if (player.isOnline()) {
/*  42 */           onlineWPs++;
/*     */         } else {
/*  46 */           offlineWPs++;
/*     */         }
/*  48 */         totalWPs++;
/*     */       }
/*  51 */       playerMsg("Online whitelisted players: " + onlineWPs);
/*  52 */       playerMsg("Offline whitelisted players: " + offlineWPs);
/*  53 */       playerMsg("Total whitelisted players: " + totalWPs);
/*     */       
/*  55 */       return true;
/*     */     }
/*  59 */     if ((!senderIsConsole) && (!TFM_AdminList.isSuperAdmin(sender)))
/*     */     {
/*  61 */       sender.sendMessage(TFM_Command.MSG_NO_PERMS);
/*  62 */       return true;
/*     */     }
/*  66 */     if (args[0].equalsIgnoreCase("on"))
/*     */     {
/*  68 */       TFM_Util.adminAction(sender.getName(), "Turning the whitelist on.", true);
/*  69 */       server.setWhitelist(true);
/*  70 */       return true;
/*     */     }
/*  74 */     if (args[0].equalsIgnoreCase("off"))
/*     */     {
/*  76 */       TFM_Util.adminAction(sender.getName(), "Turning the whitelist off.", true);
/*  77 */       server.setWhitelist(false);
/*  78 */       return true;
/*     */     }
/*  82 */     if (args[0].equalsIgnoreCase("add"))
/*     */     {
/*  84 */       if (args.length < 2) {
/*  86 */         return false;
/*     */       }
/*  89 */       String search_name = args[1].trim().toLowerCase();
/*     */       
/*  91 */       OfflinePlayer player = getPlayer(search_name);
/*  93 */       if (player == null) {
/*  95 */         player = TFM_DepreciationAggregator.getOfflinePlayer(server, search_name);
/*     */       }
/*  98 */       TFM_Util.adminAction(sender.getName(), "Adding " + player.getName() + " to the whitelist.", false);
/*  99 */       player.setWhitelisted(true);
/* 100 */       return true;
/*     */     }
/* 104 */     if ("remove".equals(args[0]))
/*     */     {
/* 106 */       if (args.length < 2) {
/* 108 */         return false;
/*     */       }
/* 111 */       String search_name = args[1].trim().toLowerCase();
/*     */       
/* 113 */       OfflinePlayer player = getPlayer(search_name);
/* 115 */       if (player == null) {
/* 117 */         player = TFM_DepreciationAggregator.getOfflinePlayer(server, search_name);
/*     */       }
/* 120 */       if (player.isWhitelisted())
/*     */       {
/* 122 */         TFM_Util.adminAction(sender.getName(), "Removing " + player.getName() + " from the whitelist.", false);
/* 123 */         player.setWhitelisted(false);
/* 124 */         return true;
/*     */       }
/* 128 */       playerMsg("That player is not whitelisted");
/* 129 */       return true;
/*     */     }
/* 135 */     if (args[0].equalsIgnoreCase("addall"))
/*     */     {
/* 137 */       TFM_Util.adminAction(sender.getName(), "Adding all online players to the whitelist.", false);
/* 138 */       int counter = 0;
/* 139 */       for (Player player : server.getOnlinePlayers()) {
/* 141 */         if (!player.isWhitelisted())
/*     */         {
/* 143 */           player.setWhitelisted(true);
/* 144 */           counter++;
/*     */         }
/*     */       }
/* 148 */       playerMsg("Whitelisted " + counter + " players.");
/* 149 */       return true;
/*     */     }
/* 153 */     if (!senderIsConsole)
/*     */     {
/* 155 */       sender.sendMessage(TFM_Command.MSG_NO_PERMS);
/* 156 */       return true;
/*     */     }
/* 160 */     if (args[0].equalsIgnoreCase("purge"))
/*     */     {
/* 162 */       TFM_Util.adminAction(sender.getName(), "Removing all players from the whitelist.", false);
/* 163 */       playerMsg("Removed " + TFM_ServerInterface.purgeWhitelist() + " players from the whitelist.");
/*     */       
/* 165 */       return true;
/*     */     }
/* 169 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\SilentBloodGaming\Desktop\MasterFreedomMod.jar!\me\StevenLawson\TotalFreedomMod\Commands\Command_whitelist.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */