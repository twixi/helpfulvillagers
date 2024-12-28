/*    */ package mods.helpfulvillagers.block;
/*    */ 
/*    */ import net.minecraft.block.material.Material;
/*    */ //import net.minecraft.client.renderer.texture.IIconRegister;
/*    */ 
/*    */ 
/*    */ public class BlockActiveConstructionFence
/*    */   extends BlockConstructionFence
/*    */ {
/*    */   public BlockActiveConstructionFence(String unlocalizedName, Material material) {
/* 11 */     super(unlocalizedName, material);
/* 12 */     //setTextureName("helpfulvillagers:construction_fence");
/* 13 */    setBlockUnbreakable();
/* 14 */	setCreativeTab(null);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
///*    */   public void registerIcons(IIconRegister reg) {
///* 21 */     this.blockIcon = reg.func_942registerIcon("helpfulvillagers:construction_fence");
///*    */   }
/*    */ }


/* Location:              D:\Users\Joseph\Downloads\helpfulvillagers-1.7.10-1.4.0b5.jar!\mods\helpfulvillagers\block\BlockActiveConstructionFence.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */