/*     */ package me.StevenLawson.TotalFreedomMod.Commands;
/*     */ 
/*     */ import java.util.List;
/*     */ import me.StevenLawson.TotalFreedomMod.TFM_AdminList;
/*     */ import me.StevenLawson.TotalFreedomMod.TFM_Util;
/*     */ import me.StevenLawson.TotalFreedomMod.World.TFM_AdminWorld;
/*     */ import me.StevenLawson.TotalFreedomMod.World.TFM_AdminWorld.TimeOfDay;
/*     */ import me.StevenLawson.TotalFreedomMod.World.TFM_AdminWorld.WeatherMode;
/*     */ import org.bukkit.Server;
/*     */ import org.bukkit.World;
/*     */ import org.bukkit.command.Command;
/*     */ import org.bukkit.command.CommandSender;
/*     */ import org.bukkit.entity.Player;
/*     */ 
/*     */ @CommandPermissions(level=AdminLevel.OP, source=SourceType.BOTH)
/*     */ @CommandParameters(description="Go to the AdminWorld.", usage="/<command> [guest < list | purge | add <player> | remove <player> > | time <morning | noon | evening | night> | weather <off | on | storm>]")
/*     */ public class Command_adminworld
/*     */   extends TFM_Command
/*     */ {
/*     */   private static enum CommandMode
/*     */   {
/*  17 */     TELEPORT,  GUEST,  TIME,  WEATHER;
/*     */     
/*     */     private CommandMode() {}
/*     */   }
/*     */   
/*     */   public boolean run(CommandSender sender, Player sender_p, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
/*     */   {
/*  23 */     CommandMode commandMode = null;
/*  25 */     if (args.length == 0) {
/*  27 */       commandMode = CommandMode.TELEPORT;
/*  29 */     } else if (args.length >= 2) {
/*  31 */       if ("guest".equalsIgnoreCase(args[0])) {
/*  33 */         commandMode = CommandMode.GUEST;
/*  35 */       } else if ("time".equalsIgnoreCase(args[0])) {
/*  37 */         commandMode = CommandMode.TIME;
/*  39 */       } else if ("weather".equalsIgnoreCase(args[0])) {
/*  41 */         commandMode = CommandMode.WEATHER;
/*     */       }
/*     */     }
/*  45 */     if (commandMode == null) {
/*  47 */       return false;
/*     */     }
/*     */     try
/*     */     {
/*  52 */       switch (commandMode)
/*     */       {
/*     */       case TELEPORT: 
/*  56 */         if ((!(sender instanceof Player)) || (sender_p == null)) {
/*  58 */           return true;
/*     */         }
/*  61 */         World adminWorld = null;
/*     */         try
/*     */         {
/*  64 */           adminWorld = TFM_AdminWorld.getInstance().getWorld();
/*     */         }
/*     */         catch (Exception ex) {}
/*  70 */         if ((adminWorld == null) || (sender_p.getWorld() == adminWorld))
/*     */         {
/*  72 */           playerMsg("Going to the main world.");
/*  73 */           sender_p.teleport(((World)server.getWorlds().get(0)).getSpawnLocation());
/*     */         }
/*  77 */         else if (TFM_AdminWorld.getInstance().canAccessWorld(sender_p))
/*     */         {
/*  79 */           playerMsg("Going to the AdminWorld.");
/*  80 */           TFM_AdminWorld.getInstance().sendToWorld(sender_p);
/*     */         }
/*     */         else
/*     */         {
/*  84 */           playerMsg("You don't have permission to access the AdminWorld.");
/*     */         }
/*  88 */         break;
/*     */       case GUEST: 
/*  92 */         if (args.length == 2)
/*     */         {
/*  94 */           if ("list".equalsIgnoreCase(args[1]))
/*     */           {
/*  96 */             playerMsg("AdminWorld guest list: " + TFM_AdminWorld.getInstance().guestListToString());
/*     */           }
/*  98 */           else if ("purge".equalsIgnoreCase(args[1]))
/*     */           {
/* 100 */             assertCommandPerms(sender, sender_p);
/* 101 */             TFM_AdminWorld.getInstance().purgeGuestList();
/* 102 */             TFM_Util.adminAction(sender.getName(), "AdminWorld guest list purged.", false);
/*     */           }
/*     */           else
/*     */           {
/* 106 */             return false;
/*     */           }
/*     */         }
/* 109 */         else if (args.length == 3)
/*     */         {
/* 111 */           assertCommandPerms(sender, sender_p);
/* 113 */           if ("add".equalsIgnoreCase(args[1]))
/*     */           {
/* 115 */             Player player = getPlayer(args[2]);
/* 117 */             if (player == null)
/*     */             {
/* 119 */               sender.sendMessage(TFM_Command.PLAYER_NOT_FOUND);
/* 120 */               return true;
/*     */             }
/* 123 */             if (TFM_AdminWorld.getInstance().addGuest(player, sender_p)) {
/* 125 */               TFM_Util.adminAction(sender.getName(), "AdminWorld guest added: " + player.getName(), false);
/*     */             } else {
/* 129 */               playerMsg("Could not add player to guest list.");
/*     */             }
/*     */           }
/* 132 */           else if ("remove".equals(args[1]))
/*     */           {
/* 134 */             Player player = TFM_AdminWorld.getInstance().removeGuest(args[2]);
/* 135 */             if (player != null) {
/* 137 */               TFM_Util.adminAction(sender.getName(), "AdminWorld guest removed: " + player.getName(), false);
/*     */             } else {
/* 141 */               playerMsg("Can't find guest entry for: " + args[2]);
/*     */             }
/*     */           }
/*     */           else
/*     */           {
/* 146 */             return false;
/*     */           }
/*     */         }
/*     */         break;
/*     */       case TIME: 
/* 154 */         assertCommandPerms(sender, sender_p);
/* 156 */         if (args.length == 2)
/*     */         {
/* 158 */           TFM_AdminWorld.TimeOfDay timeOfDay = TFM_AdminWorld.TimeOfDay.getByAlias(args[1]);
/* 159 */           if (timeOfDay != null)
/*     */           {
/* 161 */             TFM_AdminWorld.getInstance().setTimeOfDay(timeOfDay);
/* 162 */             playerMsg("AdminWorld time set to: " + timeOfDay.name());
/*     */           }
/*     */           else
/*     */           {
/* 166 */             playerMsg("Invalid time of day. Can be: sunrise, noon, sunset, midnight");
/*     */           }
/*     */         }
/*     */         else
/*     */         {
/* 171 */           return false;
/*     */         }
/*     */         break;
/*     */       case WEATHER: 
/* 178 */         assertCommandPerms(sender, sender_p);
/* 180 */         if (args.length == 2)
/*     */         {
/* 182 */           TFM_AdminWorld.WeatherMode weatherMode = TFM_AdminWorld.WeatherMode.getByAlias(args[1]);
/* 183 */           if (weatherMode != null)
/*     */           {
/* 185 */             TFM_AdminWorld.getInstance().setWeatherMode(weatherMode);
/* 186 */             playerMsg("AdminWorld weather set to: " + weatherMode.name());
/*     */           }
/*     */           else
/*     */           {
/* 190 */             playerMsg("Invalid weather mode. Can be: off, rain, storm");
/*     */           }
/*     */         }
/*     */         else
/*     */         {
/* 195 */           return false;
/*     */         }
/*     */         break;
/*     */       default: 
/* 202 */         return false;
/*     */       }
/*     */     }
/*     */     catch (PermissionDeniedException ex)
/*     */     {
/* 208 */       sender.sendMessage(ex.getMessage());
/*     */     }
/* 211 */     return true;
/*     */   }
/*     */   
/*     */   private void assertCommandPerms(CommandSender sender, Player sender_p)
/*     */     throws Command_adminworld.PermissionDeniedException
/*     */   {
/* 216 */     if ((!(sender instanceof Player)) || (sender_p == null) || (!TFM_AdminList.isSuperAdmin(sender))) {
/* 218 */       throw new PermissionDeniedException(TFM_Command.MSG_NO_PERMS, null);
/*     */     }
/*     */   }
/*     */   
/*     */   private class PermissionDeniedException
/*     */     extends Exception
/*     */   {
/*     */     private static final long serialVersionUID = 1L;
/*     */     
/*     */     private PermissionDeniedException(String string)
/*     */     {
/* 228 */       super();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\SilentBloodGaming\Desktop\MasterFreedomMod.jar!\me\StevenLawson\TotalFreedomMod\Commands\Command_adminworld.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */