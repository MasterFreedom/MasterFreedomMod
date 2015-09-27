/*    */ package me.StevenLawson.TotalFreedomMod.HTTPD;
/*    */ 
/*    */ import java.net.Socket;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import me.StevenLawson.TotalFreedomMod.TFM_Log;
/*    */ 
/*    */ public abstract class TFM_HTTPD_Module
/*    */ {
/*    */   protected final String uri;
/*    */   protected final NanoHTTPD.Method method;
/*    */   protected final Map<String, String> headers;
/*    */   protected final Map<String, String> params;
/*    */   protected final Socket socket;
/*    */   protected final NanoHTTPD.HTTPSession session;
/*    */   
/*    */   public TFM_HTTPD_Module(NanoHTTPD.HTTPSession session)
/*    */   {
/* 22 */     uri = session.getUri();
/* 23 */     method = session.getMethod();
/* 24 */     headers = session.getHeaders();
/* 25 */     params = session.getParms();
/* 26 */     socket = session.getSocket();
/* 27 */     this.session = session;
/*    */   }
/*    */   
/*    */   public String getBody()
/*    */   {
/* 32 */     return null;
/*    */   }
/*    */   
/*    */   public String getTitle()
/*    */   {
/* 37 */     return null;
/*    */   }
/*    */   
/*    */   public String getStyle()
/*    */   {
/* 42 */     return null;
/*    */   }
/*    */   
/*    */   public String getScript()
/*    */   {
/* 47 */     return null;
/*    */   }
/*    */   
/*    */   public NanoHTTPD.Response getResponse()
/*    */   {
/* 52 */     return new TFM_HTTPD_PageBuilder(getBody(), getTitle(), getStyle(), getScript()).getResponse();
/*    */   }
/*    */   
/*    */   protected final Map<String, String> getFiles()
/*    */   {
/* 57 */     Map<String, String> files = new HashMap();
/*    */     try
/*    */     {
/* 61 */       session.parseBody(files);
/*    */     }
/*    */     catch (Exception ex)
/*    */     {
/* 65 */       TFM_Log.severe(ex);
/*    */     }
/* 68 */     return files;
/*    */   }
/*    */ }


/* Location:              C:\Users\SilentBloodGaming\Desktop\MasterFreedomMod.jar!\me\StevenLawson\TotalFreedomMod\HTTPD\TFM_HTTPD_Module.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */