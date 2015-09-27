/*    */ package me.StevenLawson.TotalFreedomMod.HTTPD;
/*    */ 
/*    */ import java.util.Collection;
/*    */ import java.util.List;
/*    */ import me.StevenLawson.TotalFreedomMod.TFM_AdminList;
/*    */ import me.StevenLawson.TotalFreedomMod.TFM_Util;
/*    */ import org.bukkit.Bukkit;
/*    */ import org.bukkit.entity.Player;
/*    */ 
/*    */ public class Module_list
/*    */   extends TFM_HTTPD_Module
/*    */ {
/*    */   public Module_list(NanoHTTPD.HTTPSession session)
/*    */   {
/* 13 */     super(session);
/*    */   }
/*    */   
/*    */   public String getBody()
/*    */   {
/* 19 */     StringBuilder body = new StringBuilder();
/*    */     
/* 21 */     Collection<? extends Player> onlinePlayers = Bukkit.getOnlinePlayers();
/*    */     
/* 23 */     body.append("<p>There are ").append(onlinePlayers.size()).append("/").append(Bukkit.getMaxPlayers()).append(" players online:</p>\r\n");
/*    */     
/* 25 */     body.append("<ul>\r\n");
/* 27 */     for (Player player : onlinePlayers)
/*    */     {
/* 29 */       String prefix = "";
/* 30 */       if (TFM_AdminList.isSuperAdmin(player))
/*    */       {
/* 32 */         if (TFM_AdminList.isSeniorAdmin(player)) {
/* 34 */           prefix = "[SrA]";
/*    */         } else {
/* 38 */           prefix = "[SA]";
/*    */         }
/* 41 */         if (TFM_Util.DEVELOPERS.contains(player.getName())) {
/* 43 */           prefix = "[Dev]";
/*    */         }
/* 46 */         if (player.getName().equals("markbyron")) {
/* 48 */           prefix = "[Owner]";
/*    */         }
/*    */       }
/* 53 */       else if (player.isOp())
/*    */       {
/* 55 */         prefix = "[OP]";
/*    */       }
/* 59 */       body.append("<li>").append(prefix).append(player.getName()).append("</li>\r\n");
/*    */     }
/* 62 */     body.append("</ul>\r\n");
/*    */     
/* 64 */     return body.toString();
/*    */   }
/*    */   
/*    */   public String getTitle()
/*    */   {
/* 70 */     return "Total Freedom - Online Users";
/*    */   }
/*    */ }


/* Location:              C:\Users\SilentBloodGaming\Desktop\MasterFreedomMod.jar!\me\StevenLawson\TotalFreedomMod\HTTPD\Module_list.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */