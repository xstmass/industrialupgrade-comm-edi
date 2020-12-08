package com.Denfop.ssp.tiles.neutronfabricator;

import ic2.core.ContainerFullInv;
import ic2.core.block.invslot.InvSlot;
import ic2.core.slot.SlotInvSlot;
import java.util.List;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;

public class ContainerMatter extends ContainerFullInv<TileEntityMassFabricator> {
  public ContainerMatter(EntityPlayer player, TileEntityMassFabricator tileEntity) {
    super(player, (TileEntityMassFabricator)tileEntity, 166);
    addSlotToContainer((Slot)new SlotInvSlot((InvSlot)tileEntity.amplifierSlot, 0, 72, 40));
    addSlotToContainer((Slot)new SlotInvSlot((InvSlot)tileEntity.outputSlot, 0, 125, 59));
    addSlotToContainer((Slot)new SlotInvSlot((InvSlot)tileEntity.containerslot, 0, 125, 23));
    for (int i = 0; i < 4; i++)
      addSlotToContainer((Slot)new SlotInvSlot((InvSlot)tileEntity.upgradeSlot, i, 152, 8 + i * 18)); 
  }
  
  public List<String> getNetworkedFields() {
    List<String> ret = super.getNetworkedFields();
    ret.add("energy");
    ret.add("scrap");
    ret.add("fluidTank");
    return ret;
  }
}
