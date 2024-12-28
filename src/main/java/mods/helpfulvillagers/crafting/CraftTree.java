/*     */ package mods.helpfulvillagers.crafting;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import mods.helpfulvillagers.entity.AbstractVillager;
/*     */ import mods.helpfulvillagers.util.AIHelper;
/*     */ import net.minecraft.item.ItemStack;
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
/*     */ public class CraftTree
/*     */ {
/*     */   private Node root;
/*     */   private AbstractVillager villager;
/*     */   
/*     */   public CraftTree(ItemStack itemStack, AbstractVillager villager) {
/*  26 */     this.root = new Node();
/*  27 */     this.root.itemStack = itemStack;
/*  28 */     this.root.children = new ArrayList();
/*  29 */     this.root.inputs = new ArrayList();
/*  30 */     this.villager = villager;
/*  31 */     populateTree(this.root);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void populateTree(Node node) {
/*  42 */     VillagerRecipe recipe = this.villager.getRecipe(node.itemStack);
/*     */ 
/*     */     
/*  45 */     if (node.itemStack.getDisplayName().equals(this.villager.currentCraftItem.getItem().getDisplayName())) {
/*  46 */       this.villager.currentCraftItem.setSensitivity(recipe.getMetadataSensitivity());
/*     */     }
/*     */     
/*  49 */     if (recipe != null) {
/*     */ 
/*     */       
/*  52 */       if (recipe.isSmelted()) {
/*  53 */         AIHelper.mergeItemStackArrays(node.itemStack, this.villager.materialsNeeded);
/*     */       }
/*     */ 
/*     */       
/*  57 */       int multiplier = (int)Math.ceil(node.itemStack.stackSize / (recipe.getOutput()).stackSize);
/*  58 */       int leftover = (recipe.getOutput()).stackSize * multiplier - node.itemStack.stackSize;
/*  59 */       node.leftover = leftover;
/*  60 */       this.villager.craftChain.add(0, node);
/*     */       
/*  62 */       ArrayList<ItemStack> inputs = new ArrayList<ItemStack>();
/*     */ 
/*     */       
/*  65 */       for (ItemStack i : recipe.getTotalInputs()) {
/*  66 */         int stackSize = i.stackSize;
/*  67 */         int multipliedSize = stackSize * multiplier;
/*  68 */         int maxSize = i.getMaxStackSize();
/*  69 */         int numMax = multipliedSize / maxSize;
/*  70 */         for (int j = 0; j < numMax; j++) {
/*  71 */           inputs.add(new ItemStack(i.getItem(), maxSize));
/*  72 */           multipliedSize -= maxSize;
/*     */         } 
/*  74 */         if (multipliedSize > 0) {
/*  75 */           inputs.add(new ItemStack(i.getItem(), multipliedSize));
/*     */         }
/*     */       } 
/*     */       
/*  79 */       node.inputs.addAll(inputs);
/*     */ 
/*     */       
/*  82 */       for (ItemStack i : inputs) {
/*  83 */         boolean checkChests = true;
/*  84 */         ItemStack currItem = i.copy();
/*  85 */         this.villager.inventory.storeAsCollected(currItem, recipe.isSmelted());
/*  86 */         if (currItem != null && currItem.stackSize > 0) {
/*  87 */           this.villager.lookForItem(currItem);
/*  88 */           this.villager.inventory.storeAsCollected(currItem, recipe.isSmelted());
/*  89 */           if (currItem != null && currItem.stackSize > 0) {
/*  90 */             addChild(node, currItem.copy(), recipe.isSmelted());
/*     */           }
/*     */         } 
/*     */       } 
/*     */       
/*  95 */       for (Node n : node.children) {
/*  96 */         populateTree(n);
/*     */       }
/*     */     }
/*  99 */     else if (node.smelt) {
/* 100 */       AIHelper.mergeItemStackArrays(node.itemStack, this.villager.smeltablesNeeded);
/*     */     } else {
/* 102 */       AIHelper.mergeItemStackArrays(node.itemStack, this.villager.materialsNeeded);
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
/*     */   private void addChild(Node parent, ItemStack itemStack, boolean smelt) {
/* 114 */     Node child = new Node();
/* 115 */     child.itemStack = itemStack;
/* 116 */     child.smelt = smelt;
/* 117 */     child.parent = parent;
/* 118 */     child.children = new ArrayList();
/* 119 */     child.inputs = new ArrayList();
/* 120 */     child.parent.children.add(child);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void traverseTree() {
/* 127 */     this.root.traverseTree();
/*     */   }
/*     */ 
/*     */   
/*     */   public static class Node
/*     */   {
/*     */     private ItemStack itemStack;
/*     */     
/*     */     private boolean smelt;
/*     */     
/*     */     private int leftover;
/*     */     
/*     */     private Node parent;
/*     */     
/*     */     private List<Node> children;
/*     */     
/*     */     private List<ItemStack> inputs;
/*     */ 
/*     */     
/*     */     private void traverseTree() {
/* 147 */       System.out.println(this.itemStack);
/* 148 */       for (Node i : this.children) {
/* 149 */         i.traverseTree();
/*     */       }
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public ItemStack getItemStack() {
/* 157 */       return this.itemStack;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Node getParent() {
/* 164 */       return this.parent;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public ArrayList<ItemStack> getInputs() {
/* 171 */       return new ArrayList<ItemStack>(this.inputs);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int getLeftover() {
/* 178 */       return this.leftover;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean isSmelted() {
/* 185 */       return this.smelt;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String toString() {
/* 193 */       return this.itemStack.toString();
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\Users\Joseph\Downloads\helpfulvillagers-1.7.10-1.4.0b5.jar!\mods\helpfulvillagers\crafting\CraftTree.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */