/*     */ package me.StevenLawson.TotalFreedomMod.Listener;
/*     */ 
/*     */ import me.StevenLawson.TotalFreedomMod.Config.TFM_ConfigEntry;
/*     */ import org.bukkit.Location;
/*     */ import org.bukkit.World;
/*     */ import org.bukkit.entity.Bat;
/*     */ import org.bukkit.entity.EnderDragon;
/*     */ import org.bukkit.entity.Entity;
/*     */ import org.bukkit.entity.EntityType;
/*     */ import org.bukkit.entity.Ghast;
/*     */ import org.bukkit.entity.Giant;
/*     */ import org.bukkit.entity.HumanEntity;
/*     */ import org.bukkit.entity.Projectile;
/*     */ import org.bukkit.entity.Slime;
/*     */ import org.bukkit.entity.Tameable;
/*     */ import org.bukkit.event.EventHandler;
/*     */ import org.bukkit.event.EventPriority;
/*     */ import org.bukkit.event.Listener;
/*     */ import org.bukkit.event.entity.CreatureSpawnEvent;
/*     */ import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
/*     */ import org.bukkit.event.entity.EntityCombustEvent;
/*     */ import org.bukkit.event.entity.EntityDamageEvent;
/*     */ import org.bukkit.event.entity.EntityDeathEvent;
/*     */ import org.bukkit.event.entity.EntityExplodeEvent;
/*     */ import org.bukkit.event.entity.ExplosionPrimeEvent;
/*     */ import org.bukkit.event.entity.ProjectileHitEvent;
/*     */ 
/*     */ public class TFM_EntityListener
/*     */   implements Listener
/*     */ {
/*     */   @EventHandler(priority=EventPriority.HIGH)
/*     */   public void onEntityExplode(EntityExplodeEvent event)
/*     */   {
/*  30 */     if (!TFM_ConfigEntry.ALLOW_EXPLOSIONS.getBoolean().booleanValue())
/*     */     {
/*  32 */       event.setCancelled(true);
/*  33 */       return;
/*     */     }
/*  36 */     event.setYield(0.0F);
/*     */   }
/*     */   
/*     */   @EventHandler(priority=EventPriority.HIGH)
/*     */   public void onExplosionPrime(ExplosionPrimeEvent event)
/*     */   {
/*  42 */     if (!TFM_ConfigEntry.ALLOW_EXPLOSIONS.getBoolean().booleanValue())
/*     */     {
/*  44 */       event.setCancelled(true);
/*  45 */       return;
/*     */     }
/*  48 */     event.setRadius((float)TFM_ConfigEntry.EXPLOSIVE_RADIUS.getDouble().doubleValue());
/*     */   }
/*     */   
/*     */   @EventHandler(priority=EventPriority.HIGH)
/*     */   public void onEntityCombust(EntityCombustEvent event)
/*     */   {
/*  54 */     if (!TFM_ConfigEntry.ALLOW_EXPLOSIONS.getBoolean().booleanValue()) {
/*  56 */       event.setCancelled(true);
/*     */     }
/*     */   }
/*     */   
/*     */   @EventHandler(priority=EventPriority.HIGH)
/*     */   public void onEntityDamage(EntityDamageEvent event)
/*     */   {
/*  63 */     switch (event.getCause())
/*     */     {
/*     */     case LAVA: 
/*  67 */       if (!TFM_ConfigEntry.ALLOW_LAVA_DAMAGE.getBoolean().booleanValue())
/*     */       {
/*  69 */         event.setCancelled(true); return;
/*     */       }
/*     */       break;
/*     */     }
/*  75 */     if (TFM_ConfigEntry.ENABLE_PET_PROTECT.getBoolean().booleanValue())
/*     */     {
/*  77 */       Entity entity = event.getEntity();
/*  78 */       if ((entity instanceof Tameable)) {
/*  80 */         if (((Tameable)entity).isTamed()) {
/*  82 */           event.setCancelled(true);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   @EventHandler(priority=EventPriority.NORMAL)
/*     */   public void onCreatureSpawn(CreatureSpawnEvent event)
/*     */   {
/*  91 */     if (TFM_ConfigEntry.MOB_LIMITER_ENABLED.getBoolean().booleanValue())
/*     */     {
/*  93 */       if (event.getSpawnReason().equals(CreatureSpawnEvent.SpawnReason.EGG))
/*     */       {
/*  95 */         event.setCancelled(true);
/*  96 */         return;
/*     */       }
/*  99 */       Entity spawned = event.getEntity();
/* 101 */       if ((spawned instanceof EnderDragon))
/*     */       {
/* 103 */         if (TFM_ConfigEntry.MOB_LIMITER_DISABLE_DRAGON.getBoolean().booleanValue()) {
/* 105 */           event.setCancelled(true);
/*     */         }
/*     */       }
/* 109 */       else if ((spawned instanceof Ghast))
/*     */       {
/* 111 */         if (TFM_ConfigEntry.MOB_LIMITER_DISABLE_GHAST.getBoolean().booleanValue()) {
/* 113 */           event.setCancelled(true);
/*     */         }
/*     */       }
/* 117 */       else if ((spawned instanceof Slime))
/*     */       {
/* 119 */         if (TFM_ConfigEntry.MOB_LIMITER_DISABLE_SLIME.getBoolean().booleanValue()) {
/* 121 */           event.setCancelled(true);
/*     */         }
/*     */       }
/* 125 */       else if ((spawned instanceof Giant))
/*     */       {
/* 127 */         if (TFM_ConfigEntry.MOB_LIMITER_DISABLE_GIANT.getBoolean().booleanValue()) {
/* 129 */           event.setCancelled(true);
/*     */         }
/*     */       }
/* 133 */       else if ((spawned instanceof Bat))
/*     */       {
/* 135 */         event.setCancelled(true);
/* 136 */         return;
/*     */       }
/* 139 */       int mobLimiterMax = TFM_ConfigEntry.MOB_LIMITER_MAX.getInteger().intValue();
/* 141 */       if (mobLimiterMax > 0)
/*     */       {
/* 143 */         int mobcount = 0;
/* 145 */         for (Entity entity : event.getLocation().getWorld().getLivingEntities()) {
/* 147 */           if (!(entity instanceof HumanEntity)) {
/* 149 */             mobcount++;
/*     */           }
/*     */         }
/* 153 */         if (mobcount > mobLimiterMax) {
/* 155 */           event.setCancelled(true);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   @EventHandler(priority=EventPriority.HIGH)
/*     */   public void onEntityDeath(EntityDeathEvent event)
/*     */   {
/* 164 */     if (TFM_ConfigEntry.AUTO_ENTITY_WIPE.getBoolean().booleanValue()) {
/* 166 */       event.setDroppedExp(0);
/*     */     }
/*     */   }
/*     */   
/*     */   @EventHandler(priority=EventPriority.NORMAL)
/*     */   public void onProjectileHit(ProjectileHitEvent event)
/*     */   {
/* 173 */     if (TFM_ConfigEntry.ALLOW_EXPLOSIONS.getBoolean().booleanValue())
/*     */     {
/* 175 */       Projectile entity = event.getEntity();
/* 176 */       if (event.getEntityType() == EntityType.ARROW) {
/* 178 */         entity.getWorld().createExplosion(entity.getLocation(), 2.0F);
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\SilentBloodGaming\Desktop\MasterFreedomMod.jar!\me\StevenLawson\TotalFreedomMod\Listener\TFM_EntityListener.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */