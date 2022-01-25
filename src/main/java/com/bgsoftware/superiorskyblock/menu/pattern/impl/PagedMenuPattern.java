package com.bgsoftware.superiorskyblock.menu.pattern.impl;

import com.bgsoftware.superiorskyblock.SuperiorSkyblockPlugin;
import com.bgsoftware.superiorskyblock.menu.PagedSuperiorMenu;
import com.bgsoftware.superiorskyblock.menu.button.PagedObjectButton;
import com.bgsoftware.superiorskyblock.menu.button.SuperiorMenuButton;
import com.bgsoftware.superiorskyblock.menu.button.impl.CurrentPageButton;
import com.bgsoftware.superiorskyblock.menu.button.impl.NextPageButton;
import com.bgsoftware.superiorskyblock.menu.button.impl.PreviousPageButton;
import com.bgsoftware.superiorskyblock.menu.pattern.SuperiorMenuPattern;
import com.bgsoftware.superiorskyblock.utils.debug.PluginDebugger;
import com.bgsoftware.superiorskyblock.utils.items.ItemBuilder;
import com.bgsoftware.superiorskyblock.wrappers.SoundWrapper;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

public final class PagedMenuPattern<M extends PagedSuperiorMenu<M, T>, T> extends SuperiorMenuPattern<M> {

    private final int objectsPerPage;

    private PagedMenuPattern(String title, InventoryType inventoryType, SuperiorMenuButton<M>[] buttons,
                             SoundWrapper openingSound, boolean isPreviousMoveAllowed) {
        super(title, inventoryType, buttons, openingSound, isPreviousMoveAllowed);
        objectsPerPage = (int) Arrays.stream(buttons).filter(button -> button instanceof PagedObjectButton).count();
    }

    public int getObjectsPerPage() {
        return objectsPerPage;
    }

    @Override
    public void setupInventory(Inventory inventory, M superiorMenu) {
        int currentPage = superiorMenu.getCurrentPage();
        List<T> pagedObjects = superiorMenu.getPagedObjects();
        int pagedObjectSlot = 0;

        // Set all regular buttons in the menu
        for (int slot = 0; slot < this.buttons.length; ++slot) {
            SuperiorMenuButton<M> button = this.buttons[slot];

            if (button instanceof PagedObjectButton) {
                PagedObjectButton<M, T> pagedObjectButton = (PagedObjectButton<M, T>) button;
                int objectIndex = pagedObjectSlot + (objectsPerPage * (currentPage - 1));

                ++pagedObjectSlot;

                if (objectIndex >= pagedObjects.size()) {
                    inventory.setItem(slot, pagedObjectButton.getNullItem().build());
                    continue;
                } else {
                    pagedObjectButton.updateObject(pagedObjects.get(objectIndex));
                }
            }

            ItemStack buttonItem;

            try {
                buttonItem = button.getButtonItem(superiorMenu);
            } catch (Exception error) {
                SuperiorSkyblockPlugin.log("Failed to load menu because due to an error with slot #" + slot);
                PluginDebugger.debug(error);
                return;
            }

            if (buttonItem == null)
                continue;

            if (button instanceof PreviousPageButton) {
                inventory.setItem(slot, new ItemBuilder(buttonItem)
                        .replaceAll("{0}", (currentPage == 1 ? "&c" : "&a"))
                        .build(superiorMenu.getInventoryViewer()));
            } else if (button instanceof NextPageButton) {
                inventory.setItem(slot, new ItemBuilder(buttonItem)
                        .replaceAll("{0}", (pagedObjects.size() > currentPage * this.objectsPerPage ? "&a" : "&c"))
                        .build(superiorMenu.getInventoryViewer()));
            } else if (button instanceof CurrentPageButton) {
                inventory.setItem(slot, new ItemBuilder(buttonItem)
                        .replaceAll("{0}", currentPage + "")
                        .build(superiorMenu.getInventoryViewer()));
            } else {
                inventory.setItem(slot, buttonItem);
            }
        }
    }

    public static class Builder<M extends PagedSuperiorMenu<M, T>, T> extends
            AbstractBuilder<Builder<M, T>, PagedMenuPattern<M, T>, M> {

        public Builder<M, T> setPreviousPageSlots(List<Integer> slots) {
            return mapButtons(slots, new PreviousPageButton.Builder<>());
        }

        public Builder<M, T> setNextPageSlots(List<Integer> slots) {
            return mapButtons(slots, new NextPageButton.Builder<>());
        }

        public Builder<M, T> setCurrentPageSlots(List<Integer> slots) {
            return mapButtons(slots, new CurrentPageButton.Builder<>());
        }

        public Builder<M, T> setPagedObjectSlots(List<Integer> slots, PagedObjectButton.PagedObjectBuilder<?, ?, M> builder) {
            return mapButtons(slots, builder);
        }

        @Override
        public PagedMenuPattern<M, T> build() {
            return new PagedMenuPattern<>(this.title, this.inventoryType, this.buttons,
                    this.openingSound, this.isPreviousMoveAllowed);
        }

    }

}