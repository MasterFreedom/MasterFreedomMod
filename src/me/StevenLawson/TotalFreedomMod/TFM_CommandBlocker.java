/*     */ package me.StevenLawson.TotalFreedomMod;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import me.StevenLawson.TotalFreedomMod.Commands.TFM_CommandLoader;
/*     */ import me.StevenLawson.TotalFreedomMod.Config.TFM_ConfigEntry;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ import org.bukkit.ChatColor;
/*     */ import org.bukkit.command.Command;
/*     */ import org.bukkit.command.CommandMap;
/*     */ import org.bukkit.command.CommandSender;
/*     */ import org.bukkit.entity.Player;
/*     */ 
/*     */ public class TFM_CommandBlocker
/*     */ {
/*  21 */   private static final Map<String, CommandBlockerEntry> BLOCKED_COMMANDS = new HashMap();
/*     */   
/*     */   private TFM_CommandBlocker()
/*     */   {
/*  26 */     throw new AssertionError();
/*     */   }
/*     */   
/*     */   public static void load()
/*     */   {
/*  31 */     BLOCKED_COMMANDS.clear();
/*     */     
/*  33 */     CommandMap commandMap = TFM_CommandLoader.getCommandMap();
/*  34 */     if (commandMap == null)
/*     */     {
/*  36 */       TFM_Log.severe("Error loading commandMap.");
/*  37 */       return;
/*     */     }
/*  41 */     List<String> blockedCommands = TFM_ConfigEntry.BLOCKED_COMMANDS.getList();
/*  42 */     for (String rawEntry : blockedCommands)
/*     */     {
/*  44 */       String[] parts = rawEntry.split(":");
/*  45 */       if ((parts.length < 3) || (parts.length > 4))
/*     */       {
/*  47 */         TFM_Log.warning("Invalid command blocker entry: " + rawEntry);
/*     */       }
/*     */       else
/*     */       {
/*  51 */         CommandBlockerRank rank = CommandBlockerRank.fromToken(parts[0]);
/*  52 */         CommandBlockerAction action = CommandBlockerAction.fromToken(parts[1]);
/*  53 */         String commandName = parts[2].toLowerCase().substring(1);
/*  54 */         String message = parts.length > 3 ? parts[3] : null;
/*  56 */         if ((rank == null) || (action == null) || (commandName == null) || (commandName.isEmpty()))
/*     */         {
/*  58 */           TFM_Log.warning("Invalid command blocker entry: " + rawEntry);
/*     */         }
/*     */         else
/*     */         {
/*  62 */           String[] commandParts = commandName.split(" ");
/*  63 */           String subCommand = null;
/*  64 */           if (commandParts.length > 1)
/*     */           {
/*  66 */             commandName = commandParts[0];
/*  67 */             subCommand = StringUtils.join(commandParts, " ", 1, commandParts.length).trim().toLowerCase();
/*     */           }
/*  70 */           Command command = commandMap.getCommand(commandName);
/*  73 */           if (command == null) {
/*  75 */             TFM_Log.info("Blocking unknown command: /" + commandName);
/*     */           } else {
/*  79 */             commandName = command.getName().toLowerCase();
/*     */           }
/*  82 */           if (BLOCKED_COMMANDS.containsKey(commandName))
/*     */           {
/*  84 */             TFM_Log.warning("Not blocking: /" + commandName + " - Duplicate entry exists!");
/*     */           }
/*     */           else
/*     */           {
/*  88 */             blockedCommandEntry = new CommandBlockerEntry(rank, action, commandName, subCommand, message, null);
/*  89 */             BLOCKED_COMMANDS.put(blockedCommandEntry.getCommand(), blockedCommandEntry);
/*  91 */             if (command != null) {
/*  93 */               for (String alias : command.getAliases()) {
/*  95 */                 BLOCKED_COMMANDS.put(alias.toLowerCase(), blockedCommandEntry);
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     CommandBlockerEntry blockedCommandEntry;
/* 100 */     TFM_Log.info("Loaded " + BLOCKED_COMMANDS.size() + " blocked commands");
/*     */   }
/*     */   
/*     */   public static boolean isCommandBlocked(String command, CommandSender sender)
/*     */   {
/* 105 */     return isCommandBlocked(command, sender, false);
/*     */   }
/*     */   
/*     */   public static boolean isCommandBlocked(String command, CommandSender sender, boolean doAction)
/*     */   {
/* 110 */     if ((command == null) || (command.isEmpty())) {
/* 112 */       return false;
/*     */     }
/* 115 */     command = command.toLowerCase().trim();
/* 117 */     if (command.split(" ")[0].contains(":"))
/*     */     {
/* 119 */       TFM_Util.playerMsg(sender, "Plugin-specific commands are disabled.");
/* 120 */       return true;
/*     */     }
/* 123 */     if (command.startsWith("/")) {
/* 125 */       command = command.substring(1);
/*     */     }
/* 128 */     String[] commandParts = command.split(" ");
/* 129 */     String subCommand = null;
/* 130 */     if (commandParts.length > 1) {
/* 132 */       subCommand = StringUtils.join(commandParts, " ", 1, commandParts.length).toLowerCase();
/*     */     }
/* 135 */     CommandBlockerEntry entry = (CommandBlockerEntry)BLOCKED_COMMANDS.get(commandParts[0]);
/* 137 */     if (entry == null) {
/* 139 */       return false;
/*     */     }
/* 142 */     if (entry.getSubCommand() != null) {
/* 144 */       if ((subCommand == null) || (!subCommand.startsWith(entry.getSubCommand()))) {
/* 146 */         return false;
/*     */       }
/*     */     }
/* 150 */     if (entry.getRank().hasPermission(sender)) {
/* 152 */       return false;
/*     */     }
/* 155 */     if (doAction) {
/* 157 */       entry.doActions(sender);
/*     */     }
/* 160 */     return true;
/*     */   }
/*     */   
/*     */   public static enum CommandBlockerRank
/*     */   {
/* 165 */     ANYONE("a", 0),  OP("o", 1),  SUPER("s", 2),  TELNET("t", 3),  SENIOR("c", 4),  NOBODY("n", 5);
/*     */     
/*     */     private final String token;
/*     */     private final int level;
/*     */     
/*     */     private CommandBlockerRank(String token, int level)
/*     */     {
/* 177 */       this.token = token;
/* 178 */       this.level = level;
/*     */     }
/*     */     
/*     */     public String getToken()
/*     */     {
/* 183 */       return token;
/*     */     }
/*     */     
/*     */     public boolean hasPermission(CommandSender sender)
/*     */     {
/* 188 */       return fromSenderlevel >= level;
/*     */     }
/*     */     
/*     */     public static CommandBlockerRank fromSender(CommandSender sender)
/*     */     {
/* 193 */       if (!(sender instanceof Player)) {
/* 195 */         return TELNET;
/*     */       }
/* 198 */       if (TFM_AdminList.isSuperAdmin(sender))
/*     */       {
/* 200 */         if (TFM_AdminList.isSeniorAdmin(sender)) {
/* 202 */           return SENIOR;
/*     */         }
/* 204 */         return SUPER;
/*     */       }
/* 207 */       if (sender.isOp()) {
/* 209 */         return OP;
/*     */       }
/* 212 */       return ANYONE;
/*     */     }
/*     */     
/*     */     public static CommandBlockerRank fromToken(String token)
/*     */     {
/* 218 */       for (CommandBlockerRank rank : ) {
/* 220 */         if (rank.getToken().equalsIgnoreCase(token)) {
/* 222 */           return rank;
/*     */         }
/*     */       }
/* 225 */       return ANYONE;
/*     */     }
/*     */   }
/*     */   
/*     */   public static enum CommandBlockerAction
/*     */   {
/* 231 */     BLOCK("b"),  BLOCK_AND_EJECT("a"),  BLOCK_UNKNOWN("u");
/*     */     
/*     */     private final String token;
/*     */     
/*     */     private CommandBlockerAction(String token)
/*     */     {
/* 238 */       this.token = token;
/*     */     }
/*     */     
/*     */     public String getToken()
/*     */     {
/* 243 */       return token;
/*     */     }
/*     */     
/*     */     public static CommandBlockerAction fromToken(String token)
/*     */     {
/* 248 */       for (CommandBlockerAction action : ) {
/* 250 */         if (action.getToken().equalsIgnoreCase(token)) {
/* 252 */           return action;
/*     */         }
/*     */       }
/* 255 */       return null;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class CommandBlockerEntry
/*     */   {
/*     */     private final TFM_CommandBlocker.CommandBlockerRank rank;
/*     */     private final TFM_CommandBlocker.CommandBlockerAction action;
/*     */     private final String command;
/*     */     private final String subCommand;
/*     */     private final String message;
/*     */     
/*     */     private CommandBlockerEntry(TFM_CommandBlocker.CommandBlockerRank rank, TFM_CommandBlocker.CommandBlockerAction action, String command, String message)
/*     */     {
/* 269 */       this(rank, action, command, null, message);
/*     */     }
/*     */     
/*     */     private CommandBlockerEntry(TFM_CommandBlocker.CommandBlockerRank rank, TFM_CommandBlocker.CommandBlockerAction action, String command, String subCommand, String message)
/*     */     {
/* 274 */       this.rank = rank;
/* 275 */       this.action = action;
/* 276 */       this.command = command;
/* 277 */       this.subCommand = (subCommand == null ? null : subCommand.toLowerCase().trim());
/* 278 */       this.message = ((message == null) || (message.equals("_")) ? "That command is blocked." : message);
/*     */     }
/*     */     
/*     */     public TFM_CommandBlocker.CommandBlockerAction getAction()
/*     */     {
/* 283 */       return action;
/*     */     }
/*     */     
/*     */     public String getCommand()
/*     */     {
/* 288 */       return command;
/*     */     }
/*     */     
/*     */     public String getSubCommand()
/*     */     {
/* 293 */       return subCommand;
/*     */     }
/*     */     
/*     */     public String getMessage()
/*     */     {
/* 298 */       return message;
/*     */     }
/*     */     
/*     */     public TFM_CommandBlocker.CommandBlockerRank getRank()
/*     */     {
/* 303 */       return rank;
/*     */     }
/*     */     
/*     */     private void doActions(CommandSender sender)
/*     */     {
/* 308 */       if ((action == TFM_CommandBlocker.CommandBlockerAction.BLOCK_AND_EJECT) && ((sender instanceof Player)))
/*     */       {
/* 310 */         TFM_Util.autoEject((Player)sender, "You used a prohibited command: " + command);
/* 311 */         TFM_Util.bcastMsg(sender.getName() + " was automatically kicked for using harmful commands.", ChatColor.RED);
/* 312 */         return;
/*     */       }
/* 315 */       if (action == TFM_CommandBlocker.CommandBlockerAction.BLOCK_UNKNOWN)
/*     */       {
/* 317 */         TFM_Util.playerMsg(sender, "Unknown command. Type \"help\" for help.", ChatColor.RESET);
/* 318 */         return;
/*     */       }
/* 321 */       TFM_Util.playerMsg(sender, TFM_Util.colorize(message));
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\SilentBloodGaming\Desktop\MasterFreedomMod.jar!\me\StevenLawson\TotalFreedomMod\TFM_CommandBlocker.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */