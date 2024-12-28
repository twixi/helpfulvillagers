/*    */ package mods.helpfulvillagers.network;
/*    */ 
/*    */ import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.common.network.ByteBufUtils;
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import mods.helpfulvillagers.entity.EntityFishHookCustom;
/*    */ import mods.helpfulvillagers.entity.EntityFisherman;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.multiplayer.WorldClient;
/*    */ import net.minecraft.entity.Entity;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class FishHookPacket
/*    */   implements IMessage
/*    */ {
/*    */   private int id;
/*    */   private boolean dead;
/*    */   private int x;
/*    */   private int y;
/*    */   private int z;
/*    */   
/*    */   public FishHookPacket() {}
/*    */   
/*    */   public FishHookPacket(int id, boolean dead, int x, int y, int z) {
/* 32 */     this.id = id;
/* 33 */     this.dead = dead;
/* 34 */     this.x = x;
/* 35 */     this.y = y;
/* 36 */     this.z = z;
/*    */   }
/*    */ 
/*    */   
/*    */   public void toBytes(ByteBuf buffer) {
/* 41 */     buffer.writeInt(this.id);
/* 42 */     buffer.writeBoolean(this.dead);
/* 43 */     buffer.writeInt(this.x);
/* 44 */     buffer.writeInt(this.y);
/* 45 */     buffer.writeInt(this.z);
/*    */   }
/*    */ 
/*    */   
/*    */   public void fromBytes(ByteBuf buffer) {
/* 50 */     this.id = buffer.readInt();
/* 51 */     this.dead = buffer.readBoolean();
/* 52 */     this.x = buffer.readInt();
/* 53 */     this.y = buffer.readInt();
/* 54 */     this.z = buffer.readInt();
/*    */   }
/*    */   
/*    */   public static class Handler
/*    */     implements IMessageHandler<FishHookPacket, IMessage> {
/*    */     public IMessage onMessage(FishHookPacket message, MessageContext ctx) {
/*    */       try {
/* 61 */         if (ctx.side == Side.CLIENT) {
/* 62 */           Minecraft mc = Minecraft.getMinecraft();
/* 63 */           WorldClient world = mc.theWorld;
/* 64 */           EntityFisherman fisherman = (EntityFisherman)world.getEntityByID(message.id);
/* 65 */           if (message.dead) {
/* 66 */             fisherman.fishEntity.setDead();
/* 67 */             fisherman.fishEntity = null;
/*    */           } else {
/* 69 */             fisherman.fishEntity = new EntityFishHookCustom(fisherman.worldObj, message.x, message.y, message.z, fisherman);
/* 70 */             fisherman.worldObj.spawnEntityInWorld((Entity)fisherman.fishEntity);
/*    */           } 
/*    */         } 
/* 73 */       } catch (NullPointerException e) {}
/*    */ 
/*    */       
/* 76 */       return null;
/*    */     }
/*    */   }
/*    */ }


/* Location:              D:\Users\Joseph\Downloads\helpfulvillagers-1.7.10-1.4.0b5.jar!\mods\helpfulvillagers\network\FishHookPacket.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */