/*     */ package mods.helpfulvillagers.util;
/*     */ 
/*     */ import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
/*     */ import java.util.ArrayList;
/*     */ import mods.helpfulvillagers.entity.AbstractVillager;
/*     */ import mods.helpfulvillagers.enums.EnumConstructionType;
/*     */ import mods.helpfulvillagers.enums.EnumMessage;
/*     */ import mods.helpfulvillagers.main.HelpfulVillagers;
/*     */ import mods.helpfulvillagers.network.PlayerMessagePacket;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.nbt.NBTBase;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.nbt.NBTTagInt;
/*     */ import net.minecraft.nbt.NBTTagIntArray;
/*     */ import net.minecraft.nbt.NBTTagList;
/*     */ import net.minecraft.util.AxisAlignedBB;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ConstructionSite
/*     */ {
/*     */   private World world;
/*     */   private AxisAlignedBB bounds;
/*     */   private Block[][][] blocks;
/*     */   private EnumConstructionType jobType;
/*     */   private int[] currentIndex;
/*  35 */   private ArrayList<BlockPos> fenceCoords = new ArrayList<BlockPos>();
/*     */   
/*     */   private boolean finished = false;
/*     */   
/*     */   public ConstructionSite(World world) {
/*  40 */     this.world = world;
/*     */   }
/*     */   
/*     */   public ConstructionSite(World world, AxisAlignedBB bounds, EnumConstructionType jobType) {
/*  44 */     this.world = world;
/*  45 */     this.bounds = bounds;
/*  46 */     this.jobType = jobType;
/*  47 */     this.currentIndex = new int[] { 0, 0, 0 };
/*  48 */     this.blocks = new Block[(int)(bounds.maxX - bounds.minX + 1.0D)][(int)(bounds.maxY - bounds.minY + 1.0D)][(int)(bounds.maxZ - bounds.minZ + 1.0D)];
/*  49 */     loadBlocks();
/*     */   }
/*     */   
/*     */   public ConstructionSite(World world, AxisAlignedBB bounds, EnumConstructionType jobType, Object blueprint) {
/*  53 */     this.world = world;
/*  54 */     this.bounds = bounds;
/*  55 */     this.jobType = jobType;
/*  56 */     this.currentIndex = new int[] { 0, 0, 0 };
/*  57 */     this.blocks = new Block[(int)(bounds.maxX - bounds.minX + 1.0D)][(int)(bounds.maxY - bounds.minY + 1.0D)][(int)(bounds.maxZ - bounds.minZ + 1.0D)];
/*  58 */     loadBlocks();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void loadBlocks() {
/*  65 */     for (int i = 0; i < this.blocks.length; i++) {
/*  66 */       for (int j = 0; j < (this.blocks[i]).length; j++) {
/*  67 */         for (int k = 0; k < (this.blocks[i][j]).length; k++) {
/*  68 */           BlockPos currCoords = new BlockPos((int)this.bounds.minX + i, (int)this.bounds.minY + j, (int)this.bounds.minZ + k);
/*  69 */           Block block = this.world.getBlockState(currCoords).getBlock();
/*     */           
/*  71 */           switch (this.jobType) {
/*     */             case DEMOLISH:
/*  73 */               this.blocks[i][j][k] = Blocks.air;
/*     */               break;
/*     */             
/*     */             case RECORD:
/*  77 */               this.blocks[i][j][k] = null;
/*     */               break;
/*     */             
/*     */             default:
/*  81 */               System.out.println("Construction Job Type Not Found");
/*     */               break;
/*     */           } 
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
/*     */   
/*     */   public void doJob(AbstractVillager villager) {
/*  96 */     if (this.finished) {
/*     */       return;
/*     */     }
/*     */     
/* 100 */     for (int i = this.currentIndex[0]; i < this.blocks.length; i++) {
/* 101 */       for (int j = this.currentIndex[1]; j < (this.blocks[i]).length; j++) {
/* 102 */         for (int k = this.currentIndex[2]; k < (this.blocks[i][j]).length; k++) {
/* 103 */           BlockPos currCoords = new BlockPos((int)this.bounds.minX + i, (int)this.bounds.minY + j, (int)this.bounds.minZ + k);
/* 104 */           Block block = this.world.getBlockState(currCoords).getBlock();
/* 105 */           if (block != this.blocks[i][j][k] && !(block instanceof mods.helpfulvillagers.block.BlockActiveConstructionFence)) {
/* 106 */             switch (this.jobType) {
/*     */               case DEMOLISH:
/* 108 */                 if (block instanceof net.minecraft.block.BlockLiquid) {
/* 109 */                   jobFinished();
/*     */                   break;
/*     */                 } 
/* 112 */                 AIHelper.breakBlock(currCoords, villager);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */                 
/* 129 */                 this.currentIndex[0] = i;
/* 130 */                 this.currentIndex[1] = j;
/* 131 */                 this.currentIndex[2] = k; return;case RECORD: if (block instanceof net.minecraft.block.BlockLiquid) { this.blocks[i][j][k] = Blocks.air; jobFinished(); break; }  this.blocks[i][j][k] = block; this.currentIndex[0] = i; this.currentIndex[1] = j; this.currentIndex[2] = k; return;default: System.out.println("Construction Job Type Not Found"); this.currentIndex[0] = i; this.currentIndex[1] = j; this.currentIndex[2] = k;
/*     */                 return;
/*     */             } 
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/* 138 */     jobFinished();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void jobFinished() {
/* 146 */     for (int i = 0; i < this.blocks.length; i++) {
/* 147 */       for (int j = 0; j < (this.blocks[i]).length; j++) {
/* 148 */         for (int k = 0; k < (this.blocks[i][j]).length; k++) {
/*     */           
/* 150 */           BlockPos currCoords = new BlockPos((int)this.bounds.minX + i, (int)this.bounds.minY + j, (int)this.bounds.minZ + k);
/* 151 */           Block block = this.world.getBlockState(currCoords).getBlock();
/*     */           
/* 153 */           if (block instanceof mods.helpfulvillagers.block.BlockActiveConstructionFence) {
/* 154 */             if (!this.fenceCoords.contains(currCoords)) {
/* 155 */               this.fenceCoords.add(currCoords);
/*     */             
/*     */             }
/*     */           
/*     */           }
/* 160 */           else if (block != this.blocks[i][j][k] && !(block instanceof net.minecraft.block.BlockLiquid)) {
/* 161 */             this.currentIndex[0] = i;
/* 162 */             this.currentIndex[1] = j;
/* 163 */             this.currentIndex[2] = k;
/*     */             
/*     */             return;
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 170 */     BlockPos coords = new BlockPos((int)this.bounds.minX, (int)this.bounds.minY, (int)this.bounds.minZ);
/* 171 */     HelpfulVillagers.network.sendToAll((IMessage)new PlayerMessagePacket("Construction Job Finished", EnumMessage.CONSTRUCTION, coords));
/* 172 */     this.finished = true;
/* 173 */     removeFences();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void removeFences() {
/* 180 */     for (BlockPos coords : this.fenceCoords) {
/* 181 */       this.world.setBlockToAir(coords);
/*     */     }
/* 183 */     this.fenceCoords.clear();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BlockPos getCenter() {
/* 190 */     int x = (int)(this.bounds.minX + (this.bounds.maxX - this.bounds.minX) / 2.0D);
/* 191 */     int y = (int)(this.bounds.minY + (this.bounds.maxY - this.bounds.minY) / 2.0D);
/* 192 */     int z = (int)(this.bounds.minZ + (this.bounds.maxZ - this.bounds.minZ) / 2.0D);
/* 193 */     return new BlockPos(x, y, z);
/*     */   }
/*     */   
/*     */   public AxisAlignedBB getBounds() {
/* 197 */     return this.bounds;
/*     */   }
/*     */   
/*     */   public EnumConstructionType getJobType() {
/* 201 */     return this.jobType;
/*     */   }
/*     */   
/*     */   public boolean isFinished() {
/* 205 */     return this.finished;
/*     */   }
/*     */   
/*     */   public void cancelConstruction() {
/* 209 */     this.finished = true;
/*     */   }
/*     */ 
/*     */   
/*     */   public NBTTagCompound writeToNBT(NBTTagCompound compound) {
/* 214 */     int[] bounds = new int[6];
/* 215 */     bounds[0] = (int)this.bounds.minX;
/* 216 */     bounds[1] = (int)this.bounds.minY;
/* 217 */     bounds[2] = (int)this.bounds.minZ;
/* 218 */     bounds[3] = (int)this.bounds.maxX;
/* 219 */     bounds[4] = (int)this.bounds.maxY;
/* 220 */     bounds[5] = (int)this.bounds.maxZ;
/* 221 */     compound.setTag("Bounds", (NBTBase)new NBTTagIntArray(bounds));
/*     */ 
/*     */     
/* 224 */     compound.setTag("Job Type", (NBTBase)new NBTTagInt(this.jobType.ordinal()));
/*     */ 
/*     */     
/* 227 */     compound.setTag("Current Index", (NBTBase)new NBTTagIntArray(this.currentIndex));
/*     */ 
/*     */     
/* 230 */     NBTTagList nbtTagList = new NBTTagList();
/* 231 */     for (int i = 0; i < this.blocks.length; i++) {
/* 232 */       for (int j = 0; j < (this.blocks[i]).length; j++) {
/* 233 */         for (int k = 0; k < (this.blocks[i][j]).length; k++) {
/* 234 */           String index = i + " " + j + " " + k;
/* 235 */           NBTTagCompound nbttagcompound = new NBTTagCompound();
/* 236 */           nbttagcompound.setString("Index", index);
/* 237 */           nbttagcompound.setInteger("Block", Block.getIdFromBlock(this.blocks[i][j][k]));
/* 238 */           nbtTagList.appendTag((NBTBase)nbttagcompound);
/*     */         } 
/*     */       } 
/*     */     } 
/* 242 */     compound.setTag("Blocks", (NBTBase)nbtTagList);
/*     */     
/* 244 */     return compound;
/*     */   }
/*     */ 
/*     */   
/*     */   public void readFromNBT(NBTTagCompound compound) {
/* 249 */     int[] bounds = compound.getIntArray("Bounds");
/* 250 */     this.bounds = AxisAlignedBB.fromBounds(bounds[0], bounds[1], bounds[2], bounds[3], bounds[4], bounds[5]);
/*     */ 
/*     */     
/* 253 */     this.jobType = EnumConstructionType.values()[compound.getInteger("Job Type")];
/*     */ 
/*     */     
/* 256 */     this.currentIndex = compound.getIntArray("Current Index");
/*     */ 
/*     */     
/* 259 */     this.blocks = new Block[(int)(this.bounds.maxX - this.bounds.minX + 1.0D)][(int)(this.bounds.maxY - this.bounds.minY + 1.0D)][(int)(this.bounds.maxZ - this.bounds.minZ + 1.0D)];
/* 260 */     NBTTagList nbtTagList = compound.getTagList("Blocks", compound.getId());
/* 261 */     for (int i = 0; i < nbtTagList.tagCount(); i++) {
/* 262 */       NBTTagCompound nbttagcompound = nbtTagList.getCompoundTagAt(i);
/* 263 */       String sIndex = nbttagcompound.getString("Index");
/* 264 */       String[] sIndices = sIndex.split(" ");
/* 265 */       int[] iIndices = new int[3];
/* 266 */       iIndices[0] = Integer.parseInt(sIndices[0]);
/* 267 */       iIndices[1] = Integer.parseInt(sIndices[1]);
/* 268 */       iIndices[2] = Integer.parseInt(sIndices[2]);
/* 269 */       this.blocks[iIndices[0]][iIndices[1]][iIndices[2]] = Block.getBlockById(nbttagcompound.getInteger("Block"));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static ConstructionSite loadSiteFromNBT(NBTTagCompound compound, World world) {
/* 275 */     ConstructionSite site = new ConstructionSite(world);
/* 276 */     site.readFromNBT(compound);
/* 277 */     return site;
/*     */   }
/*     */ }


/* Location:              D:\Users\Joseph\Downloads\helpfulvillagers-1.7.10-1.4.0b5.jar!\mods\helpfulvillager\\util\ConstructionSite.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */