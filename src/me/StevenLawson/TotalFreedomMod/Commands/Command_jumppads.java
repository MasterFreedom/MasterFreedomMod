/*    */ package me.StevenLawson.TotalFreedomMod.Commands;
/*    */ 
/*    */ import me.StevenLawson.TotalFreedomMod.TFM_Jumppads;
/*    */ import me.StevenLawson.TotalFreedomMod.TFM_Jumppads.JumpPadMode;
/*    */ import me.StevenLawson.TotalFreedomMod.TFM_Util;
/*    */ import org.bukkit.ChatColor;
/*    */ import org.bukkit.command.Command;
/*    */ import org.bukkit.command.CommandSender;
/*    */ import org.bukkit.entity.Player;
/*    */ 
/*    */ @CommandPermissions(level=AdminLevel.SUPER, source=SourceType.BOTH)
/*    */ @CommandParameters(description="Manage jumppads", usage="/<command> <on | off | info | sideways <on | off> | strength <strength (1-10)>>", aliases="launchpads,jp")
/*    */ public class Command_jumppads
/*    */   extends TFM_Command
/*    */ {
/*    */   public boolean run(CommandSender sender, Player sender_p, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
/*    */   {
/* 17 */     if ((args.length == 0) || (args.length > 2)) {
/* 19 */       return false;
/*    */     }
/* 22 */     if (args.length == 1)
/*    */     {
/* 24 */       if (args[0].equalsIgnoreCase("info"))
/*    */       {
/* 26 */         playerMsg("Jumppads: " + (TFM_Jumppads.getMode().isOn() ? "Enabled" : "Disabled"), ChatColor.BLUE);
/* 27 */         playerMsg("Sideways: " + (TFM_Jumppads.getMode() == TFM_Jumppads.JumpPadMode.NORMAL_AND_SIDEWAYS ? "Enabled" : "Disabled"), ChatColor.BLUE);
/* 28 */         playerMsg("Strength: " + (TFM_Jumppads.getStrength() * 10.0D - 1.0D), ChatColor.BLUE);
/* 29 */         return true;
/*    */       }
/* 32 */       if ("off".equals(args[0]))
/*    */       {
/* 34 */         TFM_Util.adminAction(sender.getName(), "Disabling Jumppads", false);
/* 35 */         TFM_Jumppads.setMode(TFM_Jumppads.JumpPadMode.OFF);
/*    */       }
/*    */       else
/*    */       {
/* 39 */         TFM_Util.adminAction(sender.getName(), "Enabling Jumppads", false);
/* 40 */         TFM_Jumppads.setMode(TFM_Jumppads.JumpPadMode.MADGEEK);
/*    */       }
/*    */     }
/*    */     else
/*    */     {
/* 45 */       if (TFM_Jumppads.getMode() == TFM_Jumppads.JumpPadMode.OFF)
/*    */       {
/* 47 */         playerMsg("Jumppads are currently disabled, please enable them before changing jumppads settings.");
/* 48 */         return true;
/*    */       }
/* 51 */       if (args[0].equalsIgnoreCase("sideways"))
/*    */       {
/* 53 */         if ("off".equals(args[1]))
/*    */         {
/* 55 */           TFM_Util.adminAction(sender.getName(), "Setting Jumppads mode to: Madgeek", false);
/* 56 */           TFM_Jumppads.setMode(TFM_Jumppads.JumpPadMode.MADGEEK);
/*    */         }
/*    */         else
/*    */         {
/* 60 */           TFM_Util.adminAction(sender.getName(), "Setting Jumppads mode to: Normal and Sideways", false);
/* 61 */           TFM_Jumppads.setMode(TFM_Jumppads.JumpPadMode.NORMAL_AND_SIDEWAYS);
/*    */         }
/*    */       }
/* 64 */       else if (args[0].equalsIgnoreCase("strength"))
/*    */       {
/*    */         float strength;
/*    */         try
/*    */         {
/* 69 */           strength = Float.parseFloat(args[1]);
/*    */         }
/*    */         catch (NumberFormatException ex)
/*    */         {
/* 73 */           playerMsg("Invalid Strength");
/* 74 */           return true;
/*    */         }
/* 77 */         if ((strength > 10.0F) || (strength < 1.0F))
/*    */         {
/* 79 */           playerMsg("Invalid Strength: The strength may be 1 through 10.");
/* 80 */           return true;
/*    */         }
/* 83 */         TFM_Util.adminAction(sender.getName(), "Setting Jumppads strength to: " + String.valueOf(strength), false);
/* 84 */         TFM_Jumppads.setStrength(strength / 10.0F + 0.1F);
/*    */       }
/*    */       else
/*    */       {
/* 88 */         return false;
/*    */       }
/*    */     }
/* 92 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\SilentBloodGaming\Desktop\MasterFreedomMod.jar!\me\StevenLawson\TotalFreedomMod\Commands\Command_jumppads.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */