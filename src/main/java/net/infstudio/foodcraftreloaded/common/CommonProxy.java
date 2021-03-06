/**
 * FoodCraft Mod for Minecraft.
 * Copyright (C) 2016 Infinity Studio.
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * @license GPLv3
 */
package net.infstudio.foodcraftreloaded.common;

import net.infstudio.foodcraftreloaded.block.BlockLoader;
import net.infstudio.foodcraftreloaded.item.ItemLoader;
import net.infstudio.foodcraftreloaded.item.food.PropertiedFoodLoader;
import net.infstudio.foodcraftreloaded.utils.loader.LoaderManager;
import net.infstudio.foodcraftreloaded.worldgen.FruitTreeGenerator;
import net.minecraftforge.fml.common.LoaderState;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.relauncher.Side;

import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.util.Arrays;

public class CommonProxy {
    private final LoaderManager loaderManager = new LoaderManager();

    public CommonProxy() {
        Arrays.asList(
            BlockLoader.class, ItemLoader.class,
            FruitLoader.class, KitchenKnifeLoader.class,
            PropertiedFoodLoader.class,
            AchievementLoader.class, RecipeLoader.class,
            EventLoader.class
        ).forEach(loaderManager::addLoader);
    }

    @OverridingMethodsMustInvokeSuper
    public void construct(FMLConstructionEvent event) {
        loaderManager.invoke(event, LoaderState.CONSTRUCTING, Side.SERVER);
    }

    @OverridingMethodsMustInvokeSuper
    public void preInit(FMLPreInitializationEvent event) {
        loaderManager.invoke(event, LoaderState.PREINITIALIZATION, Side.SERVER);
    }

    @OverridingMethodsMustInvokeSuper
    public void init(FMLInitializationEvent event) {
        loaderManager.invoke(event, LoaderState.INITIALIZATION, Side.SERVER);
    }

    @OverridingMethodsMustInvokeSuper
    public void postInit(FMLPostInitializationEvent event) {
        loaderManager.invoke(event, LoaderState.POSTINITIALIZATION, Side.SERVER);
    }

    @OverridingMethodsMustInvokeSuper
    public void loadComplete(FMLLoadCompleteEvent event) {
        loaderManager.invoke(event, LoaderState.AVAILABLE, Side.SERVER);
        new FruitTreeGenerator();
    }

    public LoaderManager getLoaderManager() {
        return loaderManager;
    }
}
