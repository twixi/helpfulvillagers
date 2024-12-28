/*    */ package mods.helpfulvillagers.entity;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import mods.helpfulvillagers.enums.EnumActivity;
/*    */ import net.minecraft.entity.EntityCreature;
/*    */ import net.minecraft.entity.ai.EntityAIAvoidEntity;
/*    */ import net.minecraft.entity.ai.EntityAIBase;
/*    */ import net.minecraft.entity.ai.EntityAIRestrictOpenDoor;
/*    */ import net.minecraft.entity.monster.EntityZombie;
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
/*    */ public class EntityMerchant
/*    */   extends AbstractVillager
/*    */ {
/*    */   public EntityMerchant(World world) {
/* 24 */     super(world);
/* 25 */     init();
/*    */   }
/*    */   
/*    */   public EntityMerchant(AbstractVillager villager) {
/* 29 */     super(villager);
/* 30 */     init();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private void init() {
/* 37 */     this.profession = 6;
/* 38 */     this.profName = "Merchant";
/* 39 */     this.currentActivity = EnumActivity.IDLE;
/* 40 */     this.searchRadius = 10;
/* 41 */     getNewGuildHall();
/* 42 */     addThisAI();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private void addThisAI() {
/* 49 */     ((PathNavigateGround)this.getNavigator()).setAvoidsWater(false);
/*    */     
/* 51 */     this.tasks.addTask(1, (EntityAIBase)new EntityAIAvoidEntity((EntityCreature)this, EntityZombie.class, 8.0F, 0.30000001192092896D, 0.3499999940395355D));
/* 52 */     this.tasks.addTask(3, (EntityAIBase)new EntityAIRestrictOpenDoor((EntityCreature)this));
/*    */   }
/*    */ 
/*    */   
/*    */   public ArrayList getValidCoords() {
/* 57 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isValidTool(ItemStack item) {
/* 62 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canCraft() {
/* 67 */     return false;
/*    */   }
/*    */ }


/* Location:              D:\Users\Joseph\Downloads\helpfulvillagers-1.7.10-1.4.0b5.jar!\mods\helpfulvillagers\entity\EntityMerchant.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */