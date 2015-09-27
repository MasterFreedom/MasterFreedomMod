/*     */ package me.StevenLawson.TotalFreedomMod.Commands;
/*     */ 
/*     */ import me.StevenLawson.TotalFreedomMod.Config.TFM_ConfigEntry;
/*     */ import me.StevenLawson.TotalFreedomMod.TFM_GameRuleHandler;
/*     */ import me.StevenLawson.TotalFreedomMod.TFM_GameRuleHandler.TFM_GameRule;
/*     */ import me.StevenLawson.TotalFreedomMod.TFM_Util;
/*     */ import me.StevenLawson.TotalFreedomMod.TotalFreedomMod;
/*     */ import org.bukkit.command.Command;
/*     */ import org.bukkit.command.CommandSender;
/*     */ import org.bukkit.entity.Player;
/*     */ 
/*     */ @CommandPermissions(level=AdminLevel.SUPER, source=SourceType.BOTH)
/*     */ @CommandParameters(description="Toggles TotalFreedomMod settings", usage="/<command> [option] [value] [value]")
/*     */ public class Command_toggle
/*     */   extends TFM_Command
/*     */ {
/*     */   public boolean run(CommandSender sender, Player sender_p, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
/*     */   {
/*  19 */     if (args.length == 0)
/*     */     {
/*  21 */       playerMsg("Available toggles: ");
/*  22 */       playerMsg("- waterplace");
/*  23 */       playerMsg("- fireplace");
/*  24 */       playerMsg("- lavaplace");
/*  25 */       playerMsg("- fluidspread");
/*  26 */       playerMsg("- lavadmg");
/*  27 */       playerMsg("- firespread");
/*  28 */       playerMsg("- prelog");
/*  29 */       playerMsg("- lockdown");
/*  30 */       playerMsg("- petprotect");
/*  31 */       playerMsg("- droptoggle");
/*  32 */       playerMsg("- nonuke");
/*  33 */       playerMsg("- explosives");
/*  34 */       return false;
/*     */     }
/*  37 */     if (args[0].equals("waterplace"))
/*     */     {
/*  39 */       toggle("Water placement is", TFM_ConfigEntry.ALLOW_WATER_PLACE);
/*  40 */       return true;
/*     */     }
/*  43 */     if (args[0].equals("fireplace"))
/*     */     {
/*  45 */       toggle("Fire placement is", TFM_ConfigEntry.ALLOW_FIRE_PLACE);
/*  46 */       return true;
/*     */     }
/*  49 */     if (args[0].equals("lavaplace"))
/*     */     {
/*  51 */       toggle("Lava placement is", TFM_ConfigEntry.ALLOW_LAVA_PLACE);
/*  52 */       return true;
/*     */     }
/*  55 */     if (args[0].equals("fluidspread"))
/*     */     {
/*  57 */       toggle("Fluid spread is", TFM_ConfigEntry.ALLOW_FLUID_SPREAD);
/*  58 */       return true;
/*     */     }
/*  61 */     if (args[0].equals("lavadmg"))
/*     */     {
/*  63 */       toggle("Lava damage is", TFM_ConfigEntry.ALLOW_LAVA_DAMAGE);
/*  64 */       return true;
/*     */     }
/*  67 */     if (args[0].equals("firespread"))
/*     */     {
/*  69 */       toggle("Fire spread is", TFM_ConfigEntry.ALLOW_FIRE_SPREAD);
/*  70 */       TFM_GameRuleHandler.setGameRule(TFM_GameRuleHandler.TFM_GameRule.DO_FIRE_TICK, TFM_ConfigEntry.ALLOW_FIRE_SPREAD.getBoolean().booleanValue());
/*  71 */       return true;
/*     */     }
/*  74 */     if (args[0].equals("prelog"))
/*     */     {
/*  76 */       toggle("Command prelogging is", TFM_ConfigEntry.ENABLE_PREPROCESS_LOG);
/*  77 */       return true;
/*     */     }
/*  80 */     if (args[0].equals("lockdown"))
/*     */     {
/*  82 */       TFM_Util.adminAction(sender.getName(), (TotalFreedomMod.lockdownEnabled ? "De-a" : "A") + "ctivating server lockdown", true);
/*  83 */       TotalFreedomMod.lockdownEnabled = !TotalFreedomMod.lockdownEnabled;
/*  84 */       return true;
/*     */     }
/*  87 */     if (args[0].equals("petprotect"))
/*     */     {
/*  89 */       toggle("Tamed pet protection is", TFM_ConfigEntry.ENABLE_PET_PROTECT);
/*  90 */       return true;
/*     */     }
/*  93 */     if (args[0].equals("droptoggle"))
/*     */     {
/*  95 */       toggle("Automatic entity wiping is", TFM_ConfigEntry.AUTO_ENTITY_WIPE);
/*  96 */       return true;
/*     */     }
/*  99 */     if (args[0].equals("nonuke"))
/*     */     {
/* 101 */       if (args.length >= 2) {
/*     */         try
/*     */         {
/* 105 */           TFM_ConfigEntry.NUKE_MONITOR_RANGE.setDouble(Double.valueOf(Math.max(1.0D, Math.min(500.0D, Double.parseDouble(args[1])))));
/*     */         }
/*     */         catch (NumberFormatException nfex) {}
/*     */       }
/* 112 */       if (args.length >= 3) {
/*     */         try
/*     */         {
/* 116 */           TFM_ConfigEntry.NUKE_MONITOR_COUNT_BREAK.setInteger(Integer.valueOf(Math.max(1, Math.min(500, Integer.parseInt(args[2])))));
/*     */         }
/*     */         catch (NumberFormatException nfex) {}
/*     */       }
/* 123 */       toggle("Nuke monitor is", TFM_ConfigEntry.NUKE_MONITOR_ENABLED);
/* 125 */       if (TFM_ConfigEntry.NUKE_MONITOR_ENABLED.getBoolean().booleanValue())
/*     */       {
/* 127 */         playerMsg("Anti-freecam range is set to " + TFM_ConfigEntry.NUKE_MONITOR_RANGE.getDouble() + " blocks.");
/* 128 */         playerMsg("Block throttle rate is set to " + TFM_ConfigEntry.NUKE_MONITOR_COUNT_BREAK.getInteger() + " blocks destroyed per 5 seconds.");
/*     */       }
/* 131 */       return true;
/*     */     }
/* 133 */     if (args[0].equals("explosives"))
/*     */     {
/* 135 */       if (args.length == 2) {
/*     */         try
/*     */         {
/* 139 */           TFM_ConfigEntry.EXPLOSIVE_RADIUS.setDouble(Double.valueOf(Math.max(1.0D, Math.min(30.0D, Double.parseDouble(args[1])))));
/*     */         }
/*     */         catch (NumberFormatException ex)
/*     */         {
/* 143 */           playerMsg(ex.getMessage());
/* 144 */           return true;
/*     */         }
/*     */       }
/* 148 */       toggle("Explosions are", TFM_ConfigEntry.ALLOW_EXPLOSIONS);
/* 150 */       if (TFM_ConfigEntry.ALLOW_EXPLOSIONS.getBoolean().booleanValue()) {
/* 152 */         playerMsg("Radius set to " + TFM_ConfigEntry.EXPLOSIVE_RADIUS.getDouble());
/*     */       }
/* 154 */       return true;
/*     */     }
/* 157 */     return false;
/*     */   }
/*     */   
/*     */   private void toggle(String name, TFM_ConfigEntry entry)
/*     */   {
/* 162 */     playerMsg(name + " now " + (entry.setBoolean(Boolean.valueOf(!entry.getBoolean().booleanValue())).booleanValue() ? "enabled." : "disabled."));
/*     */   }
/*     */ }


/* Location:              C:\Users\SilentBloodGaming\Desktop\MasterFreedomMod.jar!\me\StevenLawson\TotalFreedomMod\Commands\Command_toggle.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */