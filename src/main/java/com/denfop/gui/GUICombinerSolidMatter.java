package com.denfop.gui;

import com.denfop.Constants;
import com.denfop.container.ContainerCombinerSolidMatter;
import ic2.api.upgrade.IUpgradableBlock;
import ic2.core.GuiIC2;
import net.minecraft.util.ResourceLocation;


public class GUICombinerSolidMatter extends GuiIC2<ContainerCombinerSolidMatter> {

    public final ContainerCombinerSolidMatter container;

    public GUICombinerSolidMatter(ContainerCombinerSolidMatter container1) {
        super(container1);
        this.container = container1;
    }


    protected void drawGuiContainerBackgroundLayer(float f, int x, int y) {
        this.mc.getTextureManager().bindTexture(getTexture());
        int xoffset = (this.width - this.xSize) / 2;
        int yoffset = (this.height - this.ySize) / 2;
        drawTexturedModalRect(xoffset, yoffset, 0, 0, this.xSize, this.ySize);
        if (this.container.base != null) {
            this.mc.getTextureManager().bindTexture(new ResourceLocation("ic2", "textures/gui/infobutton.png"));
            this.drawTexturedRect(3.0D, 3.0D, 10.0D, 10.0D, 0.0D, 0.0D);
        }
        this.mc.getTextureManager().bindTexture(getTexture());



    }


    public String getName() {
        return container.base.getInventoryName();
    }

    public ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/GUICombineSolidMatter.png");
    }

}
