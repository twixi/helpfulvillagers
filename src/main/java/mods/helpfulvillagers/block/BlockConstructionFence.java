/*    */ package mods.helpfulvillagers.block;
/*    */ 
/*    */ import mods.helpfulvillagers.tileentity.TileEntityContructionFence;
/*    */ import net.minecraft.block.BlockFence;
/*    */ import net.minecraft.block.ITileEntityProvider;
/*    */ import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
/*    */ //import net.minecraft.client.renderer.texture.IIconRegister;
/*    */ import net.minecraft.creativetab.CreativeTabs;
/*    */ import net.minecraft.entity.EntityLivingBase;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.item.ItemStack;
/*    */ import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
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
/*    */ 
/*    */ public class BlockConstructionFence
/*    */   extends BlockFence
/*    */   implements ITileEntityProvider
/*    */ {
/*    */   public BlockConstructionFence(String unlocalizedName, Material material) {
/* 32 */     super(material);
/* 33 */     setUnlocalizedName(unlocalizedName);
/* 34 */	//setTextureName("helpfulvillagers:" + unlocalizedName);
/* 35 */    setCreativeTab(CreativeTabs.tabDecorations);
/* 36 */    disableStats();
/* 37 */     setStepSound(soundTypeWood);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int p_149side, float p_14972subX, float p_14972subY, float p_14972subZ) {
/* 51 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public TileEntity createNewTileEntity(World worldIn, int meta) {
/* 56 */     return (TileEntity)new TileEntityContructionFence();
/*    */   }
/*    */ 
/*    */   
/*    */   public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase entity, ItemStack itemStack) {
/* 61 */     super.onBlockPlacedBy(world, pos, state, entity, itemStack);
/*    */     
/* 63 */     if (entity instanceof EntityPlayer) {
/* 64 */       EntityPlayer player = (EntityPlayer)entity;
/* 65 */       TileEntityContructionFence constructionFence = (TileEntityContructionFence)world.getTileEntity(pos);
/* 66 */       constructionFence.player = player.getCommandSenderEntity().getName();
/*    */     } 
/*    */   }
/*    */
}


/* Location:              D:\Users\Joseph\Downloads\helpfulvillagers-1.7.10-1.4.0b5.jar!\mods\helpfulvillagers\block\BlockConstructionFence.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */