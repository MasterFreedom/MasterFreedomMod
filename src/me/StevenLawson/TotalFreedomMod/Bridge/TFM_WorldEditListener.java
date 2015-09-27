/*    */ package me.StevenLawson.TotalFreedomMod.Bridge;
/*    */ 
/*    */ import me.StevenLawson.TotalFreedomMod.TFM_AdminList;
/*    */ import me.StevenLawson.TotalFreedomMod.TFM_ProtectedArea;
/*    */ import me.StevenLawson.TotalFreedomMod.TFM_Util;
/*    */ import me.StevenLawson.worldedit.LimitChangedEvent;
/*    */ import me.StevenLawson.worldedit.SelectionChangedEvent;
/*    */ import org.bukkit.ChatColor;
/*    */ import org.bukkit.World;
/*    */ import org.bukkit.entity.Player;
/*    */ import org.bukkit.event.EventHandler;
/*    */ import org.bukkit.event.Listener;
/*    */ 
/*    */ public class TFM_WorldEditListener
/*    */   implements Listener
/*    */ {
/*    */   @EventHandler
/*    */   public void onSelectionChange(SelectionChangedEvent event)
/*    */   {
/* 19 */     Player player = event.getPlayer();
/* 21 */     if (TFM_AdminList.isSuperAdmin(player)) {
/* 23 */       return;
/*    */     }
/* 26 */     if (TFM_ProtectedArea.isInProtectedArea(event.getMinVector(), event.getMaxVector(), event.getWorld().getName()))
/*    */     {
/* 32 */       player.sendMessage(ChatColor.RED + "The region that you selected contained a protected area. Selection cleared.");
/* 33 */       event.setCancelled(true);
/*    */     }
/*    */   }
/*    */   
/*    */   @EventHandler
/*    */   public void onLimitChanged(LimitChangedEvent event)
/*    */   {
/* 40 */     Player player = event.getPlayer();
/* 42 */     if (TFM_AdminList.isSuperAdmin(player)) {
/* 44 */       return;
/*    */     }
/* 47 */     if (!event.getPlayer().equals(event.getTarget()))
/*    */     {
/* 49 */       player.sendMessage(ChatColor.RED + "Only admins can change the limit for other players!");
/* 50 */       event.setCancelled(true);
/*    */     }
/* 53 */     if ((event.getLimit() < 0) || (event.getLimit() > 10000))
/*    */     {
/* 55 */       player.setOp(false);
/* 56 */       TFM_Util.bcastMsg(event.getPlayer().getName() + " tried to set their WorldEdit limit to " + event.getLimit() + " and has been de-opped", ChatColor.RED);
/* 57 */       event.setCancelled(true);
/* 58 */       player.sendMessage(ChatColor.RED + "You cannot set your limit higher than 10000 or to -1!");
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\SilentBloodGaming\Desktop\MasterFreedomMod.jar!\me\StevenLawson\TotalFreedomMod\Bridge\TFM_WorldEditListener.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */