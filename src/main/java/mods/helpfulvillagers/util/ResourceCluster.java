/*     */ package mods.helpfulvillagers.util;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.nbt.NBTBase;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.nbt.NBTTagList;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ResourceCluster
/*     */ {
/*     */   public World world;
/*     */   public BlockPos coords;
/*     */   public BlockPos lowestCoords;
/*     */   public Block startBlock;
/*     */   public ArrayList blockCluster;
/*     */   public boolean builtFlag;
/*     */   
/*     */   public ResourceCluster(World world) {
/*  28 */     this.world = world;
/*     */   }
/*     */   
/*     */   public ResourceCluster(World world, BlockPos coords) {
/*  32 */     this.world = world;
/*  33 */     this.coords = coords;
/*  34 */     this.lowestCoords = coords;
/*  35 */     this.startBlock = world.getBlockState(coords).getBlock();
/*  36 */     this.blockCluster = new ArrayList();
/*  37 */     this.builtFlag = false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ArrayList getAdjacent() {
/*  45 */     ArrayList<Block> blocks = new ArrayList();
/*     */     
/*  47 */     for (int x = -1; x <= 1; x++) {
/*  48 */       for (int y = -1; y <= 1; y++) {
/*  49 */         for (int z = -1; z <= 1; z++) {
/*  50 */           Block block = this.world.getBlockState(this.coords.add(x,y,z)).getBlock();
/*  51 */           blocks.add(block);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/*  56 */     return blocks;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ArrayList getAdjacentCoords(BlockPos coords) {
/*  66 */     ArrayList<BlockPos> adjacent = new ArrayList();
/*     */     
/*  68 */     for (int x = -1; x <= 1; x++) {
/*  69 */       for (int y = -1; y <= 1; y++) {
/*  70 */         for (int z = -1; z <= 1; z++) {
/*  71 */           BlockPos coord = new BlockPos(coords.getX() + x, coords.getY() + y, coords.getZ() + z);
/*  72 */           adjacent.add(coord);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/*  77 */     return adjacent;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void buildCluster() {
/*  84 */     buildCluster(0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void buildCluster(int limit) {
/*  91 */     if (!this.startBlock.equals(Blocks.air))
/*  92 */       buildCluster(this.coords, limit); 
/*     */   }
/*     */   
/*     */   private void buildCluster(BlockPos coords, int limit) {
/*  96 */     if (limit > 0) {
/*  97 */       if (AIHelper.findDistance(coords.getX(), this.coords.getX()) > limit) {
/*     */         return;
/*     */       }
/*     */       
/* 101 */       if (AIHelper.findDistance(coords.getY(), this.coords.getY()) > limit) {
/*     */         return;
/*     */       }
/*     */       
/* 105 */       if (AIHelper.findDistance(coords.getY(), this.coords.getY()) > limit) {
/*     */         return;
/*     */       }
/*     */     } 
/*     */     
/* 110 */     Block currentBlock = this.world.getBlockState(coords).getBlock();
/* 111 */     if (!this.blockCluster.contains(coords) && Block.getIdFromBlock(currentBlock) == Block.getIdFromBlock(this.startBlock)) {
/*     */       
/* 113 */       if (coords.getY() < this.lowestCoords.getY()) {
/* 114 */         this.lowestCoords = coords;
/*     */       }
/*     */       
/* 117 */       this.blockCluster.add(coords);
/* 118 */       ArrayList<BlockPos> adjacent = getAdjacentCoords(coords);
/* 119 */       for (int i = 0; i < adjacent.size(); i++) {
/* 120 */         if (!adjacent.get(i).equals(coords)) {
/* 121 */           buildCluster((BlockPos)adjacent.get(i), limit);
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean matchesCluster(ResourceCluster cluster) {
/* 133 */     ArrayList<BlockPos> otherCluster = cluster.blockCluster;
/* 134 */     if (this.blockCluster.size() != otherCluster.size()) {
/* 135 */       return false;
/*     */     }
/* 137 */     for (int i = 0; i < otherCluster.size(); i++) {
/* 138 */       BlockPos otherCoords = otherCluster.get(i);
/* 139 */       if (!this.blockCluster.contains(otherCoords)) {
/* 140 */         return false;
/*     */       }
/*     */     } 
/* 143 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public NBTTagList writeToNBT(NBTTagList par1NBTTagList) {
/* 154 */     NBTTagCompound nbttagcompound = new NBTTagCompound();
/* 155 */     int[] coords = { this.coords.getX(), this.coords.getY(), this.coords.getZ() };
/* 156 */     nbttagcompound.setIntArray("Coords", coords);
/* 157 */     par1NBTTagList.appendTag((NBTBase)nbttagcompound);
/*     */     
/* 159 */     return par1NBTTagList;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void readFromNBT(NBTTagList par1NBTTagList) {
/* 169 */     NBTTagCompound nbttagcompound = par1NBTTagList.getCompoundTagAt(0);
/* 170 */     int[] coords = nbttagcompound.getIntArray("Coords");
/* 171 */     this.coords = new BlockPos(coords[0], coords[1], coords[2]);
/* 172 */     this.lowestCoords = this.coords;
/* 173 */     this.blockCluster = new ArrayList();
/* 174 */     this.builtFlag = false;
/*     */   }
/*     */ }


/* Location:              D:\Users\Joseph\Downloads\helpfulvillagers-1.7.10-1.4.0b5.jar!\mods\helpfulvillager\\util\ResourceCluster.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */