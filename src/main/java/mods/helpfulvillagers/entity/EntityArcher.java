/*     */ package mods.helpfulvillagers.entity;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import mods.helpfulvillagers.ai.EntityAIGuardVillageArcher;
/*     */ import mods.helpfulvillagers.enums.EnumActivity;
/*     */ import net.minecraft.entity.ai.EntityAIBase;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.PathNavigateGround;
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
/*     */ public class EntityArcher
/*     */   extends AbstractVillager
/*     */ {
/*  28 */   private final ItemStack[] archerTools = new ItemStack[] { new ItemStack((Item)Items.bow) };
/*     */ 
/*     */ 
/*     */   
/*  32 */   public final int ARROW_TIME = 20;
/*     */   
/*     */   public EntityArcher(World world) {
/*  35 */     super(world);
/*  36 */     init();
/*     */   }
/*     */   
/*     */   public EntityArcher(AbstractVillager villager) {
/*  40 */     super(villager);
/*  41 */     init();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void init() {
/*  48 */     this.profession = 5;
/*  49 */     this.profName = "Archer";
/*  50 */     this.currentActivity = EnumActivity.IDLE;
/*  51 */     this.searchRadius = 5;
/*  52 */     setTools(this.archerTools);
/*  53 */     getNewGuildHall();
/*  54 */     addThisAI();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addThisAI() {
/*  61 */     ((PathNavigateGround)this.getNavigator()).setAvoidsWater(false);
/*     */     
/*  63 */     this.tasks.addTask(2, (EntityAIBase)new EntityAIGuardVillageArcher(this));
/*     */   }
/*     */   
/*     */   public boolean shouldReturn() {
/*  67 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void func_onUpdate() {
/*  72 */     super.onUpdate();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isFullyArmored() {
/*  79 */     for (int i = 28; i < 32; i++) {
/*  80 */       ItemStack armorPiece = this.inventory.getStackInSlot(i);
/*  81 */       if (armorPiece == null) {
/*  82 */         return false;
/*     */       }
/*     */     } 
/*  85 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public ArrayList getValidCoords() {
/*  90 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isValidTool(ItemStack item) {
/*  95 */     return item.getItem() instanceof net.minecraft.item.ItemBow;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canCraft() {
/* 100 */     return false;
/*     */   }
/*     */ }


/* Location:              D:\Users\Joseph\Downloads\helpfulvillagers-1.7.10-1.4.0b5.jar!\mods\helpfulvillagers\entity\EntityArcher.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */