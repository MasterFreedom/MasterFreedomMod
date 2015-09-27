/*    */ package me.StevenLawson.TotalFreedomMod.Commands;
/*    */ 
/*    */ import java.util.regex.Matcher;
/*    */ import java.util.regex.Pattern;
/*    */ import me.StevenLawson.TotalFreedomMod.Bridge.TFM_EssentialsBridge;
/*    */ import me.StevenLawson.TotalFreedomMod.TFM_Util;
/*    */ import org.apache.commons.lang3.StringUtils;
/*    */ import org.bukkit.ChatColor;
/*    */ import org.bukkit.Server;
/*    */ import org.bukkit.command.Command;
/*    */ import org.bukkit.command.CommandSender;
/*    */ import org.bukkit.entity.Player;
/*    */ 
/*    */ @CommandPermissions(level=AdminLevel.SUPER, source=SourceType.BOTH)
/*    */ @CommandParameters(description="Essentials Interface Command - Remove distracting things from nicknames of all players on server.", usage="/<command>", aliases="nc")
/*    */ public class Command_nickclean
/*    */   extends TFM_Command
/*    */ {
/* 17 */   private static final ChatColor[] BLOCKED = { ChatColor.MAGIC, ChatColor.STRIKETHROUGH, ChatColor.ITALIC, ChatColor.UNDERLINE, ChatColor.BLACK };
/* 25 */   private static final Pattern REGEX = Pattern.compile("\\u00A7[" + StringUtils.join(BLOCKED, "") + "]");
/*    */   
/*    */   public boolean run(CommandSender sender, Player sender_p, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
/*    */   {
/* 30 */     TFM_Util.adminAction(sender.getName(), "Cleaning all nicknames.", false);
/* 32 */     for (Player player : server.getOnlinePlayers())
/*    */     {
/* 34 */       String playerName = player.getName();
/* 35 */       String nickName = TFM_EssentialsBridge.getNickname(playerName);
/* 36 */       if ((nickName != null) && (!nickName.isEmpty()) && (!nickName.equalsIgnoreCase(playerName)))
/*    */       {
/* 38 */         Matcher matcher = REGEX.matcher(nickName);
/* 39 */         if (matcher.find())
/*    */         {
/* 41 */           String newNickName = matcher.replaceAll("");
/* 42 */           playerMsg(ChatColor.RESET + playerName + ": \"" + nickName + ChatColor.RESET + "\" -> \"" + newNickName + ChatColor.RESET + "\".");
/* 43 */           TFM_EssentialsBridge.setNickname(playerName, newNickName);
/*    */         }
/*    */       }
/*    */     }
/* 48 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\SilentBloodGaming\Desktop\MasterFreedomMod.jar!\me\StevenLawson\TotalFreedomMod\Commands\Command_nickclean.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */