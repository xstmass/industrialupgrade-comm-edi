package com.denfop.invslot;

import cofh.api.energy.IEnergyContainerItem;
import com.denfop.IUItem;
import com.denfop.item.modules.AdditionModule;
import com.denfop.item.modules.EnumType;
import com.denfop.item.modules.ItemWirelessModule;
import com.denfop.tiles.base.TileEntityElectricBlock;
import com.denfop.utils.ModUtils;
import com.denfop.utils.NBTData;
import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
import ic2.core.block.TileEntityInventory;
import ic2.core.block.invslot.InvSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.ArrayList;
import java.util.List;

public class InvSlotElectricBlock extends InvSlot {

    private final int type;
    private int stackSizeLimit;

    public InvSlotElectricBlock(TileEntityInventory base1, int oldStartIndex1, String name, int count) {
        super(base1, name, oldStartIndex1, InvSlot.Access.IO, count, InvSlot.InvSide.TOP);
        this.type = oldStartIndex1;
        this.stackSizeLimit = 1;
    }

    public boolean accepts(ItemStack itemStack) {
        if (type == 3)
            return ((itemStack.getItemDamage() >= 4 || itemStack.getItemDamage() == 0)
                    && itemStack.getItem() instanceof AdditionModule) || (itemStack.getItem() instanceof ItemWirelessModule)
                    || (IUItem.modules.get(itemStack.getItem()) != null && IUItem.modules.get(itemStack.getItem()).type.equals(EnumType.OUTPUT));
        if (type == 2)
            return itemStack.getItem() instanceof IElectricItem;
        if (type == 1)
            return itemStack.getItem() instanceof IElectricItem || itemStack.getItem() instanceof IEnergyContainerItem;
        return false;
    }

    public int getStackSizeLimit() {
        return this.stackSizeLimit;
    }

    public void setStackSizeLimit(int stackSizeLimit) {
        this.stackSizeLimit = stackSizeLimit;
    }

    public double charge(double amount, ItemStack stack, boolean simulate) {
        if (amount < 0.0) {
            throw new IllegalArgumentException("Amount must be > 0.");
        }
        if (amount == 0.0)
            return 0;

        return ElectricItem.manager.charge(stack, amount, 2147483647, false, simulate);
    }

    public double discharge(double amount, ItemStack stack, boolean simulate) {
        if (amount < 0.0) {
            throw new IllegalArgumentException("Amount must be > 0.");
        }
        double charged = 0.0;
        if (amount == 0.0)
            return 0;

        final double energyIn = ElectricItem.manager.discharge(stack, amount, 2147483647, false, true, simulate);
        amount -= energyIn;
        charged += energyIn;
        if (amount <= 0.0)
            return 0;

        return charged;
    }

    public List<Boolean> getstats() {
        List<Boolean> list = new ArrayList<>();
        List<Boolean> list1 = new ArrayList<>();

        for (int i = 0; i < this.size(); i++) {
            if (this.get(i) == null) {
                list.add(false);
                list.add(false);
                list.add(false);
                list.add(false);
                list.add(false);
                continue;
            }

            ItemStack stack = this.get(i);
            if (stack.getItemDamage() == 5) {
                list.add(true);
            } else {
                list.add(false);
            }
            if (stack.getItemDamage() == 6) {
                list.add(true);
            } else {
                list.add(false);
            }
            if (stack.getItemDamage() == 7) {
                list.add(true);
            } else {
                list.add(false);
            }
            if (stack.getItemDamage() == 8) {
                list.add(true);
            } else {
                list.add(false);
            }
            if (stack.getItemDamage() == 4) {
                list.add(true);
            } else {
                list.add(false);
            }

        }
        list1.add(gettrue(list.get(0), list.get(5)));
        list1.add(gettrue(list.get(1), list.get(6)));
        list1.add(gettrue(list.get(2), list.get(7)));
        list1.add(gettrue(list.get(3), list.get(8)));
        list1.add(gettrue(list.get(4), list.get(9)));

        return list1;
    }

    public boolean gettrue(boolean o, boolean j) {
        return (o || j);
    }


    public boolean wirelessA() {
        for (int i = 0; i < this.size(); i++) {
            if (this.get(i) == null)
                continue;
            ItemStack stack = this.get(i);
            if (stack.getItem() instanceof ItemWirelessModule) {
                NBTTagCompound nbttagcompound = NBTData.getOrCreateNbtData(stack);
                return nbttagcompound.getBoolean("change");

            }
        }
        return false;
    }

    public void wirelessmodule() {
        TileEntityElectricBlock tile = (TileEntityElectricBlock) base;
        for (int i = 0; i < this.size(); i++) {
            if (this.get(i) != null && this.get(i).getItem() instanceof ItemWirelessModule) {

                tile.wirelees = 1;
                int x;
                int y;
                int z;
                String name;
                int tier1;

                NBTTagCompound nbttagcompound = ModUtils.nbt(this.get(i));

                x = nbttagcompound.getInteger("Xcoord");
                y = nbttagcompound.getInteger("Ycoord");
                z = nbttagcompound.getInteger("Zcoord");
                tier1 = nbttagcompound.getInteger("tier");
                name = nbttagcompound.getString("Name");
                int world = nbttagcompound.getInteger("World1");

                if (x != 0 && y != 0 && z != 0) {
                    tile.panelx = x;
                    tile.panely = y;
                    tile.panelz = z;
                    tile.nameblock = name;
                    tile.world1 = world;
                    tile.blocktier = tier1;
                }
                break;
            } else {
                tile.wirelees = 0;
            }
        }
    }

    public void wireless(int xCoord, int yCoord, int zCoord, int tier, int dimensionId,
                         String string, String string2) {

        for (int i = 0; i < this.size(); i++) {
            if (this.get(i) == null)
                continue;
            ItemStack stack = this.get(i);
            if (stack.getItem() instanceof ItemWirelessModule) {

                NBTTagCompound nbttagcompound = NBTData.getOrCreateNbtData(stack);
                if (!nbttagcompound.getBoolean("change")) {
                    nbttagcompound.setInteger("Xcoord", xCoord);
                    nbttagcompound.setInteger("Ycoord", yCoord);
                    nbttagcompound.setInteger("Zcoord", zCoord);
                    nbttagcompound.setInteger("tier", tier);
                    nbttagcompound.setInteger("World1", dimensionId);
                    nbttagcompound.setString("World", string);
                    nbttagcompound.setString("Name", string2);
                }
            }
        }

    }

    public boolean personality() {

        for (int i = 0; i < this.size(); i++) {
            if (this.get(i) == null)
                continue;
            ItemStack stack = this.get(i);
            if (stack.getItem() instanceof AdditionModule)
                if (stack.getItemDamage() == 0)
                    return true;
        }
        return false;
    }

    public double output_plus(double l) {
        int output = 0;
        for (int i = 0; i < this.size(); i++) {
            if (this.get(i) == null)
                continue;
            ItemStack stack = this.get(i);
            if (IUItem.modules.get(stack.getItem()) != null) {
                output += l * IUItem.modules.get(stack.getItem()).percent;
            }
        }
        return output;
    }

}
