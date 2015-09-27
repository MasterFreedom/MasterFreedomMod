/*    */ package me.StevenLawson.TotalFreedomMod.Commands;
/*    */ 
/*    */ import me.StevenLawson.TotalFreedomMod.TFM_Ban;
/*    */ import me.StevenLawson.TotalFreedomMod.TFM_BanManager;
/*    */ import me.StevenLawson.TotalFreedomMod.TFM_Util;
/*    */ import me.StevenLawson.TotalFreedomMod.TFM_UuidManager;
/*    */ import org.bukkit.ChatColor;
/*    */ import org.bukkit.Location;
/*    */ import org.bukkit.World;
/*    */ import org.bukkit.command.Command;
/*    */ import org.bukkit.command.CommandSender;
/*    */ import org.bukkit.entity.Player;
/*    */ 
/*    */ @CommandPermissions(level=AdminLevel.SUPER, source=SourceType.BOTH)
/*    */ @CommandParameters(description="Temporarily bans a player for five minutes.", usage="/<command> <partialname>", aliases="noob")
/*    */ public class Command_tban
/*    */   extends TFM_Command
/*    */ {
/*    */   public boolean run(CommandSender sender, Player sender_p, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
/*    */   {
/* 21 */     if (args.length != 1) {
/* 23 */       return false;
/*    */     }
/* 26 */     Player player = getPlayer(args[0]);
/* 28 */     if (player == null)
/*    */     {
/* 30 */       playerMsg(TFM_Command.PLAYER_NOT_FOUND, ChatColor.RED);
/* 31 */       return true;
/*    */     }
/* 35 */     Location targetPos = player.getLocation();
/* 36 */     for (int x = -1; x <= 1; x++) {
/* 38 */       for (int z = -1; z <= 1; z++)
/*    */       {
/* 40 */         Location strike_pos = new Location(targetPos.getWorld(), targetPos.getBlockX() + x, targetPos.getBlockY(), targetPos.getBlockZ() + z);
/* 41 */         targetPos.getWorld().strikeLightning(strike_pos);
/*    */       }
/*    */     }
/* 45 */     TFM_Util.adminAction(sender.getName(), "Tempbanning: " + player.getName() + " for 5 minutes.", true);
/* 46 */     TFM_BanManager.addUuidBan(new TFM_Ban(TFM_UuidManager.getUniqueId(player), player.getName(), sender.getName(), TFM_Util.parseDateOffset("5m"), ChatColor.RED + "You have been temporarily banned for 5 minutes."));
/*    */     
/* 48 */     TFM_BanManager.addIpBan(new TFM_Ban(TFM_Util.getIp(player), player.getName(), sender.getName(), TFM_Util.parseDateOffset("5m"), ChatColor.RED + "You have been temporarily banned for 5 minutes."));
/*    */     
/* 51 */     player.kickPlayer(ChatColor.RED + "You have been temporarily banned for five minutes. Please read totalfreedom.me for more info.");
/*    */     
/* 53 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\SilentBloodGaming\Desktop\MasterFreedomMod.jar!\me\StevenLawson\TotalFreedomMod\Commands\Command_tban.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */