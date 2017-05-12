package net.infstudio.foodcraftreloaded.block.tileentity;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.TileFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileEntityPressureCooker extends TileFluidHandler implements IItemHandlerModifiable, ITickable {
    private IItemHandlerModifiable itemHandler = new ItemStackHandler(2);
    private EnergyStorage energyStorage = new EnergyStorage(2000);
    private FluidTank fluidTank = new FluidTank(FluidRegistry.WATER, 10, 1000);

    @Override
    public void setStackInSlot(int slot, @Nonnull ItemStack stack) {
        itemHandler.setStackInSlot(slot, stack);
    }

    @Override
    public int getSlots() {
        return 2;
    }

    @Nonnull
    @Override
    public ItemStack getStackInSlot(int slot) {
        return itemHandler.getStackInSlot(slot);
    }

    @Nonnull
    @Override
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
        if (slot == 1)
            return stack;
        return itemHandler.insertItem(slot, stack, simulate);
    }

    @Nonnull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        return itemHandler.extractItem(slot, amount, simulate);
    }

    @Override
    public int getSlotLimit(int slot) {
        return itemHandler.getSlotLimit(slot);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        CapabilityEnergy.ENERGY.getStorage().readNBT(CapabilityEnergy.ENERGY, energyStorage, null, tag.getTag("energy"));
        CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.getStorage().readNBT(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, itemHandler, null, tag.getTag("inv"));
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        tag.setTag("energy", CapabilityEnergy.ENERGY.getStorage().writeNBT(CapabilityEnergy.ENERGY, energyStorage, null));
        tag.setTag("inv", CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.getStorage().writeNBT(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, itemHandler, null));
        return super.writeToNBT(tag);
    }

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == CapabilityEnergy.ENERGY || capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY || capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
    }

    @SuppressWarnings("unchecked")
    @Nullable
    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityEnergy.ENERGY) return (T) energyStorage;
        else if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) return (T) fluidTank;
        else if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) return (T) itemHandler;
        else return super.getCapability(capability, facing);
    }

    @Override
    public void update() {
        //Update Crafting
    }
}
