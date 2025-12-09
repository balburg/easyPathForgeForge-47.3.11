package com.balburg.easypathforge;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
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
            // Raycast to find the block the player is looking at
            BlockHitResult hitResult = level.clip(new ClipContext(
                player.getEyePosition(1.0F),
                player.getEyePosition(1.0F).add(player.getViewVector(1.0F).scale(5.0)),
                ClipContext.Block.OUTLINE,
                ClipContext.Fluid.NONE,
                player
            ));
            
            // Use the hit block position if available, otherwise use player position
            BlockPos targetPos = hitResult.getType() == HitResult.Type.BLOCK ? 
                hitResult.getBlockPos() : player.blockPosition();
            
            // Clear the forest area
            clearForest(level, targetPos, itemStack, player);
        }
        
        return InteractionResultHolder.sidedSuccess(itemStack, level.isClientSide());
    }

    private void clearForest(Level level, BlockPos centerPos, ItemStack itemStack, Player player) {
        int blocksCleared = 0;
        
        // Clear a 4x4x4 cube centered on the target position
        // Using -2 to 1 (inclusive) creates a 4-block range: -2, -1, 0, 1
        for (int x = -2; x <= 1; x++) {
            for (int y = -2; y <= 1; y++) {
                for (int z = -2; z <= 1; z++) {
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
