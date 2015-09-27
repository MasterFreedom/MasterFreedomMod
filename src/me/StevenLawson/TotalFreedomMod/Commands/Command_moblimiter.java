/*    */ package me.StevenLawson.TotalFreedomMod.Commands;
/*    */ 
/*    */ import me.StevenLawson.TotalFreedomMod.Config.TFM_ConfigEntry;
/*    */ import me.StevenLawson.TotalFreedomMod.TFM_GameRuleHandler;
/*    */ import me.StevenLawson.TotalFreedomMod.TFM_GameRuleHandler.TFM_GameRule;
/*    */ import org.bukkit.command.Command;
/*    */ import org.bukkit.command.CommandSender;
/*    */ import org.bukkit.entity.Player;
/*    */ 
/*    */ @CommandPermissions(level=AdminLevel.SUPER, source=SourceType.ONLY_CONSOLE)
/*    */ @CommandParameters(description="Control mob rezzing parameters.", usage="/<command> <on|off|setmax <count>|dragon|giant|ghast|slime>")
/*    */ public class Command_moblimiter
/*    */   extends TFM_Command
/*    */ {
/*    */   public boolean run(CommandSender sender, Player sender_p, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
/*    */   {
/* 16 */     if (args.length < 1) {
/* 18 */       return false;
/*    */     }
/* 21 */     if (args[0].equalsIgnoreCase("on"))
/*    */     {
/* 23 */       TFM_ConfigEntry.MOB_LIMITER_ENABLED.setBoolean(Boolean.valueOf(true));
/*    */     }
/* 25 */     else if (args[0].equalsIgnoreCase("off"))
/*    */     {
/* 27 */       TFM_ConfigEntry.MOB_LIMITER_ENABLED.setBoolean(Boolean.valueOf(false));
/*    */     }
/* 29 */     else if (args[0].equalsIgnoreCase("dragon"))
/*    */     {
/* 31 */       TFM_ConfigEntry.MOB_LIMITER_DISABLE_DRAGON.setBoolean(Boolean.valueOf(!TFM_ConfigEntry.MOB_LIMITER_DISABLE_DRAGON.getBoolean().booleanValue()));
/*    */     }
/* 33 */     else if (args[0].equalsIgnoreCase("giant"))
/*    */     {
/* 35 */       TFM_ConfigEntry.MOB_LIMITER_DISABLE_GIANT.setBoolean(Boolean.valueOf(!TFM_ConfigEntry.MOB_LIMITER_DISABLE_GIANT.getBoolean().booleanValue()));
/*    */     }
/* 37 */     else if (args[0].equalsIgnoreCase("slime"))
/*    */     {
/* 39 */       TFM_ConfigEntry.MOB_LIMITER_DISABLE_SLIME.setBoolean(Boolean.valueOf(!TFM_ConfigEntry.MOB_LIMITER_DISABLE_SLIME.getBoolean().booleanValue()));
/*    */     }
/* 41 */     else if (args[0].equalsIgnoreCase("ghast"))
/*    */     {
/* 43 */       TFM_ConfigEntry.MOB_LIMITER_DISABLE_GHAST.setBoolean(Boolean.valueOf(!TFM_ConfigEntry.MOB_LIMITER_DISABLE_GHAST.getBoolean().booleanValue()));
/*    */     }
/*    */     else
/*    */     {
/* 47 */       if (args.length < 2) {
/* 49 */         return false;
/*    */       }
/* 52 */       if (args[0].equalsIgnoreCase("setmax")) {
/*    */         try
/*    */         {
/* 56 */           TFM_ConfigEntry.MOB_LIMITER_MAX.setInteger(Integer.valueOf(Math.max(0, Math.min(2000, Integer.parseInt(args[1])))));
/*    */         }
/*    */         catch (NumberFormatException nfex) {}
/*    */       }
/*    */     }
/* 64 */     if (TFM_ConfigEntry.MOB_LIMITER_ENABLED.getBoolean().booleanValue())
/*    */     {
/* 66 */       sender.sendMessage("Moblimiter enabled. Maximum mobcount set to: " + TFM_ConfigEntry.MOB_LIMITER_MAX.getInteger() + ".");
/*    */       
/* 68 */       playerMsg("Dragon: " + (TFM_ConfigEntry.MOB_LIMITER_DISABLE_DRAGON.getBoolean().booleanValue() ? "disabled" : "enabled") + ".");
/* 69 */       playerMsg("Giant: " + (TFM_ConfigEntry.MOB_LIMITER_DISABLE_GIANT.getBoolean().booleanValue() ? "disabled" : "enabled") + ".");
/* 70 */       playerMsg("Slime: " + (TFM_ConfigEntry.MOB_LIMITER_DISABLE_SLIME.getBoolean().booleanValue() ? "disabled" : "enabled") + ".");
/* 71 */       playerMsg("Ghast: " + (TFM_ConfigEntry.MOB_LIMITER_DISABLE_GHAST.getBoolean().booleanValue() ? "disabled" : "enabled") + ".");
/*    */     }
/*    */     else
/*    */     {
/* 75 */       playerMsg("Moblimiter is disabled. No mob restrictions are in effect.");
/*    */     }
/* 78 */     TFM_GameRuleHandler.setGameRule(TFM_GameRuleHandler.TFM_GameRule.DO_MOB_SPAWNING, !TFM_ConfigEntry.MOB_LIMITER_ENABLED.getBoolean().booleanValue());
/*    */     
/* 80 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\SilentBloodGaming\Desktop\MasterFreedomMod.jar!\me\StevenLawson\TotalFreedomMod\Commands\Command_moblimiter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */