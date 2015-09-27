/*    */ package me.StevenLawson.TotalFreedomMod.Commands;
/*    */ 
/*    */ import java.util.List;
/*    */ import me.StevenLawson.TotalFreedomMod.TFM_PermbanList;
/*    */ import org.apache.commons.lang3.StringUtils;
/*    */ import org.bukkit.ChatColor;
/*    */ import org.bukkit.command.Command;
/*    */ import org.bukkit.command.CommandSender;
/*    */ import org.bukkit.entity.Player;
/*    */ 
/*    */ @CommandPermissions(level=AdminLevel.SUPER, source=SourceType.BOTH, blockHostConsole=true)
/*    */ @CommandParameters(description="Manage permanently banned players and IPs.", usage="/<command> <list | reload>")
/*    */ public class Command_permban
/*    */   extends TFM_Command
/*    */ {
/*    */   public boolean run(CommandSender sender, Player sender_p, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
/*    */   {
/* 17 */     if (args.length != 1) {
/* 19 */       return false;
/*    */     }
/* 22 */     if (args[0].equalsIgnoreCase("list"))
/*    */     {
/* 24 */       dumplist(sender);
/*    */     }
/* 26 */     else if (args[0].equalsIgnoreCase("reload"))
/*    */     {
/* 28 */       if (!senderIsConsole)
/*    */       {
/* 30 */         sender.sendMessage(TFM_Command.MSG_NO_PERMS);
/* 31 */         return true;
/*    */       }
/* 33 */       playerMsg("Reloading permban list...", ChatColor.RED);
/* 34 */       TFM_PermbanList.load();
/* 35 */       dumplist(sender);
/*    */     }
/*    */     else
/*    */     {
/* 39 */       return false;
/*    */     }
/* 42 */     return true;
/*    */   }
/*    */   
/*    */   private void dumplist(CommandSender sender)
/*    */   {
/* 47 */     if (TFM_PermbanList.getPermbannedPlayers().isEmpty())
/*    */     {
/* 49 */       playerMsg("No permanently banned player names.");
/*    */     }
/*    */     else
/*    */     {
/* 53 */       playerMsg(TFM_PermbanList.getPermbannedPlayers().size() + " permanently banned players:");
/* 54 */       playerMsg(StringUtils.join(TFM_PermbanList.getPermbannedPlayers(), ", "));
/*    */     }
/* 57 */     if (TFM_PermbanList.getPermbannedIps().isEmpty())
/*    */     {
/* 59 */       playerMsg("No permanently banned IPs.");
/*    */     }
/*    */     else
/*    */     {
/* 63 */       playerMsg(TFM_PermbanList.getPermbannedIps().size() + " permanently banned IPs:");
/* 64 */       playerMsg(StringUtils.join(TFM_PermbanList.getPermbannedIps(), ", "));
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\SilentBloodGaming\Desktop\MasterFreedomMod.jar!\me\StevenLawson\TotalFreedomMod\Commands\Command_permban.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */