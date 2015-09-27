/*     */ package me.StevenLawson.TotalFreedomMod.Commands;
/*     */ 
/*     */ import java.util.Map;
/*     */ import me.StevenLawson.TotalFreedomMod.TFM_Log;
/*     */ import org.bukkit.Material;
/*     */ import org.bukkit.command.Command;
/*     */ import org.bukkit.command.CommandSender;
/*     */ import org.bukkit.enchantments.Enchantment;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.inventory.ItemStack;
/*     */ 
/*     */ @CommandPermissions(level=AdminLevel.OP, source=SourceType.ONLY_IN_GAME)
/*     */ @CommandParameters(description="Enchant items.", usage="/<command> <list | addall | reset | add <name> | remove <name>>")
/*     */ public class Command_enchant
/*     */   extends TFM_Command
/*     */ {
/*     */   public boolean run(CommandSender sender, Player sender_p, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
/*     */   {
/*  17 */     if (args.length < 1) {
/*  19 */       return false;
/*     */     }
/*  22 */     ItemStack itemInHand = sender_p.getItemInHand();
/*  24 */     if (itemInHand == null)
/*     */     {
/*  26 */       playerMsg("You are holding an invalid item.");
/*  27 */       return true;
/*     */     }
/*  30 */     if (args[0].equalsIgnoreCase("list"))
/*     */     {
/*  32 */       boolean has_enchantments = false;
/*     */       
/*  34 */       StringBuilder possible_ench = new StringBuilder("Possible enchantments for held item: ");
/*  35 */       for (Enchantment ench : Enchantment.values()) {
/*  37 */         if (ench.canEnchantItem(itemInHand))
/*     */         {
/*  39 */           has_enchantments = true;
/*  40 */           possible_ench.append(ench.getName()).append(", ");
/*     */         }
/*     */       }
/*  44 */       if (has_enchantments) {
/*  46 */         playerMsg(possible_ench.toString());
/*     */       } else {
/*  50 */         playerMsg("The held item has no enchantments.");
/*     */       }
/*     */     }
/*  53 */     else if (args[0].equalsIgnoreCase("addall"))
/*     */     {
/*  55 */       for (Enchantment ench : Enchantment.values()) {
/*     */         try
/*     */         {
/*  59 */           if (ench.canEnchantItem(itemInHand)) {
/*  61 */             itemInHand.addEnchantment(ench, ench.getMaxLevel());
/*     */           }
/*     */         }
/*     */         catch (Exception ex)
/*     */         {
/*  66 */           TFM_Log.info("Error using " + ench.getName() + " on " + itemInHand.getType().name() + " held by " + sender_p.getName() + ".");
/*     */         }
/*     */       }
/*  70 */       playerMsg("Added all possible enchantments for this item.");
/*     */     }
/*  72 */     else if (args[0].equalsIgnoreCase("reset"))
/*     */     {
/*  74 */       for (Enchantment ench : itemInHand.getEnchantments().keySet()) {
/*  76 */         itemInHand.removeEnchantment(ench);
/*     */       }
/*  79 */       playerMsg("Removed all enchantments.");
/*     */     }
/*     */     else
/*     */     {
/*  83 */       if (args.length < 2) {
/*  85 */         return false;
/*     */       }
/*  88 */       Enchantment ench = null;
/*     */       try
/*     */       {
/*  92 */         ench = Enchantment.getByName(args[1]);
/*     */       }
/*     */       catch (Exception ex) {}
/*  98 */       if (ench == null)
/*     */       {
/* 100 */         playerMsg(args[1] + " is an invalid enchantment for the held item. Type \"/enchant list\" for valid enchantments for this item.");
/* 101 */         return true;
/*     */       }
/* 104 */       if (args[0].equalsIgnoreCase("add"))
/*     */       {
/* 106 */         if (ench.canEnchantItem(itemInHand))
/*     */         {
/* 108 */           itemInHand.addEnchantment(ench, ench.getMaxLevel());
/*     */           
/* 110 */           playerMsg("Added enchantment: " + ench.getName());
/*     */         }
/*     */         else
/*     */         {
/* 114 */           playerMsg("Can't use this enchantment on held item.");
/*     */         }
/*     */       }
/* 117 */       else if (args[0].equals("remove"))
/*     */       {
/* 119 */         itemInHand.removeEnchantment(ench);
/*     */         
/* 121 */         playerMsg("Removed enchantment: " + ench.getName());
/*     */       }
/*     */     }
/* 125 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\SilentBloodGaming\Desktop\MasterFreedomMod.jar!\me\StevenLawson\TotalFreedomMod\Commands\Command_enchant.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */