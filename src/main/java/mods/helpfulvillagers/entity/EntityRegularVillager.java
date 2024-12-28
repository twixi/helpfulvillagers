/*    */ package mods.helpfulvillagers.entity;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.entity.EntityCreature;
/*    */ import net.minecraft.entity.ai.EntityAIAvoidEntity;
/*    */ import net.minecraft.entity.ai.EntityAIBase;
/*    */ import net.minecraft.entity.ai.EntityAIRestrictOpenDoor;
/*    */ import net.minecraft.entity.item.EntityItem;
/*    */ import net.minecraft.entity.monster.EntityZombie;
/*    */ import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.util.BlockPos;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class EntityRegularVillager
/*    */   extends AbstractVillager
/*    */ {
/*    */   private boolean dropFlag;
/*    */   
/*    */   public EntityRegularVillager(World world) {
/* 23 */     super(world);
/* 24 */     init();
/*    */   }
/*    */   
/*    */   public EntityRegularVillager(AbstractVillager villager) {
/* 28 */     super(villager);
/* 29 */     init();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private void init() {
/* 36 */     this.profession = 0;
/* 37 */     this.profName = "Villager";
/* 38 */     this.homeGuildHall = null;
/* 39 */     this.dropFlag = false;
/* 40 */     if (!this.worldObj.isRemote && this.homeVillage != null) {
			   BlockPos homePos0 = new BlockPos((this.homeVillage.getActualCenter()).getX(), (this.homeVillage.getActualCenter()).getY(), (this.homeVillage.getActualCenter()).getZ());
/* 41 */       setHomePosAndDistance(homePos0, (int)(this.homeVillage.getVillageRadius() / 0.6F));
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private void addThisAI() {
/* 49 */     ((PathNavigateGround)this.getNavigator()).setAvoidsWater(false);
/* 50 */     this.tasks.addTask(1, (EntityAIBase)new EntityAIAvoidEntity((EntityCreature)this, EntityZombie.class, 8.0F, 0.30000001192092896D, 0.3499999940395355D));
/* 51 */     this.tasks.addTask(3, (EntityAIBase)new EntityAIRestrictOpenDoor((EntityCreature)this));
/*    */   }
/*    */   
/*    */   public boolean shouldReturn() {
/* 55 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public void onUpdate() {
/* 60 */     super.onUpdate();
/* 61 */     if (!this.dropFlag) {
/* 62 */       if (getCurrentItem() != null) {
/* 63 */         if (!this.inventory.isFull()) {
/* 64 */           this.inventory.addItem(this.inventory.getCurrentItem());
/*    */         } else {
/* 66 */           EntityItem worldItem = new EntityItem(this.worldObj, this.posX, this.posY, this.posZ, getCurrentItem());
/* 67 */           this.worldObj.spawnEntityInWorld((Entity)worldItem);
/*    */         } 
/* 69 */         this.inventory.setCurrentItem(null);
/*    */       } 
/* 71 */       this.dropFlag = true;
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public ArrayList getValidCoords() {
/* 77 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isValidTool(ItemStack item) {
/* 82 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canCraft() {
/* 87 */     return false;
/*    */   }
/*    */ }


/* Location:              D:\Users\Joseph\Downloads\helpfulvillagers-1.7.10-1.4.0b5.jar!\mods\helpfulvillagers\entity\EntityRegularVillager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */