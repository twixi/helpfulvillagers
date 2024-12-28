/*     */ package mods.helpfulvillagers.village;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import mods.helpfulvillagers.util.AIHelper;
/*     */ import net.minecraft.entity.passive.EntityAnimal;
/*     */ import net.minecraft.util.AxisAlignedBB;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class RanchGuildHall
/*     */   extends GuildHall
/*     */ {
/*     */   private int minX;
/*     */   private int minZ;
/*     */   private int minY;
/*     */   private int maxY;
/*     */   private int maxX;
/*     */   private int maxZ;
/*  25 */   public ArrayList<EntityAnimal> herd = new ArrayList<EntityAnimal>();
/*  26 */   public ArrayList<EntityAnimal> checkedAnimals = new ArrayList<EntityAnimal>();
/*  27 */   public ArrayList<BlockPos> pastureCoords = new ArrayList<BlockPos>();
/*     */   
/*     */   public RanchGuildHall(World world, HelpfulVillage village) {
/*  30 */     super(world, village);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void findPastureCoords() {
/*  37 */     for (BlockPos coords : this.insideCoords) {
/*  38 */       for (BlockPos adjCoords : AIHelper.getAdjacentCoords(coords)) {
/*  39 */         if (!this.pastureCoords.contains(adjCoords) && isFence(adjCoords)) {
/*  40 */           this.pastureCoords.add(adjCoords);
/*     */           
/*  42 */           this.minX = adjCoords.getX();
/*  43 */           this.maxX = adjCoords.getX();
/*  44 */           this.minY = adjCoords.getY();
/*  45 */           this.maxY = adjCoords.getY();
/*  46 */           this.minZ = adjCoords.getZ();
/*  47 */           this.maxZ = adjCoords.getZ();
/*  48 */           findNextFence(adjCoords);
/*     */           
/*  50 */           for (int i = this.minX; i <= this.maxX; i++) {
/*  51 */             for (int j = this.minY; j <= this.maxY; j++) {
/*  52 */               for (int k = this.minZ; k <= this.maxZ; k++) {
/*  53 */                 BlockPos fillCoords = new BlockPos(i, j, k);
/*  54 */                 if (!this.pastureCoords.contains(fillCoords)) {
/*  55 */                   this.pastureCoords.add(fillCoords);
/*     */                 }
/*     */               } 
/*     */             } 
/*     */           } 
/*  60 */           AxisAlignedBB box = AxisAlignedBB.fromBounds(this.minX, this.minY, this.minZ, this.maxX, this.maxY, this.maxZ);
/*  61 */           List<EntityAnimal> animals = this.worldObj.getEntitiesWithinAABB(EntityAnimal.class, box);
/*  62 */           for (EntityAnimal animal : animals) {
/*  63 */             if (!this.herd.contains(animal)) {
/*  64 */               this.herd.add(animal);
/*     */             }
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*  70 */     System.out.println("Herd Size: " + this.herd.size());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean isFence(BlockPos coords) {
/*  78 */     return (this.worldObj.getBlockState(coords) instanceof net.minecraft.block.BlockFence || this.worldObj.getBlockState(coords).getBlock() instanceof net.minecraft.block.BlockFenceGate || this.worldObj.getBlockState(coords) instanceof net.minecraft.block.BlockDoor);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void findNextFence(BlockPos coords) {
/*  87 */     for (BlockPos fenceAdjCoords : AIHelper.getAdjacentCoords(coords)) {
/*  88 */       if (isFence(fenceAdjCoords) && !this.pastureCoords.contains(fenceAdjCoords)) {
/*  89 */         this.pastureCoords.add(fenceAdjCoords);
/*     */         
/*  91 */         if (fenceAdjCoords.getX() < this.minX) {
/*  92 */           this.minX = fenceAdjCoords.getX();
/*  93 */         } else if (fenceAdjCoords.getX() > this.maxX) {
/*  94 */           this.maxX = fenceAdjCoords.getX();
/*     */         } 
/*     */         
/*  97 */         if (fenceAdjCoords.getY() < this.minY) {
/*  98 */           this.minY = fenceAdjCoords.getY();
/*  99 */         } else if (fenceAdjCoords.getY() > this.maxY) {
/* 100 */           this.maxY = fenceAdjCoords.getY();
/*     */         } 
/*     */         
/* 103 */         if (fenceAdjCoords.getZ() < this.minZ) {
/* 104 */           this.minZ = fenceAdjCoords.getZ();
/* 105 */         } else if (fenceAdjCoords.getZ() > this.maxZ) {
/* 106 */           this.maxZ = fenceAdjCoords.getZ();
/*     */         } 
/*     */         
/* 109 */         findNextFence(fenceAdjCoords);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void checkPasture() {
/* 115 */     this.pastureCoords.clear();
/* 116 */     findPastureCoords();
/*     */   }
/*     */ }


/* Location:              D:\Users\Joseph\Downloads\helpfulvillagers-1.7.10-1.4.0b5.jar!\mods\helpfulvillagers\village\RanchGuildHall.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */