/*    */ package me.StevenLawson.TotalFreedomMod.Commands;
/*    */ 
/*    */ import me.StevenLawson.TotalFreedomMod.Config.TFM_ConfigEntry;
/*    */ import me.StevenLawson.TotalFreedomMod.TFM_AdminList;
/*    */ import me.StevenLawson.TotalFreedomMod.TFM_Util;
/*    */ import org.bukkit.Server;
/*    */ import org.bukkit.command.Command;
/*    */ import org.bukkit.command.CommandSender;
/*    */ import org.bukkit.entity.Player;
/*    */ 
/*    */ @CommandPermissions(level=AdminLevel.SUPER, source=SourceType.ONLY_CONSOLE, blockHostConsole=true)
/*    */ @CommandParameters(description="Close server to non-superadmins.", usage="/<command> [on | off]")
/*    */ public class Command_adminmode
/*    */   extends TFM_Command
/*    */ {
/*    */   public boolean run(CommandSender sender, Player sender_p, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
/*    */   {
/* 17 */     if (args.length != 1) {
/* 19 */       return false;
/*    */     }
/* 22 */     if (args[0].equalsIgnoreCase("off"))
/*    */     {
/* 24 */       TFM_ConfigEntry.ADMIN_ONLY_MODE.setBoolean(Boolean.valueOf(false));
/* 25 */       TFM_Util.adminAction(sender.getName(), "Opening the server to all players.", true);
/* 26 */       return true;
/*    */     }
/* 28 */     if (args[0].equalsIgnoreCase("on"))
/*    */     {
/* 30 */       TFM_ConfigEntry.ADMIN_ONLY_MODE.setBoolean(Boolean.valueOf(true));
/* 31 */       TFM_Util.adminAction(sender.getName(), "Closing the server to non-superadmins.", true);
/* 32 */       for (Player player : server.getOnlinePlayers()) {
/* 34 */         if (!TFM_AdminList.isSuperAdmin(player)) {
/* 36 */           player.kickPlayer("Server is now closed to non-superadmins.");
/*    */         }
/*    */       }
/* 39 */       return true;
/*    */     }
/* 42 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\SilentBloodGaming\Desktop\MasterFreedomMod.jar!\me\StevenLawson\TotalFreedomMod\Commands\Command_adminmode.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */