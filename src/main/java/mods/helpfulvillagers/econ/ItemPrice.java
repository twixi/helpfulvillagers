/*     */ package mods.helpfulvillagers.econ;
/*     */ 
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.nbt.NBTBase;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ItemPrice
/*     */ {
/*     */   private ItemStack item;
/*     */   private int price;
/*  15 */   private double supply = 1.0D;
/*  16 */   private double demand = 1.0D;
/*     */   
/*     */   public ItemPrice() {}
/*     */   
/*     */   public ItemPrice(ItemStack item, int price) {
/*  21 */     this.item = item;
/*  22 */     this.price = price;
/*     */   }
/*     */   
/*     */   public ItemPrice(ItemStack item, int price, double supply, double demand) {
/*  26 */     this.item = item;
/*  27 */     this.price = price;
/*  28 */     this.supply = supply;
/*  29 */     this.demand = demand;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void changeSupply(double val) {
/*  37 */     this.supply += val;
/*     */     
/*  39 */     if (this.supply <= 0.0D) {
/*  40 */       this.supply -= val;
/*     */       return;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void changeDemand(double val) {
/*  50 */     this.demand += val;
/*     */     
/*  52 */     if (this.demand <= 0.0D) {
/*  53 */       this.demand -= val;
/*     */       return;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void increaseSupply(double amount) {
/*  63 */     changeSupply(1.0D / this.item.getMaxStackSize() * amount);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void decreaseSupply(double amount) {
/*  71 */     changeSupply(-1.0D / this.item.getMaxStackSize() * amount);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void increaseDemand(double amount) {
/*  79 */     changeDemand(1.0D / this.item.getMaxStackSize() * amount);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void decreaseDemand(double amount) {
/*  87 */     changeDemand(-1.0D / this.item.getMaxStackSize() * amount);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ItemStack getItem() {
/*  94 */     return this.item;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getOriginalPrice() {
/* 101 */     return this.price;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getPrice() {
/* 108 */     int newPrice = (int)(this.price * this.demand / this.supply);
/* 109 */     if (newPrice <= 0) {
/* 110 */       return 1;
/*     */     }
/* 112 */     return newPrice;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getSupply() {
/* 119 */     return this.supply;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getDemand() {
/* 126 */     return this.demand;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public NBTBase writeToNBT(NBTTagCompound compound) {
/* 135 */     this.item.writeToNBT(compound);
/* 136 */     compound.setInteger("Price", this.price);
/* 137 */     compound.setDouble("Supply", this.supply);
/* 138 */     compound.setDouble("Demand", this.demand);
/* 139 */     return (NBTBase)compound;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void readFromNBT(NBTTagCompound compound) {
/* 147 */     this.item = ItemStack.loadItemStackFromNBT(compound);
/*     */     
/* 149 */     this.price = compound.getInteger("Price");
/*     */     
/* 151 */     this.supply = compound.getDouble("Supply");
/*     */     
/* 153 */     this.demand = compound.getDouble("Demand");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ItemPrice loadCraftItemFromNBT(NBTTagCompound compound) {
/* 162 */     ItemPrice itemPrice = new ItemPrice();
/* 163 */     itemPrice.readFromNBT(compound);
/* 164 */     return (itemPrice.getItem() != null) ? itemPrice : null;
/*     */   }
/*     */ }


/* Location:              D:\Users\Joseph\Downloads\helpfulvillagers-1.7.10-1.4.0b5.jar!\mods\helpfulvillagers\econ\ItemPrice.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */