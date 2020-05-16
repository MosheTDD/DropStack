package me.moshe.dropstack.listener;

import me.moshe.dropstack.DropStack;

import me.moshe.dropstack.file.DroppedItemData;
import me.moshe.dropstack.util.Utils;
import me.moshe.dropstack.util.XMaterial;
import org.bukkit.Bukkit;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.ItemDespawnEvent;
import org.bukkit.event.entity.ItemMergeEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class PlayerListener implements Listener {
    private DropStack plugin;

    public PlayerListener(DropStack plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent e) {
        Item item = e.getItemDrop();
        List<Entity> entities = e.getPlayer().getNearbyEntities(2.25, 2.25, 2.25);
        List<Item> items = new ArrayList<>();
        for (Entity entity : entities) {
            if (entity.getType() == EntityType.DROPPED_ITEM) {
                Item iteration = (Item) entity;
                if (item.getItemStack().getType() == iteration.getItemStack().getType()) {
                    items.add(iteration);
                }
            }
        }

        if (items.isEmpty()) {
            item.setCustomNameVisible(true);
            String name = XMaterial.matchXMaterial(item.getItemStack().getType().name(), item.getItemStack().getData().getData()).toString().replaceAll("Optional", "").replaceAll("\\[", "").replaceAll("]", "");
            int amount = item.getItemStack().getAmount();
            item.setCustomName(Utils.color("&6&lx" + amount + " &7&l" + name));
            getDroppedItems().put(item.getUniqueId(), amount);
            item.getItemStack().setAmount(1);
            return;
        }
        Item nearbyItem = items.get(0);
        UUID uuid = nearbyItem.getUniqueId();
        if (getDroppedItems().containsKey(uuid)) {
            int value = getDroppedItems().get(uuid);
            if (value + item.getItemStack().getAmount() > item.getItemStack().getMaxStackSize()) {
                item.setCustomNameVisible(true);
                String name = XMaterial.matchXMaterial(item.getItemStack().getType().name(), item.getItemStack().getData().getData()).toString().replaceAll("Optional", "").replaceAll("\\[", "").replaceAll("]", "");
                int amount = item.getItemStack().getAmount();
                item.setCustomName(Utils.color("&6&lx" + amount + " &7&l" + name));
                getDroppedItems().put(item.getUniqueId(), amount);
                item.getItemStack().setAmount(1);
                return;
            }
            getDroppedItems().replace(uuid, value + item.getItemStack().getAmount());
            item.remove();
            String name = XMaterial.matchXMaterial(nearbyItem.getItemStack().getType().name(), nearbyItem.getItemStack().getData().getData()).toString().replaceAll("Optional", "").replaceAll("\\[", "").replaceAll("]", "");
            String amount = getDroppedItems().get(uuid).toString();
            nearbyItem.setCustomName(Utils.color("&6&lx" + amount + "&7&l " + name));
        } else {
            if (nearbyItem.getItemStack().getAmount() + item.getItemStack().getAmount() > item.getItemStack().getMaxStackSize()) {
                item.setCustomNameVisible(true);
                String name = XMaterial.matchXMaterial(item.getItemStack().getType().name(), item.getItemStack().getData().getData()).toString().replaceAll("Optional", "").replaceAll("\\[", "").replaceAll("]", "");
                int amount = item.getItemStack().getAmount();
                item.setCustomName(Utils.color("&6&lx" + amount + " &7&l" + name));
                getDroppedItems().put(item.getUniqueId(), amount);
                item.getItemStack().setAmount(1);
                return;
            }
            getDroppedItems().put(uuid, nearbyItem.getItemStack().getAmount() + item.getItemStack().getAmount());
            item.remove();
            String name = XMaterial.matchXMaterial(nearbyItem.getItemStack().getType().name(), nearbyItem.getItemStack().getData().getData()).toString().replaceAll("Optional", "").replaceAll("\\[", "").replaceAll("]", "");
            String amount = getDroppedItems().get(uuid).toString();
            nearbyItem.setCustomNameVisible(true);
            nearbyItem.setCustomName(Utils.color("&6&lx" + amount + "&7&l " + name));
        }
    }

    @EventHandler
    public void onMerge(ItemMergeEvent e) {
        e.setCancelled(true);
        Item nearbyItem = e.getEntity();
        Item item = e.getTarget();
        UUID uuid = nearbyItem.getUniqueId();
        if (item.getItemStack().getAmount() == item.getItemStack().getMaxStackSize()) return;
        if (getDroppedItems().containsKey(item.getUniqueId()) && getDroppedItems().containsKey(uuid)) {
            int value = getDroppedItems().get(item.getUniqueId());
            int value2 = getDroppedItems().get(uuid);
            int sumVal = value + value2;
            if (sumVal > item.getItemStack().getMaxStackSize()) {
                int newItemValue = value;
                int newNearbyValue = value2;
                for (int i = value; i < item.getItemStack().getMaxStackSize(); i++) {
                    if(newItemValue >= item.getItemStack().getMaxStackSize() || newNearbyValue >= item.getItemStack().getMaxStackSize())break;
                    newItemValue++;
                    newNearbyValue--;
                }
                getDroppedItems().replace(uuid, newNearbyValue);
                getDroppedItems().replace(item.getUniqueId(), newItemValue);
                String name = XMaterial.matchXMaterial(item.getItemStack().getType().name(), item.getItemStack().getData().getData()).toString().replaceAll("Optional", "").replaceAll("\\[", "").replaceAll("]", "");
                item.setCustomName(Utils.color("&6&lx" + newItemValue + " &7&l" + name));
                String nearbyItemName = XMaterial.matchXMaterial(nearbyItem.getItemStack().getType().name(), nearbyItem.getItemStack().getData().getData()).toString().replaceAll("Optional", "").replaceAll("\\[", "").replaceAll("]", "");
                nearbyItem.setCustomName(Utils.color("&6&lx" + newNearbyValue + " &7&l" + nearbyItemName));
                return;
            }
            nearbyItem.remove();
            getDroppedItems().remove(uuid);
            getDroppedItems().replace(item.getUniqueId(), sumVal);
            String name = XMaterial.matchXMaterial(item.getItemStack().getType().name(), item.getItemStack().getData().getData()).toString().replaceAll("Optional", "").replaceAll("\\[", "").replaceAll("]", "");
            item.setCustomName(Utils.color("&6&lx" + sumVal + " &7&l" + name));
            return;
        }
        if (getDroppedItems().containsKey(uuid)) {
            e.setCancelled(true);
            int value = getDroppedItems().get(uuid);
            if (value + item.getItemStack().getAmount() > item.getItemStack().getMaxStackSize()) return;
            getDroppedItems().remove(uuid);
            getDroppedItems().put(item.getUniqueId(), value + item.getItemStack().getAmount());
            nearbyItem.remove();
            item.setCustomNameVisible(true);
            String name = XMaterial.matchXMaterial(item.getItemStack().getType().name(), item.getItemStack().getData().getData()).toString().replaceAll("Optional", "").replaceAll("\\[", "").replaceAll("]", "");
            String amount = getDroppedItems().get(item.getUniqueId()).toString();
            item.setCustomName(Utils.color("&6&lx" + amount + "&7&l " + name));
        } else if (!getDroppedItems().containsKey(uuid) && !getDroppedItems().containsKey(item.getUniqueId())) {
            int amount = nearbyItem.getItemStack().getAmount() + item.getItemStack().getAmount();
            getDroppedItems().put(item.getUniqueId(), amount);
            nearbyItem.remove();
            item.setCustomNameVisible(true);
            String name = XMaterial.matchXMaterial(item.getItemStack().getType().name(), item.getItemStack().getData().getData()).toString().replaceAll("Optional", "").replaceAll("\\[", "").replaceAll("]", "");
            item.setCustomName(Utils.color("&6&lx" + amount + " &7&l" + name));
            return;
        }
        return;
    }

    @EventHandler
    public void onPickUp(PlayerPickupItemEvent e){
        UUID uuid = e.getItem().getUniqueId();
        if(getDroppedItems().containsKey(uuid)){
           int value = getDroppedItems().get(uuid);
           ItemStack item = new ItemStack(e.getItem().getItemStack().getType(), 1, e.getItem().getItemStack().getData().getData());
           for(int i =  1; i < value; i++){
               e.getPlayer().getInventory().addItem(item);
           }
           getDroppedItems().remove(uuid);
           return;
        }
    }

    @EventHandler
    public void onItemDespawn(ItemDespawnEvent e){
        Item item = e.getEntity();
        UUID uuid = item.getUniqueId();
        if(getDroppedItems().containsKey(uuid)){
            getDroppedItems().remove(uuid);
        }
    }

    public HashMap<UUID, Integer> getDroppedItems(){
        return DroppedItemData.droppedItems;
    }

}
