/*    */ package me.StevenLawson.TotalFreedomMod.Commands;
/*    */ 
/*    */ import java.util.List;
/*    */ import me.StevenLawson.TotalFreedomMod.TFM_DepreciationAggregator;
/*    */ import org.bukkit.Location;
/*    */ import org.bukkit.Material;
/*    */ import org.bukkit.Server;
/*    */ import org.bukkit.World;
/*    */ import org.bukkit.block.Block;
/*    */ import org.bukkit.block.BlockState;
/*    */ import org.bukkit.command.Command;
/*    */ import org.bukkit.command.CommandSender;
/*    */ import org.bukkit.entity.Player;
/*    */ import org.bukkit.material.Lever;
/*    */ 
/*    */ @CommandPermissions(level=AdminLevel.ALL, source=SourceType.BOTH)
/*    */ @CommandParameters(description="Set the on/off state of the lever at position x, y, z in world 'worldname'.", usage="/<command> <x> <y> <z> <worldname> <on|off>")
/*    */ public class Command_setlever
/*    */   extends TFM_Command
/*    */ {
/*    */   public boolean run(CommandSender sender, Player sender_p, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
/*    */   {
/* 20 */     if (args.length != 5) {
/* 22 */       return false;
/*    */     }
/*    */     double x;
/*    */     double y;
/*    */     double z;
/*    */     try
/*    */     {
/* 28 */       x = Double.parseDouble(args[0]);
/* 29 */       y = Double.parseDouble(args[1]);
/* 30 */       z = Double.parseDouble(args[2]);
/*    */     }
/*    */     catch (NumberFormatException ex)
/*    */     {
/* 34 */       playerMsg("Invalid coordinates.");
/* 35 */       return true;
/*    */     }
/* 38 */     World world = null;
/* 39 */     String needleWorldName = args[3].trim();
/* 40 */     List<World> worlds = server.getWorlds();
/* 41 */     for (World testWorld : worlds) {
/* 43 */       if (testWorld.getName().trim().equalsIgnoreCase(needleWorldName))
/*    */       {
/* 45 */         world = testWorld;
/* 46 */         break;
/*    */       }
/*    */     }
/* 50 */     if (world == null)
/*    */     {
/* 52 */       playerMsg("Invalid world name.");
/* 53 */       return true;
/*    */     }
/* 56 */     Location leverLocation = new Location(world, x, y, z);
/*    */     
/* 58 */     boolean leverOn = (args[4].trim().equalsIgnoreCase("on")) || (args[4].trim().equalsIgnoreCase("1"));
/*    */     
/* 60 */     Block targetBlock = leverLocation.getBlock();
/* 62 */     if (targetBlock.getType() == Material.LEVER)
/*    */     {
/* 64 */       Lever lever = TFM_DepreciationAggregator.makeLeverWithData(TFM_DepreciationAggregator.getData_Block(targetBlock));
/* 65 */       lever.setPowered(leverOn);
/* 66 */       TFM_DepreciationAggregator.setData_Block(targetBlock, TFM_DepreciationAggregator.getData_MaterialData(lever));
/* 67 */       targetBlock.getState().update();
/*    */     }
/*    */     else
/*    */     {
/* 71 */       playerMsg("Target block " + targetBlock + "  is not a lever.");
/* 72 */       return true;
/*    */     }
/* 75 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\SilentBloodGaming\Desktop\MasterFreedomMod.jar!\me\StevenLawson\TotalFreedomMod\Commands\Command_setlever.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */