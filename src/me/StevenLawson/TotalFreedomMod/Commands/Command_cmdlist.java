/*    */ package me.StevenLawson.TotalFreedomMod.Commands;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.Collections;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.Map.Entry;
/*    */ import org.apache.commons.lang3.StringUtils;
/*    */ import org.bukkit.Server;
/*    */ import org.bukkit.command.Command;
/*    */ import org.bukkit.command.CommandSender;
/*    */ import org.bukkit.entity.Player;
/*    */ import org.bukkit.plugin.Plugin;
/*    */ import org.bukkit.plugin.PluginDescriptionFile;
/*    */ import org.bukkit.plugin.PluginManager;
/*    */ 
/*    */ @CommandPermissions(level=AdminLevel.ALL, source=SourceType.BOTH)
/*    */ @CommandParameters(description="Show all commands for all server plugins.", usage="/<command>")
/*    */ public class Command_cmdlist
/*    */   extends TFM_Command
/*    */ {
/*    */   public boolean run(CommandSender sender, Player sender_p, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
/*    */   {
/* 22 */     List<String> commands = new ArrayList();
/* 24 */     for (Plugin targetPlugin : server.getPluginManager().getPlugins()) {
/*    */       try
/*    */       {
/* 28 */         PluginDescriptionFile desc = targetPlugin.getDescription();
/* 29 */         Map<String, Map<String, Object>> map = desc.getCommands();
/* 31 */         if (map != null) {
/* 33 */           for (Map.Entry<String, Map<String, Object>> entry : map.entrySet())
/*    */           {
/* 35 */             String command_name = (String)entry.getKey();
/* 36 */             commands.add(command_name);
/*    */           }
/*    */         }
/*    */       }
/*    */       catch (Throwable ex) {}
/*    */     }
/* 45 */     Collections.sort(commands);
/*    */     
/* 47 */     sender.sendMessage(StringUtils.join(commands, ","));
/*    */     
/* 49 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\SilentBloodGaming\Desktop\MasterFreedomMod.jar!\me\StevenLawson\TotalFreedomMod\Commands\Command_cmdlist.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */