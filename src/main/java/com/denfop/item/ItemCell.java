package com.denfop.item;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.IUItem;
import com.denfop.block.base.BlockIC2Fluid;
import com.denfop.block.base.BlocksItems;
import com.denfop.utils.LiquidUtil;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ic2.core.IC2;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;

import java.util.*;

public class ItemCell extends Item {
    private final Map<Integer, String> names;
    private final List<String> itemNames;
    private IIcon[] IIconsList;

    private final Map<Block, ItemStack> cells;

    public ItemCell() {
        super();
        this.names = new HashMap<>();
        this.cells = new IdentityHashMap<>();
        setHasSubtypes(true);
        IUItem.cell = addCell(0, "itemCellEmpty");
        IUItem.uuMatterCell = addRegisterCell(1, "itemCellNeutron", "fluidNeutron");
        IUItem.HeliumCell = addRegisterCell(2, "itemCellHelium", "fluidHelium");
        IUItem.NeftCell = addRegisterCell(3, "itemCellneft", "fluidneft");
        IUItem.BenzCell = addRegisterCell(4, "itemCellbenz", "fluidbenz");
        IUItem.DizelCell = addRegisterCell(5, "itemCelldizel", "fluiddizel");
        IUItem.PolyethCell = addRegisterCell(6, "itemCellpolyeth", "fluidpolyeth");
        IUItem.PolypropCell = addRegisterCell(7, "itemCellpolyprop", "fluidpolyprop");
        IUItem.OxyCell = addRegisterCell(8, "itemCelloxy", "fluidoxy");
        IUItem.HybCell = addRegisterCell(9, "itemCellhyd", "fluidhyd");


        this.itemNames = new ArrayList<>();


        this.addItemsNames();
        this.setCreativeTab(IUCore.tabssp3);
        GameRegistry.registerItem(this, "cell");
    }

    public String getUnlocalizedName(final ItemStack stack) {
        return this.itemNames.get(stack.getItemDamage());
    }

    public IIcon getIconFromDamage(final int par1) {
        return this.IIconsList[par1];
    }

    public void addItemsNames() {

        this.itemNames.add("itemCellEmpty");
        this.itemNames.add("itemCellNeutron");
        this.itemNames.add("itemCellHelium");
        this.itemNames.add("itemCellneft");
        this.itemNames.add("itemCellbenz");
        this.itemNames.add("itemCelldizel");
        this.itemNames.add("itemCellpolyeth");
        this.itemNames.add("itemCellpolyprop");
        this.itemNames.add("itemCelloxy");
        this.itemNames.add("itemCellhyd");
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(final IIconRegister IIconRegister) {
        this.IIconsList = new IIcon[itemNames.size()];
        for (int i = 0; i < itemNames.size(); i++)
            this.IIconsList[i] = IIconRegister.registerIcon(Constants.TEXTURES_MAIN + itemNames.get(i));


    }

    public void getSubItems(final Item item, final CreativeTabs tabs, final List itemList) {
        for (int meta = 0; meta <= this.itemNames.size() - 1; ++meta) {
            final ItemStack stack = new ItemStack(this, 1, meta);
            itemList.add(stack);
        }
    }

    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side,
                             float xOffset, float yOffset, float zOffset) {
        if (!IC2.platform.isSimulating())
            return false;
        MovingObjectPosition position = getMovingObjectPositionFromPlayer(world, player, true);
        if (position == null)
            return false;
        if (position.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
            x = position.blockX;
            y = position.blockY;
            z = position.blockZ;
            if (!world.canMineBlock(player, x, y, z))
                return false;
            if (!player.canPlayerEdit(x, y, z, position.sideHit, stack))
                return false;
            if (stack.getItemDamage() == 0) {

                if (world.getBlock(x, y, z) instanceof BlockIC2Fluid) {
                    Block block = world.getBlock(x, y, z);
                    String name = block.getUnlocalizedName();

                    Fluid fluid = BlocksItems.getFluid(name);
                    name = fluid.getName().substring(name.indexOf("fluid"));

                    if (itemNames.contains(("itemCell" + name))) {

                        int meta = 0;
                        for (int i = 0; i < itemNames.size(); i++) {
                            if (itemNames.get(i).equals(("itemCell" + name))) {

                                meta = i;
                                break;
                            }
                        }
                        ItemStack stack1 = new ItemStack(IUItem.cell.getItem(), 1, meta);
                        if (player.inventory.addItemStackToInventory(stack1)) {
                            stack.stackSize--;
                            world.setBlockToAir(x, y, z);
                            player.inventoryContainer.detectAndSendChanges();

                        }
                    }
                }


            } else {
                FluidStack fs = FluidContainerRegistry.getFluidForFilledItem(stack);
                ForgeDirection dir = ForgeDirection.VALID_DIRECTIONS[position.sideHit];
                if ((LiquidUtil.placeFluid(fs, world, x, y, z)) || (player.canPlayerEdit(x + dir.offsetX,
                        y + dir.offsetY, z + dir.offsetZ, position.sideHit, stack)
                        && LiquidUtil.placeFluid(fs, world, x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ))) {
                    if (!player.capabilities.isCreativeMode)
                        stack.stackSize--;
                    ItemStack stack1 = new ItemStack(IUItem.cell.getItem(), 1, 0);
                    if (!player.capabilities.isCreativeMode)
                        player.inventory.addItemStackToInventory(stack1);
                    player.inventoryContainer.detectAndSendChanges();
                    return true;
                }
            }
        }
        return false;
    }

    private ItemStack addCell(int meta, String name, Block... blocks) {
        this.names.put(meta, name);
        ItemStack ret = new ItemStack(this, 1, meta);
        for (Block block : blocks)
            this.cells.put(block, ret);
        return ret;
    }

    private ItemStack addRegisterCell(int meta, String name, String blockName) {
        ItemStack ret = addCell(meta, name, BlocksItems.getFluidBlock(blockName));
        FluidContainerRegistry.registerFluidContainer(BlocksItems.getFluid(blockName), ret.copy(), IUItem.cell.copy());

        return ret;
    }
}
