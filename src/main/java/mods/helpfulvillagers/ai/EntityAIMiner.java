/*     */ package mods.helpfulvillagers.ai;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import mods.helpfulvillagers.crafting.CraftItem;
/*     */ import mods.helpfulvillagers.entity.AbstractVillager;
/*     */ import mods.helpfulvillagers.entity.EntityMiner;
/*     */ import mods.helpfulvillagers.enums.EnumActivity;
/*     */ import mods.helpfulvillagers.util.AIHelper;
/*     */ import mods.helpfulvillagers.util.ResourceCluster;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.tileentity.TileEntityChest;
/*     */ import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.world.IBlockAccess;
/*     */ 
/*     */ import net.minecraftforge.oredict.OreDictionary;
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
/*     */ public class EntityAIMiner
/*     */   extends EntityAIWorker
/*     */ {
/*     */   private EntityMiner miner;
/*  34 */   private final int WAIT_TIME = 40;
/*     */   
/*     */   private int previousWait;
/*     */   
/*     */   private Block lastBlock;
/*     */   private Block lastAbove;
/*     */   
/*     */   public EntityAIMiner(EntityMiner miner) {
/*  42 */     super((AbstractVillager)miner);
/*  43 */     this.miner = miner;
/*  44 */     miner.shaftIndex = miner.nearestShaftCoord();
/*  45 */     this.previousWait = 0;
/*  46 */     this.lastBlock = null;
/*  47 */     this.lastAbove = null;
/*  48 */     this.lastAbove2 = null;
/*  49 */     this.surroundingCoords = new ArrayList();
/*  50 */     this.reset = true;
/*  51 */     this.skipResource = false;
/*  52 */     setMutexBits(1);
/*     */   }
/*     */   private Block lastAbove2; private ArrayList surroundingCoords;
/*     */   private boolean reset;
/*     */   private boolean skipResource;
/*     */   
/*     */   protected boolean idle() {
/*  59 */     this.villager.currentActivity = EnumActivity.IDLE;
/*     */ 
/*     */     
/*  62 */     if (!this.villager.worldObj.isRemote && this.villager.homeVillage == null) {
/*  63 */       System.out.println("No Home Village");
/*  64 */       return false;
/*     */     } 
/*     */     
/*  67 */     this.villager.checkGuildHall();
/*     */ 
/*     */     
/*  70 */     if (this.villager.homeGuildHall == null)
/*     */     {
/*  72 */       return false;
/*     */     }
/*     */ 
/*     */     
/*  76 */     if (this.villager.currentCraftItem != null && this.villager.currentCraftItem.getPriority() >= 1) {
/*     */       
/*  78 */       if ((this.readyToCraft || this.readyToSmelt) && !this.villager.nearHall()) {
/*  79 */         mReturn();
/*  80 */         return false;
/*     */       } 
/*     */       
/*  83 */       if ((this.readyToCraft || this.readyToSmelt) && this.villager.nearHall()) {
/*  84 */         this.villager.currentActivity = EnumActivity.CRAFT;
/*  85 */         return true;
/*     */       } 
/*     */       
/*  88 */       if (!this.craftCheck) {
/*  89 */         this.villager.currentActivity = EnumActivity.CRAFT;
/*  90 */         this.craftCheck = true;
/*  91 */         return true;
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/*  97 */     if (this.villager.inventory.isFull() || !this.villager.hasTool) {
/*     */       
/*  99 */       if (this.villager.nearHall()) {
/*     */         
/* 101 */         if (this.villager.currentCraftItem != null && !this.craftCheck) {
/* 102 */           this.villager.currentActivity = EnumActivity.CRAFT;
/* 103 */           this.craftCheck = true;
/* 104 */           return true;
/*     */         } 
/* 106 */         if (!this.villager.inventory.isEmpty() || !this.villager.hasTool) {
/* 107 */           this.villager.currentActivity = EnumActivity.STORE;
/* 108 */           this.craftCheck = false;
/* 109 */           return true;
/*     */         } 
/*     */         
/* 112 */         this.craftCheck = false;
/* 113 */         return false;
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 118 */       mReturn();
/* 119 */       this.craftCheck = false;
/* 120 */       return false;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 125 */     if (this.villager.worldObj.isDaytime()) {
/* 126 */       this.villager.currentActivity = EnumActivity.GATHER;
/* 127 */       this.craftCheck = false;
/* 128 */       return true;
/*     */     } 
/*     */ 
/*     */     
/* 132 */     if (!this.villager.nearHall()) {
/* 133 */       mReturn();
/* 134 */       this.craftCheck = false;
/* 135 */       return false;
/*     */     } 
/*     */ 
/*     */     
/* 139 */     if (this.villager.currentCraftItem != null && !this.craftCheck) {
/* 140 */       this.villager.currentActivity = EnumActivity.CRAFT;
/* 141 */       this.craftCheck = true;
/* 142 */       return true;
/*     */     } 
/* 144 */     if (!this.villager.inventory.isEmpty() || !this.villager.hasTool) {
/* 145 */       this.villager.currentActivity = EnumActivity.STORE;
/* 146 */       this.craftCheck = false;
/* 147 */       return true;
/*     */     } 
/*     */     
/* 150 */     this.craftCheck = false;
/* 151 */     return true;
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
/*     */ 
/*     */   
/*     */   protected boolean gather() {
/* 165 */     if (this.miner.topCoords == null) {
/*     */       
/* 167 */       findMine();
/*     */     }
/* 169 */     else if (this.miner.shaftCoords.isEmpty()) {
/*     */       
/* 171 */       buildStairs(this.miner.topCoords, this.miner.topDir);
/*     */     }
/* 173 */     else if (!this.skipResource) {
/* 174 */       if (this.miner.currentResource == null)
/*     */       {
/* 176 */         getNewResource();
/*     */       }
/*     */       
/* 179 */       if (this.miner.currentResource == null) {
/*     */         
/* 181 */         if (this.miner.returnPath.isEmpty()) {
/* 182 */           digSection(this.miner.shaftIndex, true);
/*     */         } else {
/* 184 */           digTunnel(true);
/*     */         }
/*     */       
/* 187 */       } else if (!this.miner.tunnelCoords.isEmpty()) {
/* 188 */         digTunnel(false);
/*     */       } else {
/* 190 */         mineResource();
/*     */       } 
/*     */     } else {
/*     */       
/* 194 */       if (this.miner.returnPath.isEmpty()) {
/* 195 */         digSection(this.miner.shaftIndex, true);
/*     */       } else {
/* 197 */         digTunnel(true);
/*     */       } 
/* 199 */       if ((int)this.miner.posY < (int)this.miner.lastTickPosY) {
/* 200 */         this.skipResource = false;
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 205 */     return idle();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean store() {
/* 215 */     if (this.miner.homeGuildHall == null) {
/* 216 */       return idle();
/*     */     }
/* 218 */     TileEntityChest chest = this.miner.homeGuildHall.getAvailableChest();
			  BlockPos chestPos = new BlockPos(chest.getPos());
/* 219 */     if (!this.miner.inventory.isEmpty() || !this.miner.hasTool) {
/* 220 */       if (chest != null) {
/* 221 */         this.miner.moveTo(new BlockPos(chestPos), this.speed);
/*     */       } else {
/* 223 */         this.miner.changeGuildHall = true;
/*     */       } 
/*     */       
/* 226 */       if (chest != null && AIHelper.findDistance((int)this.miner.posX, chestPos.getX()) <= 2 && AIHelper.findDistance((int)this.miner.posY, chestPos.getY()) <= 2 && AIHelper.findDistance((int)this.miner.posZ, chestPos.getZ()) <= 2) {
/*     */ 
/*     */         
/* 229 */         int solidIndex = this.miner.inventory.findSolidBlock(this.miner.excludeBlocks);
/* 230 */         ItemStack temp = null;
/* 231 */         if (solidIndex >= 0) {
/* 232 */           temp = this.miner.inventory.getStackInSlot(solidIndex);
/* 233 */           this.miner.inventory.setMainContents(solidIndex, null);
/*     */         } 
/*     */         
/*     */         try {
/* 237 */           this.miner.inventory.dumpInventory(chest);
/* 238 */         } catch (NullPointerException e) {
/* 239 */           chest.update();
/*     */         } 
/*     */ 
/*     */         
/* 243 */         if (solidIndex >= 0) {
/* 244 */           this.miner.inventory.addItem(temp);
/*     */         }
/*     */         
/* 247 */         if (!this.miner.hasTool) {
/* 248 */           Iterator<TileEntityChest> iterator = this.miner.homeGuildHall.guildChests.iterator();
/* 249 */           while (iterator.hasNext()) {
/* 250 */             chest = iterator.next();
/* 251 */             int index = AIHelper.chestContains(chest, (AbstractVillager)this.miner);
/* 252 */             if (index >= 0) {
/* 253 */               this.miner.inventory.swapEquipment(chest, index, 0);
/*     */             }
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 260 */     if (!this.miner.hasTool && this.miner.queuedTool == null) {
/* 261 */       int lowestPrice = Integer.MAX_VALUE;
/* 262 */       ItemStack lowestItem = null;
/* 263 */       for (int i = 0; i < (this.miner.getValidTools()).length; i++) {
/* 264 */         ItemStack item = this.miner.getValidTools()[i];
/* 265 */         int price = this.miner.homeVillage.economy.getPrice(item.getDisplayName());
/* 266 */         if (price < lowestPrice || lowestItem == null) {
/* 267 */           lowestPrice = price;
/* 268 */           lowestItem = item;
/*     */         } 
/*     */       } 
/* 271 */       this.miner.addCraftItem(new CraftItem(lowestItem, (AbstractVillager)this.miner));
/* 272 */       this.miner.queuedTool = lowestItem;
/* 273 */     } else if (this.miner.hasTool) {
/* 274 */       this.miner.queuedTool = null;
/*     */     } 
/*     */     
/* 277 */     return idle();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void mReturn() {
/* 285 */     if (this.miner.currentResource != null) {
/* 286 */       this.miner.currentResource = null;
/*     */     }
/*     */     
/* 289 */     this.miner.tunnelCoords.clear();
/*     */     
/* 291 */     if (this.miner.topCoords != null && this.miner.shaftIndex > 0) {
/* 292 */       this.miner.setLocationAndAngles(this.miner.topCoords.getX(), this.miner.topCoords.getY(), this.miner.topCoords.getZ(), 0.0F, 0.0F);
/* 293 */       this.miner.shaftIndex = 0;
/* 294 */       this.miner.dugSection = false;
/* 295 */       this.miner.digCoords.clear();
/*     */     } 
/*     */     
/* 298 */     this.miner.currentActivity = EnumActivity.RETURN;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void findMine() {
/* 308 */     if (this.target == null) {
/* 309 */       if (this.miner.lastResource == null) {
/* 310 */         this.target = AIHelper.getRandOutsideCoords((AbstractVillager)this.miner, 30);
/*     */       } else {
/* 312 */         this.target = AIHelper.getRandOutsideCoords((AbstractVillager)this.miner, 60);
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 318 */     if (this.target != null) {
/* 319 */       this.miner.moveTo(this.target, this.speed);
/*     */     }
/*     */ 
/*     */     
/* 323 */     if (!AIHelper.isInRangeOfAnyVillage(this.miner.posX, this.miner.posY, this.miner.posZ)) {
/* 324 */       BlockPos currCoords = new BlockPos((int)this.miner.posX, (int)this.miner.posY - 1, (int)this.miner.posZ);
/* 325 */       Block currBlock = this.miner.worldObj.getBlockState(currCoords).getBlock();
/* 326 */       if (currBlock.equals(Blocks.stone)) {
/* 327 */         this.miner.topCoords = currCoords;
/* 328 */         this.miner.topDir = this.miner.getDirection();
/* 329 */         this.miner.lastResource = new ResourceCluster(this.miner.worldObj, this.miner.topCoords);
/*     */       } else {
/*     */         
/* 332 */         int[] oreDictIDs = OreDictionary.getOreIDs(new ItemStack(currBlock));
/* 333 */         for (int j = 0; j < oreDictIDs.length; j++) {
/* 334 */           String name = OreDictionary.getOreName(oreDictIDs[j]);
/* 335 */           if (name.contains("ore")) {
/* 336 */             this.miner.topCoords = currCoords;
/* 337 */             this.miner.topDir = this.miner.getDirection();
/* 338 */             this.miner.lastResource = new ResourceCluster(this.miner.worldObj, this.miner.topCoords);
/*     */ 
/*     */             
/*     */             break;
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 347 */     if (Math.abs(this.miner.posX - this.target.getX()) <= 2.0D && Math.abs(this.miner.posZ - this.target.getZ()) <= 2.0D) {
/*     */       
/* 349 */       this.miner.lastResource = null;
/*     */ 
/*     */       
/* 352 */       this.surroundingCoords = this.miner.getSurroundingCoords();
/* 353 */       for (int i = 0; i < this.surroundingCoords.size(); i++) {
/* 354 */         BlockPos coord = (BlockPos) this.surroundingCoords.get(i);
/* 355 */         Block block = this.miner.worldObj.getBlockState(coord).getBlock();
/* 356 */         if (block.equals(Blocks.water) || block.equals(Blocks.lava)) {
/* 357 */           this.target = null;
/*     */           
/*     */           return;
/*     */         } 
/*     */       } 
/*     */       
/* 363 */       BlockPos topCoords = this.miner.getCoords();
/*     */       while (true) {
/* 365 */         Block topBlock = this.miner.worldObj.getBlockState(topCoords).getBlock();
/* 366 */         if (!topBlock.equals(Blocks.air)) {
/*     */           break;
/*     */         }
/* 369 */         topCoords.add(0, -1, 0);
/*     */       } 
/*     */       
/* 372 */       this.miner.topCoords = topCoords;
/* 373 */       this.miner.topDir = this.miner.getDirection();
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
/*     */   private void buildStairs(BlockPos coords, int direction) {
/* 385 */     Block currentBlock = this.miner.worldObj.getBlockState(coords).getBlock();
/*     */     
/* 387 */     if (coords.getY() <= 0 || currentBlock.equals(Blocks.bedrock) || currentBlock.equals(Blocks.lava) || currentBlock.equals(Blocks.water)) {
/*     */       return;
/*     */     }
/*     */     
/* 391 */     this.miner.shaftCoords.add(coords);
/*     */ 
/*     */ 
/*     */     
/* 395 */     BlockPos nextCoords = new BlockPos(coords.add(0, -1, 0));
/* 396 */     switch (direction) {
/*     */       case 0:
/* 398 */         nextCoords.add(0, 0, 1);
/*     */         break;
/*     */       
/*     */       case 1:
/* 402 */         nextCoords.add(-1, 0, 0);
/*     */         break;
/*     */       
/*     */       case 2:
/* 406 */         nextCoords.add(0, 0, -1);
/*     */         break;
/*     */       
/*     */       case 3:
/* 410 */         nextCoords.add(1, 0 , 0);
/*     */         break;
/*     */     } 
/*     */ 
/*     */     
/* 415 */     direction++;
/* 416 */     if (direction > 3) {
/* 417 */       direction = 0;
/*     */     }
/*     */     
/* 420 */     buildStairs(nextCoords, direction);
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
/*     */   
/*     */   private void digSection(int index, boolean down) {
/* 433 */     if (index >= this.miner.shaftCoords.size())
/*     */       return; 
/* 435 */     if (index < 0) {
/* 436 */       this.miner.shaftIndex = 0;
/*     */       return;
/*     */     } 
/* 439 */     boolean shouldSwing = false;
/* 440 */     BlockPos stairCoords = (BlockPos) this.miner.shaftCoords.get(index);
/*     */     
/* 442 */     this.miner.moveTo(stairCoords, this.speed);
/*     */     
/* 444 */     if (AIHelper.findDistance((int)this.miner.posX, stairCoords.getX()) <= 3 && AIHelper.findDistance((int)this.miner.posY, stairCoords.getY()) <= 3 && AIHelper.findDistance((int)this.miner.posZ, stairCoords.getZ()) <= 3) {
/* 445 */       if (!this.miner.dugSection) {
/*     */         
/* 447 */         for (int i = 3; i >= 1; i--) {
/* 448 */           BlockPos aboveCoords = new BlockPos(stairCoords.add(0, i, 0));
/* 449 */           Block currentBlock = this.miner.worldObj.getBlockState(aboveCoords).getBlock();
/* 450 */           if (currentBlock.equals(Blocks.bedrock) || currentBlock.equals(Blocks.water) || currentBlock.equals(Blocks.lava)) {
/*     */             return;
/*     */           }
/* 453 */           if (currentBlock.isSideSolid((IBlockAccess)this.miner.worldObj, aboveCoords, EnumFacing.UP)) {
/* 454 */             this.miner.digCoords.add(aboveCoords);
/*     */           }
/*     */         } 
/*     */         
/* 458 */         this.miner.dugSection = true;
/*     */       
/*     */       }
/* 461 */       else if (!this.miner.digCoords.isEmpty()) {
/* 462 */         BlockPos currentCoords = (BlockPos) this.miner.digCoords.get(0);
/* 463 */         Block currentBlock = this.miner.worldObj.getBlockState(currentCoords).getBlock();
/* 464 */         if (this.previousTime <= 0) {
/* 465 */           this.previousTime = this.miner.ticksExisted;
/* 466 */           if (this.miner.getCurrentItem() != null) {
/* 467 */             this.harvestTime = 60.0F / this.miner.getCurrentItem().getItem().getDigSpeed(this.miner.getCurrentItem(), currentBlock.getDefaultState());
/*     */           }
/* 469 */           shouldSwing = true;
/*     */         } else {
/* 471 */           this.currentTime = this.miner.ticksExisted;
/* 472 */           if ((this.currentTime - this.previousTime) >= this.harvestTime) {
/* 473 */             this.previousTime = 0;
/* 474 */             shouldSwing = false;
/* 475 */             this.miner.digCoords.remove(0);
/* 476 */             AIHelper.breakBlock(currentCoords, (AbstractVillager)this.miner);
/*     */           } else {
/* 478 */             shouldSwing = true;
/*     */           } 
/*     */         } 
/*     */       } else {
/*     */         
/* 483 */         Block stairBlock = this.miner.worldObj.getBlockState(stairCoords).getBlock();
/* 484 */         if (!stairBlock.isSideSolid((IBlockAccess)this.miner.worldObj, stairCoords, EnumFacing.UP) || stairBlock.equals(Blocks.gravel) || stairBlock.equals(Blocks.sand)) {
/* 485 */           int solidIndex = this.miner.inventory.findSolidBlock(this.miner.excludeBlocks);
/* 486 */           if (solidIndex >= 0) {
/* 487 */             Block newBlock = Block.getBlockFromItem(this.miner.inventory.getStackInSlot(solidIndex).getItem());
/* 488 */             if (!stairBlock.equals(Blocks.air) && !stairBlock.equals(Blocks.water) && !stairBlock.equals(Blocks.lava)) {
/* 489 */               //int metadata = this.miner.worldObj.getBlockState(stairCoords).getMetadata;
/* 490 */               ItemStack item = new ItemStack(stairBlock, 1);
/*     */               try {
/* 492 */                 this.miner.inventory.addItem(item);
/* 493 */               } catch (NullPointerException e) {}
/*     */             } 
/*     */ 
/*     */             
/* 497 */             this.miner.worldObj.setBlockState(stairCoords, newBlock.getDefaultState());
/* 498 */             this.miner.inventory.decrementSlot(solidIndex);
/*     */           } else {
/*     */             return;
/*     */           } 
/*     */         } 
/*     */ 
/*     */         
/* 505 */         if (this.miner.getNavigator().noPath())
/*     */         {
/* 507 */           BlockPos upCoords = new BlockPos(stairCoords.add(0, 2, 0));
/* 508 */           if (!this.miner.beingFollowed && upCoords.getY() < this.miner.topCoords.getY()) {
/* 509 */             ArrayList<BlockPos> adjacentCoords = AIHelper.getAdjacentCoords(upCoords);
/* 510 */             for (int i = 0; i < adjacentCoords.size(); i++) {
/* 511 */               BlockPos aCoords = adjacentCoords.get(i);
/* 512 */               Block aBlock = this.miner.worldObj.getBlockState(aCoords).getBlock();
/* 513 */               if (this.miner.isInMine() && !aBlock.isSideSolid((IBlockAccess)this.miner.worldObj, aCoords, EnumFacing.UP)) {
/* 514 */                 replaceBlock(adjacentCoords.get(i));
/*     */               }
/*     */             } 
/*     */           } 
/*     */ 
/*     */           
/* 520 */           if (down) {
/* 521 */             this.miner.shaftIndex++;
/*     */           } else {
/* 523 */             this.miner.shaftIndex--;
/*     */           } 
/* 525 */           this.miner.dugSection = false;
/*     */         }
/*     */       
/*     */       }
/*     */     
/* 530 */     } else if (AIHelper.findDistance((int)this.miner.posX, stairCoords.getX()) > 10 || AIHelper.findDistance((int)this.miner.posZ, stairCoords.getZ()) > 10) {
/*     */       
/* 532 */       this.miner.moveTo(stairCoords, this.speed);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     }
/*     */     else {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 545 */       this.miner.shaftIndex = this.miner.nearestShaftCoord();
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 550 */     if (shouldSwing) {
/* 551 */       this.miner.swingItem();
/*     */     }
/* 553 */     this.miner.swingingPickaxe = shouldSwing;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean replaceBlock(BlockPos coords) {
/* 564 */     for (int i = 0; i < 4; i++) {
/* 565 */       BlockPos checkCoords = (BlockPos) this.miner.shaftCoords.get(i);
/* 566 */       if (checkCoords.getX() == coords.getX() && checkCoords.getZ() == coords.getZ()) {
/* 567 */         return false;
/*     */       }
/*     */     } 
/*     */     
/* 571 */     Block block = this.miner.worldObj.getBlockState(coords).getBlock();
/*     */     
/* 573 */     int solidIndex = this.miner.inventory.findSolidBlock(this.miner.excludeBlocks);
/* 574 */     if (solidIndex >= 0) {
/* 575 */       Block newBlock = Block.getBlockFromItem(this.miner.inventory.getStackInSlot(solidIndex).getItem());
/* 576 */       if (!block.equals(Blocks.air) && !block.equals(Blocks.water) && !block.equals(Blocks.lava)) {
/* 577 */         AIHelper.breakBlock(coords, (AbstractVillager)this.miner);
/*     */       }
/* 579 */       this.miner.worldObj.setBlockState(coords, newBlock.getDefaultState());
/* 580 */       this.miner.inventory.decrementSlot(solidIndex);
/*     */     } 
/*     */ 
/*     */     
/* 584 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void getNewResource() {
/* 593 */     ArrayList<BlockPos> boxCoords = this.miner.getValidCoords();
/* 594 */     double closestDist = Double.MAX_VALUE;
/* 595 */     ResourceCluster closestValidCluster = null;
/*     */     
/* 597 */     for (int i = 0; i < boxCoords.size(); i++) {
/* 598 */       BlockPos currentCoords = boxCoords.get(i);
/* 599 */       int dist = (int)currentCoords.distanceSq(this.miner.getCoords().getX(), this.miner.getCoords().getY(), this.miner.getCoords().getZ());
/* 600 */       if (dist < closestDist) {
/* 601 */         closestValidCluster = new ResourceCluster(this.miner.worldObj, currentCoords);
/* 602 */         closestDist = dist;
/*     */       } 
/*     */     } 
/*     */     
/* 606 */     if (closestValidCluster != null) {
/* 607 */       this.miner.currentResource = closestValidCluster;
/* 608 */       this.miner.currentResource.buildCluster();
/* 609 */       buildTunnel();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void buildTunnel() {
/* 618 */     BlockPos destCoords = this.miner.currentResource.coords;
/* 619 */     BlockPos currentCoords = new BlockPos((int)this.miner.posX, destCoords.getY(), (int)this.miner.posZ);
/*     */ 
/*     */     
/*     */     while (true) {
/* 623 */       this.miner.tunnelCoords.add(new BlockPos(currentCoords));
/* 624 */       if (currentCoords.getX() < destCoords.getX()) {
/* 625 */         currentCoords.add(1, 0, 0); continue;
/* 626 */       }  if (currentCoords.getX() > destCoords.getX()) {
/* 627 */         currentCoords.add(-1, 0, 0);
/*     */         
/*     */         continue;
/*     */       } 
/*     */       
/*     */       break;
/*     */     } 
/*     */     while (true) {
/* 635 */       this.miner.tunnelCoords.add(new BlockPos(currentCoords));
/* 636 */       if (currentCoords.getZ() < destCoords.getZ()) {
/* 637 */         currentCoords.add(0, 0, 1); continue;
/* 638 */       }  if (currentCoords.getZ() > destCoords.getZ()) {
/* 639 */         currentCoords.add(0, 0, -1);
/*     */         
/*     */         continue;
/*     */       } 
/*     */       break;
/*     */     } 
/* 645 */     this.miner.tunnelCoords.remove(this.miner.tunnelCoords.size() - 1);
/* 646 */     this.miner.shaftIndex--;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void digTunnel(boolean returning) {
/*     */     BlockPos currentCoords;
/* 656 */     boolean shouldSwing = false;
/*     */ 
/*     */     
/* 659 */     if (returning) {
/* 660 */       currentCoords = (BlockPos) this.miner.returnPath.get(this.miner.returnPath.size() - 1);
/*     */     } else {
/* 662 */       currentCoords = (BlockPos) this.miner.tunnelCoords.get(0);
/*     */     } 
/*     */     
/* 665 */     ArrayList<BlockPos> adjacent = AIHelper.getAdjacentCoords(currentCoords);
/* 666 */     for (int i = 0; i < adjacent.size(); i++) {
/* 667 */       BlockPos check = adjacent.get(i);
/* 668 */       Block checkBlock = this.miner.worldObj.getBlockState(check).getBlock();
/* 669 */       if (checkBlock.equals(Blocks.lava)) {
/* 670 */         this.miner.tunnelCoords.clear();
/* 671 */         this.skipResource = true;
/* 672 */         this.miner.currentResource = null;
/*     */         
/*     */         return;
/*     */       } 
/*     */     } 
/* 677 */     if (this.lastBlock != null && (this.lastBlock.equals(Blocks.sand) || this.lastBlock.equals(Blocks.gravel))) {
/* 678 */       if (this.previousWait <= 0) {
/* 679 */         this.previousWait = this.miner.ticksExisted;
/*     */         return;
/*     */       } 
/* 682 */       this.currentTime = this.miner.ticksExisted;
/* 683 */       if (this.currentTime - this.previousWait >= 40) {
/* 684 */         this.previousWait = 0;
/* 685 */         this.lastBlock = null;
/*     */       } else {
/* 687 */         this.miner.swingItem();
/*     */         
/*     */         return;
/*     */       } 
/*     */     } 
/*     */     
/* 693 */     if (this.lastAbove != null && (this.lastAbove.equals(Blocks.sand) || this.lastAbove.equals(Blocks.gravel))) {
/* 694 */       if (this.previousWait <= 0) {
/* 695 */         this.previousWait = this.miner.ticksExisted;
/*     */         return;
/*     */       } 
/* 698 */       this.currentTime = this.miner.ticksExisted;
/* 699 */       if (this.currentTime - this.previousWait >= 40) {
/* 700 */         this.previousWait = 0;
/* 701 */         this.lastAbove = null;
/*     */       } else {
/* 703 */         this.miner.swingItem();
/*     */         
/*     */         return;
/*     */       } 
/*     */     } 
/*     */     
/* 709 */     if (this.lastAbove2 != null && (this.lastAbove2.equals(Blocks.sand) || this.lastAbove2.equals(Blocks.gravel))) {
/* 710 */       if (this.previousWait <= 0) {
/* 711 */         this.previousWait = this.miner.ticksExisted;
/*     */         return;
/*     */       } 
/* 714 */       this.currentTime = this.miner.ticksExisted;
/* 715 */       if (this.currentTime - this.previousWait >= 40) {
/* 716 */         this.previousWait = 0;
/* 717 */         this.lastAbove2 = null;
/*     */       } else {
/* 719 */         this.miner.swingItem();
/*     */         
/*     */         return;
/*     */       } 
/*     */     } 
/*     */     
/* 725 */     BlockPos aboveCoords = new BlockPos(currentCoords.add(0, 1, 0));
/* 726 */     BlockPos aboveCoords2 = new BlockPos(currentCoords.add(0, 2, 0));
/* 727 */     BlockPos belowCoords = new BlockPos(currentCoords.add(0, -1, 0));
/* 728 */     Block currentBlock = this.miner.worldObj.getBlockState(currentCoords).getBlock();
/* 729 */     Block aboveBlock = this.miner.worldObj.getBlockState(aboveCoords).getBlock();
/* 730 */     Block aboveBlock2 = this.miner.worldObj.getBlockState(aboveCoords2).getBlock();
/* 731 */     Block belowBlock = this.miner.worldObj.getBlockState(belowCoords).getBlock();
/*     */     
/* 733 */     this.miner.moveTo(currentCoords, this.speed);
/* 734 */     if (AIHelper.findDistance((int)this.miner.posX, currentCoords.getX()) <= 3 && AIHelper.findDistance((int)this.miner.posY, currentCoords.getY()) <= 3 && AIHelper.findDistance((int)this.miner.posZ, currentCoords.getZ()) <= 3) {
/* 735 */       this.miner.getNavigator().clearPathEntity();
/*     */ 
/*     */       
/* 738 */       if (!currentBlock.isAir(this.miner.worldObj, currentCoords) && !currentBlock.equals(Blocks.water) && !currentBlock.equals(Blocks.lava)) {
/*     */         
/* 740 */         if (this.previousTime <= 0) {
/* 741 */           this.previousTime = this.miner.ticksExisted;
/* 742 */           if (this.miner.getCurrentItem() != null) {
/* 743 */             this.harvestTime = 60.0F / this.miner.getCurrentItem().getItem().getDigSpeed(this.miner.getCurrentItem(), currentBlock.getDefaultState());
/*     */           }
/* 745 */           shouldSwing = true;
/*     */         } else {
/* 747 */           this.currentTime = this.miner.ticksExisted;
/* 748 */           if ((this.currentTime - this.previousTime) >= this.harvestTime) {
/* 749 */             this.previousTime = 0;
/* 750 */             shouldSwing = false;
/* 751 */             AIHelper.breakBlock(currentCoords, (AbstractVillager)this.miner);
/* 752 */             this.lastBlock = currentBlock;
/*     */           } else {
/* 754 */             shouldSwing = true;
/*     */           }
/*     */         
/*     */         }
/*     */       
/* 759 */       } else if (!aboveBlock.equals(Blocks.air) && !aboveBlock.equals(Blocks.water) && !aboveBlock.equals(Blocks.lava)) {
/*     */         
/* 761 */         if (this.previousTime <= 0) {
/* 762 */           this.previousTime = this.miner.ticksExisted;
/* 763 */           if (this.miner.getCurrentItem() != null) {
/* 764 */             this.harvestTime = 60.0F / this.miner.getCurrentItem().getItem().getDigSpeed(this.miner.getCurrentItem(), aboveBlock.getDefaultState());
/*     */           }
/* 766 */           shouldSwing = true;
/*     */         } else {
/* 768 */           this.currentTime = this.miner.ticksExisted;
/* 769 */           if ((this.currentTime - this.previousTime) >= this.harvestTime) {
/* 770 */             this.previousTime = 0;
/* 771 */             shouldSwing = false;
/* 772 */             AIHelper.breakBlock(aboveCoords, (AbstractVillager)this.miner);
/* 773 */             this.lastAbove = aboveBlock;
/*     */           } else {
/* 775 */             shouldSwing = true;
/*     */           }
/*     */         
/*     */         }
/*     */       
/* 780 */       } else if (!aboveBlock2.equals(Blocks.air) && !aboveBlock2.equals(Blocks.water) && !aboveBlock2.equals(Blocks.lava)) {
/*     */         
/* 782 */         if (this.previousTime <= 0) {
/* 783 */           this.previousTime = this.miner.ticksExisted;
/* 784 */           if (this.miner.getCurrentItem() != null) {
/* 785 */             this.harvestTime = 60.0F / this.miner.getCurrentItem().getItem().getDigSpeed(this.miner.getCurrentItem(), aboveBlock2.getDefaultState());
/*     */           }
/* 787 */           shouldSwing = true;
/*     */         } else {
/* 789 */           this.currentTime = this.miner.ticksExisted;
/* 790 */           if ((this.currentTime - this.previousTime) >= this.harvestTime) {
/* 791 */             this.previousTime = 0;
/* 792 */             shouldSwing = false;
/* 793 */             AIHelper.breakBlock(aboveCoords2, (AbstractVillager)this.miner);
/* 794 */             this.lastAbove2 = aboveBlock2;
/*     */           } else {
/* 796 */             shouldSwing = true;
/*     */           }
/*     */         
/*     */         }
/*     */       
/* 801 */       } else if (!belowBlock.isSideSolid((IBlockAccess)this.miner.worldObj, belowCoords, EnumFacing.UP) || belowBlock.equals(Blocks.gravel) || belowBlock.equals(Blocks.sand)) {
/* 802 */         boolean replaced = replaceBlock(belowCoords);
/* 803 */         if (!replaced) {
/*     */           try {
/* 805 */             if (returning) {
/* 806 */               this.miner.returnPath.remove(this.miner.returnPath.size() - 1);
/*     */             } else {
/* 808 */               this.miner.returnPath.add(currentCoords);
/* 809 */               this.miner.tunnelCoords.remove(0);
/*     */             } 
/* 811 */           } catch (Exception e) {}
/*     */ 
/*     */ 
/*     */         
/*     */         }
/*     */ 
/*     */ 
/*     */       
/*     */       }
/* 820 */       else if (returning) {
/* 821 */         this.miner.returnPath.remove(this.miner.returnPath.size() - 1);
/*     */       } else {
/* 823 */         this.miner.returnPath.add(currentCoords);
/* 824 */         this.miner.tunnelCoords.remove(0);
/*     */       }
/*     */     
/* 827 */     } else if (AIHelper.findDistance((int)this.miner.posX, currentCoords.getX()) > 10 || AIHelper.findDistance((int)this.miner.posZ, currentCoords.getZ()) > 10) {
/* 828 */       this.miner.moveTo(currentCoords, this.speed);
/*     */     } 
/*     */ 
/*     */     
/* 832 */     if (shouldSwing) {
/* 833 */       this.miner.swingItem();
/*     */     }
/* 835 */     this.miner.swingingPickaxe = shouldSwing;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void mineResource() {
/* 843 */     boolean shouldSwing = false;
/* 844 */     BlockPos currentCoords = this.miner.currentResource.coords;
/*     */     
/* 846 */     this.miner.moveTo(currentCoords, this.speed);
/* 847 */     if (AIHelper.findDistance((int)this.miner.posX, currentCoords.getX()) <= 3 && AIHelper.findDistance((int)this.miner.posY, currentCoords.getY()) <= 3 && AIHelper.findDistance((int)this.miner.posZ, currentCoords.getZ()) <= 3) {
/* 848 */       shouldSwing = true;
/* 849 */       if (this.previousTime <= 0) {
/* 850 */         this.previousTime = this.miner.ticksExisted;
/* 851 */         this.harvestTime = 60.0F / this.miner.getCurrentItem().getItem().getDigSpeed(this.miner.getCurrentItem(), this.miner.currentResource.startBlock.getDefaultState());
/*     */       } 
/*     */       
/* 854 */       if (this.previousTime > 0) {
/* 855 */         this.currentTime = this.miner.ticksExisted;
/* 856 */         if (!this.miner.currentResource.blockCluster.isEmpty()) {
/* 857 */           if ((this.currentTime - this.previousTime) >= this.harvestTime) {
/* 858 */             this.previousTime = this.currentTime;
/* 859 */             currentCoords = (BlockPos) this.miner.currentResource.blockCluster.get(0);
/* 860 */             Block currentBlock = this.miner.worldObj.getBlockState(currentCoords).getBlock();
/* 861 */             if (Block.getIdFromBlock(currentBlock) == Block.getIdFromBlock(this.miner.currentResource.startBlock)) {
/* 862 */               AIHelper.breakBlock(currentCoords, (AbstractVillager)this.miner);
/*     */             }
/* 864 */             this.miner.currentResource.blockCluster.remove(0);
/*     */ 
/*     */             
/* 867 */             if (currentCoords.getY() == (int)this.miner.posY - 1) {
/* 868 */               replaceBlock(currentCoords);
/*     */             }
/*     */           } 
/*     */         } else {
/*     */           
/* 873 */           this.miner.currentResource = null;
/* 874 */           this.previousTime = 0;
/* 875 */           this.currentTime = 0;
/*     */         }
/*     */       
/*     */       } 
/* 879 */     } else if (AIHelper.findDistance((int)this.miner.posX, currentCoords.getX()) > 10 || AIHelper.findDistance((int)this.miner.posZ, currentCoords.getZ()) > 10) {
/* 880 */       this.miner.moveTo(currentCoords, this.speed);
/* 881 */       shouldSwing = false;
/*     */     } 
/*     */ 
/*     */     
/* 885 */     if (shouldSwing) {
/* 886 */       this.miner.swingItem();
/*     */     }
/* 888 */     this.miner.swingingPickaxe = shouldSwing;
/*     */   }
/*     */ }


/* Location:              D:\Users\Joseph\Downloads\helpfulvillagers-1.7.10-1.4.0b5.jar!\mods\helpfulvillagers\ai\EntityAIMiner.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */