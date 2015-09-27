/*    */ package me.StevenLawson.TotalFreedomMod.Commands;
/*    */ 
/*    */ import me.StevenLawson.TotalFreedomMod.TFM_Log;
/*    */ import me.StevenLawson.TotalFreedomMod.TotalFreedomMod;
/*    */ import org.apache.commons.lang3.StringUtils;
/*    */ import org.bukkit.ChatColor;
/*    */ import org.bukkit.command.Command;
/*    */ import org.bukkit.command.CommandSender;
/*    */ import org.bukkit.entity.Player;
/*    */ 
/*    */ public class TFM_CommandHandler
/*    */ {
/* 13 */   public static final String COMMAND_PATH = TFM_Command.class.getPackage().getName();
/*    */   public static final String COMMAND_PREFIX = "Command_";
/*    */   
/*    */   public static boolean handleCommand(CommandSender sender, Command cmd, String commandLabel, String[] args)
/*    */   {
/*    */     boolean senderIsConsole;
/*    */     Player playerSender;
/* 21 */     if ((sender instanceof Player))
/*    */     {
/* 23 */       boolean senderIsConsole = false;
/* 24 */       Player playerSender = (Player)sender;
/*    */       
/* 26 */       TFM_Log.info(String.format("[PLAYER_COMMAND] %s (%s): /%s %s", new Object[] { playerSender.getName(), ChatColor.stripColor(playerSender.getDisplayName()), commandLabel, StringUtils.join(args, " ") }), Boolean.valueOf(true));
/*    */     }
/*    */     else
/*    */     {
/* 34 */       senderIsConsole = true;
/* 35 */       playerSender = null;
/*    */       
/* 37 */       TFM_Log.info(String.format("[CONSOLE_COMMAND] %s: /%s %s", new Object[] { sender.getName(), commandLabel, StringUtils.join(args, " ") }), Boolean.valueOf(true));
/*    */     }
/*    */     TFM_Command dispatcher;
/*    */     try
/*    */     {
/* 46 */       ClassLoader classLoader = TotalFreedomMod.class.getClassLoader();
/* 47 */       dispatcher = (TFM_Command)classLoader.loadClass(String.format("%s.%s%s", new Object[] { COMMAND_PATH, "Command_", cmd.getName().toLowerCase() })).newInstance();
/*    */       
/* 51 */       dispatcher.setup(TotalFreedomMod.plugin, sender, dispatcher.getClass());
/*    */     }
/*    */     catch (Exception ex)
/*    */     {
/* 55 */       TFM_Log.severe("Could not load command: " + cmd.getName());
/* 56 */       TFM_Log.severe(ex);
/*    */       
/* 58 */       sender.sendMessage(ChatColor.RED + "Command Error! Could not load command: " + cmd.getName());
/* 59 */       return true;
/*    */     }
/* 62 */     if (!dispatcher.senderHasPermission())
/*    */     {
/* 64 */       sender.sendMessage(TFM_Command.MSG_NO_PERMS);
/* 65 */       return true;
/*    */     }
/*    */     try
/*    */     {
/* 70 */       return dispatcher.run(sender, playerSender, cmd, commandLabel, args, senderIsConsole);
/*    */     }
/*    */     catch (Exception ex)
/*    */     {
/* 74 */       TFM_Log.severe("Command Error: " + commandLabel);
/* 75 */       TFM_Log.severe(ex);
/* 76 */       sender.sendMessage(ChatColor.RED + "Command Error: " + ex.getMessage());
/*    */     }
/* 79 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\SilentBloodGaming\Desktop\MasterFreedomMod.jar!\me\StevenLawson\TotalFreedomMod\Commands\TFM_CommandHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */