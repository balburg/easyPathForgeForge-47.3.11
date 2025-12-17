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

public class GrassCleanerItem extends Item {

    private static final int BLOCKS_PER_DURABILITY = 5;
    private static final int CLEAR_SIZE = 8; // 8x8x8 cube

    public GrassCleanerItem(Properties properties) {
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
            
            if (hitResult.getType() == HitResult.Type.BLOCK) {
                BlockPos targetPos = hitResult.getBlockPos();
                BlockState targetBlock = level.getBlockState(targetPos);
                
                // Only activate when right-clicking on grass blocks
                if (targetBlock.is(Blocks.GRASS_BLOCK)) {
                    clearGrassAndFlowers(level, targetPos, itemStack, player);
                }
            }
        }
        
        return InteractionResultHolder.sidedSuccess(itemStack, level.isClientSide());
    }

    private void clearGrassAndFlowers(Level level, BlockPos centerPos, ItemStack itemStack, Player player) {
        int blocksCleared = 0;
        
        // Clear an 8x8x8 cube centered on the target position
        // Using -4 to 3 (inclusive) creates an 8-block range: -4, -3, -2, -1, 0, 1, 2, 3
        for (int x = -4; x <= 3; x++) {
            for (int y = -4; y <= 3; y++) {
                for (int z = -4; z <= 3; z++) {
                    BlockPos targetPos = centerPos.offset(x, y, z);
                    BlockState blockState = level.getBlockState(targetPos);
                    
                    // Check if the block is grass (short grass/tall grass) or flowers
                    // Using BlockTags.FLOWERS for flowers and checking for grass/tall grass blocks
                    if (isRemovableVegetation(blockState)) {
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
    
    /**
     * Checks if a block is removable vegetation (grass, flowers, etc.)
     * This should NOT include grass blocks or dirt blocks, only the plants on top
     */
    private boolean isRemovableVegetation(BlockState blockState) {
        // Check for flowers using the BlockTags.FLOWERS tag (includes all vanilla flowers)
        if (blockState.is(BlockTags.FLOWERS)) {
            return true;
        }
        
        // Check for various grass/plant types
        // Note: In Minecraft 1.20.1, Blocks.GRASS refers to the grass plant, not grass_block
        if (blockState.is(Blocks.GRASS) || 
            blockState.is(Blocks.TALL_GRASS) ||
            blockState.is(Blocks.FERN) ||
            blockState.is(Blocks.LARGE_FERN)) {
            return true;
        }
        
        // Also include other decorative plants
        if (blockState.is(Blocks.DEAD_BUSH) ||
            blockState.is(Blocks.SEAGRASS) ||
            blockState.is(Blocks.TALL_SEAGRASS)) {
            return true;
        }
        
        return false;
    }
}
