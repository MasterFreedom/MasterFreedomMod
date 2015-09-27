/*     */ package me.StevenLawson.TotalFreedomMod.Commands;
/*     */ 
/*     */ import java.net.InetAddress;
/*     */ import java.net.InetSocketAddress;
/*     */ import me.StevenLawson.TotalFreedomMod.Bridge.TFM_WorldEditBridge;
/*     */ import me.StevenLawson.TotalFreedomMod.TFM_Ban;
/*     */ import me.StevenLawson.TotalFreedomMod.TFM_BanManager;
/*     */ import me.StevenLawson.TotalFreedomMod.TFM_RollbackManager;
/*     */ import me.StevenLawson.TotalFreedomMod.TFM_Util;
/*     */ import me.StevenLawson.TotalFreedomMod.TFM_UuidManager;
/*     */ import org.apache.commons.lang3.ArrayUtils;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ import org.bukkit.ChatColor;
/*     */ import org.bukkit.GameMode;
/*     */ import org.bukkit.Location;
/*     */ import org.bukkit.World;
/*     */ import org.bukkit.command.Command;
/*     */ import org.bukkit.command.CommandSender;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.inventory.PlayerInventory;
/*     */ 
/*     */ @CommandPermissions(level=AdminLevel.SUPER, source=SourceType.BOTH)
/*     */ @CommandParameters(description="Makes someone GTFO (deop and ip ban by username).", usage="/<command> <partialname>")
/*     */ public class Command_gtfo
/*     */   extends TFM_Command
/*     */ {
/*     */   public boolean run(CommandSender sender, Player sender_p, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
/*     */   {
/*  25 */     if (args.length == 0) {
/*  27 */       return false;
/*     */     }
/*  30 */     Player player = getPlayer(args[0]);
/*  32 */     if (player == null)
/*     */     {
/*  34 */       playerMsg(TFM_Command.PLAYER_NOT_FOUND, ChatColor.RED);
/*  35 */       return true;
/*     */     }
/*  38 */     String reason = null;
/*  39 */     if (args.length >= 2) {
/*  41 */       reason = StringUtils.join(ArrayUtils.subarray(args, 1, args.length), " ");
/*     */     }
/*  44 */     TFM_Util.bcastMsg(player.getName() + " has been a VERY naughty, naughty boy.", ChatColor.RED);
/*     */     try
/*     */     {
/*  49 */       TFM_WorldEditBridge.undo(player, 15);
/*     */     }
/*     */     catch (NoClassDefFoundError ex) {}
/*  56 */     TFM_RollbackManager.rollback(player.getName());
/*     */     
/*  59 */     player.setOp(false);
/*     */     
/*  62 */     player.setGameMode(GameMode.SURVIVAL);
/*     */     
/*  65 */     player.getInventory().clear();
/*     */     
/*  68 */     Location targetPos = player.getLocation();
/*  69 */     for (int x = -1; x <= 1; x++) {
/*  71 */       for (int z = -1; z <= 1; z++)
/*     */       {
/*  73 */         Location strike_pos = new Location(targetPos.getWorld(), targetPos.getBlockX() + x, targetPos.getBlockY(), targetPos.getBlockZ() + z);
/*  74 */         targetPos.getWorld().strikeLightning(strike_pos);
/*     */       }
/*     */     }
/*  79 */     String ip = TFM_Util.getFuzzyIp(player.getAddress().getAddress().getHostAddress());
/*     */     
/*  81 */     StringBuilder bcast = new StringBuilder().append(ChatColor.RED).append("Banning: ").append(player.getName()).append(", IP: ").append(ip);
/*  88 */     if (reason != null) {
/*  90 */       bcast.append(" - Reason: ").append(ChatColor.YELLOW).append(reason);
/*     */     }
/*  93 */     TFM_Util.bcastMsg(bcast.toString());
/*     */     
/*  95 */     TFM_BanManager.addIpBan(new TFM_Ban(ip, player.getName(), sender.getName(), null, reason));
/*     */     
/*  98 */     TFM_BanManager.addUuidBan(new TFM_Ban(TFM_UuidManager.getUniqueId(player), player.getName(), sender.getName(), null, reason));
/*     */     
/* 101 */     player.kickPlayer(ChatColor.RED + "GTFO" + (reason != null ? "\nReason: " + ChatColor.YELLOW + reason : ""));
/*     */     
/* 103 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\SilentBloodGaming\Desktop\MasterFreedomMod.jar!\me\StevenLawson\TotalFreedomMod\Commands\Command_gtfo.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */