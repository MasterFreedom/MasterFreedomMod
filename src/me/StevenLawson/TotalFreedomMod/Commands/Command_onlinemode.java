/*    */ package me.StevenLawson.TotalFreedomMod.Commands;
/*    */ 
/*    */ import me.StevenLawson.TotalFreedomMod.TFM_AdminList;
/*    */ import me.StevenLawson.TotalFreedomMod.TFM_Log;
/*    */ import me.StevenLawson.TotalFreedomMod.TFM_ServerInterface;
/*    */ import me.StevenLawson.TotalFreedomMod.TFM_Util;
/*    */ import org.bukkit.ChatColor;
/*    */ import org.bukkit.Server;
/*    */ import org.bukkit.command.Command;
/*    */ import org.bukkit.command.CommandSender;
/*    */ import org.bukkit.entity.Player;
/*    */ 
/*    */ @CommandPermissions(level=AdminLevel.ALL, source=SourceType.BOTH)
/*    */ @CommandParameters(description="Switch server online-mode on and off.", usage="/<command> <on | off>")
/*    */ public class Command_onlinemode
/*    */   extends TFM_Command
/*    */ {
/*    */   public boolean run(CommandSender sender, Player sender_p, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
/*    */   {
/* 19 */     if (args.length < 1)
/*    */     {
/* 21 */       playerMsg("Server is currently running with 'online-mode=" + (server.getOnlineMode() ? "true" : "false") + "'.", ChatColor.WHITE);
/* 22 */       playerMsg("\"/onlinemode on\" and \"/onlinemode off\" can be used to change online mode from the console.", ChatColor.WHITE);
/*    */     }
/*    */     else
/*    */     {
/* 28 */       if (((sender instanceof Player)) && (!TFM_AdminList.isSeniorAdmin(sender, true)))
/*    */       {
/* 30 */         playerMsg(TFM_Command.MSG_NO_PERMS);
/* 31 */         return true;
/*    */       }
/*    */       boolean online_mode;
/* 34 */       if (args[0].equalsIgnoreCase("on"))
/*    */       {
/* 36 */         online_mode = true;
/*    */       }
/*    */       else
/*    */       {
/*    */         boolean online_mode;
/* 38 */         if (args[0].equalsIgnoreCase("off")) {
/* 40 */           online_mode = false;
/*    */         } else {
/* 44 */           return false;
/*    */         }
/*    */       }
/*    */       try
/*    */       {
/*    */         boolean online_mode;
/* 49 */         TFM_ServerInterface.setOnlineMode(online_mode);
/* 51 */         if (online_mode) {
/* 53 */           for (Player player : server.getOnlinePlayers()) {
/* 55 */             player.kickPlayer("Server is activating \"online-mode=true\". Please reconnect.");
/*    */           }
/*    */         }
/* 59 */         TFM_Util.adminAction(sender.getName(), "Turning player validation " + (online_mode ? "on" : "off") + ".", true);
/*    */         
/* 61 */         server.reload();
/*    */       }
/*    */       catch (Exception ex)
/*    */       {
/* 65 */         TFM_Log.severe(ex);
/*    */       }
/*    */     }
/* 69 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\SilentBloodGaming\Desktop\MasterFreedomMod.jar!\me\StevenLawson\TotalFreedomMod\Commands\Command_onlinemode.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */