/*     */ package me.StevenLawson.TotalFreedomMod.Commands;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ import me.StevenLawson.TotalFreedomMod.TFM_DepreciationAggregator;
/*     */ import me.StevenLawson.TotalFreedomMod.TotalFreedomMod;
/*     */ import org.bukkit.Bukkit;
/*     */ import org.bukkit.Location;
/*     */ import org.bukkit.Material;
/*     */ import org.bukkit.block.Block;
/*     */ import org.bukkit.command.Command;
/*     */ import org.bukkit.command.CommandSender;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.event.EventHandler;
/*     */ import org.bukkit.event.EventPriority;
/*     */ import org.bukkit.event.HandlerList;
/*     */ import org.bukkit.event.Listener;
/*     */ import org.bukkit.event.player.PlayerMoveEvent;
/*     */ import org.bukkit.plugin.PluginManager;
/*     */ import org.bukkit.plugin.RegisteredListener;
/*     */ 
/*     */ @CommandPermissions(level=AdminLevel.SUPER, source=SourceType.ONLY_IN_GAME)
/*     */ @CommandParameters(description="Pretty rainbow trails.", usage="/<command> [off]")
/*     */ public class Command_trail
/*     */   extends TFM_Command
/*     */ {
/*  24 */   private static Listener movementListener = null;
/*  25 */   private static final List<Player> trailPlayers = new ArrayList();
/*  26 */   private static final Random RANDOM = new Random();
/*     */   
/*     */   public boolean run(CommandSender sender, Player sender_p, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
/*     */   {
/*  31 */     if ((args.length > 0) && ("off".equals(args[0])))
/*     */     {
/*  33 */       trailPlayers.remove(sender_p);
/*     */       
/*  35 */       playerMsg("Trail disabled.");
/*     */     }
/*     */     else
/*     */     {
/*  39 */       if (!trailPlayers.contains(sender_p)) {
/*  41 */         trailPlayers.add(sender_p);
/*     */       }
/*  44 */       playerMsg("Trail enabled. Use \"/trail off\" to disable.");
/*     */     }
/*  47 */     if (!trailPlayers.isEmpty()) {
/*  49 */       registerMovementHandler();
/*     */     } else {
/*  53 */       unregisterMovementHandler();
/*     */     }
/*  56 */     return true;
/*     */   }
/*     */   
/*     */   private static void registerMovementHandler()
/*     */   {
/*  61 */     if (getRegisteredListener(movementListener) == null) {
/*  63 */       Bukkit.getPluginManager().registerEvents( = new Listener()
/*     */       {
/*     */         @EventHandler(priority=EventPriority.NORMAL)
/*     */         public void onPlayerMove(PlayerMoveEvent event)
/*     */         {
/*  68 */           Player player = event.getPlayer();
/*  69 */           if (Command_trail.trailPlayers.contains(player))
/*     */           {
/*  71 */             Block fromBlock = event.getFrom().getBlock();
/*  72 */             if (fromBlock.isEmpty())
/*     */             {
/*  74 */               Block toBlock = event.getTo().getBlock();
/*  75 */               if (!fromBlock.equals(toBlock))
/*     */               {
/*  77 */                 fromBlock.setType(Material.WOOL);
/*  78 */                 TFM_DepreciationAggregator.setData_Block(fromBlock, (byte)Command_trail.RANDOM.nextInt(16));
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*  78 */       }, TotalFreedomMod.plugin);
/*     */     }
/*     */   }
/*     */   
/*     */   private static void unregisterMovementHandler()
/*     */   {
/*  89 */     Listener registeredListener = getRegisteredListener(movementListener);
/*  90 */     if (registeredListener != null) {
/*  92 */       PlayerMoveEvent.getHandlerList().unregister(registeredListener);
/*     */     }
/*     */   }
/*     */   
/*     */   private static Listener getRegisteredListener(Listener listener)
/*     */   {
/*  98 */     RegisteredListener[] registeredListeners = PlayerMoveEvent.getHandlerList().getRegisteredListeners();
/*  99 */     for (RegisteredListener registeredListener : registeredListeners) {
/* 101 */       if (registeredListener.getListener() == listener) {
/* 103 */         return listener;
/*     */       }
/*     */     }
/* 106 */     return null;
/*     */   }
/*     */   
/*     */   public static void startTrail(Player player)
/*     */   {
/* 111 */     if (!trailPlayers.contains(player)) {
/* 113 */       trailPlayers.add(player);
/*     */     }
/* 116 */     if (!trailPlayers.isEmpty()) {
/* 118 */       registerMovementHandler();
/*     */     }
/*     */   }
/*     */   
/*     */   public static void stopTrail(Player player)
/*     */   {
/* 124 */     trailPlayers.remove(player);
/* 126 */     if (trailPlayers.isEmpty()) {
/* 128 */       unregisterMovementHandler();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\SilentBloodGaming\Desktop\MasterFreedomMod.jar!\me\StevenLawson\TotalFreedomMod\Commands\Command_trail.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */