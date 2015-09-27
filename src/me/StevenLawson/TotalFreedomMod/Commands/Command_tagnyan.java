/*    */ package me.StevenLawson.TotalFreedomMod.Commands;
/*    */ 
/*    */ import me.StevenLawson.TotalFreedomMod.TFM_PlayerData;
/*    */ import me.StevenLawson.TotalFreedomMod.TFM_Util;
/*    */ import org.apache.commons.lang3.StringUtils;
/*    */ import org.bukkit.ChatColor;
/*    */ import org.bukkit.command.Command;
/*    */ import org.bukkit.command.CommandSender;
/*    */ import org.bukkit.entity.Player;
/*    */ 
/*    */ @CommandPermissions(level=AdminLevel.OP, source=SourceType.ONLY_IN_GAME)
/*    */ @CommandParameters(description="Gives you a tag with random colors", usage="/<command> <tag>", aliases="tn")
/*    */ public class Command_tagnyan
/*    */   extends TFM_Command
/*    */ {
/*    */   public boolean run(CommandSender sender, Player sender_p, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
/*    */   {
/* 18 */     if (args.length < 1) {
/* 20 */       return false;
/*    */     }
/* 23 */     StringBuilder tag = new StringBuilder();
/* 25 */     for (char c : ChatColor.stripColor(TFM_Util.colorize(StringUtils.join(args, " "))).toCharArray()) {
/* 27 */       tag.append(TFM_Util.randomChatColor()).append(c);
/*    */     }
/* 30 */     TFM_PlayerData data = TFM_PlayerData.getPlayerData(sender_p);
/* 31 */     data.setTag(tag.toString());
/*    */     
/* 33 */     playerMsg("Set tag to " + tag);
/*    */     
/* 35 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\SilentBloodGaming\Desktop\MasterFreedomMod.jar!\me\StevenLawson\TotalFreedomMod\Commands\Command_tagnyan.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */