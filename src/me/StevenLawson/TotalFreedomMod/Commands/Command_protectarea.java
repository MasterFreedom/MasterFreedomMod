/*     */ package me.StevenLawson.TotalFreedomMod.Commands;
/*     */ 
/*     */ import me.StevenLawson.TotalFreedomMod.Config.TFM_ConfigEntry;
/*     */ import me.StevenLawson.TotalFreedomMod.TFM_ProtectedArea;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ import org.bukkit.command.Command;
/*     */ import org.bukkit.command.CommandSender;
/*     */ import org.bukkit.entity.Player;
/*     */ 
/*     */ @CommandPermissions(level=AdminLevel.SUPER, source=SourceType.BOTH)
/*     */ @CommandParameters(description="Protect areas so that only superadmins can directly modify blocks in those areas. WorldEdit and other such plugins might bypass this.", usage="/<command> <list | clear | remove <label> | add <label> <radius>>")
/*     */ public class Command_protectarea
/*     */   extends TFM_Command
/*     */ {
/*     */   public boolean run(CommandSender sender, Player sender_p, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
/*     */   {
/*  20 */     if (!TFM_ConfigEntry.PROTECTAREA_ENABLED.getBoolean().booleanValue())
/*     */     {
/*  22 */       playerMsg("Protected areas are currently disabled in the TotalFreedomMod configuration.");
/*  23 */       return true;
/*     */     }
/*  26 */     if (args.length == 1)
/*     */     {
/*  28 */       if (args[0].equalsIgnoreCase("list"))
/*     */       {
/*  30 */         playerMsg("Protected Areas: " + StringUtils.join(TFM_ProtectedArea.getProtectedAreaLabels(), ", "));
/*     */       }
/*  32 */       else if (args[0].equalsIgnoreCase("clear"))
/*     */       {
/*  34 */         TFM_ProtectedArea.clearProtectedAreas();
/*     */         
/*  36 */         playerMsg("Protected Areas Cleared.");
/*     */       }
/*     */       else
/*     */       {
/*  40 */         return false;
/*     */       }
/*  43 */       return true;
/*     */     }
/*  45 */     if (args.length == 2)
/*     */     {
/*  47 */       if ("remove".equals(args[0]))
/*     */       {
/*  49 */         TFM_ProtectedArea.removeProtectedArea(args[1]);
/*     */         
/*  51 */         playerMsg("Area removed. Protected Areas: " + StringUtils.join(TFM_ProtectedArea.getProtectedAreaLabels(), ", "));
/*     */       }
/*     */       else
/*     */       {
/*  55 */         return false;
/*     */       }
/*  58 */       return true;
/*     */     }
/*  60 */     if (args.length == 3)
/*     */     {
/*  62 */       if (args[0].equalsIgnoreCase("add"))
/*     */       {
/*  64 */         if (senderIsConsole)
/*     */         {
/*  66 */           playerMsg("You must be in-game to set a protected area.");
/*  67 */           return true;
/*     */         }
/*     */         Double radius;
/*     */         try
/*     */         {
/*  73 */           radius = Double.valueOf(Double.parseDouble(args[2]));
/*     */         }
/*     */         catch (NumberFormatException nfex)
/*     */         {
/*  77 */           playerMsg("Invalid radius.");
/*  78 */           return true;
/*     */         }
/*  81 */         if ((radius.doubleValue() > 50.0D) || (radius.doubleValue() < 0.0D))
/*     */         {
/*  83 */           playerMsg("Invalid radius. Radius must be a positive value less than 50.0.");
/*  84 */           return true;
/*     */         }
/*  87 */         TFM_ProtectedArea.addProtectedArea(args[1], sender_p.getLocation(), radius.doubleValue());
/*     */         
/*  89 */         playerMsg("Area added. Protected Areas: " + StringUtils.join(TFM_ProtectedArea.getProtectedAreaLabels(), ", "));
/*     */       }
/*     */       else
/*     */       {
/*  93 */         return false;
/*     */       }
/*  96 */       return true;
/*     */     }
/* 100 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\SilentBloodGaming\Desktop\MasterFreedomMod.jar!\me\StevenLawson\TotalFreedomMod\Commands\Command_protectarea.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */