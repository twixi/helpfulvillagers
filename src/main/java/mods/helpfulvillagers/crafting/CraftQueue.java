/*     */ package mods.helpfulvillagers.crafting;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import mods.helpfulvillagers.entity.AbstractVillager;
/*     */ import mods.helpfulvillagers.main.HelpfulVillagers;
/*     */ import net.minecraft.nbt.NBTBase;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.nbt.NBTTagList;
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
/*     */ public class CraftQueue
/*     */ {
/*  24 */   private ArrayList<CraftItem> playerItems = new ArrayList<CraftItem>();
/*  25 */   private ArrayList<CraftItem> villagerItems = new ArrayList<CraftItem>();
/*     */ 
/*     */   
/*     */   public CraftQueue() {}
/*     */ 
/*     */   
/*     */   public CraftQueue(ArrayList<CraftItem> items) {
/*  32 */     for (CraftItem i : items) {
/*  33 */       if (i != null) {
/*  34 */         if (i.getPriority() >= 1) {
/*  35 */           this.playerItems.add(i); continue;
/*     */         } 
/*  37 */         this.villagerItems.add(i);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void getCraftItem(AbstractVillager villager) {
/*     */     int i;
/*  48 */     for (i = 0; i < this.playerItems.size(); i++) {
/*  49 */       CraftItem item = this.playerItems.get(i);
/*  50 */       if (villager.canCraft(item)) {
/*  51 */         villager.currentCraftItem = this.playerItems.remove(i);
/*  52 */         if (HelpfulVillagers.villageCollection != null) {
/*  53 */           HelpfulVillagers.villageCollection.markDirty();
/*     */         }
/*     */         
/*     */         return;
/*     */       } 
/*     */     } 
/*  59 */     for (i = 0; i < this.villagerItems.size(); i++) {
/*  60 */       CraftItem item = this.villagerItems.get(i);
/*  61 */       if (villager.canCraft(item)) {
/*  62 */         villager.currentCraftItem = this.villagerItems.remove(i);
/*  63 */         if (HelpfulVillagers.villageCollection != null) {
/*  64 */           HelpfulVillagers.villageCollection.markDirty();
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
/*     */   public CraftItem getItemStackAt(int index) {
/*  77 */     ArrayList<CraftItem> temp = new ArrayList<CraftItem>();
/*  78 */     temp.addAll(this.playerItems);
/*  79 */     temp.addAll(this.villagerItems);
/*  80 */     if (index >= temp.size()) {
/*  81 */       return null;
/*     */     }
/*  83 */     return temp.get(index);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeItemStackAt(int index) {
/*  92 */     if (index >= this.playerItems.size()) {
/*  93 */       index -= this.playerItems.size();
/*  94 */       if (index >= this.villagerItems.size()) {
/*     */         
/*  96 */         System.out.println("ERROR: Index Too Large");
/*     */       } else {
/*  98 */         this.villagerItems.remove(index);
/*     */       } 
/*     */     } else {
/* 101 */       this.playerItems.remove(index);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addPlayerItem(CraftItem item) {
/* 110 */     if (item != null) {
/* 111 */       this.playerItems.add(item);
/* 112 */       if (HelpfulVillagers.villageCollection != null) {
/* 113 */         HelpfulVillagers.villageCollection.markDirty();
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addVillagerItem(CraftItem item) {
/* 123 */     if (item != null) {
/* 124 */       this.villagerItems.add(item);
/* 125 */       if (HelpfulVillagers.villageCollection != null) {
/* 126 */         HelpfulVillagers.villageCollection.markDirty();
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getSize() {
/* 135 */     return this.playerItems.size() + this.villagerItems.size();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ArrayList<CraftItem> getPlayerQueue() {
/* 142 */     ArrayList<CraftItem> temp = new ArrayList<CraftItem>();
/* 143 */     temp.addAll(this.playerItems);
/* 144 */     return temp;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ArrayList<CraftItem> getVillagerQueue() {
/* 151 */     ArrayList<CraftItem> temp = new ArrayList<CraftItem>();
/* 152 */     temp.addAll(this.villagerItems);
/* 153 */     return temp;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ArrayList<CraftItem> getAll() {
/* 160 */     ArrayList<CraftItem> temp = new ArrayList<CraftItem>();
/* 161 */     temp.addAll(this.playerItems);
/* 162 */     temp.addAll(this.villagerItems);
/* 163 */     return temp;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public NBTBase writeToNBT(NBTTagList nbtTagList) {
/* 173 */     for (CraftItem i : this.playerItems) {
/* 174 */       NBTTagCompound nbttagcompound = new NBTTagCompound();
/* 175 */       nbttagcompound.setBoolean("Player", true);
/* 176 */       nbttagcompound.setTag("Item", i.writeToNBT(new NBTTagCompound()));
/* 177 */       nbtTagList.appendTag((NBTBase)nbttagcompound);
/*     */     } 
/*     */     
/* 180 */     for (CraftItem i : this.villagerItems) {
/* 181 */       NBTTagCompound nbttagcompound = new NBTTagCompound();
/* 182 */       nbttagcompound.setBoolean("Player", false);
/* 183 */       nbttagcompound.setTag("Item", i.writeToNBT(new NBTTagCompound()));
/* 184 */       nbtTagList.appendTag((NBTBase)nbttagcompound);
/*     */     } 
/*     */     
/* 187 */     return (NBTBase)nbtTagList;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void readFromNBT(NBTTagList nbttaglist) {
/* 196 */     for (int i = 0; i < nbttaglist.tagCount(); i++) {
/* 197 */       NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
/* 198 */       boolean player = nbttagcompound.getBoolean("Player");
/* 199 */       NBTTagCompound craftCompound = nbttagcompound.getCompoundTag("Item");
/* 200 */       CraftItem craftItem = CraftItem.loadCraftItemFromNBT(craftCompound);
/* 201 */       if (player) {
/* 202 */         this.playerItems.add(craftItem);
/*     */       } else {
/* 204 */         this.villagerItems.add(craftItem);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clear() {
/* 213 */     this.playerItems.clear();
/* 214 */     this.villagerItems.clear();
/* 215 */     if (HelpfulVillagers.villageCollection != null) {
/* 216 */       HelpfulVillagers.villageCollection.markDirty();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void mergeQueue(CraftQueue otherQueue) {
/* 225 */     this.playerItems.addAll(otherQueue.getPlayerQueue());
/* 226 */     this.villagerItems.addAll(otherQueue.getVillagerQueue());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 233 */     return this.playerItems.toString() + " " + this.villagerItems.toString();
/*     */   }
/*     */ }


/* Location:              D:\Users\Joseph\Downloads\helpfulvillagers-1.7.10-1.4.0b5.jar!\mods\helpfulvillagers\crafting\CraftQueue.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */