package com.denfop.tiles.mechanism;

import com.denfop.api.Recipes;
import com.denfop.invslot.InvSlotProcessableMultiGeneric;
import com.denfop.tiles.base.EnumMultiMachine;
import com.denfop.tiles.base.TileEntityMultiMachine;
import ic2.core.init.Localization;

public class TileEntityTripleAssamplerScrap extends TileEntityMultiMachine {

    public TileEntityTripleAssamplerScrap() {
        super(
                EnumMultiMachine.TRIPLE_AssamplerScrap.usagePerTick,
                EnumMultiMachine.TRIPLE_AssamplerScrap.lenghtOperation,
                Recipes.createscrap,
                3
        );
        this.inputSlots = new InvSlotProcessableMultiGeneric(this, "input", sizeWorkingSlot, Recipes.createscrap);
    }


    @Override
    public EnumMultiMachine getMachine() {
        return EnumMultiMachine.TRIPLE_AssamplerScrap;
    }

    public String getInventoryName() {
        return Localization.translate("iu.blockAssamplerScrap2.name");
    }

    public String getStartSoundFile() {
        return "Machines/AssamplerScrap.ogg";
    }

    public String getInterruptSoundFile() {
        return "Machines/InterruptOne.ogg";
    }


}
