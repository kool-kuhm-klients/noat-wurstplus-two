package com.gamesense.client.module.modules.movement;

import me.travis.wurstplus.event.events.*;
import me.travis.wurstplus.wurstplustwo.util.*;
import com.gamesense.client.module.Category;
import com.gamesense.client.module.Module;
import com.mojang.realmsclient.gui.ChatFormatting;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.block.BlockLiquid;
import net.minecraft.init.MobEffects;
import me.travis.wurstplus.wurstplustwo.guiscreen.settings.WurstplusSetting;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusCategory;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusHack;

import java.util.Arrays;

/**
 * @author Crystallinqq/Auto for original code
 * @source https://github.com/Crystallinqq/Mercury-Client/blob/master/src/main/java/fail/mercury/client/client/modules/movement/Speed.java
 * @reworked by Hoosiers on 11/1/2020
 * i pasted this from gaemsenses
 */

public class WurstplusSpeed extends WurstplusHack {

    public WurstplusSpeed() {
  		super(WurstplusCategory.WURSTPLUS_MOVEMENT);

  		this.name        = "Speed";
  		this.tag         = "Speed";
  		this.description = "its like running, but faster";
  	}

    WurstplusSetting speed_mode = create("Mode", "SpeedMode", "Strafe", combobox("Strafe", "YPort"));
    WurstplusSetting jump_height = create("Jump Height", "SpeedJumpHeight", 4f, 1f, 6f);
    WurstplusSetting hit_range_wall = create("Range Wall", "CaRangeWall", 4f, 1f, 6f);

    private boolean slowDown;
    private double playerSpeed;
    private WurstplusTimer timer = new WurstplusTimer();

    public void onEnable() {
        playerSpeed = MotionUtil.getBaseMoveSpeed();
    }

    public void onDisable() {
        timer.reset();
        EntityUtil.resetTimer();
    }

    public void onUpdate() {
        if (mc.player == null || mc.world == null) {
            disable();
            return;
        }

        if (mode.get_value().equalsIgnoreCase("YPort")) {
            handleYPortSpeed();
        }
    }

    private void handleYPortSpeed() {
        if (!MotionUtil.isMoving(mc.player) || mc.player.isInWater() && mc.player.isInLava() || mc.player.collidedHorizontally) {
            return;
        }

        if (mc.player.onGround) {
            EntityUtil.setTimer(1.15f);
            mc.player.jump();
            MotionUtil.setSpeed(mc.player, MotionUtil.getBaseMoveSpeed() + yPortSpeed.get_value(1));
        } else {
            mc.player.motionY = -1;
            EntityUtil.resetTimer();
        }
    }

    @SuppressWarnings("unused")
    @EventHandler
    private final Listener<PlayerMoveEvent> playerMoveEventListener = new Listener<>(event -> {
        if (mc.player.isInLava() || mc.player.isInWater() || mc.player.isOnLadder() || mc.player.isInWeb) {
            return;
        }

        if (mode.get_value().equalsIgnoreCase("Strafe")) {
            double speedY = jumpHeight.get_value(1);

            if (mc.player.onGround && MotionUtil.isMoving(mc.player) && timer.passed(300)) {
                EntityUtil.setTimer(timerVal.get_value(1).floatValue());
                if (mc.player.isPotionActive(MobEffects.JUMP_BOOST)) {
                    speedY += (mc.player.getActivePotionEffect(MobEffects.JUMP_BOOST).getAmplifier() + 1) * 0.1f;
                }

                event.setY(mc.player.motionY = speedY);
                playerSpeed = MotionUtil.getBaseMoveSpeed() * (EntityUtil.isColliding(0, -0.5, 0) instanceof BlockLiquid && !EntityUtil.isInLiquid() ? 0.9 : 1.901);
                slowDown = true;
                timer.reset();
            } else {
                EntityUtil.resetTimer();
                if (slowDown || mc.player.collidedHorizontally) {
                    playerSpeed -= (EntityUtil.isColliding(0, -0.8, 0) instanceof BlockLiquid && !EntityUtil.isInLiquid()) ? 0.4 : 0.7 * (playerSpeed = MotionUtil.getBaseMoveSpeed());
                    slowDown = false;
                } else {
                    playerSpeed -= playerSpeed / 159.0;
                }
            }
            playerSpeed = Math.max(playerSpeed, MotionUtil.getBaseMoveSpeed());
            double[] dir = MotionUtil.forward(playerSpeed);
            event.setX(dir[0]);
            event.setZ(dir[1]);
        }
    });
}
