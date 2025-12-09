package com.balburg.easypathforge;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.tags.BlockTags;

public class ForestClearerItem extends Item {

    private static final int BLOCKS_PER_DURABILITY = 5;
    private static final int CLEAR_SIZE = 4; // 4x4x4 cube

    public ForestClearerItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        
        if (!level.isClientSide) {
            // Get the block position the player is looking at
            BlockPos targetPos = player.blockPosition();
            
            // Clear the forest area
            clearForest(level, targetPos, itemStack, player);
        }
        
        return InteractionResultHolder.sidedSuccess(itemStack, level.isClientSide());
    }

    private void clearForest(Level level, BlockPos centerPos, ItemStack itemStack, Player player) {
        int blocksCleared = 0;
        
        // Clear a 4x4x4 cube centered on the target position
        for (int x = -CLEAR_SIZE / 2; x < CLEAR_SIZE / 2; x++) {
            for (int y = -CLEAR_SIZE / 2; y < CLEAR_SIZE / 2; y++) {
                for (int z = -CLEAR_SIZE / 2; z < CLEAR_SIZE / 2; z++) {
                    BlockPos targetPos = centerPos.offset(x, y, z);
                    BlockState blockState = level.getBlockState(targetPos);
                    
                    // Check if the block is a log or leaves
                    if (blockState.is(BlockTags.LOGS) || blockState.is(BlockTags.LEAVES)) {
                        level.setBlock(targetPos, Blocks.AIR.defaultBlockState(), 3);
                        blocksCleared++;
                    }
                }
            }
        }
        
        // Damage the item based on blocks cleared (1 durability per BLOCKS_PER_DURABILITY blocks, minimum 1)
        if (blocksCleared > 0) {
            itemStack.hurtAndBreak(Math.max(1, (blocksCleared + BLOCKS_PER_DURABILITY - 1) / BLOCKS_PER_DURABILITY), player, 
                (p) -> p.broadcastBreakEvent(player.getUsedItemHand()));
        }
    }
}
