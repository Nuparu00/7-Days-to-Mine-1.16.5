package nuparu.sevendaystomine.world.horde;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.Constants;
import nuparu.sevendaystomine.config.CommonConfig;
import nuparu.sevendaystomine.entity.ZombieBaseEntity;
import nuparu.sevendaystomine.init.ModGameRules;
import nuparu.sevendaystomine.util.Utils;
import org.apache.logging.log4j.core.jmx.Server;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public abstract class Horde {

    public List<ZombieBaseEntity> zombies = new ArrayList<ZombieBaseEntity>();
    public List<HordeEntry> entries = new ArrayList<HordeEntry>();

    public BlockPos center;
    public ServerWorld world;
    public UUID uuid;
    public long startTime = 0;
    public int waves;
    public int zombiesInWave = 0;
    public int waveTimer;
    public UUID playerID;
    public HordeSavedData data;

    public Horde(ServerWorld world) {
        this.world = world;
        startTime = Utils.getDay(world);
    }

    public Horde(BlockPos center, ServerWorld world) {
        uuid = UUID.randomUUID();
        this.center = center;
        this.world = world;
        startTime = Utils.getDay(world);
        HordeSavedData.getOrCreate(world).addHorde(this);
    }

    public void onZombieKill(ZombieBaseEntity zombie) {
        zombies.remove(zombie);
        // HordeSavedData.get(world).addHorde(this);
    }

    public void start() {
        --waves;
        waveTimer = CommonConfig.hordeWaveDelay.get();
    }

    public void update() {
        boolean glow = world.getGameRules().getInt(ModGameRules.hordeGlow) > zombies.size();
        for (ZombieBaseEntity zombie : new ArrayList<ZombieBaseEntity>(zombies)) {
            if (!zombie.isAlive()) {
                zombies.remove(zombie);
                continue;
            }
            if (glow) {
                zombie.addEffect(new EffectInstance(Effects.GLOWING, 10000));
            }
        }

        if (startTime != Utils.getDay(world) && world != null) {
            HordeSavedData data = HordeSavedData.getOrCreate(world);
            if (data != null) {
                data.removeHorde(this);
                return;
            }
        } else if (zombies.size() == 0) {
            if (waves > 0) {
                if (--waveTimer <= 0) {
                    start();
                }
            } else if (playerID != null) {
                HordeSavedData data = HordeSavedData.getOrCreate(world);
                PlayerEntity player = Utils.getPlayerFromUUID(playerID);
                if (player != null) {
                    player.giveExperiencePoints(30);
                }
                if (data != null) {
                    data.removeHorde(this);
                    return;
                }
            }
        }
    }

    public HordeEntry getHordeEntry(Random rand) {
        int total = 0;
        for (HordeEntry entry : entries) {
            total += entry.weight;
        }
        int i = rand.nextInt(total);
        for (HordeEntry entry : entries) {
            i -= entry.weight;
            if (i <= 0) {
                return entry;
            }
        }
        return null;
    }

    public void addZombie(ZombieBaseEntity zombie) {
        zombies.add(zombie);
    }

    public void onPlayerStartTacking(ServerPlayerEntity player, ZombieBaseEntity zombie) {

    }

    public void onPlayerStopTacking(ServerPlayerEntity player, ZombieBaseEntity zombie) {

    }

    public void onRemove() {
        for (ZombieBaseEntity zombie : zombies) {
            zombie.horde = null;
        }

    }

    public void readFromNBT(CompoundNBT compound) {
        if (world.isClientSide()) {
            return;
        }
        waves = compound.getInt("wave");
        zombiesInWave = compound.getInt("zombiesInWave");
        uuid = NBTUtil.loadUUID(compound.get("uuid"));
        center = BlockPos.of(compound.getLong("center"));
        startTime = compound.getLong("startTime");
        waveTimer = compound.getInt("waveTimer");
        if(compound.contains("playerid", Constants.NBT.TAG_COMPOUND)) {
            playerID = NBTUtil.loadUUID(compound.get("playerid"));
        }
        zombies.clear();
    }

    public CompoundNBT writeToNBT(CompoundNBT compound) {
        compound.putInt("wave", waves);
        compound.putInt("zombiesInWave", zombiesInWave);
        compound.put("uuid", NBTUtil.createUUID(uuid));
        compound.putLong("center", center.asLong());
        compound.putLong("startTime", startTime);
        compound.putInt("waveTimer", waveTimer);
        if (playerID != null) {
            compound.put("playerid", NBTUtil.createUUID(playerID));
        }
        return compound;
    }

    public BlockPos getCenter() {
        PlayerEntity player = getPlayer();
        return player != null ? player.blockPosition() : this.center;
    }

    public PlayerEntity getPlayer() {
        return this.playerID != null ? Utils.getPlayerFromUUID(playerID) : null;
    }
}
