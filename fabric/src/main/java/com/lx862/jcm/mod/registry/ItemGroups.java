package com.lx862.jcm.mod.registry;

import com.lx862.jcm.mod.Constants;
import org.mtr.mapping.holder.ItemConvertible;
import org.mtr.mapping.holder.ItemStack;
import org.mtr.mapping.registry.CreativeModeTabHolder;

public class ItemGroups {
    public static final CreativeModeTabHolder MAIN = JCMRegistry.REGISTRY.createCreativeModeTabHolder(
            Constants.id("main"),
            () -> new ItemStack(new ItemConvertible(Blocks.MTR_STAIRS.get().data))
    );

    public static final CreativeModeTabHolder PIDS = JCMRegistry.REGISTRY.createCreativeModeTabHolder(
            Constants.id("pids"),
            () -> new ItemStack(new ItemConvertible(Blocks.RV_PIDS.get().data))
    );
}
