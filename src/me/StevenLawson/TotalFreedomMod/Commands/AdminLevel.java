/*    */ package me.StevenLawson.TotalFreedomMod.Commands;
/*    */ 
/*    */ public enum AdminLevel
/*    */ {
/*  5 */   ALL("All Player Commands"),  OP("OP Commands"),  SUPER("SuperAdmin Commands"),  SENIOR("Senior Admin Commands");
/*    */   
/*    */   private final String friendlyName;
/*    */   
/*    */   private AdminLevel(String friendlyName)
/*    */   {
/* 11 */     this.friendlyName = friendlyName;
/*    */   }
/*    */   
/*    */   public String getFriendlyName()
/*    */   {
/* 16 */     return friendlyName;
/*    */   }
/*    */ }


/* Location:              C:\Users\SilentBloodGaming\Desktop\MasterFreedomMod.jar!\me\StevenLawson\TotalFreedomMod\Commands\AdminLevel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */