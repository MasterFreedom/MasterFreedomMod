/*     */ package me.StevenLawson.TotalFreedomMod;
/*     */ 
/*     */ import java.util.List;
/*     */ import me.StevenLawson.TotalFreedomMod.Config.TFM_ConfigEntry;
/*     */ import org.bukkit.ChatColor;
/*     */ import org.bukkit.command.CommandSender;
/*     */ import org.bukkit.entity.Player;
/*     */ 
/*     */ public enum TFM_PlayerRank
/*     */ {
/*  11 */   DEVELOPER("a " + ChatColor.DARK_PURPLE + "Developer", ChatColor.DARK_PURPLE + "[Dev]"),  IMPOSTOR("an " + ChatColor.YELLOW + ChatColor.UNDERLINE + "Impostor", ChatColor.YELLOW.toString() + ChatColor.UNDERLINE + "[IMP]"),  NON_OP("a " + ChatColor.GREEN + "Non-OP", ChatColor.GREEN.toString()),  OP("an " + ChatColor.RED + "OP", ChatColor.RED + "[OP]"),  SUPER("a " + ChatColor.GOLD + "Super Admin", ChatColor.GOLD + "[SA]"),  TELNET("a " + ChatColor.DARK_GREEN + "Super Telnet Admin", ChatColor.DARK_GREEN + "[STA]"),  SENIOR("a " + ChatColor.LIGHT_PURPLE + "Senior Admin", ChatColor.LIGHT_PURPLE + "[SrA]"),  OWNER("the " + ChatColor.BLUE + "Owner", ChatColor.BLUE + "[Owner]"),  CONSOLE("The " + ChatColor.DARK_PURPLE + "Console", ChatColor.DARK_PURPLE + "[Console]");
/*     */   
/*     */   private final String loginMessage;
/*     */   private final String prefix;
/*     */   
/*     */   private TFM_PlayerRank(String loginMessage, String prefix)
/*     */   {
/*  25 */     this.loginMessage = loginMessage;
/*  26 */     this.prefix = prefix;
/*     */   }
/*     */   
/*     */   public static String getLoginMessage(CommandSender sender)
/*     */   {
/*  32 */     if (!(sender instanceof Player)) {
/*  34 */       return fromSender(sender).getLoginMessage();
/*     */     }
/*  38 */     TFM_Admin entry = TFM_AdminList.getEntry((Player)sender);
/*  39 */     if (entry == null) {
/*  42 */       return fromSender(sender).getLoginMessage();
/*     */     }
/*  46 */     String loginMessage = entry.getCustomLoginMessage();
/*  48 */     if ((loginMessage == null) || (loginMessage.isEmpty())) {
/*  50 */       return fromSender(sender).getLoginMessage();
/*     */     }
/*  53 */     return ChatColor.translateAlternateColorCodes('&', loginMessage);
/*     */   }
/*     */   
/*     */   public static TFM_PlayerRank fromSender(CommandSender sender)
/*     */   {
/*  58 */     if (!(sender instanceof Player)) {
/*  60 */       return CONSOLE;
/*     */     }
/*  63 */     if (TFM_AdminList.isAdminImpostor((Player)sender)) {
/*  65 */       return IMPOSTOR;
/*     */     }
/*  68 */     if (TFM_Util.DEVELOPERS.contains(sender.getName())) {
/*  70 */       return DEVELOPER;
/*     */     }
/*  73 */     TFM_Admin entry = TFM_AdminList.getEntryByIp(TFM_Util.getIp((Player)sender));
/*     */     TFM_PlayerRank rank;
/*     */     TFM_PlayerRank rank;
/*  77 */     if ((entry != null) && (entry.isActivated()))
/*     */     {
/*  79 */       if (TFM_ConfigEntry.SERVER_OWNERS.getList().contains(sender.getName())) {
/*  81 */         return OWNER;
/*     */       }
/*     */       TFM_PlayerRank rank;
/*  84 */       if (entry.isSeniorAdmin())
/*     */       {
/*  86 */         rank = SENIOR;
/*     */       }
/*     */       else
/*     */       {
/*     */         TFM_PlayerRank rank;
/*  88 */         if (entry.isTelnetAdmin()) {
/*  90 */           rank = TELNET;
/*     */         } else {
/*  94 */           rank = SUPER;
/*     */         }
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/*     */       TFM_PlayerRank rank;
/*  99 */       if (sender.isOp()) {
/* 101 */         rank = OP;
/*     */       } else {
/* 105 */         rank = NON_OP;
/*     */       }
/*     */     }
/* 109 */     return rank;
/*     */   }
/*     */   
/*     */   public String getPrefix()
/*     */   {
/* 114 */     return prefix;
/*     */   }
/*     */   
/*     */   public String getLoginMessage()
/*     */   {
/* 119 */     return loginMessage;
/*     */   }
/*     */ }


/* Location:              C:\Users\SilentBloodGaming\Desktop\MasterFreedomMod.jar!\me\StevenLawson\TotalFreedomMod\TFM_PlayerRank.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */