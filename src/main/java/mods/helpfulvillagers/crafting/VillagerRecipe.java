/*     */ package mods.helpfulvillagers.crafting;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import mods.helpfulvillagers.util.AIHelper;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.item.crafting.IRecipe;
/*     */ import net.minecraft.item.crafting.ShapedRecipes;
/*     */ import net.minecraft.item.crafting.ShapelessRecipes;
/*     */ import net.minecraft.nbt.NBTBase;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.nbt.NBTTagList;
/*     */ import net.minecraftforge.oredict.ShapedOreRecipe;
/*     */ import net.minecraftforge.oredict.ShapelessOreRecipe;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class VillagerRecipe
/*     */   implements Comparable
/*     */ {
/*  26 */   private ArrayList<ItemStack> inputItems = new ArrayList<ItemStack>();
/*  27 */   private ArrayList<ItemStack> totalInputs = new ArrayList<ItemStack>();
/*     */   
/*     */   private ItemStack outputItem;
/*     */   
/*     */   private boolean smeltable;
/*     */   
/*     */   private boolean metadataSensitive = false;
/*     */ 
/*     */   
/*     */   public VillagerRecipe() {}
/*     */   
/*     */   public VillagerRecipe(IRecipe recipe, boolean smeltable) {
/*  39 */     this.smeltable = smeltable;
/*  40 */     this.metadataSensitive = false;
/*  41 */     if (recipe instanceof ShapedRecipes) {
/*  42 */       this.inputItems = new ArrayList<ItemStack>();
/*  43 */       ShapedRecipes shaped = (ShapedRecipes)recipe;
/*  44 */       for (int i = 0; i < shaped.recipeItems.length; i++) {
/*  45 */         if (shaped.recipeItems[i] != null) {
/*  46 */           this.inputItems.add(shaped.recipeItems[i]);
/*     */         }
/*     */       } 
/*  49 */       this.outputItem = recipe.getRecipeOutput();
/*  50 */     } else if (recipe instanceof ShapelessRecipes) {
/*     */       
/*     */       try {
/*  53 */         this.inputItems = new ArrayList<ItemStack>(((ShapelessRecipes)recipe).recipeItems);
/*  54 */         this.outputItem = recipe.getRecipeOutput();
/*  55 */       } catch (Exception e) {
/*  56 */         System.out.println("ERROR: Recipe Parsing failed for " + recipe.getRecipeOutput());
/*     */       } 
/*  58 */     } else if (recipe instanceof ShapedOreRecipe) {
/*  59 */       this.inputItems = new ArrayList<ItemStack>();
/*  60 */       ShapedOreRecipe shapedOre = (ShapedOreRecipe)recipe;
/*  61 */       for (int i = 0; i < (shapedOre.getInput()).length; i++) {
/*  62 */         if (shapedOre.getInput()[i] != null) {
/*  63 */           if (shapedOre.getInput()[i] instanceof ItemStack) {
/*  64 */             this.inputItems.add((ItemStack)shapedOre.getInput()[i]);
/*  65 */           } else if (shapedOre.getInput()[i] instanceof ArrayList) {
/*  66 */             ArrayList<ItemStack> array = (ArrayList)shapedOre.getInput()[i];
/*  67 */             if (array.size() > 0) {
/*  68 */               this.inputItems.add(array.get(0));
/*     */             }
/*     */           } else {
/*  71 */             System.out.println("ERROR: Unknown Input Item");
/*     */           } 
/*     */         }
/*     */       } 
/*  75 */       this.outputItem = recipe.getRecipeOutput();
/*  76 */     } else if (recipe instanceof ShapelessOreRecipe) {
/*  77 */       ShapelessOreRecipe shapelessOre = (ShapelessOreRecipe)recipe;
/*  78 */       for (int i = 0; i < shapelessOre.getInput().size(); i++) {
/*  79 */         if (shapelessOre.getInput().get(i) != null) {
/*  80 */           if (shapelessOre.getInput().get(i) instanceof ItemStack) {
/*  81 */             this.inputItems.add((ItemStack) shapelessOre.getInput().get(i));
/*  82 */           } else if (shapelessOre.getInput().get(i) instanceof ArrayList) {
/*  83 */             ArrayList<ItemStack> array = (ArrayList<ItemStack>) shapelessOre.getInput().get(i);
/*  84 */             for (int j = 0; j < array.size(); j++) {
/*  85 */               if (array.get(j) instanceof ItemStack) {
/*  86 */                 this.inputItems.add(array.get(j));
/*     */               }
/*     */             } 
/*     */           } else {
/*  90 */             System.out.println("ERROR: Unknown Input Item");
/*     */           } 
/*     */         }
/*     */       } 
/*  94 */       this.outputItem = recipe.getRecipeOutput();
/*     */     } else {
/*  96 */       this.inputItems = null;
/*  97 */       this.outputItem = null;
/*  98 */       System.out.println("ERROR: Unknown Recipe for " + recipe.getRecipeOutput());
/*     */     } 
/*     */     
/* 101 */     this.totalInputs = initTotalInputs();
/* 102 */     initMetadata();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public VillagerRecipe(ArrayList<ItemStack> inputs, ItemStack output, boolean smeltable) {
/* 113 */     this.smeltable = smeltable;
/* 114 */     this.outputItem = output.copy();
/* 115 */     this.inputItems.addAll(inputs);
/* 116 */     this.totalInputs.addAll(inputs);
/* 117 */     initMetadata();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public VillagerRecipe(ItemStack input, ItemStack output, boolean smeltable) {
/* 126 */     this.smeltable = smeltable;
/* 127 */     this.outputItem = output.copy();
/* 128 */     this.inputItems.add(input.copy());
/* 129 */     this.totalInputs.add(input.copy());
/* 130 */     initMetadata();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void initMetadata() {
/* 138 */     this.metadataSensitive = this.outputItem.getHasSubtypes();
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
/*     */   private ArrayList<ItemStack> initTotalInputs() {
/* 164 */     ArrayList<ItemStack> totals = new ArrayList<ItemStack>();
/* 165 */     ArrayList<ItemStack> temp = new ArrayList<ItemStack>();
/*     */     
/* 167 */     for (ItemStack i : this.inputItems) {
/* 168 */       ItemStack newItem = i.copy();
/* 169 */       newItem.stackSize = 1;
/* 170 */       temp.add(newItem);
/*     */     } 
/*     */     
/* 173 */     AIHelper.mergeItemStackArrays(temp, totals);
/* 174 */     return totals;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ArrayList<ItemStack> getInputs() {
/* 181 */     return this.inputItems;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ArrayList<ItemStack> getTotalInputs() {
/* 188 */     return new ArrayList<ItemStack>(this.totalInputs);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ItemStack getOutput() {
/* 195 */     return this.outputItem.copy();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getMetadataSensitivity() {
/* 202 */     return this.metadataSensitive;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isSmelted() {
/* 209 */     return this.smeltable;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List getTooltip() {
/* 217 */     List<String> list = new ArrayList();
/* 218 */     String s = this.outputItem.getDisplayName();
/*     */     
/* 220 */     if (this.smeltable) {
/* 221 */       s = s + " (Smelt)";
/*     */     }
/*     */     
/* 224 */     list.add(s);
/* 225 */     list.add("");
/* 226 */     for (ItemStack i : getTotalInputs()) {
/* 227 */       s = i.getDisplayName() + " x" + i.stackSize;
/* 228 */       list.add(s);
/*     */     } 
/* 230 */     return list;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 238 */     String s = "";
/* 239 */     if (this.outputItem == null) {
/* 240 */       s = s + "null";
/*     */     } else {
/* 242 */       s = s + this.outputItem.toString();
/*     */     } 
/*     */     
/* 245 */     s = s + " <-";
/*     */     
/* 247 */     s = s + getTotalInputs().toString();
/* 248 */     return s;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int compareTo(Object o) {
/* 258 */     if (o instanceof VillagerRecipe) {
/* 259 */       VillagerRecipe v = (VillagerRecipe)o;
/* 260 */       return getOutput().getDisplayName().compareTo(v.getOutput().getDisplayName());
/*     */     } 
/* 262 */     return Integer.MAX_VALUE;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/* 271 */     if (o == null) {
/* 272 */       return false;
/*     */     }
/*     */     
/* 275 */     if (o instanceof VillagerRecipe) {
/* 276 */       VillagerRecipe v = (VillagerRecipe)o;
/* 277 */       if (this.smeltable == v.smeltable) {
/* 278 */         if (this.outputItem.getDisplayName().equals(v.outputItem.getDisplayName())) {
/* 279 */           ArrayList<ItemStack> temp1 = new ArrayList<ItemStack>(this.totalInputs);
/* 280 */           ArrayList<ItemStack> temp2 = new ArrayList<ItemStack>(v.totalInputs);
/*     */           
/* 282 */           if (temp1.size() == temp2.size()) {
/* 283 */             Iterator<ItemStack> i = temp1.iterator();
/* 284 */             while (i.hasNext()) {
/* 285 */               ItemStack itemI = i.next();
/* 286 */               for (int j = 0; j < temp2.size(); j++) {
/* 287 */                 ItemStack itemJ = temp2.get(j);
/* 288 */                 if (itemI.getDisplayName().equals(itemJ.getDisplayName()) && itemI.stackSize == itemJ.stackSize) {
/* 289 */                   i.remove();
/* 290 */                   temp2.remove(j);
/*     */                   break;
/*     */                 } 
/*     */               } 
/*     */             } 
/* 295 */             return (temp1.size() <= 0 && temp2.size() <= 0);
/*     */           } 
/* 297 */           return false;
/*     */         } 
/*     */         
/* 300 */         return false;
/*     */       } 
/*     */       
/* 303 */       return false;
/*     */     } 
/*     */     
/* 306 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public NBTTagList writeToNBT(NBTTagList par1NBTTagList) {
/* 316 */     NBTTagCompound nbttagcompound = new NBTTagCompound();
/* 317 */     nbttagcompound.setBoolean("Smelt", this.smeltable);
/* 318 */     this.outputItem.writeToNBT(nbttagcompound);
/* 319 */     par1NBTTagList.appendTag((NBTBase)nbttagcompound);
/*     */     
/* 321 */     for (ItemStack i : this.totalInputs) {
/* 322 */       nbttagcompound = new NBTTagCompound();
/* 323 */       i.writeToNBT(nbttagcompound);
/* 324 */       par1NBTTagList.appendTag((NBTBase)nbttagcompound);
/*     */     } 
/*     */     
/* 327 */     return par1NBTTagList;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void readFromNBT(NBTTagList par1NBTTagList) {
/* 335 */     for (int i = 0; i < par1NBTTagList.tagCount(); i++) {
/* 336 */       NBTTagCompound nbttagcompound = par1NBTTagList.getCompoundTagAt(i);
/* 337 */       if (i == 0) {
/* 338 */         this.smeltable = nbttagcompound.getBoolean("Smelt");
/* 339 */         this.outputItem = ItemStack.loadItemStackFromNBT(nbttagcompound);
/*     */       } else {
/* 341 */         this.totalInputs.add(ItemStack.loadItemStackFromNBT(nbttagcompound));
/*     */       } 
/*     */     } 
/* 344 */     initMetadata();
/*     */   }
/*     */ }


/* Location:              D:\Users\Joseph\Downloads\helpfulvillagers-1.7.10-1.4.0b5.jar!\mods\helpfulvillagers\crafting\VillagerRecipe.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */