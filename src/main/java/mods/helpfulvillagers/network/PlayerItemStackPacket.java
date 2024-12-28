/*    */ package mods.helpfulvillagers.network;
/*    */ 
/*    */ import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.common.network.ByteBufUtils;
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.item.ItemStack;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class PlayerItemStackPacket
/*    */   implements IMessage
/*    */ {
/*    */   private int id;
/*    */   private ItemStack stack;
/*    */   
/*    */   public PlayerItemStackPacket() {}
/*    */   
/*    */   public PlayerItemStackPacket(int id, ItemStack stack) {
/* 29 */     this.id = id;
/* 30 */     this.stack = stack;
/*    */   }
/*    */   
/*    */   public void toBytes(ByteBuf buffer) {
/* 34 */     buffer.writeInt(this.id);
/* 35 */     ByteBufUtils.writeItemStack(buffer, this.stack);
/*    */   }
/*    */   
/*    */   public void fromBytes(ByteBuf buffer) {
/* 39 */     this.id = buffer.readInt();
/* 40 */     this.stack = ByteBufUtils.readItemStack(buffer);
/*    */   }
/*    */   
/*    */   public static class Handler implements IMessageHandler<PlayerItemStackPacket, IMessage> {
/*    */     public IMessage onMessage(PlayerItemStackPacket message, MessageContext ctx) {
/*    */       try {
/* 46 */         if (ctx.side == Side.SERVER) {
/* 47 */           World world = (ctx.getServerHandler()).playerEntity.worldObj;
/* 48 */           EntityPlayer entity = (EntityPlayer)world.getEntityByID(message.id);
/* 49 */           if (entity == null) {
/* 50 */             return null;
/*    */           }
/*    */           
/* 53 */           entity.inventory.setItemStack(message.stack);
/*    */         } 
/* 55 */       } catch (NullPointerException e) {}
/*    */ 
/*    */       
/* 58 */       return null;
/*    */     }
/*    */   }
/*    */ }


/* Location:              D:\Users\Joseph\Downloads\helpfulvillagers-1.7.10-1.4.0b5.jar!\mods\helpfulvillagers\network\PlayerItemStackPacket.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */