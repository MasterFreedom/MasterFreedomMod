/*    */ package me.StevenLawson.TotalFreedomMod.Commands;
/*    */ 
/*    */ import me.StevenLawson.TotalFreedomMod.Config.TFM_ConfigEntry;
/*    */ import me.StevenLawson.TotalFreedomMod.TFM_PlayerData;
/*    */ import org.bukkit.ChatColor;
/*    */ import org.bukkit.Material;
/*    */ import org.bukkit.command.Command;
/*    */ import org.bukkit.command.CommandSender;
/*    */ import org.bukkit.entity.Player;
/*    */ import org.bukkit.inventory.ItemStack;
/*    */ 
/*    */ @CommandPermissions(level=AdminLevel.OP, source=SourceType.ONLY_IN_GAME)
/*    */ @CommandParameters(description="Modern weaponry, FTW. Use 'draw' to start firing, 'sling' to stop firing.", usage="/<command> <draw | sling>")
/*    */ public class Command_mp44
/*    */   extends TFM_Command
/*    */ {
/*    */   public boolean run(CommandSender sender, Player sender_p, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
/*    */   {
/* 19 */     if (!TFM_ConfigEntry.MP44_ENABLED.getBoolean().booleanValue())
/*    */     {
/* 21 */       playerMsg("The mp44 is currently disabled.", ChatColor.GREEN);
/* 22 */       return true;
/*    */     }
/* 25 */     if (args.length == 0) {
/* 27 */       return false;
/*    */     }
/* 30 */     TFM_PlayerData playerdata = TFM_PlayerData.getPlayerData(sender_p);
/* 32 */     if (args[0].equalsIgnoreCase("draw"))
/*    */     {
/* 34 */       playerdata.armMP44();
/*    */       
/* 36 */       playerMsg("mp44 is ARMED! Left click with gunpowder to start firing, left click again to quit.", ChatColor.GREEN);
/* 37 */       playerMsg("Type /mp44 sling to disable.  -by Madgeek1450", ChatColor.GREEN);
/*    */       
/* 39 */       sender_p.setItemInHand(new ItemStack(Material.SULPHUR, 1));
/*    */     }
/*    */     else
/*    */     {
/* 43 */       playerdata.disarmMP44();
/*    */       
/* 45 */       sender.sendMessage(ChatColor.GREEN + "mp44 Disarmed.");
/*    */     }
/* 48 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\SilentBloodGaming\Desktop\MasterFreedomMod.jar!\me\StevenLawson\TotalFreedomMod\Commands\Command_mp44.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */