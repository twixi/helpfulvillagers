/*     */ package mods.helpfulvillagers.village;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import net.minecraft.nbt.NBTBase;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.nbt.NBTTagList;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraft.world.WorldSavedData;
/*     */ import net.minecraft.world.storage.MapStorage;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class HelpfulVillageCollection
/*     */   extends WorldSavedData
/*     */ {
/*     */   static final String key = "HelpfulVillageCollection";
/*  22 */   private ArrayList<HelpfulVillage> villageList = new ArrayList<HelpfulVillage>();
/*     */   
/*     */   public HelpfulVillageCollection(String tagName) {
/*  25 */     super("HelpfulVillageCollection");
/*     */   }
/*     */   
/*     */   public HelpfulVillageCollection() {
/*  29 */     super("HelpfulVillageCollection");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static HelpfulVillageCollection forWorld(World world) {
/*  38 */     if (world.isRemote) {
/*  39 */       return null;
/*     */     }
/*  41 */     MapStorage storage = world.getPerWorldStorage();
/*  42 */     HelpfulVillageCollection result = (HelpfulVillageCollection)storage.loadData(HelpfulVillageCollection.class, "HelpfulVillageCollection");
/*  43 */     if (result == null) {
/*  44 */       result = new HelpfulVillageCollection();
/*  45 */       storage.setData("HelpfulVillageCollection", result);
/*     */     } 
/*  47 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ArrayList<HelpfulVillage> getVillages() {
/*  54 */     return this.villageList;
/*     */   }
/*     */   
/*     */   public void setVillages(ArrayList<HelpfulVillage> villages) {
/*  58 */     this.villageList.clear();
/*  59 */     this.villageList.addAll(villages);
/*  60 */     markDirty();
/*     */   }
/*     */   
/*     */   public boolean isEmpty() {
/*  64 */     return this.villageList.isEmpty();
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
/*     */   public void writeToNBT(NBTTagCompound p_76187_1_) {
/*  76 */     NBTTagList nbttaglist = new NBTTagList();
/*  77 */     Iterator<HelpfulVillage> iterator = this.villageList.iterator();
/*     */     
/*  79 */     while (iterator.hasNext()) {
/*  80 */       HelpfulVillage village = iterator.next();
/*  81 */       if (!village.isAnnihilated) {
/*  82 */         NBTTagCompound nbttagcompound1 = new NBTTagCompound();
/*  83 */         village.writeVillageDataToNBT(nbttagcompound1);
/*  84 */         nbttaglist.appendTag((NBTBase)nbttagcompound1);
/*     */       } 
/*     */     } 
/*     */     
/*  88 */     p_76187_1_.setTag("Villages", (NBTBase)nbttaglist);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void readFromNBT(NBTTagCompound p_76184_1_) {
/*  99 */     NBTTagList nbttaglist = p_76184_1_.getTagList("Villages", 10);
/*     */     
/* 101 */     for (int i = 0; i < nbttaglist.tagCount(); i++) {
/* 102 */       NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
/* 103 */       HelpfulVillage village = new HelpfulVillage();
/* 104 */       village.readVillageDataFromNBT(nbttagcompound1);
/* 105 */       this.villageList.add(village);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              D:\Users\Joseph\Downloads\helpfulvillagers-1.7.10-1.4.0b5.jar!\mods\helpfulvillagers\village\HelpfulVillageCollection.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */