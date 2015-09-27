/*    */ package me.StevenLawson.TotalFreedomMod.Commands;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import me.StevenLawson.TotalFreedomMod.TFM_Util;
/*    */ import org.apache.commons.lang3.StringUtils;
/*    */ import org.bukkit.Server;
/*    */ import org.bukkit.command.Command;
/*    */ import org.bukkit.command.CommandSender;
/*    */ import org.bukkit.entity.Player;
/*    */ 
/*    */ @CommandPermissions(level=AdminLevel.OP, source=SourceType.BOTH)
/*    */ @CommandParameters(description="Quick Op - op someone based on a partial name.", usage="/<command> <partialname>")
/*    */ public class Command_qop
/*    */   extends TFM_Command
/*    */ {
/*    */   public boolean run(CommandSender sender, Player sender_p, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
/*    */   {
/* 18 */     if (args.length < 1) {
/* 20 */       return false;
/*    */     }
/* 23 */     boolean silent = false;
/* 24 */     if (args.length == 2) {
/* 26 */       silent = args[1].equalsIgnoreCase("-s");
/*    */     }
/* 29 */     String targetName = args[0].toLowerCase();
/*    */     
/* 31 */     List<String> matchedPlayerNames = new ArrayList();
/* 32 */     for (Player player : server.getOnlinePlayers()) {
/* 34 */       if ((player.getName().toLowerCase().contains(targetName)) || (player.getDisplayName().toLowerCase().contains(targetName))) {
/* 36 */         if (!player.isOp())
/*    */         {
/* 38 */           matchedPlayerNames.add(player.getName());
/* 39 */           player.setOp(true);
/* 40 */           player.sendMessage(TFM_Command.YOU_ARE_OP);
/*    */         }
/*    */       }
/*    */     }
/* 45 */     if (!matchedPlayerNames.isEmpty())
/*    */     {
/* 47 */       if (!silent) {
/* 49 */         TFM_Util.adminAction(sender.getName(), "Opping " + StringUtils.join(matchedPlayerNames, ", "), false);
/*    */       }
/*    */     }
/*    */     else {
/* 54 */       playerMsg("No targets matched.");
/*    */     }
/* 57 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\SilentBloodGaming\Desktop\MasterFreedomMod.jar!\me\StevenLawson\TotalFreedomMod\Commands\Command_qop.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */