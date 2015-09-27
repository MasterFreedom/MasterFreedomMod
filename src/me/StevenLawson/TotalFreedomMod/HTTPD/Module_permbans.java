/*    */ package me.StevenLawson.TotalFreedomMod.HTTPD;
/*    */ 
/*    */ import java.io.File;
/*    */ import me.StevenLawson.TotalFreedomMod.TotalFreedomMod;
/*    */ 
/*    */ public class Module_permbans
/*    */   extends TFM_HTTPD_Module
/*    */ {
/*    */   public Module_permbans(NanoHTTPD.HTTPSession session)
/*    */   {
/* 10 */     super(session);
/*    */   }
/*    */   
/*    */   public NanoHTTPD.Response getResponse()
/*    */   {
/* 16 */     File permbanFile = new File(TotalFreedomMod.plugin.getDataFolder(), "permban.yml");
/* 17 */     if (permbanFile.exists()) {
/* 19 */       return TFM_HTTPD_Manager.serveFileBasic(new File(TotalFreedomMod.plugin.getDataFolder(), "permban.yml"));
/*    */     }
/* 23 */     return new NanoHTTPD.Response(NanoHTTPD.Response.Status.NOT_FOUND, "text/plain", "Error 404: Not Found - The requested resource was not found on this server.");
/*    */   }
/*    */ }


/* Location:              C:\Users\SilentBloodGaming\Desktop\MasterFreedomMod.jar!\me\StevenLawson\TotalFreedomMod\HTTPD\Module_permbans.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */