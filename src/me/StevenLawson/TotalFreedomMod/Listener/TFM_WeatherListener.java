/*    */ package me.StevenLawson.TotalFreedomMod.Listener;
/*    */ 
/*    */ import me.StevenLawson.TotalFreedomMod.Config.TFM_ConfigEntry;
/*    */ import me.StevenLawson.TotalFreedomMod.World.TFM_AdminWorld;
/*    */ import me.StevenLawson.TotalFreedomMod.World.TFM_AdminWorld.WeatherMode;
/*    */ import org.bukkit.event.EventHandler;
/*    */ import org.bukkit.event.EventPriority;
/*    */ import org.bukkit.event.Listener;
/*    */ import org.bukkit.event.weather.ThunderChangeEvent;
/*    */ import org.bukkit.event.weather.WeatherChangeEvent;
/*    */ 
/*    */ public class TFM_WeatherListener
/*    */   implements Listener
/*    */ {
/*    */   @EventHandler(priority=EventPriority.HIGH)
/*    */   public void onThunderChange(ThunderChangeEvent event)
/*    */   {
/*    */     try
/*    */     {
/* 18 */       if ((event.getWorld() == TFM_AdminWorld.getInstance().getWorld()) && (TFM_AdminWorld.getInstance().getWeatherMode() != TFM_AdminWorld.WeatherMode.OFF)) {
/* 20 */         return;
/*    */       }
/*    */     }
/*    */     catch (Exception ex) {}
/* 27 */     if ((event.toThunderState()) && (TFM_ConfigEntry.DISABLE_WEATHER.getBoolean().booleanValue()))
/*    */     {
/* 29 */       event.setCancelled(true);
/* 30 */       return;
/*    */     }
/*    */   }
/*    */   
/*    */   @EventHandler(priority=EventPriority.HIGH)
/*    */   public void onWeatherChange(WeatherChangeEvent event)
/*    */   {
/*    */     try
/*    */     {
/* 39 */       if ((event.getWorld() == TFM_AdminWorld.getInstance().getWorld()) && (TFM_AdminWorld.getInstance().getWeatherMode() != TFM_AdminWorld.WeatherMode.OFF)) {
/* 41 */         return;
/*    */       }
/*    */     }
/*    */     catch (Exception ex) {}
/* 48 */     if ((event.toWeatherState()) && (TFM_ConfigEntry.DISABLE_WEATHER.getBoolean().booleanValue()))
/*    */     {
/* 50 */       event.setCancelled(true);
/* 51 */       return;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\SilentBloodGaming\Desktop\MasterFreedomMod.jar!\me\StevenLawson\TotalFreedomMod\Listener\TFM_WeatherListener.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */