/*    */ package mods.helpfulvillagers.network;
/*    */ 
/*    */ import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.common.network.ByteBufUtils;
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import java.util.ArrayList;
/*    */ import mods.helpfulvillagers.crafting.CraftItem;
/*    */ import mods.helpfulvillagers.crafting.CraftQueue;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ public class CraftQueueServerPacket
/*    */   implements IMessage
/*    */ {
/*    */   int id;
/* 33 */   private ArrayList<CraftItem> craftQueue = new ArrayList<CraftItem>();
/*    */   
/*    */   private int queueSize;
/*    */ 
/*    */   
/*    */   public CraftQueueServerPacket(int id, ArrayList<CraftItem> craftQueue) {
/* 39 */     this.id = id;
/*    */     
/* 41 */     this.craftQueue.addAll(craftQueue);
/* 42 */     this.queueSize = craftQueue.size();
/*    */   }
/*    */ 
/*    */   
/*    */   public void toBytes(ByteBuf buffer) {
/* 47 */     buffer.writeInt(this.id);
/*    */     
/* 49 */     buffer.writeInt(this.queueSize);
/* 50 */     for (CraftItem i : this.craftQueue) {
/* 51 */       ByteBufUtils.writeItemStack(buffer, i.getItem());
/* 52 */       ByteBufUtils.writeUTF8String(buffer, i.getName());
/* 53 */       buffer.writeInt(i.getPriority());
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public CraftQueueServerPacket() {}
/*    */ 
/*    */ 
/*    */   
/*    */   public void fromBytes(ByteBuf buffer) {
/* 64 */     this.id = buffer.readInt();
/*    */     
/* 66 */     this.queueSize = buffer.readInt();
/* 67 */     for (int i = 0; i < this.queueSize; i++) {
/* 68 */       ItemStack item = ByteBufUtils.readItemStack(buffer);
/* 69 */       String name = ByteBufUtils.readUTF8String(buffer);
/* 70 */       int priority = buffer.readInt();
/* 71 */       this.craftQueue.add(new CraftItem(item, name, priority));
/*    */     } 
/*    */   }
/*    */   
/*    */   public static class Handler
/*    */     implements IMessageHandler<CraftQueueServerPacket, IMessage> {
/*    */     public IMessage onMessage(CraftQueueServerPacket message, MessageContext ctx) {
/*    */       try {
/* 79 */         if (ctx.side == Side.SERVER) {
/* 80 */           World world = (ctx.getServerHandler()).playerEntity.worldObj;
/* 81 */           AbstractVillager entity = (AbstractVillager)world.getEntityByID(message.id);
/* 82 */           if (entity == null) {
/* 83 */             return null;
/*    */           }
/*    */           
/* 86 */           if (entity.homeVillage == null) {
/* 87 */             System.out.println("PACKET ERROR: No Village");
/* 88 */             return null;
/*    */           } 
/*    */           
/* 91 */           entity.homeVillage.craftQueue = new CraftQueue(message.craftQueue);
/*    */           
/* 93 */           HelpfulVillagers.network.sendToAll(new CraftQueueClientPacket(message.id, message.craftQueue));
/*    */         } 
/* 95 */       } catch (NullPointerException e) {}
/*    */ 
/*    */       
/* 98 */       return null;
/*    */     }
/*    */   }
/*    */ }


/* Location:              D:\Users\Joseph\Downloads\helpfulvillagers-1.7.10-1.4.0b5.jar!\mods\helpfulvillagers\network\CraftQueueServerPacket.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */