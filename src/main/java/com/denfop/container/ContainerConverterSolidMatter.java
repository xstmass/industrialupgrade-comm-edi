package com.denfop.container;

import com.denfop.tiles.base.TileEntityConverterSolidMatter;
import ic2.core.ContainerFullInv;
import ic2.core.slot.SlotInvSlot;
import net.minecraft.entity.player.EntityPlayer;

import java.util.List;

public class ContainerConverterSolidMatter extends ContainerFullInv<TileEntityConverterSolidMatter> {

    public ContainerConverterSolidMatter(EntityPlayer entityPlayer, TileEntityConverterSolidMatter tileEntity) {
        super(entityPlayer, tileEntity, 240 - 16);
        for (int i = 0; i < 3; i++) {
            addSlotToContainer(new SlotInvSlot(tileEntity.MatterSlot, i, 51 + i * 18, 17));
        }
        addSlotToContainer(new SlotInvSlot(tileEntity.MatterSlot, 3, 153, 8));
        addSlotToContainer(new SlotInvSlot(tileEntity.MatterSlot, 4, 153, 26));
        addSlotToContainer(new SlotInvSlot(tileEntity.MatterSlot, 5, 153, 72));
        addSlotToContainer(new SlotInvSlot(tileEntity.MatterSlot, 6, 153, 90));

        addSlotToContainer(new SlotInvSlot(tileEntity.inputSlot, 0, 51, 51));
        addSlotToContainer(new SlotInvSlot(tileEntity.outputSlot, 0, 117, 51));
        for (int i = 0; i < 3; i++) {
            addSlotToContainer(new SlotInvSlot(tileEntity.upgradeSlot, i, 59 + i * 18, 113));
        }

    }

    public List<String> getNetworkedFields() {
        List<String> ret = super.getNetworkedFields();
        ret.add("energy");
        ret.add("progress");
        ret.add("quantitysolid");
        ret.add("guiProgress");
        return ret;
    }

}
