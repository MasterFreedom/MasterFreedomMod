/*     */ package me.StevenLawson.TotalFreedomMod.HTTPD;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import me.StevenLawson.TotalFreedomMod.Commands.AdminLevel;
/*     */ import me.StevenLawson.TotalFreedomMod.Commands.TFM_CommandLoader;
/*     */ import me.StevenLawson.TotalFreedomMod.Commands.TFM_CommandLoader.TFM_CommandInfo;
/*     */ import me.StevenLawson.TotalFreedomMod.Commands.TFM_CommandLoader.TFM_DynamicCommand;
/*     */ import org.apache.commons.lang3.StringEscapeUtils;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ import org.bukkit.command.Command;
/*     */ import org.bukkit.command.CommandMap;
/*     */ import org.bukkit.command.PluginIdentifiableCommand;
/*     */ import org.bukkit.plugin.Plugin;
/*     */ 
/*     */ public class Module_help
/*     */   extends TFM_HTTPD_Module
/*     */ {
/*     */   public Module_help(NanoHTTPD.HTTPSession session)
/*     */   {
/*  25 */     super(session);
/*     */   }
/*     */   
/*     */   public String getBody()
/*     */   {
/*  31 */     StringBuilder responseBody = new StringBuilder();
/*     */     CommandMap commandMap;
/*     */     HashMap<String, Command> knownCommands;
/*  35 */     if (((commandMap = TFM_CommandLoader.getCommandMap()) == null) || ((knownCommands = TFM_CommandLoader.getKnownCommands(commandMap)) == null)) {
/*  38 */       return HTMLGenerationTools.paragraph("Error loading commands.");
/*     */     }
/*     */     HashMap<String, Command> knownCommands;
/*  41 */     responseBody.append(HTMLGenerationTools.heading("Command Help", 1)).append(HTMLGenerationTools.paragraph("This page is an automatically generated listing of all plugin commands that are currently live on the server. Please note that it does not include vanilla server commands."));
/*     */     
/*  47 */     Map<String, List<Command>> commandsByPlugin = new HashMap();
/*     */     
/*  49 */     Iterator<Map.Entry<String, Command>> itKnownCommands = knownCommands.entrySet().iterator();
/*  50 */     while (itKnownCommands.hasNext())
/*     */     {
/*  52 */       Map.Entry<String, Command> entry = (Map.Entry)itKnownCommands.next();
/*  53 */       String name = (String)entry.getKey();
/*  54 */       Command command = (Command)entry.getValue();
/*  55 */       if (name.equalsIgnoreCase(command.getName()))
/*     */       {
/*  57 */         String pluginName = "Bukkit Default";
/*  58 */         if ((command instanceof PluginIdentifiableCommand)) {
/*  60 */           pluginName = ((PluginIdentifiableCommand)command).getPlugin().getName();
/*     */         }
/*  62 */         List<Command> pluginCommands = (List)commandsByPlugin.get(pluginName);
/*  63 */         if (pluginCommands == null) {
/*  65 */           commandsByPlugin.put(pluginName, pluginCommands = new ArrayList());
/*     */         }
/*  67 */         pluginCommands.add(command);
/*     */       }
/*     */     }
/*  71 */     Iterator<Map.Entry<String, List<Command>>> itCommandsByPlugin = commandsByPlugin.entrySet().iterator();
/*  72 */     while (itCommandsByPlugin.hasNext())
/*     */     {
/*  74 */       Map.Entry<String, List<Command>> entry = (Map.Entry)itCommandsByPlugin.next();
/*  75 */       String pluginName = (String)entry.getKey();
/*  76 */       List<Command> commands = (List)entry.getValue();
/*     */       
/*  78 */       Collections.sort(commands, new Comparator()
/*     */       {
/*     */         public int compare(Command a, Command b)
/*     */         {
/*  83 */           if (((a instanceof TFM_CommandLoader.TFM_DynamicCommand)) && ((b instanceof TFM_CommandLoader.TFM_DynamicCommand)))
/*     */           {
/*  85 */             String aName = ((TFM_CommandLoader.TFM_DynamicCommand)a).getCommandInfo().getLevel().name() + a.getName();
/*  86 */             String bName = ((TFM_CommandLoader.TFM_DynamicCommand)b).getCommandInfo().getLevel().name() + b.getName();
/*  87 */             return aName.compareTo(bName);
/*     */           }
/*  89 */           return a.getName().compareTo(b.getName());
/*     */         }
/*  92 */       });
/*  93 */       responseBody.append(HTMLGenerationTools.heading(pluginName, 2)).append("<ul>\r\n");
/*     */       
/*  95 */       AdminLevel lastTfmCommandLevel = null;
/*  96 */       for (Command command : commands)
/*     */       {
/*  98 */         if ("TotalFreedomMod".equals(pluginName))
/*     */         {
/* 100 */           AdminLevel tfmCommandLevel = ((TFM_CommandLoader.TFM_DynamicCommand)command).getCommandInfo().getLevel();
/* 101 */           if ((lastTfmCommandLevel == null) || (lastTfmCommandLevel != tfmCommandLevel)) {
/* 103 */             responseBody.append("</ul>\r\n").append(HTMLGenerationTools.heading(tfmCommandLevel.getFriendlyName(), 3)).append("<ul>\r\n");
/*     */           }
/* 105 */           lastTfmCommandLevel = tfmCommandLevel;
/*     */         }
/* 107 */         responseBody.append(buildDescription(command));
/*     */       }
/* 110 */       responseBody.append("</ul>\r\n");
/*     */     }
/* 113 */     return responseBody.toString();
/*     */   }
/*     */   
/*     */   private static String buildDescription(Command command)
/*     */   {
/* 118 */     StringBuilder sb = new StringBuilder();
/*     */     
/* 120 */     sb.append("<li><span class=\"commandName\">{$CMD_NAME}</span> - Usage: <span class=\"commandUsage\">{$CMD_USAGE}</span>".replace("{$CMD_NAME}", StringEscapeUtils.escapeHtml4(command.getName().trim())).replace("{$CMD_USAGE}", StringEscapeUtils.escapeHtml4(command.getUsage().trim())));
/* 125 */     if (!command.getAliases().isEmpty()) {
/* 127 */       sb.append(" - Aliases: <span class=\"commandAliases\">{$CMD_ALIASES}</span>".replace("{$CMD_ALIASES}", StringEscapeUtils.escapeHtml4(StringUtils.join(command.getAliases(), ", "))));
/*     */     }
/* 132 */     sb.append("<br><span class=\"commandDescription\">{$CMD_DESC}</span></li>\r\n".replace("{$CMD_DESC}", StringEscapeUtils.escapeHtml4(command.getDescription().trim())));
/*     */     
/* 136 */     return sb.toString();
/*     */   }
/*     */   
/*     */   public String getTitle()
/*     */   {
/* 142 */     return "TotalFreedomMod :: Command Help";
/*     */   }
/*     */   
/*     */   public String getStyle()
/*     */   {
/* 148 */     return ".commandName{font-weight:bold;}.commandDescription{padding-left:15px;}li{margin:.15em;padding:.15em;}";
/*     */   }
/*     */ }


/* Location:              C:\Users\SilentBloodGaming\Desktop\MasterFreedomMod.jar!\me\StevenLawson\TotalFreedomMod\HTTPD\Module_help.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */