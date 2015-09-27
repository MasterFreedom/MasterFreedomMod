/*    */ package me.StevenLawson.TotalFreedomMod.Commands;
/*    */ 
/*    */ import java.util.Map;
/*    */ import me.StevenLawson.TotalFreedomMod.Config.TFM_ConfigEntry;
/*    */ import me.StevenLawson.TotalFreedomMod.TFM_PlayerData;
/*    */ import me.StevenLawson.TotalFreedomMod.TFM_Util;
/*    */ import org.apache.commons.lang3.StringUtils;
/*    */ import org.bukkit.ChatColor;
/*    */ import org.bukkit.Material;
/*    */ import org.bukkit.command.Command;
/*    */ import org.bukkit.command.CommandSender;
/*    */ import org.bukkit.entity.EntityType;
/*    */ import org.bukkit.entity.Player;
/*    */ import org.bukkit.inventory.ItemStack;
/*    */ 
/*    */ @CommandPermissions(level=AdminLevel.OP, source=SourceType.ONLY_IN_GAME)
/*    */ @CommandParameters(description="Throw a mob in the direction you are facing when you left click with a stick.", usage="/<command> <mobtype [speed] | off | list>")
/*    */ public class Command_tossmob
/*    */   extends TFM_Command
/*    */ {
/*    */   public boolean run(CommandSender sender, Player sender_p, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
/*    */   {
/* 23 */     if (!TFM_ConfigEntry.TOSSMOB_ENABLED.getBoolean().booleanValue())
/*    */     {
/* 25 */       playerMsg("Tossmob is currently disabled.");
/* 26 */       return true;
/*    */     }
/* 29 */     TFM_PlayerData playerData = TFM_PlayerData.getPlayerData(sender_p);
/*    */     
/* 31 */     EntityType creature = EntityType.PIG;
/* 32 */     if (args.length >= 1)
/*    */     {
/* 34 */       if ("off".equals(args[0]))
/*    */       {
/* 36 */         playerData.disableMobThrower();
/* 37 */         playerMsg("MobThrower is disabled.", ChatColor.GREEN);
/* 38 */         return true;
/*    */       }
/* 41 */       if (args[0].equalsIgnoreCase("list"))
/*    */       {
/* 43 */         playerMsg("Supported mobs: " + StringUtils.join(TFM_Util.mobtypes.keySet(), ", "), ChatColor.GREEN);
/* 44 */         return true;
/*    */       }
/*    */       try
/*    */       {
/* 49 */         creature = TFM_Util.getEntityType(args[0]);
/*    */       }
/*    */       catch (Exception ex)
/*    */       {
/* 53 */         playerMsg(args[0] + " is not a supported mob type. Using a pig instead.", ChatColor.RED);
/* 54 */         playerMsg("By the way, you can type /tossmob list to see all possible mobs.", ChatColor.RED);
/* 55 */         creature = EntityType.PIG;
/*    */       }
/*    */     }
/* 59 */     double speed = 1.0D;
/* 60 */     if (args.length >= 2) {
/*    */       try
/*    */       {
/* 64 */         speed = Double.parseDouble(args[1]);
/*    */       }
/*    */       catch (NumberFormatException nfex) {}
/*    */     }
/* 71 */     if (speed < 1.0D) {
/* 73 */       speed = 1.0D;
/* 75 */     } else if (speed > 5.0D) {
/* 77 */       speed = 5.0D;
/*    */     }
/* 80 */     playerData.enableMobThrower(creature, speed);
/* 81 */     playerMsg("MobThrower is enabled. Creature: " + creature + " - Speed: " + speed + ".", ChatColor.GREEN);
/* 82 */     playerMsg("Left click while holding a " + Material.BONE.toString() + " to throw mobs!", ChatColor.GREEN);
/* 83 */     playerMsg("Type '/tossmob off' to disable.  -By Madgeek1450", ChatColor.GREEN);
/*    */     
/* 85 */     sender_p.setItemInHand(new ItemStack(Material.BONE, 1));
/*    */     
/* 87 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\SilentBloodGaming\Desktop\MasterFreedomMod.jar!\me\StevenLawson\TotalFreedomMod\Commands\Command_tossmob.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */