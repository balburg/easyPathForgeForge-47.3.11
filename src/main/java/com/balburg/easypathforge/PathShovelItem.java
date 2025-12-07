package com.balburg.easypathforge;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class PathShovelItem extends Item {

    private static final int BLOCKS_PER_DURABILITY = 5;

    public PathShovelItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        
        if (!level.isClientSide) {
            // Get player's position and facing direction
            BlockPos playerPos = player.blockPosition();
            Direction facing = player.getDirection();
            
            // Start from the block the player is standing on
            BlockPos startPos = playerPos.below();
            
            // Create the path based on facing direction
            createPath(level, startPos, facing, itemStack, player);
        }
        
        return InteractionResultHolder.sidedSuccess(itemStack, level.isClientSide());
    }

    private void createPath(Level level, BlockPos startPos, Direction facing, ItemStack itemStack, Player player) {
        int pathLength = 10;
        int pathWidth = 4;
        
        // Determine the perpendicular direction for width
        Direction leftDir = facing.getCounterClockWise();
        
        // Calculate offset to center the path on the player
        int widthOffset = pathWidth / 2;
        
        int blocksReplaced = 0;
        
        // Create the path
        for (int length = 0; length < pathLength; length++) {
            for (int width = -widthOffset; width < pathWidth - widthOffset; width++) {
                // Calculate position based on facing direction
                BlockPos targetPos = startPos.relative(facing, length).relative(leftDir, width);
                
                BlockState blockState = level.getBlockState(targetPos);
                
                // Only replace grass blocks
                if (blockState.is(Blocks.GRASS_BLOCK)) {
                    level.setBlock(targetPos, Blocks.COBBLESTONE.defaultBlockState(), 3);
                    blocksReplaced++;
                }
            }
        }
        
        // Damage the item based on blocks replaced (1 durability per BLOCKS_PER_DURABILITY blocks, minimum 1)
        if (blocksReplaced > 0) {
            itemStack.hurtAndBreak(Math.max(1, (blocksReplaced + BLOCKS_PER_DURABILITY - 1) / BLOCKS_PER_DURABILITY), player, 
                (p) -> p.broadcastBreakEvent(player.getUsedItemHand()));
        }
    }
}
