/*     */ package me.StevenLawson.TotalFreedomMod.Commands;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import me.StevenLawson.TotalFreedomMod.TFM_AdminList;
/*     */ import me.StevenLawson.TotalFreedomMod.TFM_PlayerRank;
/*     */ import me.StevenLawson.TotalFreedomMod.TFM_Util;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ import org.bukkit.ChatColor;
/*     */ import org.bukkit.Server;
/*     */ import org.bukkit.command.Command;
/*     */ import org.bukkit.command.CommandSender;
/*     */ import org.bukkit.entity.Player;
/*     */ 
/*     */ @CommandPermissions(level=AdminLevel.ALL, source=SourceType.BOTH)
/*     */ @CommandParameters(description="Lists the real names of all online players.", usage="/<command> [-a | -i]", aliases="who")
/*     */ public class Command_list
/*     */   extends TFM_Command
/*     */ {
/*     */   private static enum ListFilter
/*     */   {
/*  20 */     ALL,  ADMINS,  IMPOSTORS;
/*     */     
/*     */     private ListFilter() {}
/*     */   }
/*     */   
/*     */   public boolean run(CommandSender sender, Player sender_p, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
/*     */   {
/*  28 */     if (args.length > 1) {
/*  30 */       return false;
/*     */     }
/*  33 */     if (TFM_Util.isFromHostConsole(sender.getName()))
/*     */     {
/*  35 */       List<String> names = new ArrayList();
/*  36 */       for (Player player : server.getOnlinePlayers()) {
/*  38 */         names.add(player.getName());
/*     */       }
/*  40 */       playerMsg("There are " + names.size() + "/" + server.getMaxPlayers() + " players online:\n" + StringUtils.join(names, ", "), ChatColor.WHITE);
/*  41 */       return true;
/*     */     }
/*     */     ListFilter listFilter;
/*  45 */     if (args.length == 1)
/*     */     {
/*     */       ListFilter listFilter;
/*  47 */       if ("-a".equals(args[0]))
/*     */       {
/*  49 */         listFilter = ListFilter.ADMINS;
/*     */       }
/*     */       else
/*     */       {
/*     */         ListFilter listFilter;
/*  51 */         if ("-i".equals(args[0])) {
/*  53 */           listFilter = ListFilter.IMPOSTORS;
/*     */         } else {
/*  57 */           return false;
/*     */         }
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/*  62 */       listFilter = ListFilter.ALL;
/*     */     }
/*  65 */     StringBuilder onlineStats = new StringBuilder();
/*  66 */     StringBuilder onlineUsers = new StringBuilder();
/*     */     
/*  68 */     onlineStats.append(ChatColor.BLUE).append("There are ").append(ChatColor.RED).append(server.getOnlinePlayers().size());
/*  69 */     onlineStats.append(ChatColor.BLUE).append(" out of a maximum ").append(ChatColor.RED).append(server.getMaxPlayers());
/*  70 */     onlineStats.append(ChatColor.BLUE).append(" players online.");
/*     */     
/*  72 */     List<String> names = new ArrayList();
/*  73 */     for (Player player : server.getOnlinePlayers()) {
/*  75 */       if (((listFilter != ListFilter.ADMINS) || (TFM_AdminList.isSuperAdmin(player))) && (
/*     */       
/*  80 */         (listFilter != ListFilter.IMPOSTORS) || (TFM_AdminList.isAdminImpostor(player)))) {
/*  85 */         names.add(TFM_PlayerRank.fromSender(player).getPrefix() + player.getName());
/*     */       }
/*     */     }
/*  88 */     onlineUsers.append("Connected ");
/*  89 */     onlineUsers.append(listFilter == ListFilter.ADMINS ? "admins: " : "players: ");
/*  90 */     onlineUsers.append(StringUtils.join(names, ChatColor.WHITE + ", "));
/*  92 */     if (senderIsConsole)
/*     */     {
/*  94 */       sender.sendMessage(ChatColor.stripColor(onlineStats.toString()));
/*  95 */       sender.sendMessage(ChatColor.stripColor(onlineUsers.toString()));
/*     */     }
/*     */     else
/*     */     {
/*  99 */       sender.sendMessage(onlineStats.toString());
/* 100 */       sender.sendMessage(onlineUsers.toString());
/*     */     }
/* 103 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\SilentBloodGaming\Desktop\MasterFreedomMod.jar!\me\StevenLawson\TotalFreedomMod\Commands\Command_list.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */