package arc.bloodarsenal.modifier;

import com.google.common.collect.Multimap;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IModifiable
{
    Multimap<String, AttributeModifier> getAttributeModifiers();

    boolean canApplyModifier(Modifier modifier);

    boolean markModifierReady(ItemStack itemStack, EntityPlayer player, Modifier modifier);

    boolean applyModifier(Modifier modifier);

    boolean removeModifier(Modifier modifier);

    void onUpdate(ItemStack itemStack, World world, Entity entity, int itemSlot);

    void hitEntity(ItemStack itemStack, EntityLivingBase target, EntityLivingBase attacker);

    void onBlockDestroyed(ItemStack itemStack, World world, IBlockState state, BlockPos pos, EntityPlayer player);

    void onRelease(ItemStack itemStack, World world, EntityPlayer player, int charge);

    void onRightClick(ItemStack itemStack, World world, EntityPlayer player);

    void readFromNBT(NBTTagCompound tag);

    void writeToNBT(NBTTagCompound tag, boolean forceWrite);

    /**
     * Writes the Modifiers to the NBTTag. This will only write the trackers
     * that are dirty.
     *
     * @param tag
     *        - The NBT tag to write to
     */
    void writeDirtyToNBT(NBTTagCompound tag);

    void writeToNBT(NBTTagCompound tag);
}
