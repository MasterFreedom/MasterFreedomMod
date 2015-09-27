/*    */ package me.StevenLawson.TotalFreedomMod.Commands;
/*    */ 
/*    */ import java.util.Iterator;
/*    */ import java.util.Map;
/*    */ import java.util.Map.Entry;
/*    */ import java.util.Set;
/*    */ import me.StevenLawson.TotalFreedomMod.Bridge.TFM_EssentialsBridge;
/*    */ import me.StevenLawson.TotalFreedomMod.TFM_Util;
/*    */ import org.apache.commons.lang3.StringUtils;
/*    */ import org.bukkit.ChatColor;
/*    */ import org.bukkit.command.Command;
/*    */ import org.bukkit.command.CommandSender;
/*    */ import org.bukkit.entity.Player;
/*    */ 
/*    */ @CommandPermissions(level=AdminLevel.OP, source=SourceType.ONLY_IN_GAME)
/*    */ @CommandParameters(description="Essentials Interface Command - Color your current nickname.", usage="/<command> <color>")
/*    */ public class Command_colorme
/*    */   extends TFM_Command
/*    */ {
/*    */   public boolean run(CommandSender sender, Player sender_p, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
/*    */   {
/* 20 */     if (args.length != 1) {
/* 22 */       return false;
/*    */     }
/* 25 */     if ("list".equalsIgnoreCase(args[0]))
/*    */     {
/* 27 */       playerMsg("Colors: " + StringUtils.join(TFM_Util.CHAT_COLOR_NAMES.keySet(), ", "));
/* 28 */       return true;
/*    */     }
/* 31 */     String needle = args[0].trim().toLowerCase();
/* 32 */     ChatColor color = null;
/* 33 */     Iterator<Map.Entry<String, ChatColor>> it = TFM_Util.CHAT_COLOR_NAMES.entrySet().iterator();
/* 34 */     while (it.hasNext())
/*    */     {
/* 36 */       Map.Entry<String, ChatColor> entry = (Map.Entry)it.next();
/* 37 */       if (((String)entry.getKey()).contains(needle))
/*    */       {
/* 39 */         color = (ChatColor)entry.getValue();
/* 40 */         break;
/*    */       }
/*    */     }
/* 44 */     if (color == null)
/*    */     {
/* 46 */       playerMsg("Invalid color: " + needle + " - Use \"/colorme list\" to list colors.");
/* 47 */       return true;
/*    */     }
/* 50 */     String newNick = color + ChatColor.stripColor(sender_p.getDisplayName()).trim() + ChatColor.WHITE;
/*    */     
/* 52 */     TFM_EssentialsBridge.setNickname(sender.getName(), newNick);
/*    */     
/* 54 */     playerMsg("Your nickname is now: " + newNick);
/*    */     
/* 56 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\SilentBloodGaming\Desktop\MasterFreedomMod.jar!\me\StevenLawson\TotalFreedomMod\Commands\Command_colorme.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */