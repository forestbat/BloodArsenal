package arc.bloodarsenal.item.tool;

import WayofTime.bloodmagic.api.util.helper.NetworkHelper;
import WayofTime.bloodmagic.client.IVariantProvider;
import WayofTime.bloodmagic.util.helper.TextHelper;
import arc.bloodarsenal.BloodArsenal;
import arc.bloodarsenal.ConfigHandler;
import arc.bloodarsenal.registry.Constants;
import arc.bloodarsenal.registry.ModItems;
import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.*;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;

public abstract class ItemBloodInfusedTool extends ItemTool implements IVariantProvider
{
    protected final String tooltipBase;
    private final int enchantibility;

    private final int repairUpdate;
    private final int repairCost;

    public ItemBloodInfusedTool(String type, ToolMaterial toolMaterial, String name, float damage, Set<Block> effectiveBlocks, int enchantibility, int repairUpdate, int repairCost)
    {
        super(damage, -2.8F, toolMaterial, effectiveBlocks);

        setUnlocalizedName(BloodArsenal.MOD_ID + ".bloodInfused" + type + "." + name);
        setCreativeTab(BloodArsenal.TAB_BLOOD_ARSENAL);
        setHarvestLevel(name, toolMaterial == ModItems.BLOOD_INFUSED_IRON ? 3 : 1);

        tooltipBase = "tooltip.bloodarsenal.bloodInfused" + type + "." + name + ".";
        this.enchantibility = enchantibility;

        this.repairUpdate = repairUpdate;
        this.repairCost = repairCost;
    }

    @Override
    public void onUpdate(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected)
    {
        if (!world.isRemote && stack.getItemDamage() > 0 && world.getWorldTime() % repairUpdate == 0 && itemRand.nextBoolean())
        {
            if (entity instanceof EntityPlayer)
            {
                int cost = repairCost;

                if (stack.isItemEnchanted())
                {
                    NBTTagList enchants = stack.getEnchantmentTagList();

                    if (enchants != null && enchants.tagCount() > 0)
                    {
                        for (int i = 0; i <= enchants.tagCount(); i++)
                        {
                            NBTTagCompound enchant = enchants.getCompoundTagAt(i);
                            short lvl = enchant.getShort("lvl");

                            cost *= ((repairCost / 10) * lvl);
                        }
                    }
                }

                NetworkHelper.getSoulNetwork((EntityPlayer) entity).syphonAndDamage((EntityPlayer) entity, cost);
                stack.setItemDamage(stack.getItemDamage() - 2);
            }
        }
    }

    @Override
    public int getItemEnchantability()
    {
        return enchantibility;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advanced)
    {
        if (!stack.hasTagCompound())
            return;

        if (I18n.hasKey(tooltipBase + "desc"))
            tooltip.add(TextHelper.localizeEffect(tooltipBase + "desc"));

        super.addInformation(stack, player, tooltip, advanced);
    }

    @Override
    public List<Pair<Integer, String>> getVariants()
    {
        List<Pair<Integer, String>> ret = new ArrayList<>();
        ret.add(new ImmutablePair<>(0, "normal"));
        return ret;
    }

    public static class Wooden extends ItemBloodInfusedTool
    {
        public Wooden(String name, float damage, Set<Block> effectiveBlocks)
        {
            super(Constants.Item.WOODEN, ModItems.BLOOD_INFUSED_WOOD, name, damage, effectiveBlocks, 18, ConfigHandler.bloodInfusedWoodenToolsRepairUpdate, ConfigHandler.bloodInfusedWoodenToolsRepairCost);
        }
    }

    public static class Iron extends ItemBloodInfusedTool
    {
        public Iron(String name, float damage, Set<Block> effectiveBlocks)
        {
            super(Constants.Item.IRON, ModItems.BLOOD_INFUSED_IRON, name, damage, effectiveBlocks, 18, ConfigHandler.bloodInfusedWoodenToolsRepairUpdate, ConfigHandler.bloodInfusedWoodenToolsRepairCost);
        }

        @Override
        public EnumRarity getRarity(ItemStack stack)
        {
            return EnumRarity.UNCOMMON;
        }
    }
}
