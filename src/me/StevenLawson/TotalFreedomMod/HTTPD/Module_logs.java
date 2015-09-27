/*    */ package me.StevenLawson.TotalFreedomMod.HTTPD;
/*    */ 
/*    */ import java.io.File;
/*    */ import java.util.Map;
/*    */ import me.StevenLawson.TotalFreedomMod.Config.TFM_ConfigEntry;
/*    */ 
/*    */ public class Module_logs
/*    */   extends Module_file
/*    */ {
/*    */   public Module_logs(NanoHTTPD.HTTPSession session)
/*    */   {
/* 10 */     super(session);
/*    */   }
/*    */   
/*    */   public NanoHTTPD.Response getResponse()
/*    */   {
/* 16 */     if (TFM_ConfigEntry.LOGS_SECRET.getString().equals(params.get("password"))) {
/* 18 */       return serveFile("latest.log", params, new File("./logs"));
/*    */     }
/* 22 */     return new NanoHTTPD.Response(NanoHTTPD.Response.Status.FORBIDDEN, "text/plain", "Incorrect password.");
/*    */   }
/*    */ }


/* Location:              C:\Users\SilentBloodGaming\Desktop\MasterFreedomMod.jar!\me\StevenLawson\TotalFreedomMod\HTTPD\Module_logs.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */