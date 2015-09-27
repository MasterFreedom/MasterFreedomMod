/*    */ package me.StevenLawson.TotalFreedomMod.Commands;
/*    */ 
/*    */ import java.util.List;
/*    */ import me.StevenLawson.TotalFreedomMod.TFM_AdminList;
/*    */ import me.StevenLawson.TotalFreedomMod.TFM_BanManager;
/*    */ import me.StevenLawson.TotalFreedomMod.TFM_Log;
/*    */ import me.StevenLawson.TotalFreedomMod.TFM_Util;
/*    */ import org.bukkit.ChatColor;
/*    */ import org.bukkit.command.Command;
/*    */ import org.bukkit.command.CommandSender;
/*    */ import org.bukkit.entity.Player;
/*    */ 
/*    */ @CommandPermissions(level=AdminLevel.OP, source=SourceType.BOTH)
/*    */ @CommandParameters(description="Shows all banned player names. Superadmins may optionally use 'purge' to clear the list.", usage="/<command> [purge]")
/*    */ public class Command_tfbanlist
/*    */   extends TFM_Command
/*    */ {
/*    */   public boolean run(CommandSender sender, Player sender_p, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
/*    */   {
/* 19 */     if (args.length > 0) {
/* 21 */       if (args[0].equalsIgnoreCase("purge"))
/*    */       {
/* 23 */         if ((senderIsConsole) || (TFM_AdminList.isSuperAdmin(sender)))
/*    */         {
/*    */           try
/*    */           {
/* 27 */             TFM_Util.adminAction(sender.getName(), "Purging the ban list", true);
/* 28 */             TFM_BanManager.purgeUuidBans();
/* 29 */             sender.sendMessage(ChatColor.GRAY + "Ban list has been purged.");
/*    */           }
/*    */           catch (Exception ex)
/*    */           {
/* 33 */             TFM_Log.severe(ex);
/*    */           }
/* 36 */           return true;
/*    */         }
/* 40 */         playerMsg("You do not have permission to purge the ban list, you may only view it.");
/*    */       }
/*    */     }
/* 45 */     playerMsg(TFM_BanManager.getUuidBanList().size() + " UUID bans total");
/*    */     
/* 47 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\SilentBloodGaming\Desktop\MasterFreedomMod.jar!\me\StevenLawson\TotalFreedomMod\Commands\Command_tfbanlist.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */