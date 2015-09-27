/*     */ package me.StevenLawson.TotalFreedomMod.Commands;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import me.StevenLawson.TotalFreedomMod.TFM_AdminList;
/*     */ import me.StevenLawson.TotalFreedomMod.TFM_Util;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ import org.bukkit.ChatColor;
/*     */ import org.bukkit.Server;
/*     */ import org.bukkit.command.Command;
/*     */ import org.bukkit.command.CommandSender;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.potion.PotionEffect;
/*     */ import org.bukkit.potion.PotionEffectType;
/*     */ 
/*     */ @CommandPermissions(level=AdminLevel.OP, source=SourceType.BOTH)
/*     */ @CommandParameters(description="Manipulate potion effects. Duration is measured in server ticks (~20 ticks per second).", usage="/<command> <list | clear [target name] | add <type> <duration> <amplifier> [target name]>")
/*     */ public class Command_potion
/*     */   extends TFM_Command
/*     */ {
/*     */   public boolean run(CommandSender sender, Player sender_p, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
/*     */   {
/*  24 */     if ((args.length == 1) || (args.length == 2))
/*     */     {
/*  26 */       if (args[0].equalsIgnoreCase("list"))
/*     */       {
/*  28 */         List<String> potionEffectTypeNames = new ArrayList();
/*  29 */         for (PotionEffectType potion_effect_type : PotionEffectType.values()) {
/*  31 */           if (potion_effect_type != null) {
/*  33 */             potionEffectTypeNames.add(potion_effect_type.getName());
/*     */           }
/*     */         }
/*  36 */         playerMsg("Potion effect types: " + StringUtils.join(potionEffectTypeNames, ", "), ChatColor.AQUA);
/*     */       }
/*     */       else
/*     */       {
/*     */         Iterator i$;
/*     */         Player target;
/*  38 */         if (args[0].equalsIgnoreCase("clearall"))
/*     */         {
/*  40 */           if ((!TFM_AdminList.isSuperAdmin(sender)) && (!senderIsConsole))
/*     */           {
/*  42 */             playerMsg(TFM_Command.MSG_NO_PERMS);
/*  43 */             return true;
/*     */           }
/*  45 */           TFM_Util.adminAction(sender.getName(), "Cleared all potion effects from all players", true);
/*  46 */           for (i$ = server.getOnlinePlayers().iterator(); i$.hasNext();)
/*     */           {
/*  46 */             target = (Player)i$.next();
/*  48 */             for (PotionEffect potion_effect : target.getActivePotionEffects()) {
/*  50 */               target.removePotionEffect(potion_effect.getType());
/*     */             }
/*     */           }
/*     */         }
/*  54 */         else if (args[0].equalsIgnoreCase("clear"))
/*     */         {
/*  56 */           Player target = sender_p;
/*  58 */           if (args.length == 2)
/*     */           {
/*  60 */             target = getPlayer(args[1]);
/*  62 */             if (target == null)
/*     */             {
/*  64 */               playerMsg(TFM_Command.PLAYER_NOT_FOUND, ChatColor.RED);
/*  65 */               return true;
/*     */             }
/*     */           }
/*  69 */           if (!target.equals(sender_p))
/*     */           {
/*  71 */             if (!TFM_AdminList.isSuperAdmin(sender))
/*     */             {
/*  73 */               playerMsg("Only superadmins can clear potion effects from other players.");
/*  74 */               return true;
/*     */             }
/*     */           }
/*  77 */           else if (senderIsConsole)
/*     */           {
/*  79 */             playerMsg("You must specify a target player when using this command from the console.");
/*  80 */             return true;
/*     */           }
/*  83 */           for (PotionEffect potion_effect : target.getActivePotionEffects()) {
/*  85 */             target.removePotionEffect(potion_effect.getType());
/*     */           }
/*  88 */           playerMsg("Cleared all active potion effects " + (!target.equals(sender_p) ? "from player " + target.getName() + "." : "from yourself."), ChatColor.AQUA);
/*     */         }
/*     */         else
/*     */         {
/*  92 */           return false;
/*     */         }
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/*  95 */       if ((args.length == 4) || (args.length == 5))
/*     */       {
/*  97 */         if (args[0].equalsIgnoreCase("add"))
/*     */         {
/*  99 */           Player target = sender_p;
/* 101 */           if (args.length == 5)
/*     */           {
/* 104 */             target = getPlayer(args[4]);
/* 106 */             if (target == null)
/*     */             {
/* 108 */               playerMsg(TFM_Command.PLAYER_NOT_FOUND, ChatColor.RED);
/* 109 */               return true;
/*     */             }
/*     */           }
/* 113 */           if (!target.equals(sender_p))
/*     */           {
/* 115 */             if (!TFM_AdminList.isSuperAdmin(sender))
/*     */             {
/* 117 */               sender.sendMessage("Only superadmins can apply potion effects to other players.");
/* 118 */               return true;
/*     */             }
/*     */           }
/* 121 */           else if (senderIsConsole)
/*     */           {
/* 123 */             sender.sendMessage("You must specify a target player when using this command from the console.");
/* 124 */             return true;
/*     */           }
/* 127 */           PotionEffectType potion_effect_type = PotionEffectType.getByName(args[1]);
/* 128 */           if (potion_effect_type == null)
/*     */           {
/* 130 */             sender.sendMessage(ChatColor.AQUA + "Invalid potion effect type.");
/* 131 */             return true;
/*     */           }
/*     */           int duration;
/*     */           try
/*     */           {
/* 137 */             duration = Integer.parseInt(args[2]);
/* 138 */             duration = Math.min(duration, 100000);
/*     */           }
/*     */           catch (NumberFormatException ex)
/*     */           {
/* 142 */             playerMsg("Invalid potion duration.", ChatColor.RED);
/* 143 */             return true;
/*     */           }
/*     */           int amplifier;
/*     */           try
/*     */           {
/* 149 */             amplifier = Integer.parseInt(args[3]);
/* 150 */             amplifier = Math.min(amplifier, 100000);
/*     */           }
/*     */           catch (NumberFormatException ex)
/*     */           {
/* 154 */             playerMsg("Invalid potion amplifier.", ChatColor.RED);
/* 155 */             return true;
/*     */           }
/* 158 */           PotionEffect new_effect = potion_effect_type.createEffect(duration, amplifier);
/* 159 */           target.addPotionEffect(new_effect, true);
/* 160 */           playerMsg("Added potion effect: " + new_effect.getType().getName() + ", Duration: " + new_effect.getDuration() + ", Amplifier: " + new_effect.getAmplifier() + (!target.equals(sender_p) ? " to player " + target.getName() + "." : " to yourself."), ChatColor.AQUA);
/*     */           
/* 166 */           return true;
/*     */         }
/* 170 */         return false;
/*     */       }
/* 175 */       return false;
/*     */     }
/* 177 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\SilentBloodGaming\Desktop\MasterFreedomMod.jar!\me\StevenLawson\TotalFreedomMod\Commands\Command_potion.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */