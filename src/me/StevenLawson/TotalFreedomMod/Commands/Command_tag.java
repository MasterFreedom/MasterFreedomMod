/*     */ package me.StevenLawson.TotalFreedomMod.Commands;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import me.StevenLawson.TotalFreedomMod.TFM_AdminList;
/*     */ import me.StevenLawson.TotalFreedomMod.TFM_PlayerData;
/*     */ import me.StevenLawson.TotalFreedomMod.TFM_Util;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ import org.bukkit.ChatColor;
/*     */ import org.bukkit.Server;
/*     */ import org.bukkit.command.Command;
/*     */ import org.bukkit.command.CommandSender;
/*     */ import org.bukkit.entity.Player;
/*     */ 
/*     */ @CommandPermissions(level=AdminLevel.OP, source=SourceType.BOTH)
/*     */ @CommandParameters(description="Sets yourself a prefix", usage="/<command> <set <tag..> | off | clear <player> | clearall>")
/*     */ public class Command_tag
/*     */   extends TFM_Command
/*     */ {
/*  18 */   public static final List<String> FORBIDDEN_WORDS = Arrays.asList(new String[] { "admin", "owner", "moderator", "developer", "console" });
/*     */   
/*     */   public boolean run(CommandSender sender, Player sender_p, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
/*     */   {
/*  26 */     if (args.length == 1)
/*     */     {
/*  28 */       if ("list".equalsIgnoreCase(args[0]))
/*     */       {
/*  30 */         playerMsg("Tags for all online players:");
/*  32 */         for (Player player : server.getOnlinePlayers())
/*     */         {
/*  34 */           TFM_PlayerData playerdata = TFM_PlayerData.getPlayerData(player);
/*  35 */           if (playerdata.getTag() != null) {
/*  37 */             playerMsg(player.getName() + ": " + playerdata.getTag());
/*     */           }
/*     */         }
/*  41 */         return true;
/*     */       }
/*  43 */       if ("clearall".equalsIgnoreCase(args[0]))
/*     */       {
/*  45 */         if (!TFM_AdminList.isSuperAdmin(sender))
/*     */         {
/*  47 */           playerMsg(TFM_Command.MSG_NO_PERMS);
/*  48 */           return true;
/*     */         }
/*  51 */         TFM_Util.adminAction(sender.getName(), "Removing all tags", false);
/*     */         
/*  53 */         int count = 0;
/*  54 */         for (Player player : server.getOnlinePlayers())
/*     */         {
/*  56 */           TFM_PlayerData playerdata = TFM_PlayerData.getPlayerData(player);
/*  57 */           if (playerdata.getTag() != null)
/*     */           {
/*  59 */             count++;
/*  60 */             playerdata.setTag(null);
/*     */           }
/*     */         }
/*  64 */         playerMsg(count + " tag(s) removed.");
/*     */         
/*  66 */         return true;
/*     */       }
/*  68 */       if ("off".equalsIgnoreCase(args[0]))
/*     */       {
/*  70 */         if (senderIsConsole)
/*     */         {
/*  72 */           playerMsg("\"/tag off\" can't be used from the console. Use \"/tag clear <player>\" or \"/tag clearall\" instead.");
/*     */         }
/*     */         else
/*     */         {
/*  76 */           TFM_PlayerData.getPlayerData(sender_p).setTag(null);
/*  77 */           playerMsg("Your tag has been removed.");
/*     */         }
/*  80 */         return true;
/*     */       }
/*  84 */       return false;
/*     */     }
/*  87 */     if (args.length >= 2)
/*     */     {
/*  89 */       if ("clear".equalsIgnoreCase(args[0]))
/*     */       {
/*  91 */         if (!TFM_AdminList.isSuperAdmin(sender))
/*     */         {
/*  93 */           playerMsg(TFM_Command.MSG_NO_PERMS);
/*  94 */           return true;
/*     */         }
/*  97 */         Player player = getPlayer(args[1]);
/*  99 */         if (player == null)
/*     */         {
/* 101 */           playerMsg(TFM_Command.PLAYER_NOT_FOUND);
/* 102 */           return true;
/*     */         }
/* 105 */         TFM_PlayerData.getPlayerData(player).setTag(null);
/* 106 */         playerMsg("Removed " + player.getName() + "'s tag.");
/*     */         
/* 108 */         return true;
/*     */       }
/* 110 */       if ("set".equalsIgnoreCase(args[0]))
/*     */       {
/* 112 */         String inputTag = StringUtils.join(args, " ", 1, args.length);
/* 113 */         String outputTag = TFM_Util.colorize(StringUtils.replaceEachRepeatedly(StringUtils.strip(inputTag), new String[] { "§", "&k" }, new String[] { "", "" })) + ChatColor.RESET;
/*     */         String rawTag;
/* 123 */         if (!TFM_AdminList.isSuperAdmin(sender))
/*     */         {
/* 125 */           rawTag = ChatColor.stripColor(outputTag).toLowerCase();
/* 127 */           if (rawTag.length() > 20)
/*     */           {
/* 129 */             playerMsg("That tag is too long (Max is 20 characters).");
/* 130 */             return true;
/*     */           }
/* 133 */           for (String word : FORBIDDEN_WORDS) {
/* 135 */             if (rawTag.contains(word))
/*     */             {
/* 137 */               playerMsg("That tag contains a forbidden word.");
/* 138 */               return true;
/*     */             }
/*     */           }
/*     */         }
/* 143 */         TFM_PlayerData.getPlayerData(sender_p).setTag(outputTag);
/* 144 */         playerMsg("Tag set to '" + outputTag + "'.");
/*     */         
/* 146 */         return true;
/*     */       }
/* 150 */       return false;
/*     */     }
/* 155 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\SilentBloodGaming\Desktop\MasterFreedomMod.jar!\me\StevenLawson\TotalFreedomMod\Commands\Command_tag.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */