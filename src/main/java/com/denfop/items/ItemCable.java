package com.denfop.items;

import com.denfop.Constants;
import com.denfop.IUCore;
import com.denfop.IUItem;
import com.denfop.api.IModelRegister;
import com.denfop.tiles.mechanism.CableType;
import com.denfop.tiles.mechanism.TileEntityCable;
import com.denfop.utils.ModUtils;
import ic2.api.item.IBoxable;
import ic2.core.IC2;
import ic2.core.block.BlockTileEntity;
import ic2.core.init.BlocksItems;
import ic2.core.init.Localization;
import ic2.core.item.ItemIC2;
import ic2.core.item.block.ItemBlockTileEntity;
import ic2.core.ref.BlockName;
import ic2.core.ref.IMultiItem;
import ic2.core.ref.ItemName;
import ic2.core.ref.TeBlock;
import ic2.core.util.LogCategory;
import ic2.core.util.StackUtil;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ItemCable extends ItemIC2 implements IMultiItem<CableType>, IBoxable, IModelRegister {

    public static final List<ItemStack> variants = new ArrayList();
    private static final NumberFormat lossFormat = new DecimalFormat("0.00#");
    protected static final String NAME = "cable_iu_item";

    public ItemCable() {
        super(null);
        this.setHasSubtypes(true);
        CableType[] var1 = CableType.values;


        for (CableType type : var1) {
            for (int insulation = 0; insulation <= type.maxInsulation; ++insulation) {
                this.variants.add(getCable(type, insulation));
            }
        }
        this.setCreativeTab(IUCore.ItemTab);
        BlocksItems.registerItem((Item) this, IUCore.getIdentifier(NAME)).setUnlocalizedName(NAME);
        IUCore.proxy.addIModelRegister(this);
    }


    @SideOnly(Side.CLIENT)
    public void registerModels() {

        ModelLoader.setCustomMeshDefinition(this, ItemCable::getModelLocation);
        for (final ItemStack stack : this.variants) {
            ModelLoader.setCustomModelResourceLocation(
                    this,
                    stack.getItemDamage(),
                    getModelLocation(stack)
            );
        }
        for (final ItemStack stack : this.variants) {
            ModelBakery.registerItemVariants(this, getModelLocation(stack));
        }

    }

    @SideOnly(Side.CLIENT)
    protected void registerModel(final int meta, final ItemName name, final String extraName) {
        ModelLoader.setCustomModelResourceLocation(
                this,
                meta,
                new ModelResourceLocation(
                        Constants.MOD_ID + ":" + NAME + "/" + ItemBaseCircuit.Types.getFromID(meta).getName(),
                        null
                )
        );
    }

    @Nonnull
    @SideOnly(Side.CLIENT)
    public static ModelResourceLocation getModelLocation(ItemStack stack) {
        StringBuilder loc = new StringBuilder();
        loc.append(Constants.MOD_ID);
        loc.append(':');
        loc.append("cable").append("/").append(getName(stack));

        return new ModelResourceLocation(loc.toString(), null);
    }


    public ItemStack getItemStack(CableType type) {
        return getCable(type, 0);
    }

    public ItemStack getItemStack(String variant) {
        int pos = 0;
        CableType type = null;

        int insulation;
        int nextPos;
        for (insulation = 0; pos < variant.length(); pos = nextPos + 1) {
            nextPos = variant.indexOf(44, pos);
            if (nextPos == -1) {
                nextPos = variant.length();
            }

            int sepPos = variant.indexOf(58, pos);
            if (sepPos == -1 || sepPos >= nextPos) {
                return null;
            }

            String key = variant.substring(pos, sepPos);
            String value = variant.substring(sepPos + 1, nextPos);
            if (key.equals("type")) {
                type = CableType.get(value);
                if (type == null) {
                    IC2.log.warn(LogCategory.Item, "Invalid cable type: %s", value);
                }
            } else if (key.equals("insulation")) {
                try {
                    insulation = Integer.parseInt(value);
                } catch (NumberFormatException var10) {
                    IC2.log.warn(LogCategory.Item, "Invalid cable insulation: %s", value);
                }
            }
        }

        if (type == null) {
            return null;
        } else if (insulation >= 0 && insulation <= type.maxInsulation) {
            return getCable(type, insulation);
        } else {
            IC2.log.warn(LogCategory.Item, "Invalid cable insulation: %d", insulation);
            return null;
        }
    }

    public String getVariant(ItemStack stack) {
        if (stack == null) {
            throw new NullPointerException("null stack");
        } else if (stack.getItem() != this) {
            throw new IllegalArgumentException("The stack " + stack + " doesn't match " + this);
        } else {
            CableType type = getCableType(stack);
            int insulation = getInsulation(stack);
            return "type:" + type.getName() + ",insulation:" + insulation;
        }
    }

    public ItemStack getCable(CableType type, int insulation) {
        return new ItemStack(this, 1, type.getId());
    }

    public static ItemStack getCable(CableType type, int insulation, int k) {

        return variants.get(type.getId());
    }

    private static CableType getCableType(ItemStack stack) {
        int type = stack.getItemDamage();
        return type < CableType.values.length ? CableType.values[type] : CableType.glass;
    }

    private static int getInsulation(ItemStack stack) {
        CableType type = getCableType(stack);
        NBTTagCompound nbt = StackUtil.getOrCreateNbtData(stack);
        int insulation = nbt.getByte("insulation") & 255;
        return Math.min(insulation, type.maxInsulation);
    }

    private static String getName(ItemStack stack) {
        CableType type = getCableType(stack);
        int insulation = getInsulation(stack);
        return type.getName(insulation);
    }

    String[] name = {"itemcable", "itemcableo", "itemgoldсable", "itemgoldcablei", "itemgoldcableii", "itemironcable", "itemironcablei",
            "itemironcableii",
            "itemironcableiiii"
            , "itemglasscable"
            , "itemglasscablei"};

    public String getUnlocalizedName(ItemStack stack) {
        int meta = stack.getItemDamage();
        if (meta >= name.length) {
            meta = 0;
        }
        return "iu." + name[meta];
    }


    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, World world, List<String> info, ITooltipFlag b) {
        CableType type = getCableType(stack);
        double capacity;
        double loss;

        capacity = type.capacity;
        loss = type.loss;


        info.add(ModUtils.getString(capacity) + " " + Localization.translate("ic2.generic.text.EUt"));
        info.add(Localization.translate("ic2.cable.tooltip.loss", lossFormat.format(loss)));
    }

    public EnumActionResult onItemUse(
            EntityPlayer player,
            World world,
            BlockPos pos,
            EnumHand hand,
            EnumFacing side,
            float hitX,
            float hitY,
            float hitZ
    ) {
        ItemStack stack = StackUtil.get(player, hand);
        IBlockState oldState = world.getBlockState(pos);
        Block oldBlock = oldState.getBlock();
        if (!oldBlock.isReplaceable(world, pos)) {
            pos = pos.offset(side);
        }

        Block newBlock = BlockName.te.getInstance();
        if (!StackUtil.isEmpty(stack) && player.canPlayerEdit(pos, side, stack) && world.mayPlace(
                newBlock,
                pos,
                false,
                side,
                player
        ) && ((BlockTileEntity) newBlock).canReplace(world, pos, side, BlockName.te.getItemStack(TeBlock.cable))) {
            newBlock.getStateForPlacement(world, pos, side, hitX, hitY, hitZ, 0, player, hand);
            CableType type = getCableType(stack);
            int insulation = getInsulation(stack);

            TileEntityCable te;
            te = TileEntityCable.delegate(type, insulation);

            if (ItemBlockTileEntity.placeTeBlock(stack, player, world, pos, side, te)) {
                SoundType soundtype = newBlock.getSoundType(world.getBlockState(pos), world, pos, player);
                world.playSound(
                        player,
                        pos,
                        soundtype.getPlaceSound(),
                        SoundCategory.BLOCKS,
                        (soundtype.getVolume() + 1.0F) / 2.0F,
                        soundtype.getPitch() * 0.8F
                );
                StackUtil.consumeOrError(player, hand, 1);

            }

            return EnumActionResult.SUCCESS;
        } else {
            return EnumActionResult.PASS;
        }
    }

    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> itemList) {
        if (this.isInCreativeTab(tab)) {
            List<ItemStack> variants = new ArrayList(this.variants);


            itemList.addAll(variants);
        }
    }

    public Set<CableType> getAllTypes() {
        return EnumSet.allOf(CableType.class);
    }

    public Set<ItemStack> getAllStacks() {
        return new HashSet(this.variants);
    }

    public boolean canBeStoredInToolbox(ItemStack itemstack) {
        return true;
    }


}
