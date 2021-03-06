/**
 * MrCrayfish's Furniture Mod
 * Copyright (C) 2016  MrCrayfish (http://www.mrcrayfish.com/)
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mrcrayfish.furniture.tileentity;

import com.mrcrayfish.furniture.gui.inventory.ISimpleInventory;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

public class TileEntityPlate extends TileEntity implements ISimpleInventory
{
	private ItemStack food = null;
	private int rotation = 0;

	public void setFood(ItemStack food)
	{
		this.food = food;
	}

	public ItemStack getFood()
	{
		return food;
	}

	public void setRotation(int rotation)
	{
		this.rotation = rotation;
	}

	public int getRotation()
	{
		return rotation;
	}

	@Override
	public void readFromNBT(NBTTagCompound par1NBTTagCompound)
	{
		super.readFromNBT(par1NBTTagCompound);
		if (par1NBTTagCompound.hasKey("Items"))
		{
			NBTTagList tagList = (NBTTagList) par1NBTTagCompound.getTag("Items");
			for (int i = 0; i < tagList.tagCount(); ++i)
			{
				NBTTagCompound nbttagcompound1 = tagList.getCompoundTagAt(i);
				ItemStack stack = ItemStack.loadItemStackFromNBT(nbttagcompound1);
				this.setFood(stack);
			}
		}
		this.rotation = par1NBTTagCompound.getInteger("Rotation");
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tagCompound)
	{
		super.writeToNBT(tagCompound);
		NBTTagList tagList = new NBTTagList();
		ItemStack itemStack = food;
		if (itemStack != null)
		{
			NBTTagCompound nbttagcompound1 = new NBTTagCompound();
			itemStack.writeToNBT(nbttagcompound1);
			tagList.appendTag(nbttagcompound1);
		}
		tagCompound.setTag("Items", tagList);
		tagCompound.setInteger("Rotation", rotation);
		return tagCompound;
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt)
	{
		this.readFromNBT(pkt.getNbtCompound());
	}

	@Override
	public SPacketUpdateTileEntity getUpdatePacket() 
	{
		return new SPacketUpdateTileEntity(pos, getBlockMetadata(), this.writeToNBT(new NBTTagCompound()));
	}
	
	@Override
	public NBTTagCompound getUpdateTag() 
	{
		return this.writeToNBT(new NBTTagCompound());
	}

	@Override
	public int getSize()
	{
		return 1;
	}

	@Override
	public ItemStack getItem(int i)
	{
		return getFood();
	}

	@Override
	public void clear()
	{
		food = null;
	}
}
