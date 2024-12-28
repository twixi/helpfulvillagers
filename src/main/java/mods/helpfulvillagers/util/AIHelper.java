/*     */ package mods.helpfulvillagers.util;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
import java.util.List;
/*     */ import java.util.Random;
/*     */ import mods.helpfulvillagers.crafting.CraftTree;
/*     */ import mods.helpfulvillagers.entity.AbstractVillager;
/*     */ import mods.helpfulvillagers.main.HelpfulVillagers;
/*     */ import mods.helpfulvillagers.village.GuildHall;
/*     */ import mods.helpfulvillagers.village.HelpfulVillage;
/*     */ import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.tileentity.TileEntityChest;
/*     */ import net.minecraft.tileentity.TileEntityFurnace;
/*     */ import net.minecraft.util.BlockPos;
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
/*     */ public class AIHelper
/*     */ {
/*     */   public static BlockPos getRandOutsideCoords(AbstractVillager villager, int limit) {
/*  50 */     Random gen = new Random();
/*  51 */     HelpfulVillage village = villager.homeVillage;
/*     */     
/*  53 */     if (villager.lastResource != null) {
/*     */       int newX, newZ;
/*  55 */       BlockPos center = villager.lastResource.coords;
/*  56 */       int x = center.getX();
/*  57 */       int y = center.getY();
/*  58 */       int z = center.getZ();
/*     */ 
/*     */ 
/*     */       
/*  62 */       if (gen.nextBoolean()) {
/*  63 */         newX = x + gen.nextInt(limit / 2);
/*     */       } else {
/*  65 */         newX = x - gen.nextInt(limit / 2);
/*     */       } 
/*     */       
/*  68 */       if (gen.nextBoolean()) {
/*  69 */         newZ = z + gen.nextInt(limit / 2);
/*     */       } else {
/*  71 */         newZ = z - gen.nextInt(limit / 2);
/*     */       } 
/*     */       
/*  74 */       return new BlockPos(newX, y, newZ);
/*     */     } 
/*  76 */     if (village != null) {
/*     */       int newX, newZ;
/*  78 */       BlockPos center = village.getActualCenter();
/*  79 */       int x = center.getX();
/*  80 */       int y = center.getY();
/*  81 */       int z = center.getZ();
/*     */ 
/*     */ 
/*     */       
/*  85 */       if (gen.nextBoolean()) {
/*  86 */         newX = x - village.getActualRadius() + 10;
/*  87 */         newX -= gen.nextInt(limit / 2);
/*     */       } else {
/*  89 */         newX = x + village.getActualRadius() + 10;
/*  90 */         newX += gen.nextInt(limit / 2);
/*     */       } 
/*     */       
/*  93 */       if (gen.nextBoolean()) {
/*  94 */         newZ = z - village.getActualRadius() + 10;
/*  95 */         newZ -= gen.nextInt(limit / 2);
/*     */       } else {
/*  97 */         newZ = z + village.getActualRadius() + 10;
/*  98 */         newZ += gen.nextInt(limit / 2);
/*     */       } 
/*     */       
/* 101 */       return new BlockPos(newX, y, newZ);
/*     */     } 
/*     */     
/* 104 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static BlockPos getRandInsideCoords(AbstractVillager villager) {
/* 115 */     Random gen = new Random();
/* 116 */     HelpfulVillage village = villager.homeVillage;
/*     */     
/* 118 */     if (village != null) {
/* 119 */       BlockPos center = village.getActualCenter();
/* 120 */       int xRange = (int)(Math.abs(village.actualBounds.maxX - village.actualBounds.minX) + 5.0D);
/* 121 */       int zRange = (int)(Math.abs(village.actualBounds.maxZ - village.actualBounds.minZ) + 5.0D);
/*     */       
/* 123 */       int x = (int)(village.actualBounds.minX + gen.nextInt(xRange));
/* 124 */       int y = center.getY();
/* 125 */       int z = (int)(village.actualBounds.minZ + gen.nextInt(zRange));
/* 126 */       return new BlockPos(x, y, z);
/*     */     } 
/* 128 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int findDistance(int par1, int par2) {
/* 138 */     int temp1 = Math.abs(par1);
/* 139 */     int temp2 = Math.abs(par2);
/* 140 */     int temp3 = temp1 - temp2;
/* 141 */     return Math.abs(temp3);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int chestContains(TileEntityChest chest, ItemStack item) {
/* 151 */     for (int i = 0; i < chest.getSizeInventory(); i++) {
/* 152 */       ItemStack chestItem = chest.getStackInSlot(i);
/* 153 */       if (chestItem != null && chestItem.getDisplayName().equals(item.getDisplayName())) {
/* 154 */         return i;
/*     */       }
/*     */     } 
/* 157 */     return -1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int chestContains(TileEntityChest chest, AbstractVillager villager) {
/* 167 */     for (int i = 0; i < chest.getSizeInventory(); i++) {
/* 168 */       ItemStack chestItem = chest.getStackInSlot(i);
/* 169 */       if (chestItem != null && villager.isValidTool(chestItem)) {
/* 170 */         return i;
/*     */       }
/*     */     } 
/* 173 */     return -1;
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
/*     */   public static boolean takeItemFromChest(ItemStack item, TileEntityChest chest, AbstractVillager villager) {
/* 185 */     for (int i = 0; i < chest.getSizeInventory(); i++) {
/* 186 */       ItemStack chestItem = chest.getStackInSlot(i);
/* 187 */       Block block = Block.getBlockFromItem(item.getItem());
/* 188 */       Block chestBlock = Block.getBlockFromItem((chestItem != null) ? chestItem.getItem() : null);
/* 189 */       if (chestItem != null && chestItem.stackSize > 0 && ((villager.currentCraftItem.isSensitive() && chestItem.getDisplayName().equals(item.getDisplayName())) || (!villager.currentCraftItem.isSensitive() && (chestItem.getItem().equals(item.getItem()) || (block instanceof net.minecraft.block.BlockLog && chestBlock instanceof net.minecraft.block.BlockLog))))) {
/* 190 */         if (chestItem.stackSize >= item.stackSize) {
/* 191 */           chestItem.stackSize -= item.stackSize;
/* 192 */           villager.inventory.addItem(item);
/* 193 */           villager.homeVillage.economy.decreaseItemSupply(villager, item);
/* 194 */           if (chestItem.stackSize <= 0) {
/* 195 */             chestItem = null;
/*     */           }
/* 197 */           chest.setInventorySlotContents(i, chestItem);
/* 198 */           return true;
/*     */         } 
/* 200 */         villager.inventory.addItem(chestItem);
/* 201 */         villager.homeVillage.economy.decreaseItemSupply(villager, chestItem);
/* 202 */   chest.setInventorySlotContents(i, null);
/* 203 */  item.stackSize -= chestItem.stackSize;
/*     */       } 
/*     */     } 
/*     */     
/* 207 */     return false;
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
/*     */   public static boolean takeItemFromChest(ItemStack item, TileEntityChest chest, AbstractVillager villager, boolean sensitive) {
/* 219 */     for (int i = 0; i < chest.getSizeInventory(); i++) {
/* 220 */       ItemStack chestItem = chest.getStackInSlot(i);
/* 221 */       Block block = Block.getBlockFromItem(item.getItem());
/* 222 */       Block chestBlock = Block.getBlockFromItem((chestItem != null) ? chestItem.getItem() : null);
/* 223 */       if (chestItem != null && chestItem.stackSize > 0 && ((sensitive && chestItem.getDisplayName().equals(item.getDisplayName())) || (!sensitive && (chestItem.getItem().equals(item.getItem()) || (block instanceof net.minecraft.block.BlockLog && chestBlock instanceof net.minecraft.block.BlockLog))))) {
/* 224 */         if (chestItem.stackSize >= item.stackSize) {
/* 225 */           chestItem.stackSize -= item.stackSize;
/* 226 */           villager.inventory.addItem(item);
/*     */           
/* 228 */           if (chestItem.stackSize <= 0) {
/* 229 */             chestItem = null;
/*     */           }
/* 231 */           chest.setInventorySlotContents(i, chestItem);
/* 232 */           return true;
/*     */         } 
/* 234 */         villager.inventory.addItem(chestItem);
/*     */         
/* 236 */   chest.setInventorySlotContents(i, null);
/* 237 */  item.stackSize -= chestItem.stackSize;
/*     */       } 
/*     */     } 
/*     */     
/* 241 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean takeItemFromFurnace(ItemStack item, TileEntityFurnace furnace, AbstractVillager villager) {
/* 252 */     ItemStack furnaceItem = furnace.getStackInSlot(2);
/* 253 */     Block block = Block.getBlockFromItem(item.getItem());
/* 254 */     Block furnaceBlock = Block.getBlockFromItem((furnaceItem != null) ? furnaceItem.getItem() : null);
/* 255 */     if (furnaceItem != null && furnaceItem.stackSize > 0 && ((villager.currentCraftItem.isSensitive() && furnaceItem.getDisplayName().equals(item.getDisplayName())) || (!villager.currentCraftItem.isSensitive() && (furnaceItem.getItem().equals(item.getItem()) || (block instanceof net.minecraft.block.BlockLog && furnaceBlock instanceof net.minecraft.block.BlockLog))))) {
/* 256 */       if (furnaceItem.stackSize >= item.stackSize) {
/* 257 */         furnaceItem.stackSize -= item.stackSize;
/* 258 */         villager.inventory.addItem(item);
/* 259 */         if (furnaceItem.stackSize <= 0) {
/* 260 */           furnaceItem = null;
/*     */         }
/* 262 */         furnace.setInventorySlotContents(2, furnaceItem);
/* 263 */         return true;
/*     */       } 
/* 265 */       villager.inventory.addItem(furnaceItem);
/* 266 */   furnace.setInventorySlotContents(2, null);
/* 267 */item.stackSize -= furnaceItem.stackSize;
/*     */     } 
/*     */     
/* 270 */     return false;
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
/*     */   public static boolean takeItemFromFurnace(ItemStack item, TileEntityFurnace furnace, AbstractVillager villager, boolean sensitive) {
/* 282 */     ItemStack furnaceItem = furnace.getStackInSlot(2);
/* 283 */     Block block = Block.getBlockFromItem(item.getItem());
/* 284 */     Block furnaceBlock = Block.getBlockFromItem((furnaceItem != null) ? furnaceItem.getItem() : null);
/* 285 */     if (furnaceItem != null && furnaceItem.stackSize > 0 && ((sensitive && furnaceItem.getDisplayName().equals(item.getDisplayName())) || (!sensitive && (furnaceItem.getItem().equals(item.getItem()) || (block instanceof net.minecraft.block.BlockLog && furnaceBlock instanceof net.minecraft.block.BlockLog))))) {
/* 286 */       if (furnaceItem.stackSize >= item.stackSize) {
/* 287 */         furnaceItem.stackSize -= item.stackSize;
/* 288 */         villager.inventory.addItem(item);
/* 289 */         if (furnaceItem.stackSize <= 0) {
/* 290 */           furnaceItem = null;
/*     */         }
/* 292 */         furnace.setInventorySlotContents(2, furnaceItem);
/* 293 */         return true;
/*     */       } 
/* 295 */       villager.inventory.addItem(furnaceItem);
/* 296 */   furnace.setInventorySlotContents(2, null);
/* 297 */item.stackSize -= furnaceItem.stackSize;
/*     */     } 
/*     */     
/* 300 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void addFuelToFurnace(HelpfulVillage village, TileEntityFurnace furnace, int burnTime) {
/* 311 */     int totalTime = 0;
/* 312 */     for (int i = 0; i < village.guildHallList.size(); i++) {
/* 313 */       GuildHall hall = village.guildHallList.get(i);
/* 314 */       hall.checkChests();
/* 315 */       ArrayList<TileEntityChest> chests = hall.guildChests;
/* 316 */       for (int j = 0; j < chests.size(); j++) {
/* 317 */         TileEntityChest chest = chests.get(j);
/* 318 */         for (int k = 0; k < chest.getSizeInventory(); k++) {
/* 319 */           ItemStack item = chest.getStackInSlot(k);
/* 320 */           if (item != null && item.getItem().equals(Items.coal)) {
/* 321 */             int currTime = TileEntityFurnace.getItemBurnTime(item) * item.stackSize;
/* 322 */             totalTime += currTime;
/* 323 */             if (furnace.getStackInSlot(1) == null) {
/* 324 */               furnace.setInventorySlotContents(1, item);
/* 325 */         chest.setInventorySlotContents(k, null);
/*     */             } else {
/* 327 */               int size = item.stackSize + (furnace.getStackInSlot(1)).stackSize;
/* 328 */               if (size > 64) {
/* 329 */                 int removeAmount = item.stackSize - 64 - (furnace.getStackInSlot(1)).stackSize;
/* 330 */                 furnace.setInventorySlotContents(1, new ItemStack(item.getItem(), 64));
/* 331 */                 if (removeAmount <= 0) {
/* 332 */                   chest.setInventorySlotContents(k, null);
/*     */                 } else {
/* 334 */             chest.setInventorySlotContents(k, new ItemStack(item.getItem(), removeAmount));
/*     */                 } 
/*     */                 return;
/*     */               } 
/* 338 */               furnace.setInventorySlotContents(1, new ItemStack(item.getItem(), size));
/* 339 */               chest.setInventorySlotContents(k, null);
/*     */             } 
/*     */ 
/*     */             
/* 343 */             if (totalTime >= burnTime) {
/*     */               return;
/*     */             }
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
/*     */   public static ArrayList<BlockPos> getAdjacentCoords(BlockPos coords) {
/* 358 */     ArrayList<BlockPos> adjacent = new ArrayList();
/* 359 */     for (int x = -1; x <= 1; x++) {
/* 360 */       for (int y = -1; y <= 1; y++) {
/* 361 */         for (int z = -1; z <= 1; z++) {
/* 362 */           BlockPos coord = new BlockPos(coords.getX() + x, coords.getY() + y, coords.getZ() + z);
/* 363 */           adjacent.add(coord);
/*     */         } 
/*     */       } 
/*     */     } 
/* 367 */     return adjacent;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void breakBlock(BlockPos currentCoords, AbstractVillager villager) {
/* 378 */     Block currentBlock = villager.worldObj.getBlockState(currentCoords).getBlock();
/* 379 */     if (currentBlock != null && !currentBlock.equals(Blocks.air) && !currentBlock.equals(Blocks.water) && !currentBlock.equals(Blocks.lava) && !currentBlock.equals(Blocks.bedrock) && !currentBlock.equals(Blocks.tallgrass)) {
/* 380 */       IBlockState metadata = villager.worldObj.getBlockState(currentCoords);
/* 381 */       List<ItemStack> items = currentBlock.getDrops(villager.worldObj, currentCoords, metadata, 0);
/*     */ 
/*     */       
/* 384 */       if (villager instanceof mods.helpfulvillagers.entity.EntityBuilder) {
/* 385 */         int[] oreDictIDs = OreDictionary.getOreIDs(new ItemStack(currentBlock));
/* 386 */         for (int j = 0; j < oreDictIDs.length; j++) {
/* 387 */           String name = OreDictionary.getOreName(oreDictIDs[j]);
/* 388 */           if (name.contains("ore")) {
/* 389 */             items.clear();
/* 390 */             items.add(new ItemStack(Blocks.cobblestone));
/*     */             
/*     */             break;
/*     */           } 
/*     */         } 
/*     */       } 
/* 396 */       for (ItemStack i : items) {
/*     */         try {
/* 398 */           villager.inventory.addItem(i);
/* 399 */           villager.damageItem();
/* 400 */         } catch (NullPointerException e) {}
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 405 */     villager.worldObj.setBlockToAir(currentCoords);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isinsideAnyVillage(double x, double y, double z) {
/* 415 */     for (HelpfulVillage i : HelpfulVillagers.villages) {
/* 416 */       if (i != null && i.isInsideVillage(x, y, z)) {
/* 417 */         return true;
/*     */       }
/*     */     } 
/* 420 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isInRangeOfAnyVillage(double x, double y, double z) {
/* 430 */     for (HelpfulVillage i : HelpfulVillagers.villages) {
/* 431 */       if (i.isInRange(x, y, z)) {
/* 432 */         return true;
/*     */       }
/*     */     } 
/* 435 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void mergeItemStackArrays(ArrayList<ItemStack> from, ArrayList<ItemStack> to) {
/* 444 */     Iterator<ItemStack> iterator = from.iterator();
/* 445 */     while (iterator.hasNext()) {
/* 446 */       if (iterator.next() == null) {
/* 447 */         iterator.remove();
/*     */       }
/*     */     } 
/*     */     
/* 451 */     iterator = to.iterator();
/* 452 */     while (iterator.hasNext()) {
/* 453 */       if (iterator.next() == null) {
/* 454 */         iterator.remove();
/*     */       }
/*     */     } 
/*     */     
/* 458 */     for (ItemStack i : from) {
/* 459 */       iterator = to.iterator();
/* 460 */       while (iterator.hasNext()) {
/*     */         try {
/* 462 */           ItemStack j = iterator.next();
/* 463 */           if (i != null && i.getDisplayName() != null && j != null && j.getDisplayName() != null && i.getDisplayName().equals(j.getDisplayName()) && 
/* 464 */             i.stackSize > 0 && j.stackSize < j.getMaxStackSize()) {
/* 465 */            i.stackSize--;
/* 466 */             j.stackSize++;
/*     */           }
/*     */         
/* 469 */         } catch (Exception e) {
/* 470 */           System.out.println("ItemStack ArrayList Merge Failed");
/*     */         } 
/*     */       } 
/* 473 */       if (i.stackSize > 0) {
/* 474 */         to.add(i);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void mergeItemStackArrays(ItemStack from, ArrayList<ItemStack> to) {
/* 485 */     ArrayList<ItemStack> temp = new ArrayList<ItemStack>();
/* 486 */     temp.add(from);
/* 487 */     mergeItemStackArrays(temp, to);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean removeItemStack(ItemStack item, ArrayList<ItemStack> array) {
/* 497 */     Iterator<ItemStack> iterator = array.iterator();
/* 498 */     while (iterator.hasNext()) {
/* 499 */       ItemStack currItem = iterator.next();
/* 500 */       if (item.getItem().equals(currItem.getItem()) && currItem.getMetadata() == item.getMetadata()) {
/* 501 */         if (currItem.stackSize >= item.stackSize) {
/* 502 */           int itemSize = item.stackSize;
/* 503 */           currItem.stackSize -= itemSize;
/* 504 */           if (currItem.stackSize <= 0) {
/* 505 */             iterator.remove();
/*     */           }
/* 507 */           item.stackSize = 0;
/* 508 */           return true;
/*     */         } 
/* 510 */         item.stackSize -= currItem.stackSize;
/* 511 */         iterator.remove();
/*     */       } 
/*     */     } 
/*     */     
/* 515 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void removeNodeBranch(ArrayList<CraftTree.Node> nodes, CraftTree.Node parent) {
/* 524 */     Iterator<CraftTree.Node> i = nodes.iterator();
/* 525 */     while (i.hasNext()) {
/* 526 */       CraftTree.Node node = i.next();
/* 527 */       if (node.equals(parent) || node.getParent().equals(parent))
/* 528 */         i.remove(); 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              D:\Users\Joseph\Downloads\helpfulvillagers-1.7.10-1.4.0b5.jar!\mods\helpfulvillager\\util\AIHelper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */