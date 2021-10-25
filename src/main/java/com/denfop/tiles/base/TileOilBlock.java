package com.denfop.tiles.base;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TileOilBlock extends TileEntity {


    public final int tier;
    public final boolean  active;
    public final int facing;
    public int number;
    public int max;
    public boolean change;
    public boolean empty;

    public TileOilBlock() {
        this.tier = 14;
        this.facing=0;
        this.active=false;
        this.empty=false;

    }

    public void readFromNBT(final NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        number = nbttagcompound.getInteger("number");
        max = nbttagcompound.getInteger("max");
        change = nbttagcompound.getBoolean("change");
        empty = nbttagcompound.getBoolean("empty");
    }

    public void writeToNBT(final NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setInteger("number", number);
        nbttagcompound.setBoolean("change", change);
        nbttagcompound.setBoolean("empty", empty);
        nbttagcompound.setInteger("max", max);
    }

}
