/*    */ package mods.helpfulvillagers.network;
/*    */ 
/*    */ import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.common.network.ByteBufUtils;
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.inventory.ContainerPlayer;
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
/*    */ public class PlayerCraftMatrixResetPacket
/*    */   implements IMessage
/*    */ {
/*    */   private int id;
/*    */   
/*    */   public PlayerCraftMatrixResetPacket() {}
/*    */   
/*    */   public PlayerCraftMatrixResetPacket(int id) {
/* 28 */     this.id = id;
/*    */   }
/*    */   
/*    */   public void toBytes(ByteBuf buffer) {
/* 32 */     buffer.writeInt(this.id);
/*    */   }
/*    */   
/*    */   public void fromBytes(ByteBuf buffer) {
/* 36 */     this.id = buffer.readInt();
/*    */   }
/*    */   
/*    */   public static class Handler implements IMessageHandler<PlayerCraftMatrixResetPacket, IMessage> {
/*    */     public IMessage onMessage(PlayerCraftMatrixResetPacket message, MessageContext ctx) {
/*    */       try {
/* 42 */         if (ctx.side == Side.SERVER) {
/* 43 */           World world = (ctx.getServerHandler()).playerEntity.worldObj;
/* 44 */           EntityPlayer entity = (EntityPlayer)world.getEntityByID(message.id);
/* 45 */           if (entity == null) {
/* 46 */             return null;
/*    */           }
/*    */           
/* 49 */           if (entity.inventoryContainer instanceof ContainerPlayer) {
/* 50 */             ContainerPlayer container = (ContainerPlayer)entity.inventoryContainer;
/* 51 */             for (int i = 0; i < container.craftMatrix.getSizeInventory(); i++) {
/* 52 */               container.craftMatrix.setInventorySlotContents(i, null);
/*    */             }
/*    */           } 
/*    */         } 
/* 56 */       } catch (NullPointerException e) {}
/*    */ 
/*    */       
/* 59 */       return null;
/*    */     }
/*    */   }
/*    */ }


/* Location:              D:\Users\Joseph\Downloads\helpfulvillagers-1.7.10-1.4.0b5.jar!\mods\helpfulvillagers\network\PlayerCraftMatrixResetPacket.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */