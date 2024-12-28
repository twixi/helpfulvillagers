/*     */ package mods.helpfulvillagers.gui;
/*     */ 
/*     */ import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import mods.helpfulvillagers.crafting.VillagerRecipe;
/*     */ import mods.helpfulvillagers.entity.AbstractVillager;
/*     */ import mods.helpfulvillagers.main.HelpfulVillagers;
/*     */ import mods.helpfulvillagers.network.AddRecipePacket;
/*     */ import mods.helpfulvillagers.network.ResetRecipesPacket;
/*     */ import mods.helpfulvillagers.util.AIHelper;
/*     */ import net.minecraft.client.gui.GuiButton;
/*     */ import net.minecraft.client.gui.inventory.GuiContainer;
/*     */ import net.minecraft.client.resources.I18n;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.inventory.Container;
/*     */ import net.minecraft.inventory.ContainerWorkbench;
/*     */ import net.minecraft.inventory.IInventory;
/*     */ import net.minecraft.inventory.Slot;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import net.minecraft.world.World;
/*     */ import org.lwjgl.opengl.GL11;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class GuiTeachRecipe
/*     */   extends GuiContainer
/*     */ {
/*  39 */   private static final ResourceLocation craftingTableGuiTextures = new ResourceLocation("textures/gui/container/crafting_table.png");
/*  40 */   private final int xSizeOfTexture = 176;
/*  41 */   private final int ySizeOfTexture = 166;
/*  42 */   private final int MAX_DISPLAY_TIME = 120;
/*     */   
/*     */   private GuiButton teachButton;
/*     */   
/*     */   private GuiButton resetButton;
/*     */   
/*     */   private GuiButton backButton;
/*     */   
/*     */   private GuiButton yesButton;
/*     */   
/*     */   private GuiButton noButton;
/*     */   
/*     */   private GuiButton replaceButton;
/*     */   
/*     */   private GuiButton deleteButton;
/*     */   private GuiButton cancelButton;
/*     */   private EntityPlayer player;
/*     */   private AbstractVillager villager;
/*     */   private String displayText;
/*     */   private int displayTime;
/*     */   private static int popupNum;
/*  63 */   private List tempSlots = new ArrayList();
/*     */   
/*     */   public GuiTeachRecipe(EntityPlayer player, AbstractVillager villager) {
/*  66 */     super((Container)new VillagerContainerWorkbench(player));
/*  67 */     this.player = player;
/*  68 */     this.villager = villager;
/*  69 */     this.displayText = null;
/*  70 */     this.displayTime = 0;
/*  71 */     popupNum = -1;
/*     */   }
/*     */ 
/*     */   
/*     */   public void initGui() {
/*  76 */     super.initGui();
/*  77 */     int posX = (this.width - 176) / 2;
/*  78 */     int posY = (this.height - 166) / 2;
/*     */     
/*  80 */     this.teachButton = new GuiButton(0, posX + 90, posY + 60, 80, 20, "Teach Recipe");
/*  81 */     this.buttonList.add(this.teachButton);
/*  82 */     this.teachButton.enabled = false;
/*     */     
/*  84 */     this.resetButton = new GuiButton(1, posX + 90, posY + 5, 80, 20, "Reset Recipes");
/*  85 */     this.buttonList.add(this.resetButton);
/*     */     
/*  87 */     this.backButton = new GuiButton(2, posX + 6, posY + 33, 20, 20, "<-");
/*  88 */     this.buttonList.add(this.backButton);
/*     */     
/*  90 */     this.yesButton = new GuiButton(3, posX + 45, posY + 115, 40, 20, "Yes");
/*  91 */     this.buttonList.add(this.yesButton);
/*  92 */     this.yesButton.visible = false;
/*  93 */     this.yesButton.enabled = false;
/*     */     
/*  95 */     this.noButton = new GuiButton(4, posX + 95, posY + 115, 40, 20, "No");
/*  96 */     this.buttonList.add(this.noButton);
/*  97 */     this.noButton.visible = false;
/*  98 */     this.noButton.enabled = false;
/*     */     
/* 100 */     this.replaceButton = new GuiButton(5, posX + 15, posY + 115, 45, 20, "Replace");
/* 101 */     this.buttonList.add(this.replaceButton);
/* 102 */     this.replaceButton.visible = false;
/* 103 */     this.replaceButton.enabled = false;
/*     */     
/* 105 */     this.deleteButton = new GuiButton(6, posX + 65, posY + 115, 45, 20, "Delete");
/* 106 */     this.buttonList.add(this.deleteButton);
/* 107 */     this.deleteButton.visible = false;
/* 108 */     this.deleteButton.enabled = false;
/*     */     
/* 110 */     this.cancelButton = new GuiButton(7, posX + 115, posY + 115, 45, 20, "Cancel");
/* 111 */     this.buttonList.add(this.cancelButton);
/* 112 */     this.cancelButton.visible = false;
/* 113 */     this.cancelButton.enabled = false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void func_73updateScreen() {
/* 118 */     super.updateScreen();
/* 119 */     if (VillagerContainerWorkbench.output != null) {
/* 120 */       VillagerRecipe newRecipe = new VillagerRecipe(VillagerContainerWorkbench.inputs, VillagerContainerWorkbench.output, false);
/* 121 */       if (this.villager.knownRecipes.contains(newRecipe) && !this.villager.customRecipes.contains(newRecipe)) {
/* 122 */         this.teachButton.enabled = false;
/*     */       } else {
/* 124 */         this.teachButton.enabled = (popupNum < 0);
/*     */       } 
/*     */     } else {
/* 127 */       this.teachButton.enabled = false;
/*     */     } 
/*     */     
/* 130 */     this.resetButton.enabled = (popupNum < 0 && this.villager.customRecipes.size() > 0);
/* 131 */     this.backButton.enabled = (popupNum < 0);
/*     */     
/* 133 */     this.yesButton.visible = (popupNum == 0);
/* 134 */     this.yesButton.enabled = (popupNum == 0);
/* 135 */     this.noButton.visible = (popupNum == 0);
/* 136 */     this.noButton.enabled = (popupNum == 0);
/*     */     
/* 138 */     this.replaceButton.visible = (popupNum == 1);
/* 139 */     this.replaceButton.enabled = (popupNum == 1);
/* 140 */     this.deleteButton.visible = (popupNum == 1);
/* 141 */     this.deleteButton.enabled = (popupNum == 1);
/* 142 */     this.cancelButton.visible = (popupNum == 1);
/* 143 */     this.cancelButton.enabled = (popupNum == 1);
/*     */   }
/*     */ 
/*     */   
/*     */   public void func_73drawScreen(int x, int y, float f) {
/* 148 */     super.drawScreen(x, y, f);
/*     */ 
/*     */     
/* 151 */     for (int i = 0; i < this.buttonList.size(); i++) {
/* 152 */       if (this.buttonList.get(i) instanceof GuiButton) {
/* 153 */         GuiButton btn = this.buttonList.get(i);
/* 154 */         if (btn.isMouseOver() && !btn.enabled) {
/* 155 */           if (btn.id == 0 && VillagerContainerWorkbench.output != null && popupNum < 0) {
/* 156 */             String[] desc = { "Cannot Change Profession Recipe" };
/*     */             
/* 158 */             List<String> temp = Arrays.asList(desc);
/* 159 */             drawHoveringText(temp, x, y, this.fontRendererObj);
/* 160 */           } else if (btn.id == 1 && popupNum < 0) {
/* 161 */             String[] desc = { "No Custom Recipes Found" };
/*     */             
/* 163 */             List<String> temp = Arrays.asList(desc);
/* 164 */             drawHoveringText(temp, x, y, this.fontRendererObj);
/*     */           } 
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void actionPerformed(GuiButton button) {
/*     */     VillagerRecipe newRecipe;
/*     */     Iterator<Slot> iterator1;
/*     */     Iterator<Slot> i;
/* 178 */     switch (button.id) {
/*     */       case 0:
/* 180 */         newRecipe = new VillagerRecipe(VillagerContainerWorkbench.inputs, VillagerContainerWorkbench.output, false);
/* 181 */         if (!this.villager.canCraft(VillagerContainerWorkbench.output)) {
/* 182 */           this.villager.addCustomRecipe(newRecipe);
/* 183 */           HelpfulVillagers.network.sendToServer((IMessage)new AddRecipePacket(this.villager.getEntityId(), newRecipe, 0));
/* 184 */           displayText("Recipe Added"); break;
/*     */         } 
/* 186 */         popupNum = 1;
/* 187 */         this.tempSlots.clear();
/* 188 */         this.tempSlots.addAll(this.inventorySlots.inventorySlots);
/* 189 */         iterator1 = this.inventorySlots.inventorySlots.iterator();
/* 190 */         while (iterator1.hasNext()) {
/* 191 */           Slot slot = iterator1.next();
/* 192 */           if (slot.slotNumber >= 10 && slot.slotNumber <= 36) {
/* 193 */             iterator1.remove();
/*     */           }
/*     */         } 
/*     */         break;
/*     */ 
/*     */       
/*     */       case 1:
/* 200 */         popupNum = 0;
/* 201 */         this.tempSlots.clear();
/* 202 */         this.tempSlots.addAll(this.inventorySlots.inventorySlots);
/* 203 */         i = this.inventorySlots.inventorySlots.iterator();
/* 204 */         while (i.hasNext()) {
/* 205 */           Slot slot = i.next();
/* 206 */           if (slot.slotNumber >= 10 && slot.slotNumber <= 36) {
/* 207 */             i.remove();
/*     */           }
/*     */         } 
/*     */         break;
/*     */       
/*     */       case 2:
/* 213 */         this.player.openGui(HelpfulVillagers.instance, 4, this.villager.worldObj, this.villager.getEntityId(), 0, 0);
/*     */         break;
/*     */       
/*     */       case 3:
/* 217 */         this.villager.resetRecipes();
/* 218 */         HelpfulVillagers.network.sendToServer((IMessage)new ResetRecipesPacket(this.villager.getEntityId()));
/* 219 */         displayText("Recipes Reset");
/* 220 */         popupNum = -1;
/* 221 */         this.inventorySlots.inventorySlots.clear();
/* 222 */         this.inventorySlots.inventorySlots.addAll(this.tempSlots);
/*     */         break;
/*     */       
/*     */       case 4:
/* 226 */         popupNum = -1;
/* 227 */         this.inventorySlots.inventorySlots.clear();
/* 228 */         this.inventorySlots.inventorySlots.addAll(this.tempSlots);
/*     */         break;
/*     */       
/*     */       case 5:
/* 232 */         newRecipe = new VillagerRecipe(VillagerContainerWorkbench.inputs, VillagerContainerWorkbench.output, false);
/* 233 */         this.villager.replaceCustomRecipe(newRecipe);
/* 234 */         HelpfulVillagers.network.sendToServer((IMessage)new AddRecipePacket(this.villager.getEntityId(), newRecipe, 1));
/* 235 */         displayText("Recipe Replaced");
/*     */         
/* 237 */         popupNum = -1;
/* 238 */         this.inventorySlots.inventorySlots.clear();
/* 239 */         this.inventorySlots.inventorySlots.addAll(this.tempSlots);
/*     */         break;
/*     */       
/*     */       case 6:
/* 243 */         newRecipe = new VillagerRecipe(VillagerContainerWorkbench.inputs, VillagerContainerWorkbench.output, false);
/* 244 */         this.villager.deleteCustomRecipe(newRecipe);
/* 245 */         HelpfulVillagers.network.sendToServer((IMessage)new AddRecipePacket(this.villager.getEntityId(), newRecipe, 2));
/* 246 */         displayText("Recipe Deleted");
/*     */         
/* 248 */         popupNum = -1;
/* 249 */         this.inventorySlots.inventorySlots.clear();
/* 250 */         this.inventorySlots.inventorySlots.addAll(this.tempSlots);
/*     */         break;
/*     */       
/*     */       case 7:
/* 254 */         popupNum = -1;
/* 255 */         this.inventorySlots.inventorySlots.clear();
/* 256 */         this.inventorySlots.inventorySlots.addAll(this.tempSlots);
/*     */         break;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void displayText(String text) {
/* 266 */     this.displayText = text;
/* 267 */     this.displayTime = 0;
/*     */   }
/*     */   
/*     */   protected void drawGuiContainerForegroundLayer (int mouseX, int mouseY) {
/* 271 */     this.fontRendererObj.drawString(I18n.format("container.crafting", new Object[0]), 28, 6, 4210752);
/* 272 */     this.fontRendererObj.drawString(I18n.format("container.inventory", new Object[0]), 8, this.ySize - 96 + 2, 4210752);
/*     */     
/* 274 */     if (this.displayText != null) {
/* 275 */       drawCenteredString(this.fontRendererObj, this.displayText, 90, -10, 16777215);
/* 276 */       this.displayTime++;
/* 277 */       if (this.displayTime > 120) {
/* 278 */         this.displayText = null;
/* 279 */         this.displayTime = 0;
/*     */       } 
/*     */     } 
/*     */     
/* 283 */     if (popupNum == 0) {
/* 284 */      drawCenteredString(this.fontRendererObj, "Delete All Custom Recipes?", 90, 90, 16777215);
/* 285 */     } else if (popupNum == 1) {
/* 286 */      drawCenteredString(this.fontRendererObj, "Recipe Already Known", 90, 90, 16777215);
/* 287 */      drawCenteredString(this.fontRendererObj, "Replace Recipe?", 90, 100, 16777215);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void drawGuiContainerBackgroundLayer (float partialTicks, int mouseX, int p_146mouseY) {
/* 292 */     GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
/* 293 */     this.mc.getTextureManager().bindTexture(craftingTableGuiTextures);
/* 294 */     int posX = (this.width - this.xSize) / 2;
/* 295 */     int posY = (this.height - this.ySize) / 2;
/* 296 */     drawTexturedModalRect(posX, posY, 0, 0, this.xSize, this.ySize);
/*     */     
/* 298 */     if (popupNum >= 0) {
/* 299 */       this.mc.renderEngine.bindTexture(new ResourceLocation("helpfulvillagers", "textures/gui/dialog_background.png"));
/* 300 */       drawTexturedModalRect(posX + 7, posY + 83, 10, 10, 162, 54);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static class VillagerContainerWorkbench
/*     */     extends ContainerWorkbench
/*     */   {
/*     */     private World worldObj;
/*     */     
/*     */     private static ItemStack output;
/*     */     
/* 313 */     private static ArrayList<ItemStack> inputs = new ArrayList<ItemStack>();
				private static BlockPos pos0 = new BlockPos(0, 0, 0);
/*     */     
/*     */     public VillagerContainerWorkbench(EntityPlayer player) {
				
/* 316 */       super(player.inventory, player.worldObj, pos0);
				
/* 317 */       this.worldObj = player.worldObj;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canInteractWith(EntityPlayer player) {
/* 322 */       return true;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onCraftMatrixChanged(IInventory p_75130_1_) {
/* 327 */    craftResult.setInventorySlotContents(0, CraftingManager.getInstance().findMatchingRecipe(this.craftMatrix, this.worldObj));
/* 328 */       output = this.craftResult.getStackInSlot(0);
/* 329 */       inputs.clear();
/* 330 */       for (int i = 0; i < 3; i++) {
/* 331 */         for (int j = 0; j < 3; j++) {
/* 332 */           ItemStack item = this.craftMatrix.getStackInRowAndColumn(i, j);
/* 333 */           if (item != null) {
/* 334 */             AIHelper.mergeItemStackArrays(new ItemStack(item.getItem(), 1), inputs);
/*     */           }
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public ItemStack slotClick(int slotId, int clickedButton, int mode, EntityPlayer player) {
/* 342 */       if (GuiTeachRecipe.popupNum >= 0) {
/* 343 */         return null;
/*     */       }
/* 345 */       return super.slotClick(slotId, clickedButton, mode, player);
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\Users\Joseph\Downloads\helpfulvillagers-1.7.10-1.4.0b5.jar!\mods\helpfulvillagers\gui\GuiTeachRecipe.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */