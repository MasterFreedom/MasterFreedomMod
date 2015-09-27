/*    */ package me.StevenLawson.TotalFreedomMod.Commands;
/*    */ 
/*    */ import me.StevenLawson.TotalFreedomMod.TFM_Util;
/*    */ import org.apache.commons.lang3.StringUtils;
/*    */ import org.bukkit.command.Command;
/*    */ import org.bukkit.command.CommandSender;
/*    */ import org.bukkit.entity.Player;
/*    */ 
/*    */ @CommandPermissions(level=AdminLevel.SENIOR, source=SourceType.ONLY_CONSOLE, blockHostConsole=true)
/*    */ @CommandParameters(description="Broadcasts the given message. Supports colors.", usage="/<command> <message>")
/*    */ public class Command_rawsay
/*    */   extends TFM_Command
/*    */ {
/*    */   public boolean run(CommandSender sender, Player sender_p, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
/*    */   {
/* 16 */     if (args.length > 0) {
/* 18 */       TFM_Util.bcastMsg(TFM_Util.colorize(StringUtils.join(args, " ")));
/*    */     }
/* 21 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\SilentBloodGaming\Desktop\MasterFreedomMod.jar!\me\StevenLawson\TotalFreedomMod\Commands\Command_rawsay.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */