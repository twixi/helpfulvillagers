/*     */ package mods.helpfulvillagers.econ;
/*     */ 
/*     */ import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Random;
/*     */ import java.util.Set;
/*     */ import mods.helpfulvillagers.crafting.VillagerRecipe;
/*     */ import mods.helpfulvillagers.entity.AbstractVillager;
/*     */ import mods.helpfulvillagers.main.HelpfulVillagers;
/*     */ import mods.helpfulvillagers.network.ItemPriceClientPacket;
/*     */ import mods.helpfulvillagers.network.ItemPriceServerPacket;
/*     */ import mods.helpfulvillagers.village.HelpfulVillage;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.entity.player.EntityPlayerMP;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.item.crafting.CraftingManager;
/*     */ import net.minecraft.item.crafting.FurnaceRecipes;
/*     */ import net.minecraft.item.crafting.IRecipe;
/*     */ import net.minecraft.nbt.NBTBase;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.BlockPos;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class VillageEconomy
/*     */ {
/*     */   private HelpfulVillage village;
/*  40 */   private HashMap<String, ArrayList<VillagerRecipe>> recipeMap = new HashMap<String, ArrayList<VillagerRecipe>>();
/*  41 */   private HashMap<String, ItemPrice> itemPrices = new HashMap<String, ItemPrice>();
/*  42 */   private HashMap<String, ItemStack> searchMap = new HashMap<String, ItemStack>();
/*     */   
/*  44 */   private HashMap<String, Integer> accountMap = new HashMap<String, Integer>();
/*  45 */   private int lowestWoodPrice = Integer.MAX_VALUE;
/*     */ 
/*     */ 
/*     */   
/*     */   public VillageEconomy(HelpfulVillage village, boolean init) {
/*  50 */     this.village = village;
/*  51 */     if (init) {
/*  52 */       initPrices();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void initPrices() {
/*  61 */     this.village.priceCalcStarted = true;
/*  62 */     final HashMap<ItemStack, Integer> itemMap = new HashMap<ItemStack, Integer>();
/*  63 */     Thread pricesThread = new Thread()
/*     */       {
/*     */         
/*     */         public void run()
/*     */         {
/*  68 */           ArrayList<Item> outputs = new ArrayList<Item>();
/*  69 */           List<IRecipe> recipes = CraftingManager.getInstance().getRecipeList();
/*  70 */           for (int i = 0; i < recipes.size(); i++) {
/*  71 */             ItemStack outputStack = ((IRecipe)recipes.get(i)).getRecipeOutput();
/*  72 */             if (outputStack != null) {
/*  73 */               Item outputItem = outputStack.getItem();
/*  74 */               if (!outputs.contains(outputItem)) {
/*  75 */                 outputs.add(outputItem);
/*     */               }
/*     */             } 
/*     */           } 
/*     */           
/*  80 */           for (int y = 0; y < VillageEconomy.this.village.villageBounds.maxY; y++) {
/*  81 */             for (int x = (int)VillageEconomy.this.village.villageBounds.minX; x < VillageEconomy.this.village.villageBounds.maxX; x++) {
/*  82 */               for (int z = (int)VillageEconomy.this.village.villageBounds.minZ; z < VillageEconomy.this.village.villageBounds.maxZ; z++) {
						  BlockPos blockpos0 = new BlockPos(x, y, z);
						  BlockPos origin = new BlockPos(0, 0, 0);
/*  83 */                 Block block = VillageEconomy.this.village.world.getBlockState(blockpos0).getBlock();
/*  84 */                 List<ItemStack> items = block.getDrops(VillageEconomy.this.village.world, origin, VillageEconomy.this.village.world.getBlockState(blockpos0), 0);
/*  85 */                 if (block.canSilkHarvest(VillageEconomy.this.village.world, blockpos0, VillageEconomy.this.village.world.getBlockState(blockpos0), null)) {
/*  86 */                   ItemStack blockStack = new ItemStack(Item.getItemFromBlock(block), 1);
/*  87 */                   items.add(blockStack);
/*     */                 } 
/*  89 */                 for (ItemStack item : items) {
/*     */                   try {
/*  91 */                     if (item.getDisplayName().contains("Bedrock")) {
/*     */                       continue;
/*     */                     }
/*  94 */                   } catch (NullPointerException e) {
/*     */                     continue;
/*     */                   } 
/*     */                   
/*  98 */                   if (outputs.contains(item.getItem())) {
/*     */                     continue;
/*     */                   }
/*     */                   
/* 102 */                   if (!outputs.contains(item.getItem())) {
/* 103 */                     boolean found = false;
/* 104 */                     for (ItemStack mapItem : itemMap.keySet()) {
/* 105 */                       if (ItemStack.areItemStacksEqual(mapItem, item)) {
/* 106 */                         int val = ((Integer)itemMap.get(mapItem)).intValue();
/* 107 */                         itemMap.put(mapItem, Integer.valueOf(val + 1));
/* 108 */                         found = true;
/*     */                       } 
/*     */                     } 
/*     */                     
/* 112 */                     if (!found) {
/* 113 */                       itemMap.put(item, Integer.valueOf(1));
/*     */                     }
/*     */                   } 
/*     */                 } 
/*     */               } 
/*     */             } 
/*     */           } 
/*     */           
/* 121 */           int total = 0;
/* 122 */           int highestAmount = 0;
/* 123 */           int lowestAmount = Integer.MAX_VALUE;
/*     */           
/* 125 */           for (ItemStack item : itemMap.keySet()) {
/* 126 */             int amount = ((Integer)itemMap.get(item)).intValue();
/* 127 */             if (amount > highestAmount) {
/* 128 */               highestAmount = amount;
/*     */             }
/*     */             
/* 131 */             if (amount < lowestAmount) {
/* 132 */               lowestAmount = amount;
/*     */             }
/*     */           } 
/*     */           
/* 136 */           for (Map.Entry<ItemStack, Integer> entry : (Iterable<Map.Entry<ItemStack, Integer>>)itemMap.entrySet()) {
/* 137 */             int price = VillageEconomy.this.calcItemValue(highestAmount, lowestAmount, 100, 1, ((Integer)entry.getValue()).intValue());
/* 138 */             VillageEconomy.this.itemPrices.put(((ItemStack)entry.getKey()).getDisplayName(), new ItemPrice(entry.getKey(), price));
/*     */             
/* 140 */             if (price > 0) {
/* 141 */               Block block = Block.getBlockFromItem(((ItemStack)entry.getKey()).getItem());
/* 142 */               if (block instanceof net.minecraft.block.BlockLog && price < VillageEconomy.this.lowestWoodPrice) {
/* 143 */                 VillageEconomy.this.lowestWoodPrice = price;
/*     */               }
/*     */             } 
/*     */           } 
/*     */           
/* 148 */           ArrayList<ItemStack> outputStacks = new ArrayList<ItemStack>();
/* 149 */           for (Item item1 : outputs) {
/* 150 */             if (item1 == null) {
/*     */               continue;
/*     */             }
/*     */             
/* 154 */             if (item1.getHasSubtypes()) {
/* 155 */               ArrayList<String> names = new ArrayList<String>();
/* 156 */               for (int j = 0; j < 64; j++) {
/* 157 */                 ItemStack itemStack = new ItemStack(item1, 1, j);
/*     */                 try {
/* 159 */                   if (names.contains(itemStack.getDisplayName())) {
/*     */                     break;
/*     */                   }
/* 162 */                   names.add(itemStack.getDisplayName());
/* 163 */                   if (!VillageEconomy.this.itemPrices.containsKey(itemStack.getDisplayName())) {
/* 164 */                     outputStacks.add(itemStack);
/*     */                   }
/*     */                 }
/* 167 */                 catch (Exception e) {
/* 168 */                   if (VillageEconomy.this.itemPrices.containsKey(itemStack.getUnlocalizedName())) {
/* 169 */                     itemStack = new ItemStack(item1, 1, 0);
/* 170 */                     outputStacks.add(itemStack);
/*     */                   }  break;
/*     */                 } 
/*     */               } 
/*     */               continue;
/*     */             } 
/* 176 */             ItemStack item = new ItemStack(item1, 1, 0);
/* 177 */             if (!VillageEconomy.this.itemPrices.containsKey(item.getDisplayName())) {
/* 178 */               outputStacks.add(item);
/*     */             }
/*     */           } 
/*     */ 
/*     */           
/* 183 */           VillageEconomy.this.setupRecipes();
/* 184 */           for (ItemStack item : outputStacks) {
/* 185 */             VillageEconomy.this.calcValueFromRecipe(item);
/*     */           }
/*     */           
/* 188 */           ItemStack emerald = new ItemStack(Items.emerald);
/* 189 */           if (VillageEconomy.this.itemPrices.containsKey(emerald.getDisplayName())) {
/* 190 */             VillageEconomy.this.itemPrices.remove(emerald.getDisplayName());
/*     */           }
/*     */           
/* 193 */           if (VillageEconomy.this.itemPrices.containsKey((new ItemStack(Items.leather)).getDisplayName())) {
/* 194 */             VillageEconomy.this.itemPrices.put((new ItemStack(Items.saddle)).getDisplayName(), new ItemPrice(new ItemStack(Items.saddle), ((ItemPrice)VillageEconomy.this.itemPrices.get(new ItemStack(Items.leather))).getOriginalPrice() * 5));
/*     */           }
/*     */           
/* 197 */           ItemStack wool = new ItemStack(Blocks.wool);
/* 198 */           ItemStack iron = new ItemStack(Items.iron_ingot);
/* 199 */           ItemStack gold = new ItemStack(Items.gold_ingot);
/* 200 */           ItemStack diamond = new ItemStack(Items.diamond);
/*     */           
/* 202 */           if (VillageEconomy.this.itemPrices.containsKey(iron.getDisplayName())) {
/* 203 */             ItemStack ironArmor = new ItemStack(Items.iron_horse_armor);
/* 204 */             VillageEconomy.this.itemPrices.put(ironArmor.getDisplayName(), new ItemPrice(ironArmor, ((ItemPrice)VillageEconomy.this.itemPrices.get(iron.getDisplayName())).getOriginalPrice() * 6));
/*     */           } 
/*     */           
/* 207 */           if (VillageEconomy.this.itemPrices.containsKey(gold.getDisplayName())) {
/* 208 */             ItemStack goldArmor = new ItemStack(Items.golden_horse_armor);
/* 209 */             VillageEconomy.this.itemPrices.put(goldArmor.getDisplayName(), new ItemPrice(goldArmor, ((ItemPrice)VillageEconomy.this.itemPrices.get(gold.getDisplayName())).getOriginalPrice() * 6));
/*     */           } 
/*     */           
/* 212 */           if (VillageEconomy.this.itemPrices.containsKey(diamond.getDisplayName())) {
/* 213 */             ItemStack diamondArmor = new ItemStack(Items.diamond_horse_armor);
/* 214 */             VillageEconomy.this.itemPrices.put(diamondArmor.getDisplayName(), new ItemPrice(diamondArmor, ((ItemPrice)VillageEconomy.this.itemPrices.get(diamond.getDisplayName())).getOriginalPrice() * 6));
/*     */           } 
/*     */           
/* 217 */           VillageEconomy.this.village.pricesCalculated = true;
/*     */         }
/*     */       };
/*     */ 
/*     */ 
/*     */     
/* 223 */     pricesThread.start();
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
/*     */   private int calcItemValue(int highestAmount, int lowestAmount, int maxPrice, int minPrice, int amount) {
/* 237 */     double x1 = Math.log(lowestAmount);
/* 238 */     double x2 = Math.log(highestAmount);
/* 239 */     double y1 = Math.log(maxPrice);
/* 240 */     double y2 = Math.log(minPrice);
/*     */     
/* 242 */     double slopeNum = (x1 - x2) * (y1 - y2);
/* 243 */     double slopeDen = (x1 - x2) * (x1 - x2);
/* 244 */     double slope = slopeNum / slopeDen;
/*     */     
/* 246 */     int result = (int)(100.0D * Math.pow(amount, slope));
/*     */     
/* 248 */     return Math.max(result, 1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void setupRecipes() {
/* 255 */     List<IRecipe> recipes = HelpfulVillagers.vanillaRecipes;
/*     */ 
/*     */     
/* 258 */     for (int i = 0; i < recipes.size(); i++) {
/* 259 */       ItemStack outputItem = ((IRecipe)recipes.get(i)).getRecipeOutput();
/* 260 */       if (outputItem != null) {
/*     */         VillagerRecipe recipe = null; try {
/* 262 */           recipe = new VillagerRecipe(recipes.get(i), false);
/* 263 */         } catch (Exception e) {}
/*     */ 
/*     */         
/* 266 */         if (recipe.getOutput() != null) {
/* 267 */           if (!this.recipeMap.containsKey(outputItem.getDisplayName())) {
/* 268 */             ArrayList<VillagerRecipe> vRecipes = new ArrayList<VillagerRecipe>();
/* 269 */             vRecipes.add(recipe);
/* 270 */             this.recipeMap.put(outputItem.getDisplayName(), vRecipes);
/*     */           } else {
/* 272 */             ((ArrayList<VillagerRecipe>)this.recipeMap.get(outputItem.getDisplayName())).add(recipe);
/*     */           } 
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 278 */     Map map = FurnaceRecipes.instance().getSmeltingList();
/* 279 */     Set<Map.Entry> entrySet = map.entrySet();
/* 280 */     for (Map.Entry entry : entrySet) {
/* 281 */       ItemStack outputItem = (ItemStack)entry.getValue();
/*     */       
/* 283 */       VillagerRecipe recipe = new VillagerRecipe((ItemStack)entry.getKey(), outputItem, true);
/* 284 */       if (recipe.getOutput() != null) {
/* 285 */         if (!this.recipeMap.containsKey(outputItem.getDisplayName())) {
/* 286 */           ArrayList<VillagerRecipe> vRecipes = new ArrayList<VillagerRecipe>();
/* 287 */           vRecipes.add(recipe);
/* 288 */           this.recipeMap.put(outputItem.getDisplayName(), vRecipes); continue;
/*     */         } 
/* 290 */         ((ArrayList<VillagerRecipe>)this.recipeMap.get(outputItem.getDisplayName())).add(recipe);
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
/*     */   private int calcValueFromRecipe(ItemStack i) {
/* 303 */     if (this.itemPrices.containsKey(i.getDisplayName())) {
/* 304 */       Block block = Block.getBlockFromItem(i.getItem());
/* 305 */       if (block instanceof net.minecraft.block.BlockLog) {
/* 306 */         return this.lowestWoodPrice;
/*     */       }
/* 308 */       return ((ItemPrice)this.itemPrices.get(i.getDisplayName())).getPrice();
/*     */     } 
/*     */     
/* 311 */     if (!this.recipeMap.containsKey(i.getDisplayName())) {
/* 312 */       return -1;
/*     */     }
/*     */     
/* 315 */     this.searchMap.put(i.getDisplayName(), i);
/*     */     
/* 317 */     int price = 0;
/* 318 */     int lowestPrice = Integer.MAX_VALUE;
/* 319 */     ArrayList<VillagerRecipe> recipes = this.recipeMap.get(i.getDisplayName());
/* 320 */     for (VillagerRecipe recipe : recipes) {
/* 321 */       for (ItemStack stack : recipe.getTotalInputs()) {
/* 322 */         if (this.searchMap.containsKey(stack.getDisplayName())) {
/* 323 */           price = 0;
/*     */           break;
/*     */         } 
/* 326 */         int val = calcValueFromRecipe(stack) * stack.stackSize;
/* 327 */         if (val < 0) {
/* 328 */           price = -1;
/*     */           break;
/*     */         } 
/* 331 */         price += val;
/*     */       } 
/*     */       
/* 334 */       if (price > 0) {
/* 335 */         if ((recipe.getOutput()).stackSize > 0) {
/* 336 */           price /= (recipe.getOutput()).stackSize;
/*     */         }
/*     */         
/* 339 */         if (price <= 0) {
/* 340 */           price = 1;
/*     */         }
/*     */         
/* 343 */         if (price < lowestPrice) {
/* 344 */           lowestPrice = price;
/*     */         }
/*     */       } 
/*     */       
/* 348 */       price = 0;
/*     */     } 
/*     */     
/* 351 */     this.searchMap.remove(i.getDisplayName());
/* 352 */     if (lowestPrice < Integer.MAX_VALUE) {
/* 353 */       this.itemPrices.put(i.getDisplayName(), new ItemPrice(i, lowestPrice));
/* 354 */       return lowestPrice;
/*     */     } 
/*     */     
/* 357 */     return -1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HashMap<String, ItemPrice> getItemPrices() {
/* 365 */     return this.itemPrices;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getPrice(String name) {
/* 373 */     ItemPrice itemPrice = this.itemPrices.get(name);
/* 374 */     if (itemPrice != null) {
/* 375 */       return itemPrice.getPrice();
/*     */     }
/* 377 */     return -1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ItemPrice getItemPrice(String name) {
/* 386 */     return this.itemPrices.get(name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void putItemPrice(ItemPrice itemPrice) {
/* 394 */     this.itemPrices.put(itemPrice.getItem().getDisplayName(), itemPrice);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasItem(ItemStack item) {
/* 402 */     return this.itemPrices.containsKey(item.getDisplayName());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void accountDeposit(EntityPlayer player, int amount) {
/* 411 */     String username = player.getName();
/* 412 */     if (this.accountMap.containsKey(username)) {
/* 413 */       int currAmount = ((Integer)this.accountMap.get(username)).intValue();
/* 414 */       this.accountMap.put(username, Integer.valueOf(currAmount + amount));
/*     */     } else {
/* 416 */       this.accountMap.put(username, Integer.valueOf(amount));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int accountWithdraw(EntityPlayer player, int amount) {
/* 427 */     String username = player.getName();
/* 428 */     if (this.accountMap.containsKey(username)) {
/* 429 */       int currAmount = ((Integer)this.accountMap.get(username)).intValue();
/* 430 */       if (currAmount >= amount) {
/* 431 */         this.accountMap.put(username, Integer.valueOf(currAmount - amount));
/* 432 */         return amount;
/*     */       } 
/* 434 */       return -1;
/*     */     } 
/* 436 */     return -1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getAccount(EntityPlayer player) {
/* 444 */     String username = player.getName();
/* 445 */     if (this.accountMap.containsKey(username)) {
/* 446 */       return ((Integer)this.accountMap.get(username)).intValue();
/*     */     }
/* 448 */     return -1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAccount(EntityPlayer player, int amount) {
/* 457 */     this.accountMap.put(player.getName(), Integer.valueOf(amount));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void decreaseAllDemand() {
/* 465 */     for (Map.Entry<String, ItemPrice> entry : this.itemPrices.entrySet()) {
/* 466 */       ((ItemPrice)entry.getValue()).decreaseDemand(0.005D);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void increaseItemSupply(AbstractVillager villager, ItemStack item) {
/* 476 */     ItemPrice itemPrice = this.itemPrices.get(item.getDisplayName());
/*     */     
/* 478 */     if (itemPrice != null) {
/* 479 */       itemPrice.increaseSupply(item.stackSize);
/*     */     } else {
/* 481 */       generateNewPrice(item);
/* 482 */       itemPrice = this.itemPrices.get(item.getDisplayName());
/* 483 */       itemPrice.increaseSupply(item.stackSize);
/*     */     } 
/*     */     
/*     */     try {
/* 487 */       if (!this.village.world.isRemote) {
/* 488 */         itemSyncClient(villager, null, item);
/*     */       }
/* 490 */     } catch (NullPointerException e) {}
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void decreaseItemSupply(AbstractVillager villager, ItemStack item) {
/* 501 */     ItemPrice itemPrice = this.itemPrices.get(item.getDisplayName());
/* 502 */     if (itemPrice != null) {
/* 503 */       itemPrice.decreaseSupply(item.stackSize);
/*     */     } else {
/* 505 */       generateNewPrice(item);
/* 506 */       itemPrice = this.itemPrices.get(item.getDisplayName());
/* 507 */       itemPrice.decreaseSupply(item.stackSize);
/*     */     } 
/*     */     
/*     */     try {
/* 511 */       if (!this.village.world.isRemote) {
/* 512 */         itemSyncClient(villager, null, item);
/*     */       }
/* 514 */     } catch (NullPointerException e) {}
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void generateNewPrice(ItemStack item) {
/* 525 */     if (item == null) {
/*     */       return;
/*     */     }
/*     */     
/* 529 */     Random rand = new Random();
/* 530 */     int sum = 0;
/* 531 */     int highest = Integer.MIN_VALUE;
/*     */     
/* 533 */     for (Map.Entry<String, ItemPrice> entry : this.itemPrices.entrySet()) {
/* 534 */       if (entry != null && entry.getValue() != null) {
/* 535 */         int price = ((ItemPrice)entry.getValue()).getPrice();
/* 536 */         sum += price;
/*     */         
/* 538 */         if (price > highest) {
/* 539 */           highest = price;
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 544 */     int denom = 1;
/* 545 */     if (this.itemPrices.size() > 0) {
/* 546 */       denom = this.itemPrices.size();
/*     */     }
/* 548 */     int average = sum / denom;
/* 549 */     int range = (highest - average) / 2;
/* 550 */     int newPrice = rand.nextInt(range) + average;
/*     */     
/* 552 */     this.itemPrices.put(item.getDisplayName(), new ItemPrice(item, newPrice));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void fullSyncClient(AbstractVillager villager, EntityPlayer player) {
/* 561 */     if (player == null) {
/* 562 */       for (Map.Entry<String, ItemPrice> entry : this.itemPrices.entrySet()) {
/* 563 */         HelpfulVillagers.network.sendToAll((IMessage)new ItemPriceClientPacket(villager, entry.getValue()));
/*     */       }
/*     */     } else {
/* 566 */       for (Map.Entry<String, ItemPrice> entry : this.itemPrices.entrySet()) {
/* 567 */         HelpfulVillagers.network.sendTo((IMessage)new ItemPriceClientPacket(villager, entry.getValue()), (EntityPlayerMP)player);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void fullSyncServer(AbstractVillager villager) {
/* 577 */     for (Map.Entry<String, ItemPrice> entry : this.itemPrices.entrySet()) {
/* 578 */       HelpfulVillagers.network.sendToServer((IMessage)new ItemPriceServerPacket(villager, entry.getValue()));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void itemSyncClient(AbstractVillager villager, EntityPlayer player, ItemStack item) {
/* 589 */     if (player == null) {
/* 590 */       HelpfulVillagers.network.sendToAll((IMessage)new ItemPriceClientPacket(villager, this.itemPrices.get(item.getDisplayName())));
/*     */     } else {
/* 592 */       HelpfulVillagers.network.sendTo((IMessage)new ItemPriceClientPacket(villager, this.itemPrices.get(item.getDisplayName())), (EntityPlayerMP)player);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void itemSyncServer(AbstractVillager villager, ItemStack item) {
/* 602 */     HelpfulVillagers.network.sendToServer((IMessage)new ItemPriceServerPacket(villager, this.itemPrices.get(item.getDisplayName())));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public NBTBase writeToNBT(NBTTagList nbtTagList) {
/* 611 */     for (Map.Entry<String, ItemPrice> entry : this.itemPrices.entrySet()) {
/* 612 */       if (entry.getKey() == null || entry.getValue() == null) {
/*     */         continue;
/*     */       }
/*     */       
/* 616 */       NBTTagCompound nbttagcompound = new NBTTagCompound();
/* 617 */       nbttagcompound.setString("Name", entry.getKey());
/* 618 */       nbttagcompound.setTag("Item", ((ItemPrice)entry.getValue()).writeToNBT(new NBTTagCompound()));
/* 619 */       nbtTagList.appendTag((NBTBase)nbttagcompound);
/*     */     } 
/*     */     
/* 622 */     for (Map.Entry<String, Integer> entry : this.accountMap.entrySet()) {
/* 623 */       if (entry.getKey() == null || entry.getValue() == null) {
/*     */         continue;
/*     */       }
/*     */       
/* 627 */       NBTTagCompound nbttagcompound = new NBTTagCompound();
/* 628 */       nbttagcompound.setString("Player", entry.getKey());
/* 629 */       nbttagcompound.setInteger("Amount", ((Integer)entry.getValue()).intValue());
/* 630 */       nbtTagList.appendTag((NBTBase)nbttagcompound);
/*     */     } 
/*     */     
/* 633 */     return (NBTBase)nbtTagList;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void readFromNBT(NBTTagList nbttaglist) {
/* 641 */     for (int i = 0; i < nbttaglist.tagCount(); i++) {
/* 642 */       NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
/* 643 */       if (nbttagcompound.hasKey("Name")) {
/* 644 */         String name = nbttagcompound.getString("Name");
/* 645 */         NBTTagCompound priceCompound = nbttagcompound.getCompoundTag("Item");
/* 646 */         ItemPrice itemPrice = ItemPrice.loadCraftItemFromNBT(priceCompound);
/* 647 */         this.itemPrices.put(name, itemPrice);
/*     */       } else {
/* 649 */         String player = nbttagcompound.getString("Player");
/* 650 */         int amount = nbttagcompound.getInteger("Amount");
/* 651 */         this.accountMap.put(player, Integer.valueOf(amount));
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public VillageEconomy() {}
/*     */ }


/* Location:              D:\Users\Joseph\Downloads\helpfulvillagers-1.7.10-1.4.0b5.jar!\mods\helpfulvillagers\econ\VillageEconomy.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */