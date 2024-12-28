/*    */ package mods.helpfulvillagers.network;
/*    */ 
/*    */ import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.common.network.ByteBufUtils;
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import mods.helpfulvillagers.crafting.CraftItem;
/*    */ import mods.helpfulvillagers.entity.AbstractVillager;
/*    */ import mods.helpfulvillagers.main.HelpfulVillagers;
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
/*    */ 
/*    */ 
/*    */ public class CraftItemServerPacket
/*    */   implements IMessage
/*    */ {
/*    */   private int id;
/*    */   private CraftItem craftItem;
/*    */   private boolean craftNow;
/*    */   
/*    */   public CraftItemServerPacket() {}
/*    */   
/*    */   public CraftItemServerPacket(int id, CraftItem craftItem, boolean craftNow) {
/* 34 */     this.id = id;
/* 35 */     this.craftItem = craftItem;
/* 36 */     this.craftNow = craftNow;
/*    */   }
/*    */ 
/*    */   
/*    */   public void toBytes(ByteBuf buffer) {
/* 41 */     buffer.writeInt(this.id);
/*    */     
/* 43 */     ByteBufUtils.writeItemStack(buffer, this.craftItem.getItem());
/* 44 */     ByteBufUtils.writeUTF8String(buffer, this.craftItem.getName());
/* 45 */     buffer.writeInt(this.craftItem.getPriority());
/*    */     
/* 47 */     buffer.writeBoolean(this.craftNow);
/*    */   }
/*    */ 
/*    */   
/*    */   public void fromBytes(ByteBuf buffer) {
/* 52 */     this.id = buffer.readInt();
/*    */     
/* 54 */     ItemStack item = ByteBufUtils.readItemStack(buffer);
/* 55 */     String name = ByteBufUtils.readUTF8String(buffer);
/* 56 */     int priority = buffer.readInt();
/* 57 */     this.craftItem = new CraftItem(item, name, priority);
/*    */     
/* 59 */     this.craftNow = buffer.readBoolean();
/*    */   }
/*    */   
/*    */   public static class Handler
/*    */     implements IMessageHandler<CraftItemServerPacket, IMessage> {
/*    */     public IMessage onMessage(CraftItemServerPacket message, MessageContext ctx) {
/*    */       try {
/* 66 */         if (ctx.side == Side.SERVER) {
/* 67 */           World world = (ctx.getServerHandler()).playerEntity.worldObj;
/* 68 */           AbstractVillager entity = (AbstractVillager)world.getEntityByID(message.id);
/* 69 */           if (entity == null) {
/* 70 */             return null;
/*    */           }
/*    */           
/* 73 */           if (message.craftNow) {
/* 74 */             entity.currentCraftItem = message.craftItem;
/*    */           } else {
/* 76 */             entity.addCraftItem(message.craftItem);
/* 77 */             HelpfulVillagers.network.sendToAll(new CraftQueueClientPacket(message.id, entity.homeVillage.craftQueue.getAll()));
/*    */           } 
/*    */         } 
/* 80 */       } catch (NullPointerException e) {}
/*    */ 
/*    */       
/* 83 */       return null;
/*    */     }
/*    */   }
/*    */ }


/* Location:              D:\Users\Joseph\Downloads\helpfulvillagers-1.7.10-1.4.0b5.jar!\mods\helpfulvillagers\network\CraftItemServerPacket.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */