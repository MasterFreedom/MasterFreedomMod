/*    */ package me.StevenLawson.TotalFreedomMod.HTTPD;
/*    */ 
/*    */ import java.util.Set;
/*    */ import java.util.UUID;
/*    */ import me.StevenLawson.TotalFreedomMod.TFM_Admin;
/*    */ import me.StevenLawson.TotalFreedomMod.TFM_AdminList;
/*    */ import me.StevenLawson.TotalFreedomMod.TFM_Util;
/*    */ import me.StevenLawson.TotalFreedomMod.TotalFreedomMod;
/*    */ import org.bukkit.Server;
/*    */ import org.bukkit.entity.Player;
/*    */ import org.json.simple.JSONArray;
/*    */ import org.json.simple.JSONObject;
/*    */ 
/*    */ public class Module_players
/*    */   extends TFM_HTTPD_Module
/*    */ {
/*    */   public Module_players(NanoHTTPD.HTTPSession session)
/*    */   {
/* 15 */     super(session);
/*    */   }
/*    */   
/*    */   public NanoHTTPD.Response getResponse()
/*    */   {
/* 22 */     JSONObject responseObject = new JSONObject();
/*    */     
/* 24 */     JSONArray players = new JSONArray();
/* 25 */     JSONArray superadmins = new JSONArray();
/* 26 */     JSONArray telnetadmins = new JSONArray();
/* 27 */     JSONArray senioradmins = new JSONArray();
/* 28 */     JSONArray developers = new JSONArray();
/* 31 */     for (Player player : TotalFreedomMod.server.getOnlinePlayers()) {
/* 33 */       players.add(player.getName());
/*    */     }
/* 37 */     for (UUID superadmin : TFM_AdminList.getSuperUUIDs()) {
/* 39 */       if ((!TFM_AdminList.getSeniorUUIDs().contains(superadmin)) && 
/*    */       
/* 44 */         (!TFM_AdminList.getTelnetUUIDs().contains(superadmin))) {
/* 49 */         superadmins.add(getName(superadmin));
/*    */       }
/*    */     }
/* 53 */     for (UUID telnetadmin : TFM_AdminList.getTelnetUUIDs()) {
/* 55 */       if (!TFM_AdminList.getSeniorUUIDs().contains(telnetadmin)) {
/* 59 */         telnetadmins.add(getName(telnetadmin));
/*    */       }
/*    */     }
/* 63 */     for (UUID senioradmin : TFM_AdminList.getSeniorUUIDs()) {
/* 65 */       senioradmins.add(getName(senioradmin));
/*    */     }
/* 69 */     developers.addAll(TFM_Util.DEVELOPERS);
/*    */     
/* 71 */     responseObject.put("players", players);
/* 72 */     responseObject.put("superadmins", superadmins);
/* 73 */     responseObject.put("telnetadmins", telnetadmins);
/* 74 */     responseObject.put("senioradmins", senioradmins);
/* 75 */     responseObject.put("developers", developers);
/*    */     
/* 77 */     NanoHTTPD.Response response = new NanoHTTPD.Response(NanoHTTPD.Response.Status.OK, "application/json", responseObject.toString());
/* 78 */     response.addHeader("Access-Control-Allow-Origin", "*");
/* 79 */     return response;
/*    */   }
/*    */   
/*    */   private String getName(UUID uuid)
/*    */   {
/* 84 */     return TFM_AdminList.getEntry(uuid).getLastLoginName();
/*    */   }
/*    */ }


/* Location:              C:\Users\SilentBloodGaming\Desktop\MasterFreedomMod.jar!\me\StevenLawson\TotalFreedomMod\HTTPD\Module_players.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */