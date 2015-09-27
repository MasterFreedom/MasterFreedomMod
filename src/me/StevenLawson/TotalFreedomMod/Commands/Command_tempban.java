/*    */ package me.StevenLawson.TotalFreedomMod.Commands;
/*    */ 
/*    */ import java.text.SimpleDateFormat;
/*    */ import java.util.Date;
/*    */ import me.StevenLawson.TotalFreedomMod.TFM_Ban;
/*    */ import me.StevenLawson.TotalFreedomMod.TFM_BanManager;
/*    */ import me.StevenLawson.TotalFreedomMod.TFM_Util;
/*    */ import me.StevenLawson.TotalFreedomMod.TFM_UuidManager;
/*    */ import org.apache.commons.lang3.ArrayUtils;
/*    */ import org.apache.commons.lang3.StringUtils;
/*    */ import org.bukkit.Location;
/*    */ import org.bukkit.World;
/*    */ import org.bukkit.command.Command;
/*    */ import org.bukkit.command.CommandSender;
/*    */ import org.bukkit.entity.Player;
/*    */ 
/*    */ @CommandPermissions(level=AdminLevel.SUPER, source=SourceType.BOTH)
/*    */ @CommandParameters(description="Temporarily ban someone.", usage="/<command> [playername] [duration] [reason]")
/*    */ public class Command_tempban
/*    */   extends TFM_Command
/*    */ {
/* 20 */   private static final SimpleDateFormat date_format = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
/*    */   
/*    */   public boolean run(CommandSender sender, Player sender_p, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
/*    */   {
/* 25 */     if (args.length < 1) {
/* 27 */       return false;
/*    */     }
/* 30 */     Player player = getPlayer(args[0]);
/* 32 */     if (player == null)
/*    */     {
/* 34 */       playerMsg(TFM_Command.PLAYER_NOT_FOUND);
/* 35 */       return true;
/*    */     }
/* 38 */     StringBuilder message = new StringBuilder("Temporarily banned " + player.getName());
/*    */     
/* 40 */     Date expires = TFM_Util.parseDateOffset("30m");
/* 41 */     if (args.length >= 2)
/*    */     {
/* 43 */       Date parsed_offset = TFM_Util.parseDateOffset(args[1]);
/* 44 */       if (parsed_offset != null) {
/* 46 */         expires = parsed_offset;
/*    */       }
/*    */     }
/* 49 */     message.append(" until ").append(date_format.format(expires));
/*    */     
/* 51 */     String reason = "Banned by " + sender.getName();
/* 52 */     if (args.length >= 3)
/*    */     {
/* 54 */       reason = StringUtils.join(ArrayUtils.subarray(args, 2, args.length), " ") + " (" + sender.getName() + ")";
/* 55 */       message.append(", Reason: \"").append(reason).append("\"");
/*    */     }
/* 59 */     Location targetPos = player.getLocation();
/* 60 */     for (int x = -1; x <= 1; x++) {
/* 62 */       for (int z = -1; z <= 1; z++)
/*    */       {
/* 64 */         Location strike_pos = new Location(targetPos.getWorld(), targetPos.getBlockX() + x, targetPos.getBlockY(), targetPos.getBlockZ() + z);
/* 65 */         targetPos.getWorld().strikeLightning(strike_pos);
/*    */       }
/*    */     }
/* 69 */     TFM_Util.adminAction(sender.getName(), message.toString(), true);
/*    */     
/* 71 */     TFM_BanManager.addIpBan(new TFM_Ban(TFM_Util.getIp(player), player.getName(), sender.getName(), expires, reason));
/* 72 */     TFM_BanManager.addUuidBan(new TFM_Ban(TFM_UuidManager.getUniqueId(player), player.getName(), sender.getName(), expires, reason));
/*    */     
/* 74 */     player.kickPlayer(sender.getName() + " - " + message.toString());
/*    */     
/* 76 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\SilentBloodGaming\Desktop\MasterFreedomMod.jar!\me\StevenLawson\TotalFreedomMod\Commands\Command_tempban.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */