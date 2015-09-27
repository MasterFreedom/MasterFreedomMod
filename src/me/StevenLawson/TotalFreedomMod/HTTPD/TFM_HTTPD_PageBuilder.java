/*    */ package me.StevenLawson.TotalFreedomMod.HTTPD;
/*    */ 
/*    */ public class TFM_HTTPD_PageBuilder
/*    */ {
/*    */   private static final String TEMPLATE = "<!DOCTYPE html>\r\n<html>\r\n<head>\r\n<title>{$TITLE}</title>\r\n<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\r\n{$STYLE}{$SCRIPT}</head>\r\n<body>\r\n{$BODY}</body>\r\n</html>\r\n";
/*    */   private static final String STYLE = "<style type=\"text/css\">{$STYLE}</style>\r\n";
/*    */   private static final String SCRIPT = "<script src=\"//ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js\"></script>\r\n<script src=\"//ajax.googleapis.com/ajax/libs/jqueryui/1.10.3/jquery-ui.min.js\"></script>\r\n<script>\r\n{$SCRIPT}\r\n</script>\r\n";
/* 22 */   private String body = null;
/* 23 */   private String title = null;
/* 24 */   private String style = null;
/* 25 */   private String script = null;
/*    */   
/*    */   public TFM_HTTPD_PageBuilder() {}
/*    */   
/*    */   public TFM_HTTPD_PageBuilder(String body, String title, String style, String script)
/*    */   {
/* 33 */     this.body = body;
/* 34 */     this.title = title;
/* 35 */     this.style = style;
/* 36 */     this.script = script;
/*    */   }
/*    */   
/*    */   public void setBody(String body)
/*    */   {
/* 41 */     this.body = body;
/*    */   }
/*    */   
/*    */   public void setTitle(String title)
/*    */   {
/* 46 */     this.title = title;
/*    */   }
/*    */   
/*    */   public void setStyle(String style)
/*    */   {
/* 51 */     this.style = style;
/*    */   }
/*    */   
/*    */   public void setScript(String script)
/*    */   {
/* 56 */     this.script = script;
/*    */   }
/*    */   
/*    */   public NanoHTTPD.Response getResponse()
/*    */   {
/* 61 */     return new NanoHTTPD.Response(toString());
/*    */   }
/*    */   
/*    */   public String toString()
/*    */   {
/* 67 */     return "<!DOCTYPE html>\r\n<html>\r\n<head>\r\n<title>{$TITLE}</title>\r\n<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\r\n{$STYLE}{$SCRIPT}</head>\r\n<body>\r\n{$BODY}</body>\r\n</html>\r\n".replace("{$BODY}", body == null ? "" : body).replace("{$TITLE}", title == null ? "" : title).replace("{$STYLE}", style == null ? "" : "<style type=\"text/css\">{$STYLE}</style>\r\n".replace("{$STYLE}", style)).replace("{$SCRIPT}", script == null ? "" : "<script src=\"//ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js\"></script>\r\n<script src=\"//ajax.googleapis.com/ajax/libs/jqueryui/1.10.3/jquery-ui.min.js\"></script>\r\n<script>\r\n{$SCRIPT}\r\n</script>\r\n".replace("{$SCRIPT}", script));
/*    */   }
/*    */ }


/* Location:              C:\Users\SilentBloodGaming\Desktop\MasterFreedomMod.jar!\me\StevenLawson\TotalFreedomMod\HTTPD\TFM_HTTPD_PageBuilder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */