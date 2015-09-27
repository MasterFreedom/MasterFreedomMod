/*     */ package me.StevenLawson.TotalFreedomMod.Commands;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.URL;
/*     */ import java.security.CodeSource;
/*     */ import java.security.ProtectionDomain;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import java.util.zip.ZipEntry;
/*     */ import java.util.zip.ZipInputStream;
/*     */ import me.StevenLawson.TotalFreedomMod.TFM_Log;
/*     */ import me.StevenLawson.TotalFreedomMod.TFM_Util;
/*     */ import me.StevenLawson.TotalFreedomMod.TotalFreedomMod;
/*     */ import org.bukkit.Bukkit;
/*     */ import org.bukkit.Server;
/*     */ import org.bukkit.command.Command;
/*     */ import org.bukkit.command.CommandException;
/*     */ import org.bukkit.command.CommandMap;
/*     */ import org.bukkit.command.CommandSender;
/*     */ import org.bukkit.command.PluginIdentifiableCommand;
/*     */ import org.bukkit.plugin.Plugin;
/*     */ import org.bukkit.plugin.PluginDescriptionFile;
/*     */ 
/*     */ public class TFM_CommandLoader
/*     */ {
/*  32 */   public static final Pattern COMMAND_PATTERN = Pattern.compile(TFM_CommandHandler.COMMAND_PATH.replace('.', '/') + "/(" + "Command_" + "[^\\$]+)\\.class");
/*  33 */   private static final List<TFM_CommandInfo> COMMAND_LIST = new ArrayList();
/*     */   
/*     */   private TFM_CommandLoader()
/*     */   {
/*  38 */     throw new AssertionError();
/*     */   }
/*     */   
/*     */   public static void scan()
/*     */   {
/*  43 */     CommandMap commandMap = getCommandMap();
/*  44 */     if (commandMap == null)
/*     */     {
/*  46 */       TFM_Log.severe("Error loading commandMap.");
/*  47 */       return;
/*     */     }
/*  49 */     COMMAND_LIST.clear();
/*  50 */     COMMAND_LIST.addAll(getCommands());
/*  52 */     for (TFM_CommandInfo commandInfo : COMMAND_LIST)
/*     */     {
/*  54 */       TFM_DynamicCommand dynamicCommand = new TFM_DynamicCommand(commandInfo, null);
/*     */       
/*  56 */       Command existing = commandMap.getCommand(dynamicCommand.getName());
/*  57 */       if (existing != null) {
/*  59 */         unregisterCommand(existing, commandMap);
/*     */       }
/*  62 */       commandMap.register(TotalFreedomMod.plugin.getDescription().getName(), dynamicCommand);
/*     */     }
/*  65 */     TFM_Log.info("TFM commands loaded.");
/*     */   }
/*     */   
/*     */   public static void unregisterCommand(String commandName)
/*     */   {
/*  70 */     CommandMap commandMap = getCommandMap();
/*  71 */     if (commandMap != null)
/*     */     {
/*  73 */       Command command = commandMap.getCommand(commandName.toLowerCase());
/*  74 */       if (command != null) {
/*  76 */         unregisterCommand(command, commandMap);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public static void unregisterCommand(Command command, CommandMap commandMap)
/*     */   {
/*     */     try
/*     */     {
/*  85 */       command.unregister(commandMap);
/*  86 */       knownCommands = getKnownCommands(commandMap);
/*  87 */       if (knownCommands != null)
/*     */       {
/*  89 */         knownCommands.remove(command.getName());
/*  90 */         for (String alias : command.getAliases()) {
/*  92 */           knownCommands.remove(alias);
/*     */         }
/*     */       }
/*     */     }
/*     */     catch (Exception ex)
/*     */     {
/*     */       HashMap<String, Command> knownCommands;
/*  98 */       TFM_Log.severe(ex);
/*     */     }
/*     */   }
/*     */   
/*     */   public static CommandMap getCommandMap()
/*     */   {
/* 105 */     Object commandMap = TFM_Util.getField(Bukkit.getServer().getPluginManager(), "commandMap");
/* 106 */     if (commandMap != null) {
/* 108 */       if ((commandMap instanceof CommandMap)) {
/* 110 */         return (CommandMap)commandMap;
/*     */       }
/*     */     }
/* 113 */     return null;
/*     */   }
/*     */   
/*     */   public static HashMap<String, Command> getKnownCommands(CommandMap commandMap)
/*     */   {
/* 119 */     Object knownCommands = TFM_Util.getField(commandMap, "knownCommands");
/* 120 */     if (knownCommands != null) {
/* 122 */       if ((knownCommands instanceof HashMap)) {
/* 124 */         return (HashMap)knownCommands;
/*     */       }
/*     */     }
/* 127 */     return null;
/*     */   }
/*     */   
/*     */   private static List<TFM_CommandInfo> getCommands()
/*     */   {
/* 132 */     List<TFM_CommandInfo> commandList = new ArrayList();
/*     */     try
/*     */     {
/* 136 */       CodeSource codeSource = TotalFreedomMod.class.getProtectionDomain().getCodeSource();
/* 137 */       if (codeSource != null)
/*     */       {
/* 139 */         ZipInputStream zip = new ZipInputStream(codeSource.getLocation().openStream());
/*     */         ZipEntry zipEntry;
/* 141 */         while ((zipEntry = zip.getNextEntry()) != null)
/*     */         {
/* 143 */           String entryName = zipEntry.getName();
/* 144 */           Matcher matcher = COMMAND_PATTERN.matcher(entryName);
/* 145 */           if (matcher.find()) {
/*     */             try
/*     */             {
/* 149 */               Class<?> commandClass = Class.forName(TFM_CommandHandler.COMMAND_PATH + "." + matcher.group(1));
/*     */               
/* 151 */               CommandPermissions commandPermissions = (CommandPermissions)commandClass.getAnnotation(CommandPermissions.class);
/* 152 */               CommandParameters commandParameters = (CommandParameters)commandClass.getAnnotation(CommandParameters.class);
/* 154 */               if ((commandPermissions != null) && (commandParameters != null))
/*     */               {
/* 156 */                 TFM_CommandInfo commandInfo = new TFM_CommandInfo(commandClass, matcher.group(1).split("_")[1], commandPermissions.level(), commandPermissions.source(), commandPermissions.blockHostConsole(), commandParameters.description(), commandParameters.usage(), commandParameters.aliases());
/*     */                 
/* 166 */                 commandList.add(commandInfo);
/*     */               }
/*     */             }
/*     */             catch (ClassNotFoundException ex)
/*     */             {
/* 171 */               TFM_Log.severe(ex);
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     catch (IOException ex)
/*     */     {
/* 179 */       TFM_Log.severe(ex);
/*     */     }
/* 182 */     return commandList;
/*     */   }
/*     */   
/*     */   public static class TFM_CommandInfo
/*     */   {
/*     */     private final String commandName;
/*     */     private final Class<?> commandClass;
/*     */     private final AdminLevel level;
/*     */     private final SourceType source;
/*     */     private final boolean blockHostConsole;
/*     */     private final String description;
/*     */     private final String usage;
/*     */     private final List<String> aliases;
/*     */     
/*     */     public TFM_CommandInfo(Class<?> commandClass, String commandName, AdminLevel level, SourceType source, boolean blockHostConsole, String description, String usage, String aliases)
/*     */     {
/* 198 */       this.commandName = commandName;
/* 199 */       this.commandClass = commandClass;
/* 200 */       this.level = level;
/* 201 */       this.source = source;
/* 202 */       this.blockHostConsole = blockHostConsole;
/* 203 */       this.description = description;
/* 204 */       this.usage = usage;
/* 205 */       this.aliases = ("".equals(aliases) ? new ArrayList() : Arrays.asList(aliases.split(",")));
/*     */     }
/*     */     
/*     */     public List<String> getAliases()
/*     */     {
/* 210 */       return Collections.unmodifiableList(aliases);
/*     */     }
/*     */     
/*     */     public Class<?> getCommandClass()
/*     */     {
/* 215 */       return commandClass;
/*     */     }
/*     */     
/*     */     public String getCommandName()
/*     */     {
/* 220 */       return commandName;
/*     */     }
/*     */     
/*     */     public String getDescription()
/*     */     {
/* 225 */       return description;
/*     */     }
/*     */     
/*     */     public String getDescriptionPermissioned()
/*     */     {
/* 230 */       String _description = description;
/* 232 */       switch (TFM_CommandLoader.1.$SwitchMap$me$StevenLawson$TotalFreedomMod$Commands$AdminLevel[getLevel().ordinal()])
/*     */       {
/*     */       case 1: 
/* 235 */         _description = "Senior " + (getSource() == SourceType.ONLY_CONSOLE ? "Console" : "") + " Command - " + _description;
/* 236 */         break;
/*     */       case 2: 
/* 238 */         _description = "Superadmin Command - " + _description;
/* 239 */         break;
/*     */       case 3: 
/* 241 */         _description = "OP Command - " + _description;
/*     */       }
/* 245 */       return _description;
/*     */     }
/*     */     
/*     */     public AdminLevel getLevel()
/*     */     {
/* 250 */       return level;
/*     */     }
/*     */     
/*     */     public SourceType getSource()
/*     */     {
/* 255 */       return source;
/*     */     }
/*     */     
/*     */     public String getUsage()
/*     */     {
/* 260 */       return usage;
/*     */     }
/*     */     
/*     */     public boolean getBlockHostConsole()
/*     */     {
/* 265 */       return blockHostConsole;
/*     */     }
/*     */     
/*     */     public String toString()
/*     */     {
/* 271 */       StringBuilder sb = new StringBuilder();
/* 272 */       sb.append("commandName: ").append(commandName);
/* 273 */       sb.append("\ncommandClass: ").append(commandClass.getName());
/* 274 */       sb.append("\nlevel: ").append(level);
/* 275 */       sb.append("\nsource: ").append(source);
/* 276 */       sb.append("\nblock_host_console: ").append(blockHostConsole);
/* 277 */       sb.append("\ndescription: ").append(description);
/* 278 */       sb.append("\nusage: ").append(usage);
/* 279 */       sb.append("\naliases: ").append(aliases);
/* 280 */       return sb.toString();
/*     */     }
/*     */   }
/*     */   
/*     */   public static class TFM_DynamicCommand
/*     */     extends Command
/*     */     implements PluginIdentifiableCommand
/*     */   {
/*     */     private final TFM_CommandLoader.TFM_CommandInfo commandInfo;
/*     */     
/*     */     private TFM_DynamicCommand(TFM_CommandLoader.TFM_CommandInfo commandInfo)
/*     */     {
/* 290 */       super(commandInfo.getDescriptionPermissioned(), commandInfo.getUsage(), commandInfo.getAliases());
/*     */       
/* 292 */       this.commandInfo = commandInfo;
/*     */     }
/*     */     
/*     */     public boolean execute(CommandSender sender, String commandLabel, String[] args)
/*     */     {
/* 298 */       boolean success = false;
/* 300 */       if (!getPlugin().isEnabled()) {
/* 302 */         return false;
/*     */       }
/*     */       try
/*     */       {
/* 307 */         success = getPlugin().onCommand(sender, this, commandLabel, args);
/*     */       }
/*     */       catch (Throwable ex)
/*     */       {
/* 311 */         throw new CommandException("Unhandled exception executing command '" + commandLabel + "' in plugin " + getPlugin().getDescription().getFullName(), ex);
/*     */       }
/* 314 */       if ((!success) && (getUsage().length() > 0)) {
/* 316 */         for (String line : getUsage().replace("<command>", commandLabel).split("\n")) {
/* 318 */           sender.sendMessage(line);
/*     */         }
/*     */       }
/* 322 */       return success;
/*     */     }
/*     */     
/*     */     public Plugin getPlugin()
/*     */     {
/* 328 */       return TotalFreedomMod.plugin;
/*     */     }
/*     */     
/*     */     public TFM_CommandLoader.TFM_CommandInfo getCommandInfo()
/*     */     {
/* 333 */       return commandInfo;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\SilentBloodGaming\Desktop\MasterFreedomMod.jar!\me\StevenLawson\TotalFreedomMod\Commands\TFM_CommandLoader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */