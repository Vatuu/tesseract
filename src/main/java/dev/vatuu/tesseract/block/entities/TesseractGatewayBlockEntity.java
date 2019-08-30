package dev.vatuu.tesseract.block.entities;

import dev.vatuu.tesseract.Tesseract;
import dev.vatuu.tesseract.block.TesseractGatewayBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.dimension.DimensionType;

public class TesseractGatewayBlockEntity extends BlockEntity {

    private DimensionType targetDim;

    public TesseractGatewayBlockEntity(){
        super(Tesseract.GATEWAY_BE);
    }

    public void tryTeleportingEntity(Entity e, Direction d) {
        if (!this.world.isClient && e instanceof ServerPlayerEntity) {
            ServerWorld targetWorld = e.getServer().getWorld(targetDim);
            if(!(targetWorld.getBlockState(pos).getBlock() instanceof TesseractGatewayBlock)){
                targetWorld.setBlockState(pos, Tesseract.GATEWAY_BLOCK.getDefaultState());
            }
            BlockPos target = pos.add(0.5, 0.5, 0.5).add(d.getVector());
            ((ServerPlayerEntity)e).teleport(targetWorld, target.getX(), target.getY(), target.getZ(), e.pitch, e.yaw);
        }
    }
}
