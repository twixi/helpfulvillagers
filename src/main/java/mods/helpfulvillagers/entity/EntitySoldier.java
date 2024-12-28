/*    */ package mods.helpfulvillagers.entity;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import mods.helpfulvillagers.ai.EntityAIGuardVillageSoldier;
/*    */ import mods.helpfulvillagers.enums.EnumActivity;
/*    */ import net.minecraft.entity.ai.EntityAIBase;
/*    */ import net.minecraft.init.Items;
/*    */ import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.PathNavigateGround;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class EntitySoldier
/*    */   extends AbstractVillager
/*    */ {
/* 25 */   private final ItemStack[] soldierTools = new ItemStack[] { new ItemStack(Items.diamond_sword), new ItemStack(Items.golden_sword), new ItemStack(Items.iron_sword), new ItemStack(Items.stone_sword), new ItemStack(Items.wooden_sword) };
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public EntitySoldier(World world) {
/* 34 */     super(world);
/* 35 */     init();
/*    */   }
/*    */   
/*    */   public EntitySoldier(AbstractVillager villager) {
/* 39 */     super(villager);
/* 40 */     init();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private void init() {
/* 47 */     this.profession = 4;
/* 48 */     this.profName = "Soldier";
/* 49 */     this.currentActivity = EnumActivity.IDLE;
/* 50 */     setTools(this.soldierTools);
/* 51 */     getNewGuildHall();
/* 52 */     addThisAI();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void addThisAI() {
/* 59 */     ((PathNavigateGround)this.getNavigator()).setAvoidsWater(false);
/*    */     
/* 61 */     this.tasks.addTask(2, (EntityAIBase)new EntityAIGuardVillageSoldier(this));
/*    */   }
/*    */   
/*    */   public boolean shouldReturn() {
/* 65 */     return false;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isFullyArmored() {
/* 72 */     for (int i = 28; i < 32; i++) {
/* 73 */       ItemStack armorPiece = this.inventory.getStackInSlot(i);
/* 74 */       if (armorPiece == null) {
/* 75 */         return false;
/*    */       }
/*    */     } 
/* 78 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public void onUpdate() {
/* 83 */     super.onUpdate();
/*    */   }
/*    */ 
/*    */   
/*    */   public ArrayList getValidCoords() {
/* 88 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isValidTool(ItemStack item) {
/* 93 */     return item.getItem() instanceof net.minecraft.item.ItemSword;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canCraft() {
/* 98 */     return false;
/*    */   }
/*    */ }


/* Location:              D:\Users\Joseph\Downloads\helpfulvillagers-1.7.10-1.4.0b5.jar!\mods\helpfulvillagers\entity\EntitySoldier.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */