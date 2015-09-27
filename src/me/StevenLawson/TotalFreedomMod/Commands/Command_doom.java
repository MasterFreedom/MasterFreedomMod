/*     */ package me.StevenLawson.TotalFreedomMod.Commands;
/*     */ 
/*     */ import java.net.InetAddress;
/*     */ import java.net.InetSocketAddress;
/*     */ import me.StevenLawson.TotalFreedomMod.TFM_AdminList;
/*     */ import me.StevenLawson.TotalFreedomMod.TFM_Ban;
/*     */ import me.StevenLawson.TotalFreedomMod.TFM_BanManager;
/*     */ import me.StevenLawson.TotalFreedomMod.TFM_Player;
/*     */ import me.StevenLawson.TotalFreedomMod.TFM_PlayerList;
/*     */ import me.StevenLawson.TotalFreedomMod.TFM_Util;
/*     */ import org.bukkit.ChatColor;
/*     */ import org.bukkit.GameMode;
/*     */ import org.bukkit.World;
/*     */ import org.bukkit.command.Command;
/*     */ import org.bukkit.command.CommandSender;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.inventory.PlayerInventory;
/*     */ import org.bukkit.scheduler.BukkitRunnable;
/*     */ import org.bukkit.util.Vector;
/*     */ 
/*     */ @CommandPermissions(level=AdminLevel.SENIOR, source=SourceType.ONLY_CONSOLE)
/*     */ @CommandParameters(description="For the bad Superadmins", usage="/<command> <playername>")
/*     */ public class Command_doom
/*     */   extends TFM_Command
/*     */ {
/*     */   public boolean run(final CommandSender sender, Player sender_p, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
/*     */   {
/*  23 */     if (args.length != 1) {
/*  25 */       return false;
/*     */     }
/*  28 */     final Player player = getPlayer(args[0]);
/*  30 */     if (player == null)
/*     */     {
/*  32 */       sender.sendMessage(TFM_Command.PLAYER_NOT_FOUND);
/*  33 */       return true;
/*     */     }
/*  36 */     TFM_Util.adminAction(sender.getName(), "Casting oblivion over " + player.getName(), true);
/*  37 */     TFM_Util.bcastMsg(player.getName() + " will be completely obliviated!", ChatColor.RED);
/*     */     
/*  39 */     final String ip = player.getAddress().getAddress().getHostAddress().trim();
/*  42 */     if (TFM_AdminList.isSuperAdmin(player))
/*     */     {
/*  44 */       TFM_Util.adminAction(sender.getName(), "Removing " + player.getName() + " from the superadmin list.", true);
/*  45 */       TFM_AdminList.removeSuperadmin(player);
/*     */     }
/*  49 */     player.setWhitelisted(false);
/*     */     
/*  52 */     player.setOp(false);
/*  55 */     for (String playerIp : TFM_PlayerList.getEntry(player).getIps()) {
/*  57 */       TFM_BanManager.addIpBan(new TFM_Ban(playerIp, player.getName()));
/*     */     }
/*  61 */     TFM_BanManager.addUuidBan(player);
/*     */     
/*  64 */     player.setGameMode(GameMode.SURVIVAL);
/*     */     
/*  67 */     player.closeInventory();
/*  68 */     player.getInventory().clear();
/*     */     
/*  71 */     player.setFireTicks(10000);
/*     */     
/*  74 */     player.getWorld().createExplosion(player.getLocation(), 4.0F);
/*     */     
/*  77 */     player.setVelocity(player.getVelocity().clone().add(new Vector(0, 20, 0)));
/*     */     
/*  79 */     new BukkitRunnable()
/*     */     {
/*     */       public void run()
/*     */       {
/*  85 */         player.getWorld().strikeLightning(player.getLocation());
/*     */         
/*  88 */         player.setHealth(0.0D);
/*     */       }
/*  88 */     }.runTaskLater(plugin, 40L);
/*     */     
/*  92 */     new BukkitRunnable()
/*     */     {
/*     */       public void run()
/*     */       {
/*  98 */         TFM_Util.adminAction(sender.getName(), "Banning " + player.getName() + ", IP: " + ip, true);
/*     */         
/* 101 */         player.getWorld().createExplosion(player.getLocation(), 4.0F);
/*     */         
/* 104 */         player.kickPlayer(ChatColor.RED + "FUCKOFF, and get your shit together!");
/*     */       }
/* 104 */     }.runTaskLater(plugin, 60L);
/*     */     
/* 108 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\SilentBloodGaming\Desktop\MasterFreedomMod.jar!\me\StevenLawson\TotalFreedomMod\Commands\Command_doom.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */