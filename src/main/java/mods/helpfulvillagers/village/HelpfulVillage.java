/*     */ package mods.helpfulvillagers.village;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ import mods.helpfulvillagers.crafting.CraftQueue;
/*     */ import mods.helpfulvillagers.econ.VillageEconomy;
/*     */ import mods.helpfulvillagers.entity.AbstractVillager;
/*     */ import mods.helpfulvillagers.util.AIHelper;
/*     */ import mods.helpfulvillagers.util.ConstructionSite;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.BlockDoor;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.entity.item.EntityItemFrame;
/*     */ import net.minecraft.entity.monster.IMob;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.nbt.NBTBase;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.nbt.NBTTagInt;
/*     */ import net.minecraft.nbt.NBTTagIntArray;
/*     */ import net.minecraft.nbt.NBTTagList;
/*     */ import net.minecraft.tileentity.TileEntityChest;
/*     */ import net.minecraft.util.AxisAlignedBB;
/*     */ import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.village.Village;
/*     */ import net.minecraft.village.VillageDoorInfo;
/*     */ import net.minecraft.world.IBlockAccess;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraftforge.common.DimensionManager;
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
/*     */ public class HelpfulVillage
/*     */ {
/*  61 */   private final int NUM_PROFESSIONS = 13;
/*  62 */   private final int LOAD_TIME_MAX = 500;
/*     */   public World world;
/*     */   private long loadTime;
/*  65 */   public int dimension = Integer.MAX_VALUE;
/*     */   public BlockPos initialCenter;
/*  67 */   public BlockPos actualCenter = null;
/*  68 */   public int radius = 0;
/*  69 */   public ArrayList<GuildHall> guildHallList = new ArrayList<GuildHall>();
/*  70 */   public boolean[] unlockedHalls = new boolean[13];
/*  71 */   private int numVillagers = 0;
/*  72 */   private static int totalAdded = 0;
/*  73 */   private long lastAddedVillager = 0L;
/*     */   public boolean isAnnihilated = false;
/*     */   public AxisAlignedBB villageBounds;
/*     */   public AxisAlignedBB actualBounds;
/*  77 */   public ArrayList<BlockPos> villageDoors = new ArrayList<BlockPos>(); public int minX;
/*     */   public int minY;
/*     */   public int minZ;
/*     */   public int maxX;
/*  81 */   public CraftQueue craftQueue = new CraftQueue(); public int maxY; public int maxZ; public boolean dayCheck; public EntityLivingBase lastAggressor;
/*  82 */   public VillageEconomy economy = new VillageEconomy(this, false);
/*     */   public boolean priceCalcStarted = false;
/*     */   public boolean pricesCalculated = false;
/*  85 */   public ArrayList<ConstructionSite> constructionSites = new ArrayList<ConstructionSite>();
/*     */   
/*     */   public HelpfulVillage() {
/*  88 */     this.world = null;
/*     */   }
/*     */   
/*     */   public HelpfulVillage(World world, BlockPos center) {
/*  92 */     this.world = world;
/*  93 */     this.dimension = world.provider.getDimensionId();
/*  94 */     this.initialCenter = center;
/*  95 */     init();
/*     */   }
/*     */   
/*     */   public HelpfulVillage(World world, Village village) {
/*  99 */     this.world = world;
/* 100 */     this.dimension = world.provider.getDimensionId();
/* 101 */     this.initialCenter = village.getCenter();
/* 102 */     this.radius = village.getVillageRadius();
/* 103 */     init();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void init() {
/* 110 */     if (this.world == null) {
/* 111 */       this.world = (World)DimensionManager.getWorld(this.dimension);
/*     */     }
/*     */     
/* 114 */     this.loadTime = this.world.getTotalWorldTime();
/* 115 */     if (this.radius <= 0) {
/* 116 */       this.radius = 32;
/*     */     }
/* 118 */     this.lastAggressor = null;
/* 119 */     this.dayCheck = true;
/* 120 */     if (!this.world.isRemote) {
/* 121 */       initBounds();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void initBounds() {
/* 130 */     if (this.actualCenter == null) {
/* 131 */       this.minX = this.initialCenter.getX();
/* 132 */       this.maxX = this.initialCenter.getX();
/* 133 */       this.minY = this.initialCenter.getY();
/* 134 */       this.maxY = this.initialCenter.getY();
/* 135 */       this.minZ = this.initialCenter.getZ();
/* 136 */       this.maxZ = this.initialCenter.getZ();
/* 137 */       this.villageBounds = AxisAlignedBB.fromBounds((this.initialCenter.getX() - this.radius), (this.initialCenter.getY() - this.radius), (this.initialCenter.getZ() - this.radius), (this.initialCenter.getX() + this.radius), (this.initialCenter.getY() + this.radius), (this.initialCenter.getZ() + this.radius));
/* 138 */       this.actualCenter = this.initialCenter;
/*     */     } else {
/* 140 */       this.minX = this.actualCenter.getX();
/* 141 */       this.maxX = this.actualCenter.getX();
/* 142 */       this.minY = this.actualCenter.getY();
/* 143 */       this.maxY = this.actualCenter.getY();
/* 144 */       this.minZ = this.actualCenter.getZ();
/* 145 */       this.maxZ = this.actualCenter.getZ();
/* 146 */       this.villageBounds = AxisAlignedBB.fromBounds((this.actualCenter.getX() - this.radius), (this.actualCenter.getY() - this.radius), (this.actualCenter.getZ() - this.radius), (this.actualCenter.getX() + this.radius), (this.actualCenter.getY() + this.radius), (this.actualCenter.getZ() + this.radius));
/*     */     } 
/* 148 */     this.actualBounds = AxisAlignedBB.fromBounds(this.minX, this.minY, this.minZ, this.maxX, this.maxY, this.maxZ);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void updateVillageBox() {
/* 156 */     if (this.world == null) {
/*     */       return;
/*     */     }
/*     */     
/* 160 */     Iterator<BlockPos> iterator = this.villageDoors.iterator();
/* 161 */     while (iterator.hasNext()) {
/* 162 */       BlockPos currDoor = iterator.next();
/* 163 */       Block checkBlock = this.world.getBlockState(currDoor).getBlock();
/* 164 */       if (checkBlock instanceof BlockDoor || checkBlock.equals(Blocks.iron_door)) {
/* 165 */         iterator.remove(); continue;
/* 166 */       }  if (getDoorFromCoords(checkBlock, currDoor.getX(), currDoor.getY(), currDoor.getZ()) == null) {
/* 167 */         iterator.remove();
/*     */       }
/*     */     } 
/*     */     
/* 171 */     for (int x = (int)this.villageBounds.minX; x <= this.villageBounds.maxX; x++) {
/* 172 */       for (int y = (int)this.villageBounds.minY; y <= this.villageBounds.maxY; y++) {
/* 173 */         for (int z = (int)this.villageBounds.minZ; z <= this.villageBounds.maxZ; z++) {
					BlockPos currCoords = new BlockPos(x, y, z);
/* 174 */           Block checkBlock = this.world.getBlockState(currCoords).getBlock();
/* 175 */  
/* 176 */           if (checkBlock instanceof BlockDoor && !checkBlock.equals(Blocks.iron_door) && !this.villageDoors.contains(currCoords)) {
/* 177 */             BlockPos aboveCoords = new BlockPos(x, y + 1, z);
/* 178 */             BlockPos belowCoords = new BlockPos(x, y - 1, z);
/* 179 */             if ((!this.villageDoors.contains(aboveCoords) || !this.villageDoors.contains(belowCoords)) && 
/* 180 */               getDoorFromCoords(checkBlock, currCoords.getX(), currCoords.getY(), currCoords.getZ()) != null) {
/* 181 */               this.villageDoors.add(currCoords);
/*     */             }
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 189 */     int dist = 0;
/* 190 */     initBounds();
/* 191 */     for (int i = 0; i < this.villageDoors.size(); i++) {
/* 192 */       BlockPos currDoor = this.villageDoors.get(i);
/* 193 */       if (currDoor.getX() < this.minX) {
/* 194 */         this.minX = currDoor.getX() - 5;
/* 195 */       } else if (currDoor.getX() > this.maxX) {
/* 196 */         this.maxX = currDoor.getX() + 5;
/*     */       } 
/*     */       
/* 199 */       if (currDoor.getY() < this.minY) {
/* 200 */         this.minY = currDoor.getY() - 5;
/* 201 */       } else if (currDoor.getY() > this.maxY) {
/* 202 */         this.maxY = currDoor.getY() + 5;
/*     */       } 
/*     */       
/* 205 */       if (currDoor.getZ() < this.minZ) {
/* 206 */         this.minZ = currDoor.getZ() - 5;
/* 207 */       } else if (currDoor.getZ() > this.maxZ) {
/* 208 */         this.maxZ = currDoor.getZ() + 5;
/*     */       } 
/* 210 */       dist = (int)Math.max(getDistanceSquaredToChunkCoordinates(currDoor.getX(), currDoor.getZ(), this.actualCenter.getX(), this.actualCenter.getZ()), dist);
/*     */     } 
/* 212 */     this.radius = Math.max(32, (int)Math.sqrt(dist) + 5);
/* 213 */     this.actualBounds = AxisAlignedBB.fromBounds(this.minX, this.minY, this.minZ, this.maxX, this.maxY, this.maxZ);
/* 214 */     this.actualCenter = getActualCenter();
/* 215 */     this.villageBounds = AxisAlignedBB.fromBounds((this.actualCenter.getX() - this.radius), (this.actualCenter.getY() - this.radius), (this.actualCenter.getZ() - this.radius), (this.actualCenter.getX() + this.radius), (this.actualCenter.getY() + this.radius), (this.actualCenter.getZ() + this.radius));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BlockPos getInitialCenter() {
/* 223 */     return this.initialCenter;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BlockPos getActualCenter() {
/* 230 */     int x = (this.minX + this.maxX) / 2;
/* 231 */     int y = (this.minY + this.maxY) / 2;
/* 232 */     int z = (this.minZ + this.maxZ) / 2;
/* 233 */     return new BlockPos(x, y, z);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getActualRadius() {
/* 239 */     int xRadius = Math.abs(this.maxX - this.minX) / 2;
/* 240 */     int zRadius = Math.abs(this.maxZ - this.minZ) / 2;
/* 241 */     return Math.max(xRadius, zRadius);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getPopulation() {
/* 248 */     return this.numVillagers;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getTotalVillagers() {
/* 255 */     return this.world.countEntities(AbstractVillager.class);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getTotalAdded() {
/* 262 */     return totalAdded;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getLastAdded() {
/* 269 */     return this.lastAddedVillager;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isFullyLoaded() {
/* 276 */     return (this.world.getTotalWorldTime() - this.loadTime > 500L);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addVillager() {
/* 283 */     if (totalAdded < this.world.countEntities(AbstractVillager.class) || !isFullyLoaded()) {
/*     */       
/* 285 */       this.numVillagers++;
/* 286 */       totalAdded++;
/* 287 */       this.lastAddedVillager = this.world.getTotalWorldTime();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeVillager() {
/* 298 */     this.numVillagers--;
/* 299 */     totalAdded--;
/* 300 */     if (this.numVillagers <= 0) {
/* 301 */       this.isAnnihilated = true;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isInRange(double x, double y, double z) {
/* 313 */     if (this.villageBounds == null) {
/* 314 */       return false;
/*     */     }
/* 316 */     if (x < this.villageBounds.maxX && x > this.villageBounds.minX && y < this.villageBounds.maxY && y > this.villageBounds.minY) if ((((z < this.villageBounds.maxZ) ? 1 : 0) & ((z > this.villageBounds.minZ) ? 1 : 0)) != 0);  return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isInsideVillage(double x, double y, double z) {
/* 326 */     if (this.actualBounds == null) {
/* 327 */       return false;
/*     */     }
/* 329 */     if (x < this.actualBounds.maxX && x > this.actualBounds.minX && y < this.actualBounds.maxY && y > this.actualBounds.minY) if ((((z < this.actualBounds.maxZ) ? 1 : 0) & ((z > this.actualBounds.minZ) ? 1 : 0)) != 0);  return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public VillageDoorInfo findNearestDoorUnrestricted(int x, int y, int z) {
/* 339 */     VillageDoorInfo targetDoor = null;
/* 340 */     BlockPos currCoords = new BlockPos(x, y, z);
/* 341 */     Iterator<BlockPos> iterator = this.villageDoors.iterator();
/* 342 */     int dist = Integer.MAX_VALUE;
/* 343 */     while (iterator.hasNext()) {
/* 344 */       BlockPos currDoor = iterator.next();
/* 345 */       Block checkBlock = this.world.getBlockState(currDoor).getBlock();
/* 346 */       if ((int)Math.sqrt(getDistanceSquared(x, y, z, currDoor.getX(), currDoor.getY(), currDoor.getZ())) < dist) {
/* 347 */         dist = (int)Math.sqrt(getDistanceSquared(x, y, z, currDoor.getX(), currDoor.getY(), currDoor.getZ()));
/* 348 */         targetDoor = getDoorFromCoords(checkBlock, currDoor.getX(), currDoor.getY(), currDoor.getZ());
/*     */       } 
/*     */     } 
/* 351 */     return targetDoor;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private VillageDoorInfo getDoorFromCoords(Block theDoorBlock, int x, int y, int z) {
/* 363 */     if (!(theDoorBlock instanceof BlockDoor)) {
/* 364 */       return null;
/*     */     }
/*     */     BlockPos lpos = new BlockPos(x, y, z);
/* 367 */     int l = ((BlockDoor)theDoorBlock).combineMetadata((IBlockAccess)this.world, lpos);
/*     */ 
/*     */ 
/*     */     
/* 371 */     if (l != 0 && l != 2) {
/*     */       
/* 373 */       int i1 = 0;
/*     */       int j1;
/* 375 */       for (j1 = -5; j1 < 0; j1++) {
/*     */         
/* 377 */         if (this.world.canBlockSeeSky(lpos.add(0, 0, j1)))
/*     */         {
/* 379 */           i1--;
/*     */         }
/*     */       } 
/*     */       
/* 383 */       for (j1 = 1; j1 <= 5; j1++) {
/*     */         
/* 385 */         if (this.world.canBlockSeeSky(lpos.add(0, 0, j1)))
/*     */         {
/* 387 */           i1++;
/*     */         }
/*     */       } 
/*     */       
/* 391 */       if (i1 != 0)
/*     */       {
/* 393 */         return new VillageDoorInfo(lpos, 0, (i1 > 0) ? -2 : 2, 0);
/*     */       }
/*     */     }
/*     */     else {
/*     */       
/* 398 */       int i1 = 0;
/*     */       int j1;
/* 400 */       for (j1 = -5; j1 < 0; j1++) {
/*     */         
/* 402 */         if (this.world.canBlockSeeSky(lpos.add(j1, 0, 0)))
/*     */         {
/* 404 */           i1--;
/*     */         }
/*     */       } 
/*     */       
/* 408 */       for (j1 = 1; j1 <= 5; j1++) {
/*     */         
/* 410 */         if (this.world.canBlockSeeSky(lpos.add(j1, 0, 0)))
/*     */         {
/* 412 */           i1++;
/*     */         }
/*     */       } 
/*     */       
/* 416 */       if (i1 != 0)
/*     */       {
/* 418 */         return new VillageDoorInfo(lpos, (i1 > 0) ? -2 : 2, 0, 0);
/*     */       }
/*     */     } 
/* 421 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getVillageRadius() {
/* 428 */     return this.radius;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public EntityLivingBase findNearestVillageAggressor(EntityLivingBase entity) {
/* 436 */     if (this.actualBounds == null) {
/* 437 */       return null;
/*     */     }
/*     */     
/* 440 */     if (this.lastAggressor != null && this.lastAggressor instanceof IMob && this.lastAggressor.isEntityAlive()) {
/* 441 */       return this.lastAggressor;
/*     */     }
/*     */     
/* 444 */     List entities = this.world.getEntitiesWithinAABB(EntityLivingBase.class, this.actualBounds);
/* 445 */     Iterator<Entity> iterator = entities.iterator();
/* 446 */     double d0 = Double.MAX_VALUE;
/* 447 */     EntityLivingBase target = null;
/* 448 */     while (iterator.hasNext()) {
/* 449 */       Entity curr = iterator.next();
				if (curr instanceof IMob) {
					double d1 = entity.getDistanceSqToEntity(curr);
					if (d1 < d0) {
					d0 = d1;
					target = (EntityLivingBase)curr;
					}
				} 
/*     */     } 
/* 456 */     return target;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public GuildHall lookForExistingHall(int profession) {
/* 467 */     Iterator<GuildHall> iterator = this.guildHallList.iterator();
/* 468 */     ArrayList<GuildHall> matchedHalls = new ArrayList<GuildHall>();
/*     */     
/* 470 */     while (iterator.hasNext()) {
/* 471 */       GuildHall guildHall = iterator.next();
/* 472 */       if (guildHall.getTypeNum() == profession) {
/* 473 */         matchedHalls.add(guildHall);
/*     */       }
/*     */     } 
/*     */     
/* 477 */     if (matchedHalls.size() > 0) {
/* 478 */       Random gen = new Random();
/* 479 */       int i = gen.nextInt(matchedHalls.size());
/* 480 */       return matchedHalls.get(i);
/*     */     } 
/* 482 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void checkHalls() {
/* 490 */     for (int i = 0; i < this.guildHallList.size(); i++) {
/* 491 */       GuildHall guildHall = this.guildHallList.get(i);
/* 492 */       guildHall.checkFrame();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void findHalls() {
/* 500 */     if (this.world == null) {
/*     */       return;
/*     */     }
/*     */     
/* 504 */     for (int i = 1; i <= 13; i++) {
/* 505 */       GuildHall adder = new GuildHall(this.world, this);
/*     */ 
/*     */       
/* 508 */       if (i == 8) {
/* 509 */         adder = new RanchGuildHall(this.world, this);
/*     */       }
/*     */       
/* 512 */       List itemFrames = this.world.getEntitiesWithinAABB(EntityItemFrame.class, this.villageBounds);
/* 513 */       adder.findCoords(i, itemFrames);
/* 514 */       if (adder.typeNum > 0 && !containsHall(adder) && adder.insideCoords.size() > 0) {
/* 515 */         if (adder instanceof RanchGuildHall) {
/* 516 */           ((RanchGuildHall)adder).findPastureCoords();
/*     */         }
/*     */         
/* 519 */         this.guildHallList.add(adder);
/* 520 */         this.unlockedHalls[adder.typeNum - 1] = true;
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean containsHall(GuildHall hall) {
/* 526 */     for (int i = 0; i < this.guildHallList.size(); i++) {
/* 527 */       if (hall.equals(this.guildHallList.get(i))) {
/* 528 */         return true;
/*     */       }
/*     */     } 
/* 531 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TileEntityChest searchHallsForItem(ItemStack item) {
/* 540 */     checkHalls();
/* 541 */     for (int i = 0; i < this.guildHallList.size(); i++) {
/* 542 */       GuildHall hall = this.guildHallList.get(i);
/* 543 */       hall.checkChests();
/* 544 */       for (int j = 0; j < hall.guildChests.size(); j++) {
/* 545 */         TileEntityChest chest = (TileEntityChest) hall.guildChests.get(j);
/* 546 */         if (AIHelper.chestContains(chest, item) >= 0) {
/* 547 */           return chest;
/*     */         }
/*     */       } 
/*     */     } 
/* 551 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void mergeVillage(HelpfulVillage otherVillage) {
/* 560 */     this.villageDoors.addAll(otherVillage.villageDoors);
/*     */     
/* 562 */     int dist = 0;
/* 563 */     initBounds();
/* 564 */     for (int i = 0; i < this.villageDoors.size(); i++) {
/* 565 */       BlockPos currDoor = this.villageDoors.get(i);
/* 566 */       if (currDoor.getX() < this.minX) {
/* 567 */         this.minX = currDoor.getX() - 5;
/* 568 */       } else if (currDoor.getX() > this.maxX) {
/* 569 */         this.maxX = currDoor.getX() + 5;
/*     */       } 
/*     */       
/* 572 */       if (currDoor.getY() < this.minY) {
/* 573 */         this.minY = currDoor.getY() - 5;
/* 574 */       } else if (currDoor.getY() > this.maxY) {
/* 575 */         this.maxY = currDoor.getY() + 5;
/*     */       } 
/*     */       
/* 578 */       if (currDoor.getZ() < this.minZ) {
/* 579 */         this.minZ = currDoor.getZ() - 5;
/* 580 */       } else if (currDoor.getZ() > this.maxZ) {
/* 581 */         this.maxZ = currDoor.getZ() + 5;
/*     */       } 
/* 583 */       dist = (int)Math.max(getDistanceSquaredToChunkCoordinates(currDoor.getX(), currDoor.getZ(), this.actualCenter.getX(), this.actualCenter.getZ()), dist);
/*     */     } 
/* 585 */     this.radius = Math.max(32, (int)Math.sqrt(dist) + 5);
/* 586 */     this.actualBounds = AxisAlignedBB.fromBounds(this.minX, this.minY, this.minZ, this.maxX, this.maxY, this.maxZ);
/* 587 */     this.actualCenter = getActualCenter();
/* 588 */     this.villageBounds = AxisAlignedBB.fromBounds((this.actualCenter.getX() - this.radius), (this.actualCenter.getY() - this.radius), (this.actualCenter.getZ() - this.radius), (this.actualCenter.getX() + this.radius), (this.actualCenter.getY() + this.radius), (this.actualCenter.getZ() + this.radius));
/*     */     
/* 590 */     this.craftQueue.mergeQueue(otherVillage.craftQueue);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void writeVillageDataToNBT(NBTTagCompound nbttagcompound) {
/* 600 */     nbttagcompound.setTag("Dimension", (NBTBase)new NBTTagInt(this.dimension));
/*     */ 
/*     */     
/* 603 */     int[] villageCoords = new int[3];
/* 604 */     villageCoords[0] = this.initialCenter.getX();
/* 605 */     villageCoords[1] = this.initialCenter.getY();
/* 606 */     villageCoords[2] = this.initialCenter.getZ();
/* 607 */     nbttagcompound.setTag("Center", (NBTBase)new NBTTagIntArray(villageCoords));
/*     */ 
/*     */ 
/*     */     
/* 611 */     nbttagcompound.setTag("Crafts", this.craftQueue.writeToNBT(new NBTTagList()));
/*     */ 
/*     */     
/* 614 */     if (this.pricesCalculated) {
/* 615 */       nbttagcompound.setTag("Economy", this.economy.writeToNBT(new NBTTagList()));
/*     */     }
/*     */ 
/*     */     
/* 619 */     NBTTagList tagList = new NBTTagList();
/* 620 */     for (ConstructionSite site : this.constructionSites) {
/* 621 */       NBTTagCompound compound = new NBTTagCompound();
/* 622 */       compound.setTag("Site", (NBTBase)site.writeToNBT(new NBTTagCompound()));
/* 623 */       tagList.appendTag((NBTBase)compound);
/*     */     } 
/* 625 */     nbttagcompound.setTag("Sites", (NBTBase)tagList);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void readVillageDataFromNBT(NBTTagCompound nbttagcompound1) {
/* 634 */     this.dimension = nbttagcompound1.getInteger("Dimension");
/*     */ 
/*     */     
/* 637 */     int[] village = nbttagcompound1.getIntArray("Center");
/* 638 */     this.initialCenter = new BlockPos(village[0], village[1], village[2]);
/*     */     
/* 640 */     init();
/*     */ 
/*     */     
/* 643 */     NBTTagList nbttaglist = nbttagcompound1.getTagList("Crafts", nbttagcompound1.getId());
/* 644 */     this.craftQueue.readFromNBT(nbttaglist);
/*     */ 
/*     */     
/* 647 */     if (nbttagcompound1.hasKey("Economy")) {
/* 648 */       this.priceCalcStarted = true;
/* 649 */       nbttaglist = nbttagcompound1.getTagList("Economy", nbttagcompound1.getId());
/* 650 */       this.economy.readFromNBT(nbttaglist);
/* 651 */       this.pricesCalculated = true;
/*     */     } 
/*     */ 
/*     */     
/* 655 */     nbttaglist = nbttagcompound1.getTagList("Sites", nbttagcompound1.getId());
/* 656 */     for (int i = 0; i < nbttaglist.tagCount(); i++) {
/* 657 */       NBTTagCompound compound = nbttaglist.getCompoundTagAt(i);
/* 658 */       ConstructionSite site = ConstructionSite.loadSiteFromNBT(compound.getCompoundTag("Site"), this.world);
/* 659 */       this.constructionSites.add(site);
/*     */     } 
/*     */   }
			
			public float getDistanceSquaredToChunkCoordinates (BlockPos c, BlockPos d) {
				return getDistanceSquared(c.getX(), c.getZ(), d.getX(), d.getZ());
			}
			
			public float getDistanceSquaredToChunkCoordinates (int px1, int pz1, int px2, int pz2) {
				float f = px1 - px2;
				float f2 = pz1 - pz2;
				return f * f + f2 * f2;
			}
			
			public float getDistanceSquared (int px1, int pz1, int px2, int pz2) {
				float f = px1 - px2;
				float f2 = pz1 - pz2;
				return f * f + f2 * f2;
			}
			
			public float getDistanceSquared (int px1, int py1, int pz1, int px2, int py2, int pz2) {
				float f = px1 - px2;
				float f2 = pz1 - pz2;
				float f3 = py1 - py2;
				return f * f + f2 * f2 + f3 * f3;
			}
			

 			}


/* Location:              D:\Users\Joseph\Downloads\helpfulvillagers-1.7.10-1.4.0b5.jar!\mods\helpfulvillagers\village\HelpfulVillage.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */