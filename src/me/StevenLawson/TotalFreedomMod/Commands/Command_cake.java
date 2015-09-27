/*    */ package me.StevenLawson.TotalFreedomMod.Commands;
/*    */ 
/*    */ import java.util.Random;
/*    */ import me.StevenLawson.TotalFreedomMod.TFM_Util;
/*    */ import org.bukkit.Achievement;
/*    */ import org.bukkit.ChatColor;
/*    */ import org.bukkit.Material;
/*    */ import org.bukkit.Server;
/*    */ import org.bukkit.command.Command;
/*    */ import org.bukkit.command.CommandSender;
/*    */ import org.bukkit.entity.Player;
/*    */ import org.bukkit.inventory.ItemStack;
/*    */ import org.bukkit.inventory.PlayerInventory;
/*    */ import org.bukkit.inventory.meta.ItemMeta;
/*    */ 
/*    */ @CommandPermissions(level=AdminLevel.SUPER, source=SourceType.BOTH)
/*    */ @CommandParameters(description="For the people that are still alive.", usage="/<command>")
/*    */ public class Command_cake
/*    */   extends TFM_Command
/*    */ {
/*    */   public static final String CAKE_LYRICS = "But there's no sense crying over every mistake. You just keep on trying till you run out of cake.";
/* 19 */   private final Random random = new Random();
/*    */   
/*    */   public boolean run(CommandSender sender, Player sender_p, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
/*    */   {
/* 24 */     StringBuilder output = new StringBuilder();
/*    */     
/* 26 */     String[] words = "But there's no sense crying over every mistake. You just keep on trying till you run out of cake.".split(" ");
/* 27 */     for (String word : words) {
/* 29 */       output.append('§').append(Integer.toHexString(1 + random.nextInt(14))).append(word).append(" ");
/*    */     }
/* 32 */     ItemStack heldItem = new ItemStack(Material.CAKE);
/* 33 */     ItemMeta heldItemMeta = heldItem.getItemMeta();
/* 34 */     heldItemMeta.setDisplayName(ChatColor.WHITE + "The " + ChatColor.DARK_GRAY + "Lie");
/* 35 */     heldItem.setItemMeta(heldItemMeta);
/* 37 */     for (Player player : server.getOnlinePlayers())
/*    */     {
/* 39 */       int firstEmpty = player.getInventory().firstEmpty();
/* 40 */       if (firstEmpty >= 0) {
/* 42 */         player.getInventory().setItem(firstEmpty, heldItem);
/*    */       }
/* 45 */       player.awardAchievement(Achievement.BAKE_CAKE);
/*    */     }
/* 48 */     TFM_Util.bcastMsg(output.toString());
/*    */     
/* 50 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\SilentBloodGaming\Desktop\MasterFreedomMod.jar!\me\StevenLawson\TotalFreedomMod\Commands\Command_cake.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */