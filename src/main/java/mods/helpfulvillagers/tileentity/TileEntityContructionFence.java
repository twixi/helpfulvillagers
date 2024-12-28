/*     */ package mods.helpfulvillagers.tileentity;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.tileentity.TileEntity;
/*     */ import net.minecraft.util.AxisAlignedBB;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.world.World;
			import net.minecraftforge.fml.common.registry.GameRegistry;
/*     */ 
/*     */ 
/*     */ public class TileEntityContructionFence
/*     */   extends TileEntity
/*     */ {
/*  16 */   public String player = "";
/*     */   
/*  18 */   private final int MAX_LENGTH = 64;
/*  19 */   private ArrayList<BlockPos> foundCoords = new ArrayList<BlockPos>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AxisAlignedBB setupConstructionSite(World world, BlockPos coords) {
/*  26 */     checkCoords(world, coords);
/*  27 */     AxisAlignedBB box = constructBox(world);
/*  28 */     this.foundCoords.clear();
/*  29 */     return box;
/*     */   }
/*     */   
/*     */   private void checkCoords(World world, BlockPos coords) {
/*  33 */     this.foundCoords.add(coords);
/*  34 */     for (int i = 1; i < 64; i++) {
/*     */       
/*  36 */       checkX(world, coords, i);
/*     */       
/*  38 */       checkY(world, coords, i);
/*     */       
/*  40 */       checkZ(world, coords, i);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void checkX(World world, BlockPos coords, int i) {
/*  45 */     Block target = GameRegistry.findBlock("helpfulvillagers", "construction_fence");
/*  46 */     BlockPos newCoords = new BlockPos(coords.getX() - i, coords.getY(), coords.getZ());
/*  47 */     if (this.foundCoords.contains(newCoords)) {
/*     */       return;
/*     */     }
/*     */     
/*  51 */     if (isValidBlock(world, newCoords)) {
/*  52 */       checkCoords(world, newCoords);
/*     */     }
/*     */     
/*  55 */     newCoords = new BlockPos(coords.getX() + i, coords.getY(), coords.getZ());
/*  56 */     if (this.foundCoords.contains(newCoords)) {
/*     */       return;
/*     */     }
/*     */     
/*  60 */     if (isValidBlock(world, newCoords)) {
/*  61 */       checkCoords(world, newCoords);
/*     */     }
/*     */   }
/*     */   
/*     */   private void checkY(World world, BlockPos coords, int i) {
/*  66 */     Block target = GameRegistry.findBlock("helpfulvillagers", "construction_fence");
/*  67 */     BlockPos newCoords = new BlockPos(coords.getX(), coords.getY() - i, coords.getZ());
/*  68 */     if (this.foundCoords.contains(newCoords)) {
/*     */       return;
/*     */     }
/*     */     
/*  72 */     if (isValidBlock(world, newCoords)) {
/*  73 */       checkCoords(world, newCoords);
/*     */     }
/*     */     
/*  76 */     newCoords = new BlockPos(coords.getX(), coords.getY() + i, coords.getZ());
/*  77 */     if (this.foundCoords.contains(newCoords)) {
/*     */       return;
/*     */     }
/*     */     
/*  81 */     if (isValidBlock(world, newCoords)) {
/*  82 */       checkCoords(world, newCoords);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private void checkZ(World world, BlockPos coords, int i) {
/*  88 */     BlockPos newCoords = new BlockPos(coords.getX(), coords.getY(), coords.getZ() - i);
/*  89 */     if (this.foundCoords.contains(newCoords)) {
/*     */       return;
/*     */     }
/*     */     
/*  93 */     if (isValidBlock(world, newCoords)) {
/*  94 */       checkCoords(world, newCoords);
/*     */     }
/*     */     
/*  97 */     newCoords = new BlockPos(coords.getX(), coords.getY(), coords.getZ() + i);
/*  98 */     if (this.foundCoords.contains(newCoords)) {
/*     */       return;
/*     */     }
/*     */     
/* 102 */     if (isValidBlock(world, newCoords)) {
/* 103 */       checkCoords(world, newCoords);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean isValidBlock(World world, BlockPos coords) {
/* 113 */     TileEntity tileEntity = world.getTileEntity(coords);
/*     */     
/* 115 */     if (tileEntity != null && tileEntity instanceof TileEntityContructionFence) {
/* 116 */       TileEntityContructionFence constructionFence = (TileEntityContructionFence)tileEntity;
/* 117 */       return constructionFence.player.equals(this.player);
/*     */     } 
/*     */     
/* 120 */     return false;
/*     */   }
/*     */   
/*     */   private AxisAlignedBB constructBox(World world) {
/* 124 */     int minX = Integer.MAX_VALUE;
/* 125 */     int minY = Integer.MAX_VALUE;
/* 126 */     int minZ = Integer.MAX_VALUE;
/* 127 */     int maxX = Integer.MIN_VALUE;
/* 128 */     int maxY = Integer.MIN_VALUE;
/* 129 */     int maxZ = Integer.MIN_VALUE;
/* 130 */     for (BlockPos coords : this.foundCoords) {
/* 131 */       if (coords.getX() < minX) {
/* 132 */         minX = coords.getX();
/*     */       }
/*     */       
/* 135 */       if (coords.getX() > maxX) {
/* 136 */         maxX = coords.getX();
/*     */       }
/*     */       
/* 139 */       if (coords.getY() < minY) {
/* 140 */         minY = coords.getY();
/*     */       }
/*     */       
/* 143 */       if (coords.getY() > maxY) {
/* 144 */         maxY = coords.getY();
/*     */       }
/*     */       
/* 147 */       if (coords.getZ() < minZ) {
/* 148 */         minZ = coords.getZ();
/*     */       }
/*     */       
/* 151 */       if (coords.getZ() > maxZ) {
/* 152 */         maxZ = coords.getZ();
/*     */       }
/*     */     } 
/*     */     
/* 156 */     if (minX != maxX && minY != maxY && minZ != maxZ) {
/*     */       
/* 158 */       Block block = GameRegistry.findBlock("helpfulvillagers", "active_construction_fence");
				IBlockState state0 = block.getDefaultState();
/*     */       int i;
/* 160 */       for (i = minX; i <= maxX; i++) {
					BlockPos bk1 = new BlockPos(i, minY, minZ);
					BlockPos bk2 = new BlockPos(i, minY, maxZ);
					BlockPos bk3 = new BlockPos(i, maxY, minZ);
					BlockPos bk4 = new BlockPos(i, maxY, maxZ);
/* 161 */         world.setBlockState(bk1, state0);
/* 162 */         world.setBlockState(bk2, state0);
/* 163 */         world.setBlockState(bk3, state0);
/* 164 */         world.setBlockState(bk4, state0);
/*     */       } 
/*     */       
/* 167 */       for (i = minZ; i <= maxZ; i++) {
					BlockPos bk1 = new BlockPos(minX, minY, i);
					BlockPos bk2 = new BlockPos(maxX, minY, i);
					BlockPos bk3 = new BlockPos(minX, maxY, i);
					BlockPos bk4 = new BlockPos(maxX, maxY, i);
/* 168 */         world.setBlockState(bk1, state0);
/* 169 */         world.setBlockState(bk2, state0);
/* 170 */         world.setBlockState(bk3, state0);
/* 171 */         world.setBlockState(bk4, state0);
/*     */       } 
/*     */       
/* 174 */       for (i = minY; i <= maxY; i++) {
					BlockPos bk1 = new BlockPos(minX, i, minZ);
					BlockPos bk2 = new BlockPos(maxX, i, minZ);
					BlockPos bk3 = new BlockPos(minX, i, maxZ);
					BlockPos bk4 = new BlockPos(maxX, i, maxZ);
/* 175 */         world.setBlockState(bk1, state0);
/* 176 */         world.setBlockState(bk2, state0);
/* 177 */         world.setBlockState(bk3, state0);
/* 178 */         world.setBlockState(bk4, state0);
/*     */       } 
/*     */     } else {
/* 181 */       System.out.println("Invalid Box");
/* 182 */       System.out.println("min x: " + minX);
/* 183 */       System.out.println("min y: " + minY);
/* 184 */       System.out.println("min z: " + minZ);
/* 185 */       System.out.println("max x: " + maxX);
/* 186 */       System.out.println("max y: " + maxY);
/* 187 */       System.out.println("max z: " + maxZ);
/* 188 */       return null;
/*     */     } 
/* 190 */     return AxisAlignedBB.fromBounds(minX, minY, minZ, maxX, maxY, maxZ);
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeToNBT(NBTTagCompound compound) {
/* 195 */     super.writeToNBT(compound);
/*     */     
/* 197 */     compound.setString("player", this.player);
/*     */   }
/*     */ 
/*     */   
/*     */   public void funreadFromNBT(NBTTagCompound compound) {
/* 202 */     super.readFromNBT(compound);
/*     */     
/* 204 */     this.player = compound.getString("player");
/*     */   }
/*     */ }


/* Location:              D:\Users\Joseph\Downloads\helpfulvillagers-1.7.10-1.4.0b5.jar!\mods\helpfulvillagers\tileentity\TileEntityContructionFence.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */