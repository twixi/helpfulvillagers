/*     */ package mods.helpfulvillagers.ai;
/*     */ 
/*     */ import java.util.ArrayList;
import java.util.List;
/*     */ import java.util.Random;
/*     */ import mods.helpfulvillagers.entity.AbstractVillager;
/*     */ import mods.helpfulvillagers.entity.EntityRancher;
/*     */ import mods.helpfulvillagers.util.AIHelper;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.entity.passive.EntityAnimal;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.tileentity.TileEntityChest;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.DamageSource;
/*     */ import net.minecraft.world.IBlockAccess;
/*     */ import net.minecraftforge.common.IShearable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class EntityAIRancher
/*     */   extends EntityAIWorker
/*     */ {
/*  28 */   private final ItemStack[] breedingItems = new ItemStack[] { new ItemStack(Items.golden_apple), new ItemStack(Items.golden_carrot), new ItemStack(Items.wheat), new ItemStack(Items.carrot), new ItemStack(Items.wheat_seeds), new ItemStack(Items.rotten_flesh), new ItemStack(Items.porkchop), new ItemStack(Items.cooked_porkchop), new ItemStack(Items.beef), new ItemStack(Items.cooked_beef), new ItemStack(Items.chicken), new ItemStack(Items.cooked_chicken), new ItemStack(Items.fish), new ItemStack(Items.cooked_fish) };
/*     */ 
/*     */ 
/*     */   
/*     */   private Random rand;
/*     */ 
/*     */ 
/*     */   
/*     */   private EntityRancher rancher;
/*     */ 
/*     */ 
/*     */   
/*     */   private int searchLimit;
/*     */ 
/*     */ 
/*     */   
/*     */   private EntityAnimal foundAnimal;
/*     */ 
/*     */   
/*     */   private boolean pastureChecked;
/*     */ 
/*     */ 
/*     */   
/*     */   public EntityAIRancher(EntityRancher rancher) {
/*  52 */     super((AbstractVillager)rancher);
/*  53 */     this.rancher = rancher;
/*  54 */     this.target = null;
/*  55 */     this.rand = new Random();
/*  56 */     this.foundAnimal = null;
/*  57 */     this.searchLimit = 20;
/*  58 */     this.pastureChecked = false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean gather() {
/*  64 */     if (this.rancher.homeGuildHall == null) {
/*  65 */       return idle();
/*     */     }
/*     */     
/*  68 */     if (this.rancher.insideHall()) {
/*  69 */       BlockPos exit = this.rancher.homeGuildHall.entranceCoords;
/*  70 */       if (exit == null) {
/*  71 */         exit = AIHelper.getRandInsideCoords((AbstractVillager)this.rancher);
/*     */       }
/*  73 */       this.rancher.moveTo(exit, this.speed);
/*     */     }
/*  75 */     else if (!this.rancher.searching && this.rancher.getRanch() != null && (this.rancher.getRanch()).herd.size() > 0 && (this.rancher.getRanch()).checkedAnimals.size() < (this.rancher.getRanch()).herd.size()) {
/*     */       
/*  77 */       for (EntityAnimal animal : (this.rancher.getRanch()).herd)
/*     */       {
/*  79 */         if (animal == null || animal.isDead || (this.rancher.getRanch()).checkedAnimals.contains(animal)) {
/*     */           continue;
/*     */         }
/*     */         
/*  83 */         int distX = AIHelper.findDistance((int)this.rancher.posX, (int)animal.posX);
/*  84 */         int distY = AIHelper.findDistance((int)this.rancher.posY, (int)animal.posY);
/*  85 */         int distZ = AIHelper.findDistance((int)this.rancher.posZ, (int)animal.posZ);
/*  86 */         this.target = new BlockPos((int)animal.posX, (int)animal.posY, (int)animal.posZ);
/*  87 */         if (distX > 3 || distY > 3 || distZ > 3) {
/*  88 */           this.rancher.moveTo(this.target, this.speed); continue;
/*     */         } 
/*  90 */         checkAnimal(animal);
/*     */       }
/*     */     
/*  93 */     } else if (this.foundAnimal == null) {
/*     */       
/*  95 */       this.rancher.searching = true;
/*  96 */       findAnimal();
/*     */     } else {
/*  98 */       int distX = AIHelper.findDistance((int)this.rancher.posX, (int)this.foundAnimal.posX);
/*  99 */       int distY = AIHelper.findDistance((int)this.rancher.posY, (int)this.foundAnimal.posY);
/* 100 */       int distZ = AIHelper.findDistance((int)this.rancher.posZ, (int)this.foundAnimal.posZ);
/* 101 */       this.target = null;
/* 102 */       if (distX > 3 || distY > 3 || distZ > 3) {
/* 103 */         this.rancher.moveTo(new BlockPos((int)this.foundAnimal.posX, (int)this.foundAnimal.posY, (int)this.foundAnimal.posZ), this.speed);
/*     */       } else {
/* 105 */         retrieveAnimal();
/*     */       } 
/*     */     } 
/*     */     
/* 109 */     return idle();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void checkAnimal(EntityAnimal animal) {
/* 117 */     if (!animal.isChild()) {
/* 118 */       if (animal.getGrowingAge() == 0) {
/* 119 */         breedAnimal(animal);
/*     */       }
/*     */       
/* 122 */       if (animal instanceof IShearable) {
/* 123 */         shearAnimal(animal);
/*     */       }
/*     */       
/* 126 */       this.rancher.setPickupRadius(3);
/* 127 */       slaughterAnimal(animal);
/* 128 */       this.rancher.setPickupRadius(1);
/*     */     } 
/* 130 */     (this.rancher.getRanch()).checkedAnimals.add(animal);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void breedAnimal(EntityAnimal animal) {
/* 138 */     TileEntityChest chest = null;
/*     */     
/* 140 */     for (ItemStack i : this.breedingItems) {
/* 141 */       if (animal.isBreedingItem(i)) {
/* 142 */         if (this.rancher.inventory.containsItem(i) < 0) {
/*     */ 
/*     */           
/* 145 */           for (int j = 0; j < (this.rancher.getRanch()).guildChests.size(); j++) {
/* 146 */             TileEntityChest c = (TileEntityChest) (this.rancher.getRanch()).guildChests.get(j);
/* 147 */             if (AIHelper.chestContains(c, i) >= 0) {
/* 148 */               chest = c;
/*     */             }
/*     */           } 
/*     */ 
/*     */           
/* 153 */           if (chest == null) {
/* 154 */             chest = this.rancher.homeVillage.searchHallsForItem(i);
/*     */           }
/*     */           
/* 157 */           if (chest != null) {
/* 158 */             AIHelper.takeItemFromChest(new ItemStack(i.getItem(), i.getMaxStackSize()), chest, (AbstractVillager)this.rancher, false);
/*     */           }
/*     */         } 
/*     */         
/* 162 */         int index = this.rancher.inventory.containsItem(i);
/* 163 */         if (index >= 0) {
/* 164 */           this.rancher.inventory.decrementSlot(index);
/* 165 */           animal.setInLove(null);
/*     */         } 
/*     */         return;
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
/*     */   private void shearAnimal(EntityAnimal animal) {
/* 179 */     IShearable shearable = (IShearable)animal;
/* 180 */     if (!shearable.isShearable(new ItemStack((Item)Items.shears), (IBlockAccess)animal.worldObj, animal.getPosition())) {
/*     */       return;
/*     */     }
/*     */     
/* 184 */     TileEntityChest chest = null;
/*     */     
/* 186 */     ItemStack shears = new ItemStack((Item)Items.shears);
/*     */     
/* 188 */     if (this.rancher.inventory.containsItem(shears) < 0) {
/*     */ 
/*     */       
/* 191 */       for (int j = 0; j < (this.rancher.getRanch()).guildChests.size(); j++) {
/* 192 */         TileEntityChest c = (TileEntityChest) (this.rancher.getRanch()).guildChests.get(j);
/* 193 */         if (AIHelper.chestContains(c, shears) >= 0) {
/* 194 */           chest = c;
/*     */         }
/*     */       } 
/*     */ 
/*     */       
/* 199 */       if (chest == null) {
/* 200 */         chest = this.rancher.homeVillage.searchHallsForItem(shears);
/*     */       }
/*     */       
/* 203 */       if (chest != null) {
/* 204 */         AIHelper.takeItemFromChest(new ItemStack(shears.getItem(), shears.getMaxStackSize()), chest, (AbstractVillager)this.rancher, false);
/*     */       }
/*     */     } 
/*     */     
/* 208 */     int index = this.rancher.inventory.containsItem(shears);
/* 209 */     if (index >= 0) {
/* 210 */       shears = this.rancher.inventory.getStackInSlot(index);
/* 211 */       shears.damageItem(1, (EntityLivingBase)this.rancher);
/* 212 */       this.rancher.inventory.setInventorySlotContents(index, shears);
/* 213 */       List<ItemStack> drops = shearable.onSheared(new ItemStack(Items.shears), (IBlockAccess)animal.worldObj, animal.getPosition(), 3);
/* 214 */       for (ItemStack item : drops) {
/* 215 */         this.rancher.inventory.addItem(item);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void slaughterAnimal(EntityAnimal animal) {
/* 226 */     int count = 0;
/* 227 */     for (EntityAnimal otherAnimal : (this.rancher.getRanch()).herd) {
/*     */       
/* 229 */       count++;
/* 230 */       if (animal.getClass() == otherAnimal.getClass() && count >= 2 && (
/* 231 */         !animal.isInLove() || count >= 4)) {
/* 232 */         boolean attackSuccess = animal.attackEntityFrom(DamageSource.causeMobDamage((EntityLivingBase)this.rancher), 20.0F);
/*     */         break;
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
/*     */   private void findAnimal() {
/* 245 */     if (this.target == null) {
/* 246 */       this.target = AIHelper.getRandOutsideCoords((AbstractVillager)this.rancher, this.searchLimit);
/*     */     }
/*     */ 
/*     */     
/* 250 */     if (this.target != null) {
/* 251 */       this.rancher.moveTo(this.target, this.speed);
/*     */     }
/*     */     
/* 254 */     if (!AIHelper.isInRangeOfAnyVillage(this.rancher.posX, this.rancher.posY, this.rancher.posZ)) {
/* 255 */       this.rancher.updateBoxes();
/* 256 */       if (this.rancher.searchBox != null) {
/* 257 */         this.foundAnimal = getNewResource();
/* 258 */         if (this.foundAnimal != null) {
/* 259 */           this.rancher.getNavigator().clearPathEntity();
/*     */         }
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 265 */     if (Math.abs(this.rancher.posX - this.target.getX()) <= 5.0D && Math.abs(this.rancher.posZ - this.target.getZ()) <= 5.0D) {
/* 266 */       this.target = null;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void retrieveAnimal() {
/* 274 */     if (this.foundAnimal.getLeashed()) {
/* 275 */       if (this.foundAnimal.getLeashedToEntity().getEntityId() != this.rancher.getEntityId()) {
/* 276 */         this.foundAnimal.setLeashedToEntity((Entity)this.rancher, true);
/*     */       }
/*     */     } else {
/* 279 */       this.foundAnimal.setLeashedToEntity((Entity)this.rancher, true);
/*     */     } 
/*     */     
/* 282 */     if (!this.pastureChecked) {
/* 283 */       this.rancher.getRanch().checkPasture();
/* 284 */       this.pastureChecked = true;
/*     */     } 
/*     */     
/* 287 */     boolean hasPasture = false;
/*     */     
/* 289 */     if (this.target == null) {
/* 290 */       if ((this.rancher.getRanch()).pastureCoords.size() > 0) {
/* 291 */         int index = this.rand.nextInt((this.rancher.getRanch()).pastureCoords.size() - 1);
/* 292 */         this.target = (this.rancher.getRanch()).pastureCoords.get(index);
/* 293 */         hasPasture = true;
/*     */       } else {
/* 295 */         int index = this.rand.nextInt(this.rancher.homeGuildHall.insideCoords.size() - 1);
/* 296 */         this.target = this.rancher.homeGuildHall.insideCoords.get(index);
/*     */       } 
/*     */     }
/*     */     
/* 300 */     if (this.target != null) {
/* 301 */       this.rancher.moveTo(this.target, this.speed);
/*     */     }
/*     */     
/* 304 */     if (hasPasture) {
/* 305 */       if (this.rancher.nearPasture()) {
/* 306 */         storeAnimal();
/*     */       }
/*     */     }
/* 309 */     else if (this.rancher.nearHall()) {
/* 310 */       storeAnimal();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void storeAnimal() {
/* 320 */     this.foundAnimal.clearLeashed(true, false);
/* 321 */     this.foundAnimal.setLocationAndAngles(this.target.getX(), this.target.getY(), this.target.getZ(), this.foundAnimal.rotationPitch, this.foundAnimal.rotationYaw);
/* 322 */     this.foundAnimal.setHomePosAndDistance(this.target, 1);
/* 323 */     (this.rancher.getRanch()).herd.add(this.foundAnimal);
/* 324 */     this.foundAnimal = null;
/* 325 */     this.rancher.searching = false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private EntityAnimal getNewResource() {
/* 334 */     ArrayList<EntityAnimal> boxCoords = this.rancher.getValidCoords();
/* 335 */     double closestDist = Double.MAX_VALUE;
/* 336 */     EntityAnimal closestAnimal = null;
/*     */     
/* 338 */     for (int i = 0; i < boxCoords.size(); i++) {
/* 339 */       EntityAnimal animal = boxCoords.get(i);
/* 340 */       BlockPos currCoords = new BlockPos((int)animal.posX, (int)animal.posY, (int)animal.posZ);
/* 341 */       double dist = this.rancher.getDistance(currCoords.getX(), currCoords.getY(), currCoords.getZ());
/* 342 */       if (!(this.rancher.getRanch()).herd.contains(animal) && dist < closestDist) {
/* 343 */         closestAnimal = animal;
/* 344 */         closestDist = dist;
/*     */       } 
/*     */     } 
/*     */     
/* 348 */     return closestAnimal;
/*     */   }
/*     */ }


/* Location:              D:\Users\Joseph\Downloads\helpfulvillagers-1.7.10-1.4.0b5.jar!\mods\helpfulvillagers\ai\EntityAIRancher.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */