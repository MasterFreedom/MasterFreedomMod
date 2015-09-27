/*    */ package me.StevenLawson.TotalFreedomMod.Commands;
/*    */ 
/*    */ import me.StevenLawson.TotalFreedomMod.Bridge.TFM_EssentialsBridge;
/*    */ import me.StevenLawson.TotalFreedomMod.TFM_Util;
/*    */ import org.bukkit.Bukkit;
/*    */ import org.bukkit.ChatColor;
/*    */ import org.bukkit.command.Command;
/*    */ import org.bukkit.command.CommandSender;
/*    */ import org.bukkit.entity.Player;
/*    */ 
/*    */ @CommandPermissions(level=AdminLevel.OP, source=SourceType.ONLY_IN_GAME)
/*    */ @CommandParameters(description="Essentials Interface Command - Nyanify your nickname.", usage="/<command> <<nick> | off>")
/*    */ public class Command_nicknyan
/*    */   extends TFM_Command
/*    */ {
/*    */   public boolean run(CommandSender sender, Player sender_p, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
/*    */   {
/* 18 */     if (args.length != 1) {
/* 20 */       return false;
/*    */     }
/* 23 */     if ("off".equals(args[0]))
/*    */     {
/* 25 */       TFM_EssentialsBridge.setNickname(sender.getName(), null);
/* 26 */       playerMsg("Nickname cleared.");
/* 27 */       return true;
/*    */     }
/* 30 */     String nickPlain = ChatColor.stripColor(TFM_Util.colorize(args[0].trim()));
/* 32 */     if (!nickPlain.matches("^[a-zA-Z_0-9§]+$"))
/*    */     {
/* 34 */       playerMsg("That nickname contains invalid characters.");
/* 35 */       return true;
/*    */     }
/* 37 */     if ((nickPlain.length() < 4) || (nickPlain.length() > 30))
/*    */     {
/* 39 */       playerMsg("Your nickname must be between 4 and 30 characters long.");
/* 40 */       return true;
/*    */     }
/* 43 */     for (Player player : Bukkit.getOnlinePlayers()) {
/* 45 */       if (player != sender_p) {
/* 49 */         if ((player.getName().equalsIgnoreCase(nickPlain)) || (ChatColor.stripColor(player.getDisplayName()).trim().equalsIgnoreCase(nickPlain)))
/*    */         {
/* 51 */           playerMsg("That nickname is already in use.");
/* 52 */           return true;
/*    */         }
/*    */       }
/*    */     }
/* 56 */     StringBuilder newNick = new StringBuilder();
/*    */     
/* 58 */     char[] chars = nickPlain.toCharArray();
/* 59 */     for (char c : chars) {
/* 61 */       newNick.append(TFM_Util.randomChatColor()).append(c);
/*    */     }
/* 64 */     newNick.append(ChatColor.WHITE);
/*    */     
/* 66 */     TFM_EssentialsBridge.setNickname(sender.getName(), newNick.toString());
/*    */     
/* 68 */     playerMsg("Your nickname is now: " + newNick.toString());
/*    */     
/* 70 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\SilentBloodGaming\Desktop\MasterFreedomMod.jar!\me\StevenLawson\TotalFreedomMod\Commands\Command_nicknyan.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */