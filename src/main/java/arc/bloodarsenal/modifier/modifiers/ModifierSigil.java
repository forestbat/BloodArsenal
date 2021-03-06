package arc.bloodarsenal.modifier.modifiers;

import WayofTime.bloodmagic.api.iface.ISigil;
import WayofTime.bloodmagic.util.helper.TextHelper;
import arc.bloodarsenal.modifier.EnumModifierType;
import arc.bloodarsenal.modifier.Modifier;
import arc.bloodarsenal.registry.Constants;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class ModifierSigil extends Modifier
{
    private boolean activated = false;

    public ModifierSigil(int level)
    {
        super(Constants.Modifiers.SIGIL, 1, level, EnumModifierType.ABILITY);
        setAltName();
    }

    @Override
    public String getAlternateName(ItemStack itemStack)
    {
        return itemStack.hasTagCompound() ? itemStack.getTagCompound().getString(Constants.NBT.ITEMSTACK_NAME) + " (" + (TextHelper.localizeEffect("tooltip.bloodmagic." + (activated ? "activated" : "deactivated")) + ") ") : "一体なにをやっているんだ？";
    }

    @Override
    public void onUpdate(ItemStack itemStack, World world, Entity entity, int itemSlot)
    {
        if (activated && itemStack.hasTagCompound())
        {
            NBTTagCompound data = itemStack.getTagCompound().getCompoundTag(Constants.NBT.ITEMSTACK);
            ItemStack sigilStack = new ItemStack(data);
            if (!sigilStack.isEmpty())
                sigilStack.getItem().onUpdate(itemStack, world, entity, itemSlot, true);
        }
    }

    @Override
    public void onRightClick(ItemStack itemStack, World world, EntityPlayer player)
    {
        activated = !activated;
    }

    @Override
    public void writeToNBT(NBTTagCompound tag)
    {
        tag.setBoolean(Constants.NBT.ACTIVATED, activated);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag)
    {
        activated = tag.getBoolean(Constants.NBT.ACTIVATED);
    }

    @Override
    public void writeSpecialNBT(ItemStack itemStack, ItemStack extra)
    {
        NBTTagCompound tag = itemStack.getTagCompound();
        NBTTagCompound sigilTag = new NBTTagCompound();
        if (!extra.isEmpty() && extra.getItem() instanceof ISigil)
            extra.writeToNBT(sigilTag);

        tag.setTag(Constants.NBT.ITEMSTACK, sigilTag);
        tag.setString(Constants.NBT.ITEMSTACK_NAME, extra.getDisplayName());
    }

    @Override
    public NBTTagCompound getSpecialNBT(ItemStack itemStack)
    {
        NBTTagCompound tag = itemStack.getTagCompound();
        NBTTagCompound specialTag = new NBTTagCompound();
        specialTag.setTag(Constants.NBT.ITEMSTACK, tag.getTag(Constants.NBT.ITEMSTACK));
        specialTag.setString(Constants.NBT.ITEMSTACK_NAME, tag.getString(Constants.NBT.ITEMSTACK_NAME));

        return specialTag;
    }

    @Override
    public void removeSpecialNBT(ItemStack itemStack)
    {
        NBTTagCompound tag = itemStack.getTagCompound();
        tag.removeTag(Constants.NBT.ITEMSTACK);
        tag.removeTag(Constants.NBT.ITEMSTACK_NAME);
    }
}
