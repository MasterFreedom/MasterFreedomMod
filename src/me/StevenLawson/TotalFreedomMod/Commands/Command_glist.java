/*     */ package me.StevenLawson.TotalFreedomMod.Commands;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import me.StevenLawson.TotalFreedomMod.Config.TFM_ConfigEntry;
/*     */ import me.StevenLawson.TotalFreedomMod.TFM_AdminList;
/*     */ import me.StevenLawson.TotalFreedomMod.TFM_Ban;
/*     */ import me.StevenLawson.TotalFreedomMod.TFM_BanManager;
/*     */ import me.StevenLawson.TotalFreedomMod.TFM_Player;
/*     */ import me.StevenLawson.TotalFreedomMod.TFM_PlayerList;
/*     */ import me.StevenLawson.TotalFreedomMod.TFM_Util;
/*     */ import me.StevenLawson.TotalFreedomMod.TFM_UuidManager;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ import org.bukkit.command.Command;
/*     */ import org.bukkit.command.CommandSender;
/*     */ import org.bukkit.entity.Player;
/*     */ 
/*     */ @CommandPermissions(level=AdminLevel.SUPER, source=SourceType.BOTH)
/*     */ @CommandParameters(description="Ban/Unban any player, even those who are not logged in anymore.", usage="/<command> <purge | <ban | unban> <username>>")
/*     */ public class Command_glist
/*     */   extends TFM_Command
/*     */ {
/*     */   public boolean run(CommandSender sender, Player sender_p, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
/*     */   {
/*  25 */     if (args.length < 1) {
/*  27 */       return false;
/*     */     }
/*  30 */     if (args.length == 1)
/*     */     {
/*  32 */       if (args[0].equalsIgnoreCase("purge"))
/*     */       {
/*  34 */         if (TFM_AdminList.isSeniorAdmin(sender))
/*     */         {
/*  36 */           TFM_PlayerList.purgeAll();
/*  37 */           playerMsg("Purged playerbase");
/*     */         }
/*     */         else
/*     */         {
/*  41 */           playerMsg("Only Senior Admins may purge the userlist.");
/*     */         }
/*  43 */         return true;
/*     */       }
/*  47 */       return false;
/*     */     }
/*  50 */     if (args.length == 2)
/*     */     {
/*  53 */       List<String> ips = new ArrayList();
/*     */       
/*  55 */       Player player = getPlayer(args[1]);
/*     */       String username;
/*  57 */       if (player == null)
/*     */       {
/*  59 */         TFM_Player entry = TFM_PlayerList.getEntry(TFM_UuidManager.getUniqueId(args[1]));
/*  61 */         if (entry == null)
/*     */         {
/*  63 */           playerMsg("Can't find that user. If target is not logged in, make sure that you spelled the name exactly.");
/*  64 */           return true;
/*     */         }
/*  67 */         String username = entry.getLastLoginName();
/*  68 */         ips.addAll(entry.getIps());
/*     */       }
/*     */       else
/*     */       {
/*  72 */         username = player.getName();
/*  73 */         TFM_Player entry = TFM_PlayerList.getEntry(TFM_UuidManager.getUniqueId(player));
/*  74 */         ips.addAll(entry.getIps());
/*     */       }
/*  77 */       String mode = args[0].toLowerCase();
/*  78 */       if (mode.equalsIgnoreCase("ban"))
/*     */       {
/*  80 */         TFM_Util.adminAction(sender.getName(), "Banning " + username + " and IPs: " + StringUtils.join(ips, ", "), true);
/*     */         
/*  82 */         Player target = getPlayer(username, true);
/*  83 */         if (target != null)
/*     */         {
/*  85 */           TFM_BanManager.addUuidBan(new TFM_Ban(TFM_UuidManager.getUniqueId(target), target.getName()));
/*  86 */           target.kickPlayer("You have been banned by " + sender.getName() + "\n If you think you have been banned wrongly, appeal here: " + TFM_ConfigEntry.SERVER_BAN_URL.getString());
/*     */         }
/*     */         else
/*     */         {
/*  90 */           TFM_BanManager.addUuidBan(new TFM_Ban(TFM_UuidManager.getUniqueId(username), username));
/*     */         }
/*  93 */         for (String ip : ips)
/*     */         {
/*  95 */           TFM_BanManager.addIpBan(new TFM_Ban(ip, username));
/*  96 */           TFM_BanManager.addIpBan(new TFM_Ban(TFM_Util.getFuzzyIp(ip), username));
/*     */         }
/*     */       }
/*  99 */       else if (mode.equalsIgnoreCase("unban"))
/*     */       {
/* 101 */         TFM_Util.adminAction(sender.getName(), "Unbanning " + username + " and IPs: " + StringUtils.join(ips, ", "), true);
/* 102 */         TFM_BanManager.unbanUuid(TFM_UuidManager.getUniqueId(username));
/* 103 */         for (String ip : ips)
/*     */         {
/* 106 */           TFM_BanManager.unbanIp(ip);
/* 107 */           TFM_BanManager.unbanIp(TFM_Util.getFuzzyIp(ip));
/*     */         }
/*     */       }
/*     */       else
/*     */       {
/* 112 */         return false;
/*     */       }
/* 115 */       return true;
/*     */     }
/* 119 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\SilentBloodGaming\Desktop\MasterFreedomMod.jar!\me\StevenLawson\TotalFreedomMod\Commands\Command_glist.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */