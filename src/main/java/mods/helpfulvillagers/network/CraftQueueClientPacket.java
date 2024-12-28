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
/*    */ import mods.helpfulvillagers.village.HelpfulVillage;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.multiplayer.WorldClient;
/*    */ import net.minecraft.item.ItemStack;
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
/*    */ public class CraftQueueClientPacket
/*    */   implements IMessage
/*    */ {
/*    */   int id;
/* 32 */   private ArrayList<CraftItem> craftQueue = new ArrayList<CraftItem>();
/*    */   
/*    */   private int queueSize;
/*    */ 
/*    */   
/*    */   public CraftQueueClientPacket(int id, ArrayList<CraftItem> craftQueue) {
/* 38 */     this.id = id;
/*    */     
/* 40 */     this.craftQueue.addAll(craftQueue);
/* 41 */     this.queueSize = craftQueue.size();
/*    */   }
/*    */ 
/*    */   
/*    */   public void toBytes(ByteBuf buffer) {
/* 46 */     buffer.writeInt(this.id);
/*    */     
/* 48 */     buffer.writeInt(this.queueSize);
/* 49 */     for (CraftItem i : this.craftQueue) {
/* 50 */       ByteBufUtils.writeItemStack(buffer, i.getItem());
/* 51 */       ByteBufUtils.writeUTF8String(buffer, i.getName());
/* 52 */       buffer.writeInt(i.getPriority());
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public CraftQueueClientPacket() {}
/*    */ 
/*    */ 
/*    */   
/*    */   public void fromBytes(ByteBuf buffer) {
/* 63 */     this.id = buffer.readInt();
/*    */     
/* 65 */     this.queueSize = buffer.readInt();
/* 66 */     for (int i = 0; i < this.queueSize; i++) {
/* 67 */       ItemStack item = ByteBufUtils.readItemStack(buffer);
/* 68 */       String name = ByteBufUtils.readUTF8String(buffer);
/* 69 */       int priority = buffer.readInt();
/* 70 */       this.craftQueue.add(new CraftItem(item, name, priority));
/*    */     } 
/*    */   }
/*    */   
/*    */   public static class Handler
/*    */     implements IMessageHandler<CraftQueueClientPacket, IMessage> {
/*    */     public IMessage onMessage(CraftQueueClientPacket message, MessageContext ctx) {
/*    */       try {
/* 78 */         if (ctx.side == Side.CLIENT) {
/* 79 */           Minecraft mc = Minecraft.getMinecraft();
/* 80 */           WorldClient world = mc.theWorld;
/* 81 */           AbstractVillager entity = (AbstractVillager)world.getEntityByID(message.id);
/* 82 */           if (entity == null) {
/* 83 */             return null;
/*    */           }
/*    */           
/* 86 */           if (entity.homeVillage == null) {
/* 87 */             entity.homeVillage = new HelpfulVillage();
/*    */           }
/*    */           
/* 90 */           entity.homeVillage.craftQueue = new CraftQueue(message.craftQueue);
/*    */         } 
/* 92 */       } catch (NullPointerException e) {}
/*    */ 
/*    */       
/* 95 */       return null;
/*    */     }
/*    */   }
/*    */ }


/* Location:              D:\Users\Joseph\Downloads\helpfulvillagers-1.7.10-1.4.0b5.jar!\mods\helpfulvillagers\network\CraftQueueClientPacket.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */