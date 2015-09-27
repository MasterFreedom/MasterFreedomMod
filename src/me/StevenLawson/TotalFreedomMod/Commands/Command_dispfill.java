/*     */ package me.StevenLawson.TotalFreedomMod.Commands;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import me.StevenLawson.TotalFreedomMod.TFM_DepreciationAggregator;
/*     */ import me.StevenLawson.TotalFreedomMod.TFM_Util;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ import org.bukkit.Location;
/*     */ import org.bukkit.Material;
/*     */ import org.bukkit.block.Block;
/*     */ import org.bukkit.block.Dispenser;
/*     */ import org.bukkit.command.Command;
/*     */ import org.bukkit.command.CommandSender;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.inventory.Inventory;
/*     */ import org.bukkit.inventory.ItemStack;
/*     */ 
/*     */ @CommandPermissions(level=AdminLevel.OP, source=SourceType.ONLY_IN_GAME)
/*     */ @CommandParameters(description="Fill nearby dispensers with a set of items of your choice.", usage="/<command> <radius> <comma,separated,items>")
/*     */ public class Command_dispfill
/*     */   extends TFM_Command
/*     */ {
/*     */   public boolean run(CommandSender sender, Player sender_p, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
/*     */   {
/*  25 */     if (args.length == 2)
/*     */     {
/*     */       int radius;
/*     */       try
/*     */       {
/*  31 */         radius = Math.max(5, Math.min(25, Integer.parseInt(args[0])));
/*     */       }
/*     */       catch (NumberFormatException ex)
/*     */       {
/*  35 */         sender.sendMessage("Invalid radius.");
/*  36 */         return true;
/*     */       }
/*  39 */       List<ItemStack> items = new ArrayList();
/*     */       
/*  41 */       String[] itemsRaw = StringUtils.split(args[1], ",");
/*  42 */       for (String searchItem : itemsRaw)
/*     */       {
/*  44 */         Material material = Material.matchMaterial(searchItem);
/*  45 */         if (material == null) {
/*     */           try
/*     */           {
/*  49 */             material = TFM_DepreciationAggregator.getMaterial(Integer.parseInt(searchItem));
/*     */           }
/*     */           catch (NumberFormatException ex) {}
/*     */         }
/*  56 */         if (material != null) {
/*  58 */           items.add(new ItemStack(material, 64));
/*     */         } else {
/*  62 */           sender.sendMessage("Skipping invalid item: " + searchItem);
/*     */         }
/*     */       }
/*  66 */       ItemStack[] itemsArray = (ItemStack[])items.toArray(new ItemStack[items.size()]);
/*     */       
/*  68 */       int affected = 0;
/*  69 */       Location centerLocation = sender_p.getLocation();
/*  70 */       Block centerBlock = centerLocation.getBlock();
/*  71 */       for (int xOffset = -radius; xOffset <= radius; xOffset++) {
/*  73 */         for (int yOffset = -radius; yOffset <= radius; yOffset++) {
/*  75 */           for (int zOffset = -radius; zOffset <= radius; zOffset++)
/*     */           {
/*  77 */             Block targetBlock = centerBlock.getRelative(xOffset, yOffset, zOffset);
/*  78 */             if (targetBlock.getLocation().distanceSquared(centerLocation) < radius * radius) {
/*  80 */               if (targetBlock.getType().equals(Material.DISPENSER))
/*     */               {
/*  82 */                 sender.sendMessage("Filling dispenser @ " + TFM_Util.formatLocation(targetBlock.getLocation()));
/*  83 */                 setDispenserContents(targetBlock, itemsArray);
/*  84 */                 affected++;
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*  91 */       sender.sendMessage("Done. " + affected + " dispenser(s) filled.");
/*     */     }
/*     */     else
/*     */     {
/*  95 */       return false;
/*     */     }
/*  98 */     return true;
/*     */   }
/*     */   
/*     */   private static void setDispenserContents(Block targetBlock, ItemStack[] items)
/*     */   {
/* 103 */     if (targetBlock.getType() == Material.DISPENSER)
/*     */     {
/* 105 */       Inventory dispenserInv = ((Dispenser)targetBlock.getState()).getInventory();
/* 106 */       dispenserInv.clear();
/* 107 */       dispenserInv.addItem(items);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\SilentBloodGaming\Desktop\MasterFreedomMod.jar!\me\StevenLawson\TotalFreedomMod\Commands\Command_dispfill.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */