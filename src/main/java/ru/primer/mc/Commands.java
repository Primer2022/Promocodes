package ru.primer.mc;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Commands implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender s, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        Player p = (Player) s;
        String argsZeroPromocodes = "promocodes." + args[0];
        String argsOnePromocodes = "promocodes." + args[1];
        FileConfiguration cfg = Main.instance.getConfig();
        if (s instanceof Player) {
            if (args.length <= 0 && !p.isOp()) {
                cfg.getStringList("messages.help-player").forEach(message -> p.sendMessage(ChatColor.translateAlternateColorCodes('&', message)));
                return true;
            } else if (args.length <= 0 && p.isOp()) {
                cfg.getStringList("messages.help-admin").forEach(message -> p.sendMessage(ChatColor.translateAlternateColorCodes('&', message)));
                return true;
            }
            if (args.length > 0) {
                if (p.isOp()) {
                    switch (args[0]) {
                        case ("reload"):
                            p.sendMessage(ChatColor.translateAlternateColorCodes('&', cfg.getString("messages.reload-config")));
                            Main.instance.reloadConfig();
                            Main.instance.saveDefaultConfig();
                        break;
                        case ("list"):
                            if (cfg.getConfigurationSection("promocodes").getKeys(false).isEmpty()) {
                                p.sendMessage(ChatColor.translateAlternateColorCodes('&', cfg.getString("messages.no-promocode-list")));
                                return true;
                            }
                            List<String> list = new ArrayList<>(cfg.getConfigurationSection("promocodes").getKeys(false));
                            p.sendMessage(ChatColor.translateAlternateColorCodes('&', cfg.getString("messages.promocode-list")));
                            for (int i = 0; i < list.size(); i++) {
                                int amount = i + 1;
                                p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7" + amount + ".&f " + list.get(i)));
                            }
                        break;
                        case ("create"):
                            if (args.length >= 4) {
                                if (cfg.contains(argsOnePromocodes)) {
                                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', cfg.getString("messages.already-created")));
                                    return true;
                                }
                                try {
                                    int number = Integer.parseInt(args[2]);
                                    StringBuilder sb = new StringBuilder();
                                    for (int i = 3; i < args.length; i++) sb.append(args[i]).append(' ');
                                    if (sb.length() > 0) sb.deleteCharAt(sb.length() - 1);
                                    String command = sb.toString();
                                    Date date = new Date();
                                    SimpleDateFormat formatForDateNow = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");
                                    int uses = Integer.parseInt(args[2]);
                                    int usesstart = Integer.parseInt(args[2]);
                                    List<String> listcreate = new ArrayList<>();
                                    listcreate.add(command);
                                    cfg.set(argsOnePromocodes + ".uses", uses);
                                    cfg.set(argsOnePromocodes + ".uses-start", usesstart);
                                    cfg.set(argsOnePromocodes + ".commands", listcreate);
                                    cfg.set(argsOnePromocodes + ".date", formatForDateNow.format(date));
                                    Main.instance.saveConfig();
                                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', cfg.getString("messages.succes-created")));
                                    return true;
                                } catch (NumberFormatException e) {
                                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', cfg.getString("messages.no-int")));
                                }
                            }
                            if (!p.isOp()) {
                                cfg.getStringList("messages.help-player").forEach(message -> p.sendMessage(ChatColor.translateAlternateColorCodes('&', message)));
                                return true;
                            }
                            cfg.getStringList("messages.help-admin").forEach(message -> p.sendMessage(ChatColor.translateAlternateColorCodes('&', message)));
                        break;
                        case ("delete"):
                            if (args.length == 2) {
                                if (cfg.contains(argsOnePromocodes)) {
                                    cfg.set(argsOnePromocodes, null);
                                    Main.instance.saveConfig();
                                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', cfg.getString("messages.succes-deleted")));
                                    return true;
                                }
                                p.sendMessage(ChatColor.translateAlternateColorCodes('&', cfg.getString("messages.invalid-promocode")));
                                return true;
                            }
                            if (!p.isOp()) {
                                cfg.getStringList("messages.help-player").forEach(message -> p.sendMessage(ChatColor.translateAlternateColorCodes('&', message)));
                                return true;
                            }
                            cfg.getStringList("messages.help-admin").forEach(message -> p.sendMessage(ChatColor.translateAlternateColorCodes('&', message)));
                        break;
                        case ("addcmd"):
                            if (args.length >= 3) {
                                if (cfg.contains(argsOnePromocodes)) {
                                    StringBuilder sb = new StringBuilder();
                                    for (int i = 2; i < args.length; i++) sb.append(args[i]).append(' ');
                                    if (sb.length() > 0) sb.deleteCharAt(sb.length() - 1);
                                    String command = sb.toString();
                                    List<String> listaddcmd = cfg.getStringList(argsOnePromocodes + ".commands");
                                    listaddcmd.add(command);
                                    cfg.set(argsOnePromocodes + ".commands", listaddcmd);
                                    Main.instance.saveConfig();
                                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', cfg.getString("messages.add-command")));
                                    return true;
                                }
                                p.sendMessage(ChatColor.translateAlternateColorCodes('&', cfg.getString("messages.invalid-promocode")));
                                return true;
                            }
                            if (!p.isOp()) {
                                cfg.getStringList("messages.help-player").forEach(message -> p.sendMessage(ChatColor.translateAlternateColorCodes('&', message)));
                                return true;
                            }
                            cfg.getStringList("messages.help-admin").forEach(message -> p.sendMessage(ChatColor.translateAlternateColorCodes('&', message)));
                        break;
                        case ("delcmd"):
                            if (args.length >= 3) {
                                if (cfg.contains(argsOnePromocodes)) {
                                    int i = Integer.parseInt(args[2]);
                                    int amount = i - 1;
                                    List<String> listdelcmd = cfg.getStringList(argsOnePromocodes + ".commands");
                                    if (listdelcmd.get(amount) != null) {
                                        listdelcmd.set(amount, null);
                                        cfg.set(argsOnePromocodes + ".commands", listdelcmd);
                                        Main.instance.saveConfig();
                                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', cfg.getString("messages.del-command")));
                                        return true;
                                    }
                                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', cfg.getString("messages.no-command")));
                                    return true;
                                }
                                p.sendMessage(ChatColor.translateAlternateColorCodes('&', cfg.getString("messages.invalid-promocode")));
                                return true;
                            }
                            if (!p.isOp()) {
                                cfg.getStringList("messages.help-player").forEach(message -> p.sendMessage(ChatColor.translateAlternateColorCodes('&', message)));
                                return true;
                            }
                            cfg.getStringList("messages.help-admin").forEach(message -> p.sendMessage(ChatColor.translateAlternateColorCodes('&', message)));
                        break;
                        case ("info"):
                            if (args.length >= 2) {
                                if (cfg.contains(argsOnePromocodes)) {
                                    String promocode = args[1];
                                    int usesamountint = cfg.getInt(argsOnePromocodes + ".uses");
                                    int usesstartint = cfg.getInt(argsOnePromocodes + ".uses-start");
                                    String usesamount = String.valueOf(usesamountint);
                                    String usesstart = String.valueOf(usesstartint);
                                    String date = cfg.getString(argsOnePromocodes + ".date");
                                    cfg.getStringList("messages.promocode-info").forEach(message -> p.sendMessage(ChatColor.translateAlternateColorCodes('&', message.replaceAll("%promocode%", promocode).replaceAll("%uses-amount%", String.valueOf(usesamount)).replaceAll("%uses-start%", String.valueOf(usesstart)).replaceAll("%date%", date))));
                                    List<String> listinfo = new ArrayList<>(cfg.getStringList(argsOnePromocodes + ".commands"));
                                    for (int i = 0; i < listinfo.size(); i++) {
                                        int amount = i + 1;
                                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7" + amount + ".&f " + listinfo.get(i)));
                                    }
                                }
                                p.sendMessage(ChatColor.translateAlternateColorCodes('&', cfg.getString("messages.invalid-promocode")));
                                return true;
                            }
                            if (!p.isOp()) {
                                cfg.getStringList("messages.help-player").forEach(message -> p.sendMessage(ChatColor.translateAlternateColorCodes('&', message)));
                                return true;
                            }
                            cfg.getStringList("messages.help-admin").forEach(message -> p.sendMessage(ChatColor.translateAlternateColorCodes('&', message)));
                        break;
                    }
                }
                if (args.length == 1) {
                    if (cfg.contains(argsZeroPromocodes)) {
                        int uses = cfg.getInt(argsZeroPromocodes + ".uses");
                        if (uses > 0) {
                            if (!cfg.getStringList(argsZeroPromocodes + ".players").contains(p.getName())) {
                                int usesminus = uses - 1;
                                cfg.set(argsZeroPromocodes + ".uses", usesminus);
                                List list = new ArrayList(cfg.getStringList(argsZeroPromocodes + ".players"));
                                list.add(p.getName());
                                cfg.set(argsZeroPromocodes + ".players", list);
                                Main.instance.saveConfig();
                                List<String> listcmd = new ArrayList<>(cfg.getStringList(argsZeroPromocodes + ".commands"));
                                for (int i = 0; i < listcmd.size(); i++) {
                                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), listcmd.get(i).replaceAll("%player%", p.getName()));
                                }
                                p.sendMessage(ChatColor.translateAlternateColorCodes('&', cfg.getString("messages.succes-used")));
                                return true;
                            }
                            p.sendMessage(ChatColor.translateAlternateColorCodes('&', cfg.getString("messages.already-used")));
                            return true;
                        }
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', cfg.getString("messages.no-uses")));
                        return true;
                    }
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', cfg.getString("messages.invalid-promocode")));
                    return true;
                }
            }
        }
        return true;
    }
}