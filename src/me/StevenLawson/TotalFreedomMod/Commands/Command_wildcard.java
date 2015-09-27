/*    */ package me.StevenLawson.TotalFreedomMod.Commands;
/*    */ 
/*    */ import me.StevenLawson.TotalFreedomMod.TFM_CommandBlocker;
/*    */ import org.apache.commons.lang3.StringUtils;
/*    */ import org.bukkit.ChatColor;
/*    */ import org.bukkit.Server;
/*    */ import org.bukkit.command.Command;
/*    */ import org.bukkit.command.CommandSender;
/*    */ import org.bukkit.entity.Player;
/*    */ 
/*    */ @CommandPermissions(level=AdminLevel.SUPER, source=SourceType.BOTH)
/*    */ @CommandParameters(description="Run any command on all users, username placeholder = ?.", usage="/<command> [fluff] ? [fluff] ?")
/*    */ public class Command_wildcard
/*    */   extends TFM_Command
/*    */ {
/*    */   public boolean run(CommandSender sender, Player sender_p, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
/*    */   {
/* 17 */     if (args.length == 0) {
/* 19 */       return false;
/*    */     }
/* 22 */     if (args[0].equals("wildcard"))
/*    */     {
/* 24 */       playerMsg("What the hell are you trying to do, you stupid idiot...", ChatColor.RED);
/* 25 */       return true;
/*    */     }
/* 27 */     if (args[0].equals("gtfo"))
/*    */     {
/* 29 */       playerMsg("Nice try", ChatColor.RED);
/* 30 */       return true;
/*    */     }
/* 32 */     if (args[0].equals("doom"))
/*    */     {
/* 34 */       playerMsg("Look, we all hate people, but this is not the way to deal with it, doom is evil enough!", ChatColor.RED);
/* 35 */       return true;
/*    */     }
/* 37 */     if (args[0].equals("saconfig"))
/*    */     {
/* 39 */       playerMsg("WOA, WTF are you trying to do???", ChatColor.RED);
/* 40 */       return true;
/*    */     }
/* 43 */     String baseCommand = StringUtils.join(args, " ");
/* 45 */     if (TFM_CommandBlocker.isCommandBlocked(baseCommand, sender)) {
/* 48 */       return true;
/*    */     }
/* 51 */     for (Player player : server.getOnlinePlayers())
/*    */     {
/* 53 */       String out_command = baseCommand.replaceAll("\\x3f", player.getName());
/* 54 */       playerMsg("Running Command: " + out_command);
/* 55 */       server.dispatchCommand(sender, out_command);
/*    */     }
/* 58 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\SilentBloodGaming\Desktop\MasterFreedomMod.jar!\me\StevenLawson\TotalFreedomMod\Commands\Command_wildcard.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */