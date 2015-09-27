/*    */ package me.StevenLawson.TotalFreedomMod.Commands;
/*    */ 
/*    */ import me.StevenLawson.TotalFreedomMod.TFM_AdminList;
/*    */ import me.StevenLawson.TotalFreedomMod.TFM_Util;
/*    */ import org.bukkit.ChatColor;
/*    */ import org.bukkit.Server;
/*    */ import org.bukkit.command.Command;
/*    */ import org.bukkit.command.CommandSender;
/*    */ import org.bukkit.entity.Player;
/*    */ 
/*    */ @CommandPermissions(level=AdminLevel.SENIOR, source=SourceType.BOTH, blockHostConsole=true)
/*    */ @CommandParameters(description="Kick all non-superadmins on server.", usage="/<command>")
/*    */ public class Command_kicknoob
/*    */   extends TFM_Command
/*    */ {
/*    */   public boolean run(CommandSender sender, Player sender_p, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
/*    */   {
/* 17 */     TFM_Util.adminAction(sender.getName(), "Disconnecting all non-superadmins.", true);
/* 19 */     for (Player player : server.getOnlinePlayers()) {
/* 21 */       if (!TFM_AdminList.isSuperAdmin(player)) {
/* 23 */         player.kickPlayer(ChatColor.RED + "Disconnected by admin.");
/*    */       }
/*    */     }
/* 27 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\SilentBloodGaming\Desktop\MasterFreedomMod.jar!\me\StevenLawson\TotalFreedomMod\Commands\Command_kicknoob.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */