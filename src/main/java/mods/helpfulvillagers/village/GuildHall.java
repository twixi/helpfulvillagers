/*     */ package mods.helpfulvillagers.village;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.item.EntityItemFrame;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.tileentity.TileEntityChest;
/*     */ import net.minecraft.tileentity.TileEntityFurnace;
/*     */ import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.world.World;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class GuildHall
/*     */ {
/*     */   private HelpfulVillage village;
/*     */   public World worldObj;
/*     */   public EntityItemFrame itemFrame;
/*     */   public BlockPos doorCoords;
/*     */   public BlockPos entranceCoords;
/*  57 */   public ArrayList<BlockPos> insideCoords = new ArrayList<BlockPos>();
/*     */   public int typeNum;
/*  59 */   public ArrayList guildChests = new ArrayList();
/*  60 */   public ArrayList guildFurnaces = new ArrayList();
/*     */   
/*     */   public GuildHall() {}
/*     */   
/*     */   public GuildHall(World world, HelpfulVillage village) {
/*  65 */     this.worldObj = world;
/*  66 */     this.village = village;
/*  67 */     this.itemFrame = null;
/*  68 */     this.doorCoords = null;
/*  69 */     this.entranceCoords = null;
/*  70 */     this.typeNum = -1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void findCoords(int profession, List itemFrames) {
/*  81 */     Iterator<Entity> iterator = itemFrames.iterator();
/*     */     
/*  83 */     while (iterator.hasNext()) {
/*  84 */       Entity entity = iterator.next();
/*  85 */       if (entity instanceof EntityItemFrame) {
/*  86 */         EntityItemFrame itemFrame = (EntityItemFrame)entity;
/*  87 */         if (itemFrame.getDisplayedItem() != null && 
/*  88 */           matchesProfession(itemFrame, profession) && 
/*  89 */           isNextToDoor((Entity)itemFrame)) {
/*  90 */           this.itemFrame = itemFrame;
/*  91 */           this.typeNum = profession;
/*     */           try {
/*  93 */             fillInsideCoords();
/*  94 */             findEntranceCoords(); break;
/*  95 */           } catch (StackOverflowError e) {
/*  96 */             this.insideCoords.clear();
/*     */           } 
/*     */           return;
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean matchesProfession(EntityItemFrame itemFrame, int profession) {
/* 115 */     ItemStack itemStack = itemFrame.getDisplayedItem();
/*     */     
/* 117 */     if (itemStack == null) {
/* 118 */       return false;
/*     */     }
/*     */     
/* 121 */     switch (profession) {
/*     */       case 1:
/* 123 */         return itemStack.getItem() instanceof net.minecraft.item.ItemAxe;
/*     */       case 2:
/* 125 */         return itemStack.getItem() instanceof net.minecraft.item.ItemPickaxe;
/*     */       case 3:
/* 127 */         return itemStack.getItem() instanceof net.minecraft.item.ItemHoe;
/*     */       case 4:
/* 129 */         return itemStack.getItem() instanceof net.minecraft.item.ItemSword;
/*     */       case 5:
/* 131 */         return itemStack.getItem() instanceof net.minecraft.item.ItemBow;
/*     */       case 6:
/* 133 */         return itemStack.getItem().equals(Items.emerald);
/*     */       case 7:
/* 135 */         return itemStack.getItem() instanceof net.minecraft.item.ItemFishingRod;
/*     */       case 8:
/* 137 */         return itemStack.getItem() instanceof net.minecraft.item.ItemLead;
/*     */       case 9:
/* 139 */         return itemStack.getItem() instanceof net.minecraft.item.ItemSpade;
/*     */     } 
/* 141 */     return false;
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
/*     */   private boolean isNextToDoor(Entity entity) {
/* 153 */     int posX = (int)entity.posX;
/* 154 */     int posY = (int)entity.posY;
/* 155 */     int posZ = (int)entity.posZ;
/* 156 */     boolean doorFlag = false;
/*     */     
/* 158 */     this.doorCoords = new BlockPos(posX, posY, posZ);
/*     */     
/* 160 */     for (int i = 0; i < 3; i++) {
/* 161 */       for (int j = 0; j < 3; j++) {
/* 162 */         for (int k = 0; k < 3; k++) {
	
/*     */           
/* 164 */           Block posBlock = this.worldObj.getBlockState(doorCoords.add(i, j, k)).getBlock();
/* 165 */           Block posBlock2 = this.worldObj.getBlockState(doorCoords.add(i, j, -k)).getBlock();
/* 166 */           Block posBlock3 = this.worldObj.getBlockState(doorCoords.add(i, -j, -k)).getBlock();
/* 167 */           Block posBlock4 = this.worldObj.getBlockState(doorCoords.add(i, -j, k)).getBlock();
/* 168 */           Block posBlock5 = this.worldObj.getBlockState(doorCoords.add(-i, j, k)).getBlock();
/* 169 */           Block posBlock6 = this.worldObj.getBlockState(doorCoords.add(-i, j, -k)).getBlock();
/* 170 */           Block posBlock7 = this.worldObj.getBlockState(doorCoords.add(-i, -j, k)).getBlock();
/* 171 */           Block posBlock8 = this.worldObj.getBlockState(doorCoords.add(-i, -j, -k)).getBlock();
/*     */           
/* 173 */           if (posBlock instanceof net.minecraft.block.BlockDoor && posBlock != Blocks.iron_door) {
/* 174 */             this.doorCoords.add(i, j, k);
/* 175 */             doorFlag = true;
/* 176 */           } else if (posBlock2 instanceof net.minecraft.block.BlockDoor && posBlock2 != Blocks.iron_door) {
/* 177 */             this.doorCoords.add(i, j, -k);
/* 178 */             doorFlag = true;
/* 179 */           } else if (posBlock3 instanceof net.minecraft.block.BlockDoor && posBlock3 != Blocks.iron_door) {
/* 180 */             this.doorCoords.add(i, -j, -k);
/* 181 */             doorFlag = true;
/* 182 */           } else if (posBlock4 instanceof net.minecraft.block.BlockDoor && posBlock4 != Blocks.iron_door) {
/* 183 */             this.doorCoords.add(i, -j, k);
/* 184 */             doorFlag = true;
/* 185 */           } else if (posBlock5 instanceof net.minecraft.block.BlockDoor && posBlock5 != Blocks.iron_door) {
/* 186 */             this.doorCoords.add(-i, j, k);
/* 187 */             doorFlag = true;
/* 188 */           } else if (posBlock6 instanceof net.minecraft.block.BlockDoor && posBlock6 != Blocks.iron_door) {
/* 189 */             this.doorCoords.add(-i, j, -k);
/* 190 */             doorFlag = true;
/* 191 */           } else if (posBlock7 instanceof net.minecraft.block.BlockDoor && posBlock7 != Blocks.iron_door) {
/* 192 */             this.doorCoords.add(-i, -j, k);
/* 193 */             doorFlag = true;
/* 194 */           } else if (posBlock8 instanceof net.minecraft.block.BlockDoor && posBlock8 != Blocks.iron_door) {
/* 195 */             this.doorCoords.add(-i, -j, -k);
/* 196 */             doorFlag = true;
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 201 */     if (doorFlag) {
/* 202 */       if (entity.worldObj.getBlockState(this.doorCoords.add(0, -1, 0)).getBlock() == Blocks.oak_door) {
/* 203 */         this.doorCoords = doorCoords.add(0, -1, 0);
/*     */       }
/*     */     } else {
/* 206 */       this.doorCoords = null;
/*     */     } 
/* 208 */     return doorFlag;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void fillInsideCoords() {
/*     */     BlockPos entranceCoords;
			BlockPos startCoords;
/* 220 */     this.insideCoords.add(this.doorCoords);
/*     */     
/* 222 */     switch (this.itemFrame.facingDirection) {
/*     */       case SOUTH:
/* 224 */         startCoords = new BlockPos(this.doorCoords.add(0, 0, -1));
/* 225 */         entranceCoords = new BlockPos(this.doorCoords.add(0, 0, 1));
/* 226 */         this.insideCoords.add(entranceCoords);
/* 227 */         checkZDirection(startCoords, -1);
/*     */         return;
/*     */       case WEST:
/* 230 */         startCoords = new BlockPos(this.doorCoords.add(1, 0, 0));
/* 231 */         entranceCoords = new BlockPos(this.doorCoords.add(-1, 0, 0));
/* 232 */         this.insideCoords.add(entranceCoords);
/* 233 */         checkXDirection(startCoords, 1);
/*     */         return;
/*     */       case NORTH:
/* 236 */         startCoords = new BlockPos(this.doorCoords.add(0, 0, 1));
/* 237 */         entranceCoords = new BlockPos(this.doorCoords.add(0, 0, -1));
/* 238 */         this.insideCoords.add(entranceCoords);
/* 239 */         checkZDirection(startCoords, 1);
/*     */         return;
/*     */       case EAST:
/* 242 */         startCoords = new BlockPos(this.doorCoords.add(-1, 0, 0));
/* 243 */         entranceCoords = new BlockPos(this.doorCoords.add(1, 0, 0));
/* 244 */         this.insideCoords.add(entranceCoords);
/* 245 */         checkXDirection(startCoords, -1);
/*     */         return;
/*     */     } 
/* 248 */     startCoords = null;
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
/*     */   private void checkXDirection(BlockPos currentCoords, int direction) {
/* 262 */     boolean continueFlag = false;
/*     */     
/* 264 */     if (!this.insideCoords.contains(currentCoords) && 
/* 265 */       isInside(currentCoords)) {
/* 266 */       if (direction < 0) {
/* 267 */         if ((!this.worldObj.isSideSolid(currentCoords, EnumFacing.WEST) && this.worldObj
/* 268 */           .getBlockState(currentCoords).getBlock() != Blocks.glass && this.worldObj
/* 269 */           .getBlockState(currentCoords).getBlock() != Blocks.glass_pane && this.worldObj
/* 270 */           .getBlockState(currentCoords).getBlock() != Blocks.stained_glass && this.worldObj
/* 271 */           .getBlockState(currentCoords).getBlock() != Blocks.stained_glass_pane) || this.worldObj
/* 272 */         	.getBlockState(currentCoords).getBlock() instanceof net.minecraft.block.BlockWorkbench || this.worldObj
/* 273 */           .getBlockState(currentCoords).getBlock() instanceof net.minecraft.block.BlockFurnace) {
/* 274 */           continueFlag = true;
/*     */         }
/*     */       }
/* 277 */       else if ((!this.worldObj.isSideSolid(currentCoords, EnumFacing.EAST) && this.worldObj
/* 278 */         .getBlockState(currentCoords).getBlock() != Blocks.glass && this.worldObj
/* 279 */         .getBlockState(currentCoords).getBlock() != Blocks.glass_pane && this.worldObj
/* 280 */         .getBlockState(currentCoords).getBlock() != Blocks.stained_glass && this.worldObj
/* 281 */         .getBlockState(currentCoords).getBlock() != Blocks.stained_glass_pane) || this.worldObj
/* 282 */       	.getBlockState(currentCoords).getBlock() instanceof net.minecraft.block.BlockWorkbench || this.worldObj
/* 283 */         .getBlockState(currentCoords).getBlock() instanceof net.minecraft.block.BlockFurnace) {
/* 284 */         continueFlag = true;
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 290 */     this.insideCoords.add(currentCoords);
/* 291 */     if (continueFlag) {
/*     */       
/* 293 */       BlockPos nextXCoords = new BlockPos(currentCoords.add(direction, 0, 0));
/* 294 */       BlockPos negYCoords = new BlockPos(currentCoords.add(0, -1, 0));
/* 295 */       BlockPos posYCoords = new BlockPos(currentCoords.add(0, 1, 0));
/* 296 */       BlockPos negZCoords = new BlockPos(currentCoords.add(0, 0, -1));
/* 297 */       BlockPos posZCoords = new BlockPos(currentCoords.add(0, 0, 1));
/* 298 */       checkXDirection(nextXCoords, direction);
/* 299 */       checkYDirection(negYCoords, -1);
/* 300 */       checkYDirection(posYCoords, 1);
/* 301 */       checkZDirection(negZCoords, -1);
/* 302 */       checkZDirection(posZCoords, 1);
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
/*     */   
/*     */   private void checkYDirection(BlockPos currentCoords, int direction) {
/* 315 */     boolean continueFlag = false;
/*     */     
/* 317 */     if (!this.insideCoords.contains(currentCoords) && 
/* 318 */       isInside(currentCoords)) {
/* 319 */       if (direction < 0) {
/* 320 */         if ((!this.worldObj.isSideSolid(currentCoords, EnumFacing.UP) && this.worldObj
/* 321 */           .getBlockState(currentCoords).getBlock() != Blocks.glass && this.worldObj
/* 322 */           .getBlockState(currentCoords).getBlock() != Blocks.glass_pane && this.worldObj
/* 323 */           .getBlockState(currentCoords).getBlock() != Blocks.stained_glass && this.worldObj
/* 324 */           .getBlockState(currentCoords).getBlock() != Blocks.stained_glass_pane) || this.worldObj
/* 325 */         	.getBlockState(currentCoords).getBlock() instanceof net.minecraft.block.BlockWorkbench || this.worldObj
/* 326 */           .getBlockState(currentCoords).getBlock() instanceof net.minecraft.block.BlockFurnace) {
/* 327 */           continueFlag = true;
/*     */         }
/*     */       }
/* 330 */       else if ((!this.worldObj.isSideSolid(currentCoords, EnumFacing.DOWN) && this.worldObj
/* 331 */         .getBlockState(currentCoords).getBlock() != Blocks.glass && this.worldObj
/* 332 */         .getBlockState(currentCoords).getBlock() != Blocks.glass_pane && this.worldObj
/* 333 */         .getBlockState(currentCoords).getBlock() != Blocks.stained_glass && this.worldObj
/* 334 */         .getBlockState(currentCoords).getBlock() != Blocks.stained_glass_pane) || this.worldObj
/* 335 */       	.getBlockState(currentCoords).getBlock() instanceof net.minecraft.block.BlockWorkbench || this.worldObj
/* 336 */         .getBlockState(currentCoords).getBlock() instanceof net.minecraft.block.BlockFurnace) {
/* 337 */         continueFlag = true;
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 343 */     this.insideCoords.add(currentCoords);
/* 344 */     if (continueFlag) {
/*     */       
/* 346 */       BlockPos nextYCoords = new BlockPos(currentCoords.add(0, direction, 0));
/* 347 */       BlockPos negXCoords = new BlockPos(currentCoords.add(-1, 0, 0));
/* 348 */       BlockPos posXCoords = new BlockPos(currentCoords.add(1, 0, 0));
/* 349 */       BlockPos negZCoords = new BlockPos(currentCoords.add(0, 0, -1));
/* 350 */       BlockPos posZCoords = new BlockPos(currentCoords.add(0, 0, 1));
/* 351 */       checkYDirection(nextYCoords, direction);
/* 352 */       checkXDirection(negXCoords, -1);
/* 353 */       checkXDirection(posXCoords, 1);
/* 354 */       checkZDirection(negZCoords, -1);
/* 355 */       checkZDirection(posZCoords, 1);
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
/*     */   
/*     */   private void checkZDirection(BlockPos currentCoords, int direction) {
/* 368 */     boolean continueFlag = false;
/*     */     
/* 370 */     if (!this.insideCoords.contains(currentCoords) && 
/* 371 */       isInside(currentCoords)) {
/* 372 */       if (direction < 0) {
/* 373 */         if ((!this.worldObj.isSideSolid(currentCoords, EnumFacing.SOUTH) && this.worldObj
/* 374 */           .getBlockState(currentCoords).getBlock() != Blocks.glass && this.worldObj
/* 375 */           .getBlockState(currentCoords).getBlock() != Blocks.glass_pane && this.worldObj
/* 376 */           .getBlockState(currentCoords).getBlock() != Blocks.stained_glass && this.worldObj
/* 377 */           .getBlockState(currentCoords).getBlock() != Blocks.stained_glass_pane) || this.worldObj
/* 378 */         	.getBlockState(currentCoords).getBlock() instanceof net.minecraft.block.BlockWorkbench || this.worldObj
/* 379 */           .getBlockState(currentCoords).getBlock() instanceof net.minecraft.block.BlockFurnace) {
/* 380 */           continueFlag = true;
/*     */         }
/*     */       }
/* 383 */       else if ((!this.worldObj.isSideSolid(currentCoords, EnumFacing.NORTH) && this.worldObj
/* 384 */         .getBlockState(currentCoords).getBlock() != Blocks.glass && this.worldObj
/* 385 */         .getBlockState(currentCoords).getBlock() != Blocks.glass_pane && this.worldObj
/* 386 */         .getBlockState(currentCoords).getBlock() != Blocks.stained_glass && this.worldObj
/* 387 */         .getBlockState(currentCoords).getBlock() != Blocks.stained_glass_pane) || this.worldObj
/* 388 */       	.getBlockState(currentCoords).getBlock() instanceof net.minecraft.block.BlockWorkbench || this.worldObj
/* 389 */         .getBlockState(currentCoords).getBlock() instanceof net.minecraft.block.BlockFurnace) {
/* 390 */         continueFlag = true;
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 396 */     this.insideCoords.add(currentCoords);
/*     */     
/* 398 */     if (continueFlag) {
/*     */       
/* 400 */       BlockPos nextZCoords = new BlockPos(currentCoords.add(0,0,direction));
/* 401 */       BlockPos negXCoords = new BlockPos(currentCoords.add(-1, 0, 0));
/* 402 */       BlockPos posXCoords = new BlockPos(currentCoords.add(1, 0, 0));
/* 403 */       BlockPos negYCoords = new BlockPos(currentCoords.add(0, -1, 0));
/* 404 */       BlockPos posYCoords = new BlockPos(currentCoords.add(0, 1, 0));
/* 405 */       checkXDirection(nextZCoords, direction);
/* 406 */       checkYDirection(negYCoords, -1);
/* 407 */       checkYDirection(posYCoords, 1);
/* 408 */       checkZDirection(negXCoords, -1);
/* 409 */       checkZDirection(posXCoords, 1);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void findEntranceCoords() {
/* 419 */     if (!isInside(new BlockPos(this.doorCoords.add(3, 0, 0))) && this.worldObj.isAirBlock(this.doorCoords.add(3, 0, 0))) {
/* 420 */       this.entranceCoords = new BlockPos(this.doorCoords.add(3, 0, 0)); return;
/*     */     } 
/* 422 */     if (!isInside(new BlockPos(this.doorCoords.add(-3, 0, 0))) && this.worldObj.isAirBlock(this.doorCoords.add(-3, 0, 0))) {
/* 423 */       this.entranceCoords = new BlockPos(this.doorCoords.add(-3, 0, 0)); return;
/*     */     } 
/* 425 */     if (!isInside(new BlockPos(this.doorCoords.add(0, 0, 3))) && this.worldObj.isAirBlock(this.doorCoords.add(0, 0, 3))) {
/* 426 */       this.entranceCoords = new BlockPos(this.doorCoords.add(0, 0, 3)); return;
/*     */     } 
/* 428 */     if (!isInside(new BlockPos(this.doorCoords.add(0, 0, -3))) && this.worldObj.isAirBlock(this.doorCoords.add(0, 0, -3))) {
/* 429 */       this.entranceCoords = new BlockPos(this.doorCoords.add(0, 0, -3));
/*     */       return;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean isInside(BlockPos currentCoords) {
/* 440 */     if (!this.worldObj.canBlockSeeSky(currentCoords)) {
/* 441 */       return true;
/*     */     }
/* 443 */     for (int i = currentCoords.getY() + 1; i < 256; i++) {
				Integer currX = currentCoords.getX();
				Integer currZ = currentCoords.getZ();
				BlockPos testPos = new BlockPos(currX, i, currZ);
/* 444 */       if (!this.worldObj.isAirBlock(testPos)) {
/* 445 */         return true;
/*     */       }
/*     */     } 
/* 448 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockPos getFrameCoords() {
/* 453 */     return new BlockPos((int)this.itemFrame.posX, (int)this.itemFrame.posY, (int)this.itemFrame.posZ);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void checkFrame() {
/* 460 */     if (!this.worldObj.isRemote) {
/* 461 */       if (this.itemFrame != null && (!this.itemFrame.isEntityAlive() || !matchesProfession(this.itemFrame, this.typeNum))) {
/* 462 */         this.itemFrame = null;
/* 463 */         this.village.guildHallList.remove(this);
/* 464 */         this.village.unlockedHalls[this.typeNum - 1] = false;
/* 465 */       } else if (this.itemFrame == null) {
/* 466 */         this.village.guildHallList.remove(this);
/* 467 */         this.village.unlockedHalls[this.typeNum - 1] = false;
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void checkChests() {
/* 476 */     this.guildChests.clear();
/* 477 */     Iterator<BlockPos> iterator = this.insideCoords.iterator();
/*     */     
/* 479 */     while (iterator.hasNext()) {
/* 480 */       BlockPos currentCoords = iterator.next();
/* 481 */       if (this.worldObj.getBlockState(currentCoords).getBlock() == Blocks.chest || this.worldObj.getBlockState(currentCoords).getBlock() == Blocks.trapped_chest) {
/* 482 */         TileEntityChest chest = (TileEntityChest)this.worldObj.getTileEntity(currentCoords);
/* 483 */         if (!this.guildChests.contains(chest)) {
/* 484 */           this.guildChests.add(chest);
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void checkFurnaces() {
/* 494 */     this.guildFurnaces.clear();
/* 495 */     Iterator<BlockPos> iterator = this.insideCoords.iterator();
/* 496 */     while (iterator.hasNext()) {
/* 497 */       BlockPos currentCoords = iterator.next();
/* 498 */       if (this.worldObj.getBlockState(currentCoords).getBlock() instanceof net.minecraft.block.BlockFurnace) {
/* 499 */         TileEntityFurnace furnace = (TileEntityFurnace)this.worldObj.getTileEntity(currentCoords);
/* 500 */         if (!this.guildFurnaces.contains(furnace)) {
/* 501 */           this.guildFurnaces.add(furnace);
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasWorkbench() {
/* 511 */     Iterator<BlockPos> i = this.insideCoords.iterator();
/* 512 */     while (i.hasNext()) {
/* 513 */       BlockPos coords = i.next();
/* 514 */       Block block = this.worldObj.getBlockState(coords).getBlock();
/* 515 */       if (block instanceof net.minecraft.block.BlockWorkbench) {
/* 516 */         return true;
/*     */       }
/*     */     } 
/* 519 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object object) {
/* 524 */     if (object instanceof GuildHall) {
/* 525 */       GuildHall guildHall = (GuildHall)object;
/* 526 */       if (this != null && guildHall != null && 
/* 527 */         this.typeNum == guildHall.typeNum && 
/* 528 */         this.doorCoords.equals(guildHall.doorCoords)) {
/* 529 */         return true;
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 534 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getTypeNum() {
/* 541 */     return this.typeNum;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTypeNum(int typeNum) {
/* 549 */     this.typeNum = typeNum;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean typeMatchesName(String name) {
/* 557 */     if (name.contains("Lumberjack"))
/* 558 */       return (this.typeNum == 1); 
/* 559 */     if (name.contains("Miner"))
/* 560 */       return (this.typeNum == 2); 
/* 561 */     if (name.contains("Farmer"))
/* 562 */       return (this.typeNum == 3); 
/* 563 */     if (name.contains("Soldier"))
/* 564 */       return (this.typeNum == 4); 
/* 565 */     if (name.contains("Archer"))
/* 566 */       return (this.typeNum == 5); 
/* 567 */     if (name.contains("Merchant")) {
/* 568 */       return (this.typeNum == 6);
/*     */     }
/* 570 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TileEntityChest getAvailableChest() {
/* 578 */     checkChests();
/* 579 */     Iterator<TileEntityChest> iterator = this.guildChests.iterator();
/*     */     
/* 581 */     while (iterator.hasNext()) {
/* 582 */       TileEntityChest chest = iterator.next();
/* 583 */       int size = chest.getSizeInventory();
/* 584 */       for (int i = 0; i < size; i++) {
/* 585 */         if (chest.getStackInSlot(i) == null) {
/* 586 */           return chest;
/*     */         }
/*     */       } 
/*     */     } 
/* 590 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TileEntityFurnace getAvailableFurnace() {
/* 601 */     checkFurnaces();
/* 602 */     Iterator<TileEntityFurnace> iterator = this.guildFurnaces.iterator();
/*     */     
/* 604 */     while (iterator.hasNext()) {
/* 605 */       TileEntityFurnace furnace = iterator.next();
/* 606 */       if (furnace.getStackInSlot(0) == null) {
/* 607 */         return furnace;
/*     */       }
/*     */     } 
/*     */     
/* 611 */     return null;
/*     */   }
/*     */ }


/* Location:              D:\Users\Joseph\Downloads\helpfulvillagers-1.7.10-1.4.0b5.jar!\mods\helpfulvillagers\village\GuildHall.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */