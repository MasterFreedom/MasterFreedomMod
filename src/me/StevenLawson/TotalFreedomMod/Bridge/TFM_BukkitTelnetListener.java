/*    */ package me.StevenLawson.TotalFreedomMod.Bridge;
/*    */ 
/*    */ import java.util.Iterator;
/*    */ import java.util.Map;
/*    */ import java.util.Map.Entry;
/*    */ import java.util.Set;
/*    */ import me.StevenLawson.BukkitTelnet.api.TelnetCommandEvent;
/*    */ import me.StevenLawson.BukkitTelnet.api.TelnetPreLoginEvent;
/*    */ import me.StevenLawson.BukkitTelnet.api.TelnetRequestDataTagsEvent;
/*    */ import me.StevenLawson.TotalFreedomMod.TFM_Admin;
/*    */ import me.StevenLawson.TotalFreedomMod.TFM_AdminList;
/*    */ import me.StevenLawson.TotalFreedomMod.TFM_CommandBlocker;
/*    */ import me.StevenLawson.TotalFreedomMod.TFM_PlayerData;
/*    */ import org.bukkit.entity.Player;
/*    */ import org.bukkit.event.EventHandler;
/*    */ import org.bukkit.event.EventPriority;
/*    */ import org.bukkit.event.Listener;
/*    */ 
/*    */ public class TFM_BukkitTelnetListener
/*    */   implements Listener
/*    */ {
/*    */   @EventHandler(priority=EventPriority.NORMAL)
/*    */   public void onTelnetPreLogin(TelnetPreLoginEvent event)
/*    */   {
/* 23 */     String ip = event.getIp();
/* 24 */     if ((ip == null) || (ip.isEmpty())) {
/* 26 */       return;
/*    */     }
/* 29 */     TFM_Admin admin = TFM_AdminList.getEntryByIp(ip, true);
/* 31 */     if ((admin == null) || (!admin.isActivated()) || (!admin.isTelnetAdmin())) {
/* 33 */       return;
/*    */     }
/* 36 */     event.setBypassPassword(true);
/* 37 */     event.setName(admin.getLastLoginName());
/*    */   }
/*    */   
/*    */   @EventHandler(priority=EventPriority.NORMAL)
/*    */   public void onTelnetCommand(TelnetCommandEvent event)
/*    */   {
/* 43 */     if (TFM_CommandBlocker.isCommandBlocked(event.getCommand(), event.getSender())) {
/* 45 */       event.setCancelled(true);
/*    */     }
/*    */   }
/*    */   
/*    */   @EventHandler(priority=EventPriority.NORMAL)
/*    */   public void onTelnetRequestDataTags(TelnetRequestDataTagsEvent event)
/*    */   {
/* 52 */     Iterator<Map.Entry<Player, Map<String, Object>>> it = event.getDataTags().entrySet().iterator();
/* 53 */     while (it.hasNext())
/*    */     {
/* 55 */       Map.Entry<Player, Map<String, Object>> entry = (Map.Entry)it.next();
/* 56 */       Player player = (Player)entry.getKey();
/* 57 */       Map<String, Object> playerTags = (Map)entry.getValue();
/*    */       
/* 59 */       boolean isAdmin = false;
/* 60 */       boolean isTelnetAdmin = false;
/* 61 */       boolean isSeniorAdmin = false;
/*    */       
/* 63 */       TFM_Admin admin = TFM_AdminList.getEntry(player);
/* 64 */       if (admin != null)
/*    */       {
/* 66 */         boolean isActivated = admin.isActivated();
/*    */         
/* 68 */         isAdmin = isActivated;
/* 69 */         isTelnetAdmin = (isActivated) && (admin.isTelnetAdmin());
/* 70 */         isSeniorAdmin = (isActivated) && (admin.isSeniorAdmin());
/*    */       }
/* 73 */       playerTags.put("tfm.admin.isAdmin", Boolean.valueOf(isAdmin));
/* 74 */       playerTags.put("tfm.admin.isTelnetAdmin", Boolean.valueOf(isTelnetAdmin));
/* 75 */       playerTags.put("tfm.admin.isSeniorAdmin", Boolean.valueOf(isSeniorAdmin));
/*    */       
/* 77 */       playerTags.put("tfm.playerdata.getTag", TFM_PlayerData.getPlayerData(player).getTag());
/*    */       
/* 79 */       playerTags.put("tfm.essentialsBridge.getNickname", TFM_EssentialsBridge.getNickname(player.getName()));
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\SilentBloodGaming\Desktop\MasterFreedomMod.jar!\me\StevenLawson\TotalFreedomMod\Bridge\TFM_BukkitTelnetListener.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */