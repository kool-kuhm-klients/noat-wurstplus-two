package me.travis.wurstplus.wurstplustwo.hacks.combat;

import me.travis.wurstplus.wurstplustwo.guiscreen.settings.WurstplusSetting;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusHack;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusCategory;
import me.travis.wurstplus.wurstplustwo.util.*;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

public class WurstplusBurrow extends WurstplusHack {

    public WurstplusBurrow() {
      super(WurstplusCategory.WURSTPLUS_COMBAT);
      this.name        = "Burrow";
      this.tag         = "Burrow";
      this.description = "ifarticuhm was requesting for this one :^)";
    }

    WurstplusSetting enderchest = create("Ender Chest", "BurrowEnderChest", true);
    WurstplusSetting rotate = create("Rotate", "BurrowRotate", false);
    WurstplusSetting silent = create("Silent", "Silent", true);
    WurstplusSetting height = create ("Height", "Height", 3, 0, 30);

    private BlockPos originalPos;
    private int oldSlot = -1;
	  private final Minecraft mc = Minecraft.getMinecraft();
    private float oldTickLength = mc.timer.tickLength;

    // original author: obsidianbreaker
    @Override
    public void enable() {
      this.enable();
      if (mc.player == null || mc.world == null) return;

      final int oldSlot = mc.player.inventory.currentItem;
      oldTickLength = mc.timer.tickLength;
      BlockPos originalPos = new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ);

      if (mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ)).getBlock().equals(Blocks.OBSIDIAN) || mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ)).getBlock().equals(Blocks.ENDER_CHEST)) {
          WurstplusMessageUtil.send_client_message("you idiot, you are already in an obsidian or echests");
          this.set_disable();
          return;
      } else if (this.isInterceptedByOther(originalPos)) {
          WurstplusMessageUtil.send_client_message("you are intercepted by a fucking entity get out of there");
          this.set_disable();
          return;
      } else if (getHotbarSlot(Blocks.OBSIDIAN) == -1 && getHotbarSlot(Blocks.ENDER_CHEST) == -1) {
          WurstplusMessageUtil.send_client_message("you don't even have a fucking obsidian or an enderchest");
          this.set_disable();
          return;
      } else if (!mc.world.getBlockState(originalPos.add(0, 1, 0)).getBlock().equals(Blocks.AIR) || !mc.world.getBlockState(originalPos.add(0, 2, 0)).getBlock().equals(Blocks.AIR) || !mc.world.getBlockState(originalPos.add(0, 3, 0)).getBlock().equals(Blocks.AIR)) {
          WurstplusMessageUtil.send_client_error_message("free more space you dum dum");
          this.set_disable();
          return;
      } else if (mc.isSingleplayer()) {
          WurstplusMessageUtil.send_client_error_message("ayo wtf, why are you on singleplayer");
          this.set_disable();
          return;
      }
      if (silent.get_value(true)) mc.timer.tickLength = 1f;

      mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.41999998688698D, mc.player.posZ, true));
      mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.7531999805211997D, mc.player.posZ, true));
      mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 1.00133597911214D, mc.player.posZ, true));
      mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 1.16610926093821D, mc.player.posZ, true));
      mc.player.setPosition(mc.player.posX, mc.player.posY + 1.16610926093821D, mc.player.posZ);

      if (enderchest.get_value(true) && getHotbarSlot(Blocks.ENDER_CHEST) != -1) {
          mc.player.inventory.currentItem = getHotbarSlot(Blocks.ENDER_CHEST);
      } else if (getHotbarSlot(Blocks.OBSIDIAN) != -1) {
          mc.player.inventory.currentItem = getHotbarSlot(Blocks.OBSIDIAN);
      } else {
          mc.player.inventory.currentItem = getHotbarSlot(Blocks.ENDER_CHEST);
      }

      WurstplusBurrowUtil.placeBlock(originalPos, rotate.get_value(true),  true, false, true, false);
      mc.player.inventory.currentItem = oldSlot;
      mc.player.setPosition(mc.player.posX, mc.player.posY - 1.16610926093821D, mc.player.posZ);

      mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + height.get_value(1), mc.player.posZ, true));

      mc.timer.tickLength = oldTickLength;
      this.set_disable();
    }


    private boolean intersectsWithEntity(final BlockPos pos) {
        for (final Entity entity : mc.world.loadedEntityList) {
            if (entity.equals(mc.player)) continue;
            if (entity instanceof EntityItem) continue;
            if (new AxisAlignedBB(pos).intersects(entity.getEntityBoundingBox())) return true;
        }
        return false;
    }


    public static int getHotbarSlot(final Block block) {
        for (int i = 0; i < 9; i++) {
            final Item item = mc.player.inventory.getStackInSlot(i).getItem();
            if (item instanceof ItemBlock && ((ItemBlock) item).getBlock().equals(block)) return i;
        }
        return -1;
    }

    public static boolean getEnderChest() {
        try {
        }
        catch (Exception e) {
            return false;
        }
        return false;
    }
}
