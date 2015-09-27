/*     */ package me.StevenLawson.TotalFreedomMod.Commands;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import me.StevenLawson.TotalFreedomMod.TFM_CommandBlocker;
/*     */ import me.StevenLawson.TotalFreedomMod.TFM_DepreciationAggregator;
/*     */ import me.StevenLawson.TotalFreedomMod.TFM_Log;
/*     */ import org.apache.commons.lang3.ArrayUtils;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ import org.bukkit.Bukkit;
/*     */ import org.bukkit.Location;
/*     */ import org.bukkit.Server;
/*     */ import org.bukkit.World;
/*     */ import org.bukkit.block.Block;
/*     */ import org.bukkit.command.Command;
/*     */ import org.bukkit.command.CommandSender;
/*     */ import org.bukkit.entity.Player;
/*     */ 
/*     */ @CommandPermissions(level=AdminLevel.ALL, source=SourceType.BOTH)
/*     */ @CommandParameters(description="No Description Yet", usage="/<command>")
/*     */ public class Command_cbtool
/*     */   extends TFM_Command
/*     */ {
/*     */   public boolean run(CommandSender sender, Player sender_p, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
/*     */   {
/*  26 */     if (args.length < 1) {
/*  28 */       return false;
/*     */     }
/*  31 */     if (("targetblock".equalsIgnoreCase(args[0])) && ((sender instanceof Player)))
/*     */     {
/*  33 */       Block targetBlock = TFM_DepreciationAggregator.getTargetBlock(sender_p, null, 100);
/*  34 */       playerMsg("Your target block: " + targetBlock.getLocation().toString());
/*  35 */       return true;
/*     */     }
/*     */     try
/*     */     {
/*  40 */       StringBuffer generatedCommand = new StringBuffer();
/*     */       
/*  42 */       Matcher matcher = Pattern.compile("\\[(.+?)\\]").matcher(StringUtils.join(args, " ").trim());
/*  43 */       while (matcher.find()) {
/*  45 */         matcher.appendReplacement(generatedCommand, processSubCommand(matcher.group(1)));
/*     */       }
/*  47 */       matcher.appendTail(generatedCommand);
/*  49 */       if (TFM_CommandBlocker.isCommandBlocked(generatedCommand.toString(), sender, false)) {
/*  51 */         return true;
/*     */       }
/*  54 */       server.dispatchCommand(sender, generatedCommand.toString());
/*     */     }
/*     */     catch (SubCommandFailureException ex) {}catch (Exception ex)
/*     */     {
/*  61 */       TFM_Log.severe(ex);
/*     */     }
/*  64 */     return true;
/*     */   }
/*     */   
/*     */   private String processSubCommand(String subcommand)
/*     */     throws Command_cbtool.SubCommandFailureException
/*     */   {
/*  69 */     String[] args = StringUtils.split(subcommand, " ");
/*  71 */     if (args.length == 1) {
/*  73 */       throw new SubCommandFailureException("Invalid subcommand name.");
/*     */     }
/*  76 */     return SubCommand.getByName(args[0]).getExecutable().execute((String[])ArrayUtils.remove(args, 0));
/*     */   }
/*     */   
/*     */   private static enum SubCommand
/*     */   {
/*  81 */     PLAYER_DETECT("playerdetect", new Command_cbtool.SubCommandExecutable()),  PLAYER_DETECT_BOOLEAN("playerdetectboolean", new Command_cbtool.SubCommandExecutable());
/*     */     
/*     */     private final String name;
/*     */     private final Command_cbtool.SubCommandExecutable executable;
/*     */     
/*     */     private SubCommand(String subCommandName, Command_cbtool.SubCommandExecutable subCommandImpl)
/*     */     {
/* 169 */       name = subCommandName;
/* 170 */       executable = subCommandImpl;
/*     */     }
/*     */     
/*     */     public Command_cbtool.SubCommandExecutable getExecutable()
/*     */     {
/* 175 */       return executable;
/*     */     }
/*     */     
/*     */     public String getName()
/*     */     {
/* 180 */       return name;
/*     */     }
/*     */     
/*     */     public static SubCommand getByName(String needle)
/*     */       throws Command_cbtool.SubCommandFailureException
/*     */     {
/* 185 */       needle = needle.trim();
/* 186 */       for (SubCommand subCommand : values()) {
/* 188 */         if (subCommand.getName().equalsIgnoreCase(needle)) {
/* 190 */           return subCommand;
/*     */         }
/*     */       }
/* 193 */       throw new Command_cbtool.SubCommandFailureException("Invalid subcommand name.");
/*     */     }
/*     */   }
/*     */   
/*     */   private static abstract interface SubCommandExecutable
/*     */   {
/*     */     public abstract String execute(String[] paramArrayOfString)
/*     */       throws Command_cbtool.SubCommandFailureException;
/*     */   }
/*     */   
/*     */   private static class SubCommandFailureException
/*     */     extends Exception
/*     */   {
/*     */     public SubCommandFailureException() {}
/*     */     
/*     */     public SubCommandFailureException(String message)
/*     */     {
/* 210 */       super();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\SilentBloodGaming\Desktop\MasterFreedomMod.jar!\me\StevenLawson\TotalFreedomMod\Commands\Command_cbtool.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */