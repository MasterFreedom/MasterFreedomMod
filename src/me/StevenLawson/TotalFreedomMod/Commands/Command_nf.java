/*     */ package me.StevenLawson.TotalFreedomMod.Commands;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import me.StevenLawson.TotalFreedomMod.TFM_CommandBlocker;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ import org.bukkit.Bukkit;
/*     */ import org.bukkit.ChatColor;
/*     */ import org.bukkit.Server;
/*     */ import org.bukkit.command.Command;
/*     */ import org.bukkit.command.CommandSender;
/*     */ import org.bukkit.entity.Player;
/*     */ 
/*     */ @CommandPermissions(level=AdminLevel.OP, source=SourceType.BOTH)
/*     */ @CommandParameters(description="NickFilter: Prefix any command with this command to replace nicknames in that command with real names. Nicknames should be prefixed with a !.", usage="/<command> <other_command> !<playernick>")
/*     */ public class Command_nf
/*     */   extends TFM_Command
/*     */ {
/*     */   public boolean run(CommandSender sender, Player sender_p, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
/*     */   {
/*  23 */     boolean nickMatched = false;
/*     */     
/*  25 */     List<String> outputCommand = new ArrayList();
/*  27 */     if (args.length >= 1)
/*     */     {
/*  29 */       List<String> argsList = Arrays.asList(args);
/*  30 */       for (String arg : argsList)
/*     */       {
/*  32 */         Player player = null;
/*     */         
/*  34 */         Matcher matcher = Pattern.compile("^!(.+)$").matcher(arg);
/*  35 */         if (matcher.find())
/*     */         {
/*  37 */           String displayName = matcher.group(1);
/*     */           
/*  39 */           player = getPlayerByDisplayName(displayName);
/*  41 */           if (player == null)
/*     */           {
/*  43 */             player = getPlayerByDisplayNameAlt(displayName);
/*  45 */             if (player == null)
/*     */             {
/*  47 */               sender.sendMessage(ChatColor.GRAY + "Can't find player by nickname: " + displayName);
/*  48 */               return true;
/*     */             }
/*     */           }
/*     */         }
/*  53 */         if (player == null)
/*     */         {
/*  55 */           outputCommand.add(arg);
/*     */         }
/*     */         else
/*     */         {
/*  59 */           nickMatched = true;
/*  60 */           outputCommand.add(player.getName());
/*     */         }
/*     */       }
/*     */     }
/*  65 */     if (!nickMatched)
/*     */     {
/*  67 */       sender.sendMessage("No nicknames replaced in command.");
/*  68 */       return true;
/*     */     }
/*  71 */     String newCommand = StringUtils.join(outputCommand, " ");
/*  73 */     if (TFM_CommandBlocker.isCommandBlocked(newCommand, sender)) {
/*  76 */       return true;
/*     */     }
/*  79 */     sender.sendMessage("Sending command: \"" + newCommand + "\".");
/*  80 */     server.dispatchCommand(sender, newCommand);
/*     */     
/*  82 */     return true;
/*     */   }
/*     */   
/*     */   private static Player getPlayerByDisplayName(String needle)
/*     */   {
/*  87 */     needle = needle.toLowerCase().trim();
/*  89 */     for (Player player : Bukkit.getOnlinePlayers()) {
/*  91 */       if (player.getDisplayName().toLowerCase().trim().contains(needle)) {
/*  93 */         return player;
/*     */       }
/*     */     }
/*  97 */     return null;
/*     */   }
/*     */   
/*     */   private static Player getPlayerByDisplayNameAlt(String needle)
/*     */   {
/* 102 */     needle = needle.toLowerCase().trim();
/*     */     
/* 104 */     Integer minEditDistance = null;
/* 105 */     Player minEditMatch = null;
/* 107 */     for (Player player : Bukkit.getOnlinePlayers())
/*     */     {
/* 109 */       String haystack = player.getDisplayName().toLowerCase().trim();
/* 110 */       int editDistance = StringUtils.getLevenshteinDistance(needle, haystack.toLowerCase());
/* 111 */       if ((minEditDistance == null) || (minEditDistance.intValue() > editDistance))
/*     */       {
/* 113 */         minEditDistance = Integer.valueOf(editDistance);
/* 114 */         minEditMatch = player;
/*     */       }
/*     */     }
/* 118 */     return minEditMatch;
/*     */   }
/*     */ }


/* Location:              C:\Users\SilentBloodGaming\Desktop\MasterFreedomMod.jar!\me\StevenLawson\TotalFreedomMod\Commands\Command_nf.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */