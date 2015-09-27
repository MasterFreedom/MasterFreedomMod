/*    */ package me.StevenLawson.TotalFreedomMod.Commands;
/*    */ 
/*    */ import me.StevenLawson.TotalFreedomMod.TFM_PlayerData;
/*    */ import me.StevenLawson.TotalFreedomMod.TFM_Util;
/*    */ import org.apache.commons.lang3.StringUtils;
/*    */ import org.bukkit.command.Command;
/*    */ import org.bukkit.command.CommandSender;
/*    */ import org.bukkit.entity.Player;
/*    */ 
/*    */ @CommandPermissions(level=AdminLevel.SUPER, source=SourceType.BOTH)
/*    */ @CommandParameters(description="AdminChat - Talk privately with other admins. Using <command> itself will toggle AdminChat on and off for all messages.", usage="/<command> [message...]", aliases="adminchat")
/*    */ public class Command_o
/*    */   extends TFM_Command
/*    */ {
/*    */   public boolean run(CommandSender sender, Player sender_p, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
/*    */   {
/* 20 */     if (args.length == 0)
/*    */     {
/* 22 */       if (senderIsConsole)
/*    */       {
/* 24 */         playerMsg("Only in-game players can toggle AdminChat.");
/* 25 */         return true;
/*    */       }
/* 28 */       TFM_PlayerData userinfo = TFM_PlayerData.getPlayerData(sender_p);
/* 29 */       userinfo.setAdminChat(!userinfo.inAdminChat());
/* 30 */       playerMsg("Toggled Admin Chat " + (userinfo.inAdminChat() ? "on" : "off") + ".");
/*    */     }
/*    */     else
/*    */     {
/* 34 */       TFM_Util.adminChatMessage(sender, StringUtils.join(args, " "), senderIsConsole);
/*    */     }
/* 37 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\SilentBloodGaming\Desktop\MasterFreedomMod.jar!\me\StevenLawson\TotalFreedomMod\Commands\Command_o.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */